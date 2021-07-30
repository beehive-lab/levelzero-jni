package uk.ac.manchester.tornado.drivers.spirv.levelzero;

public class LevelZeroFence {

    private ZeFenceHandle fenceHandler;
    private ZeFenceDesc fenceDesc;

    public LevelZeroFence(ZeFenceDesc desc, ZeFenceHandle handler) {
        this.fenceDesc = desc;
        this.fenceHandler = handler;
    }

    private native int zeFenceCreate_native(long commandQueueHandlerPointer, ZeFenceDesc fenceDesc, ZeFenceHandle fenceHandler);

    public int zeFenceCreate(long commandQueueHandlerPointer, ZeFenceDesc fenceDesc, ZeFenceHandle fenceHandler) {
        return zeFenceCreate_native(commandQueueHandlerPointer, fenceDesc, fenceHandler);
    }
}
