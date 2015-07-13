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
public class Matte extends Material {
    private Lambertian ambientBRDF;
    private Lambertian diffuseBRDF;
    
    
    public Matte(){
        super();
        ambientBRDF=new Lambertian();
        diffuseBRDF=new Lambertian();
    }
    
    public Matte(Matte m){
        super(m);
        if(m.ambientBRDF!=null){
            ambientBRDF=m.ambientBRDF.clone();
        } else {
            ambientBRDF=null;
        }
        if(m.diffuseBRDF!=null){
            diffuseBRDF=m.diffuseBRDF.clone();
        } else {
            diffuseBRDF=null;
        }
    }

    @Override
    public RGBColor shade(ShadeRec sr) {
        Vector3D wo = sr.ray.d.neg();
        RGBColor L = ambientBRDF.rho(sr,wo).mul(sr.w.ambient.L(sr));
        int numLights = sr.w.lights.size();
        for(int j = 0; j<numLights; j++){
            Vector3D wi=sr.w.lights.get(j).getDirection(sr);
            double ndotwi = sr.normal.dot(wi);
            if(ndotwi>0.0){
                boolean inShadow = false;
                if(sr.w.lights.get(j).castsShadows()){
                    Ray shadowRay=new Ray(sr.hitPoint,wi);
                    inShadow = sr.w.lights.get(j).inShadow(shadowRay,sr);
                }
                if(!inShadow){
                    L.addLocal(diffuseBRDF.f(sr,wo,wi).mul(sr.w.lights.get(j).L(sr)).mul(sr.w.lights.get(j).G(sr)*ndotwi/sr.w.lights.get(j).pdf(sr)));
                }
            }
        }
        return L;
    }

    @Override
    public RGBColor pathShade(ShadeRec sr) {
        Vector3D wo = sr.ray.d.neg();
        Vector3D wi = new Vector3D();
        DoubleRef pdf = new DoubleRef();
        RGBColor f = diffuseBRDF.sampleF(sr, wo, wi, pdf);
        double ndotwi=sr.normal.dot(wi);
        Ray reflectedRay = new Ray(sr.hitPoint, wi);
        return f.mul(sr.w.tracer.traceRay(reflectedRay, sr.depth+1).mul(ndotwi/pdf.d));
    }

    @Override
    public RGBColor globalShade(ShadeRec sr) {
        RGBColor L = new RGBColor();
        if(sr.depth==0)L.setTo(shade(sr));
        Vector3D wo = sr.ray.d.neg();
        Vector3D wi = new Vector3D();
        DoubleRef pdf = new DoubleRef();
        RGBColor f = diffuseBRDF.sampleF(sr, wo, wi, pdf);
        double ndotwi=sr.normal.dot(wi);
        Ray reflectedRay = new Ray(sr.hitPoint, wi);
        L.addLocal(f.mul(sr.w.tracer.traceRay(reflectedRay, sr.depth+1).mul(ndotwi/pdf.d)));
        return L;
    }
    
    
    
    @Override
    public Material clone() {
        return new Matte(this);
    }
    
    public void setKa(double ka){
        ambientBRDF.setKd(ka);
    }
    public void setKd(double kd){
        diffuseBRDF.setKd(kd);
    }
    public void setCd(RGBColor c){
        ambientBRDF.setCd(c);
        diffuseBRDF.setCd(c);
    }
    public void setCd(double r, double g, double b){
        ambientBRDF.setCd(r,g,b);
        diffuseBRDF.setCd(r,g,b);
    }
    public void setCd(double c){
        ambientBRDF.setCd(c);
        diffuseBRDF.setCd(c);
    }

    public void setSampler(Sampler sampler) {
        diffuseBRDF.setSampler(sampler.clone());
        
    }
}
