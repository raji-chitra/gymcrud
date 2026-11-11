-- =======================================================
-- Database: gym_management
-- Gym Membership Management System
-- Compatible with MySQL 8.0+
-- =======================================================

CREATE DATABASE IF NOT EXISTS gym_management;
USE gym_management;

-- -------------------------------------------------------
-- 1. Table: membership_plans
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS membership_plans (
    plan_id INT AUTO_INCREMENT PRIMARY KEY,
    plan_name VARCHAR(50) NOT NULL,
    duration_months INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);

-- -------------------------------------------------------
-- 2. Table: members
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS members (
    member_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(10) NOT NULL,
    phone VARCHAR(15) NOT NULL UNIQUE,
    email VARCHAR(100) UNIQUE,
    join_date DATE NOT NULL,
    plan_id INT DEFAULT NULL,
    membership_start_date DATE DEFAULT NULL,
    membership_expiry_date DATE DEFAULT NULL,
    CONSTRAINT fk_member_plan FOREIGN KEY (plan_id) 
        REFERENCES membership_plans(plan_id) 
        ON DELETE SET NULL
);

-- -------------------------------------------------------
-- 3. Table: payments
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_date DATE NOT NULL,
    payment_status VARCHAR(20) NOT NULL DEFAULT 'Paid',
    CONSTRAINT fk_payment_member FOREIGN KEY (member_id) 
        REFERENCES members(member_id) 
        ON DELETE CASCADE
);

-- -------------------------------------------------------
-- Insert Sample Membership Plans
-- -------------------------------------------------------
INSERT INTO membership_plans (plan_name, duration_months, price) VALUES
('Monthly Plan', 1, 1500.00),
('Quarterly Plan', 3, 4000.00),
('Half-Yearly Plan', 6, 7500.00),
('Yearly Plan', 12, 13500.00)
ON DUPLICATE KEY UPDATE plan_name = plan_name;

-- -------------------------------------------------------
-- Insert Sample Member
-- -------------------------------------------------------
INSERT INTO members (name, age, gender, phone, email, join_date, plan_id, membership_start_date, membership_expiry_date) VALUES
('Rahul Sharma', 24, 'Male', '9876543210', 'rahul.sharma@example.com', '2026-01-15', 2, '2026-01-15', '2026-04-15'),
('Priya Patel', 22, 'Female', '9812345678', 'priya.patel@example.com', '2026-02-01', 1, '2026-02-01', '2026-03-01'),
('Amit Kumar', 28, 'Male', '9898989898', 'amit.kumar@example.com', '2026-02-10', NULL, NULL, NULL);

-- -------------------------------------------------------
-- Insert Sample Payments
-- -------------------------------------------------------
INSERT INTO payments (member_id, amount, payment_date, payment_status) VALUES
(1, 4000.00, '2026-01-15', 'Paid'),
(2, 1500.00, '2026-02-01', 'Paid');
