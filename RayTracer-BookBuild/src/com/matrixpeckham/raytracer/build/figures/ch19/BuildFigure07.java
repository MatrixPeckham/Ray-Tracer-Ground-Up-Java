/*
  Copyright (C) 2015 William Matrix Peckham
 
  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.
 
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
 
  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.matrixpeckham.raytracer.build.figures.ch19;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.triangles.Triangle;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/*
 
  @author William Matrix Peckham
 */

/**
 *
 * @author Owner
 */

public class BuildFigure07 implements BuildWorldFunction{

    @Override
    public void build(World w) {
	int num_samples = 25;
	
	w.vp.setHres(400);
	w.vp.setVres(400);
	w.vp.setSamples(num_samples);
	
	w.tracer = new RayCast(w);
	
	Pinhole pinhole_ptr = new Pinhole();
	pinhole_ptr.setEye(25, 200, 100);  
	pinhole_ptr.setLookat(-0.5, 0, 0);  
	pinhole_ptr.setViewDistance(8000);	
	pinhole_ptr.computeUVW();
	w.setCamera(pinhole_ptr);
	
	
	PointLight light_ptr1 = new PointLight();
	light_ptr1.setLocation(1, 5, 0);
	light_ptr1.scaleRadiance(3.0);
	light_ptr1.setShadows(true);
	w.addLight(light_ptr1);
	
	
	// yellow triangle
		
	Matte matte_ptr1 = new Matte();			
	matte_ptr1.setKa(0.25); 
	matte_ptr1.setKd(0.75);
	matte_ptr1.setCd(1, 1, 0);
	Triangle triangle_ptr1 = new Triangle(new Point3D(2, 0.5, 5), new Point3D(2, 1.5, -5), new Point3D(-1, 0, -4)); 
	triangle_ptr1.setMaterial(matte_ptr1);
	w.addObject(triangle_ptr1);
	
        

	// dark green triangle (transformed)
	
	Matte matte_ptr2 = new Matte();			
	matte_ptr2.setKa(0.25); 
	matte_ptr2.setKd(0.75);
	matte_ptr2.setCd(0.0, 0.5, 0.41);
	
	Instance triangle_ptr2 = new Instance(new Triangle(new Point3D(2, 1, 5),new Point3D(2, 0.5, -5),new Point3D(-1, -1, -4))); 
	triangle_ptr2.rotateY(120);
	triangle_ptr2.setMaterial(matte_ptr2);
	w.addObject(triangle_ptr2);
	
	
	// brown triangle (transformed)
			
	Matte matte_ptr3 = new Matte();			
	matte_ptr3.setKa(0.25); 
	matte_ptr3.setKd(0.75);
	matte_ptr3.setCd(0.71, 0.40, 0.16);
	
	Instance triangle_ptr3 = new Instance(new Triangle(new Point3D(2, 0, 5), new Point3D(2, 1, -5),new  Point3D(-1, 0, -4))); 
	triangle_ptr3.rotateY(240);
	triangle_ptr3.setMaterial(matte_ptr3);
	w.addObject(triangle_ptr3);    
    
    }
}
