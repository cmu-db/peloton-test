CREATE TABLE item ( i_id int NOT NULL PRIMARY KEY, i_name varchar(24) NOT NULL, i_price decimal(5,2) NOT NULL, i_data varchar(50) NOT NULL, i_im_id int NOT NULL);
INSERT INTO ITEM VALUES(55015, 'bxpsckfnunetlxjowjf', 81.44, 'lwmdjaqsupwqajspcqowdmphlbbq',1);
SELECT I_PRICE, I_NAME , I_DATA FROM ITEM WHERE I_ID = 55015;
