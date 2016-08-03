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
 * Class for a flat ring, or annulus.
 *
 * @author William Matrix Peckham
 */
public class Ring extends GeometricObject {

    /**
     * center point
     */
    private final Point3D center = new Point3D();

    /**
     * normal of the ring.
     */
    private final Normal normal = new Normal(0, 1, 0);

    /**
     * outer radius of the ring
     */
    private double outerRadius = 1;

    /**
     * inner radius of the ring
     */
    private double innerRadius = 0.5;

    /**
     * sampler if we're a light
     */
    private Sampler sampler;

    /**
     * surface area of the ring
     */
    double area = Utility.PI * outerRadius * outerRadius - Utility.PI
            * innerRadius * innerRadius;

    /**
     * inverse area of the ring
     */
    double invArea = 1 / area;

    /**
     * coordinate system definitions for picking sample point
     */
    final Vector3D up = new Vector3D(0, 1, 0);

    Vector3D u = new Vector3D(1, 0, 0);

    Vector3D v = new Vector3D(0, 0, 1);

    Vector3D w = new Vector3D(0, 1, 0);

    /**
     * default constructor
     */
    public Ring() {
    }

    /**
     * initializing constructor
     *
     * @param loc
     * @param nor
     * @param innrad
     * @param outrad
     */
    public Ring(Point3D loc, Normal nor, double innrad, double outrad) {
        center.setTo(loc);
        normal.setTo(nor);
        outerRadius = outrad;
        innerRadius = innrad;
        area = Utility.PI * outerRadius * outerRadius - Utility.PI * innerRadius
                * innerRadius;
        invArea = 1 / area;
    }

    /**
     * copy constructor
     *
     * @param d
     */
    public Ring(Ring d) {
        center.setTo(d.center);
        normal.setTo(d.normal);
        innerRadius = d.innerRadius;
        outerRadius = d.outerRadius;
        area = d.area;
        invArea = d.invArea;
        if (d.sampler != null) {
            sampler = d.sampler.cloneSampler();
            sampler.mapSamplesToUnitDisc();
        }
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
    public double getInnerRadius() {
        return innerRadius;
    }

    /**
     * setter
     *
     * @param radius
     */
    public void setInnerRadius(double radius) {
        this.innerRadius = radius;
        area = Utility.PI * outerRadius * outerRadius - Utility.PI * innerRadius
                * innerRadius;
    }

    /**
     * getter
     *
     * @return
     */
    public double getOuterRadius() {
        return outerRadius;
    }

    /**
     * setter
     *
     * @param radius
     */
    public void setOuterRadius(double radius) {
        this.outerRadius = radius;
        area = Utility.PI * outerRadius * outerRadius - Utility.PI * innerRadius
                * innerRadius;
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
     * clone method
     *
     * @return
     */
    @Override
    public GeometricObject cloneGeometry() {
        return new Ring(this);
    }

    /**
     * gets the normal at the point. returns the normal of the ring, because it
     * doesn't vary by location
     *
     * @param p
     * @return
     */
    @Override
    public Normal getNormal(Point3D p) {
        return normal;
    }

    /**
     * Hit function
     *
     * @param ray
     * @param s
     * @return
     */
    @Override
    public boolean hit(Ray ray, ShadeRec s) {
        //gets the ray/plane instersection point
        double t = (center.sub(ray.o).dot(normal) / (ray.d.dot(normal)));
        //early out if we're too close, prevent self intersection noise
        if (t < Utility.EPSILON) {
            return false;
        }
        //gets the hit point for ray/plane
        Point3D p = ray.o.add(ray.d.mul(t));
        //only if the distance from the center to hit point  between inner and outer radius
        if (center.distSquared(p) < outerRadius * outerRadius) {
            if (center.distSquared(p) > innerRadius * innerRadius) {
                s.lastT = t;
                s.normal.setTo(normal);
                s.localHitPosition.setTo(p);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hit(Ray ray, ArrayList<ShadeRec> hits, ShadeRec s1) {
        //gets the ray/plane instersection point
        double dot = (ray.d.dot(normal));
        if (dot == 0) {
            return false;
        }
        double t = (center.sub(ray.o).dot(normal) / dot);
        //early out if we're too close, prevent self intersection noise
        //gets the hit point for ray/plane
        Point3D p = ray.o.add(ray.d.mul(t));
        //only if the distance from the center to hit point  between inner and outer radius
        if (center.distSquared(p) < outerRadius * outerRadius) {
            if (center.distSquared(p) > innerRadius * innerRadius) {
                ShadeRec s = new ShadeRec(s1);
                s.lastT = t;
                s.normal.setTo(normal);
                s.localHitPosition.setTo(p);
                hits.add(s);
                return true;
            }
        }
        return false;
    }

    /**
     * shadow hit, same as hit
     *
     * @param ray
     * @param tr
     * @return
     */
    @Override
    public boolean shadowHit(Ray ray, DoubleRef tr) {
        //early out for non-shadow casting all implementations do this
        if (!shadows) {
            return false;
        }
        double t = (center.sub(ray.o).dot(normal) / (ray.d.dot(normal)));
        if (t < Utility.EPSILON) {
            return false;
        }
        Point3D p = ray.o.add(ray.d.mul(t));
        if (center.distSquared(p) < outerRadius * outerRadius) {
            if (center.distSquared(p) > innerRadius * innerRadius) {
                tr.d = t;
                return true;
            }
        }
        return false;
    }

    /**
     * bounding box function, returns a much larger bounds than it needs to.
     * this is faster and easier to understand. makes bounding box of size 2 *
     * outerRadius on all sides, when normal is axis aligned this is much bigger
     * than it should be. the bounds would really only need two axes to be 2 *
     * outerRadius and the third would be EPS sized. but our hit function is
     * simple enough that the false positives caused by this large bounds will
     * not have too large an effect.
     *
     * @return
     */
    @Override
    public BBox getBoundingBox() {
        return new BBox(center.x - outerRadius, center.x + outerRadius, center.y
                - outerRadius,
                center.y + outerRadius, center.z - outerRadius, center.z
                + outerRadius);
    }

    /**
     * sets the sampler for the object, maps it to a disc
     *
     * @param samplerPtr
     */
    public void setSampler(Sampler samplerPtr) {
        sampler = samplerPtr.cloneSampler();
        sampler.mapSamplesToUnitDisc();
    }

    /**
     * computes the UVW coordinates of the object.
     */
    public void computeUVW() {
        w.setTo(normal);
        w.normalize();
        u = up.cross(w);
        u.normalize();
        v = w.cross(u);

        //special cases for strait up and down.
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
     * samples the ring.
     *
     * @return
     */
    @Override
    public Point3D sample() {
        //sample a unit disc
        Point2D dp = sampler.sampleUnitDisc();
        //map sample to the width of the ring
        dp = dp.mul(outerRadius - innerRadius);
        //clone center point of ring
        Point3D rp = new Point3D(center);
        //offset center point by disc sample in uv directions
        rp = rp.add(u.mul(dp.x + innerRadius)).add(v.mul(dp.y + innerRadius));
        return rp;
    }

    /**
     * returns inverse of area
     *
     * @param sr
     * @return
     */
    @Override
    public double pdf(ShadeRec sr) {
        return invArea;
    }

    private static final Logger LOG = Logger.getLogger(Ring.class.getName());

}
