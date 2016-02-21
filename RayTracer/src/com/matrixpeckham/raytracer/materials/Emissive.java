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
 * Emissive material, for objects that are area lights, or things that should be
 * a color no matter what lights hit them.
 *
 * @author William Matrix Peckham
 */
public class Emissive extends Material {

    /**
     * radiance multiplier
     */
    private double ls = 1;

    /**
     * emissive color
     */
    private RGBColor ce = new RGBColor(1);

    /**
     * default constructor
     */
    public Emissive() {
    }

    /**
     * copy constructor
     *
     * @param e
     */
    public Emissive(Emissive e) {
        ls = e.ls;
        ce.setTo(e.ce);
    }

    /**
     * scales up the radiance of the light source
     *
     * @param ls
     */
    public void scaleRadiance(double ls) {
        this.ls = ls;
    }

    /**
     * setter
     *
     * @param r
     * @param g
     * @param b
     */
    public void setCe(double r, double g, double b) {
        ce.setTo(r, g, b);
    }

    /**
     * setter
     *
     * @param c
     */
    public void setCe(RGBColor c) {
        ce.setTo(c);
    }

    /**
     * setter gray
     *
     * @param c
     */
    public void setCe(double c) {
        ce.setTo(c);
    }

    /**
     * shade function simply returns the color multiplied by the radiance or
     * black on back face
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor shade(ShadeRec sr) {
        if (sr.normal.neg().dot(sr.ray.d) > 0) {
            return ce.mul(ls);
        } else {
            return Utility.BLACK;
        }
    }

    /**
     * Path shade returns color by radiance, only non-recursive implementation
     * of path shade.
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor pathShade(ShadeRec sr) {
        if (sr.normal.neg().dot(sr.ray.d) > 0) {
            return ce.mul(ls);
        } else {
            return Utility.BLACK;
        }
    }

    /**
     * returns the color multiplied by the radiance. ends recursion of global
     * shade
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor globalShade(ShadeRec sr) {
        //hack mentioned in book to stop global illumination 
        if (sr.depth == 1) {
            return Utility.BLACK;
        }
        if (sr.normal.neg().dot(sr.ray.d) > 0) {
            return ce.mul(ls);
        } else {
            return Utility.BLACK;
        }
    }

    @Override
    public RGBColor getLe(ShadeRec sr) {
        if (sr.normal.neg().dot(sr.ray.d) > 0) {
            return ce.mul(ls);
        } else {
            return Utility.BLACK;
        }
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Material clone() {
        return new Emissive(this);
    }

}
