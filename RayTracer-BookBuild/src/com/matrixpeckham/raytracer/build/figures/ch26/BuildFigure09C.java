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
package com.matrixpeckham.raytracer.build.figures.ch26;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConcavePartCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.materials.Emissive;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.tracers.PathTrace;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure09C implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.


// This builds the scene for Figure 26.9(b)
	int numSamples = 5041;
	
	w.vp.setHres(600);	  		
	w.vp.setVres(400);
	w.vp.setSamples(numSamples); 
	w.vp.setMaxDepth(5);
		
	w.tracer = new PathTrace(w);
	
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(0, 100, 2);		
	pinholePtr.setLookat(0, 0, 2);
	pinholePtr.setViewDistance(5000);	
	pinholePtr.computeUVW();     
	w.setCamera(pinholePtr);

	
	// emissive sphere
	
	Emissive emissivePtr = new Emissive();
	emissivePtr.setCe(0.75, 1, 0.75);
	emissivePtr.scaleRadiance(30.0);   
		
	Sphere spherePtr = new Sphere(new Point3D(-2, 7, 6), 1); 
	spherePtr.setMaterial(emissivePtr);
	spherePtr.setSampler(new MultiJittered(numSamples));
	w.addObject(spherePtr);
		
	
	// reflective open half cylinder
	
	Reflective reflectivePtr = new Reflective();
	reflectivePtr.setKa(0.0); 
	reflectivePtr.setKd(0.4); 
	reflectivePtr.setKs(0.0);      
	reflectivePtr.setExp(1.0);
	reflectivePtr.setKr(0.95); 
	reflectivePtr.setCr(1.0, 0.5, 0.25);  // orange 
	
	double y0 		= -1.0;
	double y1 		= 3.0;
	double radius 	= 3.0;
	double phiMin 	= 90.0;
	double phiMax 	= 270.0;
	
	ConcavePartCylinder cylinderPtr = new ConcavePartCylinder(y0, y1, radius, phiMin, phiMax); 
	cylinderPtr.setMaterial(reflectivePtr);
	w.addObject(cylinderPtr);	
	
	
	// plane

	Matte mattePtr = new Matte();			
	mattePtr.setKa(0.0);		
	mattePtr.setKd(0.75);
	mattePtr.setCd(1);
	mattePtr.setSampler(new MultiJittered(numSamples));
	
	Plane planePtr = new Plane(new Point3D(0, -1.0, 0), new Normal(0, 1, 0)); 	
	planePtr.setMaterial(mattePtr);
	w.addObject(planePtr);


    }
    
}
