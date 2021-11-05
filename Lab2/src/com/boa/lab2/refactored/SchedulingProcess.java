package com.boa.lab2.refactored;

public class SchedulingProcess implements Comparable<SchedulingProcess>{
  private int cputime;
  private int ioblocking;
  private int cpudone;
  private int ionext;
  private int numblocked;

  public SchedulingProcess(int cputime, int ioblocking, int cpudone, int ionext, int numblocked) {
    this.cputime = cputime;
    this.ioblocking = ioblocking;
    this.cpudone = cpudone;
    this.ionext = ionext;
    this.numblocked = numblocked;
  }
  public String stringProcess(){
    return cputime + " " +
            ioblocking + " " +
            cpudone;
  }
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(cputime).append(" (ms)\t");
    if (cputime < 100) builder.append('\t');
    builder.append(ioblocking).append(" (ms)\t");
    if (ioblocking < 100) builder.append('\t');
    builder.append(cpudone).append(" (ms)\t");
    if (cpudone < 100) builder.append('\t');
    builder.append(numblocked).append(" times");
    return builder.toString();
  }

  public int getCputime() {
    return cputime;
  }

  public void setCputime(int cputime) {
    this.cputime = cputime;
  }

  public int getIoblocking() {
    return ioblocking;
  }

  public void setIoblocking(int ioblocking) {
    this.ioblocking = ioblocking;
  }

  public int getCpudone() {
    return cpudone;
  }

  public void setCpudone(int cpudone) {
    this.cpudone = cpudone;
  }

  public int getIonext() {
    return ionext;
  }

  public void setIonext(int ionext) {
    this.ionext = ionext;
  }

  public int getNumblocked() {
    return numblocked;
  }

  public void setNumblocked(int numblocked) {
    this.numblocked = numblocked;
  }

  @Override
  public int compareTo(SchedulingProcess o) {
    return Integer.compare(cputime, o.cputime);
  }
}
