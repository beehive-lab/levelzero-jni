#!/bin/bash

clang -cc1 -triple spir copyData.cl -O0 -finclude-default-header -emit-llvm-bc -o copyData.bc
llvm-spirv copyData.bc -o copyData.spv

java -Djava.library.path=./levelZeroLib/build -cp target/beehive-levelzero-jni-0.1.3.jar uk.ac.manchester.tornado.drivers.spirv.levelzero.samples.TestLevelZero copyData.spv
