CREATE DATABASE IF NOT EXISTS pmauto;

CREATE USER IF NOT EXISTS 'test'@'localhost' IDENTIFIED BY 'test';
CREATE USER IF NOT EXISTS 'test'@'%' IDENTIFIED BY 'test';
GRANT ALL ON pmauto.* TO 'test'@'localhost';
GRANT ALL ON pmauto.* TO 'test'@'%';

USE pmauto;

DROP TABLE IF EXISTS employees;
CREATE TABLE employees
(
    id           TINYTEXT,
    empnumber    INT,
    firstname    VARCHAR(30),
    lastname     VARCHAR(30),
    username     VARCHAR(30),
    paygrades    TINYTEXT,
    active       BOOLEAN,
    hr           BOOLEAN,
    manager_id   TINYTEXT,
    flexTime     TINYINT,
    vacationTime TINYINT
);

INSERT INTO employees
VALUES ("623e4567-e89b-12d3-a456-556642440710", 123, "john", "ivaganov", "ivaganov",
        "123e4567-e89b-12d3-a453-556646440000", true, true, "223e4567-e89b-12d3-a456-556642446010", -7, 56);
INSERT INTO employees
VALUES ("223e4567-e89b-12d3-a456-556642446010", 153, "scott", "reid", "sreid", "123e4567-e89b-12d3-a452-556645440100",
        true, false, null, 5, 14);

INSERT INTO employees
VALUES ("223e4567-e89b-12d3-a456-556642446011", 160, "Anakin", "Skywalker", "vader",
        "123e4567-e89b-12d3-a452-556645440100",
        true, false, null, 5, 14);
INSERT INTO employees
VALUES ("223e4567-e89b-12d3-a456-556642446012", 162, "SupremeLeader", "Snoke", "snoke",
        "123e4567-e89b-12d3-a452-556645440100",
        true, false, null, 5, 14);
INSERT INTO employees
VALUES ("223e4567-e89b-12d3-a456-556642446013", 165, "General", "Greivous", "greiv",
        "123e4567-e89b-12d3-a452-556645440100",
        true, false, null, 5, 14);
INSERT INTO employees
VALUES ("223e4567-e89b-12d3-a456-556642446014", 168, "Sheev", "Palpatine", "sidious",
        "123e4567-e89b-12d3-a452-556645440100",
        true, false, null, 5, 14);
INSERT INTO employees
VALUES ("223e4567-e89b-12d3-a456-556642446015", 170, "Galen", "Marek", "starkiller",
        "123e4567-e89b-12d3-a452-556645440100",
        true, false, null, 5, 14);
INSERT INTO employees
VALUES ("223e4567-e89b-12d3-a456-556642446016", 172, "Quinlan", "Vos", "quin", "123e4567-e89b-12d3-a452-556645440100",
        true, false, null, 5, 14);
INSERT INTO employees
VALUES ("223e4567-e89b-12d3-a456-556642446017", 173, "jeffrey", "lebowski", "thedude",
        "123e4567-e89b-12d3-a452-556645440100",
        true, false, null, 5, 14);
INSERT INTO employees
VALUES ("223e4567-e89b-12d3-a456-556642446018", 178, "marty", "mcfly", "marty", "123e4567-e89b-12d3-a452-556645440100",
        true, false, null, 5, 14);
INSERT INTO employees
VALUES ("223e4567-e89b-12d3-a456-556642446200", 180, "emmet", "brown", "doc", "123e4567-e89b-12d3-a452-556645440100",
        true, false, null, 5, 14);
INSERT INTO employees
VALUES ("223e4567-e89b-12d3-a456-556642446201", 180, "jennifer", "parker", "jen",
        "123e4567-e89b-12d3-a452-556645440100",
        true, false, null, 5, 14);
INSERT INTO employees
VALUES ("223e4567-e89b-12d3-a456-556642446203", 180, "bilbo", "baggins", "burglar",
        "123e4567-e89b-12d3-a452-556645440100",
        true, false, null, 5, 14);
INSERT INTO employees
VALUES ("223e4567-e89b-12d3-a456-556642446220", 180, "gandalf", "thegrey", "gandalf",
        "123e4567-e89b-12d3-a452-556645440100",
        true, false, null, 5, 14);
INSERT INTO employees
VALUES ("223e4567-e89b-12d3-a456-556642446223", 180, "frodo", "baggins", "sneakyhobbit",
        "123e4567-e89b-12d3-a452-556645440100",
        true, false, null, 5, 14);



