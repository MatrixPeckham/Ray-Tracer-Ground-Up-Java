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

import com.matrixpeckham.raytracer.geometricobjects.primitives.*;
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

/**
 *
 * @author William Matrix Peckham
 */
public class PartRing extends GeometricObject {

    private Point3D center = new Point3D();
    private Normal normal = new Normal(0, 1, 0);
    private double outerRadius = 1;
    private double innerRadius = 0.5;
    private double phiMin = 0;
    private double phiMax = Utility.TWO_PI;

    public PartRing() {
    }

    public PartRing(Point3D loc, double innrad, double outrad,double phiMin, double phiMax) {
        center.setTo(loc);
        outerRadius = outrad;
        innerRadius = innrad;
        this.phiMin=phiMin*Utility.PI_ON_180;
        this.phiMax=phiMax*Utility.PI_ON_180;
    }

    public PartRing(PartRing d) {
        center.setTo(d.center);
        innerRadius = d.innerRadius;
        outerRadius = d.outerRadius;

    }

    public Point3D getCenter() {
        return center;
    }

    public void setCenter(Point3D center) {
        this.center.setTo(center);
    }

    public double getInnerRadius() {
        return innerRadius;
    }

    public void setInnerRadius(double radius) {
        this.innerRadius = radius;
    }

    public double getOuterRadius() {
        return outerRadius;
    }

    public void setOuterRadius(double radius) {
        this.outerRadius = radius;
    }


    @Override
    public GeometricObject clone() {
        return new PartRing(this);
    }


    @Override
    public Normal getNormal(Point3D p) {
        return normal;
    }

    @Override
    public boolean hit(Ray ray, ShadeRec s) {
        double t = (center.sub(ray.o).dot(normal) / (ray.d.dot(normal)));
        if (t < Utility.EPSILON) {
            return false;
        }
        Point3D p = ray.o.add(ray.d.mul(t));
        double phi = Math.atan2(p.x, p.z);
        if(phi<0){
            phi+=Utility.TWO_PI;
        }
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

    @Override
    public boolean shadowHit(Ray ray, DoubleRef tr) {
        if (!shadows) {
            return false;
        }
        double t = (center.sub(ray.o).dot(normal) / (ray.d.dot(normal)));
        if (t < Utility.EPSILON) {
            return false;
        }
        Point3D p = ray.o.add(ray.d.mul(t));
        double phi = Math.atan2(p.x, p.z);
        if(phi<0){
            phi+=Utility.TWO_PI;
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

    @Override
    public BBox getBoundingBox() {
        return new BBox(center.x - outerRadius, center.x + outerRadius, center.y - outerRadius,
                center.y + outerRadius, center.z - outerRadius, center.z + outerRadius);
    }




}
