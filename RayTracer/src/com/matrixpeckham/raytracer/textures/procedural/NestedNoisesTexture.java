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

import com.matrixpeckham.raytracer.textures.ConstantColor;
import com.matrixpeckham.raytracer.textures.Texture;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import java.util.logging.Logger;

/**
 * This texture is a noise texture that differs to another texture as the second
 * COlor.
 *
 * @author William Matrix Peckham
 */
public class NestedNoisesTexture implements Texture {

    /**
     * noise to use
     */
    private Noise noise = null;

    /**
     * black to this color
     */
    private final RGBColor color1 = new RGBColor();

    /**
     * Secondary color, this is another texture
     */
    private Texture color2 = null;

    /**
     * normalization min
     */
    private double minValue;

    /**
     * normalization max
     */
    private double maxValue;

    /**
     * expansion number
     */
    private double expansionNumber;

    /**
     * default constructor
     */
    public NestedNoisesTexture() {
        this(Utility.WHITE, new ConstantColor(Utility.BLACK));
    }

    /**
     * constructor that sets the separate colors
     *
     * @param col
     * @param col2
     */
    public NestedNoisesTexture(RGBColor col, Texture col2) {
        this(col, col2, 0.0, 1.0);
    }

    /**
     *
     * @param col
     * @param col2
     * @param min
     * @param max
     */
    public NestedNoisesTexture(RGBColor col, Texture col2, double min,
            double max) {
        this(col, col2, min, max, 2, new LinearNoise());
    }

    /**
     * sets all the fields
     *
     * @param col
     * @param col2
     * @param min
     * @param max
     * @param num
     * @param n
     */
    public NestedNoisesTexture(RGBColor col, Texture col2, double min,
            double max, double num, LatticeNoise n) {
        color1.setTo(col);
        color2 = col2.cloneTexture();
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
    public NestedNoisesTexture(NestedNoisesTexture t) {
        this.color1.setTo(t.color1);
        if (t.color2 != null) {
            this.color2 = t.color2.cloneTexture();
        }
        this.maxValue = t.maxValue;
        this.minValue = t.minValue;
        if (t.noise != null) {
            this.noise = t.noise.cloneNoise();
        }
        this.expansionNumber = t.expansionNumber;
    }

    /**
     * initializes the noise
     *
     * @param noisePtr
     */
    public NestedNoisesTexture(CubicNoise noisePtr) {
        this(Utility.WHITE, new ConstantColor(Utility.BLACK), 0, 1, 2, noisePtr);
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Texture cloneTexture() {
        return new NestedNoisesTexture(this);
    }

    /**
     * sample the texture
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor getColor(ShadeRec sr) {
        //sample noise
        double n = expansionNumber * noise.valueFBM(sr.localHitPosition);
        //wrap
        double value = n - Math.floor(n);
        //normalize
        value = minValue + (maxValue - minValue) * value;
        //return color or defer to inner texture
        if (n < 1) {
            return (color1.mul(value));
        } else {
            return color2.getColor(sr).mul(value);
        }
    }

    /**
     * setter
     *
     * @param d
     * @param d0
     * @param d1
     */
    public void setColor(double d, double d0, double d1) {
        color1.setTo(d, d0, d1);
    }

    /**
     * setter
     *
     * @param c2
     */
    public void setTexture(Texture c2) {
        color2 = c2.cloneTexture();
    }

    /**
     * setter
     *
     * @param d
     */
    public void setExpansionNumber(double d) {
        expansionNumber = d;
    }

    /**
     * setter
     *
     * @param d
     */
    public void setWrapFactor(double d) {
        expansionNumber = d;
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
     * setter
     *
     * @param maxValue
     */
    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    private static final Logger LOG
            = Logger.getLogger(NestedNoisesTexture.class.getName());

}
