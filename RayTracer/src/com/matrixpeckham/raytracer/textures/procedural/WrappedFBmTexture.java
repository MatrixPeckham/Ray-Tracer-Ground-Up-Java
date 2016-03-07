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
 * Wrapped noise from black to a color.
 *
 * @author William Matrix Peckham
 */
public class WrappedFBmTexture implements Texture {

    /**
     * noise
     */
    private LatticeNoise noise = null;

    /**
     *
     */
    private final RGBColor color = new RGBColor();

    /**
     * min value for normalizing
     */
    private double minValue;

    /**
     * max value for normalizing
     */
    private double maxValue;

    /**
     * multiplicative factor for number of wraps
     */
    private double expansionNumber;

    /**
     * default constructor black to white
     */
    public WrappedFBmTexture() {
        this(Utility.WHITE);
    }

    /**
     * initializes with specified noise
     *
     * @param noise
     */
    public WrappedFBmTexture(LatticeNoise noise) {
        this(Utility.WHITE, 0, 1, 2, noise);
    }

    /**
     * initializes with specified color
     *
     * @param col
     */
    public WrappedFBmTexture(RGBColor col) {
        this(col, 0.0, 1.0);
    }

    /**
     * initialize values
     *
     * @param col
     * @param min
     * @param max
     */
    public WrappedFBmTexture(RGBColor col, double min, double max) {
        this(col, min, max, 2, new LinearNoise());
    }

    /**
     * initialize all fields
     *
     * @param col
     * @param min
     * @param max
     * @param num
     * @param n
     */
    public WrappedFBmTexture(RGBColor col, double min, double max, double num,
            LatticeNoise n) {
        color.setTo(col);
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
    public WrappedFBmTexture(WrappedFBmTexture t) {
        this.color.setTo(t.color);
        this.maxValue = t.maxValue;
        this.minValue = t.minValue;
        this.noise = t.noise.cloneNoise();
        this.expansionNumber = t.expansionNumber;
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Texture cloneTexture() {
        return new WrappedFBmTexture(this);
    }

    /**
     * sample texture
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor getColor(ShadeRec sr) {
        //sample and expand noise
        double value = expansionNumber * noise.valueFBM(sr.localHitPosition);
        //wrap
        value = value - Math.floor(value);
        //normalize
        value = minValue + (maxValue - minValue) * value;
        return color.mul(value);
    }

    /**
     * setter
     *
     * @param d
     * @param d0
     * @param d1
     */
    public void setColor(double d, double d0, double d1) {
        color.setTo(d, d0, d1);
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
            = Logger.getLogger(WrappedFBmTexture.class.getName());

}
