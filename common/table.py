# -*- coding: utf-8 -*-

import os
import sys
import logging
import sqlalchemy 

from basetest import LOG

# ==================================================================

SQL_TYPES = {
    "STRING": {
        "CHAR": sqlalchemy.types.String,
        "VARCHAR": sqlalchemy.types.String,
        "TEXT": sqlalchemy.types.Text,
        "VARBINARY": None,
    },
    "NUMERIC": {
        "TINYINT": None,
        "SMALLINT": None,
        "MEDIUMINT": None,
        "INT": sqlalchemy.types.Integer,
        "BIGINT": sqlalchemy.types.BigInteger,
    },
    "DECIMAL": {
        "DECIMAL": None,
        "NUMERIC": sqlalchemy.types.Numeric,
        "REAL": None,
        "FLOAT": sqlalchemy.types.Float,
    },
    "TIMESTAMP": {
        "TIMESTAMP": None,
        "TIME": sqlalchemy.types.Time,
        "DATE": sqlalchemy.types.Date,
        "DATETIME": sqlalchemy.types.DateTime,
    },
    "MISC": {
        "BOOLEAN": sqlalchemy.types.Boolean,
    }
}
ALL_TYPES = [ ]
ALL_TYPES_MAPPINGS = { }
for category in SQL_TYPES:
    for x,y in SQL_TYPES[category].items():
        varName = "TYPE_%s" % x
        globals()[varName] = x
        ALL_TYPES_MAPPINGS[x] = y
        if not y is None: ALL_TYPES.append(x)
## FOR

# ==================================================================


class Table():
    
    def __init__(self, tableName, conn):
        def getconn(): return (conn)
        engine = sqlalchemy.create_engine('postgresql+psycopg2://', creator=getconn)
        
        self.engine = engine
        self.metadata = sqlalchemy.MetaData(bind=self.engine)
        self.tableName = tableName
        self.table = sqlalchemy.Table(self.tableName, self.metadata)
        self.attributeCtr = 0
    ## DEF
    
    def __nextName(self):
        self.attributeCtr += 1
        return "attr_%02d" % self.attributeCtr
    ## DEF
    
    def addAttribute(self, attrType, primaryKey=False, attrLength=None, attrNull=True, attrUnique=False):
        if not attrType in ALL_TYPES:
            raise Exception("Unknown type '%s'" % attrType)
        if ALL_TYPES_MAPPINGS[attrType] is None:
            raise Exception("Unsupported type '%s'" % attrType)
        
        attrName = self.__nextName()
        targetAttrType = "sqlalchemy.sql.sqltypes.%s" % ALL_TYPES_MAPPINGS[attrType].__name__
        targetAttrType += "()" if attrLength is None else "(%d)" % attrLength
        attrType = eval(targetAttrType)

        attr = sqlalchemy.Column(attrName, attrType, 
                                 primary_key=primaryKey,
                                 nullable=attrNull,
                                 unique=attrUnique)
        self.table.append_column(attr)
        LOG.debug("Added attribute %s" % (attr))
    ## DEF
        
    def create(self):
        assert not self.table is None
        LOG.debug("Creating table '%s'" % self.tableName)
        if self.table.exists():
            self.table.drop(checkfirst=False)
        self.table.create()
    ## DEF
    
    
    
## CLASS
    