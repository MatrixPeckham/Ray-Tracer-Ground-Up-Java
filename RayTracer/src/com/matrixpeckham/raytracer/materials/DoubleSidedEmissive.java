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
 * Class for two sided emissive materials. created for use in the 28.46. That
 * image didn't render properly because the emissive material was considering
 * the interior or the sphere the back of the emissive material.
 *
 * @author William Matrix Peckham
 */
public class DoubleSidedEmissive extends Material {

    /**
     * radiance scale
     */
    private double ls = 1;

    /**
     * color
     */
    private RGBColor ce = new RGBColor(1);

    /**
     * default constructor
     */
    public DoubleSidedEmissive() {
    }

    /**
     * copy constructor
     *
     * @param e
     */
    public DoubleSidedEmissive(DoubleSidedEmissive e) {
        ls = e.ls;
        ce.setTo(e.ce);
    }

    /**
     * sets ls
     *
     * @param ls
     */
    public void scaleRadiance(double ls) {
        this.ls = ls;
    }

    /**
     * set color
     *
     * @param r
     * @param g
     * @param b
     */
    public void setCe(double r, double g, double b) {
        ce.setTo(r, g, b);
    }

    /**
     * set color
     *
     * @param c
     */
    public void setCe(RGBColor c) {
        ce.setTo(c);
    }

    /**
     * sets color gray
     *
     * @param c
     */
    public void setCe(double c) {
        ce.setTo(c);
    }

    /**
     * shade function
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor shade(ShadeRec sr) {
        //multiplies color by radiance
        return ce.mul(ls);
    }

    /**
     * path shade, same as shade
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor pathShade(ShadeRec sr) {
        return ce.mul(ls);
    }

    /**
     * global shade, same as shade but with depth hack discussed in book
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor globalShade(ShadeRec sr) {
        if (sr.depth == 1) {
            return Utility.BLACK;
        }
        return ce.mul(ls);
    }

    /**
     * illumination term, color multiplied by radiance
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor getLe(ShadeRec sr) {
        return ce.mul(ls);
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Material clone() {
        return new DoubleSidedEmissive(this);
    }

}
