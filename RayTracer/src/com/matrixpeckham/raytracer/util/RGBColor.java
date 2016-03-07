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
package com.matrixpeckham.raytracer.util;

import java.util.logging.Logger;

/**
 * Holds double values for RGB colors
 *
 * @author William Matrix Peckham
 */
public class RGBColor {

    /**
     * red
     */
    public double r;

    /**
     * green
     */
    public double g;

    /**
     * blue
     */
    public double b;

    /**
     * string rep of this color, useful for debugging
     *
     * @return
     */
    @Override
    public String toString() {
        return "(" + r + "," + g + "," + b + ")RGBColor";
    }

    /**
     * default constructor black.
     */
    public RGBColor() {
        this(0);
    }

    /**
     * constructor gray of c intensity
     *
     * @param c
     */
    public RGBColor(double c) {
        this(c, c, c);
    }

    /**
     * sets the components
     *
     * @param x
     * @param y
     * @param z
     */
    public void setTo(double x, double y, double z) {
        this.r = x;
        this.g = y;
        this.b = z;
    }

    /**
     * initializes components
     *
     * @param _r
     * @param _g
     * @param _b
     */
    public RGBColor(double _r, double _g, double _b) {
        r = _r;
        g = _g;
        b = _b;
    }

    /**
     * copy constructor
     *
     * @param c
     */
    public RGBColor(
            RGBColor c) {
        r = c.r;
        g = c.g;
        b = c.b;
    }

    /**
     * java equals operator
     *
     * @param rhs
     * @return
     */
    public RGBColor setTo(RGBColor rhs) {
        r = rhs.r;
        g = rhs.g;
        b = rhs.b;
        return this;
    }

    /**
     * adds colors and returns new
     *
     * @param c
     * @return
     */
    public RGBColor add(RGBColor c) {
        return new RGBColor(r + c.r, g + c.g, b + c.b);
    }

    /**
     * in place addition.
     *
     * @param c
     * @return
     */
    public RGBColor addLocal(RGBColor c) {
        r += c.r;
        g += c.g;
        b += c.b;
        return this;
    }

    /**
     * scale color
     *
     * @param a
     * @return
     */
    public RGBColor mul(double a) {
        return new RGBColor(a * r, a * g, a * b);
    }

    /**
     * scales this color in place
     *
     * @param a
     * @return
     */
    public RGBColor mulLocal(double a) {
        r *= a;
        g *= a;
        b *= a;
        return this;
    }

    /**
     * divide color
     *
     * @param a
     * @return
     */
    public RGBColor div(double a) {
        return new RGBColor(r / a, g / a, b / a);
    }

    /**
     * component division by a double
     *
     * @param a
     * @return
     */
    public RGBColor divLocal(double a) {
        r /= a;
        g /= a;
        b /= a;
        return this;
    }

    /**
     * component wise multiplication
     *
     * @param c
     * @return
     */
    public RGBColor mul(RGBColor c) {
        return new RGBColor(r * c.r, g * c.g, b * c.b);
    }

    /**
     * equals override
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RGBColor)) {
            return false;
        }
        RGBColor c = (RGBColor) o;
        final float eps = 0.0001f;
        if (Math.abs(r - c.r) > eps) {
            return false;
        }
        if (Math.abs(g - c.g) > eps) {
            return false;
        }
        return !(Math.abs(b - c.b) > eps);
    }

    /**
     * hashcode override, for equals contract
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash
                = 83 * hash + (int) (Double.doubleToLongBits(this.r) ^ (Double.
                doubleToLongBits(this.r) >>> 32));
        hash
                = 83 * hash + (int) (Double.doubleToLongBits(this.g) ^ (Double.
                doubleToLongBits(this.g) >>> 32));
        hash
                = 83 * hash + (int) (Double.doubleToLongBits(this.b) ^ (Double.
                doubleToLongBits(this.b) >>> 32));
        return hash;
    }

    /**
     * raises components to power
     *
     * @param p
     * @return
     */
    public RGBColor powc(double p) {
        return new RGBColor(Math.pow(r, p), Math.pow(g, p), Math.pow(b, p));
    }

    /**
     * averages components
     *
     * @return
     */
    public double average() {
        return 0.33333333333333333333333333333333d * (r + g + b);
    }

    /**
     * sets color to gray of d intensity.
     *
     * @param d
     */
    public void setTo(double d) {
        setTo(d, d, d);
    }

    private static final Logger LOG = Logger.getLogger(RGBColor.class.getName());

}
