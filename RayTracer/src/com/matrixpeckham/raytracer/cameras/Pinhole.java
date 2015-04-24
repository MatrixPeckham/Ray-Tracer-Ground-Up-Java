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
package com.matrixpeckham.raytracer.cameras;

import com.matrixpeckham.raytracer.util.Point2D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.Vector3D;
import com.matrixpeckham.raytracer.world.ViewPlane;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class Pinhole extends Camera {
    private double d;//view plane dist
    private double zoom;//zoom factor
    
    public Pinhole(){
        super();
        d=500;
        zoom=1.0f;
    }
    
    public Pinhole(Pinhole c){
        super(c);
        d=c.d;
        zoom=c.zoom;
    }
    
    @Override
    public Camera clone(){
        return new Pinhole(this);
    }
    
    public Vector3D getDirection(Point2D p){
        Vector3D dir = (u.mul(p.x)).add(v.mul(p.y)).sub(w.mul(d));
        dir.normalize();
        return dir;
    }
    
    @Override
    public void renderScene(World w){
        RGBColor L = new RGBColor();
        ViewPlane vp=new ViewPlane(w.vp);
        Ray ray = new Ray();
        int depth=0;
        Point2D pp = new Point2D();
        vp.s/=zoom;
        ray.o.setTo(eye);
        
        for(int r = 0; r<vp.vRes; r++){
            for(int c = 0; c<vp.hRes; c++){
                L.setTo(0, 0, 0);
                for(int p = 0; p<vp.numSamples; p++){
                    Point2D sp = vp.sampler.sampleUnitSquare();
                    pp.x=vp.s*(c-0.5f*vp.hRes + sp.x);
                    pp.y=vp.s*(r-0.5f*vp.vRes + sp.y);
                    ray.d=getDirection(pp);
                    L.addLocal(w.tracer.traceRay(ray,depth));
                }
                L.divLocal(vp.numSamples);
                L.mulLocal(exposureTime);
                w.displayPixel(r, c, L);
            }
        }
        
    }
    
    
    public void setViewDistance(double d){
        this.d=d;
    }
    public void setZoom(double zoom){
        this.zoom=zoom;
    }

    public void setUpVector(int i, int i0, int i1) {
        up.setTo(i, i0, i1);
    }
}
