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
package com.matrixpeckham.raytracer.tracers;

import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.World;

/**
 * Whitted tracer, pretty much the same as raycast tracer. Overrides method 
 * that is needed for dielectric.  
 * @author William Matrix Peckham
 */
public class Whitted extends Tracer{

    /**
     * default 
     */
    public Whitted() {
        super();
    }

    /**
     * for world
     * @param w 
     */
    public Whitted(World w) {
        super(w);
    }

    
    /**
     * Traces a ray and returns the ray parameter in the double reference parameter
     * @param ray
     * @param t
     * @param depth
     * @return 
     */
    @Override
    public RGBColor traceRay(Ray ray,DoubleRef t, int depth) {
        //bail out for depth
        if(depth>world.vp.maxDepth){
            return Utility.BLACK;
        } else {
            //get the shaderec from the nearest hit object
            ShadeRec sr = new ShadeRec(world.hitObjects(ray));
            //book keep the shaderec for shading, updates
            //ray, depth, and edits the reference parameter.
            if(sr.hitAnObject){
                sr.depth=depth;
                sr.ray.setTo(ray);
                t.d=sr.lastT;
                //shade point
                return sr.material.shade(sr);
            } else {
                //no hit
                t.d=Utility.HUGE_VALUE;
                return world.backgroundColor;
            }
        }
    }

    @Override
    public RGBColor traceRay(Ray ray, int depth) {
        return traceRay(ray,new DoubleRef(), depth); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RGBColor traceRay(Ray ray) {
        return traceRay(ray,0); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
