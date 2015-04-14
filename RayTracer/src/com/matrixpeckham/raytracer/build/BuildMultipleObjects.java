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
package com.matrixpeckham.raytracer.build;

import com.matrixpeckham.raytracer.geometricobjects.primatives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Sphere;
import com.matrixpeckham.raytracer.tracers.MultipleObjects;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildMultipleObjects implements BuildWorldFunction {

    @Override
    public void build(World w) {
        w.vp.hRes=200;
	w.vp.vRes=200;
	w.vp.s=1.0f;
        
        w.tracer = new MultipleObjects(w); 
	
	w.backgroundColor = new RGBColor(Utility.BLACK);
	
	// use access functions to set centre and radius
	
	Sphere sphere_ptr = new Sphere();
	sphere_ptr.setCenter(new Point3D(0, -25, 0));
	sphere_ptr.setRadius(80);
	sphere_ptr.setColor(1, 0, 0);  // red
	w.addObject(sphere_ptr);

	// use constructor to set centre and radius 
	
	sphere_ptr = new Sphere(new Point3D(0, 30, 0), 60);
	sphere_ptr.setColor(1, 1, 0);	// yellow
	w.addObject(sphere_ptr);
	
	Plane plane_ptr = new Plane(new Point3D(0), new Normal(0, 1, 1));
	plane_ptr.setColor(0.0f, 0.3f, 0.0f);	// dark green
	w.addObject(plane_ptr);
    }
    
}
