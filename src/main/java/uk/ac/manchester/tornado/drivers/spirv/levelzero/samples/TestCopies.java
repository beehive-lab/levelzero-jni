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
package uk.ac.manchester.tornado.drivers.spirv.levelzero.samples;

import java.util.Arrays;

import uk.ac.manchester.tornado.drivers.spirv.levelzero.LevelZeroByteBuffer;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.LevelZeroCommandList;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.LevelZeroCommandQueue;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.LevelZeroContext;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.LevelZeroDevice;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.LevelZeroDriver;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeCommandListDescriptor;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeCommandListFlag;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeCommandListHandle;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeCommandQueueDescriptor;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeCommandQueueGroupProperties;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeCommandQueueGroupPropertyFlags;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeCommandQueueHandle;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeCommandQueueMode;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeCommandQueuePriority;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeContextDescriptor;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeDeviceMemAllocDescriptor;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeDeviceProperties;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeDevicesHandle;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeDriverHandle;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeHostMemAllocDescriptor;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeInitFlag;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.Ze_Structure_Type;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.utils.LevelZeroUtils;

public class TestCopies {

    public static LevelZeroContext zeInitContext(LevelZeroDriver driver) {
        if (driver == null) {
            return null;
        }

        int result = driver.zeInit(ZeInitFlag.ZE_INIT_FLAG_GPU_ONLY);
        LevelZeroUtils.errorLog("zeInit", result);

        int[] numDrivers = new int[1];
        result = driver.zeDriverGet(numDrivers, null);
        LevelZeroUtils.errorLog("zeDriverGet", result);

        ZeDriverHandle driverHandler = new ZeDriverHandle(numDrivers[0]);
        result = driver.zeDriverGet(numDrivers, driverHandler);
        LevelZeroUtils.errorLog("zeDriverGet", result);

        ZeContextDescriptor contextDescription = new ZeContextDescriptor();
        contextDescription.setSType(Ze_Structure_Type.ZE_STRUCTURE_TYPE_CONTEXT_DESC);
        LevelZeroContext context = new LevelZeroContext(driverHandler, contextDescription);
        result = context.zeContextCreate(driverHandler.getZe_driver_handle_t_ptr()[0]);
        LevelZeroUtils.errorLog("zeContextCreate", result);
        return context;
    }

    public static LevelZeroDevice zeGetDevices(LevelZeroContext context, LevelZeroDriver driver) {

        ZeDriverHandle driverHandler = context.getDriver();

        // Get number of devices in a driver
        int[] deviceCount = new int[1];
        int result = driver.zeDeviceGet(driverHandler, 0, deviceCount, null);
        LevelZeroUtils.errorLog("zeDeviceGet", result);

        // Instantiate a device Handler
        ZeDevicesHandle deviceHandler = new ZeDevicesHandle(deviceCount[0]);
        result = driver.zeDeviceGet(driverHandler, 0, deviceCount, deviceHandler);
        LevelZeroUtils.errorLog("zeDeviceGet", result);

        // ============================================
        // Get the device
        // ============================================
        LevelZeroDevice device = driver.getDevice(driverHandler, 0);
        ZeDeviceProperties deviceProperties = new ZeDeviceProperties();
        result = device.zeDeviceGetProperties(device.getDeviceHandlerPtr(), deviceProperties);
        LevelZeroUtils.errorLog("zeDeviceGetProperties", result);
        return device;
    }

