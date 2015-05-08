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
-- Table structure for table `authorities`
--

DROP TABLE IF EXISTS `authorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authorities` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `authority` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1937 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bms_users`
--

DROP TABLE IF EXISTS `bms_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bms_users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `password_salt` varchar(100) DEFAULT NULL,
  `buname` varchar(30) NOT NULL,
  `email` varchar(60) NOT NULL,
  `mobile` varchar(20) NOT NULL,
  `stat` varchar(60) NOT NULL,
  `cdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `cname` varchar(60) NOT NULL,
  `role_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_users_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`10.138.30.64`*/ /*!50003 trigger tri_sync_bms_user_add
after insert on p2p.bms_users
for each row
begin
delete from p2pbms.users;
delete from p2pbms.user_roles;
insert into p2pbms.users (username,password) select username,password from p2p.bms_users;
insert into p2pbms.user_roles (username,role_name) select username,role_name from p2p.bms_users;
end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`10.138.30.64`*/ /*!50003 trigger tri_sync_bms_user_edt
after update on p2p.bms_users
for each row
begin
delete from p2pbms.users;
delete from p2pbms.user_roles;
insert into p2pbms.users (username,password) select username,password from p2p.bms_users;
insert into p2pbms.user_roles (username,role_name) select username,role_name from p2p.bms_users;
end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`10.138.30.64`*/ /*!50003 trigger tri_sync_bms_user_del
after delete on p2p.bms_users
for each row
begin
delete from p2pbms.users;
delete from p2pbms.user_roles;
insert into p2pbms.users (username,password) select username,password from p2p.bms_users;
insert into p2pbms.user_roles (username,role_name) select username,role_name from p2p.bms_users;
end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `deleteme1426572177895`
--

DROP TABLE IF EXISTS `deleteme1426572177895`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `deleteme1426572177895` (
  `unused` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `deleteme1426824329050`
--

DROP TABLE IF EXISTS `deleteme1426824329050`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `deleteme1426824329050` (
  `unused` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `deleteme1429185878139`
--

DROP TABLE IF EXISTS `deleteme1429185878139`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `deleteme1429185878139` (
  `unused` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `deleteme1429185995976`
--

DROP TABLE IF EXISTS `deleteme1429185995976`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `deleteme1429185995976` (
  `unused` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `deleteme1429188535899`
--

DROP TABLE IF EXISTS `deleteme1429188535899`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `deleteme1429188535899` (
  `unused` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_seq`
--

DROP TABLE IF EXISTS `order_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_seq` (
  `cur_seq` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sequence`
--

DROP TABLE IF EXISTS `sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sequence` (
  `SEQ_NAME` varchar(50) NOT NULL,
  `SEQ_COUNT` decimal(38,0) DEFAULT NULL,
  PRIMARY KEY (`SEQ_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sudoor_lib_file`
--

DROP TABLE IF EXISTS `sudoor_lib_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sudoor_lib_file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data` tinyblob,
  `name` varchar(255) NOT NULL,
  `uri` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_acct`
--

DROP TABLE IF EXISTS `t_acct`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_acct` (
  `t_acct_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `tac_uname` varchar(16) NOT NULL,
  `tac_stat` int(11) NOT NULL DEFAULT '0',
  `tac_gender` int(11) DEFAULT NULL,
  `tac_mobile` varchar(11) NOT NULL,
  `tac_mbstat` int(11) DEFAULT NULL,
  `tac_email` varchar(60) DEFAULT NULL,
  `tac_emstat` int(11) DEFAULT NULL,
  `tac_name` varchar(30) DEFAULT NULL,
  `tac_slv` int(11) DEFAULT NULL,
  `tac_pid` char(18) DEFAULT NULL,
  `tac_edu_degree` int(11) DEFAULT NULL,
  `tac_inds` varchar(60) DEFAULT NULL,
  `tac_income` int(11) DEFAULT NULL,
  `tac_wprov` int(11) DEFAULT NULL,
  `tac_wcity` int(11) DEFAULT NULL,
  `tac_wtown` int(11) DEFAULT NULL,
  `tac_rdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `tac_rip` varchar(20) NOT NULL,
  `tac_rip_prove` int(11) DEFAULT NULL,
  `tac_rip_city` int(11) DEFAULT NULL,
  `tac_rip_town` int(11) DEFAULT NULL,
  `tac_mstat` int(11) DEFAULT NULL,
  `tac_hprov` int(11) DEFAULT NULL,
  `tac_hcity` int(11) DEFAULT NULL,
  `tac_htown` int(11) DEFAULT NULL,
  `tac_house` int(11) DEFAULT NULL,
  `tac_house_loan` int(11) DEFAULT NULL,
  `tac_car` int(11) DEFAULT NULL,
  `tac_cinds` int(11) DEFAULT NULL,
  `tac_csize` int(11) DEFAULT NULL,
  `tac_cpt` int(11) DEFAULT NULL,
  `tac_wtime` int(11) DEFAULT NULL,
  `tac_depart` varchar(60) DEFAULT NULL,
  `tac_position` varchar(60) DEFAULT NULL,
  `tac_sdate` varchar(20) DEFAULT NULL,
  `tac_cscore` int(11) DEFAULT NULL,
  `tac_tlv` int(11) DEFAULT NULL,
  `t_res_seq` bigint(20) DEFAULT NULL,
  `tac_idcs` int(11) DEFAULT NULL,
  `tac_iccs` int(11) DEFAULT NULL,
  `tac_jcs` int(11) DEFAULT NULL,
  `tac_addcs` int(11) DEFAULT NULL,
  `tac_idcs_pdate` datetime DEFAULT NULL,
  `tac_iccs_pdate` datetime DEFAULT NULL,
  `tac_jts_pdate` datetime DEFAULT NULL,
  `tac_addjs_pdate` datetime DEFAULT NULL,
  `tac_ltype` int(11) DEFAULT NULL,
  `tac_addr` varchar(300) DEFAULT NULL,
  `tac_birth` char(8) DEFAULT NULL,
  `act_third` varchar(60) DEFAULT NULL,
  `act_thbstat` int(11) DEFAULT NULL,
  `tac_tcity` int(11) DEFAULT NULL,
  `tac_tprov` int(11) DEFAULT NULL,
  `tac_role` int(11) NOT NULL DEFAULT '2',
  PRIMARY KEY (`t_acct_seq`),
  UNIQUE KEY `t_acct_seq` (`t_acct_seq`),
  UNIQUE KEY `tac_uname` (`tac_uname`),
  UNIQUE KEY `tac_mobile` (`tac_mobile`),
  KEY `idx_t_acct_tac_uname` (`tac_uname`),
  KEY `idx_t_acct_tac_gender` (`tac_gender`),
  KEY `idx_t_acct_tac_mobile` (`tac_mobile`),
  KEY `idx_t_acct_tac_email` (`tac_email`),
  KEY `idx_t_acct_tac_pid` (`tac_pid`),
  KEY `idx_t_acct_tac_wprov` (`tac_wprov`),
  KEY `idx_t_acct_tac_wcity` (`tac_wcity`),
  KEY `idx_t_acct_tac_wtown` (`tac_wtown`),
  KEY `idx_t_acct_tac_hprov` (`tac_hprov`),
  KEY `idx_t_acct_tac_hcity` (`tac_hcity`),
  KEY `idx_t_acct_tac_htown` (`tac_htown`),
  KEY `idx_t_acct_tac_stat` (`tac_stat`),
  KEY `idx_t_acct_tac_rip_prove` (`tac_rip_prove`),
  KEY `idx_t_acct_tac_rip_city` (`tac_rip_city`),
  KEY `idx_t_acct_tac_rip_town` (`tac_rip_town`)
) ENGINE=InnoDB AUTO_INCREMENT=102495 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_acct_tmp1`
--

DROP TABLE IF EXISTS `t_acct_tmp1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_acct_tmp1` (
  `tac_uname` varchar(16) NOT NULL,
  `tac_stat` int(11) NOT NULL DEFAULT '0',
  `tac_gender` int(11) NOT NULL DEFAULT '0',
  `tac_mobile` varchar(11) NOT NULL,
  `tac_mbstat` int(11) DEFAULT NULL,
  `tac_email` varchar(60) DEFAULT NULL,
  `tac_emstat` int(11) DEFAULT NULL,
  `tac_name` varchar(30) DEFAULT NULL,
  `tac_slv` int(11) DEFAULT NULL,
  `tac_pid` char(18) DEFAULT NULL,
  `tac_edu_degree` int(11) DEFAULT NULL,
  `tac_inds` varchar(60) DEFAULT NULL,
  `tac_income` int(11) DEFAULT NULL,
  `tac_wprov` int(11) DEFAULT NULL,
  `tac_wcity` int(11) DEFAULT NULL,
  `tac_wtown` int(11) DEFAULT NULL,
  `tac_rdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `tac_rip` varchar(20) NOT NULL,
  `tac_rip_prove` int(11) DEFAULT NULL,
  `tac_rip_city` int(11) DEFAULT NULL,
  `tac_rip_town` int(11) DEFAULT NULL,
  `tac_mstat` int(11) DEFAULT NULL,
  `tac_hprov` int(11) DEFAULT NULL,
  `tac_hcity` int(11) DEFAULT NULL,
  `tac_htown` int(11) DEFAULT NULL,
  `tac_house` int(11) DEFAULT NULL,
  `tac_house_loan` int(11) DEFAULT NULL,
  `tac_car` int(11) DEFAULT NULL,
  `tac_cinds` int(11) DEFAULT NULL,
  `tac_csize` int(11) DEFAULT NULL,
  `tac_cpt` int(11) DEFAULT NULL,
  `tac_wtime` int(11) DEFAULT NULL,
  `tac_depart` varchar(60) DEFAULT NULL,
  `tac_position` varchar(60) DEFAULT NULL,
  `tac_sdate` varchar(20) DEFAULT NULL,
  `tac_cscore` int(11) DEFAULT NULL,
  `tac_tlv` int(11) DEFAULT NULL,
  `t_res_seq` bigint(20) DEFAULT NULL,
  `tac_idcs` int(11) DEFAULT NULL,
  `tac_iccs` int(11) DEFAULT NULL,
  `tac_jcs` int(11) DEFAULT NULL,
  `tac_addcs` int(11) DEFAULT NULL,
  `tac_idcs_pdate` datetime DEFAULT NULL,
  `tac_iccs_pdate` datetime DEFAULT NULL,
  `tac_jts_pdate` datetime DEFAULT NULL,
  `tac_addjs_pdate` datetime DEFAULT NULL,
  `tac_ltype` int(11) DEFAULT NULL,
  `tac_addr` varchar(300) DEFAULT NULL,
  `t_acct_tmp1_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`t_acct_tmp1_seq`),
  UNIQUE KEY `t_acct_tmp1_seq` (`t_acct_tmp1_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_acct_tmp2`
--

DROP TABLE IF EXISTS `t_acct_tmp2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_acct_tmp2` (
  `tac_uname` varchar(16) NOT NULL,
  `tac_stat` int(11) NOT NULL DEFAULT '0',
  `tac_gender` int(11) NOT NULL DEFAULT '0',
  `tac_mobile` varchar(11) NOT NULL,
  `tac_mbstat` int(11) DEFAULT NULL,
  `tac_email` varchar(60) DEFAULT NULL,
  `tac_emstat` int(11) DEFAULT NULL,
  `tac_name` varchar(30) DEFAULT NULL,
  `tac_slv` int(11) DEFAULT NULL,
  `tac_pid` char(18) DEFAULT NULL,
  `tac_edu_degree` int(11) DEFAULT NULL,
  `tac_inds` varchar(60) DEFAULT NULL,
  `tac_income` int(11) DEFAULT NULL,
  `tac_wprov` int(11) DEFAULT NULL,
  `tac_wcity` int(11) DEFAULT NULL,
  `tac_wtown` int(11) DEFAULT NULL,
  `tac_rdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `tac_rip` varchar(20) NOT NULL,
  `tac_rip_prove` int(11) DEFAULT NULL,
  `tac_rip_city` int(11) DEFAULT NULL,
  `tac_rip_town` int(11) DEFAULT NULL,
  `tac_mstat` int(11) DEFAULT NULL,
  `tac_hprov` int(11) DEFAULT NULL,
  `tac_hcity` int(11) DEFAULT NULL,
  `tac_htown` int(11) DEFAULT NULL,
  `tac_house` int(11) DEFAULT NULL,
  `tac_house_loan` int(11) DEFAULT NULL,
  `tac_car` int(11) DEFAULT NULL,
  `tac_cinds` int(11) DEFAULT NULL,
  `tac_csize` int(11) DEFAULT NULL,
  `tac_cpt` int(11) DEFAULT NULL,
  `tac_wtime` int(11) DEFAULT NULL,
  `tac_depart` varchar(60) DEFAULT NULL,
  `tac_position` varchar(60) DEFAULT NULL,
  `tac_sdate` varchar(20) DEFAULT NULL,
  `tac_cscore` int(11) DEFAULT NULL,
  `tac_tlv` int(11) DEFAULT NULL,
  `t_res_seq` bigint(20) DEFAULT NULL,
  `tac_idcs` int(11) DEFAULT NULL,
  `tac_iccs` int(11) DEFAULT NULL,
  `tac_jcs` int(11) DEFAULT NULL,
  `tac_addcs` int(11) DEFAULT NULL,
  `tac_idcs_pdate` datetime DEFAULT NULL,
  `tac_iccs_pdate` datetime DEFAULT NULL,
  `tac_jts_pdate` datetime DEFAULT NULL,
  `tac_addjs_pdate` datetime DEFAULT NULL,
  `tac_ltype` int(11) DEFAULT NULL,
  `tac_addr` varchar(300) DEFAULT NULL,
  `t_acct_tmp2_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`t_acct_tmp2_seq`),
  UNIQUE KEY `t_acct_tmp2_seq` (`t_acct_tmp2_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_ass_clm`
--

DROP TABLE IF EXISTS `t_act_ass_clm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_ass_clm` (
  `t_act_ass_clm_seq` bigint(20) NOT NULL,
  `t_act_ass_inv_seq` bigint(20) NOT NULL,
  `act_clm_pqt` double(12,2) NOT NULL,
  `act_clm_aqt` double(20,10) NOT NULL,
  `act_clm_gpp` int(11) NOT NULL,
  `act_clm_status` int(11) NOT NULL,
  `t_act_clm_seq` bigint(20) NOT NULL,
  `t_acct_seq` bigint(20) NOT NULL,
  `act_clm_cdate` datetime NOT NULL,
  `act_clm_edate` datetime DEFAULT NULL,
  PRIMARY KEY (`t_act_ass_clm_seq`),
  UNIQUE KEY `t_act_ass_clm_seq` (`t_act_ass_clm_seq`),
  KEY `idx_t_act_ass_inv_seq` (`t_act_ass_inv_seq`),
  KEY `idx_act_clm_status` (`act_clm_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_ass_inv`
--

DROP TABLE IF EXISTS `t_act_ass_inv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_ass_inv` (
  `t_act_ass_inv_seq` bigint(20) NOT NULL,
  `t_act_inv_seq` bigint(20) NOT NULL,
  `act_inv_qt` double(12,2) NOT NULL,
  `act_inv_cdate` datetime NOT NULL,
  `act_inv_gpp` int(11) NOT NULL,
  `act_inv_stat` int(11) NOT NULL,
  `t_acct_seq` bigint(20) NOT NULL,
  `act_inv_num` varchar(60) NOT NULL,
  `act_inv_eform` int(11) NOT NULL,
  `act_inv_edate` datetime DEFAULT NULL,
  `t_acct_fseq` bigint(20) NOT NULL,
  PRIMARY KEY (`t_act_ass_inv_seq`),
  UNIQUE KEY `t_act_ass_inv_seq` (`t_act_ass_inv_seq`),
  KEY `idx_t_act_inv_seq` (`t_act_inv_seq`),
  KEY `idx_act_inv_cdate` (`act_inv_cdate`),
  KEY `idx_act_inv_stat` (`act_inv_stat`),
  KEY `idx_t_acct_seq` (`t_acct_seq`),
  KEY `idx_act_inv_num` (`act_inv_num`),
  KEY `idx_act_inv_eform` (`act_inv_eform`),
  KEY `idx_t_acct_fseq` (`t_acct_fseq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_ass_order`
--

DROP TABLE IF EXISTS `t_act_ass_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_ass_order` (
  `t_act_ass_order_seq` bigint(20) NOT NULL,
  `trade_date` datetime NOT NULL,
  `order_stat` int(11) NOT NULL,
  `trade_amount` double(12,2) NOT NULL,
  `t_act_inv_seq` bigint(20) NOT NULL,
  `t_pacct_seq` bigint(20) NOT NULL,
  `t_gacct_seq` bigint(20) NOT NULL,
  `order_id` bigint(20) NOT NULL,
  `act_thblc` double(12,2) NOT NULL,
  `trade_desc` varchar(90) NOT NULL,
  `trxid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`t_act_ass_order_seq`),
  UNIQUE KEY `t_act_ass_order_seq` (`t_act_ass_order_seq`),
  KEY `idx_trade_date` (`trade_date`),
  KEY `idx_order_stat` (`order_stat`),
  KEY `idx_t_act_clm_seq` (`t_act_inv_seq`),
  KEY `idx_t_pacct_seq` (`t_pacct_seq`),
  KEY `idx_t_gacct_seq` (`t_gacct_seq`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_act_thblc` (`act_thblc`),
  KEY `idx_trxid` (`trxid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_bb_clm`
--

DROP TABLE IF EXISTS `t_act_bb_clm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_bb_clm` (
  `t_act_bb_clm_seq` bigint(20) NOT NULL,
  `t_act_bb_inv_seq` bigint(20) NOT NULL,
  `act_clm_pqt` double(12,2) NOT NULL,
  `act_clm_aqt` double(20,10) NOT NULL,
  `act_clm_gpp` int(11) NOT NULL,
  `act_clm_status` int(11) NOT NULL,
  `t_act_clm_seq` bigint(20) NOT NULL,
  `t_acct_seq` bigint(20) NOT NULL,
  `act_clm_cdate` datetime NOT NULL,
  `act_clm_edate` datetime DEFAULT NULL,
  PRIMARY KEY (`t_act_bb_clm_seq`),
  UNIQUE KEY `t_act_bb_clm_seq` (`t_act_bb_clm_seq`),
  KEY `idx_t_act_bb_inv_seq` (`t_act_bb_inv_seq`),
  KEY `idx_act_clm_status` (`act_clm_status`),
  KEY `idx_t_act_clm_seq` (`t_act_clm_seq`),
  KEY `idx_t_acct_seq` (`t_acct_seq`),
  KEY `idx_act_clm_cdate` (`act_clm_cdate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_bb_inv`
--

DROP TABLE IF EXISTS `t_act_bb_inv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_bb_inv` (
  `t_act_bb_inv_seq` bigint(20) NOT NULL,
  `t_act_inv_seq` bigint(20) NOT NULL,
  `act_inv_qt` double(12,2) NOT NULL,
  `act_inv_cdate` datetime NOT NULL,
  `act_inv_gpp` int(11) NOT NULL,
  `act_inv_stat` int(11) NOT NULL,
  `act_inv_num` varchar(60) NOT NULL,
  `act_inv_eform` int(11) NOT NULL,
  `act_inv_edate` datetime DEFAULT NULL,
  `t_acct_fseq` bigint(20) NOT NULL,
  `t_acct_seq` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`t_act_bb_inv_seq`),
  UNIQUE KEY `t_act_bb_inv_seq` (`t_act_bb_inv_seq`),
  KEY `idx_t_act_inv_seq` (`t_act_inv_seq`),
  KEY `idx_act_inv_cdate` (`act_inv_cdate`),
  KEY `idx_t_acct_fseq` (`t_acct_fseq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_bb_order`
--

DROP TABLE IF EXISTS `t_act_bb_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_bb_order` (
  `t_act_bb_order_seq` bigint(20) NOT NULL,
  `trade_date` datetime NOT NULL,
  `order_stat` int(11) NOT NULL,
  `trade_amount` double(12,2) NOT NULL,
  `t_act_inv_seq` bigint(20) NOT NULL,
  `t_pacct_seq` bigint(20) NOT NULL,
  `t_gacct_seq` bigint(20) NOT NULL,
  `order_id` bigint(20) NOT NULL,
  `act_thblc` double(12,2) NOT NULL,
  `trade_desc` varchar(90) NOT NULL,
  `trxid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`t_act_bb_order_seq`),
  UNIQUE KEY `t_act_bb_order_seq` (`t_act_bb_order_seq`),
  KEY `idx_trade_date` (`trade_date`),
  KEY `idx_order_stat` (`order_stat`),
  KEY `idx_t_act_clm_seq` (`t_act_inv_seq`),
  KEY `idx_t_pacct_seq` (`t_pacct_seq`),
  KEY `idx_t_gacct_seq` (`t_gacct_seq`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_act_thblc` (`act_thblc`),
  KEY `idx_trxid` (`trxid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_bkcard`
--

DROP TABLE IF EXISTS `t_act_bkcard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_bkcard` (
  `t_act_bkcard_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `act_third` varchar(60) NOT NULL,
  `bank_code` int(11) NOT NULL,
  `abc_num` bigint(20) NOT NULL,
  `abc_ddstat` int(11) NOT NULL DEFAULT '0',
  `abc_prov` int(11) DEFAULT NULL,
  `abc_city` int(11) DEFAULT NULL,
  `abc_town` int(11) DEFAULT NULL,
  `t_acct_seq` bigint(20) DEFAULT NULL,
  `abc_name` varchar(90) DEFAULT NULL,
  `abc_mobile` varchar(11) DEFAULT NULL,
  `abc_pri_zone` varchar(255) DEFAULT NULL,
  `abc_char_set` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`t_act_bkcard_seq`),
  UNIQUE KEY `t_act_bkcard_seq` (`t_act_bkcard_seq`),
  KEY `idx_t_act_bkcard_act_third` (`act_third`),
  KEY `idx_t_act_bkcard_bank_code` (`bank_code`),
  KEY `idx_t_act_bkcard_abc_prov` (`abc_prov`),
  KEY `idx_t_act_bkcard_abc_city` (`abc_city`),
  KEY `idx_t_act_bkcard_abc_town` (`abc_town`),
  KEY `idx_t_act_bkcard_t_acct_seq` (`t_acct_seq`),
  KEY `idx_t_act_bkcard_abc_num` (`abc_num`)
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_clm`
--

DROP TABLE IF EXISTS `t_act_clm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_clm` (
  `t_act_clm_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `t_act_inv_seq` bigint(20) NOT NULL,
  `act_clm_pqt` double(12,2) NOT NULL DEFAULT '0.00',
  `act_clm_aqt` double(12,2) NOT NULL DEFAULT '0.00',
  `act_clm_gpp` int(11) NOT NULL,
  `act_clm_udays` int(11) DEFAULT NULL,
  `act_clm_ddays` int(11) DEFAULT NULL,
  `act_clm_status` int(11) NOT NULL DEFAULT '1',
  `act_clm_cdate` datetime DEFAULT NULL,
  `act_clm_edate` datetime DEFAULT NULL,
  `rec_edate` datetime DEFAULT NULL,
  PRIMARY KEY (`t_act_clm_seq`),
  UNIQUE KEY `t_act_clm_seq` (`t_act_clm_seq`),
  KEY `idx_t_act_clm_t_act_inv_seq` (`t_act_inv_seq`),
  KEY `idx_t_act_clm_act_clm_status` (`act_clm_status`),
  KEY `idx_t_act_clm_act_clm_cdate` (`act_clm_cdate`)
) ENGINE=InnoDB AUTO_INCREMENT=5052 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_cont`
--

DROP TABLE IF EXISTS `t_act_cont`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_cont` (
  `t_act_cont_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `cont_num` varchar(90) NOT NULL,
  `cont_cdate` datetime NOT NULL,
  `inv_tac_uname` varchar(30) NOT NULL,
  `inv_tac_name` varchar(30) NOT NULL,
  `inv_tac_pid` char(18) NOT NULL,
  `inv_amount` double NOT NULL,
  `inv_period` int(11) NOT NULL,
  `gmon_amount` double NOT NULL,
  `pmon_amount` double NOT NULL,
  `jin_rate` double NOT NULL,
  `bow_tac_name` varchar(30) NOT NULL,
  `bow_tac_pid` char(18) NOT NULL,
  `bow_tac_uname` varchar(30) NOT NULL,
  `bow_tac_email` varchar(60) NOT NULL,
  `loan_target` varchar(30) NOT NULL,
  `loan_amount` double NOT NULL,
  `inv_irate` double NOT NULL,
  `pm_pay_frate` double NOT NULL,
  `pay_day` varchar(16) NOT NULL,
  `pm_inv_frate` double NOT NULL,
  `ear_cln_con` varchar(255) NOT NULL,
  `t_pacct_seq` bigint(20) NOT NULL,
  `t_gacct_seq` bigint(20) NOT NULL,
  `t_inv_seq` bigint(20) NOT NULL,
  PRIMARY KEY (`t_act_cont_seq`),
  UNIQUE KEY `t_act_cont_seq` (`t_act_cont_seq`),
  KEY `idx_cont_num` (`cont_num`),
  KEY `idx_cont_cdate` (`cont_cdate`),
  KEY `idx_inv_tac_uname` (`inv_tac_uname`),
  KEY `idx_inv_tac_name` (`inv_tac_name`),
  KEY `idx_inv_tac_pid` (`inv_tac_pid`),
  KEY `idx_bow_tac_name` (`bow_tac_name`),
  KEY `idx_bow_tac_pid` (`bow_tac_pid`),
  KEY `idx_bow_tac_uname` (`bow_tac_uname`),
  KEY `idx_bow_tac_email` (`bow_tac_email`),
  KEY `idx_t_pacct_seq` (`t_pacct_seq`),
  KEY `idx_t_gacct_seq` (`t_gacct_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_contr`
--

DROP TABLE IF EXISTS `t_act_contr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_contr` (
  `t_act_contr_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `t_acct_seq` bigint(20) NOT NULL,
  `t_res_seq` bigint(20) NOT NULL,
  PRIMARY KEY (`t_act_contr_seq`),
  UNIQUE KEY `t_act_contr_seq` (`t_act_contr_seq`),
  KEY `idx_t_act_contr_t_acct_seq` (`t_acct_seq`),
  KEY `idx_t_act_contr_t_res_seq` (`t_res_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_csoh`
--

DROP TABLE IF EXISTS `t_act_csoh`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_csoh` (
  `t_act_csoh_seq` bigint(20) NOT NULL,
  `t_act_spot_order_seq` bigint(20) NOT NULL,
  `ostat_cdate` datetime NOT NULL,
  `order_stat` int(11) NOT NULL,
  PRIMARY KEY (`t_act_csoh_seq`),
  UNIQUE KEY `t_act_csoh_seq` (`t_act_csoh_seq`),
  KEY `idx_t_act_spot_order_seq` (`t_act_spot_order_seq`),
  KEY `idx_ostat_cdate` (`ostat_cdate`),
  KEY `idx_order_stat` (`order_stat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_iaoh`
--

DROP TABLE IF EXISTS `t_act_iaoh`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_iaoh` (
  `t_act_iaoh_seq` bigint(20) NOT NULL,
  `t_act_ass_order_seq` bigint(20) NOT NULL,
  `ostat_cdate` datetime NOT NULL,
  `order_stat` int(11) NOT NULL,
  PRIMARY KEY (`t_act_iaoh_seq`),
  UNIQUE KEY `t_act_iaoh_seq` (`t_act_iaoh_seq`),
  KEY `idx_t_act_ass_order_seq` (`t_act_ass_order_seq`),
  KEY `idx_ostat_cdate` (`ostat_cdate`),
  KEY `idx_order_stat` (`order_stat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_iboh`
--

DROP TABLE IF EXISTS `t_act_iboh`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_iboh` (
  `t_act_iboh_seq` bigint(20) NOT NULL,
  `t_act_bb_order_seq` bigint(20) NOT NULL,
  `ostat_cdate` datetime NOT NULL,
  `order_stat` int(11) NOT NULL,
  PRIMARY KEY (`t_act_iboh_seq`),
  UNIQUE KEY `t_act_iboh_seq` (`t_act_iboh_seq`),
  KEY `idx_t_act_bb_order_seq` (`t_act_bb_order_seq`),
  KEY `idx_ostat_cdate` (`ostat_cdate`),
  KEY `idx_order_stat` (`order_stat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_inv`
--

DROP TABLE IF EXISTS `t_act_inv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_inv` (
  `t_act_inv_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `t_acct_seq` bigint(20) NOT NULL,
  `t_inv_seq` bigint(20) NOT NULL,
  `act_inv_qt` double(12,2) DEFAULT NULL,
  `act_inv_cdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `act_inv_gpp` int(11) NOT NULL,
  `act_inv_tedate` datetime DEFAULT NULL,
  `act_inv_stat` int(11) NOT NULL DEFAULT '1',
  `act_inv_gform` int(11) NOT NULL DEFAULT '1',
  `act_inv_num` varchar(60) NOT NULL,
  `act_inv_mfee` double(12,2) NOT NULL DEFAULT '0.00',
  `act_inv_eform` int(11) DEFAULT NULL,
  `t_acct_fseq` bigint(20) NOT NULL,
  `act_inv_edate` datetime DEFAULT NULL,
  `act_inv_cont_id` varchar(60) DEFAULT NULL,
  `rec_edate` datetime DEFAULT NULL,
  PRIMARY KEY (`t_act_inv_seq`),
  UNIQUE KEY `t_act_inv_seq` (`t_act_inv_seq`),
  KEY `idx_t_act_inv_t_acct_seq` (`t_acct_seq`),
  KEY `idx_t_act_inv_t_inv_seq` (`t_inv_seq`),
  KEY `idx_t_act_inv_act_inv_cdate` (`act_inv_cdate`),
  KEY `idx_t_act_inv_act_inv_stat` (`act_inv_stat`),
  KEY `idx_t_act_inv_act_inv_gform` (`act_inv_gform`)
) ENGINE=InnoDB AUTO_INCREMENT=308 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_inv_rule`
--

DROP TABLE IF EXISTS `t_act_inv_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_inv_rule` (
  `t_act_inv_rule_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `air_pmax` double(12,2) NOT NULL DEFAULT '0.00',
  `air_period` varchar(30) DEFAULT NULL,
  `air_clv` varchar(30) DEFAULT NULL,
  `air_stat` int(11) NOT NULL DEFAULT '0',
  `air_type` int(11) NOT NULL DEFAULT '0',
  `t_acct_seq` bigint(20) NOT NULL,
  `rec_cdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `air_sdate` datetime DEFAULT NULL,
  PRIMARY KEY (`t_act_inv_rule_seq`),
  UNIQUE KEY `t_act_inv_rule_seq` (`t_act_inv_rule_seq`),
  KEY `idx_t_act_inv_rule_t_acct_seq` (`t_acct_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_inv_tmk`
--

DROP TABLE IF EXISTS `t_act_inv_tmk`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_inv_tmk` (
  `t_act_inv_tmk_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `t_act_inv_seq` bigint(20) NOT NULL,
  `t_pacct_seq` bigint(20) DEFAULT '0',
  `t_gacct_seq` bigint(20) NOT NULL,
  `trade_idate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `trade_ddate` datetime DEFAULT NULL,
  `trade_odate` datetime NOT NULL,
  `trade_stat` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`t_act_inv_tmk_seq`),
  UNIQUE KEY `t_act_inv_tmk_seq` (`t_act_inv_tmk_seq`),
  KEY `idx_t_act_inv_seq` (`t_act_inv_seq`),
  KEY `idx_t_pacct_seq` (`t_pacct_seq`),
  KEY `idx_t_gacct_seq` (`t_gacct_seq`),
  KEY `idx_trade_stat` (`trade_stat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_inv_tmk_copy`
--

DROP TABLE IF EXISTS `t_act_inv_tmk_copy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_inv_tmk_copy` (
  `t_act_inv_tmk_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `t_act_inv_seq` bigint(20) NOT NULL,
  `t_pacct_seq` bigint(20) DEFAULT '0',
  `t_gacct_seq` bigint(20) NOT NULL,
  `trade_idate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `trade_ddate` datetime DEFAULT NULL,
  `trade_odate` datetime NOT NULL,
  `trade_stat` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`t_act_inv_tmk_seq`),
  UNIQUE KEY `t_act_inv_tmk_seq` (`t_act_inv_tmk_seq`),
  KEY `idx_t_act_inv_seq` (`t_act_inv_seq`),
  KEY `idx_t_pacct_seq` (`t_pacct_seq`),
  KEY `idx_t_gacct_seq` (`t_gacct_seq`),
  KEY `idx_trade_stat` (`trade_stat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_msg`
--

DROP TABLE IF EXISTS `t_act_msg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_msg` (
  `t_act_msg_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `t_acct_seq` bigint(20) NOT NULL,
  `amsg_title` varchar(90) NOT NULL DEFAULT '',
  `t_act_remind_seq` bigint(20) NOT NULL,
  `amsg_rstat` int(11) NOT NULL DEFAULT '1',
  `amsg_cont` varchar(2000) NOT NULL,
  `amsg_sender` varchar(60) NOT NULL,
  `amsg_rdate` datetime NOT NULL,
  `ropt_form` int(11) NOT NULL,
  PRIMARY KEY (`t_act_msg_seq`),
  UNIQUE KEY `t_act_msg_seq` (`t_act_msg_seq`),
  KEY `idx_t_act_msg_t_acct_seq` (`t_acct_seq`),
  KEY `idx_t_act_msg_amsg_rstat` (`amsg_rstat`),
  KEY `idx_t_act_msg_remind_seq` (`t_act_remind_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=110765 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_oh`
--

DROP TABLE IF EXISTS `t_act_oh`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_oh` (
  `t_act_oh_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `t_act_order_seq` bigint(20) NOT NULL,
  `ostat_cdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `order_stat` int(11) NOT NULL,
  `trade_desc` varchar(90) DEFAULT NULL,
  PRIMARY KEY (`t_act_oh_seq`),
  UNIQUE KEY `t_act_oh_seq` (`t_act_oh_seq`),
  KEY `idx_t_act_oh_t_act_order_seq` (`t_act_order_seq`),
  KEY `idx_t_act_oh_ostat_cdate` (`ostat_cdate`),
  KEY `idx_t_act_oh_order_stat` (`order_stat`)
) ENGINE=InnoDB AUTO_INCREMENT=18251 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_order`
--

DROP TABLE IF EXISTS `t_act_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_order` (
  `t_act_order_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `trade_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `order_stat` int(11) NOT NULL,
  `trade_type` int(11) NOT NULL,
  `trade_amount` double(12,2) NOT NULL DEFAULT '0.00',
  `trade_ctype` int(11) DEFAULT NULL,
  `trade_ctpvalue` bigint(20) DEFAULT NULL,
  `t_pacct_seq` bigint(20) NOT NULL,
  `t_gacct_seq` bigint(20) NOT NULL,
  `cf_id` decimal(30,0) DEFAULT NULL,
  `trade_desc` varchar(90) DEFAULT '',
  `trxid` bigint(20) DEFAULT NULL,
  `act_pthblc` double(12,2) NOT NULL DEFAULT '0.00',
  `act_gthblc` double(12,2) NOT NULL DEFAULT '0.00',
  `order_edate` datetime DEFAULT NULL,
  `trade_comm` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`t_act_order_seq`),
  UNIQUE KEY `t_act_order_seq` (`t_act_order_seq`),
  KEY `idx_t_act_order_t_pacct_seq` (`t_pacct_seq`),
  KEY `idx_t_act_order_order_stat` (`order_stat`),
  KEY `idx_t_act_order_trade_type` (`trade_type`),
  KEY `idx_t_act_order_t_gacct_seq` (`t_gacct_seq`),
  KEY `idx_t_act_order_trade_ctype` (`trade_ctpvalue`),
  KEY `idx_t_act_order_trade_ctpvalue` (`trade_ctpvalue`)
) ENGINE=InnoDB AUTO_INCREMENT=12902 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_precord`
--

DROP TABLE IF EXISTS `t_act_precord`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_precord` (
  `t_act_precord_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `apr_desc` varchar(60) NOT NULL,
  `apr_name` varchar(20) NOT NULL,
  `apr_pdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `apr_adate` datetime DEFAULT NULL,
  `t_pacct_seq` bigint(20) NOT NULL,
  `t_gacct_seq` bigint(20) NOT NULL,
  `apr_gdate` datetime DEFAULT NULL,
  `apr_stat` int(11) NOT NULL DEFAULT '0',
  `apr_amount` double(12,2) NOT NULL DEFAULT '0.00',
  `apr_type` int(11) NOT NULL,
  `trade_ctpvalue` bigint(20) DEFAULT NULL,
  `cf_id` decimal(30,0) DEFAULT NULL,
  PRIMARY KEY (`t_act_precord_seq`),
  UNIQUE KEY `t_act_precord_seq` (`t_act_precord_seq`),
  KEY `idx_t_act_precord_t_pacct_seq` (`t_pacct_seq`),
  KEY `idx_t_act_precord_t_gacct_seq` (`t_gacct_seq`),
  KEY `idx_t_act_precord_apr_type` (`apr_type`)
) ENGINE=InnoDB AUTO_INCREMENT=405 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_rdrcd`
--

DROP TABLE IF EXISTS `t_act_rdrcd`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_rdrcd` (
  `t_act_rdrcd_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `ard_desc` varchar(60) NOT NULL,
  `ard_rdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `t_acct_seq` bigint(20) NOT NULL,
  `ard_gmoney` double(12,2) NOT NULL DEFAULT '0.00',
  `ard_stat` int(11) NOT NULL DEFAULT '0',
  `ard_type` int(11) NOT NULL DEFAULT '0',
  `ard_tfee` double(12,2) DEFAULT NULL,
  `bank_code` int(11) NOT NULL,
  `abc_num` bigint(20) NOT NULL,
  `ard_dpfee` double(12,2) NOT NULL DEFAULT '0.00',
  `t_tacct_seq` bigint(20) DEFAULT NULL,
  `t_dsacct_seq` bigint(20) DEFAULT NULL,
  `trade_ctpvalue` bigint(20) DEFAULT NULL,
  `cf_id` decimal(30,0) DEFAULT NULL,
  PRIMARY KEY (`t_act_rdrcd_seq`),
  UNIQUE KEY `t_act_rdrcd_seq` (`t_act_rdrcd_seq`),
  KEY `idx_t_act_rdrcd_ard_rdate` (`ard_rdate`),
  KEY `idx_t_act_rdrcd_t_acct_seq` (`t_acct_seq`),
  KEY `idx_t_act_rdrcd_ard_type` (`ard_type`),
  KEY `idx_t_act_rdrcd_bank_code` (`bank_code`),
  KEY `idx_t_act_rdrcd_abc_num` (`abc_num`)
) ENGINE=InnoDB AUTO_INCREMENT=298 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_remind`
--

DROP TABLE IF EXISTS `t_act_remind`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_remind` (
  `t_act_remind_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `t_ropt_seq` bigint(20) NOT NULL,
  `t_acct_seq` bigint(20) NOT NULL,
  `remind_stat` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`t_act_remind_seq`),
  UNIQUE KEY `t_act_remind_seq` (`t_act_remind_seq`),
  KEY `idx_t_act_remind_t_ropt_seq` (`t_ropt_seq`),
  KEY `idx_t_act_remind_t_acct_seq` (`t_acct_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_spot_clm`
--

DROP TABLE IF EXISTS `t_act_spot_clm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_spot_clm` (
  `t_act_spot_clm_seq` bigint(20) NOT NULL,
  `act_clm_pqt` double(12,2) NOT NULL,
  `act_clm_aqt` double(20,10) NOT NULL,
  `act_clm_gpp` int(11) NOT NULL,
  `act_clm_status` int(11) NOT NULL,
  `t_act_clm_seq` int(11) NOT NULL,
  `t_acct_seq` bigint(20) NOT NULL,
  `act_clm_cdate` datetime NOT NULL,
  `act_clm_edate` datetime DEFAULT NULL,
  PRIMARY KEY (`t_act_spot_clm_seq`),
  UNIQUE KEY `t_act_spot_clm_seq` (`t_act_spot_clm_seq`),
  KEY `idx_act_clm_status` (`act_clm_status`),
  KEY `idx_t_act_clm_seq` (`t_act_clm_seq`),
  KEY `idx_act_clm_cdate` (`act_clm_cdate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_spot_order`
--

DROP TABLE IF EXISTS `t_act_spot_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_spot_order` (
  `t_act_spot_order_seq` bigint(20) NOT NULL,
  `trade_date` datetime NOT NULL,
  `order_stat` int(11) NOT NULL,
  `trade_amount` double(12,2) NOT NULL,
  `t_act_clm_seq` bigint(20) NOT NULL,
  `t_pacct_seq` bigint(20) NOT NULL,
  `t_gacct_seq` bigint(20) NOT NULL,
  `order_id` bigint(20) NOT NULL,
  `act_thblc` double(12,2) NOT NULL,
  `trade_desc` varchar(90) NOT NULL,
  `trxid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`t_act_spot_order_seq`),
  UNIQUE KEY `t_act_spot_order_seq` (`t_act_spot_order_seq`),
  KEY `idx_trade_date` (`trade_date`),
  KEY `idx_order_stat` (`order_stat`),
  KEY `idx_t_act_clm_seq` (`t_act_clm_seq`),
  KEY `idx_t_pacct_seq` (`t_pacct_seq`),
  KEY `idx_t_gacct_seq` (`t_gacct_seq`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_act_thblc` (`act_thblc`),
  KEY `idx_trxid` (`trxid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_third`
--

DROP TABLE IF EXISTS `t_act_third`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_third` (
  `t_act_third_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `t_acct_seq` bigint(20) NOT NULL,
  `act_third` varchar(60) NOT NULL,
  `act_thbstat` int(11) NOT NULL DEFAULT '1',
  `act_thastat` int(11) NOT NULL DEFAULT '1',
  `act_thblc` double(12,2) NOT NULL DEFAULT '0.00',
  `act_thtype` int(11) NOT NULL DEFAULT '1',
  `act_thrdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `act_thuid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`t_act_third_seq`),
  UNIQUE KEY `t_act_third_seq` (`t_act_third_seq`),
  UNIQUE KEY `act_third` (`act_third`),
  UNIQUE KEY `idx_t_act_third_t_acct_seq` (`t_acct_seq`),
  KEY `idx_t_act_third_act_third` (`act_third`),
  KEY `idx_t_act_third_act_thrdate` (`act_thrdate`)
) ENGINE=InnoDB AUTO_INCREMENT=715 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_act_wf`
--

DROP TABLE IF EXISTS `t_act_wf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_act_wf` (
  `t_act_wf_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `t_wf_seq` bigint(20) NOT NULL,
  `t_acct_seq` bigint(20) NOT NULL,
  `taw_stat` int(11) NOT NULL,
  `taw_res` int(11) NOT NULL,
  `taw_csu` int(11) DEFAULT NULL,
  `taw_edate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `taw_cdate` datetime DEFAULT NULL,
  PRIMARY KEY (`t_act_wf_seq`),
  UNIQUE KEY `t_act_wf_seq` (`t_act_wf_seq`),
  KEY `idx_t_act_wf_t_wf_seq` (`t_wf_seq`),
  KEY `idx_t_act_wf_t_acct_seq` (`t_acct_seq`),
  KEY `idx_t_act_wf_taw_stat` (`taw_stat`),
  KEY `idx_t_act_wf_taw_res` (`taw_res`),
  KEY `idx_t_act_wf_taw_csu` (`taw_csu`),
  KEY `idx_t_act_wf_taw_cdate` (`taw_cdate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_al_log`
--

DROP TABLE IF EXISTS `t_al_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_al_log` (
  `t_al_log_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `t_acct_seq` bigint(20) NOT NULL,
  `login_ip` varchar(20) NOT NULL,
  `ip_prov` int(11) DEFAULT NULL,
  `ip_city` int(11) DEFAULT NULL,
  `ip_town` int(11) DEFAULT NULL,
  `session_id` varchar(256) NOT NULL,
  PRIMARY KEY (`t_al_log_seq`),
  UNIQUE KEY `t_al_log_seq` (`t_al_log_seq`),
  KEY `idx_t_al_log_t_acct_seq` (`t_acct_seq`),
  KEY `idx_t_al_log_login_ip` (`login_ip`),
  KEY `idx_t_al_log_ip_prov` (`ip_prov`),
  KEY `idx_t_al_log_ip_city` (`ip_city`),
  KEY `idx_t_al_log_ip_town` (`ip_town`)
) ENGINE=InnoDB AUTO_INCREMENT=107469 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_area`
--

DROP TABLE IF EXISTS `t_area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_area` (
  `t_area_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `ta_acode` int(11) NOT NULL,
  `ta_aname` varchar(20) NOT NULL,
  `ta_ccode` int(11) DEFAULT NULL,
  `ta_city` varchar(60) DEFAULT NULL,
  `ta_id` int(11) NOT NULL,
  `ta_level` int(11) NOT NULL,
  `ta_lname` varchar(30) NOT NULL,
  `ta_name` varchar(60) NOT NULL,
  `ta_prcode` int(11) DEFAULT NULL,
  `ta_prov` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`t_area_seq`),
  UNIQUE KEY `t_area_seq` (`t_area_seq`),
  UNIQUE KEY `ta_id` (`ta_id`),
  KEY `idx_t_area_ta_acode` (`ta_acode`),
  KEY `idx_t_area_ta_aname` (`ta_aname`),
  KEY `idx_t_area_ta_city` (`ta_city`),
  KEY `idx_t_area_ta_ccode` (`ta_ccode`),
  KEY `idx_t_area_ta_id` (`ta_id`),
  KEY `idx_t_area_ta_level` (`ta_level`),
  KEY `idx_t_area_ta_prcode` (`ta_prcode`),
  KEY `idx_t_area_ta_name` (`ta_name`),
  KEY `idx_t_area_ta_prov` (`ta_prov`)
) ENGINE=InnoDB AUTO_INCREMENT=7053 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_butt_menu_ref`
--

DROP TABLE IF EXISTS `t_butt_menu_ref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_butt_menu_ref` (
  `t_butt_menu_ref_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `t_menu_seq` bigint(20) NOT NULL,
  `t_button_seq` bigint(20) NOT NULL,
  PRIMARY KEY (`t_butt_menu_ref_seq`),
  UNIQUE KEY `t_butt_menu_ref_seq` (`t_butt_menu_ref_seq`),
  KEY `idx_t_butt_menu_ref_t_menu_seq` (`t_menu_seq`),
  KEY `idx_t_butt_menu_ref_t_button_seq` (`t_button_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_button`
--

DROP TABLE IF EXISTS `t_button`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_button` (
  `t_button_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `button_name` varchar(30) NOT NULL,
  `button_url` varchar(255) NOT NULL,
  `button_code` varchar(30) NOT NULL,
  `t_res_seq` bigint(20) DEFAULT NULL,
  `t_menu_seq` bigint(20) NOT NULL,
  PRIMARY KEY (`t_button_seq`),
  UNIQUE KEY `t_button_seq` (`t_button_seq`),
  KEY `idx_t_button_t_res_seq` (`t_res_seq`),
  KEY `idx_t_button_t_menu_seq` (`t_menu_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_clm_gplan`
--

DROP TABLE IF EXISTS `t_clm_gplan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_clm_gplan` (
  `t_clm_gplan_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `t_act_inv_seq` bigint(20) NOT NULL,
  `clm_gp_num` int(11) NOT NULL,
  `clm_gpppri` double(12,2) NOT NULL DEFAULT '0.00',
  `clm_gppint` double(12,2) NOT NULL DEFAULT '0.00',
  `clm_gppjint` double(12,2) NOT NULL DEFAULT '0.00',
  `clm_gpptotal` double(12,2) NOT NULL DEFAULT '0.00',
  `clm_gpps` double(12,2) NOT NULL DEFAULT '0.00',
  `clm_gp_periods` varchar(20) NOT NULL,
  `t_pacct_seq` bigint(20) NOT NULL,
  `t_gacct_seq` bigint(20) NOT NULL,
  `t_jacct_seq` bigint(20) NOT NULL,
  `clm_gp_cdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `clm_gp_ndate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `clm_gp_stat` int(11) NOT NULL,
  `clm_gpapri` double(20,10) NOT NULL DEFAULT '0.0000000000',
  `clm_gpaint` double(20,10) NOT NULL DEFAULT '0.0000000000',
  `clm_gpajint` double(20,10) NOT NULL DEFAULT '0.0000000000',
  `clm_gpatotal` double(20,10) NOT NULL DEFAULT '0.0000000000',
  `clm_gpas` double(20,10) NOT NULL DEFAULT '0.0000000000',
  `act_clm_amfee` double(20,10) NOT NULL DEFAULT '0.0000000000',
  `act_clm_pmfee` double(12,2) NOT NULL DEFAULT '0.00',
  `t_gsacct_seq` bigint(20) NOT NULL,
  `t_act_order_seq` bigint(20) DEFAULT NULL,
  `rec_edate` datetime DEFAULT NULL,
  PRIMARY KEY (`t_clm_gplan_seq`),
  UNIQUE KEY `t_clm_gplan_seq` (`t_clm_gplan_seq`),
  KEY `idx_t_clm_gplan_t_act_inv_seq` (`t_act_inv_seq`),
  KEY `idx_t_clm_gplan_t_pacct_seq` (`t_pacct_seq`),
  KEY `idx_t_clm_gplan_t_gacct_seq` (`t_gacct_seq`),
  KEY `idx_t_clm_gplan_t_jacct_seq` (`t_jacct_seq`),
  KEY `idx_t_clm_gplan_clm_gp_cdate` (`clm_gp_cdate`),
  KEY `idx_t_clm_gplan_clm_gp_ndate` (`clm_gp_ndate`),
  KEY `idx_t_clm_gplan_clm_gp_stat` (`clm_gp_stat`)
) ENGINE=InnoDB AUTO_INCREMENT=5051 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_clm_grecord`
--

DROP TABLE IF EXISTS `t_clm_grecord`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_clm_grecord` (
  `t_clm_grecord_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `t_act_clm_seq` bigint(20) NOT NULL,
  `clm_gnum` int(11) NOT NULL,
  `clm_gppri` double(12,2) NOT NULL DEFAULT '0.00',
  `clm_gpint` double(12,2) NOT NULL DEFAULT '0.00',
  `clm_gpjint` double(12,2) NOT NULL DEFAULT '0.00',
  `clm_gptotal` double(12,2) NOT NULL DEFAULT '0.00',
  `clm_gps` double(12,2) NOT NULL DEFAULT '0.00',
  `clm_gperiods` varchar(20) NOT NULL,
  `t_pacct_seq` bigint(20) NOT NULL,
  `t_gacct_seq` bigint(20) NOT NULL,
  `clm_gdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `clm_gtype` int(11) DEFAULT NULL,
  `clm_sdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `clm_gapri` double(20,10) NOT NULL DEFAULT '0.0000000000',
  `clm_gaint` double(20,5) NOT NULL DEFAULT '0.00000',
  `clm_gajint` double(20,10) NOT NULL DEFAULT '0.0000000000',
  `clm_gatotal` double(20,10) NOT NULL DEFAULT '0.0000000000',
  `clm_gas` double(20,10) NOT NULL DEFAULT '0.0000000000',
  `act_clm_pmfee` double(12,2) NOT NULL DEFAULT '0.00',
  `act_clm_amfee` double(20,10) NOT NULL DEFAULT '0.0000000000',
  `t_gsacct_seq` bigint(20) DEFAULT NULL,
  `clm_grapri` double(20,10) NOT NULL DEFAULT '0.0000000000',
  `clm_grppri` double(12,2) NOT NULL DEFAULT '0.00',
  `cf_id` decimal(30,0) DEFAULT NULL,
  PRIMARY KEY (`t_clm_grecord_seq`),
  UNIQUE KEY `t_clm_grecord_seq` (`t_clm_grecord_seq`),
  KEY `idx_t_clm_grecord_t_act_clm_seq` (`t_act_clm_seq`),
  KEY `idx_t_clm_grecord_t_pacct_seq` (`t_pacct_seq`),
  KEY `idx_t_clm_grecord_t_gacct_seq` (`t_gacct_seq`),
  KEY `idx_t_clm_grecord_clm_gdate` (`clm_gdate`)
) ENGINE=InnoDB AUTO_INCREMENT=292 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_clm_pp_his`
--

DROP TABLE IF EXISTS `t_clm_pp_his`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_clm_pp_his` (
  `t_clm_pp_his_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `t_clm_pplan_seq` bigint(20) NOT NULL,
  `pstat_cdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `clm_pp_stat` int(11) NOT NULL,
  `his_desc` varchar(90) DEFAULT NULL,
  PRIMARY KEY (`t_clm_pp_his_seq`),
  UNIQUE KEY `t_clm_pp_his_seq` (`t_clm_pp_his_seq`),
  KEY `idx_t_clm_pplan_seq` (`t_clm_pplan_seq`),
  KEY `idx_pstat_cdate` (`pstat_cdate`),
  KEY `idx_clm_pp_stat` (`clm_pp_stat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_clm_pplan`
--

DROP TABLE IF EXISTS `t_clm_pplan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_clm_pplan` (
  `t_clm_pplan_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `t_inv_seq` bigint(20) NOT NULL,
  `clm_pp_num` int(11) NOT NULL,
  `clm_pp_pri` double(12,2) DEFAULT NULL,
  `clm_pp_int` double(12,2) DEFAULT NULL,
  `clm_pp_jint` double(12,2) DEFAULT NULL,
  `clm_pp_tatol` double(12,2) DEFAULT NULL,
  `clm_pp_surplus` double(12,2) NOT NULL DEFAULT '0.00',
  `clm_pp_periods` varchar(20) NOT NULL,
  `t_pacct_seq` bigint(20) NOT NULL,
  `clm_pp_stat` int(11) NOT NULL DEFAULT '0',
  `clm_pp_pmfee` double(12,2) NOT NULL DEFAULT '0.00',
  `clm_pp_ddays` int(11) NOT NULL DEFAULT '0',
  `clm_pp_udays` int(11) NOT NULL DEFAULT '0',
  `clm_pp_sus` double(12,2) NOT NULL DEFAULT '0.00',
  `t_sacct_seq` bigint(20) NOT NULL,
  `t_racct_seq` bigint(20) NOT NULL,
  `clm_pp_rfee` double(12,2) NOT NULL DEFAULT '0.00',
  `rec_cdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `clm_pp_cdate` datetime DEFAULT NULL,
  `clm_pp_ndate` datetime DEFAULT NULL,
  `rec_edate` datetime DEFAULT NULL,
  PRIMARY KEY (`t_clm_pplan_seq`),
  UNIQUE KEY `t_clm_pplan_seq` (`t_clm_pplan_seq`),
  KEY `idx_t_clm_pplan_t_inv_seq` (`t_inv_seq`),
  KEY `idx_t_clm_pplan_t_pacct_seq` (`t_pacct_seq`),
  KEY `idx_t_clm_pplan_clm_pp_stat` (`clm_pp_stat`)
) ENGINE=InnoDB AUTO_INCREMENT=2851 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_clm_precord`
--

DROP TABLE IF EXISTS `t_clm_precord`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_clm_precord` (
  `t_clm_precord_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `t_inv_seq` bigint(20) NOT NULL,
  `clm_num` int(11) NOT NULL,
  `clm_ppri` double(12,2) NOT NULL DEFAULT '0.00',
  `clm_pint` double(12,2) NOT NULL DEFAULT '0.00',
  `clm_pjint` double(12,2) NOT NULL DEFAULT '0.00',
  `clm_ptatol` double(12,2) NOT NULL DEFAULT '0.00',
  `clm_rpri` double(12,2) NOT NULL DEFAULT '0.00',
  `clm_periods` varchar(20) NOT NULL,
  `clm_pdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `clm_ptype` int(11) DEFAULT NULL,
  `clm_sdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `clm_pp_pmfee` double(12,2) NOT NULL DEFAULT '0.00',
  `clm_pp_sus` double(12,2) NOT NULL DEFAULT '0.00',
  `clm_pp_rfee` double(12,2) NOT NULL DEFAULT '0.00',
  `t_sacct_seq` bigint(20) NOT NULL DEFAULT '0',
  `t_racct_seq` bigint(20) NOT NULL DEFAULT '0',
  `t_pacct_seq` bigint(20) DEFAULT NULL,
  `cf_id` decimal(30,0) DEFAULT NULL,
  PRIMARY KEY (`t_clm_precord_seq`),
  UNIQUE KEY `t_clm_precord_seq` (`t_clm_precord_seq`),
  KEY `idx_t_clm_precord_t_inv_seq` (`t_inv_seq`),
  KEY `idx_t_clm_precord_clm_pdate` (`clm_pdate`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_clm_trade`
--

DROP TABLE IF EXISTS `t_clm_trade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_clm_trade` (
  `t_clm_trade_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `t_act_clm_seq` bigint(20) NOT NULL,
  `t_pacct_seq` bigint(20) NOT NULL,
  `t_gacct_seq` bigint(20) NOT NULL,
  `clm_trade_idate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `clm_trade_ddate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `clm_trade_type` int(11) NOT NULL,
  `clm_trade_stat` int(11) NOT NULL DEFAULT '0',
  `clm_tfee` double(12,2) NOT NULL DEFAULT '0.00',
  `t_gsacct_seq` bigint(20) DEFAULT NULL,
  `cf_id` decimal(30,0) DEFAULT NULL,
  PRIMARY KEY (`t_clm_trade_seq`),
  UNIQUE KEY `t_clm_trade_seq` (`t_clm_trade_seq`),
  KEY `idx_t_clm_trade_t_res_seq` (`t_act_clm_seq`),
  KEY `idx_t_clm_trade_t_pacct_seq` (`t_pacct_seq`),
  KEY `idx_t_clm_trade_t_gacct_seq` (`t_gacct_seq`),
  KEY `idx_t_clm_trade_clm_trade_idate` (`clm_trade_idate`),
  KEY `idx_t_clm_trade_clm_trade_type` (`clm_trade_type`),
  KEY `idx_t_clm_trade_clm_trade_stat` (`clm_trade_stat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_dic`
--

DROP TABLE IF EXISTS `t_dic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_dic` (
  `t_dic_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `tab_name` varchar(30) NOT NULL,
  `col_name` varchar(30) NOT NULL,
  `col_value` int(11) DEFAULT NULL,
  `tab_cn` varchar(60) NOT NULL,
  `col_cn` varchar(20) NOT NULL,
  `col_cvcn` varchar(20) DEFAULT NULL,
  `rec_cdate` datetime DEFAULT CURRENT_TIMESTAMP,
  `rec_status` int(11) NOT NULL DEFAULT '0',
  `rec_edate` datetime DEFAULT NULL,
  `col_bcvcn` varchar(20) NOT NULL,
  `col_fcvcn` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`t_dic_seq`),
  UNIQUE KEY `t_dic_seq` (`t_dic_seq`),
  KEY `idx_t_dic_tab_name` (`tab_name`),
  KEY `idx_t_dic_col_name` (`col_name`),
  KEY `idx_t_dic_col_value` (`col_value`),
  KEY `idx_t_dic_rec_status` (`rec_status`),
  KEY `idx_t_dic_col_cvcn` (`col_cvcn`)
) ENGINE=InnoDB AUTO_INCREMENT=10052 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_inv`
--

DROP TABLE IF EXISTS `t_inv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_inv` (
  `t_inv_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `inv_num` varchar(60) NOT NULL DEFAULT '',
  `inv_stype` int(11) NOT NULL DEFAULT '1',
  `t_res_sseq` bigint(20) NOT NULL,
  `t_res_lseq` bigint(20) NOT NULL,
  `inv_grt` int(11) NOT NULL DEFAULT '1',
  `inv_irate` int(11) NOT NULL DEFAULT '0',
  `inv_period` int(11) NOT NULL,
  `inv_total` double(12,2) NOT NULL DEFAULT '0.00',
  `inv_rem` double(12,2) NOT NULL DEFAULT '0.00',
  `inv_jcount` int(11) NOT NULL DEFAULT '0',
  `inv_process` double(12,4) DEFAULT NULL,
  `inv_sdate` datetime DEFAULT NULL,
  `inv_edate` datetime DEFAULT NULL,
  `inv_cdate` datetime NOT NULL,
  `inv_ptype` int(11) NOT NULL,
  `inv_stat` int(11) NOT NULL DEFAULT '1',
  `t_res_seq` bigint(20) NOT NULL,
  `inv_smount` double(12,2) DEFAULT NULL,
  `inv_ldate` datetime DEFAULT NULL,
  `inv_fdate` datetime DEFAULT NULL,
  `inv_cscore` int(11) NOT NULL DEFAULT '0',
  `inv_type` int(11) NOT NULL DEFAULT '0',
  `inv_target` int(11) NOT NULL DEFAULT '1',
  `inv_odate` datetime DEFAULT NULL,
  `inv_name` varchar(90) DEFAULT NULL,
  `inv_eform` int(11) DEFAULT NULL,
  `inv_lv` int(11) DEFAULT NULL,
  `inv_cre_date` datetime DEFAULT NULL,
  `inv_pcount` int(11) DEFAULT NULL,
  `inv_gtype` int(11) NOT NULL DEFAULT '1',
  `lc_ptype` int(11) NOT NULL,
  `inv_idate` datetime DEFAULT NULL,
  PRIMARY KEY (`t_inv_seq`),
  UNIQUE KEY `t_inv_seq` (`t_inv_seq`),
  KEY `idx_t_inv_inv_sdate` (`inv_sdate`),
  KEY `idx_t_inv_inv_edate` (`inv_edate`),
  KEY `idx_t_inv_inv_cdate` (`inv_cdate`),
  KEY `idx_t_inv_inv_stat` (`inv_stat`),
  KEY `idx_t_inv_inv_ldate` (`inv_ldate`),
  KEY `idx_t_inv_inv_fdate` (`inv_fdate`),
  KEY `idx_t_inv_inv_cscore` (`inv_cscore`),
  KEY `idx_t_inv_inv_type` (`inv_type`),
  KEY `idx_t_inv_inv_target` (`inv_target`),
  KEY `idx_t_inv_act_ref_t_inv_seq` (`t_inv_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=188 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_inv_act_ref`
--

DROP TABLE IF EXISTS `t_inv_act_ref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_inv_act_ref` (
  `t_inv_act_ref_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `t_inv_seq` bigint(20) NOT NULL,
  `t_acct_seq` bigint(20) NOT NULL,
  `t_loan_data_seq` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`t_inv_act_ref_seq`),
  UNIQUE KEY `t_inv_act_ref_seq` (`t_inv_act_ref_seq`),
  UNIQUE KEY `union_cons` (`t_acct_seq`,`t_inv_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=173 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_isp_clock`
--

DROP TABLE IF EXISTS `t_isp_clock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_isp_clock` (
  `t_isp_clock_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `isp_clock` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `isp_clock_desc` varchar(60) NOT NULL,
  PRIMARY KEY (`t_isp_clock_seq`),
  UNIQUE KEY `t_isp_clock_seq` (`t_isp_clock_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_loan_cut`
--

DROP TABLE IF EXISTS `t_loan_cut`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_loan_cut` (
  `t_loan_cut_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `buss_code` varchar(60) NOT NULL DEFAULT '',
  `inv_src` int(11) NOT NULL DEFAULT '1',
  `total_sget` double(12,2) NOT NULL DEFAULT '0.00',
  `total_aget` double(12,2) NOT NULL DEFAULT '0.00',
  `loan_period` int(11) NOT NULL,
  `rec_idate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `rec_odate` datetime DEFAULT NULL,
  `cut_sdate` datetime NOT NULL,
  `cut_adate` datetime DEFAULT NULL,
  `rec_stat` int(11) NOT NULL DEFAULT '1',
  `bill_type` int(11) NOT NULL,
  `pro_stat` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`t_loan_cut_seq`),
  UNIQUE KEY `t_loan_cut_seq` (`t_loan_cut_seq`),
  KEY `idx_buss_code` (`buss_code`),
  KEY `idx_inv_src` (`inv_src`)
) ENGINE=InnoDB AUTO_INCREMENT=14005 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_loan_data`
--

DROP TABLE IF EXISTS `t_loan_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_loan_data` (
  `t_loan_data_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `tac_mobile` varchar(11) DEFAULT NULL,
  `tac_email` varchar(60) DEFAULT NULL,
  `tac_name` varchar(30) DEFAULT NULL,
  `tac_pid` char(18) DEFAULT NULL,
  `tac_inds` varchar(30) DEFAULT NULL,
  `tac_edu_degree` varchar(30) DEFAULT NULL,
  `tac_income` varchar(30) DEFAULT NULL,
  `tac_wprov` varchar(30) DEFAULT NULL,
  `tac_wcity` varchar(30) DEFAULT NULL,
  `tac_wtown` varchar(30) DEFAULT NULL,
  `tac_mstat` varchar(30) DEFAULT NULL,
  `tac_hprov` varchar(30) DEFAULT NULL,
  `tac_hcity` varchar(30) DEFAULT NULL,
  `tac_htown` varchar(30) DEFAULT NULL,
  `tac_house` varchar(30) DEFAULT NULL,
  `tac_cinds` varchar(30) DEFAULT NULL,
  `tac_csize` varchar(30) DEFAULT NULL,
  `tac_cpt` varchar(30) DEFAULT NULL,
  `tac_depart` varchar(30) DEFAULT NULL,
  `tac_position` varchar(30) DEFAULT NULL,
  `tac_sdate` varchar(30) DEFAULT NULL,
  `tac_cscore` int(11) DEFAULT NULL,
  `tac_tlv` varchar(30) DEFAULT NULL,
  `tac_ltype` varchar(30) DEFAULT NULL,
  `tac_house_loan` varchar(30) DEFAULT NULL,
  `tac_addr` varchar(300) DEFAULT NULL,
  `buss_code` varchar(60) DEFAULT NULL,
  `inv_stype` varchar(30) DEFAULT NULL,
  `inv_irate` varchar(30) DEFAULT NULL,
  `inv_period` int(11) DEFAULT NULL,
  `inv_ptype` varchar(30) DEFAULT NULL,
  `inv_target` varchar(30) DEFAULT NULL,
  `inv_cscore` int(11) DEFAULT NULL,
  `bank_code` varchar(30) DEFAULT NULL,
  `abc_num` varchar(30) DEFAULT NULL,
  `abc_prov` varchar(30) DEFAULT NULL,
  `abc_city` varchar(30) DEFAULT NULL,
  `abc_town` varchar(30) DEFAULT NULL,
  `rec_idate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `rec_odate` datetime DEFAULT NULL,
  `tac_laddr` varchar(300) DEFAULT NULL,
  `tac_haddr` varchar(300) DEFAULT NULL,
  `tac_caddr` varchar(300) DEFAULT NULL,
  `abc_name` varchar(90) DEFAULT NULL,
  `inv_mrate` varchar(10) DEFAULT NULL,
  `act_lmount` double(10,2) DEFAULT NULL,
  `p2p_sfrate` varchar(10) DEFAULT NULL,
  `tac_wtime` varchar(20) DEFAULT NULL,
  `tac_gender` varchar(10) DEFAULT NULL,
  `rec_stat` int(11) DEFAULT NULL,
  `em_lv` varchar(20) DEFAULT NULL,
  `p2p_epay_frate` double(20,10) NOT NULL DEFAULT '0.0000000000',
  `p2p_pay_frate` double(20,10) NOT NULL DEFAULT '0.0000000000',
  `p2p_oper_frate` double(20,10) NOT NULL DEFAULT '0.0000000000',
  `sign_date` datetime NOT NULL,
  `pro_stat` int(11) NOT NULL,
  `inv_src` int(11) DEFAULT NULL,
  `cont_id` varchar(60) NOT NULL,
  `inv_type` int(11) NOT NULL DEFAULT '1',
  `lc_ptype` int(11) NOT NULL,
  `prd_type` varchar(60) NOT NULL DEFAULT '缺失',
  `ss_stat` int(11) NOT NULL DEFAULT '1',
  `sync_desc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`t_loan_data_seq`),
  UNIQUE KEY `t_loan_data_seq` (`t_loan_data_seq`),
  UNIQUE KEY `unq_bc_src` (`buss_code`),
  KEY `idx_loan_data_tac_pid` (`tac_pid`),
  KEY `idx_p2p_epay_frate` (`p2p_epay_frate`),
  KEY `idx_p2p_pay_frate` (`p2p_pay_frate`),
  KEY `idx_p2p_oper_frate` (`p2p_oper_frate`)
) ENGINE=InnoDB AUTO_INCREMENT=4533 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`10.100.12.46`*/ /*!50003 trigger tri_refuse_loan_data_del
before delete on p2p.t_loan_data
for each row
begin
insert into del_refused values(1);
end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `t_loan_gain`
--

DROP TABLE IF EXISTS `t_loan_gain`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_loan_gain` (
  `t_loan_gain_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `t_inv_seq` bigint(20) NOT NULL,
  `act_gthird` varchar(60) NOT NULL,
  `act_pthird` varchar(60) NOT NULL,
  `lg_pamount` double(12,2) NOT NULL DEFAULT '0.00',
  `lg_tamount` double(12,2) NOT NULL DEFAULT '0.00',
  `lg_pdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lg_tdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lg_stat` int(11) NOT NULL DEFAULT '0',
  `lg_sdesc` varchar(60) DEFAULT NULL,
  `lg_gfee` double(12,2) DEFAULT NULL,
  PRIMARY KEY (`t_loan_gain_seq`),
  UNIQUE KEY `t_loan_gain_seq` (`t_loan_gain_seq`),
  KEY `idx_t_loan_gain_t_inv_seq` (`t_inv_seq`),
  KEY `idx_t_loan_gain_act_gthird` (`act_gthird`),
  KEY `idx_t_loan_gain_act_pthird` (`act_pthird`),
  KEY `idx_t_loan_gain_lg_pdate` (`lg_pdate`),
  KEY `idx_t_loan_gain_lg_tdate` (`lg_tdate`),
  KEY `idx_t_loan_gain_lg_stat` (`lg_stat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_loan_trans`
--

DROP TABLE IF EXISTS `t_loan_trans`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_loan_trans` (
  `t_loan_trans_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `tab_name` varchar(30) NOT NULL,
  `col_name` varchar(30) NOT NULL,
  `col_value` int(11) NOT NULL,
  `src_data` varchar(60) NOT NULL,
  UNIQUE KEY `t_loan_trans_seq` (`t_loan_trans_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_menu`
--

DROP TABLE IF EXISTS `t_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_menu` (
  `t_menu_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `menu_name` varchar(60) NOT NULL,
  `menu_url` varchar(255) NOT NULL,
  `menu_code` varchar(30) NOT NULL,
  `menu_type` int(11) DEFAULT NULL,
  `t_res_seq` bigint(20) DEFAULT NULL,
  `t_res_pseq` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`t_menu_seq`),
  UNIQUE KEY `t_menu_seq` (`t_menu_seq`),
  KEY `idx_t_menu_menu_type` (`menu_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_msg`
--

DROP TABLE IF EXISTS `t_msg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_msg` (
  `t_msg_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `msg_type` int(11) NOT NULL DEFAULT '1',
  `msg_title` varchar(60) NOT NULL,
  `msg_content` varchar(2000) NOT NULL,
  `msg_stats` int(11) NOT NULL DEFAULT '1',
  `msg_cdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `msg_sdate` datetime DEFAULT NULL,
  `msg_itype` int(11) NOT NULL DEFAULT '1',
  `t_res_seq` bigint(20) DEFAULT NULL,
  `msg_idx` int(11) DEFAULT NULL,
  `msg_ddate` datetime DEFAULT NULL,
  `username` varchar(30) NOT NULL DEFAULT '无',
  PRIMARY KEY (`t_msg_seq`),
  UNIQUE KEY `t_msg_seq` (`t_msg_seq`),
  KEY `idx_t_msg_msg_cdate` (`msg_cdate`),
  KEY `idx_t_msg_msg_sdate` (`msg_sdate`),
  KEY `idx_t_msg_msg_itype` (`msg_itype`),
  KEY `idx_t_msg_t_res_seq` (`t_res_seq`),
  KEY `idx_t_msg_type_title` (`msg_type`,`msg_title`),
  KEY `idx_t_msg_msg_idx` (`msg_idx`)
) ENGINE=InnoDB AUTO_INCREMENT=102834 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_nb_quota`
--

DROP TABLE IF EXISTS `t_nb_quota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_nb_quota` (
  `t_nb_quota_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `nb_id` int(11) NOT NULL,
  `nb_ptype` int(11) NOT NULL,
  `nb_quota` double(12,2) DEFAULT NULL,
  `nb_ctype` int(11) NOT NULL,
  `t_res_seq` bigint(20) NOT NULL,
  `nb_qdesc` varchar(255) NOT NULL,
  PRIMARY KEY (`t_nb_quota_seq`),
  UNIQUE KEY `t_nb_quota_seq` (`t_nb_quota_seq`),
  KEY `idx_t_nb_quota_nb_id` (`nb_id`),
  KEY `idx_t_nb_quota_nb_ptype` (`nb_ptype`),
  KEY `idx_t_nb_quota_nb_ctype` (`nb_ctype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_op`
--

DROP TABLE IF EXISTS `t_op`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_op` (
  `t_op_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `buss_id` varchar(60) NOT NULL,
  `op_src` int(11) NOT NULL,
  `op_name` varchar(30) NOT NULL,
  `op_passwd` varchar(30) NOT NULL,
  `op_cdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `op_status` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`t_op_seq`),
  UNIQUE KEY `t_op_seq` (`t_op_seq`),
  KEY `idx_t_op_buss_id` (`buss_id`),
  KEY `idx_t_op_op_src` (`op_src`),
  KEY `idx_t_op_op_name` (`op_name`),
  KEY `idx_t_op_op_status` (`op_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_oplog`
--

DROP TABLE IF EXISTS `t_oplog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_oplog` (
  `t_oplog_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `t_op_seq` bigint(20) DEFAULT NULL,
  `sql_text` varchar(2000) DEFAULT NULL,
  `op_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `op_type` int(11) NOT NULL,
  `t_acct_seq` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`t_oplog_seq`),
  UNIQUE KEY `t_oplog_seq` (`t_oplog_seq`),
  KEY `idx_t_oplog_t_op_seq` (`t_op_seq`),
  KEY `idx_t_oplog_op_type` (`op_type`),
  KEY `idx_t_oplog_t_acct_seq` (`t_acct_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=1635 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_phoes`
--

DROP TABLE IF EXISTS `t_phoes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_phoes` (
  `t_phoes_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `real_phos` varchar(20) NOT NULL,
  `fake_phos` varchar(20) NOT NULL,
  PRIMARY KEY (`t_phoes_seq`),
  UNIQUE KEY `t_phoes_seq` (`t_phoes_seq`),
  KEY `idx_real_phos` (`real_phos`),
  KEY `idx_fake_phos` (`fake_phos`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_rec_record`
--

DROP TABLE IF EXISTS `t_rec_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_rec_record` (
  `t_rec_record_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `t_acct_seq` bigint(20) NOT NULL,
  `rec_frd_id` varchar(60) NOT NULL,
  `rec_frd_type` int(11) NOT NULL,
  `rec_frd_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `rec_frd_stat` int(11) NOT NULL DEFAULT '0',
  `rec_frd_url` varchar(255) NOT NULL,
  PRIMARY KEY (`t_rec_record_seq`),
  UNIQUE KEY `t_rec_record_seq` (`t_rec_record_seq`),
  KEY `idx_t_rec_record_t_acct_seq` (`t_acct_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=102685 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_rep_log`
--

DROP TABLE IF EXISTS `t_rep_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_rep_log` (
  `t_rep_log_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `rep_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `rep_type` varchar(60) NOT NULL,
  `rep_code` varchar(60) DEFAULT NULL,
  `rep_desc` varchar(255) DEFAULT NULL,
  `rep_key` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`t_rep_log_seq`),
  UNIQUE KEY `t_rep_log_seq` (`t_rep_log_seq`),
  KEY `idx_rep_date` (`rep_date`),
  KEY `idx_rep_type` (`rep_type`)
) ENGINE=InnoDB AUTO_INCREMENT=16460 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_req_log`
--

DROP TABLE IF EXISTS `t_req_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_req_log` (
  `t_req_log_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_id` varchar(90) DEFAULT NULL,
  `req_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `req_type` varchar(60) NOT NULL,
  `req_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`t_req_log_seq`),
  UNIQUE KEY `t_req_log_seq` (`t_req_log_seq`),
  KEY `idx_session_id` (`session_id`),
  KEY `idx_req_date` (`req_date`),
  KEY `idx_req_type` (`req_type`)
) ENGINE=InnoDB AUTO_INCREMENT=8589 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_res`
--

DROP TABLE IF EXISTS `t_res`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_res` (
  `t_res_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `res_name` varchar(60) NOT NULL,
  `res_path` varchar(255) NOT NULL,
  `res_type` int(11) NOT NULL DEFAULT '1',
  `res_desc` varchar(60) DEFAULT NULL,
  `res_cdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`t_res_seq`),
  UNIQUE KEY `t_res_seq` (`t_res_seq`),
  KEY `idx_t_res_res_cdate` (`res_cdate`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_role`
--

DROP TABLE IF EXISTS `t_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_role` (
  `t_role_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(30) NOT NULL,
  `role_desc` varchar(60) NOT NULL,
  `role_cdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`t_role_seq`),
  UNIQUE KEY `t_role_seq` (`t_role_seq`),
  KEY `idx_t_role_role_name` (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_role_func_ref`
--

DROP TABLE IF EXISTS `t_role_func_ref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_role_func_ref` (
  `t_role_func_ref_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `t_role_seq` bigint(20) NOT NULL,
  `t_button_seq` bigint(20) NOT NULL,
  `rbr_cdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `t_menu_seq` bigint(20) NOT NULL,
  PRIMARY KEY (`t_role_func_ref_seq`),
  UNIQUE KEY `t_role_func_ref_seq` (`t_role_func_ref_seq`),
  KEY `idx_t_role_func_ref_t_role_seq` (`t_role_seq`),
  KEY `idx_t_role_func_ref_t_button_seq` (`t_button_seq`),
  KEY `idx_t_role_func_ref_t_menu_seq` (`t_menu_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_role_opt_ref`
--

DROP TABLE IF EXISTS `t_role_opt_ref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_role_opt_ref` (
  `t_role_opt_ref_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `t_role_seq` bigint(20) NOT NULL,
  `t_op_seq` bigint(20) NOT NULL,
  `ror_cdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`t_role_opt_ref_seq`),
  UNIQUE KEY `t_role_opt_ref_seq` (`t_role_opt_ref_seq`),
  KEY `idx_t_role_opt_ref_t_role_seq` (`t_role_seq`),
  KEY `idx_t_role_opt_ref_t_op_seq` (`t_op_seq`),
  KEY `idx_t_role_opt_ref_ror_cdate` (`ror_cdate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_ropt`
--

DROP TABLE IF EXISTS `t_ropt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_ropt` (
  `t_ropt_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `ropt_type` int(11) NOT NULL DEFAULT '1',
  `ropt_form` int(11) NOT NULL,
  PRIMARY KEY (`t_ropt_seq`),
  UNIQUE KEY `t_ropt_seq` (`t_ropt_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `t_sqsa`
--

DROP TABLE IF EXISTS `t_sqsa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_sqsa` (
  `t_sqsa_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `t_sqs_seq` bigint(20) NOT NULL,
  `t_acct_seq` bigint(20) NOT NULL,
  `tac_asnw` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`t_sqsa_seq`),
  UNIQUE KEY `t_sqsa_seq` (`t_sqsa_seq`),
  KEY `idx_t_sqsa_t_sqs_seq` (`t_sqs_seq`),
  KEY `idx_t_sqsa_t_acct_seq` (`t_acct_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=107 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_welfare`
--

DROP TABLE IF EXISTS `t_welfare`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_welfare` (
  `t_welfare_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `wf_name` varchar(30) NOT NULL,
  `wf_desc` varchar(60) NOT NULL,
  `wf_quota` double(12,2) DEFAULT NULL,
  `wf_edate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `t_res_seq` bigint(20) NOT NULL,
  `wf_type` int(11) NOT NULL DEFAULT '1',
  `wf_cdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `wf_total` int(11) DEFAULT NULL,
  `wf_cc` int(11) DEFAULT NULL,
  `wf_acond` varchar(90) DEFAULT NULL,
  PRIMARY KEY (`t_welfare_seq`),
  UNIQUE KEY `t_welfare_seq` (`t_welfare_seq`),
  KEY `idx_t_welfare_wf_edate` (`wf_edate`),
  KEY `idx_t_welfare_wf_cdate` (`wf_cdate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) DEFAULT NULL,
  `role_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_roles` (`username`,`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`RJAdmin`@`10.100.9.85`*/ /*!50003 trigger tri_sync_user_roles_add
after insert on p2p.user_roles
for each row
begin
delete from p2pbms.user_roles;
insert into p2pbms.user_roles select * from p2p.user_roles;
end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`RJAdmin`@`10.100.9.85`*/ /*!50003 trigger tri_sync_user_roles_edt
after update on p2p.user_roles
for each row
begin
delete from p2pbms.user_roles;
insert into p2pbms.user_roles select * from p2p.user_roles;
end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`RJAdmin`@`10.100.9.85`*/ /*!50003 trigger tri_sync_user_roles_del
after delete on p2p.user_roles
for each row
begin
delete from p2pbms.user_roles;
insert into p2pbms.user_roles select * from p2p.user_roles;
end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `userconnection`
--

DROP TABLE IF EXISTS `userconnection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userconnection` (
  `userId` varchar(255) NOT NULL,
  `providerId` varchar(255) NOT NULL,
  `providerUserId` varchar(255) NOT NULL DEFAULT '',
  `rank` int(11) NOT NULL,
  `displayName` varchar(255) DEFAULT NULL,
  `profileUrl` varchar(512) DEFAULT NULL,
  `imageUrl` varchar(512) DEFAULT NULL,
  `accessToken` varchar(255) NOT NULL,
  `secret` varchar(255) DEFAULT NULL,
  `refreshToken` varchar(255) DEFAULT NULL,
  `expireTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`userId`,`providerId`,`providerUserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `username` varchar(255) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users_credential_authorities`
--

DROP TABLE IF EXISTS `users_credential_authorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users_credential_authorities` (
  `users_username` varchar(255) NOT NULL,
  `credential_authorities_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_1chckv3h9amh1ytdd0bsm3co7` (`credential_authorities_id`),
  KEY `FK_tkc4u2nq6ajoglwcooa5d2kt3` (`users_username`),
  CONSTRAINT `FK_1chckv3h9amh1ytdd0bsm3co7` FOREIGN KEY (`credential_authorities_id`) REFERENCES `authorities` (`id`),
  CONSTRAINT `FK_tkc4u2nq6ajoglwcooa5d2kt3` FOREIGN KEY (`users_username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `wicket_data`
--

DROP TABLE IF EXISTS `wicket_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wicket_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user` varchar(255) DEFAULT NULL,
  `s1` varchar(255) DEFAULT NULL,
  `s2` varchar(255) DEFAULT NULL,
  `s3` varchar(255) DEFAULT NULL,
  `s4` varchar(255) DEFAULT NULL,
  `s5` varchar(255) DEFAULT NULL,
  `s6` varchar(255) DEFAULT NULL,
  `s7` varchar(255) DEFAULT NULL,
  `s8` varchar(255) DEFAULT NULL,
  `s9` varchar(255) DEFAULT NULL,
  `s10` varchar(255) DEFAULT NULL,
  `s11` varchar(255) DEFAULT NULL,
  `s12` varchar(255) DEFAULT NULL,
  `s13` varchar(255) DEFAULT NULL,
  `s14` varchar(255) DEFAULT NULL,
  `s15` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `d1` date DEFAULT NULL,
  `d2` date DEFAULT NULL,
  `d3` date DEFAULT NULL,
  `d4` date DEFAULT NULL,
  `d5` date DEFAULT NULL,
  `d6` date DEFAULT NULL,
  `i1` int(11) DEFAULT NULL,
  `i2` int(11) DEFAULT NULL,
  `i3` int(11) DEFAULT NULL,
  `i4` int(11) DEFAULT NULL,
  `i6` int(11) DEFAULT NULL,
  `i5` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `wicket_data_copy`
--

DROP TABLE IF EXISTS `wicket_data_copy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wicket_data_copy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user` varchar(255) DEFAULT NULL,
  `s1` varchar(255) DEFAULT NULL,
  `s2` varchar(255) DEFAULT NULL,
  `s3` varchar(255) DEFAULT NULL,
  `s4` varchar(255) DEFAULT NULL,
  `s5` varchar(255) DEFAULT NULL,
  `s6` varchar(255) DEFAULT NULL,
  `s7` varchar(255) DEFAULT NULL,
  `s8` varchar(255) DEFAULT NULL,
  `s9` varchar(255) DEFAULT NULL,
  `s10` varchar(255) DEFAULT NULL,
  `s11` varchar(255) DEFAULT NULL,
  `s12` varchar(255) DEFAULT NULL,
  `s13` varchar(255) DEFAULT NULL,
  `s14` varchar(255) DEFAULT NULL,
  `s15` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `d1` date DEFAULT NULL,
  `d2` date DEFAULT NULL,
  `d3` date DEFAULT NULL,
  `d4` date DEFAULT NULL,
  `d5` date DEFAULT NULL,
  `d6` date DEFAULT NULL,
  `i1` int(11) DEFAULT NULL,
  `i2` int(11) DEFAULT NULL,
  `i3` int(11) DEFAULT NULL,
  `i4` int(11) DEFAULT NULL,
  `i6` int(11) DEFAULT NULL,
  `i5` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'p2p'
--
/*!50003 DROP PROCEDURE IF EXISTS `proc_q1` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`JAdmin`@`%` PROCEDURE `proc_q1`(
in v_t_inv_seq bigint
)
begin
declare
v_stmt varchar(2000);
 set v_stmt='select d.cont_id,b.tac_pid,b.tac_name,c.inv_total,c.inv_total*f.col_cvcn,c.inv_total*(d.p2p_pay_frate-g.col_cvcn),c.inv_total,c.inv_total-c.inv_total*f.col_cvcn-c.inv_total*(d.p2p_pay_frate-g.col_cvcn),h.trade_date,h.order_stat,i.trade_desc,c.inv_rem,e.col_cvcn-c.inv_pcount,date_format(c.inv_fdate,''%d'') as inv_sdate,c.inv_total/e.col_cvcn,c.inv_pcount,j.ccm,c.inv_odate,k.clm_pp_surplus,l.col_cvcn from t_inv_act_ref a join t_inv c on c.t_inv_seq=a.t_inv_seq';
 if v_t_inv_seq is not null then
 set v_stmt=concat(v_stmt,' and a.t_inv_seq=',v_t_inv_seq);
 end if;
 set v_stmt=concat(v_stmt,' left join (select t_inv_seq,sum(clm_pp_pri+clm_pp_int) as clm_pp_surplus from t_clm_pplan where clm_pp_stat<>4 group by t_inv_seq) k on a.t_inv_seq=k.t_inv_seq join t_acct b on a.t_acct_seq=b.t_acct_seq join t_loan_data d on a.t_loan_data_seq=d.t_loan_data_seq join t_dic e on e.tab_name=''t_inv'' and e.col_name=''inv_period'' and e.col_value=c.inv_period join (select a1.col_cvcn from t_dic a1 where a1.tab_name=''t_dic'' and a1.col_name=''claim_pay_rrate'' and a1.col_value=1) f join (select a1.col_cvcn from t_dic a1 where a1.tab_name=''t_dic'' and a1.col_name=''p2p_pay_frate'' and a1.col_value=1) g left join t_act_order h on a.t_inv_seq=h.trade_ctpvalue and h.trade_type=25 and h.order_stat =3 left join (select a1.trade_ctpvalue,b1.trade_desc from t_act_order a1,t_act_oh b1 where a1.t_act_order_seq=b1.t_act_order_seq and b1.order_stat=4 order by t_act_oh_seq desc limit 1) i on a.t_inv_seq=i.trade_ctpvalue left join (select count(cm) as ccm,t_inv_seq from (select t_inv_seq,extract(year_month from (date_add(act_inv_cdate,interval -cast(@rownum:=@rownum+1 as char(9)) month))) as cm from t_act_inv e,(select @rownum:=0) f where extract(year_month from (date_add(act_inv_cdate,interval -cast(@rownum:=@rownum+1 as char(9)) month))) is not null and e.act_inv_gform=5) o group by o.t_inv_seq) j on j.t_inv_seq=a.t_inv_seq join t_dic l on l.tab_name=''t_loan_data'' and l.col_name=''lc_ptype'' and l.col_value=d.lc_ptype');
 set @v_sql=v_stmt;
 prepare stmt from @v_sql;
 EXECUTE stmt;
 deallocate prepare stmt;
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `proc_q2` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`JAdmin`@`%` PROCEDURE `proc_q2`(in v_t_acct_seq bigint,in v_req_type int)
begin
declare
v_stmt varchar(2000);
 if v_t_acct_seq is not null and v_req_type = 1 then
 set v_stmt=concat('select count(distinct b.trade_ctpvalue) from t_acct a,t_act_order b',' where a.t_acct_seq=',v_t_acct_seq,' and a.t_acct_seq=b.t_pacct_seq and b.trade_type in (1,20) and b.order_stat=7 group by b.t_pacct_seq');
 end if;
  if v_t_acct_seq is not null and v_req_type = 2 then
 set v_stmt=concat('select sum(b.trade_amount) from t_acct a,t_act_order b',' where a.t_acct_seq=',v_t_acct_seq,' and a.t_acct_seq=b.t_pacct_seq and b.trade_type in (1,20) and b.order_stat=7 group by b.t_pacct_seq');
 end if;
 set @v_sql=v_stmt;
 prepare stmt from @v_sql;
 EXECUTE stmt;
 deallocate prepare stmt;
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `proc_q3` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`JAdmin`@`%` PROCEDURE `proc_q3`(
in v_tac_pid varchar(18),
in v_tac_name varchar(30),
in v_tac_uname varchar(30),
in v_act_thblc double,
in v_trade_amount double,
in v_act_athblc double
)
begin
declare
v_stmt varchar(2000);
 set v_stmt='select e.t_acct_seq from (select a.t_acct_seq from t_acct a where 1=1';
 if v_tac_pid is not null then
 set v_stmt=concat(v_stmt,' and a.tac_pid=''',v_tac_pid,'''');
 end if;
 if v_tac_name is not null then
 set v_stmt=concat(v_stmt,' and a.tac_name=''',v_tac_name,'''');
 end if;
 if v_tac_uname is not null then
 set v_stmt=concat(v_stmt,' and a.tac_uname=''',v_tac_uname,'''');
 end if;
 set v_stmt=concat(v_stmt,' ) e ');
 if v_act_thblc is not null then
 set v_stmt=concat(v_stmt,' join t_act_third b on e.t_acct_seq=b.t_acct_seq and b.act_thblc=',v_act_thblc);
 end if;
 if v_trade_amount is not null and v_act_athblc is null then
 set v_stmt=concat(v_stmt,' join (select c.t_pacct_seq,sum(c.trade_amount) as strade_amount from t_act_order c where c.trade_type=23 and c.order_stat=7 group by c.t_pacct_seq) d on e.t_acct_seq=d.t_pacct_seq and d.strade_amount=',v_trade_amount);
 end if;
 if v_trade_amount is null and v_act_athblc is not null then
 set v_stmt=concat(v_stmt,' join (select c.t_pacct_seq,sum(c.trade_amount) as strade_amount from t_act_order c where c.trade_type=23 and c.order_stat=7 group by c.t_pacct_seq) d on e.t_acct_seq=d.t_pacct_seq and b.act_thblc-d.strade_amount=',v_act_athblc);
 end if;
 if v_trade_amount is not null and v_act_athblc is not null then
 set v_stmt=concat(v_stmt,' join (select c.t_pacct_seq,sum(c.trade_amount) as strade_amount from t_act_order c where c.trade_type=23 and c.order_stat=7 group by c.t_pacct_seq) d on e.t_acct_seq=d.t_pacct_seq and b.act_thblc-d.strade_amount=',v_act_athblc,' and d.strade_amount=',v_trade_amount);
 end if;    
 set @v_sql=v_stmt;
 prepare stmt from @v_sql;
 EXECUTE stmt;
 deallocate prepare stmt;
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `proc_q4` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`JAdmin`@`%` PROCEDURE `proc_q4`(
in v_date datetime,
in v_type int
)
begin
declare
v_stmt varchar(2000);
 if v_type=1 then
 set v_stmt=concat('select distinct a.t_clm_pplan_seq from t_clm_pplan a,t_loan_cut b,t_loan_data c,t_inv_act_ref d where b.buss_code=c.buss_code and b.inv_src=c.inv_src and d.t_loan_data_seq=c.t_loan_data_seq and a.t_inv_seq=d.t_inv_seq and a.clm_pp_stat in (4,6,7,8) and b.total_aget is not null and b.loan_period=a.clm_pp_num and clm_pp_ddays =0 and date_format(a.clm_pp_ndate,''%Y%c%d'')=date_format(''',v_date,''',''%Y%c%d'')');
 end if; 
 if v_type=2 then
 set v_stmt=concat('select distinct a.t_clm_pplan_seq from t_clm_pplan a,t_loan_cut b,t_loan_data c,t_inv_act_ref d where b.buss_code=c.buss_code and b.inv_src=c.inv_src and d.t_loan_data_seq=c.t_loan_data_seq and a.t_inv_seq=d.t_inv_seq and a.clm_pp_stat in (4,6,7,8) and b.total_aget >0 and b.loan_period>=a.clm_pp_num and a.clm_pp_ndate is not null and a.clm_pp_ddays>0 and date_format(a.clm_pp_ndate,''%Y%c%d'')<=date_format(''',v_date,''',''%Y%c%d'')');
 end if; 
 if v_type=3 then                                                                                                                                                                                                                                                                                                                                                               
 set v_stmt=concat('select distinct a.t_clm_pplan_seq from t_clm_pplan a,t_loan_data c,t_inv_act_ref d where d.t_loan_data_seq=c.t_loan_data_seq and a.t_inv_seq=d.t_inv_seq and a.clm_pp_stat in (3,5) and a.clm_pp_ndate is not null and a.clm_pp_ddays>0  and date_format(DATE_ADD(a.clm_pp_ndate,INTERVAL 7 day),''%Y%c%d'')<=date_format(''',v_date,''',''%Y%c%d'')');
 end if;   
 set @v_sql=v_stmt;
 prepare stmt from @v_sql;
 EXECUTE stmt;
 deallocate prepare stmt;
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `proc_q5` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`JAdmin`@`%` PROCEDURE `proc_q5`(in v_t_acct_seq bigint,in v_trade_type varchar(30))
begin
declare
v_stmt varchar(2000);
 if v_t_acct_seq is not null and v_trade_type is null then
 set v_stmt=concat('select distinct o.t_act_order_seq from (select a1.t_act_order_seq,b1.t_acct_seq,a1.trade_type,c1.col_cvcn as trt,a1.order_stat,d1.col_cvcn as odt from t_act_order a1,t_acct b1,t_dic c1,t_dic d1 where a1.t_pacct_seq=b1.t_acct_seq and c1.tab_name=''t_act_order'' and c1.col_name=''trade_type'' and c1.col_value=a1.trade_type and d1.tab_name=''t_act_order'' and d1.col_name=''order_stat'' and d1.col_value=a1.order_stat union all select a2.t_act_order_seq,b2.t_acct_seq,a2.trade_type,c2.col_cvcn as trt,a2.order_stat,d2.col_cvcn as odt from t_act_order a2,t_acct b2,t_dic c2,t_dic d2 where a2.t_gacct_seq=b2.t_acct_seq and c2.tab_name=''t_act_order'' and c2.col_name=''trade_type'' and c2.col_value=a2.trade_type and d2.tab_name=''t_act_order'' and d2.col_name=''order_stat'' and d2.col_value=a2.order_stat) o where o.t_acct_seq=',v_t_acct_seq,' and o.trade_type not in (1,20)');
 end if;
  if v_t_acct_seq is not null and v_trade_type is not null then
 set v_stmt=concat('select distinct o.t_act_order_seq from (select a1.t_act_order_seq,b1.t_acct_seq,a1.trade_type,c1.col_cvcn as trt,a1.order_stat,d1.col_cvcn as odt from t_act_order a1,t_acct b1,t_dic c1,t_dic d1 where a1.t_pacct_seq=b1.t_acct_seq and c1.tab_name=''t_act_order'' and c1.col_name=''trade_type'' and c1.col_value=a1.trade_type and d1.tab_name=''t_act_order'' and d1.col_name=''order_stat'' and d1.col_value=a1.order_stat union all select a2.t_act_order_seq,b2.t_acct_seq,a2.trade_type,c2.col_cvcn as trt,a2.order_stat,d2.col_cvcn as odt from t_act_order a2,t_acct b2,t_dic c2,t_dic d2 where a2.t_gacct_seq=b2.t_acct_seq and c2.tab_name=''t_act_order'' and c2.col_name=''trade_type'' and c2.col_value=a2.trade_type and d2.tab_name=''t_act_order'' and d2.col_name=''order_stat'' and d2.col_value=a2.order_stat) o where o.t_acct_seq=',v_t_acct_seq,' and o.trade_type in (',v_trade_type,')');
 end if;
 set @v_sql=v_stmt;
 prepare stmt from @v_sql;
 EXECUTE stmt;
 deallocate prepare stmt;
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `proc_q6` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`JAdmin`@`%` PROCEDURE `proc_q6`(
in v_t_act_inv_seq bigint
)
begin
declare
v_stmt varchar(2000);
declare
v_clm_sgpapri double default 0;
declare
v_clm_sgpaint double default 0;
declare
v_count int;
 if v_t_act_inv_seq is not null then
 select count(1) into v_count from t_clm_gplan where t_act_inv_seq=v_t_act_inv_seq and clm_gp_stat=4;  
  if v_count=0 then
   select sum(clm_gpapri),sum(clm_gpaint) into v_clm_sgpapri,v_clm_sgpaint from t_clm_gplan where t_act_inv_seq=v_t_act_inv_seq and clm_gp_stat<>4 group by t_act_inv_seq; 
  end if;
 set v_stmt=concat('select a.t_inv_seq,e.col_cvcn,f.col_cvcn,g.col_cvcn,a.act_inv_qt,',v_clm_sgpapri,',',v_clm_sgpapri,'+',v_clm_sgpaint,',h.col_cvcn-a.act_inv_gpp,date_format(b.inv_fdate,''%d'') as inv_sdate,a.act_inv_qt/h.col_cvcn,a.act_inv_gpp,i.col_cvcn,b.inv_cre_date,case when a.act_inv_eform=2 then ''是'' else ''否'' end as act_inv_eform,a.act_inv_edate,case when a.act_inv_eform is not null then j.col_cvcn else null end,k.cont_id from t_act_inv a,t_inv b,t_dic e,t_dic f,t_dic g,t_dic h,t_dic i,t_dic j,t_loan_data k,t_inv_act_ref l where a.t_act_inv_seq=',v_t_act_inv_seq,' and e.tab_name=''t_act_inv'' and e.col_name=''act_inv_gform'' and e.col_value=case when a.act_inv_gform<>1 or a.act_inv_gform<>2 or a.act_inv_gform<>3 or a.act_inv_gform<>4 or a.act_inv_gform<>5 then 1 else act_inv_gform end and f.tab_name=''t_inv'' and f.col_name=''inv_irate'' and f.col_value=1 and g.tab_name=''t_dic'' and g.col_name=''act_inv_pmfrate'' and g.col_value=1 and h.tab_name=''t_inv'' and h.col_name=''inv_period'' and h.col_value=b.inv_period and a.t_inv_seq=b.t_inv_seq and i.tab_name=''t_act_inv'' and i.col_name=''act_inv_stat'' and i.col_value=a.act_inv_stat and j.tab_name=''t_act_inv'' and j.col_name=''act_inv_eform'' and j.col_value=case when a.act_inv_eform is not null then a.act_inv_eform else 1 end and b.t_inv_seq=l.t_inv_seq and k.t_loan_data_seq=l.t_loan_data_seq');
 end if;
 set @v_sql=v_stmt;
 prepare stmt from @v_sql;
 EXECUTE stmt;
 deallocate prepare stmt;
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `proc_q7` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`JAdmin`@`%` PROCEDURE `proc_q7`(
in v_tac_uname varchar(30),
in v_t_inv_seq bigint,
in v_act_inv_gform int,
in v_act_inv_cdate datetime,
in v_inv_irate int,
in v_act_inv_stat int,
in v_cont_id varchar(60),
in v_t_act_inv_seq bigint
)
begin                                                           
declare
v_stmt varchar(2000);
 set v_stmt='select distinct a.t_act_inv_seq from t_act_inv a,t_acct b,t_inv c,t_dic d,t_dic e,t_loan_data f,t_inv_act_ref g where a.t_acct_seq=b.t_acct_seq and a.t_inv_seq=c.t_inv_seq and d.tab_name=''t_inv'' and d.col_name=''inv_irate'' and e.tab_name=''t_act_inv'' and e.col_name=''act_inv_gform'' and e.col_value=1 and d.col_value=c.inv_irate and c.t_inv_seq=g.t_inv_seq and f.t_loan_data_seq=g.t_loan_data_seq';
 if v_tac_uname is not null then
 set v_stmt=concat(v_stmt,' and b.tac_uname=''',v_tac_uname,'''');
 end if;
 if v_t_inv_seq is not null then
 set v_stmt=concat(v_stmt,' and c.t_inv_seq=',v_t_inv_seq);
 end if;
 if v_act_inv_gform is not null then
 set v_stmt=concat(v_stmt,' and a.act_inv_gform=',v_act_inv_gform);
 end if;
 if v_act_inv_cdate is not null then
 set v_stmt=concat(v_stmt,' and a.act_inv_cdate=''',v_act_inv_cdate,'''');
 end if; 
 if v_inv_irate is not null then
 set v_stmt=concat(v_stmt,' and c.inv_irate=',v_inv_irate);
 end if;
 if v_act_inv_stat is not null then
 set v_stmt=concat(v_stmt,' and a.act_inv_stat=',v_act_inv_stat);
 end if;
 if v_t_act_inv_seq is not null then
 set v_stmt=concat(v_stmt,' and a.t_act_inv_seq=',v_t_act_inv_seq);
 end if;
 if v_cont_id is not null then
 set v_stmt=concat(v_stmt,' and f.cont_id=''',v_cont_id,'''');
 end if;         
 set @v_sql=v_stmt;
 prepare stmt from @v_sql;
 EXECUTE stmt;
 deallocate prepare stmt;
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `proc_q8` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`JAdmin`@`%` PROCEDURE `proc_q8`(
in v_tac_pid varchar(18),
in v_tac_name varchar(30),
in v_tac_uname varchar(30),
in v_act_thblc double,
in v_trade_amount double,
in v_act_athblc double
)
begin
declare
v_stmt varchar(2000);
 set v_stmt='select e.t_acct_seq from (select a.t_acct_seq from t_acct a where 1=1';
 if v_tac_pid is not null then
 set v_stmt=concat(v_stmt,' and a.tac_pid=''',v_tac_pid,'''');
 end if;
 if v_tac_name is not null then
 set v_stmt=concat(v_stmt,' and a.tac_name=''',v_tac_name,'''');
 end if;
 if v_tac_uname is not null then
 set v_stmt=concat(v_stmt,' and a.tac_uname=''',v_tac_uname,'''');
 end if;
 set v_stmt=concat(v_stmt,' ) e ');
 if v_act_thblc is not null then
 set v_stmt=concat(v_stmt,' join t_act_third b on e.t_acct_seq=b.t_acct_seq and b.act_thblc=',v_act_thblc);
 end if;
 if v_trade_amount is not null and v_act_athblc is null then
 set v_stmt=concat(v_stmt,' join (select c.t_pacct_seq,sum(c.trade_amount) as strade_amount from t_act_order c where c.trade_type=23 and c.order_stat=7 group by c.t_pacct_seq) d on e.t_acct_seq=d.t_pacct_seq and d.strade_amount=',v_trade_amount);
 end if;
 if v_trade_amount is null and v_act_athblc is not null then
 set v_stmt=concat(v_stmt,' join (select c.t_pacct_seq,sum(c.trade_amount) as strade_amount from t_act_order c where c.trade_type=23 and c.order_stat=7 group by c.t_pacct_seq) d on e.t_acct_seq=d.t_pacct_seq and b.act_thblc-d.strade_amount=',v_act_athblc);
 end if;
 if v_trade_amount is not null and v_act_athblc is not null then
 set v_stmt=concat(v_stmt,' join (select c.t_pacct_seq,sum(c.trade_amount) as strade_amount from t_act_order c where c.trade_type=23 and c.order_stat=7 group by c.t_pacct_seq) d on e.t_acct_seq=d.t_pacct_seq and b.act_thblc-d.strade_amount=',v_act_athblc,' and d.strade_amount=',v_trade_amount);
 end if;    
 set @v_sql=v_stmt;
 select v_stmt;
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `proc_q9` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`JAdmin`@`%` PROCEDURE `proc_q9`(
in v_date datetime,
in v_type int
)
begin
declare
v_stmt varchar(2000);
 if v_type=1 then
 set v_stmt=concat('select sum(a.clm_pp_pri+a.clm_pp_int+a.clm_pp_pmfee+a.clm_pp_rfee) from t_clm_pplan a,t_loan_cut b,t_loan_data c,t_inv_act_ref d where b.buss_code=c.buss_code and b.inv_src=c.inv_src and d.t_loan_data_seq=c.t_loan_data_seq and a.t_inv_seq=d.t_inv_seq and a.clm_pp_stat in (4,6,7,8) and b.total_aget>0 and b.loan_period=a.clm_pp_num and clm_pp_ddays =0 and date_format(a.clm_pp_ndate,''%Y%c%d'')=date_format(''',v_date,''',''%Y%c%d'')');
 end if; 
 if v_type=2 then
 set v_stmt=concat('select sum(a.clm_pp_pri+a.clm_pp_int+a.clm_pp_pmfee+a.clm_pp_rfee+a.clm_pp_jint) from t_clm_pplan a,t_loan_cut b,t_loan_data c,t_inv_act_ref d where b.buss_code=c.buss_code and b.inv_src=c.inv_src and d.t_loan_data_seq=c.t_loan_data_seq and a.t_inv_seq=d.t_inv_seq and a.clm_pp_stat in (4,6,7,8) and b.total_aget>0 and b.loan_period>=a.clm_pp_num and a.clm_pp_ndate is not null and a.clm_pp_ddays>0 and date_format(a.clm_pp_ndate,''%Y%c%d'')<=date_format(''',v_date,''',''%Y%c%d'')');
 end if; 
 if v_type=3 then                                                                                                                                                                                                                                                                                                                                                               
 set v_stmt=concat('select sum(a.clm_pp_pri+a.clm_pp_int+a.clm_pp_pmfee+a.clm_pp_rfee+a.clm_pp_jint) from t_clm_pplan a,t_loan_data c,t_inv_act_ref d where d.t_loan_data_seq=c.t_loan_data_seq and a.t_inv_seq=d.t_inv_seq and a.clm_pp_stat in (3,5) and a.clm_pp_ndate is not null and a.clm_pp_ddays>0 and date_format(DATE_ADD(a.clm_pp_ndate,INTERVAL 7 day),''%Y%c%d'')<=date_format(''',v_date,''',''%Y%c%d'')');
 end if;   
 set @v_sql=v_stmt;
 prepare stmt from @v_sql;
 EXECUTE stmt;
 deallocate prepare stmt;
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `proc_q99` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`JAdmin`@`%` PROCEDURE `proc_q99`(
in v_t_inv_seq bigint
)
begin
declare
v_stmt varchar(2000);
 set v_stmt='select d.cont_id,b.tac_pid,b.tac_name,c.inv_total,c.inv_total*f.col_cvcn,c.inv_total*(d.p2p_pay_frate-g.col_cvcn),c.inv_total,c.inv_total-c.inv_total*f.col_cvcn-c.inv_total*(d.p2p_pay_frate-g.col_cvcn),h.trade_date,h.order_stat,i.trade_desc,c.inv_rem,e.col_cvcn-c.inv_pcount,date_format(c.inv_fdate,''%d'') as inv_sdate,c.inv_total/e.col_cvcn,c.inv_pcount,j.ccm,c.inv_odate,k.clm_pp_surplus,l.col_cvcn from t_inv_act_ref a join t_inv c on c.t_inv_seq=a.t_inv_seq';
 if v_t_inv_seq is not null then
 set v_stmt=concat(v_stmt,' and a.t_inv_seq=',v_t_inv_seq);
 end if;
 set v_stmt=concat(v_stmt,' left join (select t_inv_seq,sum(clm_pp_pri+clm_pp_int) as clm_pp_surplus from t_clm_pplan where clm_pp_stat<>4 group by t_inv_seq) k on a.t_inv_seq=k.t_inv_seq join t_acct b on a.t_acct_seq=b.t_acct_seq join t_loan_data d on a.t_loan_data_seq=d.t_loan_data_seq join t_dic e on e.tab_name=''t_inv'' and e.col_name=''inv_period'' and e.col_value=c.inv_period join (select a1.col_cvcn from t_dic a1 where a1.tab_name=''t_dic'' and a1.col_name=''claim_pay_rrate'' and a1.col_value=1) f join (select a1.col_cvcn from t_dic a1 where a1.tab_name=''t_dic'' and a1.col_name=''p2p_pay_frate'' and a1.col_value=1) g left join t_act_order h on a.t_inv_seq=h.trade_ctpvalue and h.trade_type=25 and h.order_stat =3 left join (select a1.trade_ctpvalue,b1.trade_desc from t_act_order a1,t_act_oh b1 where a1.t_act_order_seq=b1.t_act_order_seq and b1.order_stat=4 order by t_act_oh_seq desc limit 1) i on a.t_inv_seq=i.trade_ctpvalue left join (select count(cm) as ccm,t_inv_seq from (select t_inv_seq,extract(year_month from (date_add(act_inv_cdate,interval -cast(@rownum:=@rownum+1 as char(9)) month))) as cm from t_act_inv e,(select @rownum:=0) f where extract(year_month from (date_add(act_inv_cdate,interval -cast(@rownum:=@rownum+1 as char(9)) month))) is not null and e.act_inv_gform=5) o group by o.t_inv_seq) j on j.t_inv_seq=a.t_inv_seq join t_dic l on l.tab_name=''t_loan_data'' and l.col_name=''lc_ptype'' and l.col_value=d.lc_ptype');
 select v_stmt;
end ;;
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

-- Dump completed on 2015-04-20 21:18:50
