#!/usr/bin/python2.4
#
# Small script to show PostgreSQL and Pyscopg together
#
import sys
sys.path.append("..")
from common import randop

def create_sql_in_withindex():
    name = randop.create_random_string(6)
    sql = "SELECT * from COMPANY where id IN (SELECT id FROM CORP where name = 'Paul')"
    return sql

def create_sql_in_noindex():
    rowkey = randop.create_random_int(1,10000)
    sql = "SELECT * from COMPANY where age IN (SELECT age FROM CORP where id < %d )"%rowkey
    return sql

def create_sql_in_noanyindex():
    name = randop.create_random_string(6)
    sql = "SELECT * from COMPANY where age IN (SELECT age FROM CORP where name = 'Paul')"
    return sql
######################################
#     Test                           #
######################################
if __name__ == '__main__':
    a = create_sql_in_withindex()
    print (a)
