CREATE DATABASE IF NOT EXISTS portx1;

USE portx1;
CREATE TABLE IF NOT EXISTS payments (
	id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  uuid VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,

  amount DECIMAL(8, 2),
  currency VARCHAR(3),
  originator_name VARCHAR(100),
  originator_id VARCHAR(100),
  beneficiary_name VARCHAR(100),
  beneficiary_id VARCHAR(100),
  sender_account_type VARCHAR(3),
  sender_account_number VARCHAR(50),
  receiver_account_type VARCHAR(3),
  receiver_account_number VARCHAR(50),
  payment_status VARCHAR(10)
);

#USE portx1;
#CREATE INDEX idx_uuid
#ON payments (uuid);