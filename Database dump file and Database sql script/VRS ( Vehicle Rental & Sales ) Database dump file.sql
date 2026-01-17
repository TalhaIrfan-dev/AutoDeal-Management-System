CREATE DATABASE  IF NOT EXISTS `project` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `project`;
-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: project
-- ------------------------------------------------------
-- Server version	9.2.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `CustomerID` int NOT NULL AUTO_INCREMENT,
  `CustomerName` varchar(50) NOT NULL,
  `CustomerCNIC` varchar(15) NOT NULL,
  `Phone_no` varchar(15) NOT NULL,
  `Email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`CustomerID`),
  UNIQUE KEY `CustomerCNIC` (`CustomerCNIC`),
  UNIQUE KEY `Phone_no` (`Phone_no`),
  UNIQUE KEY `Email` (`Email`),
  CONSTRAINT `customers_chk_1` CHECK ((`CustomerName` <> _utf8mb4'')),
  CONSTRAINT `customers_chk_2` CHECK (regexp_like(`CustomerCNIC`,_utf8mb4'^[0-9]{5}-[0-9]{7}-[0-9]{1}$')),
  CONSTRAINT `customers_chk_3` CHECK (regexp_like(`Phone_no`,_utf8mb4'^[0-9]{4} [0-9]{7}$'))
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (1,'Ahmed Khan','42101-1234567-1','0300 1234567','ahmed.khan@example.com'),(2,'Sara Malik','35202-7654321-9','0312 7654321','sara.malik@example.com'),(3,'Zain Ali','61101-3344556-4','0321 3344556','zain.ali@example.com'),(4,'Hina Shah','42201-2233445-2','0301 2233445','hina.shah@example.com');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `history`
--

DROP TABLE IF EXISTS `history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `history` (
  `HistoryID` int NOT NULL AUTO_INCREMENT,
  `CustomerName` varchar(50) DEFAULT NULL,
  `CustomerCNIC` varchar(15) DEFAULT NULL,
  `Vehicle_name` varchar(50) DEFAULT NULL,
  `Registration_no` varchar(15) DEFAULT NULL,
  `record_date` date NOT NULL,
  `return_date` date DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `Status` enum('Rented','Sold') NOT NULL,
  PRIMARY KEY (`HistoryID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `history`
--

LOCK TABLES `history` WRITE;
/*!40000 ALTER TABLE `history` DISABLE KEYS */;
INSERT INTO `history` VALUES (1,'Sara Malik','35202-7654321-9','Suzuki Alto','GHI-4321','2025-03-10',NULL,1600000.00,'Sold'),(2,'Ahmed Khan','42101-1234567-1','KIA Sportage','JKL-8765','2025-04-01','2025-04-05',15000.00,'Rented');
/*!40000 ALTER TABLE `history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `records`
--

DROP TABLE IF EXISTS `records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `records` (
  `Record_id` int NOT NULL AUTO_INCREMENT,
  `VehicleID` int NOT NULL,
  `CustomerID` int NOT NULL,
  `record_date` date NOT NULL DEFAULT (curdate()),
  `return_date` date DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `Status` enum('Rented','Sold') NOT NULL,
  PRIMARY KEY (`Record_id`),
  KEY `VehicleID` (`VehicleID`),
  KEY `CustomerID` (`CustomerID`),
  CONSTRAINT `records_ibfk_1` FOREIGN KEY (`VehicleID`) REFERENCES `vehicles` (`VehicleID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `records_ibfk_2` FOREIGN KEY (`CustomerID`) REFERENCES `customers` (`CustomerID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `records`
--

LOCK TABLES `records` WRITE;
/*!40000 ALTER TABLE `records` DISABLE KEYS */;
INSERT INTO `records` VALUES (1,3,2,'2025-03-10',NULL,1600000.00,'Sold'),(2,4,1,'2025-04-01','2025-04-05',15000.00,'Rented');
/*!40000 ALTER TABLE `records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rentals`
--

DROP TABLE IF EXISTS `rentals`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rentals` (
  `Rental_id` int NOT NULL AUTO_INCREMENT,
  `VehicleID` int NOT NULL,
  `CustomerID` int NOT NULL,
  `rental_date` date NOT NULL DEFAULT (curdate()),
  `rental_price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`Rental_id`),
  UNIQUE KEY `VehicleID` (`VehicleID`),
  KEY `CustomerID` (`CustomerID`),
  CONSTRAINT `rentals_ibfk_1` FOREIGN KEY (`VehicleID`) REFERENCES `vehicles` (`VehicleID`) ON UPDATE CASCADE,
  CONSTRAINT `rentals_ibfk_2` FOREIGN KEY (`CustomerID`) REFERENCES `customers` (`CustomerID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rentals`
--

LOCK TABLES `rentals` WRITE;
/*!40000 ALTER TABLE `rentals` DISABLE KEYS */;
INSERT INTO `rentals` VALUES (1,5,1,'2025-05-10',15000.00),(2,2,2,'2025-05-11',3500.00);
/*!40000 ALTER TABLE `rentals` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicles`
--

DROP TABLE IF EXISTS `vehicles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicles` (
  `VehicleID` int NOT NULL AUTO_INCREMENT,
  `Vehicle_name` varchar(50) NOT NULL,
  `Registration_no` varchar(15) NOT NULL,
  `TypeId` int NOT NULL,
  `VehicleStatus` enum('available','rented','sold','not_available') NOT NULL,
  `Price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`VehicleID`),
  UNIQUE KEY `Registration_no` (`Registration_no`),
  KEY `TypeId` (`TypeId`),
  CONSTRAINT `vehicles_ibfk_1` FOREIGN KEY (`TypeId`) REFERENCES `vehicletype` (`TypeId`) ON UPDATE CASCADE,
  CONSTRAINT `vehicles_chk_1` CHECK ((`Vehicle_name` <> _utf8mb4' ')),
  CONSTRAINT `vehicles_chk_2` CHECK (regexp_like(`Registration_no`,_utf8mb4'^[A-Z]{1,3}-[0-9]{1,4}$'))
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicles`
--

LOCK TABLES `vehicles` WRITE;
/*!40000 ALTER TABLE `vehicles` DISABLE KEYS */;
INSERT INTO `vehicles` VALUES (1,'Toyota Corolla','ABC-1234',1,'available',2500000.00),(2,'Honda Civic','DEF-5678',1,'rented',2800000.00),(3,'Suzuki Alto','GHI-4321',3,'sold',1600000.00),(4,'KIA Sportage','JKL-8765',2,'available',5500000.00),(5,'Yamaha YBR 125','MNO-999',4,'rented',300000.00),(6,'Kawasaki Ninja H2R','KNH-2424',4,'available',800000.00),(19,'Hyundai Elantra','HYN-1122',1,'available',2700000.00),(20,'Toyota Fortuner','TYF-8899',2,'available',9000000.00),(21,'Suzuki Wagon R','SWG-3344',3,'available',1800000.00),(22,'Honda BR-V','HBR-5566',2,'available',4300000.00),(23,'Yamaha MT-15','YMT-777',4,'available',450000.00),(24,'Honda CD 70','HCD-888',4,'available',150000.00),(25,'Suzuki Cultus','SCL-1212',3,'available',2000000.00),(26,'Kawasaki Z1000','KZI-9090',4,'available',1200000.00),(27,'Nissan Dayz','NSD-6543',3,'available',1700000.00),(28,'Changan Karvaan','CHK-8888',5,'available',2100000.00),(29,'Toyota Hiace','THC-2222',5,'available',3800000.00),(30,'Honda Vezel','HVZ-3434',1,'available',4700000.00);
/*!40000 ALTER TABLE `vehicles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicletype`
--

DROP TABLE IF EXISTS `vehicletype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicletype` (
  `TypeId` int NOT NULL AUTO_INCREMENT,
  `Vehicle_type` varchar(15) NOT NULL,
  `Average_rent` decimal(8,2) NOT NULL,
  PRIMARY KEY (`TypeId`),
  UNIQUE KEY `Vehicle_type` (`Vehicle_type`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicletype`
--

LOCK TABLES `vehicletype` WRITE;
/*!40000 ALTER TABLE `vehicletype` DISABLE KEYS */;
INSERT INTO `vehicletype` VALUES (1,'Sedan',4000.00),(2,'SUV',5000.00),(3,'Hatchback',3000.00),(4,'Bike',2000.00),(5,'Van',3500.00);
/*!40000 ALTER TABLE `vehicletype` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-21 21:20:24
