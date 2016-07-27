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
import com.matrixpeckham.raytracer.util.Point2D;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Disk class.
 *
 * @author William Matrix Peckham
 */
public class Disk extends GeometricObject {

    /**
     * center of disk
     */
    private final Point3D center = new Point3D();

    /**
     * normal of disk
     */
    private final Normal normal = new Normal(0, 1, 0);

    /**
     * radius of disk
     */
    private double radius = 1;

    /**
     * sampler if we're a light
     */
    private Sampler sampler;

    /**
     * surface area
     */
    double area = Utility.PI * radius * radius;

    /**
     * inverse of area
     */
    double invArea = 1 / area;

    /**
     * coordinate vectors
     */
    final Vector3D up = new Vector3D(0, 1, 0);

    Vector3D u = new Vector3D(1, 0, 0);

    Vector3D v = new Vector3D(0, 0, 1);

    Vector3D w = new Vector3D(0, 1, 0);

    /**
     * default constructor
     */
    public Disk() {
    }

    /**
     * initializing constructor
     *
     * @param loc
     * @param nor
     * @param rad
     */
    public Disk(Point3D loc, Normal nor, double rad) {
        center.setTo(loc);
        normal.setTo(nor);
        radius = rad;
        area = Utility.PI * radius * radius;
        invArea = 1 / area;
    }

    /**
     * copy constructor
     *
     * @param d
     */
    public Disk(Disk d) {
        center.setTo(d.center);
        normal.setTo(d.normal);
        radius = d.radius;
        area = d.area;
        invArea = d.invArea;
        sampler = d.sampler.cloneSampler();
        sampler.mapSamplesToUnitDisc();
        u.setTo(d.u);
        v.setTo(d.v);
        w.setTo(d.w);

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
     * setter
     *
     * @param n
     */
    public void setNormal(Normal n) {
        normal.setTo(n);
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public GeometricObject cloneGeometry() {
        return new Disk(this);
    }

    /**
     * get normal at point, returns the disk normal which doesn't change
     * spatially
     *
     * @param p
     * @return
     */
    @Override
    public Normal getNormal(Point3D p) {
        return normal;
    }

    /**
     * hit function
     *
     * @param ray
     * @param s
     * @return
     */
    @Override
    public boolean hit(Ray ray, ShadeRec s) {
        //get the intersection of the ray with the plane
        double t = (center.sub(ray.o).dot(normal) / (ray.d.dot(normal)));
        //prevent self intersection noise
        if (t < Utility.EPSILON) {
            return false;
        }
        //find point on plane and check it for distance from center point
        Point3D p = ray.o.add(ray.d.mul(t));
        if (center.distSquared(p) < radius * radius) {
            s.lastT = t;
            s.normal.setTo(normal);
            s.localHitPosition.setTo(p);
            return true;
        } else {
            return false;
        }
    }

    /**
     * hit function
     *
     * @param ray
     * @param s
     * @return
     */
    @Override
    public boolean hit(Ray ray, ArrayList<ShadeRec> hit, ShadeRec sr) {
        //get the intersection of the ray with the plane
        double dot = ray.d.dot(normal);
        if (dot == 0) {
            return false;
        }
        double t = (center.sub(ray.o).dot(normal) / (ray.d.dot(normal)));
        //find point on plane and check it for distance from center point
        Point3D p = ray.o.add(ray.d.mul(t));
        if (center.distSquared(p) < radius * radius) {
            ShadeRec s = new ShadeRec(sr);
            s.lastT = t;
            s.normal.setTo(normal);
            s.localHitPosition.setTo(p);
            hit.add(s);
            return true;
        } else {
            return false;
        }
    }

    /**
     * same as hit function.
     *
     * @param ray
     * @param tr
     * @return
     */
    @Override
    public boolean shadowHit(Ray ray, DoubleRef tr) {
        //early out for non-shadow casting objects, all implementations have this
        if (!shadows) {
            return false;
        }
        double t = (center.sub(ray.o).dot(normal) / (ray.d.dot(normal)));
        if (t < Utility.EPSILON) {
            return false;
        }
        Point3D p = ray.o.add(ray.d.mul(t));
        if (center.distSquared(p) < radius * radius) {
            tr.d = t;
            return true;
        } else {
            return false;
        }
    }

    /**
     * bounding box function, returns a much larger bounds than it needs to.
     * this is faster and easier to understand. makes bounding box of size 2 *
     * radius on all sides, when normal is axis aligned this is much bigger than
     * it should be. the bounds would really only need two axes to be 2 * radius
     * and the third would be EPS sized. but our hit function is simple enough
     * that the false positives caused by this large bounds will not have too
     * large an effect.
     *
     * @return
     */
    @Override
    public BBox getBoundingBox() {
        return new BBox(center.x - radius, center.x + radius, center.y - radius,
                center.y + radius, center.z - radius, center.z + radius);
    }

    /**
     * sets the sampler
     *
     * @param samplerPtr
     */
    public void setSampler(Sampler samplerPtr) {
        sampler = samplerPtr.cloneSampler();
        sampler.mapSamplesToUnitDisc();
    }

    /**
     * computes local coordinate system
     */
    public void computeUVW() {
        w.setTo(normal);
        w.normalize();
        u = up.cross(w);
        u.normalize();
        v = w.cross(u);

        //special cases for strait up and down view.
        if (normal.x == up.x && normal.z == up.z && normal.y == up.y) {
            u.setTo(new Vector3D(0, 0, 1));
            v.setTo(new Vector3D(1, 0, 0));
            w.setTo(new Vector3D(0, 1, 0));
        }
        if (normal.x == up.x && normal.z == up.z && normal.y == -up.y) {
            u.setTo(new Vector3D(1, 0, 0));
            v.setTo(new Vector3D(0, 0, 1));
            w.setTo(new Vector3D(0, -1, 0));
        }
    }

    /**
     * sample point on the light
     *
     * @return
     */
    @Override
    public Point3D sample() {
        Point2D dp = sampler.sampleUnitDisc();
        dp = dp.mul(radius);
        Point3D rp = new Point3D(center);
        rp = rp.add(u.mul(dp.x)).add(v.mul(dp.y));
        return rp;
    }

    /**
     * return the inverse area
     *
     * @param sr
     * @return
     */
    @Override
    public double pdf(ShadeRec sr) {
        return invArea;
    }

    private static final Logger LOG = Logger.getLogger(Disk.class.getName());

}
