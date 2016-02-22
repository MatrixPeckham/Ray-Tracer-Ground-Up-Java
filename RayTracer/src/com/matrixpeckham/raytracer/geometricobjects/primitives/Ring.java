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

/**
 *
 * @author William Matrix Peckham
 */
public class Ring extends GeometricObject {

    private Point3D center = new Point3D();
    private Normal normal = new Normal(0, 1, 0);
    private double outerRadius = 1;
    private double innerRadius = 0.5;
    private Sampler sampler;
    double area = Utility.PI * outerRadius * outerRadius - Utility.PI
            * innerRadius * innerRadius;
    double invArea = 1 / area;
    final Vector3D up = new Vector3D(0, 1, 0);
    Vector3D u = new Vector3D(1, 0, 0);
    Vector3D v = new Vector3D(0, 0, 1);
    Vector3D w = new Vector3D(0, 1, 0);

    public Ring() {
    }

    public Ring(Point3D loc, Normal nor, double innrad, double outrad) {
        center.setTo(loc);
        normal.setTo(nor);
        outerRadius = outrad;
        innerRadius = innrad;
        area = Utility.PI * outerRadius * outerRadius - Utility.PI * innerRadius
                * innerRadius;
        invArea = 1 / area;
    }

    public Ring(Ring d) {
        center.setTo(d.center);
        normal.setTo(d.normal);
        innerRadius = d.innerRadius;
        outerRadius = d.outerRadius;
        area = d.area;
        invArea = d.invArea;
        if(d.sampler!=null){
            sampler = d.sampler.clone();
            sampler.mapSamplesToUnitDisc();
        }
        u.setTo(d.u);
        v.setTo(d.v);
        w.setTo(d.w);

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
        area = Utility.PI * outerRadius * outerRadius - Utility.PI * innerRadius
                * innerRadius;
    }

    public double getOuterRadius() {
        return outerRadius;
    }

    public void setOuterRadius(double radius) {
        this.outerRadius = radius;
        area = Utility.PI * outerRadius * outerRadius - Utility.PI * innerRadius
                * innerRadius;
    }

    public void setNormal(Normal n) {
        normal.setTo(n);
    }

    @Override
    public GeometricObject clone() {
        return new Ring(this);
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
    public boolean shadowHit(Ray ray, DoubleRef tr) {
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

    @Override
    public BBox getBoundingBox() {
        return new BBox(center.x - outerRadius, center.x + outerRadius, center.y - outerRadius,
                center.y + outerRadius, center.z - outerRadius, center.z + outerRadius);
    }

    public void setSampler(Sampler samplerPtr) {
        sampler = samplerPtr.clone();
        sampler.mapSamplesToUnitDisc();
    }

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

    @Override
    public Point3D sample() {
        Point2D dp = sampler.sampleUnitDisc();
        dp = dp.mul(outerRadius-innerRadius);
        Point3D rp = new Point3D(center);
        rp = rp.add(u.mul(dp.x+innerRadius)).add(v.mul(dp.y+innerRadius));
        return rp;
    }

    @Override
    public double pdf(ShadeRec sr) {
        return invArea;
    }

}
