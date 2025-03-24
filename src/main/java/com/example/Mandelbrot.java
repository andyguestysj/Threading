package com.example;

import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Mandelbrot implements Runnable{

  private static final int MAX_ITERATIONS = 10000;

  private final double xSkip;
  private final double ySkip;
  private final double x0;
  private final double y0;
  private final int pixelWidth;
  private final int pixelHeight;
  private int[][] iterations;
  private int row;
  
  Mandelbrot(int pixelWidth, int pixelHeight, int row, double startX, double endX, double startY, double endY, int [][] iterations) {
      this.pixelWidth = pixelWidth;
      this.pixelHeight = pixelHeight;
      this.x0 = startX;
      this.xSkip = (endX - startX) / pixelWidth;
      this.y0 = startY;
      this.ySkip = (endY - startY) / pixelHeight;
      this.iterations = iterations;
      this.row = row;

  }

    
    public void run()  {
      LocalTime threadStartTime = LocalTime.now();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
      System.out.println("\tThread [" + this.row + "] Start time : " + threadStartTime.format(formatter));

      for (int y = 0; y < pixelHeight; y++) {          
          for (int x = 0; x < pixelWidth; x++) {                
              //iterations[x][y+(this.row*this.pixelHeight)] = calculatePixel(x, y);
              int pixelVal = calculatePixel(x, y);
              updateIterations(x, y+(this.row*this.pixelHeight), pixelVal, iterations);
          }            
      }                

      LocalTime threadEndTime = LocalTime.now();      
      System.out.println("\tThread [" + this.row + "] End time : " + threadEndTime.format(formatter));
      Duration duration = Duration.between(threadStartTime, threadEndTime);
      System.out.println("\tThread [" + this.row + "] Run time : " + duration.toMillis() + " milliseconds");

  
      }

  public int calculatePixel(int pixelX, int pixelY) {
    double x = x0 + pixelX * xSkip;
    double y = y0 + pixelY * ySkip;
    double ix = 0;
    double iy = 0;
    int iteration = 0;
    while (ix * ix + iy * iy < 4 && iteration < MAX_ITERATIONS) {
        double xtemp = ix * ix - iy * iy + x;
        iy = 2 * ix * iy + y;
        ix = xtemp;
        iteration++;
    }
    
    return iteration;
  }

  public synchronized void updateIterations(int x, int y, int itval, int[][] iterations){
    iterations[x][y] = itval;
  }

}
