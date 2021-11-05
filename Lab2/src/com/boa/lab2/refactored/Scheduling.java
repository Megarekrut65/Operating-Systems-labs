package com.boa.lab2.refactored;
/*
 This file contains the main() function for the Scheduling
 simulation.  Init() initializes most of the variables by
 reading from a provided file.  SchedulingAlgorithm.Run() is
 called from main() to run the simulation.  Summary-Results
 is where the summary results are written, and Summary-Processes
 is where the process scheduling summary is written.

 Created by Alexander Reeder, 2001 January 06
 Refactored by Oleksandr Bandalak, 2020 November 03
 */
import com.boa.lab2.refactored.parser.DOMSettingsParser;
import com.boa.lab2.refactored.parser.SettingsHandler;
import com.boa.lab2.refactored.parser.Validator;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Vector;

public class Scheduling {

  private static SchedulingSettings settings = new SchedulingSettings();
  private static final Vector<SchedulingProcess> processVector = new Vector<>();
  private static String resultsFile = "src/com/boa/lab2/refactored/result/Summary-Results";
  private static String processFile = "src/com/boa/lab2/refactored/result/Summary-Processes";
  private static String schema = "";

  private static void init(String file) {
      String schema = "src/com/boa/lab2/refactored/data/scheduling.xsd";
      DOMSettingsParser parser = new DOMSettingsParser(schema);
      settings = parser.readSettingsFromXmlFile(file);
      if(settings != null){
          var processes = settings.getProcesses();
          for(int i = 0; i < settings.getProcessNum(); i++){
              double dX = Common.randomX();
              while ((dX + 1.0) <= 0.0001) {
                  dX = Common.randomX();
              }
              dX = dX * settings.getStandardDev();
              int cputime = (int) dX + settings.getMeanDev();
              int ioblocking = i*100;
              if(i < processes.size()){
                  ioblocking = processes.get(i);
              }
              processVector.addElement(new SchedulingProcess(cputime, ioblocking, 0, 0, 0));
          }
      }
  }

    private static void debug() {
        System.out.println(settings);
    }
    private static void checkArgs(String[] args){
        if (args.length != 2) {
            System.err.println("Usage: 'java Scheduling <INIT FILE>'");
            System.exit(-1);
        }
        schema = args[1];
        File f = new File(args[0]);
        if (!(f.exists())) {
            System.err.println("Scheduling: error, file '" + f.getName() + "' does not exist.");
            System.exit(-1);
        }
        if (!(f.canRead())) {
            System.err.println("Scheduling: error, read of " + f.getName() + " failed.");
            System.exit(-1);
        }
        try {
            var handler = new SettingsHandler();
            Validator validator = new Validator(schema, handler);
            validator.validate(args[0]);
            if(handler.getErrors().size() != 0){
                System.err.println(handler.getErrors());
                System.exit(-1);
            }
        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }
    private static void printResults(Results result){
        try {
            PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
            out.println("Scheduling Type: " + result.schedulingType);
            out.println("Scheduling Name: " + result.schedulingName);
            out.println("Simulation Run Time: " + result.compuTime);
            out.println("Mean: " + settings.getMeanDev());
            out.println("Standard Deviation: " + settings.getStandardDev());
            out.println("Process #\tCPU Time\tIO Blocking\tCPU Completed\tCPU Blocked");
            for (int i = 0; i < processVector.size(); i++) {
                SchedulingProcess process = processVector.get(i);
                out.print("\t"+i + "\t");
                if (i < 100) out.print("\t");
                out.println(process);
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        checkArgs(args);
        System.out.println("Working...");
        init(args[0]);
        //printResults(SchedulingAlgorithm.Run(settings.getRuntime(), processVector, processFile));
        printResults(ShortestProcessNextAlgorithm.Run(settings.getRuntime(), processVector, processFile));
        System.out.println("Completed.");
    }
}

