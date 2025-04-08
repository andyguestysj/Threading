package com.example;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */


    private static final int WIDTH = 1280;
    private static final int HEIGHT = 100;
    private static final int THREADS = 8;
    private static final int CONCURRENT_THREADS = 4;
    

    private static final double START_X = -1.5;
    private static final double END_X = 0.5;
    private static final double START_Y = -1.2;

    private static int img_width = WIDTH; 
    private static int img_height = THREADS * HEIGHT;

    private static int[][] r = new int[img_width][img_height];
    private static int[][] g = new int[img_width][img_height];
    private static int[][] b = new int[img_width][img_height];

    public static void main(String[] args) {
        
        int [][] iterations = new int[img_width][img_height];
        

        double width = END_X - START_X;
        double height = width * ((double)HEIGHT/WIDTH) * 2.0;

        LocalTime startTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
        System.out.println("Start time : " + startTime.format(formatter));

        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_THREADS);



        Mandelbrot mandelbrotSlice;        
        for (int thread = 0; thread < THREADS; thread++) {
            double threadSTART_Y = START_Y + thread * height;
            double threadEndY = START_Y + (thread+1) * height;
            mandelbrotSlice = new Mandelbrot(WIDTH, HEIGHT, thread, START_X, END_X, threadSTART_Y, threadEndY, iterations);            
            executor.execute(mandelbrotSlice);
        }

        executor.shutdown();
        try {
            boolean terminated = executor.awaitTermination(30, TimeUnit.SECONDS);
            if (!terminated) {
                System.out.println("Warning: Executor did not terminate within the timeout. Forcing shutdown.");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.out.println("Executor termination interrupted.");
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        System.out.println("Done");



        mandelbrotColoringRed(iterations);

        new ImageMaker(img_width, img_height, r,g,b);

    
        LocalTime endTime = LocalTime.now();        
        System.out.println("End time : " + endTime.format(formatter));
        Duration duration = Duration.between(startTime,endTime);
        System.out.println("Run time : " + duration.toMillis() + " milliseconds");




    }


    private static int calcMin(int [][] iterations) {
        int minIntensity = 1;
        for (int[] row : iterations) {
            for (int pixel : row) {
                minIntensity = Math.min(minIntensity, pixel);                
            }
        }
        return minIntensity;
    }

    private static int calcMax(int [][] iterations) {
        int maxIntensity = 1;
        for (int[] row : iterations) {
            for (int pixel : row) {
                    maxIntensity = Math.max(maxIntensity, pixel);             
            }
        }
        return maxIntensity;
    }

    
    public static void mandelbrotColoringRed(int [][] iterations) {
        int maxIterations = calcMax(iterations);

        for (int x=0; x<img_width; x++){
            for (int y=0; y<(img_height); y++){
                double factor = Math.sqrt((double)iterations[x][y]/(double)maxIterations);
                if (factor<0.025){
                    r[x][y] = 0;
                    g[x][y] = 0;
                    b[x][y] = 0;
                }
                else if (factor<0.05){
                    double intensity = Math.round(255.0 * ((factor-0.025)*40.0));
                    r[x][y] = (int)intensity;
                    g[x][y] = 0;
                    b[x][y] = 0;
                }
                else if (factor<0.1){
                    double intensity = Math.round(255.0 * ((factor-0.05)*20.0));
                    r[x][y] = 255;
                    g[x][y] = 0;
                    b[x][y] = (int)intensity;
                }
                else if (factor<0.15){
                    double intensity = Math.round(255.0 * ((factor-0.1)*20.0));
                    r[x][y] = 255;
                    g[x][y] = (int)intensity;
                    b[x][y] = (int)intensity;
                }
                else if (factor<0.35){
                    double intensity = Math.round(255.0 * ((factor-0.15)*5.0));
                    r[x][y] = 0;
                    g[x][y] = 255-(int)intensity;
                    b[x][y] = 255-(int)intensity;
                }
                else if (factor<0.55){
                    double intensity = Math.round(255.0 * ((factor-0.35)*5.0));
                    r[x][y] = 0;
                    g[x][y] = 255-(int)intensity;
                    b[x][y] = 0;
                }
            }
        }

    }

}   

