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
package com.matrixpeckham.raytracer.brdfs;

import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 * Bidirectional reflective distribution function class. Class represents the
 * materials reflectance for varying directions. Abstract base class.
 *
 * @author William Matrix Peckham
 */
public abstract class BRDF {

    /**
     * default constructor
     */
    public BRDF() {
    }

    /**
     * copy constructor
     *
     * @param brdf
     */
    public BRDF(BRDF brdf) {
    }

    /**
     * clone function, needs to return a BRDF that is the same as the class we
     * call it on.
     *
     * @return
     */
    public abstract BRDF clone();

    /**
     * F function, called from functions that don't require a recursive raycast.
     * Should return the color the BRDF reflects toward wo when illuminated from
     * wi. used for direct lighting.
     *
     * @param sr
     * @param wo
     * @param wi
     * @return
     */
    public RGBColor f(ShadeRec sr, Vector3D wo, Vector3D wi) {
        return Utility.BLACK;
    }

    /**
     * Sample the distribution, returns the color and fills wi with a direction
     * to recurse with.
     *
     * @param sr
     * @param wo
     * @param wi
     * @return
     */
    public RGBColor sampleF(ShadeRec sr, Vector3D wo, Vector3D wi) {
        return Utility.BLACK;
    }

    /**
     * Sample the distribution, returns the color and fills wi with a direction
     * to recurse with, and a pdf.
     *
     * @param sr
     * @param wo
     * @param wi
     * @param pdf
     * @return
     */
    public RGBColor sampleF(ShadeRec sr, Vector3D wo, Vector3D wi, DoubleRef pdf) {
        return Utility.BLACK;
    }

    /**
     * Gets the color of the point in full lighting. used for ambient lighting
     *
     * @param sr
     * @param wo
     * @return
     */
    public RGBColor rho(ShadeRec sr, Vector3D wo) {
        return Utility.BLACK;
    }
}
