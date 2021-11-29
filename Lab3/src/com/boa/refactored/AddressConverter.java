package com.boa.refactored;

public class AddressConverter {
    private final int count;
    private final long blockSize;

    public AddressConverter(int count, long blockSize) {
        this.count = count;
        this.blockSize = blockSize;
    }

    public int virtualToPhysical(long address) {
        for (int i = 0; i <= count; i++) {
            long low = blockSize * i;
            long high = blockSize * (i + 1);
            if (low <= address && address < high) {
                return i;
            }
        }
        return -1;
    }
}
