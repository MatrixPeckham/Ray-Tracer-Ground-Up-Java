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
 * Thin lens camera with depth of field.
 * @author William Matrix Peckham
 */
public class ThinLens extends Camera {
    //radius of lens
    private double lensRadius=1;
    private double d = 1;//view dist
    private double f = 10;//focal dist
    private double zoom = 1;//zoom
    private Sampler sampler;//sampler for lens

    /**
     * getter
     * @return 
     */
    public double getLensRadius() {
        return lensRadius;
    }
    /**
     * get view distance
     * @return 
     */
    public double getD() {
        return d;
    }
    /**
     * get focal distance
     * @return 
     */
    public double getF() {
        return f;
    }
    /**
     * get zoom
     * @return 
     */
    public double getZoom() {
        return zoom;
    }
    /**
     * set radius
     * @param lensRadius 
     */
    public void setLensRadius(double lensRadius) {
        this.lensRadius = lensRadius;
    }
    /**
     * set view distance
     * @param d 
     */
    public void setViewDistance(double d) {
        this.d = d;
    }
    /**
     * set focal distance
     * @param f 
     */
    public void setFocalDistance(double f) {
        this.f = f;
    }
    /**
     * set zoom
     * @param zoom 
     */
    public void setZoom(double zoom) {
        this.zoom = zoom;
    }
    
    
    /**
     * sets the sampler and maps it to disk
     * @param sampler 
     */
    public void setSampler(Sampler sampler) {
        this.sampler = sampler;
        sampler.mapSamplesToUnitDisk();
    }
    
    /**
     * gets the ray direction from the pixel location and lens point.
     * @param pixelPoint
     * @param lensPoint
     * @return 
     */
    private Vector3D rayDirection(Point2D pixelPoint, Point2D lensPoint){
        Point2D p = new Point2D(pixelPoint.x*f/d,pixelPoint.y*f/d);
        Vector3D dir = u.mul(p.x-lensPoint.x).add(v.mul(p.y-lensPoint.y)).sub(w.mul(f));
        dir.normalize();
        return dir;
    }

    
    /**
     * Default constructor
     */
    public ThinLens() {
        super();
    }
    /**
     * copy constructor
     * @param l 
     */
    public ThinLens(ThinLens l){
        super(l);
        lensRadius=l.lensRadius;
        d=l.d;
        f=l.f;
        zoom=l.zoom;
        sampler=l.sampler;
    }
    /**
     * render scene function
     * @param w 
     */
    @Override
    public void renderScene(World w) {
        //color
        RGBColor L = new RGBColor();
        //ray
        Ray ray = new Ray();
        //duplicate viewport because we manipulate it later
        ViewPlane vp = new ViewPlane(w.vp);
        //initial depth
        int depth = 0;
        //normal sample point
        Point2D sp = new Point2D();
        //pixel point
        Point2D pp = new Point2D();
        //normal disk point
        Point2D dp = new Point2D();
        //lens point
        Point2D lp = new Point2D();
        
        //adjust size for zoom.
        vp.s/=zoom;

        //loop through pixels
        for(int r = 0; r<vp.vRes; r++){
            for(int c=0; c<vp.hRes; c++){
                //reset color
                L.setTo(Utility.BLACK);
                //for every sample
                for(int n = 0; n<vp.numSamples; n++){
                    //find pixel point
                    sp.setTo(vp.sampler.sampleUnitSquare());
                    pp.x=vp.s*(c-vp.hRes/2.0+sp.x);
                    pp.y=vp.s*(r-vp.vRes/2.0+sp.y);
                    
                    //find lens point
                    dp.setTo(sampler.sampleUnitDisk());
                    lp.setTo(dp.mul(lensRadius));
                    
                    //ray origin is lens point
                    ray.o.setTo(eye.add(u.mul(lp.x)).add(v.mul(lp.y)));
                    //calc direction and add to color
                    ray.d.setTo(rayDirection(pp, lp));
                    L.addLocal(w.tracer.traceRay(ray, depth));
                }
                //normalize expose, and display pixel
                L.divLocal(vp.numSamples);
                L.mulLocal(exposureTime);
                w.displayPixel(r, c, L);
            }
        }
        
    }
    /**
     * clone
     * @return 
     */
    @Override
    public Camera clone() {
        return new ThinLens(this);
    }
    
}
