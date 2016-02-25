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
package com.matrixpeckham.raytracer.geometricobjects;

import com.matrixpeckham.raytracer.materials.Material;
import com.matrixpeckham.raytracer.samplers.Sampler;
import com.matrixpeckham.raytracer.util.BBox;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;

/**
 * Geometric Object abstract base class. This is the class all objects are based
 * around.
 *
 * @author William Matrix Peckham
 */
public abstract class GeometricObject {

    /**
     * color of the object, used only in the basic ray tracer from the early
     * chapters of the book
     */
    protected RGBColor color = new RGBColor();

    /**
     * material that this object is rendered with
     */
    protected Material material = null;

    /**
     * does this object cast a shadow
     */
    protected boolean shadows = true;

    /**
     * default constructor
     */
    public GeometricObject() {
    }

    /**
     * copy constructor
     *
     * @param object
     */
    public GeometricObject(GeometricObject object) {
        shadows = object.shadows;
        if (object.material != null) {
            material = object.material.clone();
        }
        color.setTo(object.color);
    }

    /**
     * clone method, should reproduce this object as a new one.
     *
     * @return
     */
    public abstract GeometricObject clone();

    /**
     * Hit function. All objects must override this. Implementing methods need
     * to check the ray for a hit, compute the ray parameter at the
     * intersection. find the normal at the intersection point.
     *
     * implementing functions store the hit ray parameter in ShadeRec.lastT
     *
     * they should store the normal in ShadeRec.normal
     *
     * they should store the hit point in ShadeRec.localHitPosition
     *
     * @param ray
     * @param s
     * @return
     */
    public abstract boolean hit(Ray ray, ShadeRec s);

    /**
     * sets the color of the object, only used for chapter five.
     *
     * @param c
     */
    public void setColor(RGBColor c) {
        color.setTo(c);
    }

    /**
     * sets the color of the object, only used for chapter five.
     *
     * @param r
     * @param g
     * @param b
     */
    public void setColor(double r, double g, double b) {
        color.setTo(r, g, b);
    }

    /**
     * gets the material this object should be rendered with
     *
     * @return
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * sets the material for this object
     *
     * @param mat
     */
    public void setMaterial(Material mat) {
        material = mat.clone();
    }

    /**
     * gets the color
     *
     * @return
     */
    public RGBColor getColor() {
        return color;
    }

    /**
     * gets the normal of the object at the hit point. only needs to be
     * overridden for objects that will be used as area lights
     *
     * @param p
     * @return
     */
    public Normal getNormal(Point3D p) {
        return new Normal();
    }

    /**
     * Probability density function, only needs to be overridden for objects
     * that will be area lights
     *
     * @param sr
     * @return
     */
    public double pdf(ShadeRec sr) {
        return 1;
    }

    /**
     * sample object, gets a sample point on the surface of the object, only
     * needs to be overridden by area lights
     *
     * @return
     */
    public Point3D sample() {
        return new Point3D(0);
    }

    /**
     * returns the bounding box of the object, called by compound and grid,
     * always culls object if not overridden. override if you plan to use inside
     * a grid or compound
     *
     * @return
     */
    public BBox getBoundingBox() {
        return new BBox();
    }

    /**
     * Shadow hit function, also checks an object for hit with a ray, but this
     * one doesn't need to compute hit point, or normal, so can sometimes be
     * implemented more efficiently than hit. this method is called more often
     * than hit so it is best if we can make it as fast as possible.
     *
     * It does still need to compute a ray parameter for the hit, and store it
     * in the DoubleRef.d in the DoubleRef passed to it.
     *
     * All implementations of this function should have if(!shadows) return
     * false; as the first statement. it's an early out for if we do not cast
     * shadows.
     *
     * @param ray
     * @param t
     * @return
     */
    public abstract boolean shadowHit(Ray ray, DoubleRef t);

    /**
     * sets if we cast shadows
     *
     * @param b
     */
    public void setShadows(boolean b) {
        shadows = b;
    }

    /**
     * returns if we cast shadows
     *
     * @return
     */
    public boolean castsShadows() {
        return shadows;
    }

}
