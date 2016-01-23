/*
 * Copyright (C) 2015 William Matrix Peckham
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.matrixpeckham.raytracer.textures.image;

import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author William Matrix Peckham
 */
public class Image {
    private int hres=100;
    private int vres=100;
    private ArrayList<RGBColor> pixels = new ArrayList<>();
    public Image(){}
    public Image(Image img){
        hres=img.hres;
        vres=img.vres;
        pixels.clear();
        pixels.addAll(img.pixels);
    }
    public RGBColor getColor(int row, int col){
        int index = col+hres*(vres-row-1);
        int n = pixels.size();
        if(index<n && index>=0){
            return pixels.get(index);
        } else {
            return Utility.RED;
        }
    }
    
    public Image clone(){
        return new Image(this);
    }
    
    public void loadFromBufferedImage(BufferedImage bi){
        hres=bi.getWidth();
        vres=bi.getHeight();
        for(int y = 0; y<vres; y++){
            for(int x= 0; x<hres; x++){
                Color col = new Color(bi.getRGB(x, y));
                double r = col.getRed()/255.0;
                double g = col.getGreen()/255.0;
                double b = col.getBlue()/255.0;
                pixels.add(new RGBColor(r, g, b));
            }
        }
    }
    
    private String readWord(BufferedInputStream in) throws IOException{
        StringBuilder s = new StringBuilder();
        char c =(char)in.read();
        while(c=='#'){
            while("\r\n".indexOf(c)==-1){
                c=(char)in.read();
//                s.append(c);
            }
            c=(char)in.read();
        }
        while(" \t\r\n".indexOf(c)==-1){
            s.append(c);
            c=(char)in.read();
        }
        return s.toString().trim();
    }
    
    /**
     * Reads a PPM file, this method reads the header and calls helper functions.
     * @param f
     * @throws IOException 
     */
    public void loadPPMFile(File f) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
        String magic = readWord(in); 
        if(!(magic.equals("P6")||magic.equals("P3"))){
            throw new IOException("File is not PPM or is Corrupted");
        }
        boolean binary = magic.charAt(1)=='6';
        int width = Integer.parseInt(readWord(in));
        int height = Integer.parseInt(readWord(in));
        hres=width;
        vres=height;
        int maxColor = Integer.parseInt(readWord(in));
        if(maxColor>255&&binary){
            read2Byte(in,width,height,maxColor);
            return;
        }
        if(binary){
            readBinary(in,width,height,maxColor);
        } else {
            readAscii(in, width, height, maxColor);
        }
    }

    private void read2Byte(BufferedInputStream in, int width, int height,
            int maxColor) throws IOException {
        for(int y = 0; y<height; y++){
            for(int x = 0; x<width; x++){
                int r = (in.read()<<2);
                r|=in.read();
                int g = (in.read()<<2);
                g|=in.read();
                int b = (in.read()<<2);
                b|=in.read();
                double rd = (double)r/(double)maxColor;
                double gd = (double)g/(double)maxColor;
                double bd = (double)b/(double)maxColor;
                pixels.add(new RGBColor(rd, gd, bd));
            }
        }
    }
    private void readBinary(BufferedInputStream in, int width, int height,
            int maxColor) throws IOException {
        for(int y = 0; y<height; y++){
            for(int x = 0; x<width; x++){
                int r = in.read();
                int g = in.read();
                int b = in.read();
                double rd = (double)r/(double)maxColor;
                double gd = (double)g/(double)maxColor;
                double bd = (double)b/(double)maxColor;
                pixels.add(new RGBColor(rd, gd, bd));
            }
        }
    }
    
    private void readAscii(BufferedInputStream in, int width, int height,
            int maxColor) throws IOException {
        for(int y = 0; y<height; y++){
            for(int x = 0; x<width; x++){
                int r = Integer.parseInt(readWord(in));
                int g = Integer.parseInt(readWord(in));
                int b = Integer.parseInt(readWord(in));
                double rd = (double)r/(double)maxColor;
                double gd = (double)g/(double)maxColor;
                double bd = (double)b/(double)maxColor;
                pixels.add(new RGBColor(rd, gd, bd));
            }
        }
    }

    public int getHres() {
        return hres;
    }

    public int getVres() {
        return vres;
    }


}
