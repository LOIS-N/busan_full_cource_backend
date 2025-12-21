-- Users 테이블 (먼저 생성 - 다른 테이블들이 참조함)
CREATE TABLE `users` (
	`id` INT NOT NULL AUTO_INCREMENT COMMENT '사용자 고유 ID',
	`user_id` VARCHAR(20) NOT NULL UNIQUE COMMENT '로그인 ID',
	`password` VARCHAR(255) NOT NULL COMMENT '암호화된 비밀번호',
	`email` VARCHAR(255) NOT NULL UNIQUE COMMENT '이메일',
	`nickname` VARCHAR(20) NOT NULL COMMENT '닉네임',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 태그 테이블들
CREATE TABLE `restaurant_tags` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`type` VARCHAR(20) NOT NULL COMMENT '태그 타입',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `place_tags` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`type` VARCHAR(20) NOT NULL COMMENT '태그 타입',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Places 테이블
CREATE TABLE `places` (
	`id` INT NOT NULL AUTO_INCREMENT COMMENT '장소 고유 ID',
	`tag` INT NOT NULL COMMENT '태그 ID',
	`x` DOUBLE NOT NULL COMMENT '경도',
	`y` DOUBLE NOT NULL COMMENT '위도',
	`name` VARCHAR(100) NOT NULL COMMENT '장소명',
	`address` VARCHAR(255) NOT NULL COMMENT '주소',
	`image_url` VARCHAR(255) NULL COMMENT '이미지 URL',
	`thumbnail_url` VARCHAR(255) NULL COMMENT '썸네일 URL',
	`average_rating` DECIMAL(2, 1) NOT NULL DEFAULT 0.0 COMMENT '평균 평점 (5점 만점)',
	PRIMARY KEY (`id`),
	FOREIGN KEY (`tag`) REFERENCES `place_tags`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Restaurants 테이블
CREATE TABLE `restaurants` (
	`id` INT NOT NULL AUTO_INCREMENT COMMENT '식당 고유 ID',
	`tag` INT NOT NULL COMMENT '태그 ID',
	`x` DOUBLE NOT NULL COMMENT '경도',
	`y` DOUBLE NOT NULL COMMENT '위도',
	`name` VARCHAR(100) NOT NULL COMMENT '식당명',
	`address` VARCHAR(255) NOT NULL COMMENT '주소',
	`tel` VARCHAR(20) NOT NULL COMMENT '전화번호',
	`opening_hours` VARCHAR(100) NOT NULL COMMENT '영업시간',
	`image_url` VARCHAR(255) NULL COMMENT '이미지 URL',
	`thumbnail_url` VARCHAR(255) NULL COMMENT '썸네일 URL',
	`average_rating` DECIMAL(2, 1) NOT NULL DEFAULT 0.0 COMMENT '평균 평점 (5점 만점)',
	PRIMARY KEY (`id`),
	FOREIGN KEY (`tag`) REFERENCES `restaurant_tags`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Reviews 테이블
CREATE TABLE `place_reviews` (
	`id` INT NOT NULL AUTO_INCREMENT COMMENT '리뷰 고유 ID',
	`user_id` INT NOT NULL COMMENT '작성자 ID',
	`rating` DECIMAL(2, 1) NOT NULL COMMENT '평점 (5점 만점)',
	`content` TEXT NULL COMMENT '리뷰 내용',
	`created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '작성일시',
	`target_type` VARCHAR(20) NOT NULL COMMENT '리뷰 대상 타입 (place/restaurant)',
	`target_id` INT NOT NULL COMMENT '리뷰 대상 ID',
	PRIMARY KEY (`id`),
	FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Review Pictures 테이블
CREATE TABLE `review_pictures` (
	`id` INT NOT NULL AUTO_INCREMENT COMMENT '사진 고유 ID',
	`review_id` INT NOT NULL COMMENT '리뷰 ID',
	`picture_path` VARCHAR(255) NOT NULL COMMENT '사진 경로',
	PRIMARY KEY (`id`),
	FOREIGN KEY (`review_id`) REFERENCES `place_reviews`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Travel Routes 테이블
CREATE TABLE `travel_routes` (
	`id` INT NOT NULL AUTO_INCREMENT COMMENT '여행 경로 고유 ID',
	`user_id` INT NOT NULL COMMENT '사용자 ID',
	`route` JSON NOT NULL COMMENT '경로 데이터',
	`created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
	PRIMARY KEY (`id`),
	FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Place Log 테이블
CREATE TABLE `place_log` (
	`id` INT NOT NULL AUTO_INCREMENT COMMENT '로그 고유 ID',
	`user_id` INT NOT NULL COMMENT '사용자 ID (오타 수정: uer_id -> user_id)',
	`log` JSON NULL COMMENT '로그 데이터',
	`created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
	PRIMARY KEY (`id`),
	FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Restaurant Log 테이블
CREATE TABLE `restaurant_log` (
	`id` INT NOT NULL AUTO_INCREMENT COMMENT '로그 고유 ID',
	`user_id` INT NOT NULL COMMENT '사용자 ID',
	`log` JSON NULL COMMENT '로그 데이터',
	`created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
	PRIMARY KEY (`id`),
	FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 인덱스 추가 (성능 최적화)
CREATE INDEX idx_users_user_id ON users(user_id);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_places_tag ON places(tag);
CREATE INDEX idx_restaurants_tag ON restaurants(tag);
CREATE INDEX idx_reviews_user_id ON place_reviews(user_id);
CREATE INDEX idx_reviews_target ON place_reviews(target_type, target_id);
CREATE INDEX idx_travel_routes_user_id ON travel_routes(user_id);
