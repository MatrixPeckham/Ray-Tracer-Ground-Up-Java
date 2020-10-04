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
import com.matrixpeckham.raytracer.geometricobjects.csg.CSGShadeRec;
import com.matrixpeckham.raytracer.util.*;
import java.util.ArrayList;

/**
 * Part Sphere class
 *
 * @author William Matrix Peckham
 */
public class PartSphere extends GeometricObject {

    /**
     * center of sphere
     */
    Point3D center = new Point3D();

    /**
     * radius
     */
    double radius = 1;

    /**
     * radial min
     */
    double phiMin = 0;

    /**
     * radial max
     */
    double phiMax = Utility.TWO_PI;

    /**
     * polar min
     */
    double thetaMin = 0;

    /**
     * polar max
     */
    double thetaMax = Utility.PI;

    /**
     * cached cosines
     */
    double cosThetaMin = 1;

    double cosThetaMax = -1;

    /**
     * default constructor
     */
    public PartSphere() {
    }

    /**
     * parameter constructor in degrees Degrees
     *
     * @param c
     * @param r
     * @param azimuthmin
     * @param azimuthmax
     * @param polarmin
     * @param polarmax
     */
    public PartSphere(Point3D c, double r, double azimuthmin, double azimuthmax,
            double polarmin, double polarmax) {
        super();
        center.setTo(c);
        radius = r;
        phiMin = azimuthmin * Utility.PI_ON_180;
        phiMax = azimuthmax * Utility.PI_ON_180;
        thetaMin = polarmin * Utility.PI_ON_180;
        thetaMax = polarmax * Utility.PI_ON_180;
        cosThetaMin = Math.cos(thetaMin);
        cosThetaMax = Math.cos(thetaMax);
    }

    /**
     * copy constructor
     *
     * @param o
     */
    public PartSphere(PartSphere o) {
        super(o);
        center.setTo(o.center);
        radius = o.radius;
        phiMax = o.phiMax;
        phiMin = o.phiMin;
        thetaMax = o.thetaMax;
        thetaMin = o.thetaMin;
        cosThetaMax = o.cosThetaMax;
        cosThetaMin = o.cosThetaMin;
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public GeometricObject cloneGeometry() {
        return new PartSphere(this);
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
            //we check the hit positions with the spherical coordinates.
            t = (-b - e) / denom;
            if (t > Utility.EPSILON) {
                Vector3D hit = ray.o.add(ray.d.mul(t)).sub(center);
                double phi = Math.atan2(hit.x, hit.z);
                if (phi < 0) {
                    phi += Utility.TWO_PI;
                }
                if (hit.y <= radius * cosThetaMin && hit.y >= radius
                        * cosThetaMax && phi >= phiMin && phi <= phiMax) {
                    s.lastT = t;
                    s.normal.setTo(temp.add(ray.d.mul(t)).div(radius)); //points out
                    if (ray.d.neg().dot(s.normal) > 0) {
                        s.normal.setTo(s.normal.neg());
                    }
                    s.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
                    return true;
                }
            }
            t = (-b + e) / denom;
            if (t > Utility.EPSILON) {
                Vector3D hit = ray.o.add(ray.d.mul(t)).sub(center);
                double phi = Math.atan2(hit.x, hit.z);
                if (phi < 0) {
                    phi += Utility.TWO_PI;
                }
                if (hit.y <= radius * cosThetaMin && hit.y >= radius
                        * cosThetaMax && phi >= phiMin && phi <= phiMax) {
                    s.lastT = t;
                    s.normal.setTo(temp.add(ray.d.mul(t)).div(radius)); //points out
                    s.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
                    return true;
                }
            }
        }
        return false;
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
    public boolean hit(Ray ray, ArrayList<CSGShadeRec> hits, ShadeRec sr) {
        double t;
        Vector3D temp = ray.o.sub(center);
        double a = ray.d.dot(ray.d);
        double b = 2.0 * temp.dot(ray.d);
        double c = temp.dot(temp) - radius * radius;
        double disc = b * b - 4.0 * a * c;
        if (disc < 0) {
            return false;
        } else {
            boolean ret = false;
            double e = Math.sqrt(disc);
            double denom = 2.0 * a;
            //we check the hit positions with the spherical coordinates.
            t = (-b - e) / denom;
            Vector3D hit = ray.o.add(ray.d.mul(t)).sub(center);
            double phi = Math.atan2(hit.x, hit.z);
            if (phi < 0) {
                phi += Utility.TWO_PI;
            }
            if (hit.y <= radius * cosThetaMin && hit.y >= radius
                    * cosThetaMax && phi >= phiMin && phi <= phiMax) {
                CSGShadeRec s = new CSGShadeRec(sr);
                s.lastT = t;
                s.normal.setTo(temp.add(ray.d.mul(t)).div(radius)); //points out
                if (ray.d.neg().dot(s.normal) > 0) {
                    s.normal.setTo(s.normal.neg());
                }
                s.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
                hits.add(s);
                ret = true;
            }
            t = (-b + e) / denom;
            hit = ray.o.add(ray.d.mul(t)).sub(center);
            phi = Math.atan2(hit.x, hit.z);
            if (phi < 0) {
                phi += Utility.TWO_PI;
            }
            if (hit.y <= radius * cosThetaMin && hit.y >= radius
                    * cosThetaMax && phi >= phiMin && phi <= phiMax) {
                CSGShadeRec s = new CSGShadeRec(sr);
                s.entering = false;
                s.lastT = t;
                s.normal.setTo(temp.add(ray.d.mul(t)).div(radius)); //points out
                s.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
                hits.add(s);
                ret = true;
            }
            return ret;
        }
    }

    /**
     * shadow hit, same as hit
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
            if (t > Utility.EPSILON) {
                Vector3D hit = ray.o.add(ray.d.mul(t)).sub(center);
                double phi = Math.atan2(hit.x, hit.z);
                if (phi < 0) {
                    phi += Utility.TWO_PI;
                }
                if (hit.y <= radius * cosThetaMin && hit.y >= radius
                        * cosThetaMax && phi >= phiMin && phi <= phiMax) {
                    tr.d = t;
                    return true;
                }
            }
            t = (-b + e) / denom;
            if (t > Utility.EPSILON) {
                Vector3D hit = ray.o.add(ray.d.mul(t)).sub(center);
                double phi = Math.atan2(hit.x, hit.z);
                if (phi < 0) {
                    phi += Utility.TWO_PI;
                }
                if (hit.y <= radius * cosThetaMin && hit.y >= radius
                        * cosThetaMax && phi >= phiMin && phi <= phiMax) {
                    tr.d = t;
                    return true;
                }
            }
        }
        return false;
    }

}
