#!/bin/bash

./scripts/compileSPIRVKernelCopy.sh
java -Djava.library.path=./levelZeroLib/build -cp target/beehive-levelzero-jni-0.1.4.jar uk.ac.manchester.tornado.drivers.spirv.levelzero.samples.TestFences copyData.spv
