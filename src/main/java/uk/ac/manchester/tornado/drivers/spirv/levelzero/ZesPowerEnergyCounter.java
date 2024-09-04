package uk.ac.manchester.tornado.drivers.spirv.levelzero;

// zes_power_energy_counter_t structure from levelzero Sysman 

public class ZesPowerEnergyCounter {

    public long energy;
    public long timestamp;

    public ZesPowerEnergyCounter(long energy, long timestamp) {
        this.energy = energy;
        this.timestamp = timestamp;
    }

    public long getEnergy() {
        return energy;
    }

    public long getTimestamp() {
        return timestamp;
    }

    
}
