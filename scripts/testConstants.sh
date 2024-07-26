
clang -cc1 -triple spir constantsParameters.cl -O0 -finclude-default-header -emit-llvm-bc -o constantsParameters.bc
llvm-spirv constantsParameters.bc -o constantsParameters.spv

java -Djava.library.path=./levelZeroLib/build -cp target/beehive-levelzero-jni-0.1.3.jar uk.ac.manchester.tornado.drivers.spirv.levelzero.samples.TestConstantParameters
