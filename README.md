# Beehive LevelZero JNI

Baremetal GPU and FPGA programming for Java using Intel's [Level Zero API](https://spec.oneapi.io/level-zero/latest/index.html). This project is a Java Native Interface (JNI) binding for Intel's Level Zero. This library is designed to be as closed as possible to the Level Zero API for C++ API, and it supports a subset of the Level Zero 1.4.0 API (Level Zero as in May 2022).
This library is used by [TornadoVM](https://github.com/beehive-lab/TornadoVM/), and the subset of the Level Zero API 1.4.0 is the minimum required to dispatch SPIR-V kernels on Intel ARC and Intel integrated GPUs under the TornadoVM Runtime. It is, by no means, a complete port of the Level Zero API, but external contributions are very welcome.

## Compilation & configuration of the JNI Level Zero API

### 1) Compile Native Library: Level Zero API

#### Linux (Tested for Fedora 35-39 and Ubuntu 22.X-24.X)

```bash
git clone https://github.com/oneapi-src/level-zero.git
cd level-zero
mkdir build
cd build
cmake ..
cmake --build . --config Release
cmake --build . --config Release --target package
```

#### Windows 10/11

Tested Configurations:
- Lenovo IdeaPad Gaming 3 15IHU6
- Dell XPS 8950
- Windows 11
- VS Community 2022
  + components: C++, Git, Spectre mitigated libraries
- CMake 3.26.3, Maven 3.9.1, JDK 21+

Run commands in _x64 Native Tools Command Prompt for VS 2022_.

```cmd
git clone https://github.com/oneapi-src/level-zero
cd level-zero
md build
cd build
cmake ..
cmake --build . --config Release

rem check
.\bin\Release\zello_world.exe
```

Note: If `zello_world.exe` fails, search for existing Level Zero API DLLs (file names start with `ze_`, e.g. `ze_tracing_layer.dll`) in `c:\windows\system32` and move them to another folder.

### 2) Compile Level Zero Java JNI native code

Set the paths to the directory of Level Zero installation. Here are examples:

#### Linux

```bash
scl enable devtoolset-9 bash # << Only for CentOS/RHEL
git clone https://github.com/beehive-lab/levelzero-jni
export ZE_SHARED_LOADER="<path-to-levelzero>/build/lib/libze_loader.so"
export CPLUS_INCLUDE_PATH=<path-to-levelzero>/include:$CPLUS_INCLUDE_PATH
export C_INCLUDE_PATH=<path-to-levelzero>/include:$CPLUS_INCLUDE_PATH
cd levelzero-jni/levelZeroLib
mkdir build
cd build
cmake ..
make
```

#### Windows 10/11

Note: Run commands in _x64 Native Tools Command Prompt for VS 2022_.

```cmd
git clone https://github.com/beehive-lab/levelzero-jni
set ZE_SHARED_LOADER=%USERPROFILE%\lab\level-zero\build\lib\release\ze_loader.lib
set CPLUS_INCLUDE_PATH=%USERPROFILE%\lab\level-zero\include
set C_INCLUDE_PATH=%USERPROFILE%\lab\level-zero\include

rem add the folder with Intel's Level Zero API DLLs to PATH
set PATH=%USERPROFILE%\lab\level-zero\build\bin\release;%PATH%

cd levelzero-jni\levelZeroLib
md build
cd build
cmake ..
cmake --build . --config Release
```

#### Obtain a SPIR-V compiler (Linux and Windows)

In case you want to compile kernels from OpenCL C to SPIR-V and use the Level Zero API, you need to download the `llvm-spirv` compiler. The implementation we are currently using is [Intel LLVM](https://github.com/intel/llvm).

### 3) Compile the Java Library and Run Tests

#### Linux

```bash
mvn clean package
./scripts/run.sh   ## < This script compiles an OpenCL C program to SPIR-V using the llvm-spirv compiler (see 2.1)
```

The OpenCL C kernel provided for this example is as follows:

```c
__kernel void copyData(__global int* input, __global int* output) {
	uint idx = get_global_id(0);
	output[idx] = input[idx];
}
```

To compile from OpenCL C to SPIR-V:

```bash
clang -cc1 -triple spir copyData.cl -O0 -finclude-default-header -emit-llvm-bc -o copyData.bc
llvm-spirv copyData.bc -o copyData.spv
```

#### Windows

Note: Java programs that use `levelzero-jni` are based on DLLs, which are provided by Intel's Level Zero API. For these programs to find these DLLs, the PATH environment variable must contain the folder that contains the DLLs.

```cmd
mvn clean package

rem add the folder with Intel's Level Zero API DLLs to PATH
set PATH=%USERPROFILE%\lab\level-zero\build\bin\release;%PATH%

rem copyData.spv file expected in CWD
.\scripts\run.cmd

# more tests
.\scripts\copies.cmd
.\scripts\events.cmd
.\scripts\fences.cmd
.\scripts\kernelTimers.cmd
.\scripts\transferTimers.cmd
.\scripts\largeBuffers.cmd
```

## Some Examples


### Instance Driver Pointer 


```java
 // Obtain the Level Zero Driver
LevelZeroDriver driver = new LevelZeroDriver();
int result = driver.zeInit(ZeInitFlag.ZE_INIT_FLAG_GPU_ONLY);
LevelZeroUtils.errorLog("zeInit", result);

int[] numDrivers = new int[1];
result = driver.zeDriverGet(numDrivers, null);
LevelZeroUtils.errorLog("zeDriverGet", result);

ZeDriverHandle driverHandler = new ZeDriverHandle(numDrivers[0]);
result = driver.zeDriverGet(numDrivers, driverHandler);
LevelZeroUtils.errorLog("zeDriverGet", result);
```

### Create Level Zero Context

```java
ZeContextDescriptor contextDescription = new ZeContextDescriptor();
LevelZeroContext context = new LevelZeroContext(driverHandler, contextDescription);
result = context.zeContextCreate(driverHandler.getZe_driver_handle_t_ptr()[0]);
LevelZeroUtils.errorLog("zeContextCreate", result);
```


### Allocate Shared Memory:

```java
ZeDeviceMemAllocDescriptor deviceMemAllocDesc = new ZeDeviceMemAllocDescriptor();
deviceMemAllocDesc.setFlags(ZeDeviceMemAllocFlags.ZE_DEVICE_MEM_ALLOC_FLAG_BIAS_UNCACHED);


ZeHostMemAllocDescriptor hostMemAllocDesc = new ZeHostMemAllocDescriptor();
hostMemAllocDesc.setFlags(ZeHostMemAllocFlags.ZE_HOST_MEM_ALLOC_FLAG_BIAS_UNCACHED);


LevelZeroBufferInteger bufferA = new LevelZeroBufferInteger();
result = context.zeMemAllocShared(context.getContextHandle().getContextPtr()[0], deviceMemAllocDesc, hostMemAllocDesc, bufferSize, 1, device.getDeviceHandlerPtr(), bufferA);
LevelZeroUtils.errorLog("zeMemAllocShared", result);
```

See the full list of examples [here](https://github.com/beehive-lab/levelzero-jni/tree/master/src/main/java/uk/ac/manchester/tornado/drivers/spirv/levelzero/samples).


## License

This project is developed at [The University of Manchester](https://www.manchester.ac.uk/), and it is fully open source under the [MIT](https://github.com/beehive-lab/levelzero-jni/blob/master/LICENSE) license.


## Acknowledgments

The work was partially funded by the EU Horizon 2020 [Elegant 957286](https://www.elegant-h2020.eu/) project, and [Intel Coorporation](https://www.intel.it/content/www/it/it/homepage.html).
