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

import com.matrixpeckham.raytracer.brdfs.GlossySpecular;
import com.matrixpeckham.raytracer.brdfs.Lambertian;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 * Phong material, has specular highlights. Usually makes surfaces appear
 * plastic-y.
 *
 * @author William Matrix Peckham
 */
public class Phong extends Material {

    /**
     * Ambient BRDF
     */
    protected Lambertian ambientBRDF;

    /**
     * diffuse BRDF
     */
    protected Lambertian diffuseBRDF;

    /**
     * Glossy Specular BRDF
     */
    protected GlossySpecular specularBRDF;

    /**
     * sets default
     */
    public Phong() {
        ambientBRDF = new Lambertian();
        diffuseBRDF = new Lambertian();
        specularBRDF = new GlossySpecular();
    }

    /**
     * copy constructor
     *
     * @param p
     */
    public Phong(Phong p) {
        super(p);
        ambientBRDF = p.ambientBRDF.clone();
        diffuseBRDF = p.diffuseBRDF.clone();
        specularBRDF = p.specularBRDF.clone();
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Material clone() {
        return new Phong(this);
    }

    /**
     * Shade function for phong. Direct lighting approach.
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor shade(ShadeRec sr) {
        //direction out (from hit point to camera)
        Vector3D wo = sr.ray.d.neg();

        //get ambient lighting.
        RGBColor L = ambientBRDF.rho(sr, wo).mul(sr.w.ambient.L(sr));

        //we loop through all the lights
        int numLights = sr.w.lights.size();
        for (int j = 0; j < numLights; j++) {

            //inoming light direction, direct direction to the light.
            Vector3D wi = sr.w.lights.get(j).getDirection(sr);

            //surface normal dot the light direction (cosine angle)
            double ndotwi = sr.normal.dot(wi);
            //correct side of surface
            if (ndotwi > 0.0) {
                boolean inShadow = false;
                //if the current light can cast a shadow
                if (sr.w.lights.get(j).castsShadows()) {
                    //create a ray from the hit point to the light
                    Ray shadowRay = new Ray(sr.hitPoint, wi);
                    //ask the current light if we're in shadow
                    inShadow = sr.w.lights.get(j).inShadow(shadowRay, sr);
                }
                //if we are not in shadow, or the material can't have shadows
                //cast on it. 
                if (!inShadow || !shadow) {
                    RGBColor diffuseColor = diffuseBRDF.f(sr, wo, wi);
                    RGBColor specularColor = specularBRDF.f(sr, wo, wi);
                    RGBColor illumination = sr.w.lights.get(j).L(sr);
                    double geometricFactor = sr.w.lights.get(j).G(sr);
                    double pdf = sr.w.lights.get(j).pdf(sr);
                    L.addLocal(
                            diffuseColor.add(specularColor).mul(illumination).
                            mul(geometricFactor * ndotwi / pdf)
                    );
                }
            }
        }
        return L;
    }

    /**
     * ambient weight
     *
     * @param ka
     */
    public void setKa(double ka) {
        ambientBRDF.setKa(ka);
    }

    /**
     * diffuse weight
     *
     * @param kd
     */
    public void setKd(double kd) {
        diffuseBRDF.setKd(kd);
    }

    /**
     * specular weight
     *
     * @param ks
     */
    public void setKs(double ks) {
        specularBRDF.setKs(ks);
    }

    /**
     * sets specular color
     *
     * @param cs
     */
    public void setCs(RGBColor cs) {
        specularBRDF.setCs(cs);
    }

    /**
     * set phong exponent
     *
     * @param exp
     */
    public void setExp(double exp) {
        specularBRDF.setExp(exp);
    }

    /**
     * set diffuse color
     *
     * @param r
     * @param g
     * @param b
     */
    public void setCd(double r, double g, double b) {
        setCd(new RGBColor(r, g, b));
    }

    /**
     * sets diffuse and ambient to gray
     *
     * @param brown
     */
    public void setCd(double brown) {
        setCd(new RGBColor(brown, brown, brown));
    }

    /**
     * set diffuse and ambient color
     *
     * @param brown
     */
    public void setCd(RGBColor brown) {
        ambientBRDF.setCd(brown);
        diffuseBRDF.setCd(brown);
    }

    /**
     * specular color
     *
     * @param d
     * @param d0
     * @param d1
     */
    public void setCs(double d, double d0, double d1) {
        setCs(new RGBColor(d, d0, d1));
    }

    /**
     * Implements phong path shading. Requires two recursive raycasts to
     * function.
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor pathShade(ShadeRec sr) {
        //direction ray is coming from

        Vector3D wo = sr.ray.d.neg();
        //out color
        RGBColor L = new RGBColor();

        //sample direction for diffuse shading
        Vector3D wiDiff = new Vector3D();

        //sample direction for specular shading
        Vector3D wiSpec = new Vector3D();

        //probability of direction being picked
        DoubleRef pdfDiff = new DoubleRef();
        DoubleRef pdfSpec = new DoubleRef();

        //material diffuse color
        RGBColor Ldiff = diffuseBRDF.sampleF(sr, wo, wiDiff, pdfDiff);

        //material specular color
        RGBColor Lspec = specularBRDF.sampleF(sr, wo, wiSpec, pdfSpec);

        //cosine terms
        double ndotwiSpec = sr.normal.dot(wiSpec);
        double ndotwiDiff = sr.normal.dot(wiDiff);

        //rays
        Ray diffRay = new Ray(sr.hitPoint, wiDiff);
        Ray specRay = new Ray(sr.hitPoint, wiSpec);

        //adds diffuse conribution
        Ldiff.setTo(Ldiff.mul(sr.w.tracer.traceRay(diffRay, sr.depth + 1).mul(
                ndotwiDiff / pdfDiff.d)));

        //adds specular contribution
        Lspec.setTo(Lspec.mul(sr.w.tracer.traceRay(specRay, sr.depth + 1).mul(
                ndotwiSpec / pdfSpec.d)));

        //add to final color
        L.addLocal(Ldiff);
        L.addLocal(Lspec);
        return L;
    }

    /**
     * Implements phong global shading.
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor globalShade(ShadeRec sr) {
        Vector3D wo = sr.ray.d.neg();
        RGBColor L = new RGBColor();

        //if depth is zero we direct shade the object, otherwise method is 
        //identical to pathshade
        if (sr.depth == 0) {
            L.setTo(shade(sr));
        }
        Vector3D wiDiff = new Vector3D();
        Vector3D wiSpec = new Vector3D();
        DoubleRef pdfDiff = new DoubleRef();
        DoubleRef pdfSpec = new DoubleRef();

        RGBColor Ldiff = diffuseBRDF.sampleF(sr, wo, wiDiff, pdfDiff);
        RGBColor Lspec = specularBRDF.sampleF(sr, wo, wiSpec, pdfSpec);
        double ndotwiSpec = sr.normal.dot(wiSpec);
        double ndotwiDiff = sr.normal.dot(wiDiff);

        Ray diffRay = new Ray(sr.hitPoint, wiDiff);
        Ray specRay = new Ray(sr.hitPoint, wiSpec);

        Ldiff.setTo(Ldiff.mul(sr.w.tracer.traceRay(diffRay, sr.depth + 1).mul(
                ndotwiDiff / pdfDiff.d)));
        Lspec.setTo(Lspec.mul(sr.w.tracer.traceRay(specRay, sr.depth + 1).mul(
                ndotwiSpec / pdfSpec.d)));
        L.addLocal(Ldiff);
        L.addLocal(Lspec);
        return L;
    }
}
