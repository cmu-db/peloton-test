
import os
import sys
import psycopg2
import unittest
import logging

from ConfigParser import RawConfigParser

LOG = logging.getLogger(__name__)

class BaseTest(unittest.TestCase):
    """Base test case code that will connect to the oracle and target database"""
    
    def __init__(self, configPath):
        
        # Load in configuration file
        self.config = RawConfigParser()
        self.config.read(configPath)
        
        # Initialize database connections
        for db in ["oracle", "target"]:
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
    
    def setUp(self):
        pass
    ## DEF
    
## CLASS

if __name__ == '__main__':
    BaseTest(os.path.realpath("../test.conf"))

## IF
