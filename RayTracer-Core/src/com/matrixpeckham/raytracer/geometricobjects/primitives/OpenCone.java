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
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Vector3D;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Cone class.
 *
 * @author William Matrix Peckham
 */
public class OpenCone extends GeometricObject {

    /**
     * height above 0
     */
    double h = 2;

    /**
     * radius at 0
     */
    double r = 1;

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    /**
     * epsilon value
     */
    private static final double EPSILON = 0.001;

    /**
     * clone
     *
     * @return
     */
    @Override
    public GeometricObject cloneGeometry() {
        return new OpenCone();
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

        //book didn't include this, only the implicit equation, I derived these
        // and it works, but I doubt it's as good as it could be.
        //param
        double t;

        //location
        double x = ray.o.x;
        double y = ray.o.y;
        double z = ray.o.z;

        //directions
        double u = ray.d.x;
        double v = ray.d.y;
        double w = ray.d.z;

        //radius and height squared
        double r2 = r * r;
        double h2 = h * h;

        //squares
        double u2 = u * u;
        double w2 = w * w;
        double v2 = v * v;
        double x2 = x * x;
        double y2 = y * y;
        double z2 = z * z;

        //quadratic coefficients
        double a = ((h2 * u2) / r2 + (h2 * w2) / r2 - v2);
        double b = (2 * h2 * x * u) / r2 + (2 * h2 * w * z) / r2 + 2 * h * v - 2
                * v * y;
        double c = (h2 * x2) / r2 + (h2 * z2) / r2 - h2 + 2 * h * y - y2;

        //solve
        double disc = b * b - 4.0 * a * c;
        if (disc < 0) {
            return false;
        } else {
            double e = Math.sqrt(disc);
            double denom = 2.0 * a;
            t = (-b - e) / denom;
            if (t > EPSILON) {
                double yhit = y + t * v;
                //check y coordinates
                if (yhit >= 0 && yhit <= h) {
                    sr.lastT = t;
                    sr.normal.setTo(calcNormal(ray.o.add(ray.d.mul(t))));
                    if (ray.d.neg().dot(new Vector3D(sr.normal)) < 0.0) {
                        sr.normal.setTo(sr.normal.neg());
                    }
                    sr.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
                    return true;
                }
            }
            t = (-b + e) / denom;
            if (t > EPSILON) {
                //check y coordinates
                double yhit = y + t * v;
                if (yhit >= 0 && yhit <= h) {
                    sr.lastT = t;
                    sr.normal.setTo(calcNormal(ray.o.add(ray.d.mul(t))));
                    if (ray.d.neg().dot(new Vector3D(sr.normal)) < 0.0) {
                        sr.normal.setTo(sr.normal.neg());
                    }
                    sr.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
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
     * @param hit
     * @param s
     * @return
     */
    @Override
    public boolean hit(Ray ray, ArrayList<ShadeRec> hit, ShadeRec s) {

        //book didn't include this, only the implicit equation, I derived these
        // and it works, but I doubt it's as good as it could be.
        //param
        double t;

        //location
        double x = ray.o.x;
        double y = ray.o.y;
        double z = ray.o.z;

        //directions
        double u = ray.d.x;
        double v = ray.d.y;
        double w = ray.d.z;

        //radius and height squared
        double r2 = r * r;
        double h2 = h * h;

        //squares
        double u2 = u * u;
        double w2 = w * w;
        double v2 = v * v;
        double x2 = x * x;
        double y2 = y * y;
        double z2 = z * z;

        //quadratic coefficients
        double a = ((h2 * u2) / r2 + (h2 * w2) / r2 - v2);
        double b = (2 * h2 * x * u) / r2 + (2 * h2 * w * z) / r2 + 2 * h * v - 2
                * v * y;
        double c = (h2 * x2) / r2 + (h2 * z2) / r2 - h2 + 2 * h * y - y2;

        //solve
        double disc = b * b - 4.0 * a * c;
        if (disc < 0) {
            return false;
        } else {
            boolean ret = false;
            double e = Math.sqrt(disc);
            double denom = 2.0 * a;
            t = (-b - e) / denom;
            double yhit = y + t * v;
            //check y coordinates
            if (yhit >= 0 && yhit <= h) {
                ShadeRec sr = new ShadeRec(s);
                sr.lastT = t;
                sr.normal.setTo(calcNormal(ray.o.add(ray.d.mul(t))));
                if (ray.d.neg().dot(new Vector3D(sr.normal)) < 0.0) {
                    sr.normal.setTo(sr.normal.neg());
                }
                sr.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
                ret = true;
                hit.add(sr);
            }
            t = (-b + e) / denom;
            //check y coordinates
            yhit = y + t * v;
            if (yhit >= 0 && yhit <= h) {
                ShadeRec sr = new ShadeRec(s);
                sr.lastT = t;
                sr.normal.setTo(calcNormal(ray.o.add(ray.d.mul(t))));
                if (ray.d.neg().dot(new Vector3D(sr.normal)) < 0.0) {
                    sr.normal.setTo(sr.normal.neg());
                }
                sr.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
                hit.add(sr);
                ret = true;
            }
            return ret;
        }
    }

    /**
     * works the same as the hit function.
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
        double x = ray.o.x;
        double y = ray.o.y;
        double z = ray.o.z;
        double u = ray.d.x;
        double v = ray.d.y;
        double w = ray.d.z;
        double r2 = r * r;
        double h2 = h * h;
        double u2 = u * u;
        double w2 = w * w;
        double v2 = v * v;
        double x2 = x * x;
        double y2 = y * y;
        double z2 = z * z;
        double a = ((h2 * u2) / r2 + (h2 * w2) / r2 - v2);
        double b = (2 * h2 * x * u) / r2 + (2 * h2 * w * z) / r2 + 2 * h * v - 2
                * v * y;
        double c = (h2 * x2) / r2 + (h2 * z2) / r2 - h2 + 2 * h * y - y2;
        double disc = b * b - 4.0 * a * c;
        if (disc < 0) {
            return false;
        } else {
            double e = Math.sqrt(disc);
            double denom = 2.0 * a;
            t = (-b - e) / denom;
            if (t > EPSILON) {
                double yhit = y + t * v;
                if (yhit >= 0 && yhit <= h) {
                    tr.d = t;
                    return true;
                }
            }
            t = (-b + e) / denom;
            if (t > EPSILON) {
                double yhit = y + t * v;
                if (yhit >= 0 && yhit <= h) {
                    tr.d = t;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * calculates the normal at a point
     *
     * @param p
     * @return
     */
    private Normal calcNormal(Point3D p) {
        Normal n = new Normal();
        n.x = h * p.x / r;
        n.y = -(p.y - h);
        n.z = h * p.z / r;
        n.normalize();
        return n;
    }

    private static final Logger LOG = Logger.getLogger(OpenCone.class.getName());

}
