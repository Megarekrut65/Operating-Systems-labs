package com.boa.refactored;

public class Instruction {
    private final String inst;
    private final long addr;

    public Instruction(String inst, long addr) {
        this.inst = inst;
        this.addr = addr;
    }

    public String getInst() {
        return inst;
    }

    public long getAddr() {
        return addr;
    }

    public boolean isRead() {
        return inst.startsWith("READ");
    }

    public boolean isWrite() {
        return inst.startsWith("WRITE");
    }
}
