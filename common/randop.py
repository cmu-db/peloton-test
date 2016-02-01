#!/usr/bin/python2.4
#
# Small script to show PostgreSQL and Pyscopg together
#

import random

from string import ascii_uppercase

def create_random_int(min, max):
    return random.randint(min, max)

def create_random_float(min, max):
    return random.uniform(min, max)

def create_random_string(lenth):
    return ''.join(random.choice(ascii_uppercase) for i in range(lenth))

######################################
#     Test                           #
######################################
if __name__ == '__main__':
    a = create_random_int(1,100)
    b = create_random_float(1,100)
    c = create_random_string(8)
    print (a)
    print (b)
    print (c)
