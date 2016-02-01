#!/usr/bin/python2.4
#
# Small script to show PostgreSQL and Pyscopg together
#
import sys
sys.path.append("..")
from common import randop

def create_sql_select_all():
    sql = '''SELECT * from COMPANY'''
    return sql


def create_sql_select_where():
    var_age = randop.create_random_int(1,100)
    sql = '''SELECT * from COMPANY where age > %d'''%var_age
    return sql


######################################
#     Test                           #
######################################
if __name__ == '__main__':
    a = create_sql_select_all()
    b = create_sql_select_where()
    print (a)
    print (b)
