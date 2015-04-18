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
 *
 * @author William Matrix Peckham
 */
public class RayCast extends Tracer {
    public RayCast(){
        super();
    }
    public RayCast(World world){
        super(world);
    }

    @Override
    public RGBColor traceRay(Ray ray) {
        return traceRay(ray,0);
    }

    @Override
    public RGBColor traceRay(Ray ray, int depth) {
        if(depth>world.vp.maxDepth){
            return Utility.BLACK;
        }
        ShadeRec sr = new ShadeRec(world.hitObjects(ray));
        if(sr.hitAnObject){
            sr.ray=ray;
            sr.depth=depth;
            return sr.material.shade(sr);
        } else {
            return world.backgroundColor;
        }
    }
    
    
}
