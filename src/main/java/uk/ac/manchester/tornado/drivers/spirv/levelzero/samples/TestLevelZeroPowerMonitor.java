package uk.ac.manchester.tornado.drivers.spirv.levelzero.samples;

import java.io.IOException;
import java.util.List;

import uk.ac.manchester.tornado.drivers.spirv.levelzero.LevelZeroPowerMonitor;
import uk.ac.manchester.tornado.drivers.spirv.levelzero.ZesPowerEnergyCounter;

public class TestLevelZeroPowerMonitor {
    
    
    public static void main(String[] args) throws IOException {

        LevelZeroPowerMonitor powerUsage = new LevelZeroPowerMonitor();
        long[] sysmanDevices = powerUsage.ZesInit();
        
        int checkResult = powerUsage.checkPowerQueryPossible(sysmanDevices);

        if (checkResult == -1) {
            System.out.println("Power query is not possible. No sysman devices found.");
        } else if (checkResult == -2 ) {
            System.err.println("Power query is not possible. All devices are Integrated GPUs.");
        }
        else {
            // Success - There are query-able devices
        }

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
