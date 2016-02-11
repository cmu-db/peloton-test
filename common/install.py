#!/usr/bin/python
#
# Install script for postgres and peloton
#

import os
import fileop
import logging
import datetime
import subprocess
import shlex

##################################################
#             Global Variables                   #
##################################################
my_env = os.environ.copy()

# Same logger as dependencies.py for code consistency
LOG = logging.getLogger(__name__)
LOG_handler = logging.StreamHandler()
LOG_formatter = logging.Formatter(
    fmt='%(asctime)s [%(funcName)s:%(lineno)03d] %(levelname)-5s: %(message)s',
    datefmt='%m-%d-%Y %H:%M:%S'
)
LOG_handler.setFormatter(LOG_formatter)
LOG.addHandler(LOG_handler)
LOG.setLevel(logging.INFO)

##################################################
#             Utility Functions                  #
##################################################

def get_timestamp():
    return datetime.datetime.now().strftime('%Y-%m-%d_%H-%M-%S')

suffix = get_timestamp()

#
# Creates temp dir based on timestamp. Easier to follow all dirs created that way.
#
def make_tmp_dir(dir_name):
    try:
        os.mkdir(dir_name)
    except OSError, e:
        # Not a directory exists error
        if e.errno != 17:
            raise
#
# Wrapper over exec_cmd for error checking
#
def check_exec_cmd(cmd,log_dir,log_name):
    if (exec_cmd(cmd,log_dir,log_name) == -1):
        LOG.error('Error executing command: ' + cmd)
        sys.exit(1)
#
# Slightly modified exec_cmd from dependencies.py
#
def exec_cmd(cmd,log_dir,log_name):
    """
    Execute the external command and get its exitcode, stdout and stderr.
    """
    args = shlex.split(cmd)
    log_file_name = log_dir + '/' + log_name + '.log'
    log_file = open(log_file_name, 'w');

    # Try
    try:
        subprocess.check_call(args, env=my_env, stdout=log_file, stderr=log_file)
    # Exception
    except subprocess.CalledProcessError as e:
        print "Command     :: ", e.cmd
        print "Return Code :: ", e.returncode
        print "Output      :: ", e.output
        return -1

##################################################
#             Postgres Build and install         #
##################################################

def build_and_install_pg(conf_path):
    try:
        # Create random temporary session name
        LOG.info('Timestamp: ' + suffix)

        # Create temporary folder in /tmp for build
        PG_BLD_DIR = '/tmp/' + suffix + '_' + 'pg_bld_dir'
        make_tmp_dir(PG_BLD_DIR)
        LOG.info('Creating temp build dir for postgres: ' + PG_BLD_DIR)

        # Create temporary folder in /tmp for install
        PG_INST_DIR = '/tmp/' + suffix + '_' + 'pg_inst_dir'
        make_tmp_dir(PG_INST_DIR)
        LOG.info('Creating temp install dir for postgres: ' + PG_INST_DIR)

        # Create logs directory in /tmp for install
        PG_LOG_DIR = '/tmp/' + suffix + '_' + 'logs'        
        make_tmp_dir(PG_LOG_DIR)
        LOG.info('Creating temp log dir for postgres: ' + PG_LOG_DIR)

        # Get postgres src path from config file
        PG_SRC_DIR = fileop.parse_conf_postgres(conf_path)['srcdir_postgres']
	
        # Configure
        LOG.info('Configuring Postgres')
        os.chdir(PG_BLD_DIR)
        cmd = PG_SRC_DIR + '/configure --prefix=' + PG_INST_DIR
        check_exec_cmd(cmd,PG_LOG_DIR,'configure')
        LOG.info('Finished configuring Postgres. Log: ' + PG_LOG_DIR + '/configure.log')

        # Make
        LOG.info('Building Postgres')
        cmd = 'make -j4'
        check_exec_cmd(cmd,PG_LOG_DIR,'make')
        LOG.info('Finished building Postgres. Log: ' + PG_LOG_DIR + '/make.log')
        
        # Make install to local folder
        LOG.info('Installing Postgres locally at: ' + PG_INST_DIR)
        cmd = 'make install'
        check_exec_cmd(cmd,PG_LOG_DIR,'makeinstall')
        LOG.info('Finished installing Postgres. Log: ' + PG_LOG_DIR + '/makeinstall.log')

    except IOError, e:
        print "Error %d: %s" % (e.args[0], e.args[1])
        sys.exit(1)

##################################################
#             Peloton Build and install          #
##################################################

def build_and_install_pt(conf_path):
    try:
        # Create random temporary session name
        LOG.info('Timestamp: ' + suffix)

        # Create temporary folder in /tmp for build
        PT_BLD_DIR = '/tmp/' + suffix + '_' + 'pt_bld_dir'
        make_tmp_dir(PT_BLD_DIR)
        LOG.info('Creating temp build dir for peloton: ' + PT_BLD_DIR)

        # Create temporary folder in /tmp for install
        PT_INST_DIR = '/tmp/' + suffix + '_' + 'pt_inst_dir'
        make_tmp_dir(PT_INST_DIR)
        LOG.info('Creating temp install dir for peloton: ' + PT_INST_DIR)

        # Create logs directory in /tmp for install
        PT_LOG_DIR = '/tmp/' + suffix + '_' + 'logs'        
        make_tmp_dir(PT_LOG_DIR)
        LOG.info('Creating temp log dir for peloton: ' + PT_LOG_DIR)

        # Get peloton src path from config file
        PT_SRC_DIR = fileop.parse_conf_peloton(conf_path)['srcdir_peloton']
	
        # Bootstrap
        LOG.info('Bootstrapping Peloton')
        os.chdir(PT_SRC_DIR)
        cmd = "rm -f aclocal.m4"
        exec_cmd(cmd,PT_LOG_DIR,'boostrapping')

        cmd = "mkdir -p config"
        exec_cmd(cmd,PT_LOG_DIR,'boostrapping')

        cmd = "autoreconf -fvi"
        exec_cmd(cmd,PT_LOG_DIR,'boostrapping')
        LOG.info('Finished bootstrapping Peloton. Log: ' + PT_LOG_DIR + '/bootstrapping.log')

        # Configure
        LOG.info('Configuring Peloton')
        os.chdir(PT_BLD_DIR)
        cmd = PT_SRC_DIR + '/configure --prefix=' + PT_INST_DIR
        check_exec_cmd(cmd,PT_LOG_DIR,'configure')
        LOG.info('Finished configuring Peloton. Log: ' + PT_LOG_DIR + '/configure.log')

        # Make
        LOG.info('Building Peloton')
        cmd = 'make -j4'
        check_exec_cmd(cmd,PT_LOG_DIR,'make')
        LOG.info('Finished building Peloton. Log: ' + PT_LOG_DIR + '/make.log')
        
        # Make install to local folder
        LOG.info('Installing Peloton locally at: ' + PT_INST_DIR)
        cmd = 'make install'
        check_exec_cmd(cmd,PT_LOG_DIR,'makeinstall')
        LOG.info('Finished installing Peloton. Log: ' + PT_LOG_DIR + '/makeinstall.log')
        
    except IOError, e:
        print "Error %d: %s" % (e.args[0], e.args[1])
        sys.exit(1)

if __name__ == '__main__':
#    build_and_install_pg('/home/ghatage/peloton-test/peloton_test.conf')
#    build_and_install_pt('/home/ghatage/peloton-test/peloton_test.conf')
