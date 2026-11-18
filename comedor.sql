-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
-- DATOS FICTICIOS SOLO PARA PRUEBAS (sin información personal real).
-- Usuarios de ejemplo:
--   Alumno     -> codigo 0000000001 / clave 0000000001
--   Secretaria -> codigo 12345678   / clave 12345678
-- El menu y las compras usan CURDATE() para que las pruebas funcionen
-- el mismo dia en que se importe el dump.
--
-- Host: localhost    Database: comedor
-- ------------------------------------------------------
-- Server version	5.6.28-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `alumnos`
--

DROP TABLE IF EXISTS `alumnos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `alumnos` (
  `alum_id` int(11) NOT NULL AUTO_INCREMENT,
  `alum_nombres` varchar(100) DEFAULT NULL,
  `alum_apellido_pa` varchar(150) DEFAULT NULL,
  `alum_apellido_ma` varchar(150) DEFAULT NULL,
  `alum_codigo` varchar(11) DEFAULT NULL,
  `alum_pass` varchar(11) DEFAULT NULL,
  `alum_correo` varchar(100) DEFAULT NULL,
  `alum_celular` varchar(11) DEFAULT NULL,
  `alum_saldo` int(11) DEFAULT NULL,
  `alum_beca` enum('si','no') DEFAULT NULL,
  PRIMARY KEY (`alum_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alumnos`
--

LOCK TABLES `alumnos` WRITE;
/*!40000 ALTER TABLE `alumnos` DISABLE KEYS */;
INSERT INTO `alumnos` (`alum_id`, `alum_nombres`, `alum_apellido_pa`, `alum_apellido_ma`, `alum_codigo`, `alum_pass`, `alum_correo`, `alum_celular`, `alum_saldo`, `alum_beca`) VALUES
(1, 'Ana', 'Perez', 'Lopez', '0000000001', '0000000001', 'ana.perez@example.com', '900000001', 10, 'no');
/*!40000 ALTER TABLE `alumnos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `compra`
--

DROP TABLE IF EXISTS `compra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `compra` (
  `comp_id` int(11) NOT NULL AUTO_INCREMENT,
  `comp_qr` varchar(10) DEFAULT NULL,
  `comp_fecha` date DEFAULT NULL,
  `comp_almuerzo` enum('si','no') DEFAULT NULL,
  `comp_cena` enum('si','no') DEFAULT NULL,
  `alum_id` int(11) NOT NULL,
  `menu_id` int(11) NOT NULL,
  PRIMARY KEY (`comp_id`),
  KEY `fk_alumnos_has_menu_menu1_idx` (`menu_id`),
  KEY `fk_alumnos_has_menu_alumnos_idx` (`alum_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `compra`
--

