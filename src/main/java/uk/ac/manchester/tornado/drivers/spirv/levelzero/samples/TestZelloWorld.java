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

import java.io.IOException;

import uk.ac.manchester.tornado.drivers.spirv.levelzero.LevelZeroContext;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.LevelZeroDevice;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.LevelZeroDriver;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeAPIVersion;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeCommandListHandle;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeCommandQueueDescriptor;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeContextDescriptor;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeDevicesHandle;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeDriverHandle;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeDriverProperties;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeEventDescriptor;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeEventHandle;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeEventPoolDescriptor;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeEventPoolFlags;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeEventPoolHandle;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeEventScopeFlags;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeInitFlag;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.Ze_Structure_Type;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.utils.LevelZeroUtils;

/**
 * This example is a replica of the LevelZero zello_world.cpp example implemented in Java.
 * How to run?
 *
 * <code>
 *     tornado uk.ac.manchester.tornado.drivers.spirv.levelzero.samples.TestZelloWorld copyData.spv
 * </code>
 */
public class TestZelloWorld {

    // Test Program
    public static void main(String[] args) throws IOException {
        System.out.println("Level-ZERO JNI Library - TestZelloWorld");

        // Create the Level Zero Driver
        LevelZeroDriver driver = new LevelZeroDriver();
        int result = driver.zeInit(ZeInitFlag.ZE_INIT_FLAG_GPU_ONLY);
        LevelZeroUtils.errorLog("zeInit", result);

        int[] numDrivers = new int[1];
        result = driver.zeDriverGet(numDrivers, null);
        LevelZeroUtils.errorLog("zeDriverGet", result);

        ZeDriverHandle driverHandler = new ZeDriverHandle(numDrivers[0]);

        result = driver.zeDriverGet(numDrivers, driverHandler);
        LevelZeroUtils.errorLog("zeDriverGet", result);

        // ============================================
        // Create the Context
        // ============================================
        // Create context Description
        ZeContextDescriptor contextDescription = new ZeContextDescriptor();
        // Create context object
        LevelZeroContext context = new LevelZeroContext(driverHandler, contextDescription);
        // Call native method for creating the context
        result = context.zeContextCreate(driverHandler.getZe_driver_handle_t_ptr()[0]);
        LevelZeroUtils.errorLog("zeContextCreate", result);

        // Get number of devices in a driver
        int[] deviceCount = new int[1];
        result = driver.zeDeviceGet(driverHandler, 0, deviceCount, null);
        LevelZeroUtils.errorLog("zeDeviceGet", result);

        // Instantiate a device Handler
        ZeDevicesHandle deviceHandler = new ZeDevicesHandle(deviceCount[0]);
        result = driver.zeDeviceGet(driverHandler, 0, deviceCount, deviceHandler);
        LevelZeroUtils.errorLog("zeDeviceGet", result);

        // ============================================
        // Query driver properties
        // ============================================
        ZeDriverProperties driverProperties = new ZeDriverProperties(Ze_Structure_Type.ZE_STRUCTURE_TYPE_DRIVER_PROPERTIES);
        result = driver.zeDriverGetProperties(driverHandler, 0, driverProperties);
        LevelZeroUtils.errorLog("zeDriverGetProperties", result);

        System.out.println("Driver Version: " + driverProperties.getDriverVersion());

        ZeAPIVersion apiVersion = new ZeAPIVersion();
        result = driver.zeDriverGetApiVersion(driverHandler, 0, apiVersion);
        LevelZeroUtils.errorLog("zeDriverGetApiVersion", result);

        System.out.println("Level Zero API Version: " + apiVersion);

        // ============================================
        // Query device properties
        // ============================================
        LevelZeroDevice device = driver.getDevice(driverHandler, 0);

        // ============================================
        // Create an immediate command list for direct submission
        // ============================================
        ZeCommandQueueDescriptor commandQueueDescription = new ZeCommandQueueDescriptor();
        ZeCommandListHandle commandList = new ZeCommandListHandle();
        result = context.zeCommandListCreateImmediate(context.getContextHandle().getContextPtr()[0], device.getDeviceHandlerPtr(), commandQueueDescription, commandList);
        LevelZeroUtils.errorLog("zeCommandListCreateImmediate", result);

        // ============================================
        // Create an event to be signaled by the device
        // ============================================
        ZeEventPoolHandle eventPoolHandle = new ZeEventPoolHandle();
        ZeEventHandle event = new ZeEventHandle();

        ZeEventPoolDescriptor eventPoolDescription = new ZeEventPoolDescriptor();
        eventPoolDescription.setCount(1);
        eventPoolDescription.setFlags(ZeEventPoolFlags.ZE_EVENT_POOL_FLAG_HOST_VISIBLE);

        result = context.zeEventPoolCreate(context.getDefaultContextPtr(), eventPoolDescription, 1, device.getDeviceHandlerPtr(), eventPoolHandle);
        LevelZeroUtils.errorLog("zeEventPoolCreate", result);

        // ============================================
        // Create an Event
        // ============================================
        ZeEventDescriptor eventDescription = new ZeEventDescriptor();
        eventDescription.setIndex(0);
        eventDescription.setSignal(ZeEventScopeFlags.ZE_EVENT_SCOPE_FLAG_HOST);
        eventDescription.setWait(ZeEventScopeFlags.ZE_EVENT_SCOPE_FLAG_HOST);
        result = context.zeEventCreate(eventPoolHandle, eventDescription, event);
        LevelZeroUtils.errorLog("zeEventCreate", result);

        // ============================================
        // Signal the event from the device and wait for completion
        // ============================================
        //TODO Add support for those functions
//        zeCommandListAppendSignalEvent(command_list, event);
//        zeEventHostSynchronize(event, UINT64_MAX );

        // ============================================
        // Destroy objects
        // ============================================
        result = driver.zeContextDestroy(context);
        LevelZeroUtils.errorLog("zeContextDestroy", result);

        result = context.zeCommandListDestroy(commandList);
        LevelZeroUtils.errorLog("zeCommandListDestroy", result);

        result = context.zeEventDestroy(event);
        LevelZeroUtils.errorLog("zeEventDestroy", result);

        result = context.zeEventPoolDestroy(eventPoolHandle);
        LevelZeroUtils.errorLog("zeEventPoolDestroy", result);

        System.out.println("Congratulations, the device completed execution!");
    }
}