package com.boa.lab2.refactored;

import java.util.Random;

public class Common {
  static public double randomX() {
    Random generator = new Random();
    double U = generator.nextDouble();
    double V = generator.nextDouble();
    double X =  Math.sqrt((8.0/Math.E)) * (V - 0.5)/U;
    if (!(r2(X,U))) { return -1; }
    if (!(r3(X,U))) { return -1; }
    if (!(r4(X,U))) { return -1; }
    return X;
  }

  private static boolean r2(double X, double U) {
    return (X * X) <= (5 - 4 * Math.exp(.25) * U);
  }

  private static boolean r3(double X, double U) {
    return !((X * X) >= (4 * Math.exp(-1.35) / U + 1.4));
  }

  private static boolean r4(double X, double U) {
    return (X * X) < (-4 * Math.log(U));
  }

}

