#!/bin/bash 

./scripts/compileSPIRVKernelCopy.sh
java -Djava.library.path=./levelZeroLib/build -cp target/levelzero-1.0-SNAPSHOT.jar uk.ac.manchester.tornado.drivers.spirv.levelzero.samples.TestFences
