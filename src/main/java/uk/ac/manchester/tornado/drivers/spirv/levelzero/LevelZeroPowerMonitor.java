package uk.ac.manchester.tornado.drivers.spirv.levelzero;

import java.util.List;

public class LevelZeroPowerMonitor {
    
    static {
        // Use -Djava.library.path=./levelZeroLib/build/
        System.loadLibrary("tornado-levelzero");
    }

    public native long[] ZesInit();
    public native int checkPowerQueryPossible(long[] allSysmanDevices);
    public native long[] getSysmanDevicesToQuery(long[] allSysmanDevices);

    public native List<ZesPowerEnergyCounter> getEnergyCounters(long sysmanDeviceHandle);

    public native double calculatePowerUsage(List<ZesPowerEnergyCounter> initialEnergyCounters, List<ZesPowerEnergyCounter> finalEnergyCounters);

    private native int zesDeviceEnumPowerDomains_native(long deviceHandler, int[] numPowerDomains, long[] hMemory);

    public int zesDeviceEnumPowerDomains(long deviceHandler, int[] numPowerDomains, long[] hMemory) {
        int result = zesDeviceEnumPowerDomains_native(deviceHandler, numPowerDomains, hMemory);
        return result;
    }

}
