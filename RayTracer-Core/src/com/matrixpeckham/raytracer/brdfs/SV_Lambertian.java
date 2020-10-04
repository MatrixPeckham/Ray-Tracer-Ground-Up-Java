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
package com.matrixpeckham.raytracer.brdfs;

import com.matrixpeckham.raytracer.samplers.Sampler;
import com.matrixpeckham.raytracer.textures.Texture;
import com.matrixpeckham.raytracer.util.*;

/**
 * Lambertian texture for colors
 *
 * @author William Matrix Peckham
 */
public class SV_Lambertian extends BRDF {

    /**
     * multiplier
     */
    private double kd;

    /**
     * color texture
     */
    private Texture cd;

    /**
     * sampler for direction sampling
     */
    private Sampler sampler;

    /**
     * default constructor
     */
    public SV_Lambertian() {
        super();
        kd = 0;
    }

    /**
     * copy constructor
     *
     * @param lamb
     */
    public SV_Lambertian(SV_Lambertian lamb) {
        super(lamb);
        kd = lamb.kd;
        if (lamb.cd != null) {
            cd = lamb.cd.cloneTexture();
        }
        if (lamb.sampler != null) {
            sampler = lamb.sampler.cloneSampler();
        }
    }

    /**
     * sets the sampler
     *
     * @param clone
     */
    public void setSampler(Sampler clone) {
        this.sampler = clone.cloneSampler();
        this.sampler.mapSamplesToHemisphere(1);

    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public SV_Lambertian cloneBRDF() {
        return new SV_Lambertian(this);
    }

    /**
     * f function
     *
     * @param sr
     * @param wo
     * @param wi
     *
     * @return
     */
    @Override
    public RGBColor f(ShadeRec sr, Vector3D wo, Vector3D wi) {
        return cd.getColor(sr).mul(kd).mul(Utility.INV_PI);
    }

    /**
     * samples the distribution,returns color and stores reflected ray in wi,
     * and pdf in reference
     *
     * @param sr
     * @param wo
     * @param wi
     * @param pdf
     *
     * @return
     */
    @Override
    public RGBColor sampleF(ShadeRec sr, Vector3D wo, Vector3D wi, DoubleRef pdf) {
        Vector3D w = new Vector3D(sr.normal);
        Vector3D v = new Vector3D(0.0034, 1, 0.0071).cross(w);
        v.normalize();
        Vector3D u = v.cross(w);

        Point3D sp = sampler.sampleHemisphere();
        //wi = sp.x * u + sp.y * v + sp.z * w;
        wi.setTo(u.mul(sp.x).add(v.mul(sp.y).add(w.mul(sp.z))));
        wi.normalize();

        pdf.d = sr.normal.dot(wi) * Utility.INV_PI;

        return (cd.getColor(sr).mul(kd).mul(Utility.INV_PI));
    }

    /**
     * rho
     *
     * @param sr
     * @param wo
     *
     * @return
     */
    @Override
    public RGBColor rho(ShadeRec sr, Vector3D wo) {
        return cd.getColor(sr).mul(kd);
    }

    /**
     * setter
     *
     * @param kd
     */
    public void setKd(double kd) {
        this.kd = kd;
    }

    /**
     *
     * @param kd
     */
    public void setKa(double kd) {
        this.kd = kd;
    }

    /**
     * setter
     *
     * @param cd
     */
    public void setCd(Texture cd) {
        this.cd = cd;
    }

}
