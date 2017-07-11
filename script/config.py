# parameters for connecting to postgresql
pg_database = "test"
pg_username = "test_peloton"
pg_password = "test_peloton"

# parameters for connecting to postgresql
peloton_path = "../../build/bin/peloton"
peloton_port = 23333
peloton_username = ""
peloton_password = ""

# sqllite trace file path
trace_file_root = "trace"

# peloton-test path
peloton_test_path = "peloton-test"

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
