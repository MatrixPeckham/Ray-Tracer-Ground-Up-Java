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
import com.matrixpeckham.raytracer.util.BBox;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Vector3D;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Paraboloid primitive class.
 *
 * @author William Matrix Peckham
 */
public class Paraboloid extends GeometricObject {

    /**
     * small value
     */
    private static final double EPSILON = 0.001;

    private double a;

    private double b;

    private double c;

    private double zl;

    private double zh;

    private boolean hyperbolic;

    /**
     * default constructor
     */
    public Paraboloid() {
        super();
        a = 1;
        b = 1;
        c = 1;
        zl = -1;
        zh = 1;
        hyperbolic = false;
    }

    /**
     * constructor initializes center and radius
     *
     * @param c
     * @param r
     */
    public Paraboloid(double a, double b, double c, double z1, double z2,
            boolean hyper) {
        super();
        this.a = a;
        this.b = b;
        this.c = c;
        this.zl = z1;
        this.zh = z2;
        hyperbolic = hyper;
    }

    /**
     * copy constructor
     *
     * @param sphere
     */
    public Paraboloid(Paraboloid sphere) {
        super(sphere);
        this.a = sphere.a;
        this.b = sphere.b;
        this.c = sphere.c;
        this.zl = sphere.zl;
        this.zh = sphere.zh;
        this.hyperbolic = sphere.hyperbolic;
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public GeometricObject cloneGeometry() {
        return new Paraboloid(this);
    }

    /**
     * hit function, sphere is one of the simplest
     *
     * @param ray
     * @param sr
     * @return
     */
    @Override
    public boolean hit(Ray ray, ShadeRec sr) {
        //intersect ray param
        double t;

        double ox = ray.o.x;
        double oy = ray.o.y;
        double oz = ray.o.z;
        double dx = ray.d.x;
        double dy = ray.d.y;
        double dz = ray.d.z;

        double a2 = a * a;
        double b2 = b * b;

        double quadA, quadB, quadC;
        if (!hyperbolic) {
            double x0 = 1 / c;
            double x1 = 1 / a2;
            double x2 = 1 / b2;
            quadC = ox * ox * x1 + oy * oy * x2 - oz * x0;
            quadB = 2 * dx * ox * x1 + 2 * dy * oy * x2 - dz * x0;
            quadA = dx * dx * x1 + dy * dy * x2;
        } else {
            double x0 = 1 / c;
            double x1 = 1 / b2;
            double x2 = 1 / a2;
            quadC = -ox * ox * x2 + oy * oy * x1 - oz * x0;
            quadB = -dx * ox * x2 + 2 * dy * oy * x1 - dz * x0;
            quadA = -dx * dx * x2 + dy * dy * x1;
        }

        //set up the quadratic for ray parameter
        double disc = quadB * quadB - 4.0 * quadA * quadC;

        //solves the quadratic equation and sets all the ShadeRec fields
        //solves the quadratic equation and sets all the ShadeRec fields
        if (disc < 0) {
            return false;
        } else {
            double e = Math.sqrt(disc);
            double denom = 2.0 * quadA;
            t = (-quadB - e) / denom;
            if (t > EPSILON) {
                Point3D loc = ray.o.add(ray.d.mul(t));
                if (loc.z < zl) {
                    return false;
                }
                if (loc.z > zh) {
                    return false;
                }
                sr.lastT = t;
                sr.localHitPosition.setTo(loc);
                sr.normal.
                        setTo(2 * loc.x / (a * a), 2 * loc.y / (b * b), -1 / c);
                sr.normal.normalize();
                if (ray.d.neg().dot(new Vector3D(sr.normal)) < 0.0) {
                    sr.normal.setTo(sr.normal.neg());
                }
                return true;
            }
            t = (-quadB + e) / denom;
            if (t > EPSILON) {
                Point3D loc = ray.o.add(ray.d.mul(t));
                if (loc.z < zl) {
                    return false;
                }
                if (loc.z > zh) {
                    return false;
                }
                sr.lastT = t;
                sr.localHitPosition.setTo(loc);
                sr.normal.
                        setTo(2 * loc.x / (a * a), 2 * loc.y / (b * b), -1 / c);
                sr.normal.normalize();
                if (ray.d.neg().dot(new Vector3D(sr.normal)) < 0.0) {
                    sr.normal.setTo(sr.normal.neg());
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hit(Ray ray, ArrayList<ShadeRec> hits, ShadeRec sr1) {
        //intersect ray param
        double t;

        double ox = ray.o.x;
        double oy = ray.o.y;
        double oz = ray.o.z;
        double dx = ray.d.x;
        double dy = ray.d.y;
        double dz = ray.d.z;

        double a2 = a * a;
        double b2 = b * b;
        double quadA, quadB, quadC;
        if (!hyperbolic) {
            double x0 = 1 / c;
            double x1 = 1 / a2;
            double x2 = 1 / b2;
            quadC = ox * ox * x1 + oy * oy * x2 - oz * x0;
            quadB = 2 * dx * ox * x1 + 2 * dy * oy * x2 - dz * x0;
            quadA = dx * dx * x1 + dy * dy * x2;
        } else {
            double x0 = 1 / c;
            double x1 = 1 / b2;
            double x2 = 1 / a2;
            quadC = -ox * ox * x2 + oy * oy * x1 - oz * x0;
            quadB = -dx * ox * x2 + 2 * dy * oy * x1 - dz * x0;
            quadA = -dx * dx * x2 + dy * dy * x1;
        }

        //set up the quadratic for ray parameter
        double disc = quadB * quadB - 4.0 * quadA * quadC;

        //solves the quadratic equation and sets all the ShadeRec fields
        if (disc < 0) {
            return false;
        } else {
            double e = Math.sqrt(disc);
            double denom = 2.0 * quadA;
            t = (-quadB - e) / denom;
            Point3D loc = ray.o.add(ray.d.mul(t));
            ShadeRec sr;
            if (!(loc.z < zl)) {
                if (!(loc.z > zh)) {

                    sr = new ShadeRec(sr1);
                    sr.lastT = t;
                    sr.localHitPosition.setTo(loc);
                    sr.normal.
                            setTo(2 * loc.x / (a * a), 2 * loc.y / (b * b), -1
                                    / c);
                    sr.normal.normalize();
                    if (ray.d.neg().dot(new Vector3D(sr.normal)) < 0.0) {
                        sr.normal.setTo(sr.normal.neg());
                    }
                    hits.add(sr);
                }
            }
            t = (-quadB + e) / denom;
            loc = ray.o.add(ray.d.mul(t));
            if (!(loc.z < zl)) {
                if (!(loc.z > zh)) {
                    sr = new ShadeRec(sr1);
                    sr.lastT = t;
                    sr.localHitPosition.setTo(loc);
                    sr.normal.
                            setTo(2 * loc.x / (a * a), 2 * loc.y * (b * b), -1
                                    / c);
                    sr.normal.normalize();
                    if (ray.d.neg().dot(new Vector3D(sr.normal)) < 0.0) {
                        sr.normal.setTo(sr.normal.neg());
                    }
                    hits.add(sr);
                }
            }
            return true;
        }
    }

    /**
     * returns the normal at the point
     *
     * @param p
     * @return
     */
    @Override
    public Normal getNormal(Point3D loc
    ) {
        //simply the direction from the center to the hit point
        Normal n = new Normal(2 * loc.x / (a * a), 2 * loc.y / (b * b), -1 / c);
        n.normalize();
        return n;
    }

    /**
     * shadow hit, works just like the hit function, but doesn't have to compute
     * normals
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
        //intersect ray param
        double t;

        double ox = ray.o.x;
        double oy = ray.o.y;
        double oz = ray.o.z;
        double dx = ray.d.x;
        double dy = ray.d.y;
        double dz = ray.d.z;

        double a2 = a * a;
        double b2 = b * b;
        double quadA, quadB, quadC;
        if (!hyperbolic) {
            double x0 = 1 / c;
            double x1 = 1 / a2;
            double x2 = 1 / b2;
            quadC = ox * ox * x1 + oy * oy * x2 - oz * x0;
            quadB = 2 * dx * ox * x1 + 2 * dy * oy * x2 - dz * x0;
            quadA = dx * dx * x1 + dy * dy * x2;
        } else {
            double x0 = 1 / c;
            double x1 = 1 / b2;
            double x2 = 1 / a2;
            quadC = -ox * ox * x2 + oy * oy * x1 - oz * x0;
            quadB = -dx * ox * x2 + 2 * dy * oy * x1 - dz * x0;
            quadA = -dx * dx * x2 + dy * dy * x1;
        }

        //set up the quadratic for ray parameter
        double disc = quadB * quadB - 4.0 * quadA * quadC;

        //solves the quadratic equation and sets all the ShadeRec fields
        //solves the quadratic equation and sets all the ShadeRec fields
        if (disc < 0) {
            return false;
        } else {
            double e = Math.sqrt(disc);
            double denom = 2.0 * quadA;
            t = (-quadB - e) / denom;
            Point3D loc = ray.o.add(ray.d.mul(t));
            if (loc.z < zl) {
                return false;
            }
            if (loc.z > zh) {
                return false;
            }
            if (t > EPSILON) {
                tr.d = t;
                return true;
            }
            t = (-quadB + e) / denom;
            loc = ray.o.add(ray.d.mul(t));
            if (loc.z < zl) {
                return false;
            }
            if (loc.z > zh) {
                return false;
            }
            if (t > EPSILON) {
                tr.d = t;
                return true;
            }
        }
        return false;
    }

    @Override
    public BBox getBoundingBox() {
        //TODO: needs impl
        return new BBox();
    }

    private static final Logger LOG = Logger.getLogger(Paraboloid.class.
            getName());

}
