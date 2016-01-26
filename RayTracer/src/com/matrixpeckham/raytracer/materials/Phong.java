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
 *
 * @author William Matrix Peckham
 */
public class Phong extends Material {

    protected Lambertian ambientBRDF;
    protected Lambertian diffuseBRDF;
    protected GlossySpecular specularBRDF;

    public Phong() {
        ambientBRDF = new Lambertian();
        diffuseBRDF = new Lambertian();
        specularBRDF = new GlossySpecular();
    }

    public Phong(Phong p) {
        super(p);
        ambientBRDF = p.ambientBRDF.clone();
        diffuseBRDF = p.diffuseBRDF.clone();
        specularBRDF = p.specularBRDF.clone();
    }

    @Override
    public Material clone() {
        return new Phong(this);
    }

    @Override
    public RGBColor shade(ShadeRec sr) {
        Vector3D wo = sr.ray.d.neg();
        RGBColor L = ambientBRDF.rho(sr, wo).mul(sr.w.ambient.L(sr));
        int numLights = sr.w.lights.size();
        for (int j = 0; j < numLights; j++) {
            Vector3D wi = sr.w.lights.get(j).getDirection(sr);
            double ndotwi = sr.normal.dot(wi);
            if(ndotwi > 0.0){
                boolean inShadow = false;
                if(sr.w.lights.get(j).castsShadows()){
                    Ray shadowRay=new Ray(sr.hitPoint,wi);
                    inShadow = sr.w.lights.get(j).inShadow(shadowRay,sr);
                }
                if (!inShadow||!shadow) {
                    L.addLocal(diffuseBRDF.f(sr, wo, wi).add(specularBRDF.f(sr, wo,
                            wi)).mul(sr.w.lights.get(j).
                                    L(sr)).mul(sr.w.lights.get(j).G(sr)*ndotwi/sr.w.lights.get(j).pdf(sr)));
                }
            } else {
                int breakable = (int)sr.lastT;
            }
        }
        return L;
    }

    public void setKa(double ka) {
        ambientBRDF.setKa(ka);
    }

    public void setKd(double kd) {
        diffuseBRDF.setKd(kd);
    }

    public void setKs(double ks) {
        specularBRDF.setKs(ks);
    }
    public void setCs(RGBColor cs) {
        specularBRDF.setCs(cs);
    }

    public void setExp(double exp) {
        specularBRDF.setExp(exp);
    }

    public void setCd(double r, double g, double b){
        setCd(new RGBColor(r,g,b));
    }
    
    public void setCd(double brown) {
        setCd(new RGBColor(brown,brown,brown));
    }
    public void setCd(RGBColor brown) {
        ambientBRDF.setCd(brown);
        diffuseBRDF.setCd(brown);
    }

    public void setCs(double d, double d0, double d1) {
        setCs(new RGBColor(d,d0,d1));
    }

    @Override
    public RGBColor pathShade(ShadeRec sr) {
        Vector3D wo = sr.ray.d.neg();
        RGBColor L = new RGBColor();
        Vector3D wiDiff = new Vector3D();
        Vector3D wiSpec = new Vector3D();
        DoubleRef pdfDiff = new DoubleRef();
        DoubleRef pdfSpec = new DoubleRef();
        
        RGBColor Ldiff = diffuseBRDF.sampleF(sr, wo, wiDiff, pdfDiff);
        RGBColor Lspec = specularBRDF.sampleF(sr, wo, wiSpec, pdfSpec);
        double ndotwiSpec = sr.normal.dot(wiSpec);
        double ndotwiDiff = sr.normal.dot(wiDiff);
        
        Ray diffRay = new Ray(sr.hitPoint,wiDiff);
        Ray specRay = new Ray(sr.hitPoint,wiSpec);
        
        Ldiff.setTo(Ldiff.mul(sr.w.tracer.traceRay(diffRay, sr.depth+1).mul(ndotwiDiff/pdfDiff.d)));
        Lspec.setTo(Lspec.mul(sr.w.tracer.traceRay(specRay, sr.depth+1).mul(ndotwiSpec/pdfSpec.d)));
        L.addLocal(Ldiff);
        L.addLocal(Lspec);
        return L;
    }

    @Override
    public RGBColor globalShade(ShadeRec sr) {
        Vector3D wo = sr.ray.d.neg();
        RGBColor L = new RGBColor();
        if(sr.depth==0)L.setTo(shade(sr));
        Vector3D wiDiff = new Vector3D();
        Vector3D wiSpec = new Vector3D();
        DoubleRef pdfDiff = new DoubleRef();
        DoubleRef pdfSpec = new DoubleRef();
        
        RGBColor Ldiff = diffuseBRDF.sampleF(sr, wo, wiDiff, pdfDiff);
        RGBColor Lspec = specularBRDF.sampleF(sr, wo, wiSpec, pdfSpec);
        double ndotwiSpec = sr.normal.dot(wiSpec);
        double ndotwiDiff = sr.normal.dot(wiDiff);
        
        Ray diffRay = new Ray(sr.hitPoint,wiDiff);
        Ray specRay = new Ray(sr.hitPoint,wiSpec);
        
        Ldiff.setTo(Ldiff.mul(sr.w.tracer.traceRay(diffRay, sr.depth+1).mul(ndotwiDiff/pdfDiff.d)));
        Lspec.setTo(Lspec.mul(sr.w.tracer.traceRay(specRay, sr.depth+1).mul(ndotwiSpec/pdfSpec.d)));
        L.addLocal(Ldiff);
        L.addLocal(Lspec);
        return L;
    }
}
