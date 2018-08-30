-- MySQL dump 10.13  Distrib 5.7.21, for macos10.13 (x86_64)
--
-- Host: localhost    Database: tale
-- ------------------------------------------------------
-- Server version	5.7.21

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
-- Table structure for table `t_attach`
--

DROP TABLE IF EXISTS `t_attach`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_attach` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `fname` varchar(100) NOT NULL DEFAULT '',
  `ftype` varchar(50) DEFAULT '',
  `fkey` varchar(100) NOT NULL DEFAULT '',
  `author_id` int(10) DEFAULT NULL,
  `created` int(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_attach`
--

LOCK TABLES `t_attach` WRITE;
/*!40000 ALTER TABLE `t_attach` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_attach` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_comments`
--

DROP TABLE IF EXISTS `t_comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_comments` (
  `coid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `cid` int(10) unsigned DEFAULT '0',
  `created` int(10) unsigned DEFAULT '0',
  `author` varchar(200) DEFAULT NULL,
  `author_id` int(10) unsigned DEFAULT '0',
  `owner_id` int(10) unsigned DEFAULT '0',
  `mail` varchar(200) DEFAULT NULL,
  `url` varchar(200) DEFAULT NULL,
  `ip` varchar(64) DEFAULT NULL,
  `agent` varchar(200) DEFAULT NULL,
  `content` text,
  `type` varchar(16) DEFAULT 'comment',
  `status` varchar(16) DEFAULT 'approved',
  `parent` int(10) unsigned DEFAULT '0',
  PRIMARY KEY (`coid`),
  KEY `cid` (`cid`),
  KEY `created` (`created`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_comments`
--

LOCK TABLES `t_comments` WRITE;
/*!40000 ALTER TABLE `t_comments` DISABLE KEYS */;
INSERT INTO `t_comments` VALUES (1,3,1530534507,'hezijian',0,1,'627222344@qq.com','','0:0:0:0:0:0:0:1',NULL,'垃圾！垃圾','comment','approved',0),(6,29,1535594225,'Fatboy',0,2,'fatboy88888888@hotmail.com','','127.0.0.1',NULL,'辣鸡hzj','comment','approved',0);
/*!40000 ALTER TABLE `t_comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_contents`
--

DROP TABLE IF EXISTS `t_contents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_contents` (
  `cid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(200) DEFAULT NULL,
  `slug` varchar(200) DEFAULT NULL,
  `created` int(10) unsigned DEFAULT '0',
  `modified` int(10) unsigned DEFAULT '0',
  `content` text COMMENT '内容文字',
  `author_id` int(10) unsigned DEFAULT '0',
  `type` varchar(16) DEFAULT 'post',
  `status` varchar(16) DEFAULT 'publish',
  `tags` varchar(200) DEFAULT NULL,
  `categories` varchar(200) DEFAULT NULL,
  `hits` int(10) unsigned DEFAULT '0',
  `comments_num` int(10) unsigned DEFAULT '0',
  `allow_comment` tinyint(1) DEFAULT '1',
  `allow_ping` tinyint(1) DEFAULT '1',
  `allow_feed` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`cid`),
  UNIQUE KEY `slug` (`slug`),
  KEY `created` (`created`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_contents`
--

LOCK TABLES `t_contents` WRITE;
/*!40000 ALTER TABLE `t_contents` DISABLE KEYS */;
INSERT INTO `t_contents` VALUES (1,'about my blog','about',1487853610,1487872488,'### Hello World\r\n\r\nabout me\r\n\r\n### ...\r\n\r\n...',1,'page','publish',NULL,NULL,41,0,1,1,1),(2,'Hello My Blog',NULL,1487861184,1487872798,'## Hello  World.\r\n\r\n> ...\r\n\r\n----------\r\n\r\n\r\n<!--more-->\r\n\r\n```java\r\npublic static void main(String[] args){\r\n    System.out.println(\"Hello 13 Blog.\");\r\n}\r\n```',1,'post','publish','','',13,0,1,1,1),(3,'Docker','my-first-articale',1530534470,1530534470,'这个项目的docker无法成功部署怎么办',1,'post','publish','docker','',19,1,1,1,1),(5,'Other Blog','OtherBlog',1530617941,1530617941,'其他人的博客',2,'page','publish',NULL,NULL,21,0,1,1,1),(28,'设计模式','Design_Model',1535587708,1535589930,'## 设计模式 ##\r\n\r\n**创建型**\r\n\r\n 1. 单例模式（Singleton）\r\n 2. 简单工厂（Simple Factory）\r\n 3. 工厂方法（Factory Method）',2,'post','publish','代码思想','IT',3,0,1,1,1),(29,'微信小程序问题--1','MiniProgram_Problem',1535589816,1535618194,'## 小程序 ##\r\n\r\n>需要手动设置Button来进行第一次授权\r\n    暂时无法让第一次为默认登陆并且注册，必须引导用户点击\r\n>>设置第一页启动页引导用户在没有登陆注册的情况下进行授权登陆\r\n\r\n>同一账号，不同设备登陆无法进行完整的授权登陆\r\n只有MAC设备能够正常使用\r\n>>- code重复发送，导致失效无法访问\r\n\r\n>购物车统计价钱有问题\r\n>>- 结算的时候价钱错误\r\n>>- 如果选择两个以上的没有直接算总数\r\n\r\n>商品浏览图片有问题\r\n\r\n## 后台 ##\r\n\r\n>前台发送code代码到后台时候，下面代码发生报错\r\n>>    - ```WxMaJscode2SessionResult session = this.wxService.getUserService().getSessionInfo(code);```\r\n\r\n>尝试能否改变登陆/注册的方式\r\n>>直接使用基础信息，而不用具体解密代码信息等等\r\n>>>- 因为我们只需要基本用户名字和头像信息就足够了   \r\n>>>- 即便是在收货地址的时候有一个一键获取微信地址\r\n>>>- 密码或许直接使用用户的具体信息进行拼接完成',2,'post','publish','小程序','小程序',13,1,1,1,1),(30,'微信小程序--授权登陆首页','MiniProgram_Authorization',1535617202,1535617202,'## login.js ##\r\n```javascript\r\nvar api = require(\'../../api.js\');\r\nvar api1 = require(\'../../api1.js\');\r\nvar app = getApp();\r\n\r\nPage({\r\n    data: {\r\n        //判断小程序的API，回调，参数，组件等是否在当前版本可用。\r\n        canIUse: wx.canIUse(\'button.open-type.getUserInfo\')\r\n    },\r\n    onLoad: function () {\r\n        var that = this;\r\n        var pages = getCurrentPages();\r\n        var page = pages[(pages.length - 2)];\r\n        // 查看是否授权\r\n        wx.getSetting({\r\n            success: function (res) {\r\n                if (res.authSetting[\'scope.userInfo\']) {\r\n                    wx.getUserInfo({\r\n                        success: function (res) {\r\n                            var access_token = wx.getStorageSync(\"access_token\");\r\n                            console.log(\'11token:\' + access_token);\r\n                            if(access_token)\r\n                            wx.switchTab({\r\n                                url: \"../index/index\"\r\n                            })\r\n                        }\r\n                    });\r\n                }\r\n            }\r\n        })\r\n    },\r\n    bindGetUserInfo: function (e) {\r\n        if (e.detail.userInfo) {\r\n            //用户按了允许授权按钮\r\n            var that = this;\r\n            //插入登录的用户的相关信息到数据库\r\n            that.login();\r\n            //授权成功后，跳转进入小程序首页\r\n            wx.switchTab({\r\n                url: \"../index/index\"\r\n            })\r\n        } else {\r\n            //用户按了拒绝按钮\r\n            wx.showModal({\r\n                title: \'警告\',\r\n\r\n    login: function () {\r\n        });\r\n    },\r\n\r\n})\r\n```\r\n\r\n## login.wxml ##\r\n\r\n```\r\n<view wx:if=\"{{canIUse}}\">\r\n    <view class=\'header\'>\r\n        <!-- <image src=\'/images/wx_login.png\'></image> -->\r\n    </view>\r\n \r\n    <view class=\'content\'>\r\n        <view>申请获取以下权限</view>\r\n        <text>获得你的公开信息(昵称，头像等)</text>\r\n    </view>\r\n \r\n    <button class=\'bottom\' type=\'primary\' open-type=\"getUserInfo\" lang=\"zh_CN\" bindgetuserinfo=\"bindGetUserInfo\">\r\n        授权登录\r\n    </button>\r\n</view>\r\n \r\n<view wx:else>请升级微信版本</view>\r\n\r\n```\r\n\r\n## login.wxss ##\r\n\r\n```\r\n.header {\r\n    margin: 90rpx 0 90rpx 50rpx;\r\n    border-bottom: 1px solid #ccc;\r\n    text-align: center;\r\n    width: 650rpx;\r\n    height: 300rpx;\r\n    line-height: 450rpx;\r\n}\r\n \r\n.header image {\r\n    width: 200rpx;\r\n    height: 200rpx;\r\n}\r\n \r\n.content {\r\n    margin-left: 50rpx;\r\n    margin-bottom: 90rpx;\r\n}\r\n \r\n.content text {\r\n    display: block;\r\n    color: #9d9d9d;\r\n    margin-top: 40rpx;\r\n}\r\n \r\n.bottom {\r\n    border-radius: 80rpx;\r\n    margin: 70rpx 50rpx;\r\n    font-size: 35rpx;\r\n}\r\n\r\n```\r\n\r\n## login.json ##\r\n\r\n```\r\n{\r\n    \"navigationBarTitleText\": \"授权登录\"\r\n}\r\n\r\n```',2,'post','publish','mini_program,Authorization,login_page','小程序,代码',3,0,1,1,1);
/*!40000 ALTER TABLE `t_contents` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_logs`
--

DROP TABLE IF EXISTS `t_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_logs` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `action` varchar(100) DEFAULT NULL,
  `data` varchar(2000) DEFAULT NULL,
  `author_id` int(10) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `created` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_logs`
--

LOCK TABLES `t_logs` WRITE;
/*!40000 ALTER TABLE `t_logs` DISABLE KEYS */;
INSERT INTO `t_logs` VALUES (1,'登录后台',NULL,1,'0:0:0:0:0:0:0:1',1530532631),(2,'登录后台',NULL,1,'0:0:0:0:0:0:0:1',1530532934),(3,'登录后台',NULL,1,'0:0:0:0:0:0:0:1',1530534403),(4,'登录后台',NULL,1,'0:0:0:0:0:0:0:1',1530534545),(5,'登录后台',NULL,1,'0:0:0:0:0:0:0:1',1530535313),(6,'登录后台',NULL,2,'0:0:0:0:0:0:0:1',1530535780),(7,'登录后台',NULL,2,'0:0:0:0:0:0:0:1',1530617789),(8,'登录后台',NULL,2,'0:0:0:0:0:0:0:1',1530873864),(9,'登录后台',NULL,2,'0:0:0:0:0:0:0:1',1530874417),(10,'登录后台',NULL,2,'0:0:0:0:0:0:0:1',1530874423),(11,'登录后台',NULL,2,'0:0:0:0:0:0:0:1',1530874428),(12,'登录后台',NULL,2,'0:0:0:0:0:0:0:1',1531210782),(13,'登录后台',NULL,2,'0:0:0:0:0:0:0:1',1531223637),(14,'登录后台',NULL,2,'0:0:0:0:0:0:0:1',1531267793),(15,'登录后台',NULL,2,'0:0:0:0:0:0:0:1',1531268122),(16,'登录后台',NULL,2,'0:0:0:0:0:0:0:1',1531268386),(17,'登录后台',NULL,2,'0:0:0:0:0:0:0:1',1531268469),(18,'登录后台',NULL,2,'0:0:0:0:0:0:0:1',1531270494),(19,'登录后台',NULL,12,'0:0:0:0:0:0:0:1',1531296390),(20,'登录后台',NULL,23,'0:0:0:0:0:0:0:1',1531297305),(21,'登录后台',NULL,2,'0:0:0:0:0:0:0:1',1531354287),(22,'登录后台',NULL,2,'0:0:0:0:0:0:0:1',1531383156),(23,'登录后台',NULL,2,'0:0:0:0:0:0:0:1',1531980558),(24,'登录后台',NULL,2,'127.0.0.1',1531981594),(25,'登录后台',NULL,1,'0:0:0:0:0:0:0:1',1531987928),(26,'登录后台',NULL,2,'127.0.0.1',1535586988),(27,'登录后台',NULL,2,'127.0.0.1',1535589759),(28,'删除文章','22',2,'127.0.0.1',1535589830),(29,'删除文章','21',2,'127.0.0.1',1535589837),(30,'删除文章','20',2,'127.0.0.1',1535589841),(31,'删除文章','19',2,'127.0.0.1',1535589846),(32,'删除文章','4',2,'127.0.0.1',1535589849),(33,'删除文章','27',2,'127.0.0.1',1535589857),(34,'删除文章','26',2,'127.0.0.1',1535589880),(35,'登录后台',NULL,2,'127.0.0.1',1535597148),(36,'登录后台',NULL,2,'127.0.0.1',1535608525),(37,'登录后台',NULL,2,'127.0.0.1',1535615654),(38,'登录后台',NULL,2,'127.0.0.1',1535636509),(39,'登录后台',NULL,2,'127.0.0.1',1535636615);
/*!40000 ALTER TABLE `t_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_metas`
--

DROP TABLE IF EXISTS `t_metas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_metas` (
  `mid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(200) DEFAULT NULL,
  `slug` varchar(200) DEFAULT NULL,
  `type` varchar(32) NOT NULL DEFAULT '',
  `description` varchar(200) DEFAULT NULL,
  `sort` int(10) unsigned DEFAULT '0',
  `parent` int(10) unsigned DEFAULT '0',
  PRIMARY KEY (`mid`),
  KEY `slug` (`slug`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_metas`
--

LOCK TABLES `t_metas` WRITE;
/*!40000 ALTER TABLE `t_metas` DISABLE KEYS */;
INSERT INTO `t_metas` VALUES (7,'docker','docker','tag',NULL,0,0),(8,'my page','http://www.baidu.com','link','',0,0),(9,'Spring boot','Spring boot','tag',NULL,0,0),(10,'默认分类','默认分类','category',NULL,0,0),(12,'test','test','tag',NULL,0,0),(13,'cwh','cwh','tag',NULL,0,0),(14,'标签','标签','tag',NULL,0,0),(15,'zjq','zjq','tag',NULL,0,0),(16,'sb','sb','tag',NULL,0,0),(17,'zhbit','zhbit','tag',NULL,0,0),(19,'代码思想','代码思想','tag',NULL,0,0),(20,'IT',NULL,'category',NULL,0,0),(21,'算法',NULL,'category',NULL,0,0),(22,'代码',NULL,'category',NULL,0,0),(23,'思想',NULL,'category',NULL,0,0),(24,'生活',NULL,'category',NULL,0,0),(25,'小程序',NULL,'category',NULL,0,0),(26,'小程序','小程序','tag',NULL,0,0),(27,'mini_program','mini_program','tag',NULL,0,0),(28,'Authorization','Authorization','tag',NULL,0,0),(29,'login_page','login_page','tag',NULL,0,0),(30,'小程序后台','http://zhbitshop.dragonsking.club','link','',1,0);
/*!40000 ALTER TABLE `t_metas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_options`
--

DROP TABLE IF EXISTS `t_options`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_options` (
  `name` varchar(32) NOT NULL DEFAULT '',
  `value` varchar(1000) DEFAULT '',
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_options`
--

LOCK TABLES `t_options` WRITE;
/*!40000 ALTER TABLE `t_options` DISABLE KEYS */;
INSERT INTO `t_options` VALUES ('site_description','13 Blog',NULL),('site_keywords','13 Blog',NULL),('site_record','','备案号'),('site_theme','default',NULL),('site_title','My Blog',''),('social_github','',NULL),('social_twitter','',NULL),('social_weibo','',NULL),('social_zhihu','',NULL);
/*!40000 ALTER TABLE `t_options` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_relationships`
--

DROP TABLE IF EXISTS `t_relationships`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_relationships` (
  `cid` int(10) unsigned NOT NULL,
  `mid` int(10) unsigned NOT NULL,
  PRIMARY KEY (`cid`,`mid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_relationships`
--

LOCK TABLES `t_relationships` WRITE;
/*!40000 ALTER TABLE `t_relationships` DISABLE KEYS */;
INSERT INTO `t_relationships` VALUES (3,7),(28,19),(28,20),(29,25),(29,26),(30,22),(30,25),(30,27),(30,28),(30,29);
/*!40000 ALTER TABLE `t_relationships` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_relationshipsforlinks`
--

DROP TABLE IF EXISTS `t_relationshipsforlinks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_relationshipsforlinks` (
  `uid` int(10) unsigned NOT NULL,
  `mid` int(10) unsigned NOT NULL,
  PRIMARY KEY (`uid`,`mid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_relationshipsforlinks`
--

LOCK TABLES `t_relationshipsforlinks` WRITE;
/*!40000 ALTER TABLE `t_relationshipsforlinks` DISABLE KEYS */;
INSERT INTO `t_relationshipsforlinks` VALUES (1,8),(2,6),(2,30);
/*!40000 ALTER TABLE `t_relationshipsforlinks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_users`
--

DROP TABLE IF EXISTS `t_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_users` (
  `uid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(32) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  `home_url` varchar(200) DEFAULT NULL,
  `screen_name` varchar(32) DEFAULT NULL,
  `created` int(10) unsigned DEFAULT '0',
  `activated` int(10) unsigned DEFAULT '0',
  `logged` int(10) unsigned DEFAULT '0',
  `group_name` varchar(16) DEFAULT 'visitor',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `name` (`username`),
  UNIQUE KEY `mail` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_users`
--

LOCK TABLES `t_users` WRITE;
/*!40000 ALTER TABLE `t_users` DISABLE KEYS */;
INSERT INTO `t_users` VALUES (1,'admin','a66abb5684c45962d887564f08346e8d','1034683568@qq.com',NULL,'admin',1490756162,0,0,'visitor'),(2,'hezijian6338','67062032da0b72b78074144a5ef90dd5','627222344@qq.com',NULL,'hezijian6338',1490756162,0,0,'visitor'),(23,'zengjianqi','dfaf06bdb03552f70d0477712e12038d','888@.qq.com',NULL,'zengjianqi',1531297285,0,0,'visitor');
/*!40000 ALTER TABLE `t_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'tale'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-08-30 21:52:05
