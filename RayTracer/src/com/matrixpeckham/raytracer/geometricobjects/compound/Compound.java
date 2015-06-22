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
package com.matrixpeckham.raytracer.geometricobjects.compound;

import com.matrixpeckham.raytracer.geometricobjects.GeometricObject;
import com.matrixpeckham.raytracer.geometricobjects.triangles.SmoothTriangle;
import com.matrixpeckham.raytracer.materials.Material;
import com.matrixpeckham.raytracer.util.BBox;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import java.util.ArrayList;

/**
 *
 * @author William Matrix Peckham
 */
public class Compound extends GeometricObject {
    protected ArrayList<GeometricObject> objects = new ArrayList<>();
    
    public Compound(){
        super();
    }

    public Compound(Compound c){
        copyObjects(c.objects);
    }
    
    @Override
    public GeometricObject clone() {
        return new Compound(this);
    }
    
    public void addObject(GeometricObject obj){
        objects.add(obj);
    }
    
    @Override
    public void setMaterial(Material mat){
        for(GeometricObject obj : objects){
            obj.setMaterial(mat);
        }
    }

    @Override
    public boolean hit(Ray ray, ShadeRec s) {
        Normal n = new Normal();
        Point3D localHitPoint = new Point3D();
        boolean hit = false;
        double tmin = Utility.HUGE_VALUE;
        int numObjects = objects.size();
        
        for(int j = 0; j<numObjects; j++){
            if(objects.get(j).hit(ray, s)&&s.lastT<tmin){
                hit=true;
                tmin=s.lastT;
                material=objects.get(j).getMaterial();
                n.setTo(s.normal);
                localHitPoint.setTo(s.localHitPosition);
            }
        }
        
        if(hit){
            s.t=tmin;
            s.lastT=tmin;
            s.normal.setTo(n);
            s.localHitPosition.setTo(localHitPoint);
        }
        
        return hit;
        
    }

    @Override
    public boolean shadowHit(Ray ray, DoubleRef t) {
        if(!shadows)return false;
        boolean hit = false;
        double tmin = Utility.HUGE_VALUE;
        int numObjects = objects.size();
        
        for(int j = 0; j<numObjects; j++){
            if(objects.get(j).shadowHit(ray, t)&&t.d<tmin){
                hit=true;
                tmin=t.d;
            }
        }
        
        if(hit){
            t.d=tmin;
        }
        
        return hit;
    }
    

    
    private void deleteObjects(){
        objects.clear();
    }
    
    private void copyObjects(ArrayList<GeometricObject> rhs){
        deleteObjects();
        for(GeometricObject obj : rhs){
            objects.add(obj.clone());
        }
    }
    
    
    
    
    
    
    
    public int getNumObjects(){
        return objects.size();
    }

    public BBox getBoundingBox() {
        return new BBox();
    }

    
}
