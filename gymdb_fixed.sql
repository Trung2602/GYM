
-- Tạo CSDL kế thừa theo kiểu JOINED
DROP DATABASE IF EXISTS gymdb;
CREATE DATABASE gymdb;
USE gymdb;

-- Cơ sở (Facility)
CREATE TABLE Facility (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) UNIQUE NOT NULL,
    address VARCHAR(255) NOT NULL
);

INSERT INTO Facility (name, address) VALUES
('Cơ sở XH', '97 Võ Văn Tần, P. Xuân Hòa, TP. Hồ Chí Minh'),
('Cơ sở COL', '35-37 Hồ Hảo Hớn, P. Cầu Ông Lãnh, TP. Hồ Chí Minh'),
('Cơ sở HP', 'Khu dân cư Nhơn Đức, xã Hiệp Phước, TP. Hồ Chí Minh'),
('Cơ sở TD', '02 Mai Thị Lựu, P. Tân Định, TP. Hồ Chí Minh');

-- Bảng cha: Account
CREATE TABLE Account (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    birthday DATE NOT NULL,
    gender TINYINT NOT NULL,
    role ENUM('Admin', 'Staff', 'Customer') NOT NULL,
    mail VARCHAR(100) UNIQUE NOT NULL,
    avatar VARCHAR(255),
    is_active BOOLEAN NOT NULL
);

INSERT INTO Account (id, username, password, name, birthday, gender, role, mail, avatar, is_active) VALUES
(1, 'Admin123', '$2a$10$Okh2cGnAQ1jAGnUnxhEqluSm.FhhJn6JMMe7hOdGZjz2iaUjcdOMG', 'Admin', '1990-01-01', 0, 'Admin', 'admin@gmail.com', 'https://res.cloudinary.com/dxgc9wwrd/image/upload/v1754928114/nzoi1xjxasxfvsut1azv.jpg', TRUE),
(2, 'Trung2602', '$2a$10$Okh2cGnAQ1jAGnUnxhEqluSm.FhhJn6JMMe7hOdGZjz2iaUjcdOMG', 'Lư Hiếu Trung', '1995-06-15', 1, 'Staff', 'luduahau@gmail.com', 'https://res.cloudinary.com/dxgc9wwrd/image/upload/v1754928114/nzoi1xjxasxfvsut1azv.jpg', TRUE),
(3, 'ThanhTu01', '$2a$10$Okh2cGnAQ1jAGnUnxhEqluSm.FhhJn6JMMe7hOdGZjz2iaUjcdOMG', 'Đinh Thanh Tú', '1998-03-22', 0, 'Staff', 'tuthanh@gmail.com', 'https://res.cloudinary.com/dxgc9wwrd/image/upload/v1754928114/nzoi1xjxasxfvsut1azv.jpg', TRUE),
(4, 'Chuong1305', '$2a$10$Okh2cGnAQ1jAGnUnxhEqluSm.FhhJn6JMMe7hOdGZjz2iaUjcdOMG', 'Trương Nguyên Chương', '2000-08-10', 1, 'Staff', 'chuong1305@gmail.com', 'https://res.cloudinary.com/dxgc9wwrd/image/upload/v1754928114/nzoi1xjxasxfvsut1azv.jpg', TRUE),
(5, 'Duc123', '$2a$10$Okh2cGnAQ1jAGnUnxhEqluSm.FhhJn6JMMe7hOdGZjz2iaUjcdOMG', 'Nguyễn Văn Đức', '1992-12-05', 0, 'Customer', 'ngvanduc@gmail.com', 'https://res.cloudinary.com/dxgc9wwrd/image/upload/v1754928114/nzoi1xjxasxfvsut1azv.jpg', TRUE),
(6, 'Tuan123', '$2a$10$Okh2cGnAQ1jAGnUnxhEqluSm.FhhJn6JMMe7hOdGZjz2iaUjcdOMG', 'Kiều Minh Tuấn', '1992-12-05', 1, 'Customer', 'tuankieu@gmail.com', 'https://res.cloudinary.com/dxgc9wwrd/image/upload/v1754928114/nzoi1xjxasxfvsut1azv.jpg', TRUE);

-- Admin kế thừa từ Account
CREATE TABLE Admin (
    id INT PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES Account(id)
);
INSERT INTO Admin (id) VALUES (1);

