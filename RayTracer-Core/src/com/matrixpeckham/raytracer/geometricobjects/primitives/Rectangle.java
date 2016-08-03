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
 * rectangle class, starts at a point and uses two vectors for extents
 *
 * @author William Matrix Peckham
 */
public class Rectangle extends GeometricObject {

    /**
     * coordinate point
     */
    private final Point3D p0 = new Point3D(-1, 0, -1);

    /**
     * one directions extents
     */
    private final Vector3D a = new Vector3D(0, 0, 2);

    /**
     * other directions extents
     */
    private final Vector3D b = new Vector3D(2, 0, 0);

    /**
     * square of length along a
     */
    private double aLenSquared = 4;

    /**
     * square of length along b
     */
    private double bLenSquared = 4;

    /**
     * normal of rectangle
     */
    private final Normal normal = new Normal(0, 1, 0);

    /**
     * area of rectangle
     */
    private double area = 4;

    /**
     * inverse of area
     */
    private double invArea = 0.25;

    /**
     * sampler if this is an area light
     */
    private Sampler sampler = null;

    /**
     * default constructor
     */
    public Rectangle() {
        super();
    }

    /**
     * initializing constructor
     *
     * @param p
     * @param a
     * @param b
     */
    public Rectangle(Point3D p, Vector3D a, Vector3D b) {
        super();
        p0.setTo(p);
        this.a.setTo(a);
        this.b.setTo(b);
        aLenSquared = a.lenSquared();
        bLenSquared = b.lenSquared();
        area = a.length() * b.length();
        invArea = 1 / area;
        sampler = null;
        normal.setTo(a.cross(b));
        normal.normalize();
    }

    /**
     * another initializing constructor, this one takes the normal too which
     * prevents it from being recalculated if the calling code already has
     *
     * @param p
     * @param a
     * @param b
     * @param n
     */
    public Rectangle(Point3D p, Vector3D a, Vector3D b, Normal n) {
        super();
        p0.setTo(p);
        this.a.setTo(a);
        this.b.setTo(b);
        area = a.length() * b.length();
        aLenSquared = a.lenSquared();
        bLenSquared = b.lenSquared();
        invArea = 1 / area;
        sampler = null;
        normal.setTo(n);
        normal.normalize();
    }

    /**
     * copy constructor
     *
     * @param r
     */
    public Rectangle(Rectangle r) {
        super(r);
        p0.setTo(r.p0);
        this.a.setTo(r.a);
        this.b.setTo(r.b);
        aLenSquared = r.aLenSquared;
        bLenSquared = r.bLenSquared;
        area = r.area;
        invArea = r.invArea;
        sampler = null;
        normal.setTo(r.normal);
        normal.normalize();
        if (r.sampler != null) {
            sampler = r.sampler.cloneSampler();
        }
    }

    /**
     * gets a bounding box
     *
     * @return
     */
    @Override
    public BBox getBoundingBox() {
        double delta = 0.0001;

        return (new BBox(Math.min(p0.x, p0.x + a.x + b.x) - delta, Math.
                max(p0.x, p0.x + a.x + b.x) + delta,
                Math.min(p0.y, p0.y + a.y + b.y) - delta, Math.max(p0.y, p0.y
                        + a.y + b.y) + delta,
                Math.min(p0.z, p0.z + a.z + b.z) - delta, Math.max(p0.z, p0.z
                        + a.z + b.z) + delta));
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public GeometricObject cloneGeometry() {
        return new Rectangle(this);
    }

    /**
     * hit method
     *
     * @param ray
     * @param s
     * @return
     */
    @Override
    public boolean hit(Ray ray, ShadeRec s) {
        //plane intersection parameter
        double t = p0.sub(ray.o).dot(normal) / ray.d.dot(normal);
        if (t <= Utility.EPSILON) {
            return false;
        }

        //hit point
        Point3D p = ray.o.add(ray.d.mul(t));

        //from point on plane to hit point
        Vector3D d = p.sub(p0);

        //project onto a
        double ddota = d.dot(a);
        //we're outside of the a direction
        if (ddota < 0 || ddota > aLenSquared) {
            return false;
        }

        //project onto b
        double ddotb = d.dot(b);
        //we're outside of the b direction
        if (ddotb < 0 || ddotb > bLenSquared) {
            return false;
        }
        //if we're here we hit and set shaderec up
        s.lastT = t;
        s.normal.setTo(normal);
        s.localHitPosition.setTo(p);
        return true;
    }

    /**
     * hit method
     *
     * @param ray
     * @param s
     * @return
     */
    @Override
    public boolean hit(Ray ray, ArrayList<ShadeRec> hit, ShadeRec sr) {
        //plane intersection parameter
        double dot = ray.d.dot(normal);
        if (dot == 0) {
            return false;
        }
        double t = p0.sub(ray.o).dot(normal) / dot;

        //hit point
        Point3D p = ray.o.add(ray.d.mul(t));

        //from point on plane to hit point
        Vector3D d = p.sub(p0);

        //project onto a
        double ddota = d.dot(a);
        //we're outside of the a direction
        if (ddota < 0 || ddota > aLenSquared) {
            return false;
        }

        //project onto b
        double ddotb = d.dot(b);
        //we're outside of the b direction
        if (ddotb < 0 || ddotb > bLenSquared) {
            return false;
        }
        //if we're here we hit and set shaderec up
        ShadeRec s = new ShadeRec(sr);
        s.lastT = t;
        s.normal.setTo(normal);
        s.localHitPosition.setTo(p);
        hit.add(s);
        return true;
    }

    /**
     * shadow hit function, works the same way as the hit function
     *
     * @param ray
     * @param tr
     * @return
     */
    @Override
    public boolean shadowHit(Ray ray, DoubleRef tr) {
        //early out if we don't cast shadows all implementations have this
        if (!shadows) {
            return false;
        }
        double t = p0.sub(ray.o).dot(normal) / ray.d.dot(normal);
        if (t <= Utility.EPSILON) {
            return false;
        }
        Point3D p = ray.o.add(ray.d.mul(t));
        Vector3D d = p.sub(p0);

        double ddota = d.dot(a);

        if (ddota < 0 || ddota > aLenSquared) {
            return false;
        }

        double ddotb = d.dot(b);

        if (ddotb < 0 || ddotb > bLenSquared) {
            return false;
        }

        tr.d = t;
        return true;
    }

    /**
     * sets a sampler
     *
     * @param s
     */
    public void setSampler(Sampler s) {
        sampler = s;
    }

    /**
     * samples the rectangle
     *
     * @return
     */
    @Override
    public Point3D sample() {
        Point2D samplePoint = sampler.sampleUnitSquare();
        return p0.add(a.mul(samplePoint.x).add(b.mul(samplePoint.y)));
    }

    /**
     * returns the normal, rectangle normals don't vary based on position.
     *
     * @param p
     * @return
     */
    @Override
    public Normal getNormal(Point3D p) {
        return normal;
    }

    @Override
    public double pdf(ShadeRec s) {
        return invArea;
    }

    private static final Logger LOG
            = Logger.getLogger(Rectangle.class.getName());

}
