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

import com.matrixpeckham.raytracer.materials.Emissive;
import com.matrixpeckham.raytracer.materials.Material;
import com.matrixpeckham.raytracer.samplers.Sampler;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 * Environment light.
 *
 * @author William Matrix Peckham
 */
public class EnvironmentLight extends Light {

    /**
     * sampler
     */
    Sampler sampler;

    /**
     * material to use for light color
     */
    Material material;

    //uvw coordinate system at hit point 
    private ThreadLocal<Vector3D> u = new ThreadLocal<Vector3D>() {

        @Override
        protected Vector3D initialValue() {
            return new Vector3D(1, 0, 0);
        }

    };
    private ThreadLocal<Vector3D> v = new ThreadLocal<Vector3D>() {

        @Override
        protected Vector3D initialValue() {
            return new Vector3D(0, 1, 0);
        }

    };
    private ThreadLocal<Vector3D> w = new ThreadLocal<Vector3D>() {

        @Override
        protected Vector3D initialValue() {
            return new Vector3D(0, 0, 1);
        }

    };

    //sampled direction to light
    Vector3D wi = new Vector3D();

    /**
     * default constructor
     */
    public EnvironmentLight() {
    }

    /**
     * copy constructor
     *
     * @param l
     */
    public EnvironmentLight(EnvironmentLight l) {
        sampler = l.sampler.clone();
        material = l.material.clone();
        u.get().setTo(l.u.get());
        v.get().setTo(l.v.get());
        w.get().setTo(l.w.get());
        wi.setTo(l.wi);
    }

    /**
     * sets sampler
     *
     * @param sampler
     */
    public void setSampler(Sampler sampler) {
        this.sampler = sampler.clone();
        this.sampler.mapSamplesToHemisphere(1);
    }

    /**
     * clone method
     *
     * @return
     */
    @Override
    public Light clone() {
        return new EnvironmentLight(this);
    }

    /**
     * samples the direction
     *
     * @param sr
     * @return
     */
    @Override
    public Vector3D getDirection(ShadeRec sr) {
        w.get().setTo(sr.normal);
        v.get().setTo(w.get().cross(new Vector3D(0.0034, 1, 0.0071)));
        v.get().normalize();
        u.get().setTo(v.get().cross(w.get()));
        Point3D sp = sampler.sampleHemisphere();
        wi.setTo(u.get().mul(sp.x).add(v.get().mul(sp.y)).add(w.get().mul(sp.z)));
        return wi;
    }

    /**
     * same as ambient occlusion, if the ray hits anything that casts shadows
     * return true
     *
     * @param shadowRay
     * @param sr
     * @return
     */
    @Override
    public boolean inShadow(Ray shadowRay, ShadeRec sr) {
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
     * samples material for light color at hit location
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor L(ShadeRec sr) {
        return material.getLe(sr);
    }

    /**
     * pdf function, cosine term over pi
     *
     * @param sr
     * @return
     */
    @Override
    public double pdf(ShadeRec sr) {
        return sr.normal.dot(wi) * Utility.INV_PI;
    }

    /**
     * sets the material to use
     *
     * @param m
     */
    public void setMaterial(Material m) {
        material = m.clone();
    }

}
