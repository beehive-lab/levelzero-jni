#!/bin/bash

java -Djava.library.path=./levelZeroLib/build -cp target/beehive-levelzero-jni-0.1.5.jar uk.ac.manchester.tornado.drivers.spirv.levelzero.samples.TestCopies
