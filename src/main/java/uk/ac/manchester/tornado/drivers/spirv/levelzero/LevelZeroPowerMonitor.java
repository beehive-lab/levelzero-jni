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
package uk.ac.manchester.tornado.drivers.spirv.levelzero;

import java.util.List;

public class LevelZeroPowerMonitor {

    private native int zesDeviceEnumPowerDomains_native(long deviceHandler, int[] numPowerDomains, long[] hMemory);

    public int zesDeviceEnumPowerDomains(long deviceHandler, int[] numPowerDomains, long[] hMemory) {
        int result = zesDeviceEnumPowerDomains_native(deviceHandler, numPowerDomains, hMemory);
        return result;
    }

    public native int getEnergyCounters_native(long sysmanDeviceHandle, List<ZesPowerEnergyCounter> energyCounterList);
    
    public int getEnergyCounters(long sysmanDeviceHandle, List<ZesPowerEnergyCounter> energyCounterList) {
        int result = getEnergyCounters_native(sysmanDeviceHandle, energyCounterList);
        return result;  
    }

    /**
     * Checks the power support status for a given device.
     *
     * @param sysmanDevice the device to check the power support status for
     * @return true if the device supports power (has 1 or more power domains), false otherwise
     */
    public boolean getPowerSupportStatusForDevice(long sysmanDevice) {
        int[] numPowerDomains = new int[1];
        zesDeviceEnumPowerDomains(sysmanDevice, numPowerDomains, null);
        if (numPowerDomains[0] > 0) {
            return true;
        } else {
            return false;
        }
    }

    public double calculatePowerUsage_mW(List<ZesPowerEnergyCounter> initialEnergyCounters, List<ZesPowerEnergyCounter> finalEnergyCounters) {
        if (initialEnergyCounters.size() != finalEnergyCounters.size()) {
            throw new IllegalArgumentException("Initial and final energy counter lists must have the same size.");
        }
        long totalEnergyUsed = 0;
        long totalTimeElapsed = 0;
        for (int i = 0; i < initialEnergyCounters.size(); i++) {
            ZesPowerEnergyCounter initialCounter = initialEnergyCounters.get(i);
            ZesPowerEnergyCounter finalCounter = finalEnergyCounters.get(i);

            long initialEnergy = initialCounter.getEnergy();
            long initialTimestamp = initialCounter.getTimestamp();
            long finalEnergy = finalCounter.getEnergy();
            long finalTimestamp = finalCounter.getTimestamp();

            totalEnergyUsed += finalEnergy - initialEnergy;
            totalTimeElapsed += finalTimestamp - initialTimestamp;
        }
        return totalTimeElapsed != 0 ? (double) totalEnergyUsed / totalTimeElapsed * 1000 : 0.0;
    } 
}
