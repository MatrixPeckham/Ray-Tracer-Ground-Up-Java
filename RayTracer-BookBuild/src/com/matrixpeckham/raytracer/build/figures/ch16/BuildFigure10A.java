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
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
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
public class BuildFigure10A implements BuildWorldFunction{

    @Override
    public void build(World w) {
        int num_samples = 100;

	w.vp.setHres(400);
	w.vp.setVres(400);
	w.vp.setPixelSize(0.5);
	w.vp.setSamples(num_samples); 
	
	w.tracer = new RayCast(w);
	
	Ambient ambient_ptr = new Ambient();
	ambient_ptr.scaleRadiance(1.0);
	w.setAmbient(ambient_ptr);
	
	Pinhole pinhole_ptr = new Pinhole();
	pinhole_ptr.setEye(0, 0, 500);
	pinhole_ptr.setLookat(-15, -10, 0);
	pinhole_ptr.setViewDistance(850.0);
	pinhole_ptr.computeUVW();
	w.setCamera(pinhole_ptr);
	
	PointLight light_ptr2 = new PointLight();
	light_ptr2.setLocation(100, 50, 150);
	light_ptr2.scaleRadiance(3.0); 
	light_ptr2.setShadows(true); 
	w.addLight(light_ptr2);

	Matte matte_ptr1 = new Matte();
	matte_ptr1.setKa(0.25);	
	matte_ptr1.setKd(0.65);
	matte_ptr1.setCd(1, 1, 0);	  				// yellow	
	Sphere sphere_ptr1 = new Sphere(new Point3D(10, -5, 0), 27); 
	sphere_ptr1.setMaterial(matte_ptr1);
	w.addObject(sphere_ptr1);
	
	Matte matte_ptr2 = new Matte();
	matte_ptr2.setKa(0.15);
	matte_ptr2.setKd(0.85);
	matte_ptr2.setCd(0.71, 0.40, 0.16);   		// brown
	Sphere	sphere_ptr2 = new Sphere(new Point3D(-25, 10, -35), 27); 			
	sphere_ptr2.setMaterial(matte_ptr2);							
	w.addObject(sphere_ptr2);
	
	Matte matte_ptr3 = new Matte();
	matte_ptr3.setKa(0.15);	
	matte_ptr3.setKd(0.5);
	matte_ptr3.setCd(0, 0.4, 0.2);				// dark green
	Plane plane_ptr = new Plane(new Point3D(0, 0, -50), new Normal(0, 0, 1)); 
	plane_ptr.setMaterial(matte_ptr3);
	w.addObject(plane_ptr);
    }
    
}
