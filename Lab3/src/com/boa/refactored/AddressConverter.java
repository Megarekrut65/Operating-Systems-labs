package com.boa.refactored;

public class AddressConverter {
    private final int count;
    private final long blockSize;

    public AddressConverter(int count, long blockSize) {
        this.count = count;
        this.blockSize = blockSize;
    }
    public int virtualToPhysical(long address){
        return Virtual2Physical.pageNum(address,count,blockSize);
    }
}
