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
import com.matrixpeckham.raytracer.geometricobjects.primitives.ConcaveSphere;
import com.matrixpeckham.raytracer.geometricobjects.primitives.OpenCylinder;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.materials.Emissive;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure39 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 24.39

 												

	int numSamples = 16;
	
	w.vp.setHres(400);
	w.vp.setVres(400);
	w.vp.setSamples(numSamples);
	w.vp.setMaxDepth(10);
	
	w.tracer = new Whitted(w);	

	Ambient ambientPtr = new Ambient();
	ambientPtr.scaleRadiance(0.0);
	w.setAmbient(ambientPtr);	
		
			
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(0, 0, 0); 
	pinholePtr.setLookat(0, 0, -100);
	pinholePtr.setViewDistance(500);
	pinholePtr.computeUVW(); 
	w.setCamera(pinholePtr);
	
	
	Emissive emissivePtr = new Emissive();
	emissivePtr.setCe(1.0, 1.0, 0.5); 	// yellow 
	emissivePtr.scaleRadiance(0.85);  


	ConcaveSphere spherePtr = new ConcaveSphere();
	spherePtr.setRadius(1000000.0);
	spherePtr.setMaterial(emissivePtr);
	spherePtr.setShadows(false);
	w.addObject(spherePtr);

	
	// cylinder
	
	Reflective reflectivePtr = new Reflective();			
	reflectivePtr.setKa(0.0); 
	reflectivePtr.setKd(0.0); 
	reflectivePtr.setCd(Utility.BLACK);
	reflectivePtr.setKs(0.0);
	reflectivePtr.setExp(1.0);
	reflectivePtr.setKr(0.9);
	reflectivePtr.setCr(1.0, 0.75, 0.5);  // orange
	
	
	double bottom 	= -2.0;
	double top 		= 2.0; 
	double radius	= 10.0;
	
	OpenCylinder cylinderPtr2 = new OpenCylinder(bottom, top, radius);
	cylinderPtr2.setMaterial(reflectivePtr);
	w.addObject(cylinderPtr2);



    }
    
}
