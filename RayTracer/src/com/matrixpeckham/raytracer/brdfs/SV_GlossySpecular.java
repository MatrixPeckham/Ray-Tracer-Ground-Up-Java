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
package com.matrixpeckham.raytracer.brdfs;

import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.samplers.Sampler;
import com.matrixpeckham.raytracer.textures.Texture;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Vector3D;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;

/**
 *
 * @author William Matrix Peckham
 */
public class SV_GlossySpecular extends BRDF {
    private double ks=0;
    private Texture cs=null;
    private double exp=2;
    private Sampler sampler=null;
    
    public SV_GlossySpecular(){
        super();
    }
    
    public SV_GlossySpecular(SV_GlossySpecular gs){
        super(gs);
        ks=gs.ks;
        if(gs.cs!=null){
            cs=gs.cs.clone();
        }
        exp=gs.exp;
        if(gs.sampler!=null)
            sampler=gs.sampler.clone();
    }
    
    public SV_GlossySpecular clone(){
        return new SV_GlossySpecular(this);
    }
    
    public void setSampler(Sampler s, double exp){
        sampler=s;
        sampler.mapSamplesToHemisphere(exp);
    }
    
    public void setSamples(int num, double exp){
        sampler=new MultiJittered(num);
        sampler.mapSamplesToHemisphere(exp);
    }

    @Override
    public RGBColor f(ShadeRec sr, Vector3D wo, Vector3D wi) {
        RGBColor l = new RGBColor();
        double ndotwi = sr.normal.dot(wi);
        Vector3D r = wi.neg().add(new Vector3D(sr.normal.mul(2*ndotwi)));
        double rdotwo = r.dot(wo);
        if(rdotwo>0){
            l.setTo(cs.getColor(sr).mul(ks*Math.pow(rdotwo, exp)));
        }
        return l;
    }

    @Override
    public RGBColor sampleF(ShadeRec sr, Vector3D wo, Vector3D wi, DoubleRef pdf) {
        double ndotwo = sr.normal.dot(wo);
        Vector3D r = wo.neg().add(new Vector3D(sr.normal.mul(2*ndotwo)));
        Vector3D w = new Vector3D(r);
        Vector3D u = new Vector3D(0.00424,1,0.00764).cross(w);
        u.normalize();
        Vector3D v = u.cross(w);
        
        Point3D sp = sampler.sampleHemisphere();
        wi.setTo(u.mul(sp.x).add(v.mul(sp.y).add(w.mul(sp.z))));
        if(sr.normal.dot(wi)<0.0){
            wi.setTo(u.mul(-sp.x).add(v.mul(-sp.y).add(w.mul(sp.z))));
        }
        double phong_lobe = Math.pow(r.dot(w), exp);
        pdf.d=phong_lobe*sr.normal.dot(wi);
        return cs.getColor(sr).mul(ks*phong_lobe);
    }

    @Override
    public RGBColor rho(ShadeRec sr, Vector3D wo) {
        return Utility.BLACK;
    }
    
    
    
    public void setKs(double k){
        ks=k;
    }
    
    public void setExp(double e){
        exp=e;
    }
        
    public void setCs(Texture c){
        cs=c.clone();
    }
    
}
