insert into role values(100,'ROLE_ADMIN','ADMIN',1,now(),now());
insert into role values(200,'ROLE_LIBRARIAN','LIBRARIAN',1,now(),now());

insert into role_url_mapping values(1,100,'/',1,now(),now());
insert into role_url_mapping values(2,100,'/api/v1/health',1,now(),now());
insert into role_url_mapping values(3,200,'/api/v1/book/**',1,now(),now());
insert into role_url_mapping values(4,100,'/api/v1/librarian/**',1,now(),now());
insert into role_url_mapping values(5,200,'/api/v1/student/**',1,now(),now());
insert into role_url_mapping values(6,200,'/api/v1/student',1,now(),now());

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

--add book
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

--add student
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
