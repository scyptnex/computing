#! /bin/bash

gcc -pthread gpu_sim.c -DADDITION -o add
gcc -pthread gpu_sim.c -o mult

