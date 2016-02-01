#!/usr/bin/python2.4
#
# Small script to show PostgreSQL and Pyscopg together
#

import psycopg2

try:
    conn = psycopg2.connect(database="test", user="tester", password="123", host="127.0.0.1", port="57721")
    print "Connect testdb successfully"
except:
    print "I am unable to connect to the database"

cur = conn.cursor()
try:
    cur.execute("INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS) \
          VALUES (1, 'Paul', 32, 'California')");

    cur.execute("INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS) \
          VALUES (2, 'Allen', 25, 'Texas')");

    cur.execute("INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS) \
          VALUES (3, 'Teddy', 23, 'Norway')");

    cur.execute("INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS) \
          VALUES (4, 'Mark', 25, 'Rich-Mond ')");
    conn.commit()
    print "Data inserted successfully"
except:
    print "Can not INSERT data"

conn.close()
