-- MySQL dump 10.13  Distrib 5.6.15, for Linux (x86_64)
--
-- Host: localhost    Database: p2p
-- ------------------------------------------------------
-- Server version	5.6.15-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_sqs`
--

DROP TABLE IF EXISTS `t_sqs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_sqs` (
  `t_sqs_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `quest_text` varchar(60) NOT NULL,
  `quest_stat` int(11) NOT NULL DEFAULT '0',
  `quest_cdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `quest_edate` datetime DEFAULT NULL,
  PRIMARY KEY (`t_sqs_seq`),
  UNIQUE KEY `t_sqs_seq` (`t_sqs_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_sqs`
--

LOCK TABLES `t_sqs` WRITE;
/*!40000 ALTER TABLE `t_sqs` DISABLE KEYS */;
INSERT INTO `t_sqs` VALUES (23,'爷爷的名字？',0,'2015-02-06 11:16:39',NULL),(24,'爸爸的名字？',0,'2015-02-06 11:16:40',NULL),(25,'母亲的姓氏？',0,'2015-02-06 11:16:43',NULL),(26,'出生的城市？',0,'2015-02-06 11:16:43',NULL),(27,'您就读的小学？',0,'2015-02-06 11:16:44',NULL),(28,'您就读的中学？',0,'2015-02-06 11:16:44',NULL),(29,'寝室室友的名字？',0,'2015-02-06 11:16:44',NULL),(30,'对你影响最大的名人？',0,'2015-02-06 11:16:44',NULL),(31,'出生医院的名字？',0,'2015-02-06 11:16:44',NULL);
/*!40000 ALTER TABLE `t_sqs` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-03-19 10:46:59