    public static int getCommandQueueOrdinal(LevelZeroDevice device) {
        int[] numQueueGroups = new int[1];
        int result = device.zeDeviceGetCommandQueueGroupProperties(device.getDeviceHandlerPtr(), numQueueGroups, null);
        LevelZeroUtils.errorLog("zeDeviceGetCommandQueueGroupProperties", result);

        if (numQueueGroups[0] == 0) {
            throw new RuntimeException("Number of Queue Groups is 0 for device: " + device.getDeviceProperties().getName());
        }
        int ordinal = numQueueGroups[0];

        ZeCommandQueueGroupProperties[] commandQueueGroupProperties = new ZeCommandQueueGroupProperties[numQueueGroups[0]];
        result = device.zeDeviceGetCommandQueueGroupProperties(device.getDeviceHandlerPtr(), numQueueGroups, commandQueueGroupProperties);
        LevelZeroUtils.errorLog("zeDeviceGetCommandQueueGroupProperties", result);

        for (int i = 0; i < numQueueGroups[0]; i++) {
            if ((commandQueueGroupProperties[i].getFlags()
                    & ZeCommandQueueGroupPropertyFlags.ZE_COMMAND_QUEUE_GROUP_PROPERTY_FLAG_COMPUTE) == ZeCommandQueueGroupPropertyFlags.ZE_COMMAND_QUEUE_GROUP_PROPERTY_FLAG_COMPUTE) {
                ordinal = i;
                System.out.println("ORDINAL: " + ordinal);
                break;
            }
        }
        return ordinal;
    }

    public static LevelZeroCommandQueue createCommandQueue(LevelZeroContext context, LevelZeroDevice device) {
        // Create Command Queue
        ZeCommandQueueDescriptor cmdDescriptor = new ZeCommandQueueDescriptor();
        cmdDescriptor.setFlags(0);
        cmdDescriptor.setMode(ZeCommandQueueMode.ZE_COMMAND_QUEUE_MODE_DEFAULT);
        cmdDescriptor.setPriority(ZeCommandQueuePriority.ZE_COMMAND_QUEUE_PRIORITY_NORMAL);
        cmdDescriptor.setOrdinal(getCommandQueueOrdinal(device));
        cmdDescriptor.setIndex(0);

        ZeCommandQueueHandle zeCommandQueueHandle = new ZeCommandQueueHandle();
        int result = context.zeCommandQueueCreate(context.getContextHandle().getContextPtr()[0], device.getDeviceHandlerPtr(), cmdDescriptor, zeCommandQueueHandle);
        LevelZeroUtils.errorLog("zeCommandQueueCreate", result);
        return new LevelZeroCommandQueue(context, zeCommandQueueHandle);
    }

    public static LevelZeroCommandList createCommandList(LevelZeroContext context, LevelZeroDevice device) {
        ZeCommandListDescriptor cmdListDescriptor = new ZeCommandListDescriptor();
        cmdListDescriptor.setFlags(ZeCommandListFlag.ZE_COMMAND_LIST_FLAG_RELAXED_ORDERING);
        cmdListDescriptor.setCommandQueueGroupOrdinal(getCommandQueueOrdinal(device));
        ZeCommandListHandle commandListHandler = new ZeCommandListHandle();
        int result = context.zeCommandListCreate(context.getContextHandle().getContextPtr()[0], device.getDeviceHandlerPtr(), cmdListDescriptor, commandListHandler);
        LevelZeroUtils.errorLog("zeCommandListCreate", result);
        return new LevelZeroCommandList(context, commandListHandler);
    }

