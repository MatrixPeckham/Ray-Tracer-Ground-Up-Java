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
package com.matrixpeckham.raytracer.textures.procedural;

import com.matrixpeckham.raytracer.textures.Texture;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import java.util.logging.Logger;

/**
 * Wraps two colors from black... n &lt; 0 black-&gt color1 and for &gt;1 black
 * -&gt; color2
 *
 * @author William Matrix Peckham
 */
public class WrappedTwoColors implements Texture {

    /**
     * noise to use
     */
    private Noise noise = null;

    /**
     * first color
     */
    private final RGBColor color1 = new RGBColor();

    /**
     * second color
     */
    private final RGBColor color2 = new RGBColor();

    /**
     * min value to use for color ramping (should be 0-1)
     */
    private double minValue;

    /**
     * max value to use for color ramping (should be 0-1(
     */
    private double maxValue;

    /**
     * multiplies noise value before wrapping and normalization (should be
     * positive)
     */
    private double expansionNumber;

    /**
     * white to black
     */
    public WrappedTwoColors() {
        this(Utility.WHITE, Utility.BLACK);
    }

    /**
     * sets the colors
     *
     * @param col
     * @param col2
     */
    public WrappedTwoColors(RGBColor col, RGBColor col2) {
        this(col, col2, 0.0, 1.0);
    }

    /**
     * sets the two colors and sets the values
     *
     * @param col
     * @param col2
     * @param min
     * @param max
     */
    public WrappedTwoColors(RGBColor col, RGBColor col2, double min, double max) {
        this(col, col2, min, max, 2, new LinearNoise());
    }

    /**
     * sets all the values
     *
     * @param col
     * @param col2
     * @param min
     * @param max
     * @param num
     * @param n
     */
    public WrappedTwoColors(RGBColor col, RGBColor col2, double min, double max,
            double num, LatticeNoise n) {
        color1.setTo(col);
        color2.setTo(col2);
        minValue = min;
        maxValue = max;
        noise = n;
        expansionNumber = num;
    }

    /**
     * copy constructor
     *
     * @param t
     */
    public WrappedTwoColors(WrappedTwoColors t) {
        this.color1.setTo(t.color1);
        this.color2.setTo(t.color2);
        this.maxValue = t.maxValue;
        this.minValue = t.minValue;
        this.noise = t.noise.cloneNoise();
        this.expansionNumber = t.expansionNumber;
    }

    /**
     * white to black with specific noise
     *
     * @param noisePtr
     */
    public WrappedTwoColors(CubicNoise noisePtr) {
        this(Utility.WHITE, Utility.BLACK, 0, 1, 2, noisePtr);
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Texture cloneTexture() {
        return new WrappedTwoColors(this);
    }

    /**
     * gets the color
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor getColor(ShadeRec sr) {
        //expand noise from approx -1-1 -n-n
        double n = expansionNumber * noise.valueFBM(sr.localHitPosition);
        //wrap to range 0-1
        double value = n - Math.floor(n);
        //normalize wrapped value
        value = minValue + (maxValue - minValue) * value;
        if (n < 1) {
            return (color1.mul(value));
        } else {
            return color2.mul(value);
        }
    }

    /**
     * Sets the first color
     *
     * @param d
     * @param d0
     * @param d1
     */
    public void setColor1(double d, double d0, double d1) {
        color1.setTo(d, d0, d1);
    }

    /**
     * sets the color 2
     *
     * @param d
     * @param d0
     * @param d1
     */
    public void setColor2(double d, double d0, double d1) {
        color2.setTo(d, d0, d1);
    }

    /**
     * sets expansion number.
     *
     * @param d
     */
    public void setExpansionNumber(double d) {
        expansionNumber = d;
    }

    private static final Logger LOG
            = Logger.getLogger(WrappedTwoColors.class.getName());

}
