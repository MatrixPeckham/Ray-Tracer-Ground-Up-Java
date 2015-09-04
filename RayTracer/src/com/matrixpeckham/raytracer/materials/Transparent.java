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

import com.matrixpeckham.raytracer.brdfs.PerfectSpecular;
import com.matrixpeckham.raytracer.btdf.PerfectTransmitter;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 *
 * @author William Matrix Peckham
 */
public class Transparent extends Phong {
    private PerfectSpecular reflectiveBRDF;
    private PerfectTransmitter specularBTDF;

    public Transparent(){
        reflectiveBRDF=new PerfectSpecular();
        specularBTDF=new PerfectTransmitter();
    }
    public Transparent(Transparent t){
        super(t);
        reflectiveBRDF=t.reflectiveBRDF.clone();
        specularBTDF=t.specularBTDF.clone();
    }

    @Override
    public Material clone() {
        return new Transparent(this);
    }
    
    @Override
    public RGBColor shade(ShadeRec sr) {
        RGBColor L = new RGBColor(super.shade(sr));
        Vector3D wo = sr.ray.d.neg();
        Vector3D wi = new Vector3D();
        RGBColor fr = reflectiveBRDF.sampleF(sr, wo, wi);
        Ray reflectedRay = new Ray(sr.hitPoint, wi);
        if(specularBTDF.tir(sr)){
            L.addLocal(sr.w.tracer.traceRay(reflectedRay, sr.depth+1));
            //kr=1;
        } else {
            Vector3D wt = new Vector3D();
            RGBColor ft = specularBTDF.sampleF(sr, wo, wt);
            Ray transmittedRay = new Ray(sr.hitPoint, wt);
            L.addLocal(fr.mul(sr.w.tracer.traceRay(reflectedRay, sr.depth+1)).mul(Math.abs(sr.normal.dot(wi))));
            L.addLocal(ft.mul(sr.w.tracer.traceRay(transmittedRay, sr.depth+1)).mul(Math.abs(sr.normal.dot(wt))));
        }
        
        
        return L;
    }

    public void setIor(double d) {
        specularBTDF.setIor(d);
    }

    public void setKr(double d) {
        reflectiveBRDF.setKr(d);
    }

    public void setKt(double d) {
        specularBTDF.setKt(d);
    }

  
    
    
    
    
}
