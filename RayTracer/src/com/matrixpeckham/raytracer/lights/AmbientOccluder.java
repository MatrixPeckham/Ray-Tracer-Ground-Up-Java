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
package com.matrixpeckham.raytracer.lights;

import com.matrixpeckham.raytracer.samplers.Sampler;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 *
 * @author William Matrix Peckham
 */
public class AmbientOccluder extends Light{
    private double ls = 1;
    private RGBColor color = new RGBColor(1);
    private double minAmount = 0.25;
    private Vector3D u = new Vector3D();
    private Vector3D v = new Vector3D();
    private Vector3D w = new Vector3D();
    private Sampler sampler;

    public AmbientOccluder() {
    }

    public void setLs(double ls) {
        this.ls = ls;
    }

    public void setColor(RGBColor color) {
        this.color.setTo(color);
    }
    public void setColor(double r, double g, double b) {
        this.color.setTo(r,g,b);
    }
    public void setColor(double color) {
        this.color.setTo(color,color,color);
    }

    public void setMinAmount(double minAmount) {
        this.minAmount = minAmount;
    }

    @Override
    public RGBColor L(ShadeRec sr) {
        w.setTo(sr.normal);
        v=w.cross(new Vector3D(0.0072, 1, 0.0034));
        v.normalize();
        u=v.cross(w);
        
        Ray shadowRay = new Ray();
        shadowRay.o.setTo(sr.hitPoint);
        shadowRay.d.setTo(getDirection(sr));
        if(inShadow(shadowRay, sr)){
            return color.mul(minAmount*ls);
        } else {
            return color.mul(ls);
        }
        
        
    }
    
    
    
    public AmbientOccluder(AmbientOccluder a){
        this.ls=a.ls;
        color.setTo(a.color);
        minAmount=a.minAmount;
        if(a.sampler!=null)
            sampler=a.sampler.clone();
    }
    
    public void setSampler(Sampler sampler){
        this.sampler=sampler;
        this.sampler.mapSamplesToHemisphere(1);
    }
    
    @Override
    public Light clone() {
        return new AmbientOccluder(this);
    }

    @Override
    public Vector3D getDirection(ShadeRec sr) {
        Point3D sp = sampler.sampleHemisphere();
        return u.mul(sp.x).add(v.mul(sp.y)).add(w.mul(sp.z));
    }
    
    

    @Override
    public boolean inShadow(Ray shadowRay, ShadeRec sr) {
        int numObjects = sr.w.objects.size();
        DoubleRef t = new DoubleRef();
        for(int j=0; j<numObjects; j++){
            if(sr.w.objects.get(j).shadowHit(shadowRay, t)&&sr.material.getShadows()){
                return true;
            }
        }
        return false;
    }

    public void scaleRadiance(double d) {
        ls=d;
    }
    
}
