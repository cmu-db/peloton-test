# -*- coding: utf-8 -*-

import os
import sys
basedir = os.path.realpath(os.path.dirname(__file__))
sys.path.append(os.path.join(basedir, "..", ".."))

import common
LOG = common.LOG

# FIXME
# Need to figure out how to get the config path from the commandline
configPath = os.path.realpath(os.path.join(basedir, "../../test.conf"))

class TestCreateTable(common.BaseTest):
    
    def __init__(self, testName):
        common.BaseTest.__init__(self, testName, configPath, self.__class__.__name__)

    def testSingleAttribute(self):
        """Check that the DBMS supports tables with a single attribute with and without a pkey"""
        for primaryKey in [True, False]:
            for attrType in common.ALL_TYPES:
                tableName = self.nextTableName()
                LOG.debug("%s -> %s // primaryKey=%s" % (tableName, attrType, primaryKey))

                for conn in self.getConnections():
                    t = common.Table(tableName, conn)
                    t.addAttribute(attrType, primaryKey=primaryKey)
                    t.create()

                # Check to make sure that the table was created
                self.assertIn(tableName, self.getTestTables(common.DB_ORACLE))
                self.assertIn(tableName, self.getTestTables(common.DB_TARGET))
                LOG.debug("%s -> CREATED!" % tableName)
    ## DEF
        
        
    
    def testNotNulls(self):
        """Check that the DBMS supports all of the different types as not null"""
        for attrType in common.ALL_TYPES:
            tableName = self.nextTableName()
            LOG.info("%s -> %s // NULL" % (tableName, attrType))

            for conn in self.getConnections():
                t = common.Table(tableName, conn)
                t.addAttribute("INT", primaryKey=True)
                t.addAttribute(attrType, primaryKey=False, attrNull=True)
                t.create()

            # Check to make sure that the table was created
            self.assertIn(tableName, self.getTestTables(common.DB_ORACLE))
            self.assertIn(tableName, self.getTestTables(common.DB_TARGET))
            LOG.info("%s -> CREATED!" % tableName)
    ## DEF
## CLASS