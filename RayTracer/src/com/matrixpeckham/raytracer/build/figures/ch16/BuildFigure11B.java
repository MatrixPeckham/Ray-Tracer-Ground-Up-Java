/*
  Copyright (C) 2015 William Matrix Peckham
 *
  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.
 *
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
 *
  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.matrixpeckham.raytracer.build.figures.ch16;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Sphere;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
  @author William Matrix Peckham
 */
public class BuildFigure11B implements BuildWorldFunction {

    @Override
    public void build(World w) {
    int num_samples = 16;
	
	w.vp.setHres(600);	  		
	w.vp.setVres(600);
	w.vp.setSamples(num_samples);
	
	w.tracer = new RayCast(w);
			
	Pinhole camera_ptr = new Pinhole();
	camera_ptr.setEye(0, 7, 10);
	camera_ptr.setLookat(0, -1.5, 0);
	camera_ptr.setViewDistance(1200);     
	camera_ptr.computeUVW();
	w.setCamera(camera_ptr);
		
	PointLight light_ptr1 = new PointLight();
	light_ptr1.setLocation(3, 10, 2); 
	light_ptr1.setColor(1, 0, 0);				// red
	light_ptr1.scaleRadiance(12.0);
	light_ptr1.setShadows(true);
	w.addLight(light_ptr1);
	
	PointLight light_ptr2 = new PointLight();
	light_ptr2.setLocation(-3, 10, 2); 
	light_ptr2.setColor(0, 1, 0);				// green
	light_ptr2.scaleRadiance(12.0);
	light_ptr2.setShadows(true);
	w.addLight(light_ptr2);
	
	PointLight light_ptr3 = new PointLight();
	light_ptr3.setLocation(0, 10, -3); 
	light_ptr3.setColor(0, 0, 1);				// blue
	light_ptr3.scaleRadiance(12.0);
	light_ptr3.setShadows(true);
	w.addLight(light_ptr3);
	
	// sphere
	
	Matte matte_ptr1 = new Matte();
        matte_ptr1.setKa(0.6); 
	matte_ptr1.setKd(0.2); 
	matte_ptr1.setCd(0.5);
		
	Sphere	sphere_ptr1 = new Sphere();  
	sphere_ptr1.setMaterial(matte_ptr1);
	w.addObject(sphere_ptr1);	
	
	// ground plane
	
	Matte matte_ptr2 = new Matte();			
	matte_ptr2.setKa(0.0); 
	matte_ptr2.setKd(0.35);
	matte_ptr2.setCd(0.7); 	
	
	Plane plane_ptr = new Plane(new Point3D(0, -3, 0), new Normal(0, 1, 0));
	plane_ptr.setMaterial(matte_ptr2);
	w.addObject(plane_ptr);
    }
}
