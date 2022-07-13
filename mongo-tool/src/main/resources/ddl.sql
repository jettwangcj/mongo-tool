CREATE TABLE `connection` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `authority_key` varchar(500) DEFAULT NULL COMMENT '密钥',
  `hosts` varchar(1000) DEFAULT NULL COMMENT '主机',
  `database` varchar(100) DEFAULT NULL COMMENT '数据库',
	`url` varchar(2000) DEFAULT NULL COMMENT 'url',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COMMENT='mongo 连接';
