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
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 *
 * @author William Matrix Peckham
 */
public class OpenCone extends GeometricObject {

    double h = 2;
    double r = 1;
    private static final double EPSILON = 0.001;

    @Override
    public GeometricObject clone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hit(Ray ray, ShadeRec sr) {
        double t = Utility.HUGE_VALUE;
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
                    sr.lastT = t;
                    sr.normal.setTo(calcNormal(ray.o.add(ray.d.mul(t))));
                    if (ray.d.neg().dot(new Vector3D(sr.normal)) < 0.0) {
                        sr.normal = sr.normal.neg();
                    }
                    sr.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
                    return true;
                }
            }
            t = (-b + e) / denom;
            if (t > EPSILON) {
                double yhit = y + t * v;
                if (yhit >= 0 && yhit <= h) {
                    sr.lastT = t;
                    sr.normal.setTo(calcNormal(ray.o.add(ray.d.mul(t))));
                    if (ray.d.neg().dot(new Vector3D(sr.normal)) < 0.0) {
                        sr.normal = sr.normal.neg();
                    }
                    sr.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean shadowHit(Ray ray, DoubleRef tr) {
        double t = Utility.HUGE_VALUE;
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
                    tr.d=t;
                    return true;
                }
            }
            t = (-b + e) / denom;
            if (t > EPSILON) {
                double yhit = y + t * v;
                if (yhit >= 0 && yhit <= h) {
                    tr.d=t;
                    return true;
                }
            }
        }
        return false;    }

    private Normal calcNormal(Point3D p) {
        Normal n = new Normal();
        n.x = h * p.x / r;
        n.y = -(p.y - h);
        n.z = h * p.z / r;
        n.normalize();
        return n;
    }

}
