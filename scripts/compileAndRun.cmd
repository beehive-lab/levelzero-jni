mvn clean install

java -Djava.library.path=./levelZeroLib/build/Release -cp target/beehive-levelzero-jni-0.1.2.jar uk.ac.manchester.tornado.drivers.spirv.levelzero.samples.TestLevelZero copyData.spv
