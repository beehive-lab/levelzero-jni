How to build native


Note: Using commit `551dd5810a3cea7a7e26ac4441da31878e804b53` from `level-zero` 


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

