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
 * Class for images. Holds float RGB color pixels.
 *
 * @author William Matrix Peckham
 */
public class Image {

    /**
     * horizontal resolution, width, max x
     */
    private int hres = 100;

    /**
     * vertical resolution, height, max y
     */
    private int vres = 100;

    /**
     * pixels
     */
    private ArrayList<RGBColor> pixels = new ArrayList<>();

    /**
     * default constructor (blank image, size (100,100) returns all RED.
     */
    public Image() {
    }

    /**
     * Copy constructor.
     *
     * @param img
     */
    public Image(Image img) {
        hres = img.hres;
        vres = img.vres;
        pixels.clear();
        pixels.addAll(img.pixels);
    }

    /**
     * access given pixel if row or col is out of bounds returns RED
     *
     * @param row
     * @param col
     * @return
     */
    public RGBColor getColor(int row, int col) {
        //calculate index
        int index = col + hres * (vres - row - 1);
        //total pixels
        int n = pixels.size();
        //index in bounds
        if (index < n && index >= 0) {
            return pixels.get(index);
        } else {
            return Utility.RED;
        }
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Image clone() {
        return new Image(this);
    }

    /**
     * Loads an image from a BufferedImage, utility function, not used in code
     * from book but useful for people who want to test with their own images in
     * PNG, JPEG, or BMP formats, which ImageIO can read into a BufferedImage.
     *
     * @param bi
     */
    public void loadFromBufferedImage(BufferedImage bi) {
        //get size
        hres = bi.getWidth();
        vres = bi.getHeight();

        //loop through all image pixels in left to right, top to bottom order
        for (int y = 0; y < vres; y++) {
            for (int x = 0; x < hres; x++) {
                //uses java Color class to seperate RGB values from BufferedImage
                //int return value, could be done manually, but this is cleaner.
                Color col = new Color(bi.getRGB(x, y));
                //normalize RGB values
                double r = col.getRed() / 255.0;
                double g = col.getGreen() / 255.0;
                double b = col.getBlue() / 255.0;
                //put into pixel array
                pixels.add(new RGBColor(r, g, b));
            }
        }
    }

    /**
     * Utility function to read a single word from a text file.
     *
     * @param in
     * @return
     * @throws IOException
     */
    private String readWord(BufferedInputStream in) throws IOException {
        StringBuilder s = new StringBuilder();
        char c = (char) in.read();
        //ignore # comments to end of line
        while (c == '#') {
            //read until end of line
            while ("\r\n".indexOf(c) == -1) {
                c = (char) in.read();
//                s.append(c);
            }
            c = (char) in.read();
        }
        //read until end of line or word, and add character to string
        while (" \t\r\n".indexOf(c) == -1) {
            s.append(c);
            c = (char) in.read();
        }
        //return the string without any whitespace around it
        return s.toString().trim();
    }

    /**
     * Reads a PPM file, this method reads the header and calls helper
     * functions.
     *
     * @param f
     * @throws IOException
     */
    public void loadPPMFile(File f) throws IOException {
        //open file
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
        //read a text word on the file, should be P3 or P6
        String magic = readWord(in);
        //if the first word in the file isn't P6 or P3, bail because we can't load it
        if (!(magic.equals("P6") || magic.equals("P3"))) {
            throw new IOException("File is not PPM or is Corrupted");
        }
        //We are reading a binary file if the first word is P6
        boolean binary = magic.charAt(1) == '6';

        //regardless of binary status the width and height are store as ascii base 10 numbers
        int width = Integer.parseInt(readWord(in));
        int height = Integer.parseInt(readWord(in));
        hres = width;
        vres = height;

        //the next word should be a base 10 ascii number for the max number size
        //this is used to determine if we're a 1 byte or two byte binary file
        //and for normalizing the color to a 0-1 float value
        int maxColor = Integer.parseInt(readWord(in));
        //if we have more than a byte size color and we are binary we read 2 byte
        if (maxColor > 255 && binary) {
            read2Byte(in, width, height, maxColor);
            return;
        }
        //if we get here it's either one byte per channel binary or ascii
        if (binary) {
            readBinary(in, width, height, maxColor);
        } else {
            readAscii(in, width, height, maxColor);
        }
    }

    /**
     * loads 2 byte ppm data, most significant byte first
     *
     * @param in
     * @param width
     * @param height
     * @param maxColor
     * @throws IOException
     */
    private void read2Byte(BufferedInputStream in, int width, int height,
            int maxColor) throws IOException {
        //loop through image data
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                //shift first byte by 8 bits and or with second byte for each channel
                int r = (in.read() << 8);
                r |= in.read();
                int g = (in.read() << 8);
                g |= in.read();
                int b = (in.read() << 8);
                b |= in.read();

                //normalize color channels to 0-1
                double rd = (double) r / (double) maxColor;
                double gd = (double) g / (double) maxColor;
                double bd = (double) b / (double) maxColor;

                //add pixel
                pixels.add(new RGBColor(rd, gd, bd));
            }
        }
    }

    /**
     * reads a single byte binary file
     *
     * @param in
     * @param width
     * @param height
     * @param maxColor
     * @throws IOException
     */
    private void readBinary(BufferedInputStream in, int width, int height,
            int maxColor) throws IOException {
        //loop through all pixels
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                //read each channels byte
                int r = in.read();
                int g = in.read();
                int b = in.read();

                //normalize the channels
                double rd = (double) r / (double) maxColor;
                double gd = (double) g / (double) maxColor;
                double bd = (double) b / (double) maxColor;

                //add the pixel
                pixels.add(new RGBColor(rd, gd, bd));
            }
        }
    }

    /**
     * reads an ascii file.
     *
     * @param in
     * @param width
     * @param height
     * @param maxColor
     * @throws IOException
     */
    private void readAscii(BufferedInputStream in, int width, int height,
            int maxColor) throws IOException {
        //loop through pixels
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                //read each channel
                int r = Integer.parseInt(readWord(in));
                int g = Integer.parseInt(readWord(in));
                int b = Integer.parseInt(readWord(in));

                //normalize
                double rd = (double) r / (double) maxColor;
                double gd = (double) g / (double) maxColor;
                double bd = (double) b / (double) maxColor;

                //add pixel
                pixels.add(new RGBColor(rd, gd, bd));
            }
        }
    }

    /**
     * getter
     *
     * @return
     */
    public int getHres() {
        return hres;
    }

    /**
     * getter
     *
     * @return
     */
    public int getVres() {
        return vres;
    }

}
