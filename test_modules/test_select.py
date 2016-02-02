#!/usr/bin/python2.4
#
# Small script to show PostgreSQL and Pyscopg together
#

import psycopg2
import os
import filecmp
import sys
sys.path.append("..")
from sql import SELECT
from common import fileop
from common import dbop

#####################################################
#              Test                                 #
#####################################################
    
def test_select_where():
    SQL = SELECT.create_sql_select_where()
    pgfile = dbop.sql_exe_pg('../peloton_test.conf', SQL, 'select_where.pg')
    ptfile = dbop.sql_exe_pt('../peloton_test.conf', SQL, 'select_where.pt')
    res = fileop.compare_results(pgfile, ptfile)
    assert(res == True)

def test_select_all():
    SQL = SELECT.create_sql_select_all()
    pgfile = dbop.sql_exe_pg('../peloton_test.conf', SQL, 'select_all.pg')
    ptfile = dbop.sql_exe_pt('../peloton_test.conf', SQL, 'select_all.pt')
    res = fileop.compare_results(pgfile, ptfile)
    assert(res == True)