LOCK TABLES `compra` WRITE;
/*!40000 ALTER TABLE `compra` DISABLE KEYS */;
INSERT INTO `compra` (`comp_id`, `comp_qr`, `comp_fecha`, `comp_almuerzo`, `comp_cena`, `alum_id`, `menu_id`) VALUES
(1, '####', CURDATE(), 'no', 'no', 1, 1);
/*!40000 ALTER TABLE `compra` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu` (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT,
  `menu_fecha` date DEFAULT NULL,
  `menu_desayuno` varchar(100) DEFAULT NULL,
  `menu_des_cont` int(11) DEFAULT NULL,
  `menu_alm_entrada` varchar(60) DEFAULT NULL,
  `menu_alm_postre` varchar(60) DEFAULT NULL,
  `menu_almuerzo` varchar(100) DEFAULT NULL,
  `menu_alm_bebida` varchar(60) DEFAULT NULL,
  `menu_alm_cont` int(11) DEFAULT NULL,
  `menu_alm_contbeca` int(11) DEFAULT NULL,
  `menu_cen_entrada` varchar(60) DEFAULT NULL,
  `menu_cen_postre` varchar(60) DEFAULT NULL,
  `menu_cena` varchar(100) DEFAULT NULL,
  `menu_cen_bebida` varchar(60) DEFAULT NULL,
  `menu_cen_cont` int(11) DEFAULT NULL,
  `menu_cen_contbeca` int(11) DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` (`menu_id`, `menu_fecha`, `menu_desayuno`, `menu_des_cont`, `menu_alm_entrada`, `menu_alm_postre`, `menu_almuerzo`, `menu_alm_bebida`, `menu_alm_cont`, `menu_alm_contbeca`, `menu_cen_entrada`, `menu_cen_postre`, `menu_cena`, `menu_cen_bebida`, `menu_cen_cont`, `menu_cen_contbeca`) VALUES
(1, CURDATE(), 'Avena con banana', 20, 'Sopa de verduras', 'Gelatina', 'Arroz con pollo', 'Chicha morada', 20, 5, 'Caldo de gallina', 'Papaya', 'Tallarines con pollo', 'Refresco de limon', 20, 5);
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock`
--

DROP TABLE IF EXISTS `stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock` (
  `stoc_id` int(11) NOT NULL AUTO_INCREMENT,
  `stoc_periodo` varchar(45) DEFAULT NULL,
  `stoc_becados` int(11) DEFAULT NULL,
  PRIMARY KEY (`stoc_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock`
--

LOCK TABLES `stock` WRITE;
/*!40000 ALTER TABLE `stock` DISABLE KEYS */;
INSERT INTO `stock` (`stoc_id`, `stoc_periodo`, `stoc_becados`) VALUES
(1, CONCAT(YEAR(CURDATE()), '-I'), 5);
/*!40000 ALTER TABLE `stock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `usu_id` int(11) NOT NULL AUTO_INCREMENT,
  `usu_nombre` varchar(100) DEFAULT NULL,
  `usu_apellido` varchar(150) DEFAULT NULL,
  `usu_direccion` varchar(150) DEFAULT NULL,
  `usu_celular` varchar(11) DEFAULT NULL,
  `usu_codigo_dni` varchar(8) DEFAULT NULL,
  `usu_contrasena` varchar(8) DEFAULT NULL,
  `usu_correo` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`usu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` (`usu_id`, `usu_nombre`, `usu_apellido`, `usu_direccion`, `usu_celular`, `usu_codigo_dni`, `usu_contrasena`, `usu_correo`) VALUES
(1, 'Maria', 'Gomez Ruiz', 'Av. Central 123', '900000002', '12345678', '12345678', 'maria.gomez@example.com');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'comedor'
--

--
-- Dumping routines for database 'comedor'
--
/*!50003 DROP FUNCTION IF EXISTS `cantidad_becados_almuerzo` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=CURRENT_USER FUNCTION `cantidad_becados_almuerzo`() RETURNS int(11)
BEGIN
RETURN (select count(*) from alumnos a, compra c, menu m
where a.alum_id=c.alum_id and m.menu_id=c.menu_id
and c.comp_fecha=date(now()) and c.comp_almuerzo='si' and a.alum_beca='si');
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `cantidad_becados_cena` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=CURRENT_USER FUNCTION `cantidad_becados_cena`() RETURNS int(11)
BEGIN
RETURN (select count(*) from alumnos a, compra c, menu m
where a.alum_id=c.alum_id and m.menu_id=c.menu_id
and c.comp_fecha=date(now()) and c.comp_cena='si' and a.alum_beca='si');
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `cantidad_compra_almuerzo` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=CURRENT_USER FUNCTION `cantidad_compra_almuerzo`() RETURNS int(11)
BEGIN
RETURN (select count(*) from alumnos a, compra c, menu m
where a.alum_id=c.alum_id and m.menu_id=c.menu_id
and c.comp_fecha=date(now()) and c.comp_almuerzo='si');
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `cantidad_compra_almuerzo_cena` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=CURRENT_USER FUNCTION `cantidad_compra_almuerzo_cena`() RETURNS int(11)
BEGIN
RETURN (select count(*) from alumnos a, compra c, menu m
where a.alum_id=c.alum_id and m.menu_id=c.menu_id
and c.comp_fecha=date(now()) and c.comp_cena='si' and c.comp_almuerzo='si');
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `cantidad_compra_cena` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=CURRENT_USER FUNCTION `cantidad_compra_cena`() RETURNS int(11)
BEGIN
RETURN (select count(*) from alumnos a, compra c, menu m
where a.alum_id=c.alum_id and m.menu_id=c.menu_id
and c.comp_fecha=date(now()) and c.comp_cena='si');
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `cantidad_nobecados_almuerzo` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=CURRENT_USER FUNCTION `cantidad_nobecados_almuerzo`() RETURNS int(11)
BEGIN
RETURN (select count(*) from alumnos a, compra c, menu m
where a.alum_id=c.alum_id and m.menu_id=c.menu_id
and c.comp_fecha=date(now()) and c.comp_almuerzo='si' and a.alum_beca='no');
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `cantidad_nobecados_almu_cena` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=CURRENT_USER FUNCTION `cantidad_nobecados_almu_cena`() RETURNS int(11)
BEGIN
RETURN (select count(*) from alumnos a, compra c, menu m
where a.alum_id=c.alum_id and m.menu_id=c.menu_id
and c.comp_fecha=date(now()) and c.comp_almuerzo='si' and c.comp_cena='si' and a.alum_beca='no');
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `cantidad_nobecados_cena` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=CURRENT_USER FUNCTION `cantidad_nobecados_cena`() RETURNS int(11)
BEGIN
RETURN (select count(*) from alumnos a, compra c, menu m
where a.alum_id=c.alum_id and m.menu_id=c.menu_id
and c.comp_fecha=date(now()) and c.comp_cena='si' and a.alum_beca='no');
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `editar_menu` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=CURRENT_USER PROCEDURE `editar_menu`(IN `fecha` DATE, IN `desayuno` VARCHAR(100), IN `almen` VARCHAR(100),
 IN `almpostre` VARCHAR(100), IN `almuerzo` VARCHAR(100), IN `almbeb` VARCHAR(100), 
 IN `cenaen` VARCHAR(100), IN `cenapos` VARCHAR(100), IN `cena` VARCHAR(100), IN `cenabeb` VARCHAR(100), in menuid int)
BEGIN
UPDATE `menu`
SET
`menu_fecha` = fecha,
`menu_desayuno` = desayuno,
`menu_alm_entrada` = almen,
`menu_alm_postre` = almpostre,
`menu_almuerzo` = almuerzo,
`menu_alm_bebida` = almbeb,
`menu_cen_entrada` = cenaen,
`menu_cen_postre` = cenapos,
`menu_cena` = cena,
`menu_cen_bebida` = cenabeb
WHERE `menu_id` = menuid;
commit;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `generar_recarga` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=CURRENT_USER PROCEDURE `generar_recarga`(IN `alumcod` VARCHAR(12), IN `recarga` INT)
BEGIN
declare idal int default null;
declare magnolia_me_ama char(5) default null;
declare continue handler for not found begin end;

select alum_id, alum_beca into idal, magnolia_me_ama from alumnos where alum_codigo=alumcod limit 1;

if idal is null then
select 'Alumno no encontrado' mensaje;
elseif recarga is null or recarga <= 0 then
select 'Monto de recarga inválido' mensaje;
elseif magnolia_me_ama='no' then
UPDATE `alumnos` SET `alum_saldo` = alum_saldo+recarga WHERE `alum_id` = idal;
select concat('Usted recargó ',recarga,if(recarga>1,' soles a su cuenta',' sol a su cuenta')) mensaje;
else
select 'Alumno es becado' mensaje;
end if;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `list_consumo_almuerzo_cena` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=CURRENT_USER PROCEDURE `list_consumo_almuerzo_cena`()
BEGIN
select a.alum_codigo, concat(a.alum_nombres,' ',a.alum_apellido_pa,' ', a.alum_apellido_ma) alumno from alumnos a, compra c, menu m
where a.alum_id=c.alum_id and m.menu_id=c.menu_id
and c.comp_fecha=date(now()) and c.comp_cena='si';
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `registrar_alumno` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=CURRENT_USER PROCEDURE `registrar_alumno`(IN `n` VARCHAR(100), IN `ap` VARCHAR(150), IN `am` VARCHAR(150), IN `cod` VARCHAR(11), IN `cor` VARCHAR(100), IN `celular` VARCHAR(11), IN `beca` ENUM('si','no'))
BEGIN
/*select concat(n,ap,am,cod,cod,cor,celular);*/
INSERT INTO `alumnos`
(`alum_nombres`,
`alum_apellido_pa`,
`alum_apellido_ma`,
`alum_codigo`,
`alum_pass`,
`alum_correo`,
`alum_celular`, alum_saldo,alum_beca)
VALUES
(n,ap,am,cod,cod,cor,celular,0,beca);
select 'El registro se registro correctamente' mensaje;
commit;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `registrar_compra_almuerzo` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=CURRENT_USER PROCEDURE `registrar_compra_almuerzo`(IN `alumcod` VARCHAR(12))
BEGIN
declare idal int default null;
declare menuid int default null;
declare compid int default null;
declare becado char(5) default null;
declare saldo int default 0;
declare stock_reg int default 0;
declare stock_beca int default 0;
declare ya_almuerzo char(5) default null;
declare continue handler for not found begin end;
declare exit handler for sqlexception begin
    rollback;
    select 'Error al procesar la compra' mensaje;
