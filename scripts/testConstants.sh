
clang -cc1 -triple spir copyParameters.cl -O0 -finclude-default-header -emit-llvm-bc -o copyParameters.bc
llvm-spirv copyParameters.bc -o copyParameters.spv

java -Djava.library.path=./levelZeroLib/build -cp target/beehive-levelzero-jni-0.1.3.jar uk.ac.manchester.tornado.drivers.spirv.levelzero.samples.TestParameters
