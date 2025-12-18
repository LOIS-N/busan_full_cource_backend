package com.ssafy.gt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.gt.dto.ai.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class GeminiService {

    @Value("${ai.api.url}")
    private String apiUrl;

    @Value("${ai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GeminiService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * AI를 이용한 장소 추천
     */
    public List<Integer> recommendPlaces(List<SimplifiedSearchHistory> userHistory, List<SimplifiedPlace> candidates) {
        try {
            // 1. 입력 데이터 구성
            PlaceRecommendationInput input = PlaceRecommendationInput.builder()
                    .userHistory(userHistory)
                    .candidates(candidates)
                    .build();

            // 2. 프롬프트 구성
            String prompt = buildPrompt(input);

            // 3. Gemini API 호출
            String responseText = callGeminiApi(prompt);

            // 4. 응답 파싱
            return parseRecommendationResponse(responseText);

        } catch (Exception e) {
            log.error("AI 추천 중 오류 발생", e);
            // 오류 발생 시 후보 장소를 그대로 반환 (순서 유지)
            return candidates.stream()
                    .map(SimplifiedPlace::getId)
                    .toList();
        }
    }

    /**
     * AI에게 보낼 프롬프트 구성
     */
    private String buildPrompt(PlaceRecommendationInput input) throws JsonProcessingException {
        String inputJson = objectMapper.writeValueAsString(input);

        return """
                당신은 사용자의 취향을 분석하여 장소를 추천해주는 AI 큐레이터입니다.

                사용자의 '최근 검색 기록'과 현재 위치 주변의 '후보 장소 리스트'가 주어집니다.
                검색 기록의 검색어(searchKeyword)와 카테고리(tagType)를 분석하여 사용자가 좋아할 만한 장소 순서대로 후보 장소를 정렬하세요.

                **중요 지침:**
                1. 검색 기록에서 자주 나타나는 카테고리(tagType)를 우선적으로 고려하세요.
                2. 검색 기록의 검색어와 후보 장소의 이름(name)이 유사하거나 관련이 있는 경우 우선순위를 높이세요.
                3. 검색 기록이 없는 경우, 후보 장소를 그대로 순서대로 반환하세요.
                4. 결과는 반드시 아래 JSON 형식으로만 반환해야 하며, 추가 설명이나 주석을 포함하지 마세요.
                5. recommended_ids는 후보 장소의 id만을 포함하는 배열이어야 합니다.
                6. 모든 후보 장소의 id가 결과에 포함되어야 하며, 중복되거나 누락되어서는 안 됩니다.

                **입력 데이터:**
                %s

                **출력 형식 (JSON만):**
                {
                  "recommended_ids": [93, 102, 45, ...]
                }

                **다시 한번 강조: JSON 형식만 반환하고, 그 외 어떤 텍스트도 포함하지 마세요.**
                """.formatted(inputJson);
    }

    /**
     * Gemini API 호출
     */
    private String callGeminiApi(String prompt) {
        // Gemini API 요청 구성
        GeminiRequest request = GeminiRequest.builder()
                .contents(Collections.singletonList(
                        GeminiRequest.Content.builder()
                                .parts(Collections.singletonList(
                                        GeminiRequest.Part.builder()
                                                .text(prompt)
                                                .build()
                                ))
                                .build()
                ))
                .generationConfig(GeminiRequest.GenerationConfig.builder()
                        .temperature(0.7)
                        .maxOutputTokens(2048)
                        .build()
                )
                .build();

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey);

        // HTTP 요청
        HttpEntity<GeminiRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<GeminiResponse> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                GeminiResponse.class
        );

        // 응답에서 텍스트 추출
        if (response.getBody() != null &&
                response.getBody().getCandidates() != null &&
                !response.getBody().getCandidates().isEmpty()) {

            GeminiResponse.Candidate candidate = response.getBody().getCandidates().get(0);
            if (candidate.getContent() != null &&
                    candidate.getContent().getParts() != null &&
                    !candidate.getContent().getParts().isEmpty()) {

                return candidate.getContent().getParts().get(0).getText();
            }
        }

        throw new RuntimeException("Gemini API 응답이 비어있습니다.");
    }

    /**
     * AI 응답 파싱 (JSON에서 recommended_ids 추출)
     */
    private List<Integer> parseRecommendationResponse(String responseText) throws JsonProcessingException {
        log.info("AI 응답: {}", responseText);

        // JSON 블록 추출 (```json ... ``` 형태로 감싸진 경우 처리)
        String cleanedResponse = responseText.trim();
        if (cleanedResponse.startsWith("```json")) {
            cleanedResponse = cleanedResponse
                    .replaceFirst("```json\\s*", "")
                    .replaceFirst("\\s*```$", "")
                    .trim();
        } else if (cleanedResponse.startsWith("```")) {
            cleanedResponse = cleanedResponse
                    .replaceFirst("```\\s*", "")
                    .replaceFirst("\\s*```$", "")
                    .trim();
        }

        // JSON 파싱
        PlaceRecommendationOutput output = objectMapper.readValue(cleanedResponse, PlaceRecommendationOutput.class);

        if (output.getRecommendedIds() == null || output.getRecommendedIds().isEmpty()) {
            throw new RuntimeException("AI가 추천 결과를 반환하지 않았습니다.");
        }

        return output.getRecommendedIds();
    }
}