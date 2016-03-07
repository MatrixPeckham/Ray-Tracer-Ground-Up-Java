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
package com.matrixpeckham.raytracer.geometricobjects.partobjects;

import com.matrixpeckham.raytracer.geometricobjects.GeometricObject;
import com.matrixpeckham.raytracer.util.BBox;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;

/**
 * Part of a ring.
 *
 * @author William Matrix Peckham
 */
public class PartRing extends GeometricObject {

    /**
     * center point of ring
     */
    private final Point3D center = new Point3D();

    /**
     * normal of ring
     */
    private final Normal normal = new Normal(0, 1, 0);

    /**
     * outer radius
     */
    private double outerRadius = 1;

    /**
     * inner radius
     */
    private double innerRadius = 0.5;

    /**
     * low angle
     */
    private double phiMin = 0;

    /**
     * high angle
     */
    private double phiMax = Utility.TWO_PI;

    /**
     * default constructor
     */
    public PartRing() {
    }

    /**
     * initialization constructor (degrees)
     *
     * @param loc
     * @param innrad
     * @param outrad
     * @param phiMin
     * @param phiMax
     */
    public PartRing(Point3D loc, double innrad, double outrad, double phiMin,
            double phiMax) {
        center.setTo(loc);
        outerRadius = outrad;
        innerRadius = innrad;
        this.phiMin = phiMin * Utility.PI_ON_180;
        this.phiMax = phiMax * Utility.PI_ON_180;
    }

    /**
     * copy constructor
     *
     * @param d
     */
    public PartRing(PartRing d) {
        center.setTo(d.center);
        normal.setTo(d.normal);
        innerRadius = d.innerRadius;
        outerRadius = d.outerRadius;
        phiMin = d.phiMin;
        phiMax = d.phiMax;
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
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public GeometricObject cloneGeometry() {
        return new PartRing(this);
    }

    /**
     * gets the normal for the point, the normal of the ring doesn't vary over
     * space so we return the normal
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
        //plane hit
        double t = (center.sub(ray.o).dot(normal) / (ray.d.dot(normal)));
        if (t < Utility.EPSILON) {
            return false;
        }

        //location of plane hit
        Point3D p = ray.o.add(ray.d.mul(t));

        //angle of point
        double phi = Math.atan2(p.x, p.z);

        //resets angle
        if (phi < 0) {
            phi += Utility.TWO_PI;
        }

        //checks location ray parameter and distance and angle for hit 
        if (phi >= phiMin && phi <= phiMax) {
            if (center.distSquared(p) < outerRadius * outerRadius) {
                if (center.distSquared(p) > innerRadius * innerRadius) {
                    s.lastT = t;
                    s.normal.setTo(normal);
                    s.localHitPosition.setTo(p);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * shadow hit, same as hit function
     *
     * @param ray
     * @param tr
     * @return
     */
    @Override
    public boolean shadowHit(Ray ray, DoubleRef tr) {
        //early out shadow, all implementations do this
        if (!shadows) {
            return false;
        }
        double t = (center.sub(ray.o).dot(normal) / (ray.d.dot(normal)));
        if (t < Utility.EPSILON) {
            return false;
        }
        Point3D p = ray.o.add(ray.d.mul(t));
        double phi = Math.atan2(p.x, p.z);
        if (phi < 0) {
            phi += Utility.TWO_PI;
        }
        if (phi >= phiMin && phi <= phiMax) {
            if (center.distSquared(p) < outerRadius * outerRadius) {
                if (center.distSquared(p) > innerRadius * innerRadius) {
                    tr.d = t;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * naive bounding box, for whole ring returned
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

}
