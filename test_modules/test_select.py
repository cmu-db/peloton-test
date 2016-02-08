#!/usr/bin/python2.4
#
# Small script to show PostgreSQL and Pyscopg together
#

import sys
import os
import psycopg2
import filecmp

from basetest import BaseTest

from sql import SELECT
from common import fileop
from common import dbop


class TestSelect(BaseTest):
    
    def setUp(self):
        pass
        
    def test_select_where():
        SQL = SELECT.create_sql_select_where()
        pgfile = dbop.sql_exe_pg_tofile('../peloton_test.conf', SQL, 'select_where.out')
        ptfile = dbop.sql_exe_pt_tofile('../peloton_test.conf', SQL, 'select_where.out')
        res = fileop.compare_results(pgfile, ptfile)
        self.assertTrue(res)
    ## DEF

    def test_select_all():
        SQL = SELECT.create_sql_select_all()
        pgfile = dbop.sql_exe_pg_tofile('../peloton_test.conf', SQL, 'select_all.out')
        ptfile = dbop.sql_exe_pt_tofile('../peloton_test.conf', SQL, 'select_all.out')
        res = fileop.compare_results(pgfile, ptfile)
        self.assertTrue(res)
    ## DEF

    def test_select_where_withoutfile():
        SQL = SELECT.create_sql_select_where()
        pgres = dbop.sql_exe_pg('../peloton_test.conf', SQL)
        ptres = dbop.sql_exe_pt('../peloton_test.conf', SQL)
        selt.assertTrue(pgres)
    ## DEF
    
## CLASS
