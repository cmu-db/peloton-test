# -*- coding: utf-8 -*-

import os
import sys
basedir = os.path.realpath(os.path.dirname(__file__))
sys.path.append(os.path.join(basedir, "..", ".."))

from common import BaseTest
from common import Table

import logging
LOG = logging.getLogger()

class TestCreateTable(BaseTest):
    
    def __init__(self, configPath):
        BaseTest.__init__(self, configPath, self.__class__.__name__)
    
    def setUp(self):
        self.dropTables()
    
    def testSingleAttribute(self):
        """Check that the DBMS supports tables with a single attribute with and without a pkey"""
        
        for primaryKey in [False, True]:
            for attrType in ALL_TYPES:
                tableName = self.nextTableName()
                LOG.info("%s -> %s // primaryKey=%s" % (tableName, attrType, primaryKey))
                
                t = Table(tableName, self.getOracleConn())
                t.addAttribute(attrType, primaryKey=primaryKey)
                t.create()
                
                # Check to make sure that the table was created
                self.assertIn(tableName, self.getTestTables())
                LOG.info("%s -> CREATED!" % tableName)
            ## FOR
        ## FOR
    ## DEF
        
        
    
    def testNotNulls(self):
        """Check that the DBMS supports all of the different types as not null"""
        pass
    ## DEF
## CLASS