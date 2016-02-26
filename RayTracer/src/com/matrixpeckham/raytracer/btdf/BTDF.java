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
package com.matrixpeckham.raytracer.btdf;

import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 * Class for Bidirectional Transmission Distribution Functions
 *
 * @author William Matrix Peckham
 */
public abstract class BTDF {

    /**
     * default constructor
     */
    public BTDF() {
    }

    /**
     * copy constructor
     *
     * @param v
     */
    public BTDF(BTDF v) {
    }

    /**
     * set to function
     *
     * @param o
     * @return
     */
    public BTDF setTo(BTDF o) {
        return this;
    }

    /**
     * clone function
     *
     * @return
     */
    public abstract BTDF clone();

    /**
     * Function for getting the color of this point in direction wo, as illuminated from wi. Not used for transparent
     * objects, most likely will return black.
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
     * Samples the object. returns the color, sets wi to the direction of light
     * transport.
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
     * Rho function. not used for transparency.
     *
     * @param sr
     * @param wo
     * @return
     */
    public RGBColor rho(ShadeRec sr, Vector3D wo) {
        return Utility.BLACK;
    }
}
