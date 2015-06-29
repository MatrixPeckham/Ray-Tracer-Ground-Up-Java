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
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
import com.matrixpeckham.raytracer.util.Normal;

/**
 *
 * @author William Matrix Peckham
 */
public class OpenCylinder extends GeometricObject {

    protected double y0;
    protected double y1;
    protected double radius;
    protected double invRadius;

    public OpenCylinder() {
        super();
        y0 = -1;
        y1 = 1;
        radius = 1;
        invRadius = 1;
    }

    public OpenCylinder(double y0, double y1, double radius) {
        super();
        this.y0 = y0;
        this.y1 = y1;
        this.radius = radius;
        this.invRadius = 1.0 / radius;
    }

    public OpenCylinder(OpenCylinder cy) {
        super(cy);
        y0 = cy.y0;
        y1 = cy.y1;
        radius = cy.radius;
        invRadius = cy.invRadius;
    }

    @Override
    public GeometricObject clone() {
        return new OpenCylinder(this);
    }

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

                if (yhit > y0 && yhit < y1) {
                    sr.lastT = t;
                    sr.normal.setTo(new Normal((ox + t * dx) * invRadius, 0.0,
                            (oz + t * dz) * invRadius));

				// test for hitting from inside
                    if (ray.d.neg().dot(new Vector3D(sr.normal)) < 0.0) {
                        sr.normal = sr.normal.neg();
                    }

                    sr.localHitPosition.setTo(ray.o.add(Vector3D.mul(sr.lastT,
                            ray.d)));

                    return (true);
                }
            }

            t = (-b + e) / denom;    // larger root

            if (t > Utility.EPSILON) {
                double yhit = oy + t * dy;

                if (yhit > y0 && yhit < y1) {
                    sr.lastT = t;
                    sr.normal = new Normal((ox + t * dx) * invRadius, 0.0, (oz
                            + t * dz) * invRadius);

				// test for hitting inside surface
                    if (ray.d.neg().dot(sr.normal) < 0.0) {
                        sr.normal = sr.normal.neg();
                    }

                    sr.localHitPosition.setTo(ray.o.add(Vector3D.mul(sr.lastT,
                            ray.d)));

                    return (true);
                }
            }
        }

        return (false);
    }

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

                if (yhit > y0 && yhit < y1) {
                    tr.d = t;
                    return (true);
                }
            }

            t = (-b + e) / denom;    // larger root

            if (t > Utility.EPSILON) {
                double yhit = oy + t * dy;

                if (yhit > y0 && yhit < y1) {
                    tr.d = t;
                    return (true);
                }
            }
        }

        return (false);
    }

    @Override
    public BBox getBoundingBox() {
        return new BBox(-radius, radius, y0, y1, -radius, radius);
    }
    
}
