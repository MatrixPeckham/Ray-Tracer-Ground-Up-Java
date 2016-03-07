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
import com.matrixpeckham.raytracer.samplers.Sampler;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Vector3D;
import java.util.logging.Logger;

/**
 * Glossy reflective material, imperfect blurry reflections
 *
 * @author William Matrix Peckham
 */
public class GlossyReflector extends Phong {

    /**
     * BRDF for reflection
     */
    private GlossySpecular glossySpecularBrdf = new GlossySpecular();

    /**
     * default constructor
     */
    public GlossyReflector() {
    }

    /**
     * copy constructor
     *
     * @param g
     */
    public GlossyReflector(GlossyReflector g) {
        super(g);
        if (g.glossySpecularBrdf != null) {
            glossySpecularBrdf = g.glossySpecularBrdf.cloneBRDF();
        }
    }

    /**
     * sets reflective multiplier
     *
     * @param k
     */
    public void setKr(double k) {
        glossySpecularBrdf.setKs(k);
    }

    /**
     * reflective exponent
     *
     * @param ex
     */
    public void setExponent(double ex) {
        glossySpecularBrdf.setExp(ex);
    }

    /**
     * sets sampler to new multijittered. with exponent
     *
     * @param samples
     * @param exp
     */
    public void setSamples(int samples, double exp) {
        glossySpecularBrdf.setSamples(samples, exp);
    }

    /**
     * sets reflective color
     *
     * @param d
     * @param d0
     * @param d1
     */
    public void setCr(double d, double d0, double d1) {
        glossySpecularBrdf.setCs(d, d0, d1);
    }

    /**
     * shade function
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor shade(ShadeRec sr) {

        //gets phong shading
        RGBColor L = super.shade(sr);

        //camera direction
        Vector3D wo = sr.ray.d.neg();

        //reflected sample direction
        Vector3D wi = new Vector3D();

        //sample pdf
        DoubleRef pdf = new DoubleRef();

        //sample brdf
        RGBColor fr = glossySpecularBrdf.sampleF(sr, wo, wi, pdf);

        //ray
        Ray reflectedRay = new Ray(sr.hitPoint, wi);

        //recursive trace and color computation
        L.addLocal(fr.mul(sr.w.tracer.traceRay(reflectedRay, sr.depth + 1)).mul(
                sr.normal.dot(wi) / pdf.d));

        return L;
    }

    /**
     * path shade function, for this is identical to shade function, but
     * differed to super path shade instead of shade.
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor pathShade(ShadeRec sr) {
        RGBColor L = super.pathShade(sr);
        Vector3D wo = sr.ray.d.neg();
        Vector3D wi = new Vector3D();
        DoubleRef pdf = new DoubleRef();
        RGBColor fr = glossySpecularBrdf.sampleF(sr, wo, wi, pdf);
        Ray reflectedRay = new Ray(sr.hitPoint, wi);
        L.addLocal(fr.mul(sr.w.tracer.traceRay(reflectedRay, sr.depth + 1)).mul(
                sr.normal.dot(wi) / pdf.d));

        return L;
    }

    /**
     * global shade function, same as shade function but differed to super
     * global shade instead of plain shade funcion
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor globalShade(ShadeRec sr) {
        RGBColor L = super.globalShade(sr);
        Vector3D wo = sr.ray.d.neg();
        Vector3D wi = new Vector3D();
        DoubleRef pdf = new DoubleRef();
        RGBColor fr = glossySpecularBrdf.sampleF(sr, wo, wi, pdf);
        Ray reflectedRay = new Ray(sr.hitPoint, wi);
        L.addLocal(fr.mul(sr.w.tracer.traceRay(reflectedRay, sr.depth + 1)).mul(
                sr.normal.dot(wi) / pdf.d));

        return L;
    }

    /**
     * setter
     *
     * @param c
     */
    public void setCr(RGBColor c) {
        glossySpecularBrdf.setCs(c);
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Material cloneMaterial() {
        return new GlossyReflector(this);
    }

    /**
     * sets sampler, and maps it to hemisphere
     *
     * @param multiJittered
     * @param exp1
     */
    public void setSampler(Sampler multiJittered, double exp1) {
        glossySpecularBrdf.setSampler(multiJittered, exp1);
    }

    private static final Logger LOG
            = Logger.getLogger(GlossyReflector.class.getName());

}
