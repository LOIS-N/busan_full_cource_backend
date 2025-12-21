CREATE TABLE password_reset_token (
      id INT AUTO_INCREMENT PRIMARY KEY,
      user_id VARCHAR(50) NOT NULL,
      token VARCHAR(100) NOT NULL UNIQUE,
      expiry_date DATETIME NOT NULL,
      created_date DATETIME NOT NULL,
      used BOOLEAN DEFAULT FALSE,
      FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- 인덱스 추가 (성능 향상)
CREATE INDEX idx_token ON password_reset_token(token);
CREATE INDEX idx_user_id ON password_reset_token(user_id);
CREATE INDEX idx_expiry_date ON password_reset_token(expiry_date);