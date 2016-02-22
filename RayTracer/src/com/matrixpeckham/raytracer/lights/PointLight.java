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
package com.matrixpeckham.raytracer.lights;

import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 * Point Light class.
 *
 * @author William Matrix Peckham
 */
public class PointLight extends Light {

    /**
     * radiance
     */
    private double ls;

    /**
     * attenuation exponent
     */
    private double exp;

    /**
     * light color
     */
    private RGBColor color;

    /**
     * point light location
     */
    Point3D location;

    /**
     * default constructor
     */
    public PointLight() {
        super();
        ls = 1;
        exp = 0;
        color = new RGBColor(1);
        location = new Point3D(0, 1, 0);
    }

    /**
     * copy constructor
     *
     * @param dl
     */
    public PointLight(PointLight dl) {
        super(dl);
        ls = dl.ls;
        exp = dl.exp;
        color = new RGBColor(dl.color);
        location = new Point3D(dl.location);
    }

    /**
     * sets the radiance
     *
     * @param b
     */
    public void scaleRadiance(double b) {
        ls = b;
    }

    /**
     * sets the attenuation exponent
     *
     * @param exp
     */
    public void setExp(double exp) {
        this.exp = exp;
    }

    /**
     * sets color gray
     *
     * @param c
     */
    public void setColor(double c) {
        color.r = c;
        color.g = c;
        color.b = c;
    }

    /**
     * sets the color
     *
     * @param c
     */
    public void setColor(RGBColor c) {
        color.setTo(c);
    }

    /**
     * sets the color
     *
     * @param r
     * @param g
     * @param b
     */
    public void setColor(double r, double g, double b) {
        color.setTo(r, g, b);
    }

    /**
     * sets location
     *
     * @param d
     */
    public void setLocation(Point3D d) {
        location.setTo(d);
    }

    /**
     * sets location
     *
     * @param dx
     * @param dy
     * @param dz
     */
    public void setLocation(double dx, double dy, double dz) {
        location.setTo(dx, dy, dz);
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Light clone() {
        return new PointLight(this);
    }

    /**
     * returns direction from hit point to light location
     *
     * @param sr
     * @return
     */
    @Override
    public Vector3D getDirection(ShadeRec sr) {
        return location.sub(sr.hitPoint).hat();
    }

    /**
     * returns the illumination provided by this light to the hit point. it's
     * the color times radiance over distance to the power of exponent
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor L(ShadeRec sr) {
        double dist = sr.hitPoint.distance(location);
        return color.mul(ls / Math.pow(dist, exp));
    }

    /**
     * in shadow function
     *
     * @param ray
     * @param sr
     * @return
     */
    @Override
    public boolean inShadow(Ray ray, ShadeRec sr) {
        //reference double for shadow hit function
        DoubleRef t = new DoubleRef(0);

        //distance from light to hit position
        double d = location.distance(ray.o);

        //loop though objects in scene
        int numObjects = sr.w.objects.size();
        for (int j = 0; j < numObjects; j++) {
            //shadow hit function returns distance through reference
            //if that point is closer than the light we're in shadow
            //also check if the material casts shadows
            if (sr.w.objects.get(j).shadowHit(ray, t) && t.d < d && sr.material.
                    getShadows()) {
                return true;
            }
        }
        return false;
    }

}
