#!/usr/bin/python2.4
#
# Small script to show PostgreSQL and Pyscopg together
#

import sys
import psycopg2

from common import randop
from common import fileop


addr = fileop.parse_conf_peloton('../peloton_test.conf')

try:
    # Connect to database
    conn = psycopg2.connect(database=addr['database'], user=addr['user'], password=addr['password'], host=addr['host'], port=addr['port'])
    print "Connect db:%s successfully"%addr['database']
except:
    print "I am unable to connect to the database"

cur = conn.cursor()
try:
    cur.execute("INSERT INTO CORP (ID,NAME,AGE,ADDRESS) \
          VALUES (1, 'Paul', 28, 'California')");

    cur.execute("INSERT INTO CORP (ID,NAME,AGE,ADDRESS) \
          VALUES (2, 'Allen', 12, 'Texas')");

    cur.execute("INSERT INTO CORP (ID,NAME,AGE,ADDRESS) \
          VALUES (3, 'Teddy', 32, 'Norway')");

    cur.execute("INSERT INTO CORP (ID,NAME,AGE,ADDRESS) \
          VALUES (4, 'Mark', 29, 'Rich-Mond ')");
    conn.commit()
    print "Data inserted successfully"
except:
    print "Can not INSERT data"

try:
    for rowkey in range(5, 10001):
        name = randop.create_random_string(6)
        age = randop.create_random_int(1,80)
        address = randop.create_random_string(12)
        query = "INSERT INTO CORP (ID,NAME,AGE,ADDRESS) VALUES (%s, %s, %s, %s);"
        data = (rowkey, name, age, address)
        print (query)
        print (data)
        cur.execute(query, data)

    conn.commit()
    print "Batch insert successfully"
except:
    print "Error in batch insert"
    
conn.close()
