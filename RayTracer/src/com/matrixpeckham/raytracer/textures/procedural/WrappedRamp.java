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
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import java.util.logging.Logger;

/**
 * Uses noise value to sample a ramp texture.
 *
 * @author William Matrix Peckham
 */
public class WrappedRamp implements Texture {

    /**
     * noise
     */
    private LatticeNoise noise = null;

    /**
     * ramp image
     */
    private Image ramp = null;

    /**
     * min value
     */
    private double minValue = 0;

    /**
     * max value
     */
    private double maxValue = 1;

    /**
     * scaling parameter
     */
    private double expansionNumber = 1;

    /**
     * make empty ramp
     */
    public WrappedRamp() {
    }

    /**
     * make ramp with this noise
     *
     * @param noise
     */
    public WrappedRamp(LatticeNoise noise) {
        this.noise = noise.cloneNoise();
    }

    /**
     * an image initializing constructor
     *
     * @param col
     */
    public WrappedRamp(Image col) {
        this(col, 0.0, 1.0);
    }

    /**
     * constructor for image and normalization parameters
     *
     * @param col
     * @param min
     * @param max
     */
    public WrappedRamp(Image col, double min, double max) {
        this(col, min, max, 2, new LinearNoise());
    }

    /**
     * all field constructor
     *
     * @param col
     * @param min
     * @param max
     * @param num
     * @param n
     */
    public WrappedRamp(Image col, double min, double max, double num,
            LatticeNoise n) {
        ramp = col;
        minValue = min;
        maxValue = max;
        noise = n;
        expansionNumber = num;
    }

    /**
     * copy constructor.
     *
     * @param t
     */
    public WrappedRamp(WrappedRamp t) {
        this.ramp = t.ramp;
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
        return new WrappedRamp(this);
    }

    /**
     * returns color for this texture.
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor getColor(ShadeRec sr) {
        //get noise
        double value = expansionNumber * noise.valueFBM(sr.localHitPosition);
        //wrap
        value = value - Math.floor(value);
        //normalize
        value = minValue + (maxValue - minValue) * value;
        //sample ramp
        return ramp.getColor(0, (int) (value * (ramp.getHres() - 1)));
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

    /**
     * setter
     *
     * @param noise
     */
    public void setNoise(LatticeNoise noise) {
        this.noise = noise.cloneNoise();
    }

    /**
     * setter
     *
     * @param d
     */
    public void setWrapNumber(double d) {
        expansionNumber = d;
    }

    private static final Logger LOG
            = Logger.getLogger(WrappedRamp.class.getName());

}
