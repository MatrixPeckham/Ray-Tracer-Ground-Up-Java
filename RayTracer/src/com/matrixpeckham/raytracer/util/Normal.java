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
    @Override
    public String toString(){
        return "("+x+","+y+","+z+")Normal";
    }

    // default ructor
    public Normal() {
        this(0);
    }

    // ructor
    public Normal(double a) {
        this(a, a, a);
    }
    public void setTo(double x, double y, double z){
        this.x=x;
        this.y=y;
        this.z=z;
    }

    // ructor
    public Normal(double _x, double _y, double _z) {
        x = _x;
        y = _y;
        z = _z;
    }

    // copy ructor
    public Normal(Normal n) {
        this(n.x, n.y, n.z);
    }

    // ructs a normal from vector
    public Normal(Vector3D v) {
        this(v.x, v.y, v.z);
    }

    // assignment operator
    public Normal setTo(Normal rhs) {
        x = rhs.x;
        y = rhs.y;
        z = rhs.z;
        return this;
    }

    // assignment of a vector to a normal
    public Normal setTo(Vector3D rhs) {
        x = rhs.x;
        y = rhs.y;
        z = rhs.z;
        return this;
    }

    // assignment of a point to a normal
    public Normal setTo(
            Point3D rhs) {
        x = rhs.x;
        y = rhs.y;
        z = rhs.z;
        return this;
    }

    // unary minus
    public Normal neg() {
        return new Normal(-x, -y, -z);
    }

    // addition
    public Normal add(Normal n) {
        return new Normal(x + n.x, y + n.y, z + n.z);
    }

    // compound addition
    public Normal addLocal(Normal n) {
        x += n.x;
        y += n.y;
        z += n.z;
        return this;
    }

    // dot product with a vector on the right
    public double dot(Vector3D v) {
        return x * v.x + y * v.y + z * v.z;
    }

    // multiplication by a double on the right
    public Vector3D mul(double a) {
        return new Vector3D(x * a, y * a, z * a);
    }

    // convert normal to a unit normal
    public void normalize() {
        double len = Math.sqrt(x * x + y * y + z * z);
        x /= len;
        y /= len;
        z /= len;
    }

    public static Normal mul(double a, Normal n) {
        return new Normal(a * n.x, a * n.y, a * n.z);
    }

    public static Vector3D add(Vector3D v, Normal n) {
        return new Vector3D(v.x + n.x, v.y + n.y, v.z + n.z);
    }

    public static Vector3D sub(Vector3D v, Normal n) {
        return new Vector3D(v.x - n.x, v.y - n.y, v.z - n.z);
    }

    public static double dot(Vector3D v, Normal n) {
        return v.x * n.x + v.y * n.y + v.z * n.z;
    }

    public static Normal mul(Matrix mat, Normal n) {
        return new Normal(
                mat.m[0][0] * n.x + mat.m[1][0] * n.y + mat.m[2][0] * n.z,
                mat.m[0][1] * n.x + mat.m[1][1] * n.y + mat.m[2][1] * n.z,
                mat.m[0][2] * n.x + mat.m[1][2] * n.y + mat.m[2][2] * n.z);
    }

}
