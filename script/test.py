#!/usr/bin/python

import os
import re
import socket
import time
from config import *
from subprocess import Popen

tmp_dir = "tmp"
tmp_sql_file = "tmp/test.sql"
tmp_config = "tmp/config"

def extract_sql(input_path, output_path):
  infile = open(input_path)
  outfile = open(output_path, "w")

  new_statement = True
  wait_for_blank = False
  query = ""

  for line in infile:
    line = line.strip() + " "
    if line == " ":
      if not wait_for_blank:
	outfile.write(query + ";\n")
      new_statement = True
      wait_for_blank = False
      query = ""
      continue
    if wait_for_blank:
      continue
    l = line.strip().split()
    if l[0] in ['#', 'statement', 'query', 'halt', 'hash-threshold']:
      continue
    if l[0] in ['onlyif', 'skipif']:
      wait_for_blank = True
      continue
    if l[0] == '----':
      outfile.write(query + ";\n")
      wait_for_blank = True
      continue
    query += line
    for kw in kw_filter:
      if re.search(kw, line.upper()):
	wait_for_blank = True

  infile.close()
  outfile.close()

def get_pg_jdbc():
  return "jdbc:postgresql://127.0.0.1:5432/%s" % pg_database

def get_peloton_jdbc():
  return "jdbc:postgresql://127.0.0.1:%s/" % peloton_port

def gen_config(path):
  conf = open(path, "w")

  conf.write(get_peloton_jdbc() + "\n")
  conf.write(peloton_username + "\n")
  conf.write(peloton_password + "\n")

  conf.write(get_pg_jdbc() + "\n")
  conf.write(pg_username + "\n")
  conf.write(pg_password + "\n")

def wait_for_peloton_ready():
  peloton_ready = False
  while not peloton_ready:
    try:
      sk = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
      sk.settimeout(1)
      sk.connect(('127.0.0.1', peloton_port))
      peloton_ready = True
      sk.close()
    except Exception:
      pass
    if not peloton_ready:
      time.sleep(1)

def main():
  global peloton_path, tmp_dir, tmp_config, tmp_sql_file

  work_path = os.path.abspath(".")
  tmp_config = os.path.abspath(tmp_config)
  tmp_sql_file = os.path.abspath(tmp_sql_file)
  test_log_file = os.path.abspath(peloton_test_log_file)

  if not os.path.exists(tmp_dir):
    os.mkdir(tmp_dir)
  gen_config(tmp_config)

  peloton_log = open(peloton_log_file, "w")
  test_log = open(test_log_file, "w")

  print_log = Popen(["tail", "-f", test_log_file])

  try:
    for path in traces:
      for f in files:
	extract_sql(path, tmp_sql_file)
	print "=============================================================="
	print "test: ", path
	print "=============================================================="

	os.system("dropdb --if-exist -U %s -w %s" % (pg_username, pg_database))
	os.system("createdb -U %s -w %s" % (pg_username, pg_database))

	peloton = Popen([peloton_path, "-port", str(peloton_port)], 
			stdout=peloton_log, stderr=peloton_log)

	wait_for_peloton_ready()

	os.chdir(peloton_test_path)
	test = Popen(["bash", "run.sh", "-config", tmp_config, "-trace", tmp_sql_file],
		      stdout=test_log, stderr=test_log)
	test.wait()
	os.chdir(work_path)
	peloton.kill()
  except Exception as e:
    print e
  finally:
    peloton.kill()
      
  print_log.kill()
  #os.system("rm -rf %s" % tmp_dir)
  os.system("dropdb --if-exist -U %s -w %s" % (pg_username, pg_database))

if __name__ == "__main__":
  main()
