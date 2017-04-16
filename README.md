# peloton-test
Testing Framework for the Peloton DBMS

You can find more information about the project in the documentation folder.

To run the command line tool for query generation, execute the following command, if output directory is provided,
XML output is written to that directory. Otherwise the result is printed to the command line.
```
./run.sh -config <config_file> -trace <trace_file> [-out <output directory>] [-batchsize <batchsize, default 50>]
```

Currently only postgresql is supported
