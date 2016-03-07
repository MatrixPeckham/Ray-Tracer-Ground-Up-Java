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
package com.matrixpeckham.raytracer.materials;

import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;

/**
 * Material is the class responsible for determining the way an object interacts
 * with light. This is the abstract base class for all materials. It contains
 * only one common field, getter and setter. This field is a shadow
 * boolean.which determines if the material can have a shadow cast on it. Also
 * has default implementation of Le, the emitted radiance of this material to
 * black.
 *
 * @author William Matrix Peckham
 */
public abstract class Material {

    /**
     * can a shadow be cast on this material
     */
    protected boolean shadow = true;

    /**
     * default constructor, by default materials can have shadows cast on them.
     */
    public Material() {
    }

    /**
     * copy constructor
     *
     * @param mat
     */
    public Material(Material mat) {
        shadow = mat.shadow;
    }

    /**
     * A clone method, should copy all information necessary to use the
     * material.
     *
     * @return
     */
    public abstract Material cloneMaterial();

    /**
     * Shade function. This is the main shading method that most of the tracers
     * call. It will be within this method that the materials will make a
     * recursive call to trace child rays, and will mix lighting.
     *
     * @param sr
     * @return
     */
    public abstract RGBColor shade(ShadeRec sr);

    /**
     * Path shade function. Called by path tracer, this shade function should
     * generate a recursive ray to get lighting unless it hits a light. hitting
     * a light or bailing out for depth should be the only end to recursion with
     * this tracer.
     *
     * @param sr
     * @return
     */
    public abstract RGBColor pathShade(ShadeRec sr);

    /**
     * Global Shade function. Called by global tracer, this shade function is
     * the same as path shade, except that at the first (0) depth it will
     * compute regular direct lighting as well as adding recursive shading. this
     * typically gives better image quality than path shading because path
     * shading tends to be very dark and requires very large sample sizes
     *
     * @param sr
     * @return
     */
    public abstract RGBColor globalShade(ShadeRec sr);

    /**
     * gets the emitted light from this material. defaults to black because most
     * materials don't produce their own light.
     *
     * @param sr
     * @return
     */
    public RGBColor getLe(ShadeRec sr) {
        return Utility.BLACK;
    }

    /**
     * setter
     *
     * @param s
     */
    public void setShadows(boolean s) {
        shadow = s;
    }

    /**
     * getter
     *
     * @return
     */
    public boolean getShadows() {
        return shadow;
    }
}