DROP TABLE IF EXISTS paygrades;
CREATE TABLE paygrades
(
    id     TINYTEXT,
    salary DECIMAL(10, 2),
    name   TINYTEXT
);

INSERT INTO paygrades
VALUES ("123e4567-e89b-12d3-a336-556642440000", 100, "P1");
INSERT INTO paygrades
VALUES ("123e4567-e89b-12d3-a236-556643440100", 110, "P2");
INSERT INTO paygrades
VALUES ("123e4567-e89b-12d3-a451-556644440000", 120, "P3");
INSERT INTO paygrades
VALUES ("123e4567-e89b-12d3-a452-556645440100", 130, "P4");
INSERT INTO paygrades
VALUES ("123e4567-e89b-12d3-a453-556646440000", 140, "P5");
INSERT INTO paygrades
VALUES ("123e4567-e89b-12d3-a454-556647440100", 150, "P6");
INSERT INTO paygrades
VALUES ("123e4567-e89b-12d3-a455-556648440000", 160, "DS");
INSERT INTO paygrades
VALUES ("123e4567-e89b-12d3-a456-556649440100", 170, "JS");
INSERT INTO paygrades
VALUES ("123e4567-e89b-12d3-a457-556641140100", 180, "XS");

DROP TABLE IF EXISTS credentials;
CREATE TABLE credentials
(
    id       TINYTEXT,
    userName TINYTEXT,
    password TINYTEXT
);

INSERT INTO credentials
VALUES ("123e4567-e89b-12d3-9999-556642440000", "sreid", "password");
INSERT INTO credentials
VALUES ("123e4567-e89b-12d3-8888-556642440100", "ivaganov", "password1");


DROP TABLE IF EXISTS packagecostestimate;
CREATE TABLE packagecostestimate
(
    id                 TINYTEXT,
    workpackage        TINYTEXT,
    employee           TINYTEXT,
    paygrade           TINYTEXT,
    persondaysestimate DECIMAL(10, 2),
    dollarcostestimate DECIMAL(10, 2)
);

INSERT INTO packagecostestimate
VALUES ("123e4567-e89b-12d3-a456-556642430000", "123e4567-e89b-12d3-a456-556342440300", NULL,
        "123e4567-e89b-12d3-a456-556642440000", 10, 100);
INSERT INTO packagecostestimate
VALUES ("123e4537-e89b-12d4-a456-556642430000", "123e4567-e89b-12d3-a456-556342440300", NULL,
        "123e4567-e89b-12d3-a456-556642440000", 5, 50);

DROP TABLE IF EXISTS projects;
CREATE TABLE projects
(
    id             TINYTEXT,
    projectname    TINYTEXT,
    projectnumber  TINYTEXT,
    projmanager    TINYTEXT,
    startdate      DATE,
    enddate        DATE,
    estimatebudget DECIMAL(10, 2),
    markuprate     DECIMAL(10, 2),
    projectbudget  DECIMAL(10, 2)
);

INSERT INTO projects
VALUES ("12121212-e89b-12d3-a456-556642430000", "Death Star Dish", "12345",
        "623e4567-e89b-12d3-a456-556642440710", '2025-03-30',
        '2025-03-30', 10000, 70, 10001);

INSERT INTO projects
VALUES ("12121212-e89b-12d3-a456-556665430000", "Death Star Dish II", "12346",
        "623e4567-e89b-12d3-a456-556642440710", '2025-03-30',
        '2025-03-30', 10000, 70, 10001);

DROP TABLE IF EXISTS workpackageallocation;
CREATE TABLE workpackageallocation
(
    id                 TINYTEXT,
    workpackage        TINYTEXT,
    paygrade           TINYTEXT,
    persondaysestimate DECIMAL(10, 2)
);

INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-aaa1-a456-556642431111", "123e4537-e89b-a1a1-a456-556642431111",
        "123e4567-e89b-12d3-a336-556642440000", 0);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-aaa2-a456-556642431111", "123e4537-e89b-a1a1-a456-556642431111",
        "123e4567-e89b-12d3-a236-556643440100", 0);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-aaa3-a456-556642431111", "123e4537-e89b-a1a1-a456-556642431111",
        "123e4567-e89b-12d3-a451-556644440000", 0);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-aaa4-a456-556642431111", "123e4537-e89b-a1a1-a456-556642431111",
        "123e4567-e89b-12d3-a452-556645440100", 0);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-aaa5-a456-556642431111", "123e4537-e89b-a1a1-a456-556642431111",
        "123e4567-e89b-12d3-a453-556646440000", 0);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-aaa6-a456-556642431111", "123e4537-e89b-a1a1-a456-556642431111",
        "123e4567-e89b-12d3-a454-556647440100", 0);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-aaa7-a456-556642431111", "123e4537-e89b-a1a1-a456-556642431111",
        "123e4567-e89b-12d3-a455-556648440000", 0);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-aaa8-a456-556642431111", "123e4537-e89b-a1a1-a456-556642431111",
        "123e4567-e89b-12d3-a456-556649440100", 0);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-aaa9-a456-556642431111", "123e4537-e89b-a1a1-a456-556642431111",
        "123e4567-e89b-12d3-a457-556641140100", 0);

INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-bbb1-a456-556642431111", "123e4537-e89b-b2b2-a456-556642431111",
        "123e4567-e89b-12d3-a336-556642440000", 0);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-bbb2-a456-556642431111", "123e4537-e89b-b2b2-a456-556642431111",
        "123e4567-e89b-12d3-a236-556643440100", 0);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-bbb3-a456-556642431111", "123e4537-e89b-b2b2-a456-556642431111",
        "123e4567-e89b-12d3-a451-556644440000", 0);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-bbb4-a456-556642431111", "123e4537-e89b-b2b2-a456-556642431111",
        "123e4567-e89b-12d3-a452-556645440100", 0);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-bbb5-a456-556642431111", "123e4537-e89b-b2b2-a456-556642431111",
        "123e4567-e89b-12d3-a453-556646440000", 0);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-bbb6-a456-556642431111", "123e4537-e89b-b2b2-a456-556642431111",
        "123e4567-e89b-12d3-a454-556647440100", 0);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-bbb7-a456-556642431111", "123e4537-e89b-b2b2-a456-556642431111",
        "123e4567-e89b-12d3-a455-556648440000", 0);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-bbb8-a456-556642431111", "123e4537-e89b-b2b2-a456-556642431111",
        "123e4567-e89b-12d3-a456-556649440100", 0);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-bbb9-a456-556642431111", "123e4537-e89b-b2b2-a456-556642431111",
        "123e4567-e89b-12d3-a457-556641140100", 0);

INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-ccc1-a456-556642431111", "123e4537-e89b-c3c3-a456-556642431111",
        "123e4567-e89b-12d3-a336-556642440000", 0);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-ccc2-a456-556642431111", "123e4537-e89b-c3c3-a456-556642431111",
        "123e4567-e89b-12d3-a236-556643440100", 0);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-ccc3-a456-556642431111", "123e4537-e89b-c3c3-a456-556642431111",
        "123e4567-e89b-12d3-a451-556644440000", 0);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-ccc4-a456-556642431111", "123e4537-e89b-c3c3-a456-556642431111",
        "123e4567-e89b-12d3-a452-556645440100", 0);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-ccc5-a456-556642431111", "123e4537-e89b-c3c3-a456-556642431111",
        "123e4567-e89b-12d3-a453-556646440000", 0);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-ccc6-a456-556642431111", "123e4537-e89b-c3c3-a456-556642431111",
        "123e4567-e89b-12d3-a454-556647440100", 0);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-ccc7-a456-556642431111", "123e4537-e89b-c3c3-a456-556642431111",
        "123e4567-e89b-12d3-a455-556648440000", 0);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-ccc8-a456-556642431111", "123e4537-e89b-c3c3-a456-556642431111",
        "123e4567-e89b-12d3-a456-556649440100", 0);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-ccc9-a456-556642431111", "123e4537-e89b-c3c3-a456-556642431111",
        "123e4567-e89b-12d3-a457-556641140100", 0);

INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-ddd1-a456-556642431111", "123e4537-e89b-d4d4-a456-556642431111",
        "123e4567-e89b-12d3-a336-556642440000", 1);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-ddd2-a456-556642431111", "123e4537-e89b-d4d4-a456-556642431111",
        "123e4567-e89b-12d3-a236-556643440100", 2);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-ddd3-a456-556642431111", "123e4537-e89b-d4d4-a456-556642431111",
        "123e4567-e89b-12d3-a451-556644440000", 3);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-ddd4-a456-556642431111", "123e4537-e89b-d4d4-a456-556642431111",
        "123e4567-e89b-12d3-a452-556645440100", 4);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-ddd5-a456-556642431111", "123e4537-e89b-d4d4-a456-556642431111",
        "123e4567-e89b-12d3-a453-556646440000", 5);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-ddd6-a456-556642431111", "123e4537-e89b-d4d4-a456-556642431111",
        "123e4567-e89b-12d3-a454-556647440100", 6);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-ddd7-a456-556642431111", "123e4537-e89b-d4d4-a456-556642431111",
        "123e4567-e89b-12d3-a455-556648440000", 7);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-ddd8-a456-556642431111", "123e4537-e89b-d4d4-a456-556642431111",
        "123e4567-e89b-12d3-a456-556649440100", 8);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-ddd9-a456-556642431111", "123e4537-e89b-d4d4-a456-556642431111",
        "123e4567-e89b-12d3-a457-556641140100", 9);

INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-eee1-a456-556642431111", "123e4537-e89b-e5e5-a456-556642431111",
        "123e4567-e89b-12d3-a336-556642440000", 1);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-eee2-a456-556642431111", "123e4537-e89b-e5e5-a456-556642431111",
        "123e4567-e89b-12d3-a236-556643440100", 2);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-eee3-a456-556642431111", "123e4537-e89b-e5e5-a456-556642431111",
        "123e4567-e89b-12d3-a451-556644440000", 3);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-eee4-a456-556642431111", "123e4537-e89b-e5e5-a456-556642431111",
        "123e4567-e89b-12d3-a452-556645440100", 4);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-eee5-a456-556642431111", "123e4537-e89b-e5e5-a456-556642431111",
        "123e4567-e89b-12d3-a453-556646440000", 5);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-eee6-a456-556642431111", "123e4537-e89b-e5e5-a456-556642431111",
        "123e4567-e89b-12d3-a454-556647440100", 6);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-eee7-a456-556642431111", "123e4537-e89b-e5e5-a456-556642431111",
        "123e4567-e89b-12d3-a455-556648440000", 7);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-eee8-a456-556642431111", "123e4537-e89b-e5e5-a456-556642431111",
        "123e4567-e89b-12d3-a456-556649440100", 8);
INSERT INTO workpackageallocation
VALUES ("123e4537-e89b-eee9-a456-556642431111", "123e4537-e89b-e5e5-a456-556642431111",
        "123e4567-e89b-12d3-a457-556641140100", 9);

DROP TABLE IF EXISTS timesheets;
CREATE TABLE timesheets
(
    id       TINYTEXT,
    employee TINYTEXT,
    end_date DATE,
    sick     DECIMAL(10, 2),
    flex     DECIMAL(10, 2),
    vacation DECIMAL(10, 2),
    approved BOOLEAN
);

INSERT INTO timesheets
VALUES ("111a1111-e89b-12d4-a456-556642431111", "623e4567-e89b-12d3-a456-556642440710", '2021-03-28', 0, 0, 0, true);
INSERT INTO timesheets
VALUES ("111a2222-e89b-12d4-a456-556642431111", "623e4567-e89b-12d3-a456-556642440710", '2021-03-07', 1, 10, 10, false);
INSERT INTO timesheets
VALUES ("111a3333-e89b-12d4-a456-556642431111", "623e4567-e89b-12d3-a456-556642440710", '2021-03-01', 1, 5, 5, false);
INSERT INTO timesheets
VALUES ("111a4444-e89b-12d4-a456-556642431111", "623e4567-e89b-12d3-a456-556642440710", '2021-04-11', 1, 15, 10, false);


DROP TABLE IF EXISTS timesheetrow;
CREATE TABLE timesheetrow
(
    id           TINYTEXT,
    project      TINYTEXT,
    workpackage  TINYTEXT,
    parent_sheet TINYTEXT,
    comments     TINYTEXT,
    mon          DECIMAL(10, 2),
    tue          DECIMAL(10, 2),
    wed          DECIMAL(10, 2),
    thu          DECIMAL(10, 2),
    fri          DECIMAL(10, 2),
    sat          DECIMAL(10, 2),
    sun          DECIMAL(10, 2)
);

INSERT INTO timesheetrow
VALUES ("123e4537-999b-99a1-a456-556642431111",
        "12121212-e89b-12d3-a456-556665430000",
        "123e4537-e89b-a1a1-a456-556642431111",
        "111a1111-e89b-12d4-a456-556642431111",
        "comments were made",
        0,
        0,
        0,
        0,
        0,
        0,
        0);

