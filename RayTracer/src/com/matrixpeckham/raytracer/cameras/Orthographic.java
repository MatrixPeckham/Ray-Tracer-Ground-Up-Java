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
public class Orthographic extends Camera {
    
    public Orthographic(){
        super();
    }
    
    public Orthographic(Orthographic c){
        super(c);
    }
    
    @Override
    public Camera clone(){
        return new Orthographic(this);
    }
    
    public Vector3D getDirection(Point2D p){
        Vector3D dir = (lookat.sub(eye));
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
        Point2D sp = new Point2D();
        
        for(int r = 0; r<vp.vRes; r++){
            for(int c = 0; c<vp.hRes; c++){
                L.setTo(0, 0, 0);
                for(int p = 0; p<vp.sampler.getNumsamples(); p++){
                        sp.setTo(vp.sampler.sampleUnitSquare());
                        pp.x=vp.s*(c-0.5f*vp.hRes + sp.x);
                        pp.y=vp.s*(r-0.5f*vp.vRes + sp.y);
                        ray.d=getDirection(pp);
                        ray.o.setTo(eye.add(u.mul(pp.x).add(v.mul(pp.y))));
                        L.addLocal(w.tracer.traceRay(ray,depth));
                    }
                L.divLocal(vp.numSamples);
                L.mulLocal(exposureTime);
                w.displayPixel(r, c, L);
            }
        }
        
    }
    
}