end;

start transaction;

select alum_id, alum_beca, alum_saldo into idal, becado, saldo
from alumnos where alum_codigo=alumcod limit 1 for update;

select menu_id, menu_alm_cont, menu_alm_contbeca into menuid, stock_reg, stock_beca
from menu where menu_fecha=curdate() limit 1 for update;

if idal is null then
    rollback;
    select 'Alumno no encontrado' mensaje;
elseif menuid is null then
    rollback;
    select 'No hay menú registrado para hoy' mensaje;
else
    select comp_id, comp_almuerzo into compid, ya_almuerzo
    from compra where alum_id=idal and menu_id=menuid and comp_fecha=curdate() limit 1 for update;

    if ya_almuerzo = 'si' then
        rollback;
        select 'Usted ya compró su almuerzo' mensaje;
    elseif becado = 'si' then
        if stock_beca is null or stock_beca <= 0 then
            rollback;
            select 'Sin stock disponible' mensaje;
        else
            if compid is null then
                INSERT INTO `compra` (`comp_qr`,`comp_fecha`,`comp_almuerzo`,`comp_cena`,`alum_id`,`menu_id`)
                VALUES ('####',curdate(),'si','no',idal,menuid);
            else
                UPDATE `compra` SET `comp_almuerzo` = 'si' WHERE `comp_id` = compid;
            end if;
            UPDATE `menu` SET `menu_alm_contbeca` = (menu_alm_contbeca-1) WHERE `menu_id` = menuid;
            commit;
            select 'Compra exitosa' mensaje;
        end if;
    else
        if saldo is null or saldo <= 0 then
            rollback;
            select 'Saldo insuficiente' mensaje;
        elseif stock_reg is null or stock_reg <= 0 then
            rollback;
            select 'Sin stock disponible' mensaje;
        else
            if compid is null then
                INSERT INTO `compra` (`comp_qr`,`comp_fecha`,`comp_almuerzo`,`comp_cena`,`alum_id`,`menu_id`)
                VALUES ('####',curdate(),'si','no',idal,menuid);
            else
                UPDATE `compra` SET `comp_almuerzo` = 'si' WHERE `comp_id` = compid;
            end if;
            UPDATE `alumnos` SET `alum_saldo` = alum_saldo-1 WHERE `alum_id` = idal;
            UPDATE `menu` SET `menu_alm_cont` = (menu_alm_cont-1) WHERE `menu_id` = menuid;
            commit;
            select 'Compra exitosa' mensaje;
        end if;
    end if;
