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

/**
 *
 * @author William Matrix Peckham
 */
public class RGBColor {

    public float r;
    public float g;
    public float b;

    // default ructor
    public RGBColor() {
        this(0);
    }

    // ructor
    public RGBColor(float c) {
        this(c, c, c);
    }
    public void setTo(float x, float y, float z){
        this.r=x;
        this.g=y;
        this.b=z;
    }

    // ructor
    public RGBColor(float _r, float _g, float _b) {
        r = _r;
        g = _g;
        b = _b;
    }

    // copy ructor
    public RGBColor(
            RGBColor c) {
        r = c.r;
        g = c.g;
        b = c.b;
    }

    // assignment operator
    public RGBColor setTo(RGBColor rhs) {
        r = rhs.r;
        g = rhs.g;
        b = rhs.b;
        return this;
    }

    // addition
    public RGBColor add(RGBColor c) {
        return new RGBColor(r + c.r, g + c.g, b + c.b);
    }

    // compound addition
    public RGBColor addLocal(RGBColor c) {
        r += c.r;
        g += c.g;
        b += c.b;
        return this;
    }

    // multiplication by a float on the right
    public RGBColor mul(float a) {
        return new RGBColor(a * r, a * g, a * b);
    }

    // compound multiplication by a float on the right
    public RGBColor mulLocal(float a) {
        r *= a;
        g *= a;
        b *= a;
        return this;
    }

    // division by a float
    public RGBColor div(float a) {
        return new RGBColor(r / a, g / a, b / a);
    }

    // compound division by a float
    public RGBColor divLocal(float a) {
        r /= a;
        g /= a;
        b /= a;
        return this;
    }

    // component-wise multiplication
    public RGBColor mul(RGBColor c) {
        return new RGBColor(r*c.r, g*c.g, b*c.b);
    }

    // are two RGBColours the same?
    @Override
    public boolean equals( Object o ) {
        if(! (o instanceof RGBColor)) return false;
        RGBColor c = (RGBColor)o;
        return r==c.r&&g==c.g&&b==c.b;
    }				

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Float.floatToIntBits(this.r);
        hash = 59 * hash + Float.floatToIntBits(this.g);
        hash = 59 * hash + Float.floatToIntBits(this.b);
        return hash;
    }

    // raise components to a power
    public RGBColor powc(float p){
        return new RGBColor((float)Math.pow(r, p),(float)Math.pow(g, p),(float)Math.pow(b, p));
    }

    // the average of the components
    public float average(){
        return 0.33333333333333333333333333333333f *(r+g+b);
    }

}
