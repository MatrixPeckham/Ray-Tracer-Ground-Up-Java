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
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
import java.util.logging.Logger;

/**
 * Axis Aligned Box class.
 *
 * @author William Matrix Peckham
 */
public class Box extends GeometricObject {

    /**
     * low x
     */
    public double x0 = -1;

    /**
     * low y
     */
    public double y0 = -1;

    /**
     * low x
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
     * get bounding box, a bit redundant for a box class, but necessary for
     * proper interaction with Grids
     *
     * @return
     */
    @Override
    public BBox getBoundingBox() {
        return new BBox(x0, x1, y0, y1, z0, z1); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * default constructor
     */
    public Box() {
    }

    /**
     * initializing constructor
     *
     * @param x0
     * @param x1
     * @param y0
     * @param y1
     * @param z0
     * @param z1
     */
    public Box(double x0, double x1, double y0, double y1, double z0, double z1) {
        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;
        this.z0 = z0;
        this.z1 = z1;
    }

    /**
     * initialize with corner points
     *
     * @param p0
     * @param p1
     */
    public Box(Point3D p0, Point3D p1) {
        x0 = p0.x;
        y0 = p0.y;
        z0 = p0.z;
        x1 = p1.x;
        y1 = p1.y;
        z1 = p1.z;
    }

    /**
     * copy constructor
     *
     * @param b
     */
    public Box(Box b) {
        this.x0 = b.x0;
        this.x1 = b.x1;
        this.y0 = b.y0;
        this.y1 = b.y1;
        this.z0 = b.z0;
        this.z1 = b.z1;
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

        //convienence variables
        double ox = ray.o.x;
        double oy = ray.o.y;
        double oz = ray.o.z;
        double dx = ray.d.x;
        double dy = ray.d.y;
        double dz = ray.d.z;

        //variables for ray parameters
        double tx_min, ty_min, tz_min;
        double tx_max, ty_max, tz_max;

        //calculate slab intersection times
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

        //find the times for entering and exiting the box and the faces
        double t0, t1;

        int face_in, face_out;

        // find largest entering t value
        if (tx_min > ty_min) {
            t0 = tx_min;
            face_in = (a >= 0.0) ? 0 : 3;
        } else {
            t0 = ty_min;
            face_in = (b >= 0.0) ? 1 : 4;
        }

        if (tz_min > t0) {
            t0 = tz_min;
            face_in = (c >= 0.0) ? 2 : 5;
        }

        // find smallest exiting t value
        if (tx_max < ty_max) {
            t1 = tx_max;
            face_out = (a >= 0.0) ? 3 : 0;
        } else {
            t1 = ty_max;
            face_out = (b >= 0.0) ? 4 : 1;
        }

        if (tz_max < t1) {
            t1 = tz_max;
            face_out = (c >= 0.0) ? 5 : 2;
        }

        //hit test and fills shaderec.
        if (t0 < t1 && t1 > Utility.EPSILON) {  // condition for a hit
            if (t0 > Utility.EPSILON) {
                sr.lastT = t0;  			// ray hits outside surface
                sr.normal.setTo(getNormal(face_in));
            } else {
                sr.lastT = t1;				// ray hits inside surface
                sr.normal.setTo(getNormal(face_out));
            }

            sr.localHitPosition.setTo(ray.o.add(Vector3D.mul(sr.lastT, ray.d)));
            return (true);
        } else {
            return (false);
        }
    }

    /**
     * private method for transforming an integer index into the proper normal
     *
     * @param i
     * @return
     */
    private Normal getNormal(int i) {
        switch (i) {
            case 0:
                return (new Normal(-1, 0, 0));	// -x face
            case 1:
                return (new Normal(0, -1, 0));	// -y face
            case 2:
                return (new Normal(0, 0, -1));	// -z face
            case 3:
                return (new Normal(1, 0, 0));	// +x face
            case 4:
                return (new Normal(0, 1, 0));	// +y face
            case 5:
                return (new Normal(0, 0, 1));	// +z face
            default:
                return new Normal();
        }
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public GeometricObject cloneGeometry() {
        return new Box(this);
    }

    /**
     * shadow hit function, same as the hit function but doesn't do normal
     * calculations
     *
     * @param ray
     * @param tr
     * @return
     */
    @Override
    public boolean shadowHit(Ray ray, DoubleRef tr) {
        //early out that all implementations have
        if (!shadows) {
            return false;
        }
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

        if (t0 < t1 && t1 > Utility.EPSILON) {  // condition for a hit
            if (t0 > Utility.EPSILON) {
                tr.d = t0;  			// ray hits outside surface
            } else {
                tr.d = t1;				// ray hits inside surface
            }
            return (true);
        } else {
            return (false);
        }
    }

    /**
     * setter
     *
     * @param d
     * @param i
     * @param d0
     */
    public void setP0(double d, double i, double d0) {
        x0 = d;
        y0 = i;
        z0 = d0;
    }

    /**
     * setter
     *
     * @param d
     * @param d0
     * @param d1
     */
    public void setP1(double d, double d0, double d1) {
        x1 = d;
        y1 = d0;
        z1 = d1;
    }

    private static final Logger LOG = Logger.getLogger(Box.class.getName());

}
