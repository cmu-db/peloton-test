
import os
import sys
import psycopg2
import unittest
import logging

from ConfigParser import RawConfigParser

LOG = logging.getLogger(__name__)

class BaseTest(unittest.TestCase):
    """Base test case code that will connect to the oracle and target database"""
    DB_ORACLE = "oracle"
    DB_TARGET = "target"
    
    def __init__(self, configPath, baseName):
        # Basename for all tables in this test
        self.baseName = baseName.lower()
        
        # Load in configuration file
        self.config = RawConfigParser()
        self.config.read(configPath)
        
        # Initialize database connections
        for db in [BaseTest.DB_ORACLE, BaseTest.DB_TARGET]:
            try:
                self.__dict__[db] = psycopg2.connect(
                    host=self.config.get(db, 'db_host'),
                    port=self.config.get(db, 'db_port'),
                    database=self.config.get(db, 'db_name'), 
                    user=self.config.get(db, 'db_user'),
                    password=self.config.get(db, 'db_pass'),
                )
            except:
                LOG.error("Unable to connect to %s database" % db.upper())
                raise
            assert(not self.__dict__[db] is None)
            LOG.debug("Connected to %s database" % db.upper())
        ## FOR
    ## DEF
    
    def getTestTables(self, db):
        cursor = self.__dict__[db].cursor()
        cursor.execute("""
            SELECT table_name FROM information_schema.tables
             WHERE table_name LIKE %s
               AND table_catalog !~ '^(pg_|sql_)'
            """, (self.baseName+'_%',))
        return [ x for x in cursor.fetchall() ]
    ## DEF
    
    def dropTables(self):
        for db in [BaseTest.DB_ORACLE, BaseTest.DB_TARGET]:
            LOG.debug("Dropping %s.%s tables" % (db, self.baseName))
            cursor = self.__dict__[db].cursor()
            for tableName in self.getTestTables(db):
                cursor.execute("DROP TABLE IF EXISTS %s" % tableName)
        ## FOR
    ## DEF
    
    def setUp(self):
        pass
    ## DEF
    
## CLASS