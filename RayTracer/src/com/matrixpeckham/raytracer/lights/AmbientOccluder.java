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

import com.matrixpeckham.raytracer.samplers.Sampler;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 * Ambient Occluder class for ambient lighting that is more realistic than
 * regular ambient. This class changes the illumination based on how much of the
 * outside of the world the hit point has visibility to.
 *
 * @author William Matrix Peckham
 */
public class AmbientOccluder extends Light {

    /**
     * radiance
     */
    private double ls = 1;

    /**
     * light color
     */
    private RGBColor color = new RGBColor(1);

    /**
     * minimum radiance value, prevents truly unlit areas
     */
    private double minAmount = 0.25;

    //coordinate system u,v,w cached between method calls used for shadow calculations
    private final Vector3D u = new Vector3D();
    private final Vector3D v = new Vector3D();
    private final Vector3D w = new Vector3D();

    /**
     * sampler used to select shadow direction to sample
     */
    private Sampler sampler;

    /**
     * default constructor
     */
    public AmbientOccluder() {
    }

    /**
     * radiance
     *
     * @param ls
     */
    public void setLs(double ls) {
        this.ls = ls;
    }

    /**
     * light color
     *
     * @param color
     */
    public void setColor(RGBColor color) {
        this.color.setTo(color);
    }

    /**
     * set color
     *
     * @param r
     * @param g
     * @param b
     */
    public void setColor(double r, double g, double b) {
        this.color.setTo(r, g, b);
    }

    /**
     * sets color
     *
     * @param color
     */
    public void setColor(double color) {
        this.color.setTo(color, color, color);
    }

    /**
     * sets the minimum radiance
     *
     * @param minAmount
     */
    public void setMinAmount(double minAmount) {
        this.minAmount = minAmount;
    }

    /**
     * get radiance
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor L(ShadeRec sr) {

        //store uvw coordinte system of hit point
        w.setTo(sr.normal);
        v.setTo(w.cross(new Vector3D(0.0072, 1, 0.0034)));
        v.normalize();
        u.setTo(v.cross(w));

        //shadow ray
        Ray shadowRay = new Ray();
        shadowRay.o.setTo(sr.hitPoint);
        //direction
        shadowRay.d.setTo(getDirection(sr));

        //if we're in shadow we illuminate with minimum, otherwise max.
        //over the number of samples this gives soft shading 
        if (inShadow(shadowRay, sr)) {
            return color.mul(minAmount * ls);
        } else {
            return color.mul(ls);
        }

    }

    /**
     * copy constructor
     *
     * @param a
     */
    public AmbientOccluder(AmbientOccluder a) {
        this.ls = a.ls;
        color.setTo(a.color);
        minAmount = a.minAmount;
        if (a.sampler != null) {
            sampler = a.sampler.clone();
        }
    }

    /**
     * sets the sampler
     *
     * @param sampler
     */
    public void setSampler(Sampler sampler) {
        this.sampler = sampler;
        this.sampler.mapSamplesToHemisphere(1);
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Light clone() {
        return new AmbientOccluder(this);
    }

    /**
     * gets the direction to trace for shadow ray, overrides getDirection in
     * light, but will not be called like other lights because this will be in
     * the worlds ambient pointer and not in the lights array
     *
     * @param sr
     * @return
     */
    @Override
    public Vector3D getDirection(ShadeRec sr) {
        //samples the area aroun the hit point
        Point3D sp = sampler.sampleHemisphere();
        //transform sample point to a direction from the hit point
        return u.mul(sp.x).add(v.mul(sp.y)).add(w.mul(sp.z));
    }

    /**
     * in shadow function
     *
     * @param shadowRay
     * @param sr
     * @return
     */
    @Override
    public boolean inShadow(Ray shadowRay, ShadeRec sr) {
        //returns true if any object is hit and can cast shadows
        int numObjects = sr.w.objects.size();
        DoubleRef t = new DoubleRef();
        for (int j = 0; j < numObjects; j++) {
            if (sr.w.objects.get(j).shadowHit(shadowRay, t) && sr.material.
                    getShadows()) {
                return true;
            }
        }
        return false;
    }

    /**
     * sets radiance
     *
     * @param d
     */
    public void scaleRadiance(double d) {
        ls = d;
    }

}
