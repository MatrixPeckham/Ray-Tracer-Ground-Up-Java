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

import com.matrixpeckham.raytracer.geometricobjects.GeometricObject;
import com.matrixpeckham.raytracer.materials.Material;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Normal;
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
public class AreaLight extends Light {
    private GeometricObject obj=null;
    private Material material=null;
    private Point3D samplePoint=new Point3D();
    private Normal lightNormal=new Normal();
    private Vector3D wi=new Vector3D();
    
    public AreaLight(){
        super();
    }
    public AreaLight(AreaLight a){
        super(a);
        if(a.obj!=null){
            obj=a.obj.clone();
        }
        if(a.material!=null){
            material=a.material.clone();
        }
    }
    
    public void setObject(GeometricObject obj){
        this.obj=obj;
        material=obj.getMaterial();
    }

    @Override
    public Light clone() {
        return new AreaLight(this);
    }

    @Override
    public Vector3D getDirection(ShadeRec sr) {
        samplePoint.setTo(obj.sample());
        lightNormal.setTo(obj.getNormal(samplePoint));
        wi.setTo(samplePoint.sub(sr.hitPoint));
        wi.normalize();
        return wi;
    }

    @Override
    public RGBColor L(ShadeRec sr) {
        double ndotd=lightNormal.neg().dot(wi);
        if(ndotd>0){
            return material.getLe(sr);
        } else return Utility.BLACK;
    }
    
    

    @Override
    public boolean inShadow(Ray ray, ShadeRec sr) {
        DoubleRef t = new DoubleRef();
        int numObjects = sr.w.objects.size();
        double ts = (samplePoint.sub(ray.o).dot(ray.d));
        for(int j = 0; j<numObjects; j++){
            if(sr.w.objects.get(j).shadowHit(ray, t)&&t.d<ts&&sr.material.getShadows()){
                return true;
            }
        }
        return false;
    }

    @Override
    public double G(ShadeRec sr) {
        double ndotd = lightNormal.neg().dot(wi);
        double d2 = samplePoint.distSquared(sr.hitPoint);
        return ndotd/d2;
    }

    @Override
    public double pdf(ShadeRec sr) {
        return obj.pdf(sr);
    }
    
    
    
}
