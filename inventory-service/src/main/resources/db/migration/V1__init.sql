create table `t_inventory`
(
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `sku_code` varchar(255) DEFAULT null,
    `quantity` int(11) DEFAULT null,
    PRIMARY KEY(`id`)
);