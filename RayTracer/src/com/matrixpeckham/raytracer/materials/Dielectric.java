/*
 * Copyright (C) 2016 William Matrix Peckham
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

import com.matrixpeckham.raytracer.brdfs.FresnelReflector;
import com.matrixpeckham.raytracer.btdf.FresnelTransmitter;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 *
 * @author William Matrix Peckham
 */
public class Dielectric extends Phong {
    private RGBColor cfIn=new RGBColor();
    private RGBColor cfOut=new RGBColor();
    FresnelReflector fresnelBRDF;
    FresnelTransmitter fresnelBTDF;
    
    public Dielectric(){
        fresnelBRDF=new FresnelReflector();
        fresnelBTDF=new FresnelTransmitter();
    }
    
    public Dielectric(Dielectric cp){
        super(cp);
        fresnelBRDF=new FresnelReflector(cp.fresnelBRDF);
        fresnelBTDF=new FresnelTransmitter(cp.fresnelBTDF);
        cfIn.setTo(cp.cfIn);
        cfOut.setTo(cp.cfOut);
    }

    @Override
    public Material clone() {
        return new Dielectric(this); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    @Override
    public RGBColor shade(ShadeRec sr) {
        RGBColor L = super.shade(sr); //To change body of generated methods, choose Tools | Templates.
        
        Vector3D wi = new Vector3D();
        Vector3D wo = new Vector3D(sr.ray.d.neg());
        RGBColor fr = fresnelBRDF.sampleF(sr, wo, wi);
        Ray reflectedRay=new Ray(sr.hitPoint,wi);
        RGBColor Lr;
        RGBColor Lt;
        double ndotwi = sr.normal.dot(wi);
        DoubleRef t = new DoubleRef(Utility.HUGE_VALUE);
        if(fresnelBTDF.tir(sr)){//total internal reflection
            if(ndotwi<0){
                //inside
                Lr = sr.w.tracer.traceRay(reflectedRay,t, sr.depth+1);
                L.addLocal(cfIn.powc(t.d).mul(Lr));
            } else {
                //reflected outside
                Lr = sr.w.tracer.traceRay(reflectedRay,t, sr.depth+1);
                L.addLocal(cfOut.powc(t.d).mul(Lr));
            }
        } else {
            Vector3D wt = new Vector3D();
            RGBColor ft = fresnelBTDF.sampleF(sr, wo, wt);
            Ray transmittedRay = new Ray(sr.hitPoint,wt);
            double ndotwt = sr.normal.dot(wt);
            
            if(ndotwi<0){
                //reflect inside
                Lr=sr.w.tracer.traceRay(reflectedRay,t, sr.depth+1).mul(Math.abs(ndotwi)).mul(fr);
                L.addLocal(Lr.mul(cfIn.powc(t.d)));
                //transmit outside
                Lt=sr.w.tracer.traceRay(transmittedRay,t,sr.depth+1).mul(Math.abs(ndotwt)).mul(ft);
                L.addLocal(Lt.mul(cfOut.powc(t.d)));
            } else {
                //reflect outside
                Lr=(sr.w.tracer.traceRay(reflectedRay,t, sr.depth+1).mul(Math.abs(ndotwi)).mul(fr));
                L.addLocal(Lr.mul(cfOut.powc(t.d)));
                //transmit inside
                Lt=(sr.w.tracer.traceRay(transmittedRay,t,sr.depth+1).mul(Math.abs(ndotwt)).mul(ft));
                L.addLocal(Lt.mul(cfIn.powc(t.d)));
            }
            
        }
        
        return L;
    }

    public void setIorIn(double d) {
        fresnelBRDF.setIorIn(d);
        fresnelBTDF.setIorIn(d);
    }

    public void setIorOut(double d) {
        fresnelBRDF.setIorOut(d);
        fresnelBTDF.setIorOut(d);
    }

    public void setCfIn(RGBColor c) {
        cfIn.setTo(c);
    }

    public void setCfOut(RGBColor c) {
        cfOut.setTo(c);
    }

    public void setKt(double d) {
        fresnelBTDF.setKt(d);
    }
    
    
}
