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
package com.matrixpeckham.raytracer.build.figures.ch25;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.compound.SolidCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primitives.ConcaveSphere;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.AmbientOccluder;
import com.matrixpeckham.raytracer.lights.EnvironmentLight;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Emissive;
import com.matrixpeckham.raytracer.materials.GlossyReflector;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.materials.SV_GlossyReflector;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.textures.procedural.PlaneChecker;
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
public class BuildPageOneImage implements BuildWorldFunction{

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for the Chapter 25 page one image.

	int numSamples = 100;   // use 1 for testing!
	
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
	pinholePtr.setLookat(-10, 35, 0);  
	pinholePtr.setViewDistance(400);  
	pinholePtr.computeUVW(); 
	w.setCamera(pinholePtr);
	
		
	PointLight lightPtr2 = new PointLight();
	lightPtr2.setLocation(150, 250, -150);   
	lightPtr2.scaleRadiance(1.5);
	lightPtr2.setShadows(true);
	w.addLight(lightPtr2);


	Emissive emissivePtr = new Emissive();
	emissivePtr.setCe(1.0, 1.0, 0.5); 	 // lemon
	emissivePtr.scaleRadiance(0.85); 
	
	
	EnvironmentLight environmentLightPtr = new EnvironmentLight();
	environmentLightPtr.setMaterial(emissivePtr);
	environmentLightPtr.setSampler(new MultiJittered(numSamples));
	environmentLightPtr.setShadows(true);
	w.addLight(environmentLightPtr);
	
	
	// large concave sphere for direct rendering of environment light
	
	ConcaveSphere spherePtr = new ConcaveSphere();
	spherePtr.setRadius(1000000.0);
	spherePtr.setMaterial(emissivePtr);
	spherePtr.setShadows(false);
	w.addObject(spherePtr);
	
	
	// other objects
		
	// large sphere
	
	GlossyReflector glossyPtr1 = new GlossyReflector();
	glossyPtr1.setSamples(numSamples, 100.0);			
	glossyPtr1.setKa(0.0); 
	glossyPtr1.setKd(0.0);
	glossyPtr1.setKs(0.3);
	glossyPtr1.setExp(100.0);
	glossyPtr1.setCd(1.0, 1.0, 0.3);	 // orange
	glossyPtr1.setKr(0.9);
	glossyPtr1.setExponent(100.0);
	glossyPtr1.setCr(1.0, 0.75, 0.5);  // orange
	
	Sphere spherePtr1 = new Sphere(new Point3D(38, 20, -24), 20);
	spherePtr1.setMaterial(glossyPtr1);
	w.addObject(spherePtr1);
	
	
	// small sphere
		
	Matte mattePtr2 = new Matte();			
	mattePtr2.setKa(0.4); 
	mattePtr2.setKd(0.4);
	mattePtr2.setCd(0.75, 0, 0);     // red
	
	Sphere spherePtr2 = new Sphere(new Point3D(34, 12, 13), 12);
	spherePtr2.setMaterial(mattePtr2);
	w.addObject(spherePtr2);
	
	
	// medium sphere
	
	Reflective reflectivePtr = new Reflective();			
	reflectivePtr.setCd(0.75);
	reflectivePtr.setKa(0.0); 
	reflectivePtr.setKd(0.0); 
	reflectivePtr.setKs(0.0);
	reflectivePtr.setExp(20);
	reflectivePtr.setKr(0.9);
	reflectivePtr.setCr(1.0, 0.75, 0.5);   // orange
	
	Sphere spherePtr3 = new Sphere(new Point3D(-7, 15, 42), 16);
	spherePtr3.setMaterial(reflectivePtr);
	w.addObject(spherePtr3);
	
	
	// cylinder

	GlossyReflector glossyPtr2 = new GlossyReflector();
	glossyPtr2.setSamples(numSamples, 10.0);			
	glossyPtr2.setKa(0.0); 
	glossyPtr2.setKd(0.0);
	glossyPtr2.setKs(0.75);
	glossyPtr2.setCs(0.35, 0.75, 0.55);  // green
	glossyPtr2.setExp(10.0);
	glossyPtr2.setCd(1.0, 1.0, 0.3);
	glossyPtr2.setKr(0.9);
	glossyPtr2.setExponent(10.0);
	glossyPtr2.setCr(0.35, 0.75, 0.55);   // green
	
	double bottom 	= 0.0;
	double top 		= 85.0;
	double radius	= 22.0;
	SolidCylinder cylinderPtr = new SolidCylinder(bottom, top, radius);
	cylinderPtr.setMaterial(glossyPtr2);
	w.addObject(cylinderPtr);

	
	// box

	GlossyReflector glossyPtr3 = new GlossyReflector();
	glossyPtr3.setSamples(numSamples, 1000.0);			
	glossyPtr3.setKa(0.2); 
	glossyPtr3.setKd(0.3);
	glossyPtr3.setKs(0.0);
	glossyPtr3.setExp(1000.0);
	glossyPtr3.setCd(0.4, 0.5, 1.0);   // blue
	glossyPtr3.setKr(0.9);
	glossyPtr3.setExponent(1000.0);
	glossyPtr3.setCr(0.4, 0.5, 1.0);   // blue
	
	Box boxPtr = new Box(new Point3D(-35, 0, -110), new Point3D(-25, 60, 65));
	boxPtr.setMaterial(glossyPtr3);
	w.addObject(boxPtr);
	
	
	// ground plane with checker
	
	// checker with stripes
	
	PlaneChecker checkerPtr = new PlaneChecker();
	checkerPtr.setSize(20.0);		
	checkerPtr.setOutlineWidth(2.0);
	checkerPtr.setColor1(1.0, 0.75, 0.5);
	checkerPtr.setColor2(Utility.WHITE);
	checkerPtr.setOutlineColor(Utility.BLACK); 
	
	// Using the following glossy reflector material with the exponent = 1.0, 
	// allows us to render color bleeding from the objects onto the plane.
	// You will have to write w material and the PlaneChecker material
	// as exercises.
	
	SV_GlossyReflector svGlossyPtr = new SV_GlossyReflector();		
	svGlossyPtr.setSamples(numSamples, 1.0);			
	svGlossyPtr.setKa(0.0);
	svGlossyPtr.setKd(0.0);
	svGlossyPtr.setKs(0.0);
	svGlossyPtr.setCs(checkerPtr);
	svGlossyPtr.setExp(1.0);
	svGlossyPtr.setCd(checkerPtr);
	svGlossyPtr.setKr(0.75); 
	svGlossyPtr.setExponent(1.0);
	svGlossyPtr.setCr(checkerPtr);
	
	Plane planePtr = new Plane(new Point3D(0, 0.01, 0), new Normal(0, 1, 0));
	planePtr.setMaterial(svGlossyPtr);
	w.addObject(planePtr);


    }
    
}
