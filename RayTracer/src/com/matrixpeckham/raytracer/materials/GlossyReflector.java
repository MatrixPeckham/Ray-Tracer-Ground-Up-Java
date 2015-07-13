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
import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.samplers.Sampler;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 *
 * @author William Matrix Peckham
 */
public class GlossyReflector extends Phong {
    private GlossySpecular glossySpecularBrdf = new GlossySpecular();

    public GlossyReflector(){}
    
    public GlossyReflector(GlossyReflector g) {
        super(g);
        if(g.glossySpecularBrdf!=null)glossySpecularBrdf=g.glossySpecularBrdf.clone();
    }
    
    public void setKr(double k){
        glossySpecularBrdf.setKs(k);
    }
    public void setExponent(double ex){
        glossySpecularBrdf.setExp(ex);
    }
    public void setSamples(int samples, double exp){
        glossySpecularBrdf.setSamples(samples, exp);
    }

    public void setCr(double d, double d0, double d1) {
        glossySpecularBrdf.setCs(d, d0, d1);
    }

    @Override
    public RGBColor shade(ShadeRec sr) {
        RGBColor L = super.shade(sr);
        Vector3D wo = sr.ray.d.neg();
        Vector3D wi = new Vector3D();
        DoubleRef pdf=new DoubleRef();
        RGBColor fr = glossySpecularBrdf.sampleF(sr, wo, wi, pdf);
        Ray reflectedRay = new Ray(sr.hitPoint, wi);
        L.addLocal(fr.mul(sr.w.tracer.traceRay(reflectedRay, sr.depth+1)).mul(
                sr.normal.dot(wi)/pdf.d));
        
        return L; //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public RGBColor pathShade(ShadeRec sr) {
        RGBColor L = super.pathShade(sr);
        Vector3D wo = sr.ray.d.neg();
        Vector3D wi = new Vector3D();
        DoubleRef pdf=new DoubleRef();
        RGBColor fr = glossySpecularBrdf.sampleF(sr, wo, wi, pdf);
        Ray reflectedRay = new Ray(sr.hitPoint, wi);
        L.addLocal(fr.mul(sr.w.tracer.traceRay(reflectedRay, sr.depth+1)).mul(
                sr.normal.dot(wi)/pdf.d));
        
        return L; //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public RGBColor globalShade(ShadeRec sr) {
        RGBColor L = super.globalShade(sr);
        Vector3D wo = sr.ray.d.neg();
        Vector3D wi = new Vector3D();
        DoubleRef pdf=new DoubleRef();
        RGBColor fr = glossySpecularBrdf.sampleF(sr, wo, wi, pdf);
        Ray reflectedRay = new Ray(sr.hitPoint, wi);
        L.addLocal(fr.mul(sr.w.tracer.traceRay(reflectedRay, sr.depth+1)).mul(
                sr.normal.dot(wi)/pdf.d));
        
        return L; //To change body of generated methods, choose Tools | Templates.
    }

    public void setCr(RGBColor c) {
        glossySpecularBrdf.setCs(c);
    }

    @Override
    public Material clone() {
        return new GlossyReflector(this);
    }

    public void setSampler(Sampler multiJittered, double exp1) {
        glossySpecularBrdf.setSampler(multiJittered, exp1);
    }
    
    
    
}