#!/bin/bash

java -Djava.library.path=./levelZeroLib/build -cp target/beehive-levelzero-jni-0.1.3.jar uk.ac.manchester.tornado.drivers.spirv.levelzero.samples.TestLargeBuffer
