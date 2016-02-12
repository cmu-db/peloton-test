# -*- coding: utf-8 -*-

import os
import sys
import logging

LOG = logging.getLogger(__name__)

# ==================================================================

SQL_TYPES = {
    "STRING": [
        "CHAR",
        "VARCHAR",
        "TEXT",
        "VARBINARY",
    ],
    "NUMERIC": [
        "TINYINT",
        "SMALLINT",
        "INT",
        "BIGINT",
    ],
    "DECIMAL": [
        "DECIMAL",
        "NUMERIC",
        "REAL",
    ],
    "TIMESTAMP": [
        "TIMESTAMP",
        "DATE",
    ],
}
ALL_TYPES = [ ]
for category in SQL_TYPES:
    for x in SQL_TYPES[category]:
        varName = "TYPE_%s" % x
        globals()[varName] = SQL_TYPES[category]
        ALL_TYPES.append(SQL_TYPES[category])
## FOR

# ==================================================================


class TableBuilder():
    
    def __init__(self, tableName):
        self.tableName = tableName
        self.attributes = [ ]
        self.attributeCtr = 0
    ## DEF
    
    def __nextName(self):
        self.attributeCtr += 1
        return "attr_%02d" % self.attributeCtr
    ## DEF
    
    def addIntColumn(self, unique=False):
        attr = Attribute(self.__nextName(), TYPE_INT, attrUnique=unique)
        self.attributes.append(attr)
    ## DEF
        
    
    
    
    
## CLASS

class Attribute():
    
    def __init__(self, attrName, attrType, attrLength=None, attrUnique=False):
        assert(attrType in ALL_TYPES)
        self.attrName = attrName
        self.attrType = attrType
    ## DEF
    
    
## CLASS
    