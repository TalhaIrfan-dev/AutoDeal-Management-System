create database Project;

use Project;
 drop database Project;
 
create table Customers(
	CustomerID INT primary key auto_increment,
    CustomerName varchar(50) not null check (CustomerName != ''),
    CustomerCNIC varchar(15) unique not null,
    Phone_no varchar(15) not null unique,     
    Email varchar(100) unique,      -- can also be not null but if a prson dont have email id so what will he do
    check(CustomerCNIC regexp "^[0-9]{5}-[0-9]{7}-[0-9]{1}$"),
    check(Phone_no regexp "^[0-9]{4} [0-9]{7}$")
   -- check(Email like'%@%.%')
);
desc Customers;

-- ========== 2. Vehicle Types ==========
create table VehicleType(
	TypeId INT primary key auto_increment,
    Vehicle_type varchar(15) not null unique,
    Average_rent decimal(8,2) not null
);
desc VehicleType;


create table Vehicles(
	VehicleID INT primary key auto_increment,
    Vehicle_name varchar(50) not null check(Vehicle_name != " "),          -- vehicle name
    Registration_no varchar(15) unique not null,
    TypeId INT not null,
    VehicleStatus ENUM('available', 'rented', 'sold', 'not_available') not null,
    Price decimal(10, 2) not null,
    check (Registration_no regexp '^[A-Z]{1,3}-[0-9]{1,4}$'),   -- A to Z can be 1 2 or 3 alphabets and space after from 0-9 can be 1 2 3 or 4 numbers
	foreign key (TypeId ) references VehicleType(TypeId) on update cascade
);
-- alter table Vehicles modify Status ENUM('available', 'rented', 'sold', 'not_available') not null;
desc Vehicles;



create table Rentals(     -- vehicles that are on rent
	Rental_id int  primary key auto_increment,
    VehicleID INT not null unique,
    CustomerID INT not null,
    rental_date Date not null default(current_date),
    rental_price decimal(10, 2) not null,
    foreign key (VehicleID) references Vehicles(VehicleID) on update cascade,
    foreign key (CustomerID) references Customers(CustomerID) on update cascade
);
desc Rentals;


create table Records(
	Record_id int  primary key auto_increment,
    VehicleID INT not null,
    CustomerID INT not null,
    record_date Date not null default(current_date),
    return_date Date ,
    price decimal(10, 2) not null,
    Status ENUM('Rented' , 'Sold') not null,
	foreign key (VehicleID) references Vehicles(VehicleID) on update cascade on delete cascade,
    foreign key (CustomerID) references Customers(CustomerID) on update cascade on delete cascade
);

-- alter table Records modify Status ENUM('Rented' , 'Sold') not null;

desc Records;



-- ========== 8. History Table (keep records all transactions in past and present too) ==========
create table History (
    HistoryID INT primary key auto_increment,
    CustomerName varchar(50),
    CustomerCNIC varchar(15),
    Vehicle_name varchar(50),
    Registration_no varchar(15),
    record_date date not null,    -- DEFAULT (CURRENT_DATE)
    return_date date,
    price DECIMAL(10,2) not null,
    Status ENUM('Rented', 'Sold') not null
);
desc history;


INSERT INTO History (                 -- if i insert id then after deleting record its id will remain same in this table but in actual table on that id there will be other row
    CustomerName, CustomerCNIC, Vehicle_name, Registration_no, 
    record_date, return_date, price, Status
)
SELECT 
    C.CustomerName, C.CustomerCNIC,
    V.Vehicle_name, V.Registration_no,
    R.record_date, R.return_date, R.price, R.Status
FROM Records R
JOIN Customers C ON R.CustomerID = C.CustomerID
JOIN Vehicles V ON R.VehicleID = V.VehicleID
WHERE NOT EXISTS (
    SELECT 1 FROM History H 
    WHERE H.CustomerCNIC = C.CustomerCNIC 
      AND H.Registration_no = V.Registration_no 
      AND H.record_date = R.record_date
);

INSERT INTO Customers (CustomerName, CustomerCNIC, Phone_no, Email) VALUES
('Ahmed Khan', '42101-1234567-1', '0300 1234567', 'ahmed.khan@example.com'),
('Sara Malik', '35202-7654321-9', '0312 7654321', 'sara.malik@example.com'),
('Zain Ali', '61101-3344556-4', '0321 3344556', 'zain.ali@example.com'),
('Hina Shah', '42201-2233445-2', '0301 2233445', 'hina.shah@example.com'),
('Talha Rizvi', '34602-8087367-6', '0345 9138746', 'taha.rigii@example.com'),
('Talha Rizvi', '33102-9988776-6', '0345 9988776', 'talha.rizvi@example.com');

INSERT INTO Vehicles (Vehicle_name, Registration_no, TypeId, VehicleStatus, Price) VALUES
('Toyota Corolla', 'ABC-1234', 1, 'available', 2500000),
('Honda Civic', 'DEF-5678', 1, 'rented', 2800000),
('Suzuki Alto', 'GHI-4321', 3, 'sold', 1600000),
('KIA Sportage', 'JKL-8765', 2, 'available', 5500000),
('Yamaha YBR 125', 'MNO-999', 4, 'rented', 300000),
('Kawasaki Ninja H2R', 'KNH-2424', 4, 'available', 800000),
('Hyundai Elantra', 'HYN-1122', 1, 'available', 2700000),
('Toyota Fortuner', 'TYF-8899', 2, 'available', 9000000),
('Suzuki Wagon R', 'SWG-3344', 3, 'available', 1800000),
('Honda BR-V', 'HBR-5566', 2, 'available', 4300000),
('Yamaha MT-15', 'YMT-777', 4, 'available', 450000),
('Honda CD 70', 'HCD-888', 4, 'available', 150000),
('Suzuki Cultus', 'SCL-1212', 3, 'available', 2000000),
('Kawasaki Z1000', 'KZI-9090', 4, 'available', 1200000),
('Nissan Dayz', 'NSD-6543', 3, 'available', 1700000),
('Changan Karvaan', 'CHK-8888', 5, 'available', 2100000),
('Toyota Hiace', 'THC-2222', 5, 'available', 3800000),
('Honda Vezel', 'HVZ-3434', 1, 'available', 4700000);

INSERT INTO VehicleType (Vehicle_type, Average_rent) VALUES
('Sedan', 4000),
('SUV', 5000),
('Hatchback', 3000),
('Bike', 2000),
('Van', 3500);

INSERT INTO Rentals (Rental_id, VehicleID, CustomerID, rental_date, rental_price) VALUES
(1, 5, 1, '2025-05-10', 15000),
(2, 2, 2, '2025-05-11', 3500);


INSERT INTO Records (VehicleID, CustomerID, record_date, return_date, price, Status) VALUES
(3, 2, '2025-03-10', NULL, 1600000, 'Sold'),
(4, 1, '2025-04-01', '2025-04-05', 15000, 'Rented')
;




truncate table records;
INSERT INTO Records (VehicleID, CustomerID, record_date, price, Status)
SELECT VehicleID, CustomerID, rental_date, rental_price, 'Rented'
FROM Rentals;

select * from Records;

delete from records where Record_id = 10;
select * from vehicleType;
select * from vehicles;
select*from rentals;
delete from VehicleType where TypeId = 6;
select * from customers;