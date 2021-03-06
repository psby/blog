CREATE SEQUENCE  SEQ_SEMESTER
START WITH 1
INCREMENT BY 1;


CREATE TABLE SEMESTER (

	SEMESTER_ID NUMBER PRIMARY KEY NOT NULL,
	SEMESTER_NAME VARCHAR(50) NOT NULL

);

CREATE SEQUENCE  SEQ_SUBJECT
START WITH 1
INCREMENT BY 1;

CREATE TABLE SUBJECT (

	SUBJECT_ID NUMBER PRIMARY KEY NOT NULL,
	SUBJECT_NAME VARCHAR(50) NOT NULL,
	SEMESTER_ID_FK NUMBER NOT NULL,
	CONSTRAINT CNSTR_SEMESTER_ID_FK FOREIGN KEY(SEMESTER_ID_FK) REFERENCES SEMESTER(SEMESTER_ID)
	
);