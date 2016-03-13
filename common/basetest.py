
import os
import sys
import psycopg2
import unittest
import logging
from pprint import pprint

from ConfigParser import RawConfigParser

LOG = logging.getLogger()
LOG_handler = logging.StreamHandler()
LOG_formatter = logging.Formatter(fmt='%(asctime)s [%(funcName)s:%(lineno)03d] %(levelname)-5s: %(message)s',
                                  datefmt='%m-%d-%Y %H:%M:%S')
LOG_handler.setFormatter(LOG_formatter)
LOG.addHandler(LOG_handler)
LOG.setLevel(logging.INFO)

DB_ORACLE = "oracle"
DB_TARGET = "target"

class BaseTest(unittest.TestCase):
    """Base test case code that will connect to the oracle and target database"""
    
    def __init__(self, testName, configPath, baseName):
        unittest.TestCase.__init__(self, testName)
        self.tableCtr = 0
        
        # Basename for all tables in this test
        self.baseName = baseName.lower()
        
        # Load in configuration file
        LOG.info("Loading config file '%s'" % configPath)
        self.config = RawConfigParser()
        self.config.read(configPath)
        
        # Initialize database connections
        for db in [DB_ORACLE, DB_TARGET]:
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
    
    def nextTableName(self):
        name = "%s_%02d" % (self.baseName, self.tableCtr)
        self.tableCtr += 1
        return name
    ## DEF
    
    def getTargetConn(self):
        return self.__dict__[DB_TARGET]

    def getOracleConn(self):
        return self.__dict__[DB_ORACLE]
    
    def getTestTables(self, db):
        cursor = self.__dict__[db].cursor()
        cursor.execute("""
            SELECT table_name FROM information_schema.tables
             WHERE table_name LIKE %s
               AND table_catalog !~ '^(pg_|sql_)'
            """, (self.baseName+'_%',))
        return [ x[0] for x in cursor.fetchall() ]
    ## DEF
    
    def dropTables(self):
        for db in [DB_ORACLE, DB_TARGET]:
            LOG.debug("Dropping %s.%s tables" % (db, self.baseName))
            cursor = self.__dict__[db].cursor()
            for tableName in self.getTestTables(db):
                cursor.execute("DROP TABLE IF EXISTS %s" % tableName)
        ## FOR
    ## DEF
    
    def setUp(self):
        #self.dropTables()
        pass
    ## DEF
    
    def tearDown(self):
        self.dropTables()
        pass
    ## DEF
    
## CLASS