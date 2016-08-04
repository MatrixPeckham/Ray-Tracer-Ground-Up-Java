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
package com.matrixpeckham.raytracer.materials;

import com.matrixpeckham.raytracer.brdfs.Lambertian;
import com.matrixpeckham.raytracer.samplers.Sampler;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Vector3D;
import java.util.logging.Logger;

/**
 * Perfectly Diffuse Matte Material.
 *
 * @author William Matrix Peckham
 */
public class Matte extends Material {

    /**
     * Ambient BRDF
     */
    private final Lambertian ambientBRDF;

    /**
     * Diffuse BRDF
     */
    private final Lambertian diffuseBRDF;

    /**
     * default constructor
     */
    public Matte() {
        super();
        ambientBRDF = new Lambertian();
        diffuseBRDF = new Lambertian();
    }

    /**
     * copy constructor
     *
     * @param m
     */
    public Matte(Matte m) {
        super(m);
        if (m.ambientBRDF != null) {
            ambientBRDF = m.ambientBRDF.cloneBRDF();
        } else {
            ambientBRDF = null;
        }
        if (m.diffuseBRDF != null) {
            diffuseBRDF = m.diffuseBRDF.cloneBRDF();
        } else {
            diffuseBRDF = null;
        }
    }

    /**
     * Shade function implementation for perfect diffuse
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor shade(ShadeRec sr) {

        //direction from camera
        Vector3D wo = sr.ray.d.neg();

        //ambient color
        RGBColor L = ambientBRDF.rho(sr, wo).mul(sr.w.ambient.L(sr));
        int numLights = sr.w.lights.size();

        //loop through lights
        for (int j = 0; j < numLights; j++) {
            //direction to light
            Vector3D wi = sr.w.lights.get(j).getDirection(sr);

            //cosine term
            double ndotwi = sr.normal.dot(wi);
            //front face
            if (ndotwi > 0.0) {
                //shadow cast
                boolean inShadow = false;
                if (sr.w.lights.get(j).castsShadows()) {
                    Ray shadowRay = new Ray(sr.hitPoint, wi);
                    inShadow = sr.w.lights.get(j).inShadow(shadowRay, sr);
                }
                if (!inShadow || !shadow) {
                    //not in shadow: diffuseColor * light * geometricTerm * cosine / pdf
                    L.addLocal(diffuseBRDF.f(sr, wo, wi).mul(sr.w.lights.get(j).
                            L(sr)).mul(sr.w.lights.get(j).G(sr) * ndotwi
                                    / sr.w.lights.get(j).pdf(sr)));
                }
            }
        }
        return L;
    }

    /**
     * Path shade implementation
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor pathShade(ShadeRec sr) {
        //ray from camera
        Vector3D wo = sr.ray.d.neg();

        //sample ray
        Vector3D wi = new Vector3D();

        //sample pdf
        DoubleRef pdf = new DoubleRef();

        //sample
        RGBColor f = diffuseBRDF.sampleF(sr, wo, wi, pdf);

        //cos term
        double ndotwi = sr.normal.dot(wi);

        //sample ray
        Ray reflectedRay = new Ray(sr.hitPoint, wi);

        //recursive trace and color calculation.
        return f.mul(sr.w.tracer.traceRay(reflectedRay, sr.depth + 1)).mul(
                ndotwi / pdf.d);
    }

    /**
     * global shade
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor globalShade(ShadeRec sr) {
        RGBColor L = new RGBColor();
        //method is identical to path shade except depth 0 direct illumiation
        if (sr.depth == 0) {
            L.setTo(shade(sr));
        }
        Vector3D wo = sr.ray.d.neg();
        Vector3D wi = new Vector3D();
        DoubleRef pdf = new DoubleRef();
        RGBColor f = diffuseBRDF.sampleF(sr, wo, wi, pdf);
        double ndotwi = sr.normal.dot(wi);
        Ray reflectedRay = new Ray(sr.hitPoint, wi);
        L.addLocal(f.mul(sr.w.tracer.traceRay(reflectedRay, sr.depth + 1).mul(
                ndotwi / pdf.d)));
        return L;
    }

    @Override
    public Material cloneMaterial() {
        return new Matte(this);
    }

    /**
     *
     * @param ka
     */
    public void setKa(double ka) {
        ambientBRDF.setKd(ka);
    }

    /**
     *
     * @param kd
     */
    public void setKd(double kd) {
        diffuseBRDF.setKd(kd);
    }

    /**
     *
     * @param c
     */
    public void setCd(RGBColor c) {
        ambientBRDF.setCd(c);
        diffuseBRDF.setCd(c);
    }

    /**
     *
     * @param r
     * @param g
     * @param b
     */
    public void setCd(double r, double g, double b) {
        ambientBRDF.setCd(r, g, b);
        diffuseBRDF.setCd(r, g, b);
    }

    /**
     *
     * @param c
     */
    public void setCd(double c) {
        ambientBRDF.setCd(c);
        diffuseBRDF.setCd(c);
    }

    /**
     *
     * @param sampler
     */
    public void setSampler(Sampler sampler) {
        diffuseBRDF.setSampler(sampler);

    }

    private static final Logger LOG = Logger.getLogger(Matte.class.getName());

}
