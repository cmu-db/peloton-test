package edu.cmu.cs.db.peloton.test.app;

import com.beust.jcommander.Parameter;

/**
 * Created by tianyuli on 4/14/17.
 */
public class Args {
    @Parameter(names = "-config",
            description = "path to configuration file specifying how to connect to test databases",
            required = true)
    private String configFile;

    @Parameter(names = "-trace",
            description = "path to trace file with one query per line to execute",
            required = true)
    private String traceFile;

    @Parameter(names = "-out",
            description = "path to output dir, if empty then just print to command line")
    private String outDir;

    public String getConfigFile() {
        return configFile;
    }

    public String getTraceFile() {
        return traceFile;
    }

    public String getOutDir() {
        return outDir;
    }
}
