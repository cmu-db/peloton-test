#!/usr/bin/python2.4
#
# Small script to show PostgreSQL and Pyscopg together
#

import os
import random
import filecmp
import ConfigParser
from string import ascii_uppercase


def create_output():    
    # change to the current path
    os.chdir('.')

    # if output dir exists
    if( os.path.exists('../output') == False ):
        os.mkdir("../output")


def create_output_dir(path):
    # change to the current path
    os.chdir('.')

    # if output dir exists
    if( os.path.exists(path) == False ):
        os.mkdir(path)

###  Compare File ###

def compare_results(file_path1, file_path2):
    try:
        res = filecmp.cmp(file_path1, file_path2)

        if(res == True):
            print "True"
        else:
            print "False"
    except IOError, e:
        print "Error %d: %s" % (e.args[0], e.args[1])
        sys.exit(1)

    return res

def parse_conf_postgres(file_path):
    
    try:
        # open config file
        f = ConfigParser.ConfigParser()
        f.read(file_path)
        addr = {}
        addr["host"] = f.get("postgres", "host_postgres")
        addr["port"] = f.getint("postgres", "port_postgres")
        addr["user"] = f.get("postgres", "user_postgres")
        addr["password"] = f.get("postgres", "password_postgres")
        addr["database"] = f.get("postgres", "database_postgres")

    except IOError, e:
        print "Error %d: %s" % (e.args[0], e.args[1])
        sys.exit(1)
    
    return addr     

def parse_conf_peloton(file_path):
    try:
        # open config file
        f = ConfigParser.ConfigParser()
        f.read(file_path)

        addr = {}
        addr["host"] = f.get("peloton", "host_peloton")
        addr["port"] = f.getint("peloton", "port_peloton")
        addr["user"] = f.get("peloton", "user_peloton")
        addr["password"] = f.get("peloton", "password_peloton")
        addr["database"] = f.get("peloton", "database_peloton")
    
    except IOError, e:
        print "Error %d: %s" % (e.args[0], e.args[1])
        sys.exit(1)
    
    return addr
    
######################################
if __name__ == '__main__':
    #create_output()
    #create_output_dir('../aaaaaaaaaaaaaaaaaaaaaaaa')
    b = compare_results('../output/a.out','../output/b.out')
    print (b)
    addr = parse_conf_peloton('../peloton_test.conf')
    print (addr)
