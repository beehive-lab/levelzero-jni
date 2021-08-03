#!/bin/bash

clang -cc1 -triple spir copy_data.cl -O0 -finclude-default-header -emit-llvm-bc -o opencl-copy.bc
llvm-spirv opencl-copy.bc -o opencl-copy.spv
mv opencl-copy.spv /tmp/opencl-copy.spv

