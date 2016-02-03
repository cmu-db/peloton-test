#!/usr/bin/python2.4
#
# Small script to show PostgreSQL and Pyscopg together
#

import psycopg2
import os
import filecmp
import fileop
from collections import Counter

##################################################
#             Postgres Execution                 #
##################################################
def sql_exe_pg_tofile(conf_path, SQL, filename):

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

### Execute SQL and put the result into multiset(counter) ### 
def sql_exe_pg(conf_path, SQL):

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
        
        res = Counter()
        # write the data into multiset
        for row in rows:
            res[row] = res[row] + 1
            print (row)

    except psycopg2.DatabaseError, e:
        print "Unable to execute postgres:%s"%e
        sys.exit(1)

    finally:
        if conn:
            conn.close()

    return res


##################################################
#             Peloton  Execution                 #
##################################################
def sql_exe_pt_tofile(conf_path, SQL, filename):

    path = '../output/'
    file_path = path + filename
    addr = fileop.parse_conf_peloton(conf_path)

    try:
        # Connect to database
        conn = psycopg2.connect(database=addr['database'], user=addr['user'], password=addr['password'], host=addr['host'], port=addr['port'])
        print "Connect testdb successfully"

        # Get the db handler
        cur = conn.cursor()

        # Insert data
        insert_data(conn, 'company', 100)
        insert_data(conn, 'corp', 100)

        # Execute SQL statement
        cur.execute(SQL)
        
        # Insert data
        insert_data(conn, 'company', 100)
        insert_data(conn, 'corp', 100)

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

### Execute SQL and put the result into multiset(counter) ### 
def sql_exe_pt(conf_path, SQL):

    addr = fileop.parse_conf_peloton(conf_path)

    try:
        # Connect to database
        conn = psycopg2.connect(database=addr['database'], user=addr['user'], password=addr['password'], host=addr['host'], port=addr['port'])
        print "Connect testdb successfully"

        # Get the db handler
        cur = conn.cursor()
        
        # insert data
        insert_data(conn, 'company', 100)
        insert_data(conn, 'corp', 100)
        
        # Execute SQL statement
        cur.execute(SQL)
    
        # Get the data
        rows = cur.fetchall()

        res = Counter()
        # write the data into multiset
        for row in rows:
            res[row] = res[row] + 1
            print (row)

    except psycopg2.DatabaseError, e:
        print "Unable to execute postgres:%s"%e
        sys.exit(1)

    finally:
        if conn:
            conn.close()

    return res
######################################
#         Insert data                #
######################################
def insert_data(conn, table, amount):
    namelist =    ['Adam', 'Paul', 'Lisa', 'Andy', 'Mike']
    agelist  =    [10, 20, 30, 40, 50] 
    addresslist = ['California','Pittsburgh','Newyork','Texas','Beijing']

    try:
        cur = conn.cursor();
        cur.execute("DROP TABLE IF EXISTS %s"%table)
        cur.execute('''CREATE TABLE %s
            (ID INT PRIMARY KEY,
            NAME           TEXT,
            AGE            INT,
            ADDRESS        CHAR(50));'''%table)

        for rowkey in range(1, amount):
            i = rowkey%5 - 1
            name = namelist[i]
            age = agelist[i]
            address = addresslist[i]
            data = (rowkey, name, age, address) 
            query_pre = "INSERT INTO %s (ID,NAME,AGE,ADDRESS)"%table
            query = query_pre + " VALUES (%s, %s, %s, %s);"
            print (query)
            print (data)
            cur.execute(query, data)

        conn.commit()
        print "Batch insert successfully"
    
    except psycopg2.DatabaseError, e: 
        print 'Error %s' % e    
        sys.exit(1)

######################################
#     Test                           #
######################################
if __name__ == '__main__':
    #a = sql_exe_pg_tofile('../peloton_test.conf', 'SELECT * FROM company', 'pg_in.out')
    #b = sql_exe_pt_tofile('../peloton_test.conf', 'SELECT * FROM company', 'pt_in.out')
    #print (a)
    #print (b)

    #c = sql_exe_pg('../peloton_test.conf', 'SELECT * FROM company where age <5')
    d = sql_exe_pt('../peloton_test.conf', 'SELECT * FROM company where age <15')
    #print c
    print d
    
    # insert_data('', 'company', 31)