    public static boolean testAppendMemoryCopyFromHeapToDeviceToHeap(LevelZeroContext context, LevelZeroDevice device) {

        final int allocSize = 4096;
        byte[] heapBuffer = new byte[allocSize];

        LevelZeroByteBuffer deviceBuffer = new LevelZeroByteBuffer();
        byte[] heapBuffer2 = new byte[allocSize];

        LevelZeroCommandQueue commandQueue = createCommandQueue(context, device);
        LevelZeroCommandList commandList = createCommandList(context, device);

        ZeDeviceMemAllocDescriptor deviceMemAllocDesc = new ZeDeviceMemAllocDescriptor();
        deviceMemAllocDesc.setOrdinal(0);
        deviceMemAllocDesc.setFlags(0);
        final int alignment = 1;

        // This is the equivalent of a clCreateBuffer
        int result = context.zeMemAllocDevice(context.getContextHandle().getContextPtr()[0], deviceMemAllocDesc, allocSize, alignment, device.getDeviceHandlerPtr(), deviceBuffer);
        LevelZeroUtils.errorLog("zeMemAllocDevice", result);

        // Initialize second buffer (Java side) to 0
        Arrays.fill(heapBuffer2, (byte) 0);

        // Fill heap buffer (Java side)
        for (int i = 0; i < allocSize; i++) {
            heapBuffer[i] = 'a';
        }

        // Copy from HEAP -> Device Allocated Memory
        result = commandList.zeCommandListAppendMemoryCopy(commandList.getCommandListHandlerPtr(), deviceBuffer, heapBuffer, allocSize, null, 0, null);
        LevelZeroUtils.errorLog("zeCommandListAppendMemoryCopy", result);
        result = commandList.zeCommandListAppendBarrier(commandList.getCommandListHandlerPtr(), null, 0, null);
        LevelZeroUtils.errorLog("zeCommandListAppendBarrier", result);

        // Copy From Device-Allocated memory to host (heapBuffer2)
        result = commandList.zeCommandListAppendMemoryCopy(commandList.getCommandListHandlerPtr(), heapBuffer2, deviceBuffer, allocSize, null, 0, null);
        LevelZeroUtils.errorLog("zeCommandListAppendMemoryCopy", result);

        // Close the command list
        result = commandList.zeCommandListClose(commandList.getCommandListHandlerPtr());
        LevelZeroUtils.errorLog("zeCommandListClose", result);
        result = commandQueue.zeCommandQueueExecuteCommandLists(commandQueue.getCommandQueueHandlerPtr(), 1, commandList.getCommandListHandler(), null);
        LevelZeroUtils.errorLog("zeCommandQueueExecuteCommandLists", result);
        result = commandQueue.zeCommandQueueSynchronize(commandQueue.getCommandQueueHandlerPtr(), Long.MAX_VALUE);
        LevelZeroUtils.errorLog("zeCommandQueueSynchronize", result);

        // Reset a command List
        commandList.zeCommandListReset(commandList.getCommandListHandlerPtr());

        boolean isValid = true;
        for (int i = 0; i < allocSize; i++) {
            if (heapBuffer[i] != heapBuffer2[i]) {
                System.out.println(heapBuffer[i] + " != " + heapBuffer2[i]);
                isValid = false;
                break;
            }
        }

        // Free resources
        context.zeMemFree(context.getDefaultContextPtr(), deviceBuffer);
        context.zeCommandListDestroy(commandList.getCommandListHandler());
        context.zeCommandQueueDestroy(commandQueue.getCommandQueueHandle());
        return isValid;
    }

