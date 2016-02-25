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
package com.matrixpeckham.raytracer.geometricobjects.triangles;

import com.matrixpeckham.raytracer.geometricobjects.GeometricObject;
import com.matrixpeckham.raytracer.util.BBox;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;

/**
 * Triangle class for rendering a single triangle with known coordinates and a
 * single normal that defines the plane of the triangle
 *
 * @author William Matrix Peckham
 */
public class Triangle extends GeometricObject {

    /**
     * point 1
     */
    private Point3D v0 = new Point3D(0, 0, 0);

    /**
     * point 2
     */
    private Point3D v1 = new Point3D(0, 0, 1);

    /**
     * point 3
     */
    private Point3D v2 = new Point3D(1, 0, 0);

    /**
     * normal of the triangle
     */
    private Normal normal = new Normal(0, 1, 0);

    /**
     * default constructor
     */
    public Triangle() {
        super();
    }

    /**
     * Constructor for three points
     *
     * @param a
     * @param b
     * @param c
     */
    public Triangle(Point3D a, Point3D b, Point3D c) {
        super();
        v0.setTo(a);
        v1.setTo(b);
        v2.setTo(c);
        computeNormal();
    }

    /**
     * copy constructor
     *
     * @param t
     */
    public Triangle(Triangle t) {
        super(t);
        v0.setTo(t.v0);
        v1.setTo(t.v1);
        v2.setTo(t.v2);
        normal.setTo(t.normal);
    }

    /**
     * clone method
     *
     * @return
     */
    @Override
    public GeometricObject clone() {
        return new Triangle(this);
    }

    /**
     * compute normal
     */
    public void computeNormal() {
        //cross product of the difference of the points
        normal = new Normal(v1.sub(v0).cross(v2.sub(v0)));
        normal.normalize();
    }

    /**
     * override the get bounds method
     *
     * @return
     */
    @Override
    public BBox getBoundingBox() {
        //offset so that the bounds are slightly larger than the triangle
        //prevents roundoff area causeing false negatives 
        double delta = 0.000001;
        //we simply use the min/max of each of the coordinates of all points

        return (new BBox(Math.min(Math.min(v0.x, v1.x), v2.x) - delta, Math.max(
                Math.max(v0.x, v1.x), v2.x) + delta,
                Math.min(Math.min(v0.y, v1.y), v2.y) - delta, Math.max(Math.max(
                                v0.y, v1.y), v2.y) + delta,
                Math.min(Math.min(v0.z, v1.z), v2.z) - delta, Math.max(Math.max(
                                v0.z, v1.z), v2.z) + delta));
    }

    /**
     * hit function
     *
     * @param ray
     * @param sr
     * @return
     */
    @Override
    public boolean hit(Ray ray, ShadeRec sr) {

        //we find the barycentric coordinates where the ray crosses the triangle
        //plane, then we ensure they are valid in-triangle coordinates
        //compute locations.
        double a = v0.x - v1.x, b = v0.x - v2.x, c = ray.d.x, d = v0.x - ray.o.x;
        double e = v0.y - v1.y, f = v0.y - v2.y, g = ray.d.y, h = v0.y - ray.o.y;
        double i = v0.z - v1.z, j = v0.z - v2.z, k = ray.d.z, l = v0.z - ray.o.z;

        double m = f * k - g * j, n = h * k - g * l, p = f * l - h * j;
        double q = g * i - e * k, s = e * j - f * i;

        double inv_denom = 1.0 / (a * m + b * q + c * s);

        double e1 = d * m - b * n - c * p;
        double beta = e1 * inv_denom;

        if (beta < 0.0) {
            return (false);
        }

        double r = e * l - h * i;
        double e2 = a * n + d * q + c * r;
        double gamma = e2 * inv_denom;

        if (gamma < 0.0) {
            return (false);
        }

        if (beta + gamma > 1.0) {
            return (false);
        }

        double e3 = a * p - b * r + d * s;
        double t = e3 * inv_denom;

        if (t < Utility.EPSILON) {
            return (false);
        }

        sr.lastT = t;
        sr.normal.setTo(normal);
        sr.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));

        return (true);
    }

    /**
     * shadow hit function
     *
     * @param ray
     * @param tr
     * @return
     */
    @Override
    public boolean shadowHit(Ray ray, DoubleRef tr) {
        //early bailout if we don't cast shadows, all shadowHit()'s do this
        if (!shadows) {
            return false;
        }

        //otherwise works the same as hit. doesn't compute hit point 
        double a = v0.x - v1.x, b = v0.x - v2.x, c = ray.d.x, d = v0.x - ray.o.x;
        double e = v0.y - v1.y, f = v0.y - v2.y, g = ray.d.y, h = v0.y - ray.o.y;
        double i = v0.z - v1.z, j = v0.z - v2.z, k = ray.d.z, l = v0.z - ray.o.z;

        double m = f * k - g * j, n = h * k - g * l, p = f * l - h * j;
        double q = g * i - e * k, s = e * j - f * i;

        double inv_denom = 1.0 / (a * m + b * q + c * s);

        double e1 = d * m - b * n - c * p;
        double beta = e1 * inv_denom;

        if (beta < 0.0) {
            return (false);
        }

        double r = r = e * l - h * i;
        double e2 = a * n + d * q + c * r;
        double gamma = e2 * inv_denom;

        if (gamma < 0.0) {
            return (false);
        }

        if (beta + gamma > 1.0) {
            return (false);
        }

        double e3 = a * p - b * r + d * s;
        double t = e3 * inv_denom;

        if (t < Utility.EPSILON) {
            return (false);
        }

        tr.d = t;

        return (true);
    }

    //TODO: this class could be used as an area light, implementation
    //the surface area of a triangle is calculable and to mapping a square
    //sample space to it shouldn't be hard.
}
