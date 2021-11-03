package com.boa.lab2.refactored;
/* Run() is called from Scheduling.main() and is where
 the scheduling algorithm written by the user resides.
 User modification should occur within the Run() function.
*/
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

public class SchedulingAlgorithm {
  private static String stringInfo(String text,int currentProcess, SchedulingProcess process){
    return "Process: " +
            currentProcess + " "+text+"... ("
            + process.stringProcess() + ")";
  }
  public static Results Run(int runtime, Vector<SchedulingProcess> processVector, String resultsFile) {
    Results result = new Results("Batch (Nonpreemptive)",
            "First-Come First-Served", 0);
    int i = 0;
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
            result.compuTime = comptime;
            out.close();
            return result;
          }
          for (i = size - 1; i >= 0; i--) {
            process = processVector.get(i);
            if (process.getCpudone() < process.getCputime()) {
              currentProcess = i;
            }
          }
          process = processVector.elementAt(currentProcess);
          out.println(stringInfo("registered", currentProcess, process));
        }      
        if (process.getIoblocking() == process.getIonext()) {
          out.println(stringInfo("I/O blocked",currentProcess, process));
          process.setNumblocked(process.getNumblocked() + 1) ;
          process.setIonext(0);
          previousProcess = currentProcess;
          for (i = size - 1; i >= 0; i--) {
            process = processVector.get(i);
            if (process.getCpudone() < process.getCputime() && previousProcess != i) {
              currentProcess = i;
            }
          }
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
    result.compuTime = comptime;
    return result;
  }
}
