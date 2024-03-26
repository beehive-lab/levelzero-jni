#!/bin/bash

clang -cc1 -triple spir copyData.cl -O0 -finclude-default-header -emit-llvm-bc -o copyData.bc
llvm-spirv copyData.bc -o copyData.spv
