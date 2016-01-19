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
 * Class to represent normals
 * @author William Matrix Peckham
 */
public class Normal {

    /**
     * x
     */
    public double x;
    
    /**
     * y
     */
    public double y;
    
    /**
     * z
     */
    public double z;

    /**
     * string rep of normal useful for debugging
     * @return 
     */
    @Override
    public String toString(){
        return "("+x+","+y+","+z+")Normal";
    }

    /**
     * normal (0,0,0)
     */
    public Normal() {
        this(0);
    }

    /**
     * normal (a,a,a)
     * @param a 
     */
    public Normal(double a) {
        this(a, a, a);
    }

    /**
     * set normal to (x,y,z)
     * @param x
     * @param y
     * @param z 
     */
    public void setTo(double x, double y, double z){
        this.x=x;
        this.y=y;
        this.z=z;
    }

    /**
     * new normal (x,y,z)
     * @param _x
     * @param _y
     * @param _z 
     */
    public Normal(double _x, double _y, double _z) {
        x = _x;
        y = _y;
        z = _z;
    }


    /**
     * copy constructor
     * @param n 
     */
    public Normal(Normal n) {
        this(n.x, n.y, n.z);
    }


    /**
     * construct normal from vector
     * @param v 
     */
    public Normal(Vector3D v) {
        this(v.x, v.y, v.z);
    }

    /**
     * java equals operator
     * @param rhs
     * @return 
     */
    public Normal setTo(Normal rhs) {
        x = rhs.x;
        y = rhs.y;
        z = rhs.z;
        return this;
    }

    /**
     * copys vector into this normal
     * @param rhs
     * @return 
     */
    public Normal setTo(Vector3D rhs) {
        x = rhs.x;
        y = rhs.y;
        z = rhs.z;
        return this;
    }

    /**
     * sets this normal to point
     * @param rhs
     * @return 
     */
    public Normal setTo(
            Point3D rhs) {
        x = rhs.x;
        y = rhs.y;
        z = rhs.z;
        return this;
    }

    /**
     * negation, returns new vector
     * @return 
     */
    public Normal neg() {
        return new Normal(-x, -y, -z);
    }

    /**
     * add two normals returns new vector
     * @param n
     * @return 
     */
    public Normal add(Normal n) {
        return new Normal(x + n.x, y + n.y, z + n.z);
    }

    /**
     * add a vector to this one return this one
     * @param n
     * @return 
     */
    public Normal addLocal(Normal n) {
        x += n.x;
        y += n.y;
        z += n.z;
        return this;
    }

    /**
     * dot product
     * @param v
     * @return 
     */
    public double dot(Vector3D v) {
        return x * v.x + y * v.y + z * v.z;
    }

    /**
     * scale normal
     * @param a
     * @return 
     */
    public Vector3D mul(double a) {
        return new Vector3D(x * a, y * a, z * a);
    }

    /**
     * normalizes this normal
     */
    public void normalize() {
        double len = Math.sqrt(x * x + y * y + z * z);
        x /= len;
        y /= len;
        z /= len;
    }

    /**
     * static method to left multiply normal and scalar
     * @param a
     * @param n
     * @return 
     */
    public static Normal mul(double a, Normal n) {
        return new Normal(a * n.x, a * n.y, a * n.z);
    }

    /**
     * adds a vector to a normal.
     * @param v
     * @param n
     * @return 
     */
    public static Vector3D add(Vector3D v, Normal n) {
        return new Vector3D(v.x + n.x, v.y + n.y, v.z + n.z);
    }
    
    /**
     * subtracts a normal from a vector.
     * @param v
     * @param n
     * @return 
     */
    public static Vector3D sub(Vector3D v, Normal n) {
        return new Vector3D(v.x - n.x, v.y - n.y, v.z - n.z);
    }

    /**
     * dot product of two normals.
     * @param v
     * @param n
     * @return 
     */
    public static double dot(Vector3D v, Normal n) {
        return v.x * n.x + v.y * n.y + v.z * n.z;
    }
    
    /**
     * transforms a normal by a matrix.
     * @param mat
     * @param n
     * @return 
     */
    public static Normal mul(Matrix mat, Normal n) {
        return new Normal(
                mat.m[0][0] * n.x + mat.m[1][0] * n.y + mat.m[2][0] * n.z,
                mat.m[0][1] * n.x + mat.m[1][1] * n.y + mat.m[2][1] * n.z,
                mat.m[0][2] * n.x + mat.m[1][2] * n.y + mat.m[2][2] * n.z);
    }

}
