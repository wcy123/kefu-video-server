CREATE TABLE IF NOT EXISTS `growing_io_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userName` char(64) NOT NULL,
  `tenantId` int(11) DEFAULT NULL,
  `growingUserId` char(64) NOT NULL,
  `projectId` char(64) NOT NULL,
  `createDateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `TENANID_UNIQUE` (`userName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS `growing_io_company` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tenantId` int(11) NOT NULL,
  `userId` varchar(64) DEFAULT NULL,
  `accountId` varchar(64) DEFAULT NULL,
  `projectId` varchar(64) DEFAULT NULL,
  `mail` varchar(64) DEFAULT NULL,
  `refreshToken` varchar(64) DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  `createDateTime` datetime DEFAULT NULL,
  `lastUpdateDateTime` datetime DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `scale` int(10) DEFAULT NULL,
  `industry` varchar(128) DEFAULT NULL,
  `domain` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `TENANID_UNIQUE` (`tenantId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS `growing_io_authority` (
  `tenantId` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `createDateTime` date NOT NULL,
  `status` varchar(1) NOT NULL,
  `remark` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`tenantId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
