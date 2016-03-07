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
 * Black to color noise texture
 *
 * @author William Matrix Peckham
 */
public class FBmTexture implements Texture {

    /**
     * noise
     */
    private LatticeNoise noise = null;

    /**
     * color
     */
    private final RGBColor color = new RGBColor();

    /**
     * min value for normalizing noise
     */
    private double minValue;

    /**
     * max value for normalizing noise
     */
    private double maxValue;

    /**
     * default constructor
     */
    public FBmTexture() {
        this(Utility.WHITE);
    }

    /**
     * specify color
     *
     * @param col
     */
    public FBmTexture(RGBColor col) {
        this(col, 0.0, 1.0);
    }

    /**
     * uses color and min/max
     *
     * @param col
     * @param min
     * @param max
     */
    public FBmTexture(RGBColor col, double min, double max) {
        this(col, min, max, new LinearNoise());
    }

    /**
     * white with specified noise
     *
     * @param n
     */
    public FBmTexture(LatticeNoise n) {
        this(Utility.WHITE, 0, 1, n);
    }

    /**
     * sets all the fields
     *
     * @param col
     * @param min
     * @param max
     * @param n
     */
    public FBmTexture(RGBColor col, double min, double max, LatticeNoise n) {
        color.setTo(col);
        minValue = min;
        maxValue = max;
        noise = n;
    }

    /**
     * copy constructor
     *
     * @param t
     */
    public FBmTexture(FBmTexture t) {
        this.color.setTo(t.color);
        this.maxValue = t.maxValue;
        this.minValue = t.minValue;
        this.noise = t.noise.cloneNoise();
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Texture cloneTexture() {
        return new FBmTexture(this);
    }

    /**
     * sample texture
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor getColor(ShadeRec sr) {
        //samples noise and normalizes it to multiply by the color.
        double value = noise.valueFBM(sr.localHitPosition);
        value = minValue + (maxValue - minValue) * value;
        return color.mul(value);
    }

    /**
     * setter
     *
     * @param color
     */
    public void setColor(RGBColor color) {
        this.color.setTo(color);
    }

    /**
     * setter
     *
     * @param r
     * @param g
     * @param b
     */
    public void setColor(double r, double g, double b) {
        this.color.setTo(r, g, b);
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
            = Logger.getLogger(FBmTexture.class.getName());

}
