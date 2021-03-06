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
 * Part Cylinder
 *
 * @author William Matrix Peckham
 */
public class PartCylinder extends GeometricObject {

    /**
     * min y
     */
    protected double y0;

    /**
     * max y
     */
    protected double y1;

    /**
     * min angle
     */
    double phiMin = 0;

    /**
     * max angle
     */
    double phiMax = Utility.TWO_PI;

    /**
     * radius
     */
    protected double radius;

    /**
     * inverse of the radius
     */
    protected double invRadius;

    /**
     * default constructor
     */
    public PartCylinder() {
        super();
        y0 = -1;
        y1 = 1;
        radius = 1;
        invRadius = 1;
    }

    /**
     * initializing constructor (degrees)
     *
     * @param y0
     * @param y1
     * @param radius
     * @param azimuthmin
     * @param azimuthmax
     */
    public PartCylinder(double y0, double y1, double radius, double azimuthmin,
            double azimuthmax) {
        super();
        this.y0 = y0;
        this.y1 = y1;
        phiMin = azimuthmin * Utility.PI_ON_180;
        phiMax = azimuthmax * Utility.PI_ON_180;
        this.radius = radius;
        this.invRadius = 1.0 / radius;
    }

    /**
     * copy constructor
     *
     * @param cy
     */
    public PartCylinder(PartCylinder cy) {
        super(cy);
        y0 = cy.y0;
        y1 = cy.y1;
        phiMax = cy.phiMax;
        phiMin = cy.phiMin;
        radius = cy.radius;
        invRadius = cy.invRadius;
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public GeometricObject cloneGeometry() {
        return new PartCylinder(this);
    }

    /**
     * hit function
     *
     * @param ray
     * @param sr
     *
     * @return
     */
    @Override
    public boolean hit(Ray ray, ShadeRec sr) {
        double t;
        double ox = ray.o.x;
        double oy = ray.o.y;
        double oz = ray.o.z;
        double dx = ray.d.x;
        double dy = ray.d.y;
        double dz = ray.d.z;

        double a = dx * dx + dz * dz;
        double b = 2.0 * (ox * dx + oz * dz);
        double c = ox * ox + oz * oz - radius * radius;
        double disc = b * b - 4.0 * a * c;

        if (disc < 0.0) {
            return (false);
        } else {
            double e = Math.sqrt(disc);
            double denom = 2.0 * a;
            t = (-b - e) / denom;    // smaller root

            if (t > Utility.EPSILON) {
                double yhit = oy + t * dy;
                Vector3D hit = new Vector3D(ray.o.add(ray.d.mul(t)));
                double phi = Math.atan2(hit.x, hit.z);
                if (phi < 0) {
                    phi += Utility.TWO_PI;
                }

                //check height and angle
                if (yhit > y0 && yhit < y1 && phi >= phiMin && phi <= phiMax) {
                    sr.lastT = t;
                    sr.normal.setTo(new Normal((ox + t * dx) * invRadius, 0.0,
                            (oz + t * dz) * invRadius));

                    // test for hitting from inside
                    if (ray.d.neg().dot(new Vector3D(sr.normal)) < 0.0) {
                        sr.normal.setTo(sr.normal.neg());
                    }

                    sr.localHitPosition.setTo(ray.o.add(Vector3D.mul(sr.lastT,
                            ray.d)));

                    return (true);
                }
            }

            t = (-b + e) / denom;    // larger root

            if (t > Utility.EPSILON) {
                double yhit = oy + t * dy;
                Vector3D hit = new Vector3D(ray.o.add(ray.d.mul(t)));
                double phi = Math.atan2(hit.x, hit.z);
                if (phi < 0) {
                    phi += Utility.TWO_PI;
                }

                //height and angle
                if (yhit > y0 && yhit < y1 && phi >= phiMin && phi <= phiMax) {
                    sr.lastT = t;
                    sr.normal.setTo(new Normal((ox + t * dx) * invRadius, 0.0,
                            (oz
                            + t * dz) * invRadius));

                    // test for hitting inside surface
                    if (ray.d.neg().dot(sr.normal) < 0.0) {
                        sr.normal.setTo(sr.normal.neg());
                    }

                    sr.localHitPosition.setTo(ray.o.add(Vector3D.mul(sr.lastT,
                            ray.d)));

                    return (true);
                }
            }
        }

        return (false);
    }

    /**
     * hit function
     *
     * @param ray
     * @param sr
     *
     * @return
     */
    @Override
    public boolean hit(Ray ray, ArrayList<CSGShadeRec> hits, ShadeRec s) {
        double t;
        double ox = ray.o.x;
        double oy = ray.o.y;
        double oz = ray.o.z;
        double dx = ray.d.x;
        double dy = ray.d.y;
        double dz = ray.d.z;

        double a = dx * dx + dz * dz;
        double b = 2.0 * (ox * dx + oz * dz);
        double c = ox * ox + oz * oz - radius * radius;
        double disc = b * b - 4.0 * a * c;

        if (disc < 0.0) {
            return (false);
        } else {
            double e = Math.sqrt(disc);
            double denom = 2.0 * a;
            t = (-b - e) / denom;    // smaller root
            boolean ret = false;
            double yhit = oy + t * dy;
            Vector3D hit = new Vector3D(ray.o.add(ray.d.mul(t)));
            double phi = Math.atan2(hit.x, hit.z);
            if (phi < 0) {
                phi += Utility.TWO_PI;
            }

            //check height and angle
            if (yhit > y0 && yhit < y1 && phi >= phiMin && phi <= phiMax) {
                CSGShadeRec sr = new CSGShadeRec(s);
                sr.lastT = t;
                sr.normal.setTo(new Normal((ox + t * dx) * invRadius, 0.0,
                        (oz + t * dz) * invRadius));

                // test for hitting from inside
                if (ray.d.neg().dot(new Vector3D(sr.normal)) < 0.0) {
                    sr.normal.setTo(sr.normal.neg());
                }

                sr.localHitPosition.setTo(ray.o.add(Vector3D.mul(sr.lastT,
                        ray.d)));
                hits.add(sr);
                ret = (true);
            }

            t = (-b + e) / denom;    // larger root

            yhit = oy + t * dy;
            hit = new Vector3D(ray.o.add(ray.d.mul(t)));
            phi = Math.atan2(hit.x, hit.z);
            if (phi < 0) {
                phi += Utility.TWO_PI;
            }

            //height and angle
            if (yhit > y0 && yhit < y1 && phi >= phiMin && phi <= phiMax) {
                CSGShadeRec sr = new CSGShadeRec(s);
                sr.entering = false;
                sr.lastT = t;
                sr.normal.setTo(new Normal((ox + t * dx) * invRadius, 0.0,
                        (oz
                        + t * dz) * invRadius));

                // test for hitting inside surface
                if (ray.d.neg().dot(sr.normal) < 0.0) {
                    sr.normal.setTo(sr.normal.neg());
                }

                sr.localHitPosition.setTo(ray.o.add(Vector3D.mul(sr.lastT,
                        ray.d)));
                hits.add(sr);
                ret = (true);
            }
            return ret;
        }

    }

    /**
     * shadow hit, same as hit function
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
        double ox = ray.o.x;
        double oy = ray.o.y;
        double oz = ray.o.z;
        double dx = ray.d.x;
        double dy = ray.d.y;
        double dz = ray.d.z;

        double a = dx * dx + dz * dz;
        double b = 2.0 * (ox * dx + oz * dz);
        double c = ox * ox + oz * oz - radius * radius;
        double disc = b * b - 4.0 * a * c;

        if (disc < 0.0) {
            return (false);
        } else {
            double e = Math.sqrt(disc);
            double denom = 2.0 * a;
            t = (-b - e) / denom;    // smaller root

            if (t > Utility.EPSILON) {
                double yhit = oy + t * dy;
                Vector3D hit = new Vector3D(ray.o.add(ray.d.mul(t)));
                double phi = Math.atan2(hit.x, hit.z);
                if (phi < 0) {
                    phi += Utility.TWO_PI;
                }

                if (yhit > y0 && yhit < y1 && phi >= phiMin && phi <= phiMax) {
                    tr.d = t;
                    return (true);
                }
            }

            t = (-b + e) / denom;    // larger root

            if (t > Utility.EPSILON) {
                double yhit = oy + t * dy;
                Vector3D hit = new Vector3D(ray.o.add(ray.d.mul(t)));
                double phi = Math.atan2(hit.x, hit.z);
                if (phi < 0) {
                    phi += Utility.TWO_PI;
                }

                if (yhit > y0 && yhit < y1 && phi >= phiMin && phi <= phiMax) {
                    tr.d = t;
                    return (true);
                }
            }
        }

        return (false);
    }

}
