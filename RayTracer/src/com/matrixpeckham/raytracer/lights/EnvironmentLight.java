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

import com.matrixpeckham.raytracer.materials.Emissive;
import com.matrixpeckham.raytracer.materials.Material;
import com.matrixpeckham.raytracer.samplers.Sampler;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 *
 * @author William Matrix Peckham
 */
public class EnvironmentLight extends Light{
    
    Sampler sampler;
    Material material;
    Vector3D u=new Vector3D(1, 0, 0);
    Vector3D v=new Vector3D(0, 1, 0);
    Vector3D w=new Vector3D(0, 0, 1);
    Vector3D wi=new Vector3D();
    
    public EnvironmentLight(){}
    
    public EnvironmentLight(EnvironmentLight l){
        sampler=l.sampler.clone();
        material=l.material.clone();
        u.setTo(l.u);
        v.setTo(l.v);
        w.setTo(l.w);
        wi.setTo(l.wi);
    }

    public void setSampler(Sampler sampler) {
        this.sampler = sampler.clone();
        this.sampler.mapSamplesToHemisphere(1);
    }
    
    

    @Override
    public Light clone() {
        return new EnvironmentLight(this);
    }

    @Override
    public Vector3D getDirection(ShadeRec sr) {
        w.setTo(sr.normal);
        v.setTo(w.cross(new Vector3D(0.0034,1,0.0071)));
        v.normalize();
        u.setTo(v.cross(w));
        Point3D sp=sampler.sampleHemisphere();
        wi.setTo(u.mul(sp.x).add(v.mul(sp.y)).add(w.mul(sp.z)));
        return wi;
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

    @Override
    public RGBColor L(ShadeRec sr) {
        return material.getLe(sr);
    }

    @Override
    public double pdf(ShadeRec sr) {
        return sr.normal.dot(wi)*Utility.INV_PI;
    }

    public void setMaterial(Material m) {
        material=m.clone();
    }
    
    
    
}
