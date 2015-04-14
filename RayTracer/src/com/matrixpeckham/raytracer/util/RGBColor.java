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

    public double r;
    public double g;
    public double b;
    @Override
    public String toString(){
        return "("+r+","+g+","+b+")RGBColor";
    }

    // default ructor
    public RGBColor() {
        this(0);
    }

    // ructor
    public RGBColor(double c) {
        this(c, c, c);
    }
    public void setTo(double x, double y, double z){
        this.r=x;
        this.g=y;
        this.b=z;
    }

    // ructor
    public RGBColor(double _r, double _g, double _b) {
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

    // multiplication by a double on the right
    public RGBColor mul(double a) {
        return new RGBColor(a * r, a * g, a * b);
    }

    // compound multiplication by a double on the right
    public RGBColor mulLocal(double a) {
        r *= a;
        g *= a;
        b *= a;
        return this;
    }

    // division by a double
    public RGBColor div(double a) {
        return new RGBColor(r / a, g / a, b / a);
    }

    // compound division by a double
    public RGBColor divLocal(double a) {
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
        hash
                = 83 * hash +
                (int) (Double.doubleToLongBits(this.r) ^
                (Double.doubleToLongBits(this.r) >>> 32));
        hash
                = 83 * hash +
                (int) (Double.doubleToLongBits(this.g) ^
                (Double.doubleToLongBits(this.g) >>> 32));
        hash
                = 83 * hash +
                (int) (Double.doubleToLongBits(this.b) ^
                (Double.doubleToLongBits(this.b) >>> 32));
        return hash;
    }

    

    // raise components to a power
    public RGBColor powc(double p){
        return new RGBColor(Math.pow(r, p), Math.pow(g, p), Math.pow(b, p));
    }

    // the average of the components
    public double average(){
        return 0.33333333333333333333333333333333f *(r+g+b);
    }

}
