package com.boa.lab2.refactored;

import java.util.ArrayList;
import java.util.List;

public class SchedulingSettings{
    private int processNum = 5;
    private int meanDev = 1000;
    private int standardDev = 100;
    private List<Integer> processes = new ArrayList<>();
    private int runtime = 1000;

    public SchedulingSettings() {
    }

    public SchedulingSettings(int processNum, int meanDev, int standardDev, List<Integer> processes, int runtime) {
        this.processNum = processNum;
        this.meanDev = meanDev;
        this.standardDev = standardDev;
        this.processes = processes;
        this.runtime = runtime;
    }

    @Override
    public String toString() {
        return "SchedulingSettings{" +
                "processNum=" + processNum +
                ", meanDev=" + meanDev +
                ", standardDev=" + standardDev +
                ", processes=" + processes +
                ", runtime=" + runtime +
                '}';
    }

    public List<Integer> getProcesses() {
        return processes;
    }

    public void setProcesses(List<Integer> processes) {
        this.processes = processes;
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
