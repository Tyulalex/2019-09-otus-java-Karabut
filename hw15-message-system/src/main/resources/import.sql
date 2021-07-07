insert into Users (address_id, age, login, name, password, role, id) values (null, 13, 'admin', 'Admin', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', 'ADMIN', idusers.nextval);
insert into addresses (id, street) values (idaddress.nextval, 'Black Street');
insert into Users (address_id, age, login, name, password, role, id) values (idaddress.currval, 13, 'test', 'Test', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', 'WEBUSER', idusers.nextval);
insert into phones (number, user_id, id) values ('(123)-111-11-11', idusers.currval, idphone.nextval);