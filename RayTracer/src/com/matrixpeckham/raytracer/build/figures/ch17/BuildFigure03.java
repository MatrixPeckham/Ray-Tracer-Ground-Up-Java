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
package com.matrixpeckham.raytracer.build.figures.ch17;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.AmbientOccluder;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.samplers.Regular;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/*
 
  @author William Matrix Peckham
 */
public class BuildFigure03 implements BuildWorldFunction{

    @Override
    public void build(World w) {
//	int num_samples = 1;   	// for Figure 17.3(a)
	int num_samples = 256;	// for Figure 17.3(b)
	
	w.vp.setHres(400);	  		
	w.vp.setVres(400);
	w.vp.setSamples(num_samples);  
	
	w.tracer = new RayCast(w);
        
        MultiJittered sampler_ptr = new MultiJittered(num_samples);
	
        Ambient ambient = new Ambient();
        ambient.scaleRadiance(1);
//        w.setAmbient(ambient);//for a
        
	AmbientOccluder occluder_ptr = new AmbientOccluder();
	occluder_ptr.scaleRadiance(1.0);
	occluder_ptr.setMinAmount(0.0);
	occluder_ptr.setSampler(sampler_ptr);
	w.setAmbient(occluder_ptr);//for b	
	
		
	Pinhole pinhole_ptr = new Pinhole();
	pinhole_ptr.setEye(25, 20, -45);
	pinhole_ptr.setLookat(0, 1, 0); 
	pinhole_ptr.setViewDistance(5000);	
	pinhole_ptr.computeUVW();
	w.setCamera(pinhole_ptr);
	
	// sphere
	
	Matte matte_ptr1 = new Matte();			
	matte_ptr1.setKa(0.75);		
	matte_ptr1.setKd(0);
	matte_ptr1.setCd(1, 1, 0);   // yellow

	Sphere sphere_ptr = new Sphere (new Point3D(0, 1, 0), 1);  
	sphere_ptr.setMaterial(matte_ptr1);
	w.addObject(sphere_ptr);	
	
	// ground plane
	
	Matte matte_ptr2 = new Matte();			
	matte_ptr2.setKa(0.75);		
	matte_ptr2.setKd(0);
	matte_ptr2.setCd(Utility.WHITE);        
	
	Plane plane_ptr = new Plane(new Point3D(0), new Normal(0, 1, 0));
	plane_ptr.setMaterial(matte_ptr2);
	w.addObject(plane_ptr);	    }
    
}
