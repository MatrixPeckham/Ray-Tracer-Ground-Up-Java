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
import com.matrixpeckham.raytracer.geometricobjects.compound.SolidCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primitives.ConcaveSphere;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.AmbientOccluder;
import com.matrixpeckham.raytracer.lights.EnvironmentLight;
import com.matrixpeckham.raytracer.materials.Emissive;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure43 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 24.43

 												

	int numSamples = 100;
	
	w.vp.setHres(600);
	w.vp.setVres(400);
	w.vp.setSamples(numSamples);
	w.vp.setMaxDepth(10);
		
	w.tracer = new Whitted(w);	
		
	AmbientOccluder ambientOccluderPtr = new AmbientOccluder();
	ambientOccluderPtr.setSampler(new MultiJittered(numSamples));
	ambientOccluderPtr.setMinAmount(0.5);
	w.setAmbient(ambientOccluderPtr);

			
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(100, 45, 100);  
	pinholePtr.setLookat(-10, 40, 0);  
	pinholePtr.setViewDistance(400);  
	pinholePtr.computeUVW(); 
	w.setCamera(pinholePtr);

	
	Emissive emissivePtr = new Emissive();
	emissivePtr.scaleRadiance(0.90);
	emissivePtr.setCe(1.0, 1.0, 0.5); 	// lemon yellow

		
	ConcaveSphere spherePtr = new ConcaveSphere();
	spherePtr.setRadius(1000000.0);
	spherePtr.setMaterial(emissivePtr);
	spherePtr.setShadows(false);
	w.addObject(spherePtr);
	
	EnvironmentLight environmentLightPtr = new EnvironmentLight();
	environmentLightPtr.setMaterial(emissivePtr);
	environmentLightPtr.setSampler(new MultiJittered(numSamples));
	environmentLightPtr.setShadows(true);
	w.addLight(environmentLightPtr);
	
	
	// common reflective material for large sphere, medium sphere, and cylinder
	
	Reflective reflectivePtr = new Reflective();
	reflectivePtr.setKa(0.0); 
	reflectivePtr.setKd(0.0); 
	reflectivePtr.setCd(Utility.BLACK);
	reflectivePtr.setKs(0.0);
	reflectivePtr.setExp(1.0);			
	reflectivePtr.setKr(0.9);
	reflectivePtr.setCr(1.0, 0.75, 0.5);   // orange
	
	double ka = 0.2;  // commom ambient reflection coefficient for other objects
	
		// large sphere
	
	Sphere spherePtr1 = new Sphere(new Point3D(38, 20, -24), 20);
	spherePtr1.setMaterial(reflectivePtr);
	w.addObject(spherePtr1);
	
	
	// small sphere
	
	Matte mattePtr2 = new Matte();			
	mattePtr2.setKa(ka); 
	mattePtr2.setKd(0.5);
	mattePtr2.setCd(0.85);
	
	Sphere spherePtr2 = new Sphere(new Point3D(34, 12, 13), 12);
	spherePtr2.setMaterial(mattePtr2);
	w.addObject(spherePtr2);
	
	
	// medium sphere
	
	Sphere spherePtr3 = new Sphere(new Point3D(-7, 15, 42), 16);
	spherePtr3.setMaterial(reflectivePtr);
	w.addObject(spherePtr3);
	
	
	// cylinder
	
	double bottom 	= 0.0;
	double top 		= 85; 
	double radius	= 22;
	SolidCylinder cylinderPtr = new SolidCylinder(bottom, top, radius);
	cylinderPtr.setMaterial(reflectivePtr);
	w.addObject(cylinderPtr);

	
	// box
	
	Matte mattePtr5 = new Matte();			
	mattePtr5.setKa(ka); 
	mattePtr5.setKd(0.5);
	mattePtr5.setCd(0.95);
	
	Box boxPtr = new Box(new Point3D(-35, 0, -110), new Point3D(-25, 60, 65));
	boxPtr.setMaterial(mattePtr5);
	w.addObject(boxPtr);
	
				
	// ground plane
		
	Matte mattePtr6 = new Matte();			
	mattePtr6.setKa(0.15); 
	mattePtr6.setKd(0.5);	
	mattePtr6.setCd(0.7);    
	
	Plane planePtr = new Plane(new Point3D(0, 0.01, 0), new Normal(0, 1, 0));
	planePtr.setMaterial(mattePtr6);
	w.addObject(planePtr);



    }
    
}
