CREATE TABLE foo (
    id INT,
    value VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE bar (
    id INT,
    num INT,
    PRIMARY KEY (id)
);

INSERT INTO foo VALUES (42, 'foo');
INSERT INTO foo VALUES (15, 'bar');
INSERT INTO bar VALUES (10, 20);
INSERT INTO bar VALUES (11, 25);