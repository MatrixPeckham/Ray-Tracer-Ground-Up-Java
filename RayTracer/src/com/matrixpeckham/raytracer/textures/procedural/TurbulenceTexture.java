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
 * Turbulence noise texture for black to color.
 *
 * @author William Matrix Peckham
 */
public class TurbulenceTexture implements Texture {

    /**
     * noise
     */
    private LatticeNoise noise = null;

    /**
     * colors
     */
    private final RGBColor color = new RGBColor();

    /**
     * min value for normalization
     */
    private double minValue;

    /**
     * max value for normalization
     */
    private double maxValue;

    /**
     * default turbulence
     */
    public TurbulenceTexture() {
        this(Utility.WHITE);
    }

    /**
     * this constructor sets color
     *
     * @param col
     */
    public TurbulenceTexture(RGBColor col) {
        this(col, 0.0, 1.0);
    }

    /**
     * construct with specified noise.
     *
     * @param n
     */
    public TurbulenceTexture(LatticeNoise n) {
        this(Utility.WHITE, 0, 1, n);
    }

    /**
     * default noise with other values specified
     *
     * @param col
     * @param min
     * @param max
     */
    public TurbulenceTexture(RGBColor col, double min, double max) {
        this(col, min, max, new LinearNoise());
    }

    /**
     * sets all the values
     *
     * @param col
     * @param min
     * @param max
     * @param n
     */
    public TurbulenceTexture(RGBColor col, double min, double max,
            LatticeNoise n) {
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
    public TurbulenceTexture(TurbulenceTexture t) {
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
        return new TurbulenceTexture(this);
    }

    /**
     * Sample texture
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor getColor(ShadeRec sr) {
        //samble turbulence noise
        double value = noise.valueTurbulence(sr.localHitPosition);
        //normalize
        value = minValue + (maxValue - minValue) * value;
        //calculate final color
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
            = Logger.getLogger(TurbulenceTexture.class.getName());

}