end if;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `registrar_compra_cena` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=CURRENT_USER PROCEDURE `registrar_compra_cena`(IN `alumcod` VARCHAR(12))
BEGIN
declare idal int default null;
declare menuid int default null;
declare compid int default null;
declare becado char(5) default null;
declare saldo int default 0;
declare stock_reg int default 0;
declare stock_beca int default 0;
declare mag char(5) default null;
declare continue handler for not found begin end;
declare exit handler for sqlexception begin
    rollback;
    select 'Error al procesar la compra' mensaje;
end;

start transaction;

select alum_id, alum_beca, alum_saldo into idal, becado, saldo
from alumnos where alum_codigo=alumcod limit 1 for update;

select menu_id, menu_cen_cont, menu_cen_contbeca into menuid, stock_reg, stock_beca
from menu where menu_fecha=curdate() limit 1 for update;

if idal is null then
    rollback;
    select 'Alumno no encontrado' mensaje;
elseif menuid is null then
    rollback;
    select 'No hay menú registrado para hoy' mensaje;
else
    select comp_id, comp_cena into compid, mag
    from compra where comp_fecha=curdate() and menu_id=menuid and alum_id=idal limit 1 for update;

    if mag = 'si' then
        rollback;
        select 'Usted ya compró cena' mensaje;
    elseif becado = 'si' then
        if stock_beca is null or stock_beca <= 0 then
            rollback;
            select 'Sin stock disponible' mensaje;
        else
            if compid is null then
                INSERT INTO `compra`(`comp_qr`,`comp_fecha`,`comp_almuerzo`,`comp_cena`,`alum_id`,`menu_id`)
                VALUES ('####',curdate(),'no','si',idal,menuid);
            else
                UPDATE `compra` SET `comp_cena` = 'si' WHERE `comp_id` = compid;
            end if;
            UPDATE `menu` SET `menu_cen_contbeca` = (menu_cen_contbeca-1) WHERE `menu_id` = menuid;
            commit;
            select 'Compra exitosa' mensaje;
        end if;
    else
        if saldo is null or saldo <= 0 then
            rollback;
            select 'Saldo insuficiente' mensaje;
        elseif stock_reg is null or stock_reg <= 0 then
            rollback;
            select 'Sin stock disponible' mensaje;
        else
            if compid is null then
                INSERT INTO `compra`(`comp_qr`,`comp_fecha`,`comp_almuerzo`,`comp_cena`,`alum_id`,`menu_id`)
                VALUES ('####',curdate(),'no','si',idal,menuid);
            else
                UPDATE `compra` SET `comp_cena` = 'si' WHERE `comp_id` = compid;
            end if;
            UPDATE `alumnos` SET `alum_saldo` = alum_saldo-1 WHERE `alum_id` = idal;
            UPDATE `menu` SET `menu_cen_cont` = (menu_cen_cont-1) WHERE `menu_id` = menuid;
            commit;
            select 'Compra exitosa' mensaje;
        end if;
    end if;
