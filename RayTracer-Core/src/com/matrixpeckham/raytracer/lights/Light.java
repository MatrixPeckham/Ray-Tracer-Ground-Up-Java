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
package com.matrixpeckham.raytracer.lights;

import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 * Light class. This class is for light sources in the scene.
 *
 * @author William Matrix Peckham
 */
public abstract class Light {

    /**
     * boolean flag for whether or not this light will cast shadows.
     */
    protected boolean shadows = true;

    /**
     * default constructor
     */
    public Light() {
    }

    /**
     * copy constructor
     *
     * @param ls
     */
    public Light(Light ls) {
        this.shadows = ls.shadows;
    }

    /**
     * clone method
     *
     * @return
     */
    public abstract Light cloneLight();

    /**
     * Gets the direction from the hit point to this light. For area lights this
     * will choose a sample point on the light and return the direction to that
     * point.
     *
     * @param sr
     * @return
     */
    public abstract Vector3D getDirection(ShadeRec sr);

    /**
     * Gets the light that this light is producing.
     *
     * @param sr
     * @return
     */
    public RGBColor L(ShadeRec sr) {
        return Utility.BLACK;
    }

    /**
     * part of the geometric term.
     *
     * @param sr
     * @return
     */
    public double G(ShadeRec sr) {
        return 1;
    }

    /**
     * probability density function.
     *
     * @param sr
     * @return
     */
    public double pdf(ShadeRec sr) {
        return 1;
    }

    /**
     * setters
     *
     * @param b
     */
    public void setShadows(boolean b) {
        shadows = b;
    }

    /**
     * does this light cast shadows
     *
     * @return
     */
    public boolean castsShadows() {
        return shadows;
    }

    /**
     * Whether or not the hit point is in shadow following the ray.
     *
     * @param shadowRay
     * @param sr
     * @return
     */
    public abstract boolean inShadow(Ray shadowRay, ShadeRec sr);
}
