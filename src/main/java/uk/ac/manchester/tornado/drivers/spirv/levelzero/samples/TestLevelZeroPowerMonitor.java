package uk.ac.manchester.tornado.drivers.spirv.levelzero.samples;

import java.io.IOException;
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
import uk.ac.manchester.tornado.drivers.spirv.levelzero.Ze_Structure_Type;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZesPowerEnergyCounter;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.utils.LevelZeroUtils;

public class TestLevelZeroPowerMonitor {
    
    
    public static void main(String[] args) throws IOException {

        System.out.println("Level-ZERO JNI Library - TestSysmanPowerFunctions");

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

        ZeDeviceProperties deviceProperties = new ZeDeviceProperties();
        result = device.zeDeviceGetProperties(device.getDeviceHandlerPtr(), deviceProperties);
        LevelZeroUtils.errorLog("zeDeviceGetProperties", result);
        System.out.println("Device   : " + deviceProperties.getName());
        System.out.println("Type     : " + deviceProperties.getType());
        System.out.println("Vendor ID: " + deviceProperties.getVendorId());

        // get all device handles
        long[] devicePointers = deviceHandler.getDevicePointers();
        
        LevelZeroPowerMonitor powerUsage = new LevelZeroPowerMonitor();
        
        // cast all device handles to sysman handles    
        long[] sysmanDevices = powerUsage.ZesInit(devicePointers);
        
        // Check if power query is possible for any devices - any iGPU devices?
        int checkResult = powerUsage.checkPowerQueryPossible(sysmanDevices);

        if (checkResult == -1) {
            System.out.println("Power query is not possible. No sysman devices found.");
        } else if (checkResult == -2 ) {
            System.err.println("Power query is not possible. All devices are Integrated GPUs.");
        }
        else {
            // Success - There are query-able devices (at least one dGPU)
        }

        // Get only the devices that we can query (dGPUs)
        long[] devicesToQuery = powerUsage.getSysmanDevicesToQuery(sysmanDevices);

        // Testing with the 1st Sysman device that we can query
        List<ZesPowerEnergyCounter> initialEnergyCounters = powerUsage.getEnergyCounters(devicesToQuery[0]);

        // wait 5 seconds - this is what we're measuring
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<ZesPowerEnergyCounter> finalEnergyCounters = powerUsage.getEnergyCounters(devicesToQuery[0]);
        
        double powerUsage_mW = powerUsage.calculatePowerUsage(initialEnergyCounters, finalEnergyCounters);

        System.out.println("Power Usage: " + powerUsage_mW + " mW");
    }
}
