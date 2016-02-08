
import psycopg2
import unittest
import logging

LOG = logging.getLogger(__name__)

class BaseTest(unittest.TestCase):
    
    def __init__(self, config):
        
        setupVars = {
            "oracle": [ ],
            "target": [ ],
        }
        
        for db in setupVars.keys():
            conn = None
            try:
                conn = psycopg2.connect(database=config.get(db, ['db_name']), 
                                        user=config.get(db, ['db_user']),
                                        password=config.get(db, ['db_pass']),
                                        host=config.get(db, ['db_host']),
                                        port=config.get(db, ['db_port']))
            except:
                LOG.error("Unable to connect to %s database" % db.upper())
                raise
            assert(not conn is None)
            self.__dict__[db] = conn
        ## FOR
    ## DEF

    
    def setUp(self):
        pass
    ## DEF
    
## CLASS

if __name__ == '__main__':
    print "HELLO"

## IF
