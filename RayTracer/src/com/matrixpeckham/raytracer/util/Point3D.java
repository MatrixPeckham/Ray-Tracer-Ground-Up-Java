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
public class Point3D {

    public double x;
    public double y;
    public double z;

    // default ructor
    public Point3D() {
        this(0);
    }

    // ructor
    public Point3D(double a) {
        this(a, a, a);
    }

    // ructor
    public Point3D(double a, double b, double c) {
        x = a;
        y = b;
        z = c;
    }

    // copy ructor
    public Point3D(Point3D p) {
        this();
        setTo(p);
    }

    // assignment operator
    public Point3D setTo(Point3D p) {
        x = p.x;
        y = p.y;
        z = p.z;
        return this;
    }

    // unary minus
    public Point3D neg() {
        return new Point3D(-x, -y, -z);
    }

    // vector joining two points
    public Vector3D sub(Point3D p) {
        return new Vector3D(x - p.x, y - p.y, z - p.z);
    }

    // addition of a vector				
    public Point3D add(Vector3D v) {
        return new Point3D(x + v.x, y + v.y, z + v.z);
    }

    // subtraction of a vector
    public Point3D sub(Vector3D v) {
        return new Point3D(x - v.x, y - v.y, z - v.z);
    }

    // multiplication by a double on the right
    public Point3D mul(double a) {
        return new Point3D(x * a, y * a, z * a);
    }

    // square of distance bertween two points
    public double distSquared(Point3D p) {
        return x - p.x * x - p.x + y - p.y * y - p.y + z - p.z * z - p.z;
    }

    // distance bewteen two points
    public double distance(Point3D p) {
        return Math.sqrt(distSquared(p));
    }

    public static Point3D mul(double a, Point3D p) {
        return new Point3D(a * p.x, a * p.y, a * p.z);
    }

    public static Point3D mul(Matrix mat, Point3D p) {
        return new Point3D(mat.m[0][0] * p.x + mat.m[0][1] * p.y + mat.m[0][2]
                * p.z + mat.m[0][3],
                mat.m[1][0] * p.x + mat.m[1][1] * p.y + mat.m[1][2] * p.z
                + mat.m[1][3],
                mat.m[2][0] * p.x + mat.m[2][1] * p.y + mat.m[2][2] * p.z
                + mat.m[2][3]);
    }

}
