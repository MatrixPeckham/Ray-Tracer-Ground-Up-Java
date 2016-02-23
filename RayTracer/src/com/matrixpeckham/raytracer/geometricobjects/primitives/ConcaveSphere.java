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
import com.matrixpeckham.raytracer.samplers.Sampler;
import com.matrixpeckham.raytracer.util.BBox;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 * Concave Sphere class is the same as Sphere, but normals always point inwards.
 *
 * @author William Matrix Peckham
 */
public class ConcaveSphere extends GeometricObject {

    /**
     * center
     */
    private Point3D center;

    /**
     * radius
     */
    private double radius;

    /**
     * epsilon value for this class
     */
    private static final double EPSILON = 0.001;

    /**
     * inverse of the surface area
     */
    double invSurfaceArea;

    /**
     * sampler if we're a light
     */
    private Sampler sampler = null;

    /**
     * default constructor
     */
    public ConcaveSphere() {
        super();
        center = new Point3D(0);
        radius = 1;
        invSurfaceArea = 1 / (4 * Utility.PI * radius * radius);
    }

    /**
     * initialization constructor
     *
     * @param c
     * @param r
     */
    public ConcaveSphere(Point3D c, double r) {
        super();
        center = new Point3D(c);
        radius = r;
        invSurfaceArea = 1 / (4 * Utility.PI * radius * radius);
    }

    /**
     * copy constructor
     *
     * @param sphere
     */
    public ConcaveSphere(ConcaveSphere sphere) {
        super(sphere);
        center = new Point3D(sphere.center);
        radius = sphere.radius;
        invSurfaceArea = 1 / (4 * Utility.PI * radius * radius);
    }

    /**
     * clone method
     *
     * @return
     */
    @Override
    public GeometricObject clone() {
        return new ConcaveSphere(this);
    }

    /**
     * hit function, just like Sphere, but returns the opposite normal
     *
     * @param ray
     * @param sr
     * @return
     */
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
                sr.normal.setTo(temp.add(ray.d.mul(t)).div(-radius));
                sr.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
                return true;
            }
            t = (-b + e) / denom;
            if (t > EPSILON) {
                sr.lastT = t;
                sr.normal.setTo(temp.add(ray.d.mul(t)).div(-radius));
                sr.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
                return true;
            }
        }
        return false;
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
     * returns the normal, same as Sphere but negates normal
     *
     * @param p
     * @return
     */
    @Override
    public Normal getNormal(Point3D p) {
        Vector3D n = center.sub(p);
        n.normalize();
        return new Normal(n.neg());
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
     * shadow hit works just like sphere
     *
     * @param ray
     * @param tr
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
     * sets sampler and maps to sphere
     *
     * @param sampler
     */
    public void setSampler(Sampler sampler) {
        this.sampler = sampler.clone();
        this.sampler.mapSamplesToSphere();
    }

    /**
     * samples a point on the sphere
     *
     * @return
     */
    @Override
    public Point3D sample() {
        return sampler.sampleSphere().mul(radius).add(new Vector3D(center));
    }

    /**
     * bounding box
     *
     * @return
     */
    @Override
    public BBox getBoundingBox() {
        return new BBox(center.add(new Vector3D(-radius)), center.add(
                new Vector3D(radius)));
    }

    /**
     * inverse area
     *
     * @param sr
     * @return
     */
    @Override
    public double pdf(ShadeRec sr) {
        return invSurfaceArea;
    }

}
