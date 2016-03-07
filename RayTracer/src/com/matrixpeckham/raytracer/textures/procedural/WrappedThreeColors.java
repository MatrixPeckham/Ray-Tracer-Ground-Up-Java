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
 * Wraps two colors from black... n &lt; 0 black-&gt color1 and for &gt;thresh1
 * black -&gt; color2 and for &gtthresh2 black -&gt; color3
 *
 * @author William Matrix Peckham
 */
public class WrappedThreeColors implements Texture {

    /**
     * noise to use
     */
    private LatticeNoise noise = null;

    /**
     * first color
     */
    private final RGBColor color1 = new RGBColor();

    /**
     * second color
     */
    private final RGBColor color2 = new RGBColor();

    /**
     * third color
     */
    private final RGBColor color3 = new RGBColor();

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
     * first threshold
     */
    private double thresh1 = 1.35;

    /**
     * second threshold
     */
    private double thresh2 = 2;

    /**
     * white to black
     */
    public WrappedThreeColors() {
        this(Utility.WHITE, Utility.BLACK);
    }

    /**
     * col to col2
     *
     * @param col
     * @param col2
     */
    public WrappedThreeColors(RGBColor col, RGBColor col2) {
        this(col, col2, 0.0, 1.0);
    }

    /**
     * sets values
     *
     * @param col
     * @param col2
     * @param min
     * @param max
     */
    public WrappedThreeColors(RGBColor col, RGBColor col2, double min,
            double max) {
        this(col, col2, min, max, 2, new LinearNoise());
    }

    /**
     * sets values
     *
     * @param col
     * @param col2
     * @param min
     * @param max
     * @param num
     * @param n
     */
    public WrappedThreeColors(RGBColor col, RGBColor col2, double min,
            double max, double num, LatticeNoise n) {
        color1.setTo(col);
        color2.setTo(col2);
        minValue = min;
        maxValue = max;
        noise = n;
        expansionNumber = num;
    }

    /**
     * copy constuctor
     *
     * @param t
     */
    public WrappedThreeColors(WrappedThreeColors t) {
        this.color1.setTo(t.color1);
        this.color2.setTo(t.color2);
        this.color3.setTo(t.color3);
        this.maxValue = t.maxValue;
        this.minValue = t.minValue;
        this.noise = t.noise.cloneNoise();
        this.expansionNumber = t.expansionNumber;
    }

    /**
     * initializes noise
     *
     * @param noisePtr
     */
    public WrappedThreeColors(CubicNoise noisePtr) {
        this(Utility.WHITE, Utility.BLACK, 0, 1, 2, noisePtr);
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Texture cloneTexture() {
        return new WrappedThreeColors(this);
    }

    /**
     * getter
     *
     * @return
     */
    public LatticeNoise getNoise() {
        return noise;
    }

    /**
     * setter
     *
     * @param noise
     */
    public void setNoise(LatticeNoise noise) {
        this.noise = noise;
    }

    /**
     * getter
     *
     * @return
     */
    public double getMinValue() {
        return minValue;
    }

    /**
     * setter
     *
     * @param minValue
     */
    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    /**
     * getter
     *
     * @return
     */
    public double getMaxValue() {
        return maxValue;
    }

    /**
     * setter
     *
     * @param maxValue
     */
    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * getter
     *
     * @return
     */
    public double getThresh1() {
        return thresh1;
    }

    /**
     * setter
     *
     * @param thresh1
     */
    public void setThresh1(double thresh1) {
        this.thresh1 = thresh1;
    }

    /**
     * getter
     *
     * @return
     */
    public double getThresh2() {
        return thresh2;
    }

    /**
     * setter
     *
     * @param thresh2
     */
    public void setThresh2(double thresh2) {
        this.thresh2 = thresh2;
    }

    /**
     * gets color from texture.
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor getColor(ShadeRec sr) {
        //expand noise number
        double n = expansionNumber * noise.valueFBM(sr.localHitPosition);
        //wrap
        double value = n - Math.floor(n);
        //normalize
        value = minValue + (maxValue - minValue) * value;
        if (n < thresh1) {
            return color1.mul(value);
        } else if (n < thresh2) {
            return color2.mul(value);
        } else {
            return color3.mul(value);
        }
    }

    /**
     * setter
     *
     * @param d
     * @param d0
     * @param d1
     */
    public void setColor1(double d, double d0, double d1) {
        color1.setTo(d, d0, d1);
    }

    /**
     * setter
     *
     * @param d
     * @param d0
     * @param d1
     */
    public void setColor2(double d, double d0, double d1) {
        color2.setTo(d, d0, d1);
    }

    /**
     * setter
     *
     * @param d
     * @param d0
     * @param d1
     */
    public void setColor3(double d, double d0, double d1) {
        color3.setTo(d, d0, d1);
    }

    /**
     * setter
     *
     * @param d
     */
    public void setExpansionNumber(double d) {
        expansionNumber = d;
    }

    private static final Logger LOG
            = Logger.getLogger(WrappedThreeColors.class.getName());

}
