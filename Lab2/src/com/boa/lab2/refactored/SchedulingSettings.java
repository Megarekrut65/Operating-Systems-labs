package com.boa.lab2.refactored;

public class SchedulingSettings{
    private int processNum = 5;
    private int meanDev = 1000;
    private int standardDev = 100;
    private int runtime = 1000;

    public SchedulingSettings() {
    }

    public SchedulingSettings(int processNum, int meanDev, int standardDev, int runtime) {
        this.processNum = processNum;
        this.meanDev = meanDev;
        this.standardDev = standardDev;
        this.runtime = runtime;
    }

    @Override
    public String toString() {
        return "SchedulingSettings{" +
                "processNum=" + processNum +
                ", meanDev=" + meanDev +
                ", standardDev=" + standardDev +
                ", runtime=" + runtime +
                '}';
    }

    public int getProcessNum() {
        return processNum;
    }

    public void setProcessNum(int processNum) {
        this.processNum = processNum;
    }

    public int getMeanDev() {
        return meanDev;
    }

    public void setMeanDev(int meanDev) {
        this.meanDev = meanDev;
    }

    public int getStandardDev() {
        return standardDev;
    }

    public void setStandardDev(int standardDev) {
        this.standardDev = standardDev;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }
}
