-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: ecochef
-- ------------------------------------------------------
-- Server version	8.0.45

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
-- Table structure for table `alimentos`
--

DROP TABLE IF EXISTS `alimentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alimentos` (
  `id_alimentos` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `tipo` varchar(50) DEFAULT NULL,
  `calorias` int DEFAULT NULL,
  `id_categoria_alimento` int DEFAULT NULL,
  `fecha_caducidad` date DEFAULT NULL,
  `id_categoria` int DEFAULT NULL,
  PRIMARY KEY (`id_alimentos`),
  UNIQUE KEY `id_alimentos_UNIQUE` (`id_alimentos`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alimentos`
--

LOCK TABLES `alimentos` WRITE;
/*!40000 ALTER TABLE `alimentos` DISABLE KEYS */;
/*!40000 ALTER TABLE `alimentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categorias_alimentos`
--

DROP TABLE IF EXISTS `categorias_alimentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorias_alimentos` (
  `idcategoria_alimento` int NOT NULL AUTO_INCREMENT,
  `nombre_categoria` varchar(100) NOT NULL,
  PRIMARY KEY (`idcategoria_alimento`),
  UNIQUE KEY `idcategoria_alimento_UNIQUE` (`idcategoria_alimento`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorias_alimentos`
--

LOCK TABLES `categorias_alimentos` WRITE;
/*!40000 ALTER TABLE `categorias_alimentos` DISABLE KEYS */;
INSERT INTO `categorias_alimentos` VALUES (1,'Frutas'),(2,'VERDURAS'),(3,'CARNES'),(4,'LÁCTEOS'),(5,'DESPENSA');
/*!40000 ALTER TABLE `categorias_alimentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categorias_recetas`
--

DROP TABLE IF EXISTS `categorias_recetas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorias_recetas` (
  `idcategoria_receta` int NOT NULL AUTO_INCREMENT,
  `nombre_categoria` varchar(100) NOT NULL,
  PRIMARY KEY (`idcategoria_receta`),
  UNIQUE KEY `idcategoria_receta_UNIQUE` (`idcategoria_receta`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorias_recetas`
--

LOCK TABLES `categorias_recetas` WRITE;
/*!40000 ALTER TABLE `categorias_recetas` DISABLE KEYS */;
INSERT INTO `categorias_recetas` VALUES (1,'Saludable'),(2,'Pasta y Caprichos'),(3,'Guisos Tradicionales');
/*!40000 ALTER TABLE `categorias_recetas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ingredientes_base`
--

DROP TABLE IF EXISTS `ingredientes_base`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ingredientes_base` (
  `id_ingrediente_base` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_ingrediente_base`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ingredientes_base`
--

LOCK TABLES `ingredientes_base` WRITE;
/*!40000 ALTER TABLE `ingredientes_base` DISABLE KEYS */;
INSERT INTO `ingredientes_base` VALUES (1,'Pasta'),(2,'Huevo'),(3,'Panceta'),(4,'Lentejas'),(5,'Chorizo'),(6,'Salmón'),(7,'Lechuga'),(8,'Pollo'),(9,'Pan'),(10,'Salsa César'),(11,'Plátano'),(12,'Avena'),(13,'Leche'),(14,'Patata'),(15,'Cebolla'),(16,'Aguacate'),(17,'Tomate'),(18,'Albahaca'),(19,'Piñones'),(20,'Queso'),(21,'Crema de coco'),(22,'Curry'),(23,'Pepino'),(24,'Pimiento'),(25,'Ajo'),(26,'Aceite'),(27,'Vinagre'),(28,'Arroz'),(29,'Pepino'),(30,'Pimiento'),(31,'Ajo'),(32,'Aceite'),(33,'Vinagre'),(34,'Arroz'),(35,'Harina'),(36,'Tomate frito'),(37,'Jamon york');
/*!40000 ALTER TABLE `ingredientes_base` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventario`
--

DROP TABLE IF EXISTS `inventario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventario` (
  `id_inventario` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `id_alimento` int NOT NULL,
  `cantidad` int NOT NULL,
  `fecha_caducidad` date DEFAULT NULL,
  PRIMARY KEY (`id_inventario`),
  UNIQUE KEY `id_inventario_UNIQUE` (`id_inventario`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventario`
--

LOCK TABLES `inventario` WRITE;
/*!40000 ALTER TABLE `inventario` DISABLE KEYS */;
INSERT INTO `inventario` VALUES (1,1,64,500,'2026-12-31'),(2,6,9,1000,'2026-06-15');
/*!40000 ALTER TABLE `inventario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recetas`
--

DROP TABLE IF EXISTS `recetas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recetas` (
  `id_receta` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text,
  `tiempo_preparacion` int DEFAULT NULL,
  `id_usuario` int NOT NULL,
  PRIMARY KEY (`id_receta`),
  UNIQUE KEY `id_receta_UNIQUE` (`id_receta`),
  KEY `fk_receta_usuario` (`id_usuario`),
  CONSTRAINT `fk_receta_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`idusuarios`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recetas`
--

LOCK TABLES `recetas` WRITE;
/*!40000 ALTER TABLE `recetas` DISABLE KEYS */;
INSERT INTO `recetas` VALUES (2,'Gazpacho Andaluz','Triturar tomates, pepino, pimiento, ajo, aceite y vinagre.',10,1),(3,'Pasta Carbonara','Cocer pasta, saltear panceta y mezclar con huevo y queso pecorino.',15,1),(5,'Ensalada Cesar','Lechuga, pollo, costrones de pan y salsa cesar.',15,1),(6,'Salmon al Horno','Salmon con base de patatas y cebolla al limon.',30,1),(7,'Batido Energetico','Mezcla de platano, avena y leche.',5,1),(8,'Tortilla de Patatas','El clasico espanol con huevo, patata y cebolla.',25,1),(9,'Lentejas con Chorizo','Guiso tradicional de lentejas con chorizo, patata y zanahoria.',40,1),(10,'Arroz a la Cubana','Arroz blanco acompanado de huevo frito y salsa de tomate.',20,1),(11,'Toast de Aguacate','Pan integral tostado con aguacate machacado y un toque de limon.',5,1),(12,'Pollo al Curry','Dados de pollo cocinados con leche de coco y especias curry.',25,1),(13,'Ensalada Campera','Ensalada fria de patata cocida, tomate, cebolla y huevo.',15,1),(14,'Pasta al Pesto','Pasta con salsa de albahaca, pinones y queso.',12,1),(18,'Pizza','Harina, tomate, queso, jamon york',20,1);
/*!40000 ALTER TABLE `recetas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recetas_categorias`
--

DROP TABLE IF EXISTS `recetas_categorias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recetas_categorias` (
  `id_recetas_categorias` int NOT NULL AUTO_INCREMENT,
  `id_receta` int NOT NULL,
  `id_categoria_receta` varchar(45) NOT NULL,
  PRIMARY KEY (`id_recetas_categorias`),
  UNIQUE KEY `id_recetas_categorias_UNIQUE` (`id_recetas_categorias`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recetas_categorias`
--

LOCK TABLES `recetas_categorias` WRITE;
/*!40000 ALTER TABLE `recetas_categorias` DISABLE KEYS */;
INSERT INTO `recetas_categorias` VALUES (1,2,'1'),(2,3,'2');
/*!40000 ALTER TABLE `recetas_categorias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recetas_ingredientes`
--

DROP TABLE IF EXISTS `recetas_ingredientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recetas_ingredientes` (
  `id_recetas_ingredientes` int NOT NULL AUTO_INCREMENT,
  `id_receta` int NOT NULL,
  `id_alimento` int NOT NULL,
  `cantidad` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_recetas_ingredientes`),
  UNIQUE KEY `id_recetas_ingredientes_UNIQUE` (`id_recetas_ingredientes`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recetas_ingredientes`
--

LOCK TABLES `recetas_ingredientes` WRITE;
/*!40000 ALTER TABLE `recetas_ingredientes` DISABLE KEYS */;
INSERT INTO `recetas_ingredientes` VALUES (1,5,1,'200g'),(2,5,2,'2 ud'),(3,5,3,'100g'),(4,5,20,'50g'),(5,6,7,'1 ud'),(6,6,8,'150g'),(7,6,9,'1 rebanada'),(8,6,10,'2 cucharadas'),(9,7,6,'1 lomo'),(10,7,14,'1 ud'),(11,7,15,'1/2 ud'),(12,9,11,'1 ud'),(13,9,12,'40g'),(14,9,13,'200ml'),(15,11,2,'4 ud'),(16,11,14,'3 ud'),(17,11,15,'1 ud'),(18,12,9,'1 rebanada'),(19,12,16,'1 ud'),(20,12,2,'1 ud'),(21,8,8,'200g'),(22,8,21,'100ml'),(23,8,22,'1 cucharadita'),(24,13,14,'2 ud'),(25,13,17,'1 ud'),(26,13,2,'1 ud'),(27,14,1,'200g'),(28,14,18,'1 manojo'),(29,14,19,'20g'),(30,14,20,'30g'),(31,2,17,'500g'),(32,2,23,'1 ud'),(33,2,24,'1 ud'),(34,2,25,'1 diente'),(35,2,26,'Un chorro'),(36,2,17,'500g'),(37,2,23,'1 ud'),(38,2,24,'1 ud'),(39,2,25,'1 diente'),(40,2,26,'Un chorro'),(41,3,1,'200g'),(42,3,2,'2 ud'),(43,3,3,'100g'),(44,3,20,'50g'),(45,10,28,'100g'),(46,10,2,'1 ud'),(47,10,17,'3 cucharadas'),(48,18,35,'200g'),(49,18,36,'100g'),(50,18,20,'150g'),(51,18,37,'100g');
/*!40000 ALTER TABLE `recetas_ingredientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `idusuarios` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `contraseña` varchar(255) NOT NULL,
  `rol` varchar(20) NOT NULL,
  PRIMARY KEY (`idusuarios`),
  UNIQUE KEY `idusuarios_UNIQUE` (`idusuarios`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Pepe Test','pepe@test.com','1234','USUARIO'),(2,'pepe','pepe@gmail.com','pepe','USUARIO'),(4,'trujillo','trujillo@gmail.com','trujillo','USUARIO'),(5,'maria','maria@gmail.com','maria','USUARIO'),(6,'oscar','oscar@gmail.com','oscar','ADMIN');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios_recetas_fav`
--

DROP TABLE IF EXISTS `usuarios_recetas_fav`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios_recetas_fav` (
  `id_favorito` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `id_receta` int NOT NULL,
  `fecha_guardado` date DEFAULT NULL,
  PRIMARY KEY (`id_favorito`),
  UNIQUE KEY `id_favorito_UNIQUE` (`id_favorito`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios_recetas_fav`
--

LOCK TABLES `usuarios_recetas_fav` WRITE;
/*!40000 ALTER TABLE `usuarios_recetas_fav` DISABLE KEYS */;
INSERT INTO `usuarios_recetas_fav` VALUES (1,1,2,'2026-05-22'),(2,6,18,'2026-05-22');
/*!40000 ALTER TABLE `usuarios_recetas_fav` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-23 10:33:57
