/*
 * Copyright (C) 2016 William Matrix Peckham
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
package com.matrixpeckham.raytracer.build.figures.ch28;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.compound.Compound;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Dielectric;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure29 implements BuildWorldFunction{

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.


// This renders the scene for Figure 28.29

	int numSamples = 25;
	
	w.vp.setHres(600);	  		
	w.vp.setVres(600);
	w.vp.setSamples(numSamples);
	w.vp.setMaxDepth(6);

	w.backgroundColor = new RGBColor(0.0, 0.13, 0.1);

	w.tracer = new Whitted(w);
	
	Ambient ambientPtr = new Ambient();
	ambientPtr.setColor(0.45, 0.5, 0.45);
	ambientPtr.scaleRadiance(0.25);
	w.setAmbient(ambientPtr);

	
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(0, 0, 10);
	pinholePtr.setLookat(0, 0, 0);    
	pinholePtr.setViewDistance(2800.0);		
	pinholePtr.computeUVW();     
	w.setCamera(pinholePtr);
	
		
	Directional lightPtr1 = new Directional();
	lightPtr1.setDirection(10, 10, 10);   
	lightPtr1.scaleRadiance(7.0);  
	lightPtr1.setShadows(false);
	w.addLight(lightPtr1);
		
	Directional lightPtr2 = new Directional();
	lightPtr2.setDirection(-1, 0, 0);   
	lightPtr2.scaleRadiance(7.0);  
	lightPtr2.scaleRadiance(13.0);  
	lightPtr2.setShadows(false);
	w.addLight(lightPtr2);
	

	// transparent unit sphere at the origin
		
	Dielectric dielectricPtr = new Dielectric();
	dielectricPtr.setIorIn(2.42);		// glass
	dielectricPtr.setIorOut(1.0);		// air
	dielectricPtr.setCfIn(Utility.WHITE);
	dielectricPtr.setCfOut(Utility.WHITE); 
		
	Sphere spherePtr1 = new Sphere();
	spherePtr1.setMaterial(dielectricPtr);
	
	
	// Utility.RED reflective sphere inside the transparent sphere
	// the Utility.RED comes from the diffuse color
	
	// the Reflective parameters below are for the reflective sphere in a diamond sphere
			
	Reflective reflectivePtr = new Reflective();
	reflectivePtr.setKa(0.1);	
	reflectivePtr.setKd(1.0);  
	reflectivePtr.setCd(Utility.RED);
	reflectivePtr.setKs(0.3);  
	reflectivePtr.setExp(200.0);
	reflectivePtr.setKr(0.75);
	reflectivePtr.setKr(1.0);
	reflectivePtr.setCr(Utility.WHITE);
	
	double radius = 0.1;
	double distance = 0.8;   // from center of transparent sphere 
		
	Sphere spherePtr2 = new Sphere(new Point3D(0, 0, distance), radius);
	spherePtr2.setMaterial(reflectivePtr);
	
	// store the spheres in a compound object
	
	Compound spheresPtr = new Compound();
	spheresPtr.addObject(spherePtr1);
	spheresPtr.addObject(spherePtr2);
	
	// now store compound object in an instance so that we can rotate it
	
	Instance rotatedSpheresPtr = new Instance(spheresPtr);
//	rotatedSpheresPtr.rotateY(172.5);	// for Figure 28.29(a)
//	rotatedSpheresPtr.rotateY(173.0);  	// for Figure 28.29(b)
	rotatedSpheresPtr.rotateY(178.0);  	// for Figure 28.29(c)
	w.addObject(rotatedSpheresPtr);
	
	
	// ground plane
	
	Checker3D checker3DPtr = new Checker3D();
	checker3DPtr.setSize(50.0); 
	checker3DPtr.setColor1(0.5); 
	checker3DPtr.setColor2(1.0); 
	
	SV_Matte svMattePtr = new SV_Matte();		
	svMattePtr.setKa(0.25);
	svMattePtr.setKd(0.5);
	svMattePtr.setCd(checker3DPtr);
		
	Plane planePtr = new Plane(new Point3D(0, -40.5, 0), new Normal(0, 1, 0));  
	planePtr.setMaterial(svMattePtr);
	w.addObject(planePtr);
}


    
}
