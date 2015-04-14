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

import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.samplers.Sampler;
import com.matrixpeckham.raytracer.util.Point2D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
import com.matrixpeckham.raytracer.world.ViewPlane;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class ThinLens extends Camera {
    private double lensRadius=1;
    private double d = 1;//view dist
    private double f = 10;//focal dist
    private double zoom = 1;
    private Sampler sampler;

    public double getLensRadius() {
        return lensRadius;
    }

    public double getD() {
        return d;
    }

    public double getF() {
        return f;
    }

    public double getZoom() {
        return zoom;
    }

    public void setLensRadius(double lensRadius) {
        this.lensRadius = lensRadius;
    }

    public void setViewDistance(double d) {
        this.d = d;
    }

    public void setFocalDistance(double f) {
        this.f = f;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }
    
    

    public void setSampler(Sampler sampler) {
        this.sampler = sampler;
        sampler.mapSamplesToUnitDisk();
    }
    
    private Vector3D rayDirection(Point2D pixelPoint, Point2D lensPoint){
        Point2D p = new Point2D(pixelPoint.x*f/d,pixelPoint.y*f/d);
        Vector3D dir = u.mul(p.x-lensPoint.x).add(v.mul(p.y-lensPoint.y)).sub(w.mul(f));
        dir.normalize();
        return dir;
    }

    public ThinLens() {
        super();
    }
    
    public ThinLens(ThinLens l){
        super(l);
        lensRadius=l.lensRadius;
        d=l.d;
        f=l.f;
        zoom=l.zoom;
        sampler=l.sampler;
    }
    
    @Override
    public void renderScene(World w) {
        RGBColor L = new RGBColor();
        Ray ray = new Ray();
        ViewPlane vp = new ViewPlane(w.vp);
        int depth = 0;
        Point2D sp = new Point2D();
        Point2D pp = new Point2D();
        Point2D dp = new Point2D();
        Point2D lp = new Point2D();
        
        vp.s/=zoom;
        
        for(int r = 0; r<vp.vRes; r++){
            for(int c=0; c<vp.hRes; c++){
                L.setTo(Utility.BLACK);
                for(int n = 0; n<vp.numSamples; n++){
                    sp.setTo(vp.sampler.sampleUnitSquare());
                    pp.x=vp.s*(c-vp.hRes/2.0+sp.x);
                    pp.y=vp.s*(r-vp.vRes/2.0+sp.y);
                    
                    dp.setTo(sampler.sampleUnitDisk());
                    lp.setTo(dp.mul(lensRadius));
                    
                    ray.o.setTo(eye.add(u.mul(lp.x)).add(v.mul(lp.y)));
                    ray.d.setTo(rayDirection(pp, lp));
                    L.addLocal(w.tracer.traceRay(ray, depth));
                }
                L.divLocal(vp.numSamples);
                L.mulLocal(exposureTime);
                w.displayPixel(r, c, L);
            }
        }
        
    }

    @Override
    public Camera clone() {
        return new ThinLens(this);
    }
    
}
