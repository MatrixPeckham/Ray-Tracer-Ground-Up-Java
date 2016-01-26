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

import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.World;

/**
 * GlobalTrace tracer, pretty much the same as PathTrace tracer. But shades with
 * different function.  globalshade function should use direct lighting AND recursive ray to determine
 * incoming radiance.  this makes the images it generates similar to ray cast/whitted
 * tracers, however it adds light from secondary sources. 
 * @author William Matrix Peckham
 */
public class GlobalTrace extends Tracer{

    public GlobalTrace() {
        super();
    }

    public GlobalTrace(World w) {
        super(w);
    }

    @Override
    public RGBColor traceRay(Ray ray, int depth) {
        if(depth>world.vp.maxDepth){//depth bail out
            return Utility.BLACK;
        } else {
            //closest intersection
            ShadeRec sr = new ShadeRec(world.hitObjects(ray));
            if(sr.hitAnObject){//book keep and shade.
                sr.depth=depth;
                sr.ray.setTo(ray);
                return sr.material.globalShade(sr);
            } else {
                return world.backgroundColor;
            }
        }
    }
    
    
}
