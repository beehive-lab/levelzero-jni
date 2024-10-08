/*
 * MIT License
 *
 * Copyright (c) 2024, APT Group, Department of Computer Science,
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
import java.util.ArrayList;
import java.util.List;

import uk.ac.manchester.tornado.drivers.spirv.levelzero.LevelZeroContext;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.LevelZeroDevice;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.LevelZeroDriver;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.LevelZeroPowerMonitor;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeAPIVersion;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeContextDescriptor;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeDeviceProperties;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeDevicesHandle;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeDriverHandle;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeDriverProperties;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeInitFlag;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZeResult;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.Ze_Structure_Type;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZesPowerEnergyCounter;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.utils.LevelZeroUtils;

public class TestLevelZeroPowerMonitor {
    
    public static void main(String[] args) throws IOException {

        System.out.println("Level-ZERO JNI Library - TestLevelZeroPowerMonitor");

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
        
        ZeDeviceProperties deviceProperties = new ZeDeviceProperties();
     
        // get all device handles
        long[] devicePointers = deviceHandler.getDevicePointers();
        
        LevelZeroPowerMonitor powerUsage = new LevelZeroPowerMonitor();
      
        if (devicePointers.length == 0) {
            throw new IllegalStateException("No devices found.");
        }

        for (int i = 0; i < devicePointers.length; i++) {

            long sysmanDevice = devicePointers[i];
            LevelZeroDevice device = driver.getDevice(driverHandler, i);
    
            result = device.zeDeviceGetProperties(device.getDeviceHandlerPtr(), deviceProperties);
            LevelZeroUtils.errorLog("zeDeviceGetProperties", result);
            
            boolean queryStatus = powerUsage.getPowerSupportStatusForDevice(sysmanDevice);

            if (queryStatus) {
                System.out.println("Power query is possible for device: " + deviceProperties.getName());
            } else {
                throw new IllegalStateException("No power domains found for device: " + deviceProperties.getName());
            }

            List<ZesPowerEnergyCounter> initialEnergyCounters = new ArrayList<>();
            result = powerUsage.getEnergyCounters(sysmanDevice, initialEnergyCounters);

            if (result != ZeResult.ZE_RESULT_SUCCESS) {
                throw new RuntimeException("Failed to get initial energy counters. Error code: " + result);
            }

            // wait 5 seconds - this is what we're measuring
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            List<ZesPowerEnergyCounter> finalEnergyCounters = new ArrayList<>();
            result = powerUsage.getEnergyCounters(sysmanDevice, finalEnergyCounters);

            if (result != ZeResult.ZE_RESULT_SUCCESS) {
                throw new RuntimeException("Failed to get final energy counters. Error code: " + result);
            }
            
            double powerUsage_mW = powerUsage.calculatePowerUsage_mW(initialEnergyCounters, finalEnergyCounters);
            System.out.println("Power Usage: " + powerUsage_mW + " mW");
        }
    }
}