end if;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `registrar_menu` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
CREATE DEFINER=CURRENT_USER PROCEDURE `registrar_menu`(IN `fecha` DATE, IN `desayuno` VARCHAR(100), IN `almen` VARCHAR(100), IN `almpostre` VARCHAR(100), IN `almuerzo` VARCHAR(100), IN `almbeb` VARCHAR(100), IN `cenaen` VARCHAR(100), IN `cenapos` VARCHAR(100), IN `cena` VARCHAR(100), IN `cenabeb` VARCHAR(100))
BEGIN
declare becados int  default(select stoc_becados from stock order by stoc_id desc limit 1);

INSERT INTO `comedor`.`menu`
(`menu_fecha`,`menu_desayuno`,`menu_des_cont`,`menu_alm_entrada`,`menu_alm_postre`,`menu_almuerzo`,`menu_alm_bebida`,
`menu_alm_cont`,`menu_cen_entrada`,`menu_cen_postre`,`menu_cena`,`menu_cen_bebida`,`menu_cen_cont`,
menu_alm_contbeca, menu_cen_contbeca)
VALUES
(fecha,desayuno,320,almen,almpostre,almuerzo,almbeb,(650-becados),
cenaen,cenapos,cena,cenabeb,(360-becados),becados,becados);


END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-07-09  8:03:03
