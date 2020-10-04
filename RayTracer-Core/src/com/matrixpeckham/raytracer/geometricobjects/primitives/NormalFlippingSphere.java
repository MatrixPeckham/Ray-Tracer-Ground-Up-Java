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
 * Sphere that flips its normal based on which side the ray hits on. This was an
 * attempt to fix 28.46 bug I had, this didn't fix it, but I figured this class
 * may be useful in the future.
 *
 * @author William Matrix Peckham
 */
public class NormalFlippingSphere extends GeometricObject {

    //All implementation is the same as Sphere, for comments please see that class
    private final Point3D center;

    private double radius;

    private static final double EPSILON = 0.001;

    double invSurfaceArea;

    private Sampler sampler = null;

    /**
     * default constructor
     */
    public NormalFlippingSphere() {
        super();
        center = new Point3D(0);
        radius = 1;
        invSurfaceArea = 1 / (4 * Utility.PI * radius * radius);
    }

    /**
     *
     * @param c
     * @param r
     */
    public NormalFlippingSphere(Point3D c, double r) {
        super();
        center = new Point3D(c);
        radius = r;
        invSurfaceArea = 1 / (4 * Utility.PI * radius * radius);
    }

    /**
     *
     * @param sphere
     */
    public NormalFlippingSphere(NormalFlippingSphere sphere) {
        super(sphere);
        center = new Point3D(sphere.center);
        radius = sphere.radius;
        invSurfaceArea = 1 / (4 * Utility.PI * radius * radius);
    }

    @Override
    public GeometricObject cloneGeometry() {
        return new NormalFlippingSphere(this);
    }

    @Override
    public boolean hit(Ray ray, ShadeRec sr) {
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
                sr.lastT = t;
                sr.normal.setTo(temp.add(ray.d.mul(t)).div(radius));
                if (sr.normal.dot(sr.ray.d.neg()) < 0) {//only difference flip normal
                    sr.normal.setTo(temp.add(ray.d.mul(t)).div(-radius));
                }
                sr.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
                return true;
            }
            t = (-b + e) / denom;
            if (t > EPSILON) {
                sr.lastT = t;
                sr.normal.setTo(temp.add(ray.d.mul(t)).div(radius));
                if (sr.normal.dot(sr.ray.d.neg()) < 0) {//only difference flip normal
                    sr.normal.setTo(temp.add(ray.d.mul(t)).div(-radius));
                }
                sr.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hit(Ray ray, ArrayList<CSGShadeRec> hit, ShadeRec s) {
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
            CSGShadeRec sr = new CSGShadeRec(s);
            sr.lastT = t;
            sr.normal.setTo(temp.add(ray.d.mul(t)).div(radius));
            if (sr.normal.dot(sr.ray.d.neg()) < 0) {//only difference flip normal
                sr.normal.setTo(temp.add(ray.d.mul(t)).div(-radius));
            }
            sr.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
            hit.add(sr);
            sr = new CSGShadeRec(s);
            sr.entering = false;
            t = (-b + e) / denom;
            sr.lastT = t;
            sr.normal.setTo(temp.add(ray.d.mul(t)).div(radius));
            if (sr.normal.dot(sr.ray.d.neg()) < 0) {//only difference flip normal
                sr.normal.setTo(temp.add(ray.d.mul(t)).div(-radius));
            }
            sr.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
            hit.add(sr);
            return true;
        }
    }

    /**
     *
     * @return
     */
    public Point3D getCenter() {
        return center;
    }

    /**
     *
     * @param center
     */
    public void setCenter(Point3D center) {
        this.center.setTo(center);
    }

    @Override
    public Normal getNormal(Point3D p) {
        Vector3D n = center.sub(p);
        n.normalize();
        return new Normal(n);
    }

    /**
     *
     * @return
     */
    public double getRadius() {
        return radius;
    }

    /**
     *
     * @param radius
     */
    public void setRadius(double radius) {
        this.radius = radius;
        invSurfaceArea = 1 / (4 * Utility.PI * radius * radius);
    }

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

    private static final Logger LOG
            = Logger.getLogger(NormalFlippingSphere.class.getName());

}
