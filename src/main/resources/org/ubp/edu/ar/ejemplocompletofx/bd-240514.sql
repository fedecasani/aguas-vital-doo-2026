CREATE DATABASE  IF NOT EXISTS `ejemplo-completo-fx-2024` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `ejemplo-completo-fx-2024`;
-- MySQL dump 10.13  Distrib 8.0.36, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: ejemplo-completo-fx-2024
-- ------------------------------------------------------
-- Server version	8.0.35-0ubuntu0.23.04.1

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
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` longtext NOT NULL,
  `apellido` longtext NOT NULL,
  `dni` varchar(9) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES (1,'JUAN','GOMEZ','20369875'),(2,'MARIANA','JEREZ','25687411');
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detallepedido`
--

DROP TABLE IF EXISTS `detallepedido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detallepedido` (
  `id` int NOT NULL AUTO_INCREMENT,
  `idPedido` int NOT NULL,
  `idProducto` int NOT NULL,
  `cantidad` float NOT NULL,
  `precioVta` float NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_DetallePedido_producto1_idx` (`idProducto`),
  KEY `fk_DetallePedido_pedido1_idx` (`idPedido`),
  CONSTRAINT `fk_DetallePedido_pedido1` FOREIGN KEY (`idPedido`) REFERENCES `pedido` (`id`),
  CONSTRAINT `fk_DetallePedido_producto1` FOREIGN KEY (`idProducto`) REFERENCES `producto` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=69;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detallepedido`
--

LOCK TABLES `detallepedido` WRITE;
/*!40000 ALTER TABLE `detallepedido` DISABLE KEYS */;
INSERT INTO `detallepedido` VALUES (20,2,3,1,6000),(21,2,5,2,5000),(22,2,2,1,3455),(45,14,2,2,11222),(46,14,5,2,2500),(60,18,2,1,55000),(61,18,5,1,5000),(62,12,3,1,2500),(63,12,1,3,6500),(64,12,5,1,5000),(65,19,2,1,55000),(66,19,5,1,5000),(67,1,3,1,6000),(68,1,2,1,55000);
/*!40000 ALTER TABLE `detallepedido` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estado`
--

DROP TABLE IF EXISTS `estado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estado` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fecha` datetime NOT NULL,
  `nombreEstado` varchar(50) NOT NULL,
  `idPedido` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_estado_pedido1_idx` (`idPedido`),
  CONSTRAINT `fk_estado_pedido1` FOREIGN KEY (`idPedido`) REFERENCES `pedido` (`id`)
) ENGINE=InnoDB;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estado`
--

LOCK TABLES `estado` WRITE;
/*!40000 ALTER TABLE `estado` DISABLE KEYS */;
/*!40000 ALTER TABLE `estado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedido`
--

DROP TABLE IF EXISTS `pedido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedido` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fecha` datetime NOT NULL,
  `idCliente` int NOT NULL,
  `idVendedor` int NOT NULL,
  `nro` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nro_UNIQUE` (`nro`),
  KEY `fk_pedido_vendedor_idx` (`idVendedor`),
  KEY `fk_pedido_cliente1_idx` (`idCliente`),
  CONSTRAINT `fk_pedido_cliente1` FOREIGN KEY (`idCliente`) REFERENCES `cliente` (`id`),
  CONSTRAINT `fk_pedido_vendedor` FOREIGN KEY (`idVendedor`) REFERENCES `vendedor` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedido`
--

LOCK TABLES `pedido` WRITE;
/*!40000 ALTER TABLE `pedido` DISABLE KEYS */;
INSERT INTO `pedido` VALUES (1,'2024-05-12 00:00:00',1,1,1),(2,'2024-05-12 00:00:00',2,1,2),(12,'2024-05-12 00:00:00',2,2,6),(14,'2024-05-23 00:00:00',1,1,7),(18,'2024-05-12 00:00:00',2,1,9),(19,'2024-05-12 00:00:00',1,1,10);
/*!40000 ALTER TABLE `pedido` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producto`
--

DROP TABLE IF EXISTS `producto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `producto` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` longtext NOT NULL,
  `codBarra` varchar(50) NOT NULL,
  `precio` float NOT NULL,
  `cantidad` float NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `codBarra_UNIQUE` (`codBarra`)
) ENGINE=InnoDB AUTO_INCREMENT=6;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producto`
--

LOCK TABLES `producto` WRITE;
/*!40000 ALTER TABLE `producto` DISABLE KEYS */;
INSERT INTO `producto` VALUES (1,'PELOTA FUTBOL NRO 5','7869554',3500,1000),(2,'BOTINES','6987544',55000,150),(3,'PELOTA BASQUET NRO 7','6545454',6000,500),(4,'RAQUETA TENIS','5554445',30000,100),(5,'PROTECTOR BUCAL','4565774',5000,250);
/*!40000 ALTER TABLE `producto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vendedor`
--

DROP TABLE IF EXISTS `vendedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vendedor` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` longtext NOT NULL,
  `apellido` longtext NOT NULL,
  `legajo` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vendedor`
--

LOCK TABLES `vendedor` WRITE;
/*!40000 ALTER TABLE `vendedor` DISABLE KEYS */;
INSERT INTO `vendedor` VALUES (1,'JORGE','GOMEZ','2023-01'),(2,'JUANA','MASLIA','2021-05');
/*!40000 ALTER TABLE `vendedor` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-14 15:31:17
