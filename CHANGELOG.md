## Beehive LevelZero-JNI 0.1.3
26/03/2024

- Add Windows 10/11 installer support using Window Microsoft Studio Tools: [commit](https://github.com/beehive-lab/levelzero-jni/commit/02b24de0e8fe86790bdc29e1cf50b2d199c8e999).


## Beehive LevelZero-JNI 0.1.2
12/12/23

- Multiple SPIR-V Devices Fixed: [commit](https://github.com/beehive-lab/levelzero-jni/commit/fe20b18c9623b4d0533ee50d878b266ecdce46dc)
- Support for data send/receive using Panama off-heap buffers 
- Use `-O2` optimization: [commit](https://github.com/beehive-lab/levelzero-jni/commit/721b8aed7ac4e419843b3029be99c11267eeb32c)
- Fix `string` release in JNI code: [commit](https://github.com/beehive-lab/levelzero-jni/commit/3c6d463ebafbf9d2de7be128f79483ff28c5ace6)
- Fix Device properties: [commit](https://github.com/beehive-lab/levelzero-jni/commit/83c2e032197e2f8a13d895d2b75f72693424bd7b)

## LevelZero-JNI 0.1.1
10/03/2022

- Buffer Allocation Fixed: [commit](https://github.com/beehive-lab/levelzero-jni/commit/26e5155dc349ff9db9a01fe6d9ec08104a0a5d70)
- `ZeMemAdvice` added
- Functions `zeCommandListAppendMemoryPrefetch` and `zeCommandListAppendMemAdvise` supported
- Javadoc improved
- Function `zeKernelSetCacheConfig` supported


## LevelZero-JNI 0.1.0
03/12/2021

- Initial prototype of LevelZero JNI
- It covers a subset of the Intel Level-Zero 1.1.2 Spec (Feb 2021)
- C++ Wrapper for JNI provided
- Java Interface provided
- Set of examples and documentation

