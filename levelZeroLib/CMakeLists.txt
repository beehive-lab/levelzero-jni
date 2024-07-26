cmake_minimum_required (VERSION 3.6)
project (tornado)

find_package(JNI REQUIRED)

#if (DEFINED $ENV{ZE_SHARED_LOADER})
#	message("- ZE_SHARED_LOADER found")
#else()
#	message("ERROR: ZE_SHARED_LOADER is not set")
#if(MSVC)
#	message("Set this export to PATH-TO\level-zero\build\lib\release\ze_loader.lib")
#else()
#	message("Set this export to PATH-TO/level-zero/build/lib/libze_loader.so")
#endif()
#	return()
#endif()

# set the C++14 standard
set(CMAKE_CXX_STANDARD 14)

if(MSVC)
	string(REPLACE "/MDd" "/MTd" CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG}")
	string(REPLACE "/MD" "/MT" CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE}")

	# enable multi-process compilation, not supported by clang-cl
	if(NOT CMAKE_CXX_COMPILER_ID STREQUAL Clang)
		set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} /MP")
	endif()

	# enable exceptions handling
	set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} /EHsc")

	# enable creation of PDB files for Release Builds
	set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} /Zi")
	set(CMAKE_SHARED_LINKER_FLAGS_RELEASE "${CMAKE_SHARED_LINKER_FLAGS_RELEASE} /DEBUG /OPT:REF /OPT:ICF")

	# enable CET shadow stack
	set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} /CETCOMPAT")

	#Use of sccache with MSVC requires workaround of replacing /Zi with /Z7
	#https://github.com/mozilla/sccache
	if(USE_Z7) #sccache
		string(REPLACE "/Zi" "/Z7" CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG}")
		string(REPLACE "/Zi" "/Z7" CMAKE_C_FLAGS_DEBUG "${CMAKE_C_FLAGS_DEBUG}")
		string(REPLACE "/Zi" "/Z7" CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE}")
		string(REPLACE "/Zi" "/Z7" CMAKE_C_FLAGS_RELEASE "${CMAKE_C_FLAGS_RELEASE}")
		string(REPLACE "/Zi" "/Z7" CMAKE_CXX_FLAGS_RELWITHDEBINFO "${CMAKE_CXX_FLAGS_RELWITHDEBINFO}")
		string(REPLACE "/Zi" "/Z7" CMAKE_C_FLAGS_RELWITHDEBINFO "${CMAKE_C_FLAGS_RELWITHDEBINFO}")
	endif()
else()
	set(GCC_INTEL_LEVEL0 "-std=c++14")
	set(GCC_INTEL_LEVEL0 "${GCC_INTEL_LEVEL0} -O2")
	set(GCC_INTEL_LEVEL0 "${GCC_INTEL_LEVEL0} -fpermissive")
	set(GCC_INTEL_LEVEL0 "${GCC_INTEL_LEVEL0} -fno-stack-protector")
	set(GCC_INTEL_LEVEL0 "${GCC_INTEL_LEVEL0} -fPIC")
	set(GCC_INTEL_LEVEL0 "${GCC_INTEL_LEVEL0} -rdynamic")
	set(GCC_INTEL_LEVEL0 "${GCC_INTEL_LEVEL0} -lstdc++")
	add_definitions(${GCC_INTEL_LEVEL0})
endif()

# This should point to:
#  Linux  : "PATH-TO/level-zero/build/lib/libze_loader.so"
#  Windows: "PATH-TO\level-zero\build\lib\release\ze_loader.lib"
set(ZE_LOADER $ENV{ZE_SHARED_LOADER})

include_directories(
	./
	${JNI_INCLUDE_DIRS}
	${OPENCL_INCLUDE_DIRS}
	$ENV{CPLUS_INCLUDE_PATH}
	$ENV{C_INCLUDE_PATH}
	)

file(GLOB_RECURSE "src/*.cpp")
add_library(tornado-levelzero SHARED
	src/levelZeroDriver.cpp
	src/levelZeroContext.cpp
	src/levelZeroDevice.cpp
	src/levelZeroBuffer.cpp
	src/levelZeroModule.cpp
	src/levelZeroKernel.cpp
	src/levelZeroCommandList.cpp
	src/levelZeroCommandQueue.cpp
	src/levelZeroFence.cpp
	src/levelZeroTimeStampKernel.cpp
	src/levelZeroDescriptors.cpp
    src/levelZeroPointer.cpp
	)

target_link_libraries(tornado-levelzero ${OpenCL_LIBRARIES} ${JNI_LIB_DIRS})
target_link_libraries(tornado-levelzero ${ZE_LOADER})
