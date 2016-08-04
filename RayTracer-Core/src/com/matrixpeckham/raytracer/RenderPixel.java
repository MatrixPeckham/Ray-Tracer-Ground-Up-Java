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
package com.matrixpeckham.raytracer;

import java.awt.Color;

/**
 * Class that holds a pixel that is ready to render to the image.
 *
 * @author William Matrix Peckham
 */
public class RenderPixel {

    /**
     * pixel x location
     */
    public final int x;

    /**
     * pixel y location
     */
    public final int y;

    /**
     * pixel integer rgba color
     */
    public final int color;

    /**
     * Constructor converts r,g,b from integers (0-255) to the integer color.
     * Also clamps values.
     *
     * @param x pixel x
     * @param y pixel y
     * @param r pixel red (0-255)
     * @param g pixel green (0-255)
     * @param b pixel blue (0-255)
     */
    public RenderPixel(int x, int y, int r, int g, int b) {
        this.x = x;
        this.y = y;
        if (r > 255) {
            r = 255;
        }
        if (g > 255) {
            g = 255;
        }
        if (b > 255) {
            b = 255;
        }
        if (r < 0) {
            r = 0;
        }
        if (g < 0) {
            g = 0;
        }
        if (b < 0) {
            b = 0;
        }
        Color c = new Color(r, g, b);
        color = c.getRGB();
    }
}
