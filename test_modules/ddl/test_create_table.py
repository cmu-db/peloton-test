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
                    LOG.debug("%s -> CREATED!" % tableName)

                    # Check to make sure that the table was created
                    self.assertIn(tableName, self.getTestTables(conn))
    ## DEF

    def testNotNulls(self):
        """Check that the DBMS supports all of the different types as not null"""
        for attrType in common.ALL_TYPES:
            tableName = self.nextTableName()
            LOG.debug("%s -> %s // NULL" % (tableName, attrType))

            for conn in self.getConnections():
                t = common.Table(tableName, conn)
                t.addAttribute("INT", primaryKey=True)
                t.addAttribute(attrType, primaryKey=False, attrNull=True)
                t.create()
                LOG.debug("%s -> CREATED!" % tableName)

                # Check to make sure that the table was created
                self.assertIn(tableName, self.getTestTables(conn))
    ## DEF

    def testUnique(self):
        """Check that the DBMS supports all of the different types as unique"""
        for attrType in common.ALL_TYPES:
            tableName = self.nextTableName()
            LOG.debug("%s -> %s // NULL" % (tableName, attrType))

            for conn in self.getConnections():
                t = common.Table(tableName, conn)
                t.addAttribute("INT", primaryKey=True)
                t.addAttribute(attrType, primaryKey=False, attrNull=False, attrUnique=True)
                t.create()
                LOG.debug("%s -> CREATED!" % tableName)

                # Check to make sure that the table was created
                self.assertIn(tableName, self.getTestTables(conn))
    ## DEF

    def testUniqueMulti(self):
        """Check that the DBMS supports different sets of unique attributes"""
        for attrType2 in common.ALL_TYPES:
            for attrType3 in common.ALL_TYPES:
                tableName = self.nextTableName()
                for conn in self.getConnections():
                    t = common.Table(tableName, conn)
                    attrName1 = t.addAttribute("INT", primaryKey=True)
                    attrName2 = t.addAttribute(attrType2, primaryKey=False)
                    attrName3 = t.addAttribute(attrType3, primaryKey=False)
                    t.addUniqueConstraint(attrName1, attrName2, attrName3)
                    t.create()
                    LOG.debug("%s -> CREATED!" % tableName)

                    # Check to make sure that the table was created
                    self.assertIn(tableName, self.getTestTables(conn))
    ## DEF

## CLASS