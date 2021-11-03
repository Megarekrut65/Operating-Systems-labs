//package com.boa.lab2.refactored;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.PrintStream;
//import java.util.Vector;
//
//public class ShortestProcessNextAlgorithm {
//    private static int getNext(Vector processVector, int without){
//        int size = processVector.size();
//        int min = size - 1;
//        var minProcess = (SchedulingProcess) processVector.elementAt(min);
//        while((minProcess.cpudone >= minProcess.cputime|| min == without) && min > 0 ){
//            min--;
//            minProcess = (SchedulingProcess) processVector.elementAt(min);
//        }
//        for (int i = min - 1; i >= 0; i--) {
//            var process = (SchedulingProcess) processVector.elementAt(i);
//            if (i != without && process.cpudone < process.cputime && process.cputime < minProcess.cputime) {
//                min = i;
//                minProcess = (SchedulingProcess) processVector.elementAt(min);
//            }
//        }
//        return Math.max(min, 0);
//    }
//    public static Results Run(int runtime, Vector processVector) {
//        Results result = new Results("SPN (Nonpreemptive)",
//                "Process-Shortest Process-Served", 0);
//        int comptime = 0;
//        int currentProcess = 0;
//        int previousProcess = 0;
//        int size = processVector.size();
//        int completed = 0;
//        String resultsFile = "Summary-Processes";
//        /////////////////////
//        try {
//            PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
//            SchedulingProcess process = (SchedulingProcess) processVector.elementAt(currentProcess);
//            out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
//            while (comptime < runtime) {
//                if (process.cpudone == process.cputime) {
//                    completed++;
//                    out.println("Process: " + currentProcess + " completed... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
//                    if (completed == size) {
//                        result.compuTime = comptime;
//                        out.close();
//                        return result;
//                    }
//                    currentProcess = getNext(processVector, -1);
//                    process = (SchedulingProcess) processVector.elementAt(currentProcess);
//                    out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
//                }
//                if (process.ioblocking == process.ionext) {
//                    out.println("Process: " + currentProcess + " I/O blocked... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
//                    process.numblocked++;
//                    process.ionext = 0;
//                    previousProcess = currentProcess;
//                    currentProcess = getNext(processVector, previousProcess);
//                    process = (SchedulingProcess) processVector.elementAt(currentProcess);
//                    out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
//                }
//                process.cpudone++;
//                if (process.ioblocking > 0) {
//                    process.ionext++;
//                }
//                comptime++;
//            }
//            out.close();
//        } catch (IOException e) { /* Handle exceptions */ }
//        /////////////////////////////
//        result.compuTime = comptime;
//        return result;
//    }
//}
