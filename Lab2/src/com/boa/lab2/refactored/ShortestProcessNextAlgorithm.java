package com.boa.lab2.refactored;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Vector;

public class ShortestProcessNextAlgorithm {
    private static String stringInfo(String text,int currentProcess, SchedulingProcess process){
        return "Process: " +
                currentProcess + " "+text+"... ("
                + process.stringProcess() + ")";
    }
    private static int getNext(Vector<SchedulingProcess> processVector, int current, int without){
        int size = processVector.size();
        int index = current;
        for (int i = size - 1; i >= 0; i--) {
            var process = processVector.get(i);
            if (process.getCpudone() < process.getCputime() && without != i) {
                index = i;
            }
        }
        return index;
    }
    public static Results Run(int runtime, Vector<SchedulingProcess> processes, String resultsFile) {
        Results result = new Results("SPN (Nonpreemptive)",
                "Process-Shortest Process-Served", 0);
        Vector<SchedulingProcess> processVector = (Vector<SchedulingProcess>) processes.clone();
        Collections.sort(processVector);
        int comptime = 0;
        int currentProcess = 0;
        int previousProcess = 0;
        int size = processVector.size();
        int completed = 0;
        try {
            PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
            SchedulingProcess process = processVector.get(currentProcess);
            out.println(stringInfo("registered",currentProcess, process));
            while (comptime < runtime) {
                if (process.getCpudone() == process.getCputime()) {
                    completed++;
                    out.println(stringInfo("completed", currentProcess, process));
                    if (completed == size) {
                        result.setCompuTime(comptime);
                        out.close();
                        return result;
                    }
                    currentProcess = getNext(processVector, currentProcess, -1);
                    process = processVector.get(currentProcess);
                    out.println(stringInfo("registered", currentProcess, process));
                }
                if (process.getIoblocking() == process.getIonext()) {
                    out.println(stringInfo("I/O blocked",currentProcess, process));
                    process.setNumblocked(process.getNumblocked() + 1) ;
                    process.setIonext(0);
                    previousProcess = currentProcess;
                    currentProcess = getNext(processVector, currentProcess, previousProcess);
                    process = processVector.get(currentProcess);
                    out.println(stringInfo("registered",currentProcess, process));
                }
                process.setCpudone(process.getCpudone() + 1);
                if (process.getIoblocking() > 0) {
                    process.setIonext(process.getIonext() + 1);
                }
                comptime++;
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        result.setCompuTime(comptime);
        return result;
    }
}
