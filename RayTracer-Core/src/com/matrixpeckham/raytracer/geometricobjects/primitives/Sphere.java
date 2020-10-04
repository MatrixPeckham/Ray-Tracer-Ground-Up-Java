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
import com.matrixpeckham.raytracer.samplers.Sampler;
import com.matrixpeckham.raytracer.util.*;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Sphere primitive class.
 *
 * @author William Matrix Peckham
 */
public class Sphere extends GeometricObject {

    /**
     * sphere center
     */
    private final Point3D center;

    /**
     * sphere radius
     */
    private double radius;

    /**
     * small value
     */
    private static final double EPSILON = 0.001;

    /**
     * inverse surface area
     */
    double invSurfaceArea;

    /**
     * sampler for area lights
     */
    private Sampler sampler = null;

    /**
     * default constructor
     */
    public Sphere() {
        super();
        center = new Point3D(0);
        radius = 1;
        invSurfaceArea = 1 / (4 * Utility.PI * radius * radius);
    }

    /**
     * constructor initializes center and radius
     *
     * @param c
     * @param r
     */
    public Sphere(Point3D c, double r) {
        super();
        center = new Point3D(c);
        radius = r;
        //surface area calculated
        invSurfaceArea = 1 / (4 * Utility.PI * radius * radius);
    }

    /**
     * copy constructor
     *
     * @param sphere
     */
    public Sphere(Sphere sphere) {
        super(sphere);
        center = new Point3D(sphere.center);
        radius = sphere.radius;
        invSurfaceArea = 1 / (4 * Utility.PI * radius * radius);
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public GeometricObject cloneGeometry() {
        return new Sphere(this);
    }

    /**
     * hit function, sphere is one of the simplest
     *
     * @param ray
     * @param sr
     *
     * @return
     */
    @Override
    public boolean hit(Ray ray, ShadeRec sr) {
        //intersect ray param
        double t;

        //set up the quadratic for ray parameter
        Vector3D temp = ray.o.sub(center);
        double a = ray.d.dot(ray.d);
        double b = 2.0 * temp.dot(ray.d);
        double c = temp.dot(temp) - radius * radius;
        double disc = b * b - 4.0 * a * c;

        //solves the quadratic equation and sets all the ShadeRec fields
        if (disc < 0) {
            return false;
        } else {
            double e = Math.sqrt(disc);
            double denom = 2.0 * a;
            t = (-b - e) / denom;
            if (t > EPSILON) {
                sr.lastT = t;
                sr.normal.setTo(temp.add(ray.d.mul(t)).div(radius));
                sr.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
                return true;
            }
            t = (-b + e) / denom;
            if (t > EPSILON) {
                sr.lastT = t;
                sr.normal.setTo(temp.add(ray.d.mul(t)).div(radius));
                sr.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hit(Ray ray, ArrayList<CSGShadeRec> hits, ShadeRec sr1) {
        //intersect ray param
        double t;

        //set up the quadratic for ray parameter
        Vector3D temp = ray.o.sub(center);
        double a = ray.d.dot(ray.d);
        double b = 2.0 * temp.dot(ray.d);
        double c = temp.dot(temp) - radius * radius;
        double disc = b * b - 4.0 * a * c;

        //solves the quadratic equation and sets all the ShadeRec fields
        if (disc < 0) {
            return false;
        } else {
            double e = Math.sqrt(disc);
            double denom = 2.0 * a;
            t = (-b - e) / denom;
            CSGShadeRec sr = new CSGShadeRec(sr1);
            sr.lastT = t;
            sr.normal.setTo(temp.add(ray.d.mul(t)).div(radius));
            sr.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
            hits.add(sr);
            t = (-b + e) / denom;
            sr = new CSGShadeRec(sr1);
            sr.entering = false;
            sr.lastT = t;
            sr.normal.setTo(temp.add(ray.d.mul(t)).div(radius));
            sr.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
            hits.add(sr);
            return true;
        }
    }

    /**
     * getter
     *
     * @return
     */
    public Point3D getCenter() {
        return center;
    }

    /**
     * setter
     *
     * @param center
     */
    public void setCenter(Point3D center) {
        this.center.setTo(center);
    }

    /**
     * returns the normal at the point
     *
     * @param p
     *
     * @return
     */
    @Override
    public Normal getNormal(Point3D p) {
        //simply the direction from the center to the hit point
        Vector3D n = center.sub(p);
        n.normalize();
        return new Normal(n);
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
        invSurfaceArea = 1 / (4 * Utility.PI * radius * radius);
    }

    /**
     * shadow hit, works just like the hit function, but doesn't have to compute
     * normals
     *
     * @param ray
     * @param tr
     *
     * @return
     */
    @Override
    public boolean shadowHit(Ray ray, DoubleRef tr) {
        if (!shadows) {
            return false;
        }
        double t;
        Vector3D temp = ray.o.sub(center);
        double a = ray.d.dot(ray.d);
        double b = 2.0 * temp.dot(ray.d);
        double c = temp.dot(temp) - radius * radius;
        double disc = b * b - 4.0 * a * c;
        if (disc < 0) {
            return false;
        } else {
            double e = Math.sqrt(disc);
            double denom = 2.0 * a;
            t = (-b - e) / denom;
            if (t > EPSILON) {
                tr.d = t;
                return true;
            }
            t = (-b + e) / denom;
            if (t > EPSILON) {
                tr.d = t;
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param sampler
     */
    public void setSampler(Sampler sampler) {
        this.sampler = sampler.cloneSampler();
        this.sampler.mapSamplesToSphere();
    }

    @Override
    public Point3D sample() {
        return sampler.sampleSphere().mul(radius).add(new Vector3D(center));
    }

    @Override
    public BBox getBoundingBox() {
        return new BBox(center.add(new Vector3D(-radius)), center.add(
                new Vector3D(radius)));
    }

    @Override
    public double pdf(ShadeRec sr) {
        return invSurfaceArea;
    }

    private static final Logger LOG = Logger.getLogger(Sphere.class.getName());

}
