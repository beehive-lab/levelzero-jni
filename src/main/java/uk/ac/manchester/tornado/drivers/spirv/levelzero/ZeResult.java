/*
 * MIT License
 *
 * Copyright (c) 2021, APT Group, Department of Computer Science,
 * The University of Manchester.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package uk.ac.manchester.tornado.drivers.spirv.levelzero;

/**
 * Return Error codes from Level Zero API.
 *
 * Values taken from the Level Zero SPEC:
 * {@url https://github.com/oneapi-src/level-zero/blob/master/include/ze_api.h}
 */
public class ZeResult {
    /**
     * [Core] success
     */
    public static final int ZE_RESULT_SUCCESS = 0;

    /**
     * [Core] synchronization primitive not signaled
     */
    public static final int ZE_RESULT_NOT_READY = 1;

    /**
     * [Core] device hung, reset, was removed, or driver update occurred
     */
    public static final int ZE_RESULT_ERROR_DEVICE_LOST = 0x70000001;

    /**
     * [Core] insufficient host memory to satisfy call
     */
    public static final int ZE_RESULT_ERROR_OUT_OF_HOST_MEMORY = 0x70000002;

    /**
     * [Core] insufficient device memory to satisfy call
     */
    public static final int ZE_RESULT_ERROR_OUT_OF_DEVICE_MEMORY = 0x70000003;

    /**
     * [Core] error occurred when building module, see build log for details
     */
    public static final int ZE_RESULT_ERROR_MODULE_BUILD_FAILURE = 0x70000004;

    /**
     * [Core] error occurred when linking modules, see build log for details
     */
    public static final int ZE_RESULT_ERROR_MODULE_LINK_FAILURE = 0x70000005;

    /**
     * [Sysman] access denied due to permission level
     */
    public static final int ZE_RESULT_ERROR_INSUFFICIENT_PERMISSIONS = 0x70010000;

    /**
     * [Sysman] resource already in use and simultaneous access not allowed or resource was removed
     */
    public static final int ZE_RESULT_ERROR_NOT_AVAILABLE = 0x70010001;

    /**
     * [Tools] external required dependency is unavailable or missing
     */
    public static final int ZE_RESULT_ERROR_DEPENDENCY_UNAVAILABLE = 0x70020000;

    /**
     * [Validation] driver is not initialized
     */
    public static final int ZE_RESULT_ERROR_UNINITIALIZED = 0x78000001;

    /**
     * [Validation] generic error code for unsupported versions
     */
    public static final int ZE_RESULT_ERROR_UNSUPPORTED_VERSION = 0x78000002;

    /**
     * [Validation] generic error code for unsupported features
     */
    public static final int ZE_RESULT_ERROR_UNSUPPORTED_FEATURE = 0x78000003;

    /**
     * [Validation] generic error code for invalid arguments
     */
    public static final int ZE_RESULT_ERROR_INVALID_ARGUMENT = 0x78000004;

    /**
     * [Validation] handle argument is not valid
     */
    public static final int ZE_RESULT_ERROR_INVALID_NULL_HANDLE = 0x78000005;

    /**
     * [Validation] object pointed to by handle still in-use by device
     */
    public static final int ZE_RESULT_ERROR_HANDLE_OBJECT_IN_USE = 0x78000006;

    /**
     * [Validation] pointer argument may not be nullptr
     */
    public static final int ZE_RESULT_ERROR_INVALID_NULL_POINTER = 0x78000007;

    /**
     * [Validation] size argument is invalid (e.g., must not be zero)
     */
    public static final int ZE_RESULT_ERROR_INVALID_SIZE = 0x78000008;

    /**
     * [Validation] size argument is not supported by the device (e.g., toolarge)
     */
    public static final int ZE_RESULT_ERROR_UNSUPPORTED_SIZE = 0x78000009;

    /**
     * [Validation] alignment argument is not supported by the device (e.g.,too small)
     */
    public static final int ZE_RESULT_ERROR_UNSUPPORTED_ALIGNMENT = 0x7800000a;

    /**
     * [Validation] synchronization object in invalid state
     */
    public static final int ZE_RESULT_ERROR_INVALID_SYNCHRONIZATION_OBJECT = 0x7800000b;

    /**
     * [Validation] enumerator argument is not valid
     */
    public static final int ZE_RESULT_ERROR_INVALID_ENUMERATION = 0x7800000c;

    /**
     * [Validation] enumerator argument is not supported by the device
     */
    public static final int ZE_RESULT_ERROR_UNSUPPORTED_ENUMERATION = 0x7800000d;

    /**
     * [Validation] image format is not supported by the device
     */
    public static final int ZE_RESULT_ERROR_UNSUPPORTED_IMAGE_FORMAT = 0x7800000e;

    /**
     * [Validation] native binary is not supported by the device
     */
    public static final int ZE_RESULT_ERROR_INVALID_NATIVE_BINARY = 0x7800000f;

    /**
     * [Validation] global variable is not found in the module
     */
    public static final int ZE_RESULT_ERROR_INVALID_GLOBAL_NAME = 0x78000010;

    /**
     * [Validation] kernel name is not found in the module
     */
    public static final int ZE_RESULT_ERROR_INVALID_KERNEL_NAME = 0x78000011;

    /**
     * [Validation] function name is not found in the module
     */
    public static final int ZE_RESULT_ERROR_INVALID_FUNCTION_NAME = 0x78000012;

    /**
     * [Validation] group size dimension is not valid for the kernel or device
     */
    public static final int ZE_RESULT_ERROR_INVALID_GROUP_SIZE_DIMENSION = 0x78000013;

    /**
     * [Validation] global width dimension is not valid for the kernel or device
     */
    public static final int ZE_RESULT_ERROR_INVALID_GLOBAL_WIDTH_DIMENSION = 0x78000014;

    /**
     * [Validation] kernel argument index is not valid for kernel
     */
    public static final int ZE_RESULT_ERROR_INVALID_KERNEL_ARGUMENT_INDEX = 0x78000015;

    /**
     * [Validation] kernel argument size does not match kernel
     */
    public static final int ZE_RESULT_ERROR_INVALID_KERNEL_ARGUMENT_SIZE = 0x78000016;

    /**
     * [Validation] value of kernel attribute is not valid for the kernel or device
     */
    public static final int ZE_RESULT_ERROR_INVALID_KERNEL_ATTRIBUTE_VALUE = 0x78000017;

    /**
     * [Validation] module with imports needs to be linked before kernels can be created from it.
     */
    public static final int ZE_RESULT_ERROR_INVALID_MODULE_UNLINKED = 0x78000018;

    /**
     * [Validation] command list type does not match command queue type
     */
    public static final int ZE_RESULT_ERROR_INVALID_COMMAND_LIST_TYPE = 0x78000019;

    /**
     * [Validation] copy operations do not support overlapping regions of memory
     */
    public static final int ZE_RESULT_ERROR_OVERLAPPING_REGIONS = 0x7800001a;

    /**
     * [Core] unknown or internal error
     */
    public static final int ZE_RESULT_ERROR_UNKNOWN = 0x7ffffffe;


    public static final int ZE_RESULT_FORCE_UINT32 = 0x7fffffff;
}