-- Loại nhân viên
CREATE TABLE Staff_type (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50),
    salary INT NOT NULL,
    day_off INT NOT NULL,
    description TEXT
);
INSERT INTO Staff_type (name, salary, day_off, description) VALUES
('Fulltime', 15000000, 2, 'Nhân viên toàn thời gian, lương tính theo tháng.'),
('PartTime', 30000, 0, 'Nhân viên bán thời gian, lương tính theo giờ.'),
('Intern', 19000, 0, 'Thực tập sinh, lương tính theo giờ.');

-- Staff kế thừa Account
CREATE TABLE Staff (
    id INT PRIMARY KEY,
    created_date DATE NOT NULL,
    staff_type_id INT NOT NULL,
    facility_id INT NOT NULL,
    FOREIGN KEY (id) REFERENCES Account(id),
    FOREIGN KEY (staff_type_id) REFERENCES Staff_type(id),
    FOREIGN KEY (facility_id) REFERENCES Facility(id)
);
INSERT INTO Staff (id, created_date, staff_type_id, facility_id) VALUES
(2, '2022-05-10', 1, 1),
(3, '2024-02-26', 2, 3),
(4, '2024-11-11', 3, 4);


-- Ngày nghỉ phép
CREATE TABLE Staff_day_off (
    id INT PRIMARY KEY AUTO_INCREMENT,
    date_off DATE NOT NULL,
    staff_id INT NOT NULL,
    FOREIGN KEY (staff_id) REFERENCES Staff(id) on delete cascade
);

INSERT INTO Staff_day_off (date_off, staff_id) VALUES
('2025-06-15', 2),
('2025-06-22', 2),
('2025-06-29', 2);

-- Ca làm việc
CREATE TABLE Shift (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50),
    checkin TIME NOT NULL,
    checkout TIME NOT NULL,
    duration DOUBLE
);

INSERT INTO Shift (name, checkin, checkout, duration) VALUES
('Làm hành chính', '05:00:00', '21:00:00', 16),
('Ca sáng', '05:00:00', '13:00:00', 8),
('Ca chiều', '13:00:00', '21:00:00', 8),
('Ca tối', '21:00:00', '05:00:00', 8),
('Ca gãy sáng', '05:00:00', '09:00:00', 4),
('Ca gãy trưa', '09:00:00', '13:00:00', 4),
('Ca gãy chiều', '13:00:00', '17:00:00', 4),
('Ca gãy tối', '17:00:00', '21:00:00', 4);

-- Lịch làm việc parttime/intern
CREATE TABLE Staff_schedule (
    id INT PRIMARY KEY AUTO_INCREMENT,
    date DATE NOT NULL,
    shift_id INT NOT NULL,
    staff_id INT NOT NULL,
    FOREIGN KEY (shift_id) REFERENCES Shift(id),
    FOREIGN KEY (staff_id) REFERENCES Staff(id) on delete cascade
);

INSERT INTO Staff_schedule (date, shift_id, staff_id) VALUES
('2025-08-01', 4, 3),
('2025-08-01', 7, 4),
('2025-08-02', 3, 3),
('2025-08-03', 1, 3),
('2025-08-04', 2, 4),
('2025-08-05', 5, 3),
('2025-08-05', 6, 4),
('2025-08-06', 2, 3),
('2025-08-07', 3, 4),
('2025-08-08', 5, 3),
('2025-08-09', 8, 4),
('2025-08-10', 6, 3),
('2025-08-11', 3, 3),
('2025-08-11', 7, 4),
('2025-08-12', 1, 4),
('2025-08-13', 6, 3),
('2025-08-14', 8, 4),
('2025-08-15', 7, 3),
('2025-08-16', 5, 4),
('2025-08-17', 2, 3),
('2025-08-18', 4, 3),
('2025-08-18', 6, 4),
('2025-08-20', 7, 3),
('2025-08-20', 8, 4),
('2025-08-21', 5, 4),
('2025-08-22', 1, 3),
('2025-08-23', 2, 4),
('2025-08-24', 4, 3),
('2025-08-25', 6, 4),
('2025-08-26', 8, 3),
('2025-08-27', 3, 4),
('2025-08-28', 6, 3),
('2025-08-29', 7, 4),
('2025-08-30', 5, 3),
('2025-08-31', 2, 4);


