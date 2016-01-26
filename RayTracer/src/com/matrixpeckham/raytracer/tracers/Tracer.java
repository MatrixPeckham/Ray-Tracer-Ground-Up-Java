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
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.world.World;

/**
 * Do nothing tracer, returns defaults. 
 * @author William Matrix Peckham
 */
public class Tracer {
    /**
     * World reference.
     */
    protected World world=null;
    /**
     * default constructor. 
     */
    public Tracer(){}
    /**
     * constructor that sets the world. 
     * @param w 
     */
    public Tracer(World w){world=w;}
    /**
     * Trace a ray from start point to final color. 
     * does not incorporate depth.
     * @param ray
     * @return 
     */
    public RGBColor traceRay(Ray ray){
        return Utility.BLACK;
    }
    /**
     * Trace a ray to a color taking into account the current depth.
     * should bail out if depth is over the world.vp.maxDepth.
     * @param ray
     * @param depth
     * @return 
     */
    public RGBColor traceRay(Ray ray, int depth){return Utility.BLACK;}
    /**
     * Trace a ray to a color with depth.  uses reference t to return ray parameter
     * at intersection
     * @param ray ray to trace
     * @param t reference return parameter.
     * @param depth depth
     * @return 
     */
    public RGBColor traceRay(Ray ray,DoubleRef t, int depth) {
        t.d=0;
        return traceRay(ray,depth);
    }

}
