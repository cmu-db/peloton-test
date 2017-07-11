# parameters for connecting to postgresql
pg_database = "SQLite_trace_test"
pg_username = "jenkins"
pg_password = "jenkinsTest123"

# parameters for connecting to postgresql
peloton_path = "peloton"
peloton_port = 52726
peloton_username = ""
peloton_password = ""

# sqllite trace file path
trace_file_root = "trace"

# peloton-test path
peloton_test_path = "../"

# log file
peloton_log_file = "peloton_log"
peloton_test_log_file = "test_log"

# sql keyword filter
# ** in upper case **
kw_filter = [
    "CASE",
    "BETWEEN",
    "<>",
    "IS",
    "\\(SELECT",
    "ABS",
  ]
