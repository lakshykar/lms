use library;

CREATE TABLE `parameter_types` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE `parameters` (
  `id` int NOT NULL AUTO_INCREMENT,
  `parameter_type_id` int DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `min_length` int DEFAULT NULL,
  `max_length` int DEFAULT NULL,
  `min_value` decimal(12,3) DEFAULT NULL,
  `max_value` decimal(13,3) DEFAULT NULL,
  `regex` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `ref_name` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name_type` (`parameter_type_id`,`name`),
  KEY `FKf7378qcbfalbqleuf8yr8m8jx` (`parameter_type_id`),
  CONSTRAINT `FKf7378qcbfalbqleuf8yr8m8jx` FOREIGN KEY (`parameter_type_id`) REFERENCES `parameter_types` (`id`)
) ENGINE=InnoDB;

CREATE TABLE `interactions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `interaction_type_id` int DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE `request_structure` (
  `id` int NOT NULL AUTO_INCREMENT,
  `interaction_id` int DEFAULT NULL,
  `parameter_id` int DEFAULT NULL,
  `min_length` int DEFAULT NULL,
  `max_length` int DEFAULT NULL,
  `min_value` decimal(12,3) DEFAULT NULL,
  `max_value` decimal(12,3) DEFAULT NULL,
  `regex` varchar(255) DEFAULT NULL,
  `is_required` int DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_interaction_parameter` (`interaction_id`,`parameter_id`),
  KEY `FKdh986kin1lqbmsu2obot7mlmg` (`interaction_id`),
  KEY `FK43a5ka41vhcx6nyvigsey84hu` (`parameter_id`),
  CONSTRAINT `FK43a5ka41vhcx6nyvigsey84hu` FOREIGN KEY (`parameter_id`) REFERENCES `parameters` (`id`),
  CONSTRAINT `FKdh986kin1lqbmsu2obot7mlmg` FOREIGN KEY (`interaction_id`) REFERENCES `interactions` (`id`)
) ENGINE=InnoDB;

CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `mobile` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `user_type` int DEFAULT '0',
  `status` int NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `id_card_number` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `uk_id_card` (`id_card_number`)
) ENGINE=InnoDB;

CREATE TABLE `role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `permission` varchar(100) DEFAULT NULL,
  `status` int NOT NULL,
  `updated_at` datetime NOT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE `user_role_mapping` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `status` int NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_role_mapping_1` (`role_id`),
  KEY `fk_user_role_mapping_2` (`user_id`),
  CONSTRAINT `fk_user_role_mapping_1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `fk_user_role_mapping_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB;

CREATE TABLE `role_url_mapping` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int DEFAULT NULL,
  `url_pattern` varchar(255) DEFAULT NULL,
  `status` int NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_role_url_mapping_1` (`role_id`),
  CONSTRAINT `fk_role_url_mapping_1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB;

CREATE TABLE `book` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `isbn` varchar(13) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `number_of_copies` int NOT NULL,
  `publisher` varchar(255) DEFAULT NULL,
  `keywords` varchar(255) DEFAULT NULL,
  `year_of_publication` varchar(255) DEFAULT NULL,
  `status` int NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `created_by` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_isbn` (`isbn`),
  KEY `fk_book_1` (`created_by`),
  CONSTRAINT `fk_book_1` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB;

CREATE TABLE `issued_book` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `book_id` bigint DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `issued_till` datetime DEFAULT NULL,
  `return_date` datetime DEFAULT NULL,
  `issued_by` int DEFAULT NULL,
  `received_by` int DEFAULT NULL,
  `status` int NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_issued_book_1` (`book_id`),
  KEY `fk_issued_book_2` (`user_id`),
  KEY `fk_issued_book_3` (`issued_by`),
  KEY `fk_issued_book_4` (`received_by`),
  CONSTRAINT `fk_issued_book_1` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`),
  CONSTRAINT `fk_issued_book_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_issued_book_3` FOREIGN KEY (`issued_by`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_issued_book_4` FOREIGN KEY (`received_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB;


insert into role values(100,'ROLE_ADMIN','ADMIN',1,now(),now());
insert into role values(200,'ROLE_LIBRARIAN','LIBRARIAN',1,now(),now());

-- admin user
insert into user (id,mobile,name,email,username,password,user_type,status,created_at,updated_at) values(1,'9000000000','admin','admin@gmail.com','admin','$2a$10$bUBJCU2GEGVvqsYe9QTGROjTcB54LCkfKaerDBFWFBGlba2L51TBC',1,1,now(),now());
insert into user_role_mapping values(1,100,1,1,now(),now());

insert into role_url_mapping values(1,100,'/**',1,now(),now());
insert into role_url_mapping values(2,200,'/api/v1/book/**',1,now(),now());
insert into role_url_mapping values(4,200,'/api/v1/student/**',1,now(),now());

-- parameter types
insert into parameter_types values(1,'Numeric','Numeric',now(),now());
insert into parameter_types values(2,'Text','Text',now(),now());
insert into parameter_types values(3,'List','List',now(),now());
	
-- parameters
insert into parameters(id,parameter_type_id,name,min_length,max_length,ref_name) values(1,1,'user_id',null,null,'user_id');
insert into parameters(id,parameter_type_id,name,min_length,max_length,ref_name) values(2,2,'name',4,100,'name');
insert into parameters(id,parameter_type_id,name,min_length,max_length,ref_name) values(3,2,'mobile',10,10,'mobile');
insert into parameters(id,parameter_type_id,name,min_length,max_length,ref_name) values(4,2,'username',4,20,'username');
insert into parameters(id,parameter_type_id,name,min_length,max_length,ref_name) values(5,2,'password',4,16,'password');
insert into parameters(id,parameter_type_id,name,min_length,max_length,ref_name) values(6,1,'user_type',1,1,'user_type');
insert into parameters(id,parameter_type_id,name,min_length,max_length,ref_name) values(7,1,'status',null,null,'status');
insert into parameters(id,parameter_type_id,name,min_length,max_length,ref_name) values(8,2,'email',5,100,'email');
insert into parameters(id,parameter_type_id,name,min_length,max_length,ref_name) values(9,2,'title',2,200,'title');
insert into parameters(id,parameter_type_id,name,min_length,max_length,ref_name) values(10,2,'isbn',10,13,'isbn');
insert into parameters(id,parameter_type_id,name,min_length,max_length,ref_name) values(11,2,'location',null,null,'location');
insert into parameters(id,parameter_type_id,name,min_length,max_length,ref_name) values(12,2,'author',2,100,'author');
insert into parameters(id,parameter_type_id,name,min_length,max_length,ref_name) values(13,1,'number_of_copies',null,null,'number_of_copies');
insert into parameters(id,parameter_type_id,name,min_length,max_length,ref_name) values(14,2,'publisher',2,100,'publisher');
insert into parameters(id,parameter_type_id,name,min_length,max_length,ref_name) values(15,1,'limit',1,5,'limit');
insert into parameters(id,parameter_type_id,name,min_length,max_length,ref_name) values(16,1,'offset',1,5,'offset');
insert into parameters(id,parameter_type_id,name,min_length,max_length,ref_name) values(17,2,'icard_number',5,20,'icard_number');
insert into parameters(id,parameter_type_id,name,min_length,max_length,ref_name) values(18,2,'return_date',10,10,'return_date');
insert into parameters(id,parameter_type_id,name,min_length,max_length,ref_name) values(19,1,'issued_book_id',1,20,'issued_book_id');



-- interactions
insert into interactions values(1000,1000,'Add Librarian',now(),now());
insert into interactions values(1001,1001,'Get Librarian',now(),now());
insert into interactions values(1002,1002,'Delete Librarian',now(),now()); 
insert into interactions values(1003,1003,'Add Book',now(),now());
insert into interactions values(1004,1004,'View Books',now(),now());
insert into interactions values(1005,1005,'Issue Book',now(),now());
insert into interactions values(1006,1006,'View Issued Books',now(),now());
insert into interactions values(1007,1007,'Return Issued Book',now(),now());
insert into interactions values(1008,1008,'Update book',now(),now());
insert into interactions values(1009,1009,'Add Student',now(),now());
insert into interactions values(1010,1010,'View Student',now(),now());


-- add librarian 
insert into request_structure (interaction_id,parameter_id,is_required) values(1000,2,1);
insert into request_structure (interaction_id,parameter_id,is_required) values(1000,3,1);
insert into request_structure (interaction_id,parameter_id,is_required) values(1000,4,1);
insert into request_structure (interaction_id,parameter_id,is_required) values(1000,5,1);
insert into request_structure (interaction_id,parameter_id,is_required) values(1000,8,1);

-- get librarian
insert into request_structure (interaction_id,parameter_id,is_required) values(1001,1,0);

-- delete librarian
insert into request_structure (interaction_id,parameter_id,is_required) values(1002,1,1);

-- add book
insert into request_structure (interaction_id,parameter_id,is_required) values(1003,9,1);
insert into request_structure (interaction_id,parameter_id,is_required) values(1003,10,1);
insert into request_structure (interaction_id,parameter_id,is_required) values(1003,11,1);
insert into request_structure (interaction_id,parameter_id,is_required) values(1003,12,1);
insert into request_structure (interaction_id,parameter_id,is_required) values(1003,13,1);
insert into request_structure (interaction_id,parameter_id,is_required) values(1003,14,1);

-- view book
insert into request_structure (interaction_id,parameter_id,is_required) values(1004,10,0);
insert into request_structure (interaction_id,parameter_id,is_required) values(1004,9,0);
insert into request_structure (interaction_id,parameter_id,is_required) values(1004,15,0);
insert into request_structure (interaction_id,parameter_id,is_required) values(1004,16,0);

-- update book
insert into request_structure (interaction_id,parameter_id,is_required) values(1008,10,1);
insert into request_structure (interaction_id,parameter_id,is_required) values(1008,13,1);

-- add student
insert into request_structure (interaction_id,parameter_id,is_required) values(1009,2,1);
insert into request_structure (interaction_id,parameter_id,is_required) values(1009,3,1);
insert into request_structure (interaction_id,parameter_id,is_required) values(1009,4,1);
insert into request_structure (interaction_id,parameter_id,is_required) values(1009,5,1);
insert into request_structure (interaction_id,parameter_id,is_required) values(1009,8,1);

-- get student
insert into request_structure (interaction_id,parameter_id,is_required) values(1010,17,0);

-- issue book
insert into request_structure (interaction_id,parameter_id,is_required) values(1005,17,1);
insert into request_structure (interaction_id,parameter_id,is_required) values(1005,10,1);


-- view issued book 
insert into request_structure (interaction_id,parameter_id,is_required) values(1006,17,0);
insert into request_structure (interaction_id,parameter_id,is_required) values(1006,10,0);


-- return issued book
insert into request_structure (interaction_id,parameter_id,is_required) values(1007,19,1);
