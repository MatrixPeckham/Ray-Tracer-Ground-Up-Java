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

import com.matrixpeckham.raytracer.brdfs.SV_GlossySpecular;
import com.matrixpeckham.raytracer.textures.Texture;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Vector3D;
import java.util.logging.Logger;

/**
 * Textured glossy reflector
 *
 * @author William Matrix Peckham
 */
public class SV_GlossyReflector extends SV_Phong {

    /**
     * textured glossy specular
     */
    private SV_GlossySpecular glossySpecularBrdf = new SV_GlossySpecular();

    /**
     * default constructor
     */
    public SV_GlossyReflector() {
    }

    /**
     * copy constructor
     *
     * @param g
     */
    public SV_GlossyReflector(SV_GlossyReflector g) {
        super(g);
        if (g.glossySpecularBrdf != null) {
            glossySpecularBrdf = g.glossySpecularBrdf.cloneBRDF();
        }
    }

    /**
     * reflectance
     *
     * @param k
     */
    public void setKr(double k) {
        glossySpecularBrdf.setKs(k);
    }

    /**
     * setter
     *
     * @param ex
     */
    public void setExponent(double ex) {
        glossySpecularBrdf.setExp(ex);
    }

    /**
     * setter
     *
     * @param samples
     * @param exp
     */
    public void setSamples(int samples, double exp) {
        glossySpecularBrdf.setSamples(samples, exp);
    }

    /**
     * setter
     *
     * @param tex
     */
    public void setCr(Texture tex) {
        glossySpecularBrdf.setCs(tex);
    }

    /**
     * shade function, same as GlossyReflector.shade() except differs to
     * textures
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor shade(ShadeRec sr) {
        RGBColor L = super.shade(sr);
        Vector3D wo = sr.ray.d.neg();
        Vector3D wi = new Vector3D();
        DoubleRef pdf = new DoubleRef();
        RGBColor fr = glossySpecularBrdf.sampleF(sr, wo, wi, pdf);
        Ray reflectedRay = new Ray(sr.hitPoint, wi);
        L.addLocal(fr.mul(sr.w.tracer.traceRay(reflectedRay, sr.depth + 1)).mul(
                sr.normal.dot(wi) / pdf.d));

        return L; //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * path shade function same as GlossyReflector.pathShade() except differs to
     * textures
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

        return L; //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * global shade function same as GlossySpecular.globalShade() except differs
     * to textures
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

        return L; //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Material cloneMaterial() {
        return new SV_GlossyReflector(this);
    }

    private static final Logger LOG
            = Logger.getLogger(SV_GlossyReflector.class.getName());

}
