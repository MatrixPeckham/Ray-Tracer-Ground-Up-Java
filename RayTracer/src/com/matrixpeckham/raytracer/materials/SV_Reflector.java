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
import com.matrixpeckham.raytracer.brdfs.SV_PerfectSpecular;
import com.matrixpeckham.raytracer.textures.Texture;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 *
 * @author William Matrix Peckham
 */
public class SV_Reflector extends Phong {
    private SV_PerfectSpecular perfectBRDF;

    public SV_Reflector() {
        super();
        perfectBRDF=new SV_PerfectSpecular();
    }
    public SV_Reflector(SV_Reflector r){
        super(r);
        perfectBRDF=r.perfectBRDF.clone();
    }

    @Override
    public Material clone() {
        return new SV_Reflector(this);
    }
    public void setCr(Texture c){
        perfectBRDF.setCr(c);
    }
    public void setKr(double k){
        perfectBRDF.setKr(k);
    }

    @Override
    public RGBColor shade(ShadeRec sr) {
        RGBColor L = super.shade(sr);
        Vector3D wo = sr.ray.d.neg();
        Vector3D wi = new Vector3D();
        
        RGBColor fr = perfectBRDF.sampleF(sr, wo, wi);
        Ray reflectedRay = new Ray(sr.hitPoint,wi);
        
        L.addLocal(fr.mul(sr.w.tracer.traceRay(reflectedRay, sr.depth+1).mul(sr.normal.dot(wi))));
        
        return L;
    }

    @Override
    public RGBColor pathShade(ShadeRec sr) {
        RGBColor L = new RGBColor();
        Vector3D wo = sr.ray.d.neg();
        Vector3D wi = new Vector3D();
        
        RGBColor fr = perfectBRDF.sampleF(sr, wo, wi);
        Ray reflectedRay = new Ray(sr.hitPoint,wi);
        
        L.addLocal(fr.mul(sr.w.tracer.traceRay(reflectedRay, sr.depth+1).mul(sr.normal.dot(wi))));
        
        return L;
    }

    @Override
    public RGBColor globalShade(ShadeRec sr) {
        RGBColor L = new RGBColor();
        Vector3D wo = sr.ray.d.neg();
        Vector3D wi = new Vector3D();
        
        RGBColor fr = perfectBRDF.sampleF(sr, wo, wi);
        Ray reflectedRay = new Ray(sr.hitPoint,wi);
        if(sr.depth==0){
            L.addLocal(fr.mul(sr.w.tracer.traceRay(reflectedRay, sr.depth+2).mul(sr.normal.dot(wi))));
        } else {
            L.addLocal(fr.mul(sr.w.tracer.traceRay(reflectedRay, sr.depth+1).mul(sr.normal.dot(wi))));
        }
        
        return L;
    }
    
    

    
}
