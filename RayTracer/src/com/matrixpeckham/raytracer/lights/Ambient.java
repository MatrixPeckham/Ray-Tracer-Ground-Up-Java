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
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 * Ambient light, simplest light, no shadows. approximates background light
 *
 * @author William Matrix Peckham
 */
public class Ambient extends Light {

    /**
     * radiance
     */
    private double ls;

    /**
     * color
     */
    private RGBColor color;

    /**
     * default constructor
     */
    public Ambient() {
        super();
        ls = 1;
        color = new RGBColor(1);
    }

    /**
     * copy constructor
     *
     * @param a
     */
    public Ambient(Ambient a) {
        super(a);
        ls = a.ls;
        color.setTo(a.color);
    }

    /**
     * sets the radiance
     *
     * @param b
     */
    public void scaleRadiance(double b) {
        ls = b;
    }

    /**
     * sets the color gray
     *
     * @param c
     */
    public void setColor(double c) {
        color.setTo(c, c, c);
    }

    /**
     * sets the color
     *
     * @param c
     */
    public void setColor(RGBColor c) {
        color.setTo(c);
    }

    /**
     * sets the color
     *
     * @param r
     * @param g
     * @param b
     */
    public void setColor(double r, double g, double b) {
        color.r = r;
        color.g = g;
        color.b = b;
    }

    @Override
    public Light clone() {
        return new Ambient(this);
    }

    /**
     * get direction, there is no direction for an ambient light
     *
     * @param sr
     * @return
     */
    @Override
    public Vector3D getDirection(ShadeRec sr) {
        return new Vector3D(0);
    }

    /**
     * returns color multiplied by radiance
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor L(ShadeRec sr) {
        return color.mul(ls);
    }

    /**
     * ambient lights don't cast shadows, this returns false.
     *
     * @param shadowRay
     * @param sr
     * @return
     */
    @Override
    public boolean inShadow(Ray shadowRay, ShadeRec sr) {
        return false;
    }

}
