How to build native


Note: Using commit `e2b2969e82f8fe667f36be44d99a101bd9b73ce7` from `level-zero` 


```bash
$ scl enable devtoolset-9 bash
$ export CPLUS_INCLUDE_PATH=/home/juan/manchester/SPIRV/level-zero/include:$CPLUS_INCLUDE_PATH
$ export LD_LIBRARY_PATH=/home/juan/manchester/SPIRV/level-zero/build/lib:$LD_LIBRARY_PATH 
$ export ZE_SHARED_LOADER="/home/juan/manchester/SPIRV/level-zero/build/lib/libze_loader.so"
$ mkdir build
$ cd build
$ cmake .. 
$ make 
```