    /**
     * This example creates a buffer using the zeMemHostAlloc and copies data from
     * this buffer into the device buffer created with zeMemDeviceAlloc.
     *
     * @param context
     *            {@link LevelZeroContext}
     * @param device
     *            {@link LevelZeroDevice}
     * @return True if the buffers are identical
     */
    public static boolean testAppendMemoryCopyFromHostToDeviceToHeap(LevelZeroContext context, LevelZeroDevice device) {

        final int allocSize = 4096;
        byte[] heapBuffer = new byte[allocSize];

        LevelZeroByteBuffer hostBuffer = new LevelZeroByteBuffer();
        LevelZeroByteBuffer deviceBuffer = new LevelZeroByteBuffer();

        LevelZeroCommandQueue commandQueue = createCommandQueue(context, device);
        LevelZeroCommandList commandList = createCommandList(context, device);

        ZeHostMemAllocDescriptor hostMemAllocDesc = new ZeHostMemAllocDescriptor();
        hostMemAllocDesc.setFlags(0);
        int result = context.zeMemAllocHost(context.getContextHandle().getContextPtr()[0], hostMemAllocDesc, allocSize, 1, hostBuffer);
        LevelZeroUtils.errorLog("zeMemAllocHost", result);

        ZeDeviceMemAllocDescriptor deviceMemAllocDesc = new ZeDeviceMemAllocDescriptor();
        deviceMemAllocDesc.setOrdinal(0);
        deviceMemAllocDesc.setFlags(0);
        result = context.zeMemAllocDevice(context.getContextHandle().getContextPtr()[0], deviceMemAllocDesc, allocSize, allocSize, device.getDeviceHandlerPtr(), deviceBuffer);
        LevelZeroUtils.errorLog("zeMemAllocDevice", result);

        // Initialize buffers
        byte[] initArray = new byte[allocSize];
        Arrays.fill(initArray, (byte) 100);
        hostBuffer.copy(initArray);
        Arrays.fill(heapBuffer, (byte) 0);

        // Copy from ze Host Allocated -> Device Allocated Memory
        result = commandList.zeCommandListAppendMemoryCopy(commandList.getCommandListHandlerPtr(), deviceBuffer, hostBuffer, allocSize, null, 0, null);
        LevelZeroUtils.errorLog("zeCommandListAppendMemoryCopy", result);
        result = commandList.zeCommandListAppendBarrier(commandList.getCommandListHandlerPtr(), null, 0, null);
        LevelZeroUtils.errorLog("zeCommandListAppendBarrier", result);

        // Copy From Device-Allocated memory to host (heapBuffer2)
        result = commandList.zeCommandListAppendMemoryCopy(commandList.getCommandListHandlerPtr(), heapBuffer, deviceBuffer, allocSize, null, 0, null);
        LevelZeroUtils.errorLog("zeCommandListAppendMemoryCopy", result);

        // Close the command list
        result = commandList.zeCommandListClose(commandList.getCommandListHandlerPtr());
        LevelZeroUtils.errorLog("zeCommandListClose", result);
        result = commandQueue.zeCommandQueueExecuteCommandLists(commandQueue.getCommandQueueHandlerPtr(), 1, commandList.getCommandListHandler(), null);
        LevelZeroUtils.errorLog("zeCommandQueueExecuteCommandLists", result);
        result = commandQueue.zeCommandQueueSynchronize(commandQueue.getCommandQueueHandlerPtr(), Long.MAX_VALUE);
        LevelZeroUtils.errorLog("zeCommandQueueSynchronize", result);

        byte[] b = hostBuffer.getByteBuffer();
        boolean isValid = true;
        for (int i = 0; i < allocSize; i++) {
            if (heapBuffer[i] != b[i]) {
                System.out.println(heapBuffer[i] + " != " + b[i]);
                isValid = false;
                break;
            }
        }

        // Free resources
        context.zeMemFree(context.getDefaultContextPtr(), hostBuffer);
        context.zeMemFree(context.getDefaultContextPtr(), deviceBuffer);
        context.zeCommandListDestroy(commandList.getCommandListHandler());
        context.zeCommandQueueDestroy(commandQueue.getCommandQueueHandle());
        return isValid;
    }

