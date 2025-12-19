import json
import pymysql

# RDS 연결 정보
connection = pymysql.connect(
    host='localhost', 
    port=3306,
    user='ssafy',
    password='ssafy',
    database='gt',
    charset='utf8mb4'
)

try:
    with connection.cursor() as cursor:
        # # JSON 파일 로드
        with open('tour_data_place.json', 'r', encoding='utf-8') as f:
            data = json.load(f)
        
        # 배치 삽입
        insert_query = """
        INSERT INTO places (
            id, tag, x, y, name, address, tel, opening_hours, image_url, thumbnail_url
        ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        """

        for item in data:
            cursor.execute(insert_query, (
                # 1. ID: 데이터가 없으면 None(NULL) 처리
                int(item.get('UC_SEQ')) if item.get('UC_SEQ') is not None else None,
                
                # 2. TAG: FK ID
                item.get('tag'),
                
                # 3. LNG/LAT: float 변환 시 None 체크 필수
                float(item.get('x')) if item.get('x') is not None else None,
                float(item.get('y')) if item.get('y') is not None else None,
                
                # 4. 문자열 필드: .get()은 키가 없으면 기본적으로 None을 반환
                item.get('name'),
                item.get('address'),
                item.get('tel') if item.get('tel') else None,
                
                # 5. 영업시간: 데이터가 있을 때만 100자 슬라이싱
                item.get('opening_hours')[:100] if item.get('opening_hours') else None,
                
                # 6. 이미지 URL
                item.get('image_url'),
                item.get('thumbnail_url')
            ))

        connection.commit()
        print(f"✅ {len(data)}개 데이터 삽입 완료!")

except Exception as e:
    connection.rollback()
    print(f"❌ 오류 발생: {e}")

finally:
    connection.close()