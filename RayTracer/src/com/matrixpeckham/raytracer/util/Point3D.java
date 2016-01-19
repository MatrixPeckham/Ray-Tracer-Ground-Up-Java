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
 * Point3D class.
 *
 * @author William Matrix Peckham
 */
public class Point3D {

    /**
     * X
     */
    public double x;
    /**
     * Y
     */
    public double y;
    /**
     * Z
     */
    public double z;

    /**
     * Returns string version of this object, useful for debugging.
     *
     * @return
     */
    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")Point3D";
    }

    /**
     * Default Constructor. (0,0,0)
     */
    public Point3D() {
        this(0);
    }

    /**
     * Constructor (a,a,a)
     *
     * @param a
     */
    public Point3D(double a) {
        this(a, a, a);
    }

    /**
     * Constructor (a,b,c)
     */
    public Point3D(double a, double b, double c) {
        x = a;
        y = b;
        z = c;
    }

    /**
     * Sets the point to the coordinates supplied.
     *
     * @param x
     * @param y
     * @param z
     */
    public void setTo(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Copy constructor.
     *
     * @param p
     */
    public Point3D(Point3D p) {
        this();
        setTo(p);
    }

    /**
     * Java version of equals operator.
     *
     * @param p
     * @return
     */
    public Point3D setTo(Point3D p) {
        x = p.x;
        y = p.y;
        z = p.z;
        return this;
    }

    /**
     * Returns negated vector.
     *
     * @return
     */
    public Point3D neg() {
        return new Point3D(-x, -y, -z);
    }

    /**
     * Vector from other point to this point.
     *
     * @param p
     * @return new vector
     */
    public Vector3D sub(Point3D p) {
        return new Vector3D(x - p.x, y - p.y, z - p.z);
    }

    /**
     * Adds two vectors and returns the new point.
     *
     * @param v
     * @return
     */
    public Point3D add(Vector3D v) {
        return new Point3D(x + v.x, y + v.y, z + v.z);
    }

    /**
     * subtracts the vector and returns the new point
     *
     * @param v
     * @return
     */
    public Point3D sub(Vector3D v) {
        return new Point3D(x - v.x, y - v.y, z - v.z);
    }

    /**
     * scale by scalar and return new point
     *
     * @param a
     * @return
     */
    public Point3D mul(double a) {
        return new Point3D(x * a, y * a, z * a);
    }

    /**
     * returns the squared distance from this point to another
     *
     * @param p
     * @return
     */
    public double distSquared(Point3D p) {
        return (x - p.x) * (x - p.x) + (y - p.y) * (y - p.y) + (z - p.z) * (z
                - p.z);
    }

    /**
     * get the distance between this and the other point.
     * @param p
     * @return 
     */
    public double distance(Point3D p) {
        return Math.sqrt(distSquared(p));
    }

    /**
     * Static point scaling, for left multiplication.
     *  returns new point
     * @param a
     * @param p
     * @return 
     */
    public static Point3D mul(double a, Point3D p) {
        return new Point3D(a * p.x, a * p.y, a * p.z);
    }

    /**
     * Multiply the point by the matrix. returns transformed point
     * @param mat
     * @param p
     * @return 
     */
    public static Point3D mul(Matrix mat, Point3D p) {
        return new Point3D(
                mat.m[0][0] * p.x + mat.m[0][1] * p.y + mat.m[0][2] * p.z
                + mat.m[0][3],
                mat.m[1][0] * p.x + mat.m[1][1] * p.y + mat.m[1][2] * p.z
                + mat.m[1][3],
                mat.m[2][0] * p.x + mat.m[2][1] * p.y + mat.m[2][2] * p.z
                + mat.m[2][3]);
    }

}
