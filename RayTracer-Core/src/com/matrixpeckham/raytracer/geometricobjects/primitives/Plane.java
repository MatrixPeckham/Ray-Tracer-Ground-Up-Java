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
package com.matrixpeckham.raytracer.geometricobjects.primitives;

import com.matrixpeckham.raytracer.geometricobjects.GeometricObject;
import com.matrixpeckham.raytracer.geometricobjects.csg.CSGShadeRec;
import com.matrixpeckham.raytracer.util.*;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Plane class, models an infinite plane, that goes through a point and has a
 * normal.
 *
 * @author William Matrix Peckham
 */
public class Plane extends GeometricObject {

    /**
     * Point that the plane will go through
     */
    private final Point3D a;

    /**
     * normal
     */
    private final Normal n;

    //epsilon
    private static final double EPSILON = 0.001;

    /**
     * default constructor plane at 0 height with positive y normal
     */
    public Plane() {
        super();
        a = new Point3D(0);
        n = new Normal(0, 1, 0);
    }

    /**
     * initializing constructor
     *
     * @param point
     * @param normal
     */
    public Plane(Point3D point, Normal normal) {
        super();
        a = new Point3D(point);
        n = new Normal(normal);
        n.normalize();
    }

    /**
     * copy constructor
     *
     * @param p
     */
    public Plane(Plane p) {
        super(p);
        a = new Point3D(p.a);
        n = new Normal(p.n);
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public GeometricObject cloneGeometry() {
        return new Plane(this);
    }

    /**
     * hit function
     *
     * @param ray
     * @param s
     *
     * @return
     */
    @Override
    public boolean hit(Ray ray, ShadeRec s) {
        //intersection point with plane
        double t = a.sub(ray.o).dot(new Vector3D(n)) / (ray.d.dot(
                new Vector3D(n)));
        //t is greater than eps
        if (t > EPSILON) {
            s.lastT = t;
            s.normal.setTo(n);
            s.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
            return true;
        }
        return false;
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
    public boolean hit(Ray ray, ArrayList<CSGShadeRec> hits, ShadeRec sr) {
        //intersection point with plane
        double dot = (ray.d.dot(new Vector3D(n)));
        if (dot == 0) {
            return false;
        }
        double t = a.sub(ray.o).dot(new Vector3D(n)) / dot;
        //t is greater than eps
        CSGShadeRec s = new CSGShadeRec(sr);
        s.lastT = t;
        s.normal.setTo(n);
        s.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
        hits.add(s);
        return true;
    }

    /**
     * same as hit function
     *
     * @param ray
     * @param t
     *
     * @return
     */
    @Override
    public boolean shadowHit(Ray ray, DoubleRef t) {
        if (!shadows) {
            return false;
        }
        t.d = a.sub(ray.o).dot(new Vector3D(n)) / (ray.d.dot(new Vector3D(n)));
        return t.d > EPSILON;
    }

    private static final Logger LOG = Logger.getLogger(Plane.class.getName());

}
