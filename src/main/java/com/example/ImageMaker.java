package com.example;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;

public class ImageMaker {

  int width = 640;
  int height = 480;

  ImageMaker(int width, int height, int[][] r, int[][] g, int[][] b){

    this.width = width; this.height = height;

    try {      
      
      BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            int rgb = r[x][y];
            rgb = (rgb << 8) + g[x][y]; 
            rgb = (rgb << 8) + b[x][y];
            image.setRGB(x, y, rgb);
        }
      }



      File outputfile = new File("image.jpg");
      ImageIO.write(image, "jpg", outputfile);
     
    }
    catch (IOException e){
      throw new RuntimeException(e);
    }
  }

  
}