    private static boolean testAppendMemoryCopyFromHeapToSubregionDeviceToHeap(LevelZeroContext context, LevelZeroDevice device) {

        // Device Buffer is 4096 Bytes
        final int allocSize = 4096;
        LevelZeroByteBuffer deviceBuffer = new LevelZeroByteBuffer();

        // SubRegion is 1024 bytes
        final int subRegionBuffer = 1024;
        byte[] heapBuffer = new byte[subRegionBuffer];
        byte[] heapBuffer2 = new byte[subRegionBuffer];

        LevelZeroCommandQueue commandQueue = createCommandQueue(context, device);
        LevelZeroCommandList commandList = createCommandList(context, device);

        ZeDeviceMemAllocDescriptor deviceMemAllocDesc = new ZeDeviceMemAllocDescriptor();
        deviceMemAllocDesc.setOrdinal(0);
        deviceMemAllocDesc.setFlags(0);
        int alignment = 1;

        // This is the equivalent of a clCreateBuffer
        int result = context.zeMemAllocDevice(context.getContextHandle().getContextPtr()[0], deviceMemAllocDesc, allocSize, alignment, device.getDeviceHandlerPtr(), deviceBuffer);
        LevelZeroUtils.errorLog("zeMemAllocDevice", result);

        // Initialize second buffer (Java side) to 0
        Arrays.fill(heapBuffer2, (byte) 0);

        // Fill heap buffer (Java side)
        for (int i = 0; i < subRegionBuffer; i++) {
            heapBuffer[i] = 'a';
        }

        int deviceOffset = 1024;
        int hostOffset = 0;

        // Copy from HEAP -> Device Allocated Memory
        result = commandList.zeCommandListAppendMemoryCopyWithOffset(commandList.getCommandListHandlerPtr(), deviceBuffer, heapBuffer, subRegionBuffer, deviceOffset, hostOffset, null, 0, null);
        LevelZeroUtils.errorLog("zeCommandListAppendMemoryCopyWithOffset", result);
        result = commandList.zeCommandListAppendBarrier(commandList.getCommandListHandlerPtr(), null, 0, null);
        LevelZeroUtils.errorLog("zeCommandListAppendBarrier", result);

        // Copy From Device-Allocated memory to host (heapBuffer2)
        result = commandList.zeCommandListAppendMemoryCopyWithOffset(commandList.getCommandListHandlerPtr(), heapBuffer2, deviceBuffer, subRegionBuffer, hostOffset, deviceOffset, null, 0, null);
        LevelZeroUtils.errorLog("zeCommandListAppendMemoryCopy", result);

        // Close the command list
        result = commandList.zeCommandListClose(commandList.getCommandListHandlerPtr());
        LevelZeroUtils.errorLog("zeCommandListClose", result);
        result = commandQueue.zeCommandQueueExecuteCommandLists(commandQueue.getCommandQueueHandlerPtr(), 1, commandList.getCommandListHandler(), null);
        LevelZeroUtils.errorLog("zeCommandQueueExecuteCommandLists", result);
        result = commandQueue.zeCommandQueueSynchronize(commandQueue.getCommandQueueHandlerPtr(), Long.MAX_VALUE);
        LevelZeroUtils.errorLog("zeCommandQueueSynchronize", result);

        boolean isValid = true;
        for (int i = 0; i < subRegionBuffer; i++) {
            if (heapBuffer[i] != heapBuffer2[i]) {
                System.out.println(heapBuffer[i] + " != " + heapBuffer2[i]);
                isValid = false;
                break;
            }
        }

        // Free resources
        context.zeMemFree(context.getDefaultContextPtr(), deviceBuffer);
        context.zeCommandListDestroy(commandList.getCommandListHandler());
        context.zeCommandQueueDestroy(commandQueue.getCommandQueueHandle());
        return isValid;
    }

    public static void main(String[] args) {
        LevelZeroDriver driver = new LevelZeroDriver();
        LevelZeroContext context = zeInitContext(driver);
        LevelZeroDevice device = zeGetDevices(context, driver);

        ZeDeviceProperties deviceProperties = device.getDeviceProperties();
        System.out.println("Device: ");
        System.out.println("\tName     : " + deviceProperties.getName());
        System.out.println("\tVendor ID: " + Integer.toHexString(deviceProperties.getVendorId()));

        boolean isValid = testAppendMemoryCopyFromHeapToDeviceToHeap(context, device);
        if (isValid) {
            isValid = testAppendMemoryCopyFromHostToDeviceToHeap(context, device);
        }
        if (isValid) {
            isValid = testAppendMemoryCopyFromHeapToSubregionDeviceToHeap(context, device);
        }
        System.out.println("is valid? " + isValid);
    }

}