-- Bảng lương
CREATE TABLE Salary (
    id INT PRIMARY KEY AUTO_INCREMENT,
    date DATE NOT NULL,
    duration DOUBLE,
    day_off INT,
    price DOUBLE,
    staff_id INT NOT NULL,
    FOREIGN KEY (staff_id) REFERENCES Staff(id) on delete cascade
);

INSERT INTO Salary (date, duration, day_off, price, staff_id) VALUES
('2025-06-10', 160, 2, 15500000, 2),
('2025-07-10', 80, 0, 4800000, 3),
('2025-07-10', 80, 0, 988000, 4),
('2025-07-10', 80, 0, 14000000, 2);


-- Customer kế thừa Account
CREATE TABLE Customer (
    id INT PRIMARY KEY,
    expiry_date DATE NOT NULL,
    FOREIGN KEY (id) REFERENCES Account(id)
);
INSERT INTO Customer (id, expiry_date) VALUES
(5, '2026-06-10'),
(6, '2025-08-02');


-- Gói tập
CREATE TABLE Plan (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    price INT NOT NULL,
    duration_days INT NOT NULL,
    description TEXT
);

INSERT INTO Plan (name, price, duration_days, description) VALUES
('Gói sinh viên', 269000, 30, 'Điều kiện và học sinh và sinh viên dưới 22 tuổi'),
('Gói tháng', 299000, 30, 'Gói tập 1 tháng, không giới hạn lượt tập'),
('Gói 3 tháng', 829000, 91, 'Gói tập 3 tháng, tặng 1 lần xông hơi'),
('Gói 6 tháng', 1499000, 186, 'Gói tập 3 tháng, tặng 10 lần xông hơi'),
('Gói năm', 2399000, 365, 'Gói tập 1 năm, tặng 30 lần xông hơi');

-- Thanh toán gói tập
CREATE TABLE Pay_customer (
    id INT PRIMARY KEY AUTO_INCREMENT,
    date DATE NOT NULL,
    plan_id INT NOT NULL,
    customer_id INT NOT NULL,
    FOREIGN KEY (plan_id) REFERENCES Plan(id),
    FOREIGN KEY (customer_id) REFERENCES Customer(id) on delete cascade
);

INSERT INTO Pay_customer (date, plan_id, customer_id) VALUES
('2025-05-10', 1, 5),
('2025-06-02', 2, 6),
('2025-06-10', 5, 5),
('2025-07-02', 2, 6);

-- Lịch tập khách hàng
CREATE TABLE Customer_schedule (
    id INT PRIMARY KEY AUTO_INCREMENT,
    date DATE NOT NULL,
    checkin TIME NOT NULL,
    checkout TIME NOT NULL,
    facility_id INT NOT NULL,
    staff_id INT NOT NULL,
    customer_id INT NOT NULL,
    FOREIGN KEY (facility_id) REFERENCES Facility(id),
    FOREIGN KEY (staff_id) REFERENCES Staff(id) on delete cascade,
    FOREIGN KEY (customer_id) REFERENCES Customer(id) on delete cascade
);

INSERT INTO Customer_schedule (date, checkin, checkout, facility_id, staff_id, customer_id) VALUES
('2025-08-13', '08:00:00', '09:30:00', 1, 2, 5),
('2025-08-14', '10:00:00', '11:30:00', 2, 2, 6),
('2025-08-15', '07:30:00', '09:00:00', 3, 3, 5),
('2025-08-16', '09:00:00', '10:30:00', 4, 4, 6),
('2025-08-17', '15:00:00', '16:30:00', 1, 2, 6),
('2025-08-18', '17:00:00', '18:30:00', 2, 3, 5),
('2025-08-19', '08:30:00', '10:00:00', 3, 4, 6),
('2025-08-20', '14:00:00', '15:30:00', 4, 2, 5),
('2025-08-21', '06:00:00', '07:30:00', 1, 3, 6),
('2025-08-22', '19:00:00', '20:30:00', 2, 4, 5),
('2025-08-23', '09:30:00', '11:00:00', 3, 2, 6),
('2025-08-24', '13:00:00', '14:30:00', 4, 3, 5),
('2025-08-25', '16:00:00', '17:30:00', 1, 4, 6),
('2025-08-26', '18:00:00', '19:30:00', 2, 2, 5),
('2025-08-27', '07:00:00', '08:30:00', 3, 3, 6),
('2025-08-28', '20:00:00', '21:30:00', 4, 4, 5);

