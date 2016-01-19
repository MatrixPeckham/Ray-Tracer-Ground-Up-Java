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
 * Vector class for directions.
 *
 * @author William Matrix Peckham
 *
 */
public class Vector3D {

    /**
     * X coordinate
     *
     */
    public double x;
    /**
     * Y coordinate
     *
     */
    public double y;
    /**
     * Z coordinate
     *
     */
    public double z;

    /**
     * For debugging
     *
     * @return      *
     */
    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")Vector3D";
    }

    /**
     * (0,0,0)
     */
    public Vector3D() {
        this(0);
    }

    /**
     * (a,a,a)
     *
     * @param a
     */
    public Vector3D(double a) {
        this(a, a, a);
    }

    /**
     * (_x,_y,_z)
     *
     * @param _x
     * @param _y
     * @param _z
     */
    public Vector3D(double _x, double _y, double _z) {
        x = _x;
        y = _y;
        z = _z;
    }

    /**
     * copy constructor
     *
     * @param v
     */
    public Vector3D(Vector3D v) {
        this(v.x, v.y, v.z);
    }

    /**
     * constructs a vector from a Normal
     *
     * @param n
     */
    public Vector3D(Normal n) {
        this(n.x, n.y, n.z);
    }

    /**
     * constructs a vector from a point
     *
     * @param p
     */
    public Vector3D(Point3D p) {
        this(p.x, p.y, p.z);
    }

    /**
     * assignment operator java substitute
     *
     * @param other
     * @return      *
     */
    public Vector3D setTo(Vector3D other) {
        x = other.x;
        y = other.y;
        z = other.z;
        return this;
    }

    /**
     * assignment operator java substitute
     *
     * @param x
     * @param y
     * @param z      *
     */
    public void setTo(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * assignment operator java substitute
     *
     * @param other
     * @return      *
     */
    public Vector3D setTo(Normal other) {
        x = other.x;
        y = other.y;
        z = other.z;
        return this;
    }

    /**
     * assignment operator java substitute
     *
     * @param other
     * @return      *
     */
    public Vector3D setTo(Point3D other) {
        x = other.x;
        y = other.y;
        z = other.z;
        return this;
    }

    /**
     * java substitute for unary minus
     *
     * @return      new negated vector
     */
    public Vector3D neg() {
        return new Vector3D(-x, -y, -z);
    }

    /**
     * length
     *
     * @return
     */
    public double length() {
        return Math.sqrt(lenSquared());
    }

    /**
     * square of the length
     *
     * @return
     */
    public double lenSquared() {
        return x * x + y * y + z * z;
    }

    /**
     * multiplication by a double on the right
     *
     * @param a
     * @return
     */
    public Vector3D mul(double a) {
        return new Vector3D(x * a, y * a, z * a);
    }

    /**
     * division by a double
     *
     * @param a
     * @return
     */
    public Vector3D div(double a) {
        return new Vector3D(x / a, y / a, z / a);
    }

    /**
     * addition
     *
     * @param v
     * @return      *
     */
    public Vector3D add(Vector3D v) {
        return new Vector3D(x + v.x, y + v.y, z + v.z);
    }

    /**
     * compound addition
     *
     * @param v
     * @return
     */
    public Vector3D addLocal(Vector3D v) {
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }

    /**
     * subtraction
     *
     * @param v
     * @return
     */
    public Vector3D sub(Vector3D v) {
        return new Vector3D(x - v.x, y - v.y, z - v.z);
    }

    /**
     * dot product
     *
     * @param v
     * @return
     */
    public double dot(Vector3D v) {
        return (x * v.x + y * v.y + z * v.z);
    }

    /**
     * cross product
     *
     * @param v
     * @return
     */
    public Vector3D cross(Vector3D v) {
        return new Vector3D(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y
                * v.x);
    }

    /**
     * convert vector to a unit vector
     */
    public void normalize() {
        double len = length();
        x /= len;
        y /= len;
        z /= len;
    }

    /**
     * return a unit vector, and normalize the vector
     *
     * @return this
     *
     */
    public Vector3D hat() {
        normalize();
        return this;
    }

    /**
     * Static version of mul, for left multiplication
     *
     * @param a
     * @param v
     * @return      *
     */
    public static Vector3D mul(double a, Vector3D v) {
        return new Vector3D(a * v.x, a * v.y, a * v.z);
    }

    /**
     * Matrix multiplication
     *
     * @param mat
     * @param v
     * @return      *
     */
    public static Vector3D mul(Matrix mat, Vector3D v) {
        return new Vector3D(
                mat.m[0][0] * v.x + mat.m[0][1] * v.y + mat.m[0][2] * v.z,
                mat.m[1][0] * v.x + mat.m[1][1] * v.y + mat.m[1][2] * v.z,
                mat.m[2][0] * v.x + mat.m[2][1] * v.y + mat.m[2][2] * v.z);
    }

    /**
     * dot product with normal
     *
     * @param v
     * @return      *
     */
    public double dot(Normal v) {
        return (x * v.x + y * v.y + z * v.z);
    }

}
