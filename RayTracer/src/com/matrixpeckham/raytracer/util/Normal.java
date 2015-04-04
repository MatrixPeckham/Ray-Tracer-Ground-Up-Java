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
 * @author William Matrix Peckham
 */
public class Normal {
    public double x;
    public double y;
    public double z;

    // default ructor
    public Normal() {

    }

    // ructor
    public Normal(double a) {

    }

    // ructor
    public Normal(double _x, double _y, double _z) {

    }

    // copy ructor
    public Normal(
            Normal n) {

    }

    // ructs a normal from vector
    public Normal(
            Vector3D v) {

    }

    // assignment operator
    public Normal setTo(
            Normal rhs) {

    }

    // assignment of a vector to a normal
    public Normal setTo(
            Vector3D rhs) {

    }

    // assignment of a point to a normal
    public Normal setTo(
            Point3D rhs) {

    }

    // unary minus
    public Normal neg() {

    }

    // addition
    public Normal add(Normal n) {

    }

    // compound addition
    public Normal addLocal(Normal n) {

    }

    // dot product with a vector on the right
    public double dot(Vector3D v) {

    }

    // multiplication by a double on the right
    public Normal mul(double a) {

    }

    // convert normal to a unit normal
    public void normalize() {

    }

}
