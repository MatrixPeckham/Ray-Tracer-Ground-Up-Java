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
package com.matrixpeckham.raytracer.build.figures.ch24;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure20B implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 24.20(b)

 												

	int numSamples = 1;
	
	w.vp.setHres(800);   
	w.vp.setVres(800);
	w.vp.setSamples(numSamples);
	w.vp.setMaxDepth(12);
		
	w.tracer = new Whitted(w);

	Ambient ambientPtr = new Ambient();
	ambientPtr.scaleRadiance(0.5);
	w.setAmbient(ambientPtr);	
					
	
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(0.0, 0.05, 0.0);
	pinholePtr.setLookat(0.0, 1.0, 0.0);  
	pinholePtr.setViewDistance(600.0);   
	pinholePtr.computeUVW(); 
	w.setCamera(pinholePtr);
	
	
	// the four spheres
		
	double ka = 0.75;
	double kd = 0.75;
	double ks = 0.1;
	double e = 20.0;
	double kr = 1.0;
	
	Reflective reflectivePtr1 = new Reflective();
	reflectivePtr1.setKa(ka); 
	reflectivePtr1.setKd(kd); 
	reflectivePtr1.setKs(ks);  
	reflectivePtr1.setCd(0.168, 0.171, 0.009);  		// pink 
	reflectivePtr1.setExp(e);
	reflectivePtr1.setKr(kr);
	
	Sphere spherePtr1 = new Sphere(new Point3D(0.0, 1.414, 0.0), 0.866);
	spherePtr1.setMaterial(reflectivePtr1);
	w.addObject(spherePtr1);
	
	
	Reflective reflectivePtr2 = new Reflective();
	reflectivePtr2.setKa(ka); 
	reflectivePtr2.setKd(kd); 
	reflectivePtr2.setCd(0.094, 0.243, 0.029);    	// green
	reflectivePtr2.setKs(ks);    
	reflectivePtr2.setExp(e);
	reflectivePtr2.setKr(kr);
	
	Sphere spherePtr2 = new Sphere(new Point3D(0.0, 0.0, 1.0), 0.866);
	spherePtr2.setMaterial(reflectivePtr2);
	w.addObject(spherePtr2);
	
	
	Reflective reflectivePtr3 = new Reflective();
	reflectivePtr3.setKa(ka); 
	reflectivePtr3.setKd(kd);
	reflectivePtr3.setCd(0.243, 0.018, 0.164);     	// red 
	reflectivePtr3.setKs(ks);    
	reflectivePtr3.setExp(e);
	reflectivePtr3.setKr(kr);
	
	Sphere spherePtr3 = new Sphere(new Point3D(0.866, 0.0, -0.5), 0.866);
	spherePtr3.setMaterial(reflectivePtr3);
	w.addObject(spherePtr3);
	
	Reflective reflectivePtr4 = new Reflective();
	reflectivePtr4.setKa(ka); 
	reflectivePtr4.setKd(kd); 
	reflectivePtr4.setCd(0.094, 0.1, 0.243);    		// blue
	reflectivePtr4.setKs(ks);    
	reflectivePtr4.setExp(e);
	reflectivePtr4.setKr(kr);
	
	Sphere spherePtr4 = new Sphere(new Point3D(-0.866, 0.0, -0.5), 0.866);
	spherePtr4.setMaterial(reflectivePtr4);
	w.addObject(spherePtr4);



    }
    
}
