#!/usr/bin/python2.4
#
# Small script to show PostgreSQL and Pyscopg together
#

import psycopg2
import os
import filecmp
import fileop

##################################################
#             Postgres Execution                 #
##################################################
def sql_exe_pg(conf_path, SQL, filename):

    path = '../output/'
    file_path = path + filename
    addr = fileop.parse_conf_peloton(conf_path)
    
    try:
        # Connect to database
        conn = psycopg2.connect(database=addr['database'], user=addr['user'], password=addr['password'], host=addr['host'], port=addr['port'])
        print "Connect testdb successfully"
        
        # Get the db handler
        cur = conn.cursor()

        # Execute SQL statement
        cur.execute(SQL)

        # Get the data
        rows = cur.fetchall()

        # create folder if not exists
        fileop.create_output_dir(path) 
        
        # open a file
        f = open(file_path,'w')

        # write the data
        for row in rows:
            row_line = '%d %s %d %s'%row
            f.write(row_line)
            f.write('\n')
            print (row_line)

    except psycopg2.DatabaseError, e:
        print "Unable to execute postgres:%s"%e
        sys.exit(1)
    except IOError, e:
        print "Error %d: %s" % (e.args[0], e.args[1])
        sys.exit(1)
    
    finally:
        # close the file
        if f:
            f.close()
        # close the connection with db
        if conn:
            conn.close()

    return file_path

##################################################
#             Peloton  Execution                 #
##################################################
def sql_exe_pt(conf_path, SQL, filename):

    path = '../output/'
    file_path = path + filename
    addr = fileop.parse_conf_peloton(conf_path)

    try:
        # Connect to database
        conn = psycopg2.connect(database=addr['database'], user=addr['user'], password=addr['password'], host=addr['host'], port=addr['port'])
        print "Connect testdb successfully"

        # Get the db handler
        cur = conn.cursor()

        # Execute SQL statement
        cur.execute(SQL)

        # Get the data
        rows = cur.fetchall()

        # create folder if not exists
        fileop.create_output_dir(path)

        # open a file
        f = open(file_path,'w')

        # write the data
        for row in rows:
            row_line = '%d %s %d %s'%row
            f.write(row_line)
            f.write('\n')
            print (row_line)

    except psycopg2.DatabaseError, e:
        print "Unable to execute peloton:%s"%e
        sys.exit(1)
    except IOError, e:
        print "Error %d: %s" % (e.args[0], e.args[1])
        sys.exit(1)

    finally:
        # close the file
        if f:
            f.close()
        # close the connection with db
        if conn:
            conn.close()

    return file_path

######################################
#     Test                           #
######################################
if __name__ == '__main__':
    a = sql_exe_pg('../peloton_test.conf', 'SELECT * FROM company', 'pg_in.out')
    b = sql_exe_pt('../peloton_test.conf', 'SELECT * FROM company', 'pt_in.out')
    print (a)
    print (b)
