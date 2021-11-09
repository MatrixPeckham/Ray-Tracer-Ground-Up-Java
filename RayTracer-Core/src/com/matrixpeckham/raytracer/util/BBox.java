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
package com.matrixpeckham.raytracer.util;

import java.util.logging.Logger;

/**
 * Bounding box class.
 *
 * @author William Matrix Peckham
 */
public class BBox {

    /**
     * low x
     */
    public double x0 = -1;

    /**
     * low y
     */
    public double y0 = -1;

    /**
     * low z
     */
    public double z0 = -1;

    /**
     * high x
     */
    public double x1 = 1;

    /**
     * high y
     */
    public double y1 = 1;

    /**
     * high z
     */
    public double z1 = 1;

    /**
     * Default constructor
     */
    public BBox() {
    }

    /**
     * Initializing constructor
     *
     * @param x0
     * @param x1
     * @param y0
     * @param y1
     * @param z0
     * @param z1
     */
    public BBox(double x0, double x1, double y0, double y1, double z0, double z1) {
        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;
        this.z0 = z0;
        this.z1 = z1;
    }

    /**
     * Another initializing constructor
     *
     * @param p0
     * @param p1
     */
    public BBox(Point3D p0, Point3D p1) {
        x0 = p0.x;
        y0 = p0.y;
        z0 = p0.z;
        x1 = p1.x;
        y1 = p1.y;
        z1 = p1.z;
    }

    /**
     * Copy Constructor
     *
     * @param b
     */
    public BBox(BBox b) {
        this.x0 = b.x0;
        this.x1 = b.x1;
        this.y0 = b.y0;
        this.y1 = b.y1;
        this.z0 = b.z0;
        this.z1 = b.z1;
    }

    /**
     * Checks ray/box intersection
     *
     * @param ray
     *
     * @return
     */
    public boolean hit(Ray ray) {
        double ox = ray.o.x;
        double oy = ray.o.y;
        double oz = ray.o.z;
        double dx = ray.d.x;
        double dy = ray.d.y;
        double dz = ray.d.z;

        double tx_min, ty_min, tz_min;
        double tx_max, ty_max, tz_max;

        double a = 1.0 / dx;
        if (a >= 0) {
            tx_min = (x0 - ox) * a;
            tx_max = (x1 - ox) * a;
        } else {
            tx_min = (x1 - ox) * a;
            tx_max = (x0 - ox) * a;
        }

        double b = 1.0 / dy;
        if (b >= 0) {
            ty_min = (y0 - oy) * b;
            ty_max = (y1 - oy) * b;
        } else {
            ty_min = (y1 - oy) * b;
            ty_max = (y0 - oy) * b;
        }

        double c = 1.0 / dz;
        if (c >= 0) {
            tz_min = (z0 - oz) * c;
            tz_max = (z1 - oz) * c;
        } else {
            tz_min = (z1 - oz) * c;
            tz_max = (z0 - oz) * c;
        }

        double t0, t1;

        // find largest entering t value
        if (tx_min > ty_min) {
            t0 = tx_min;
        } else {
            t0 = ty_min;
        }

        if (tz_min > t0) {
            t0 = tz_min;
        }

        // find smallest exiting t value
        if (tx_max < ty_max) {
            t1 = tx_max;
        } else {
            t1 = ty_max;
        }

        if (tz_max < t1) {
            t1 = tz_max;
        }

        return (t0 < t1 && t1 > Utility.EPSILON);
    }

    /**
     * point in box check
     *
     * @param p
     *
     * @return
     */
    public boolean inside(Point3D p) {
        return ((p.x > x0 && p.x < x1) && (p.y > y0 && p.y < y1) && (p.z > z0
                && p.z < z1));
    }

    /**
     * sets this box to the other box
     *
     * @param b
     */
    public void setTo(BBox b) {
        this.x0 = b.x0;
        this.x1 = b.x1;
        this.y0 = b.y0;
        this.y1 = b.y1;
        this.z0 = b.z0;
        this.z1 = b.z1;
    }

    /**
     * expands box to contain its current contents and the new box
     *
     * @param bb
     */
    public void expandToFit(BBox bb) {
        if (bb.x0 < x0) {
            x0 = bb.x0;
        }
        if (bb.y0 < y0) {
            y0 = bb.y0;
        }
        if (bb.z0 < z0) {
            z0 = bb.z0;
        }
        if (bb.x1 > x1) {
            x1 = bb.x1;
        }
        if (bb.y1 > y1) {
            y1 = bb.y1;
        }
        if (bb.z1 > z1) {
            z1 = bb.z1;
        }
    }

    @Override
    public String toString() {
        return "(" + x0 + ", " + y0 + "," + z0 + "), (" + x1 + ", " + y1 + ","
                + z1 + ")";
    }

    private static final Logger LOG = Logger.getLogger(BBox.class.getName());

}
