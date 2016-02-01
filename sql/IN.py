#!/usr/bin/python2.4
#
# Small script to show PostgreSQL and Pyscopg together
#
import sys
sys.path.append("..")
from common import utils

def create_sql_in_withindex():
    var_age = utils.create_random_int(1,100)
    sql = '''SELECT * from COMPANY where age > %d'''%var_age
    return sql

def create_sql_in_noindex():
    var_age = utils.create_random_int(1,100)
    sql = '''SELECT * from COMPANY where age > %d'''%var_age


######################################
#     Test                           #
######################################
if __name__ == '__main__':
    a = create_sql_in_withindex()
    print (a)
