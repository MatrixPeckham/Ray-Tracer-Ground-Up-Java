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
import com.matrixpeckham.raytracer.geometricobjects.compound.SolidCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primitives.ConcaveSphere;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.EnvironmentLight;
import com.matrixpeckham.raytracer.materials.Emissive;
import com.matrixpeckham.raytracer.materials.Matte;
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
public class BuildFigure05 implements BuildWorldFunction{

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.


// This builds the scene for Figure 26.5
	int numSamples = 100;

	w.vp.setHres(600);
	w.vp.setVres(400);
	w.vp.setSamples(numSamples);
	
	w.tracer = new Whitted(w);

	Ambient ambientPtr = new Ambient();
	ambientPtr.scaleRadiance(0.0);
	w.setAmbient(ambientPtr);	

			
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(100, 45, 100);  
	pinholePtr.setLookat(-10, 40, 0);  
	pinholePtr.setViewDistance(400);  	
	pinholePtr.computeUVW(); 
	w.setCamera(pinholePtr);
	
	
	Emissive emissivePtr = new Emissive();
	emissivePtr.setCe(Utility.WHITE);  
	emissivePtr.scaleRadiance(1.5);   
							
	EnvironmentLight environmentLightPtr = new EnvironmentLight();
	environmentLightPtr.setMaterial(emissivePtr);
	environmentLightPtr.setSampler(new MultiJittered(numSamples));
	environmentLightPtr.setShadows(true);
	w.addLight(environmentLightPtr);
	
	
	ConcaveSphere spherePtr = new ConcaveSphere();		// centered on the origin
	spherePtr.setRadius(1000000.0);
	spherePtr.setShadows(false);
	spherePtr.setMaterial(emissivePtr);
	w.addObject(spherePtr);	
	
		
	double ka = 0.2;  // common ambient reflection coefficient	

		
	// large sphere

	Matte mattePtr1 = new Matte();			
	mattePtr1.setKa(ka); 
	mattePtr1.setKd(0.60);
	mattePtr1.setCd(Utility.WHITE);
	
	Sphere spherePtr1 = new Sphere(new Point3D(38, 20, -24), 20); 
	spherePtr1.setMaterial(mattePtr1);
	w.addObject(spherePtr1);
	
	
	// small sphere
	
	Matte mattePtr2 = new Matte();			
	mattePtr2.setKa(ka); 
	mattePtr2.setKd(0.5);
	mattePtr2.setCd(0.85);				// gray
	
	Sphere spherePtr2 = new Sphere(new Point3D(34, 12, 13), 12);
	spherePtr2.setMaterial(mattePtr2);
	w.addObject(spherePtr2);
	
	
	// medium sphere
		
	Matte mattePtr3 = new Matte();			
	mattePtr3.setKa(ka); 
	mattePtr3.setKd(0.75);
	mattePtr3.setCd(0.73, 0.22, 0.0);    // orange
	
	Sphere spherePtr3 = new Sphere(new Point3D(-7, 15, 42), 16);
	spherePtr3.setMaterial(mattePtr3);
	w.addObject(spherePtr3);
	
	
	// cylinder
	
	Matte mattePtr4 = new Matte();			
	mattePtr4.setKa(ka); 
	mattePtr4.setKd(0.75);
	mattePtr4.setCd(0.60);				// gray
			
	double bottom 	= 0.0;
	double top 		= 85.0;
	double radius	= 22.0;
	SolidCylinder cylinderPtr = new SolidCylinder(bottom, top, radius);
	cylinderPtr.setMaterial(mattePtr4);
	w.addObject(cylinderPtr);

	
	// box
	
	MultiJittered samplerPtr5 = new MultiJittered(numSamples);
	
	Matte mattePtr5 = new Matte();			
	mattePtr5.setKa(ka); 
	mattePtr5.setKd(0.75);
	mattePtr5.setCd(0.95);				// gray
	
	Box boxPtr = new Box(new Point3D(-55, 0, -110), new Point3D(-25, 60, 65));  // thicker
	boxPtr.setMaterial(mattePtr5);
	w.addObject(boxPtr);
	
	
	// ground plane
			
	Matte mattePtr6 = new Matte();			
	mattePtr6.setKa(0.15); 
	mattePtr6.setKd(0.95);	
	mattePtr6.setCd(0.37, 0.43, 0.08);     // olive green
	
	Plane planePtr = new Plane(new Point3D(0, 0.01, 0),new Normal(0, 1, 0));
	planePtr.setMaterial(mattePtr6);
	w.addObject(planePtr);


    }
    
}
