/*
 * Copyright (C) 2016 William Matrix Peckham
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
package com.matrixpeckham.raytracer.geometricobjects.primitives;

import com.matrixpeckham.raytracer.geometricobjects.GeometricObject;
import com.matrixpeckham.raytracer.geometricobjects.csg.CSGShadeRec;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Class represents a square on the xz plane centered on the origin with a hole
 * cut in the center.
 *
 * @author William Matrix Peckham
 */
public class CutFace extends GeometricObject {

    /**
     * length of a side of the square
     */
    private double size;

    /**
     * radius of the circle to cut into the middle of the square
     */
    private double radius;

    /**
     * default constructor
     */
    public CutFace() {
        size = 1;
        radius = 0.5;
    }

    /**
     * initializing constructor
     *
     * @param size
     * @param radius
     */
    public CutFace(double size, double radius) {
        this.size = size;
        this.radius = radius;
    }

    /**
     * copy constructor
     *
     * @param c
     */
    public CutFace(CutFace c) {
        this.size = c.size;
        this.radius = c.radius;
    }

    /**
     * clone function
     *
     * @return
     */
    @Override
    public GeometricObject cloneGeometry() {
        return new CutFace(this);
    }

    /**
     * getter
     *
     * @return
     */
    public double getSize() {
        return size;
    }

    /**
     * setter
     *
     * @param size
     */
    public void setSize(double size) {
        this.size = size;
    }

    /**
     * getter
     *
     * @return
     */
    public double getRadius() {
        return radius;
    }

    /**
     * setter
     *
     * @param radius
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * hit function
     *
     * @param ray
     * @param sr
     *
     * @return
     */
    @Override
    public boolean hit(Ray ray, ShadeRec sr) {
        //because we're on the xz plane we only need the y intercept as the t param
        double t = -ray.o.y / ray.d.y;

        //we do hit the plane
        if (t > Utility.EPSILON) {
            //x and y coordinates of hit point
            double xi = ray.o.x + t * ray.d.x;
            double zi = ray.o.z + t * ray.d.z;
            //distance on plane
            double d = xi * xi + zi * zi;
            //half width of square
            double size_on_two = 0.5 * size;

            //checks for inside square and outside circle.
            if ((-size_on_two <= xi && xi <= size_on_two) && (-size_on_two <= zi
                    && zi <= size_on_two) // inside square
                    && d >= radius * radius) // outside circle
            {
                sr.lastT = t;
                sr.normal.setTo(0.0, 1.0, 0.0);
                sr.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));

                return (true);
            } else {
                return (false);
            }
        }

        return (false);
    }

    /**
     * hit function
     *
     * @param ray
     * @param sr
     *
     * @return
     */
    @Override
    public boolean hit(Ray ray, ArrayList<CSGShadeRec> hit, ShadeRec s) {
        //because we're on the xz plane we only need the y intercept as the t param
        if (ray.d.y == 0) {
            return false;
        }
        double t = -ray.o.y / ray.d.y;

        //we do hit the plane
        //x and y coordinates of hit point
        double xi = ray.o.x + t * ray.d.x;
        double zi = ray.o.z + t * ray.d.z;
        //distance on plane
        double d = xi * xi + zi * zi;
        //half width of square
        double size_on_two = 0.5 * size;

        //checks for inside square and outside circle.
        if ((-size_on_two <= xi && xi <= size_on_two) && (-size_on_two <= zi
                && zi <= size_on_two) // inside square
                && d >= radius * radius) // outside circle
        {
            CSGShadeRec sr = new CSGShadeRec(s);
            sr.lastT = t;
            sr.normal.setTo(0.0, 1.0, 0.0);
            sr.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
            hit.add(sr);
            return (true);
        }
        return false;
    }

    /**
     * shadow function works the same way as hit function
     *
     * @param ray
     * @param tr
     *
     * @return
     */
    @Override
    public boolean shadowHit(Ray ray, DoubleRef tr) {
        double t = -ray.o.y / ray.d.y;

        if (t > Utility.EPSILON) {
            double xi = ray.o.x + t * ray.d.x;
            double zi = ray.o.z + t * ray.d.z;
            double d = xi * xi + zi * zi;
            double size_on_two = 0.5 * size;

            if ((-size_on_two <= xi && xi <= size_on_two) && (-size_on_two <= zi
                    && zi <= size_on_two) // inside square
                    && d >= radius * radius) // outside circle
            {
                tr.d = t;
                return (true);
            } else {
                return (false);
            }
        }

        return (false);
    }

    private static final Logger LOG = Logger.getLogger(CutFace.class
            .getName());

}