INSERT INTO timesheetrow
VALUES ("123e4537-888b-88a1-a456-556642431111",
        "12121212-e89b-12d3-a456-556665430000",
        "123e4537-e89b-b2b2-a456-556642431111",
        "111a1111-e89b-12d4-a456-556642431111",
        "comments were not made",
        0,
        0,
        3,
        0,
        0,
        5,
        0);

INSERT INTO timesheetrow
VALUES ("123e4537-997c-76a1-a456-556642431111",
        "12121212-e89b-12d3-a456-556665430000",
        "123e4537-e89b-c3c3-a456-556642431111",
        "111a2222-e89b-12d4-a456-556642431111",
        "comments here",
        0,
        0,
        3,
        0,
        0,
        0,
        2);

INSERT INTO timesheetrow
VALUES ("123e4537-854b-54a1-a456-556642431111",
        "12121212-e89b-12d3-a456-556665430000",
        "123e4537-e89b-e5e5-a456-556642431111",
        "111a2222-e89b-12d4-a456-556642431111",
        "comments out here",
        0,
        6,
        0,
        0,
        0,
        7,
        0);
INSERT INTO timesheetrow
VALUES ("123e4537-777b-54a1-a456-556642432222",
        "12121212-e89b-12d3-a456-556665430000",
        "123e4537-e89b-e5e5-a456-556642431111",
        "111a4444-e89b-12d4-a456-556642431111",
        "Go vacation",
        5,
        5,
        5,
        10,
        0,
        0,
        0);

DROP TABLE IF EXISTS recepackage;
CREATE TABLE recepackage
(
    id                TINYTEXT,
    parentwp          TINYTEXT,
    paygrade          TINYTEXT,
    persondayestimate DECIMAL(10, 2),
    employeeId        TINYTEXT,
    wp                TINYTEXT
);

INSERT INTO recepackage
VALUES ("123e4537-e89b-21a6-a456-550000000000",
        "123e4537-e89b-b2b2-a456-556642431111",
        "123e4567-e89b-12d3-a451-556644440000",
        1,
        "223e4567-e89b-12d3-a456-556642446010",
        "123e4537-e89b-a1a1-a456-556642431111");

DROP TABLE IF EXISTS workpackages;
CREATE TABLE workpackages
(
    id                  TINYTEXT,
    workpackagenumber   TINYTEXT,
    parentworkpackage   TINYTEXT,
    projectbudget       DECIMAL(10, 2),
    completedbudget     DECIMAL(10, 2),
    completedpersondays DECIMAL(10, 2),
    startdate           DATE,
    enddate             DATE,
    isleaf              BOOLEAN,
    project             TINYTEXT,
    responsibleengineer TINYTEXT
);

INSERT INTO workpackages
VALUES ("123e4537-e89b-a1a1-a456-556642431111",
        "10000",
        NULL,
        0,
        0,
        0,
        '2021-03-07',
        '2021-03-14',
        false,
        "12121212-e89b-12d3-a456-556665430000",
        NULL);

INSERT INTO workpackages
VALUES ("123e4537-e89b-b2b2-a456-556642431111",
        "11000",
        "123e4537-e89b-a1a1-a456-556642431111",
        25000,
        0,
        0,
        '2021-03-07',
        '2021-03-14',
        false,
        "12121212-e89b-12d3-a456-556665430000",
        NULL);

INSERT INTO workpackages
VALUES ("123e4537-e89b-c3c3-a456-556642431111",
        "12000",
        "123e4537-e89b-a1a1-a456-556642431111",
        25000,
        0,
        0,
        '2021-03-07',
        '2021-03-14',
        false,
        "12121212-e89b-12d3-a456-556665430000",
        NULL);

INSERT INTO workpackages
VALUES ("123e4537-e89b-d4d4-a456-556642431111",
        "12100",
        "123e4537-e89b-c3c3-a456-556642431111",
        25000,
        0,
        0,
        '2021-03-07',
        '2021-03-14',
        true,
        "12121212-e89b-12d3-a456-556665430000",
        "623e4567-e89b-12d3-a456-556642440710");

INSERT INTO workpackages
VALUES ("123e4537-e89b-e5e5-a456-556642431111",
        "12200",
        "123e4537-e89b-c3c3-a456-556642431111",
        0,
        0,
        0,
        '2021-03-07',
        '2021-03-14',
        true,
        "12121212-e89b-12d3-a456-556665430000",
        "223e4567-e89b-12d3-a456-556642446010");
