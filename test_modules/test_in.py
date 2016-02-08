#!/usr/bin/python2.4
#
# Small script to show PostgreSQL and Pyscopg together
#

import psycopg2
import os
import filecmp
import sys

from sql import IN
from common import fileop
from common import dbop

#              Test                                 #
#####################################################
#def setUp():
#    sql_pg()
#    sql_pt()
    
def test_in_withindex():
    SQL = IN.create_sql_in_withindex()
    pgfile = dbop.sql_exe_pg('../peloton_test.conf', SQL, 'in_index.out')
    ptfile = dbop.sql_exe_pt('../peloton_test.conf', SQL, 'in_index.out')
    res = fileop.compare_results(pgfile, ptfile)
    assert(res == True)

def test_in_noindex():
    SQL = IN.create_sql_in_noindex()
    pgfile = dbop.sql_exe_pg('../peloton_test.conf', SQL, 'in_noindex.out')
    ptfile = dbop.sql_exe_pt('../peloton_test.conf', SQL, 'in_noindex.out')
    res = fileop.compare_results(pgfile, ptfile)
    assert(res == True)

def test_in_noanyindex():
    SQL = IN.create_sql_in_noanyindex()
    pgfile = dbop.sql_exe_pg('../peloton_test.conf', SQL, 'in_noanyindex.out')
    ptfile = dbop.sql_exe_pt('../peloton_test.conf', SQL, 'in_noanyindex.out')
    res = fileop.compare_results(pgfile, ptfile)
    assert(res == True)

