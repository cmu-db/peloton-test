# -*- coding: utf-8 -*-

import os, sys
basedir = os.path.realpath(os.path.dirname(__file__))
sys.path.append(os.path.join(basedir, ".."))

import psycopg2
import filecmp

from common import BaseTest
from sql import SELECT
from common import fileop
from common import dbop


class TestSelect(BaseTest):
    
    def __init__(self, configPath):
        BaseTest.__init__(self, configPath, self.__class__.__name__)
    
    def setUp(self):
        pass
        
    def test_select_where(self):
        SQL = SELECT.create_sql_select_where()
        pgfile = dbop.sql_exe_pg_tofile('../peloton_test.conf', SQL, 'select_where.out')
        ptfile = dbop.sql_exe_pt_tofile('../peloton_test.conf', SQL, 'select_where.out')
        res = fileop.compare_results(pgfile, ptfile)
        self.assertTrue(res)
    ## DEF

    def test_select_all(self):
        SQL = SELECT.create_sql_select_all()
        pgfile = dbop.sql_exe_pg_tofile('../peloton_test.conf', SQL, 'select_all.out')
        ptfile = dbop.sql_exe_pt_tofile('../peloton_test.conf', SQL, 'select_all.out')
        res = fileop.compare_results(pgfile, ptfile)
        self.assertTrue(res)
    ## DEF

    def test_select_where_withoutfile(self):
        SQL = SELECT.create_sql_select_where()
        pgres = dbop.sql_exe_pg('../peloton_test.conf', SQL)
        ptres = dbop.sql_exe_pt('../peloton_test.conf', SQL)
        selt.assertTrue(pgres)
    ## DEF
    
## CLASS

if __name__ == '__main__':
    x = TestSelect(os.path.realpath("../test.conf"))
    from pprint import pprint
    pprint(sorted(x.getTestTables("target")))
## IF

