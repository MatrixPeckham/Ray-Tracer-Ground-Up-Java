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
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Rectangle;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.materials.SV_Emissive;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
import com.matrixpeckham.raytracer.textures.procedural.SphereChecker;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure15A implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figures 24.15(a)
// The SphereTexture texture is part of Exercise 30.4, 
// but the code is included in the Code download

 												

	int numSamples = 25;
	
	w.vp.setHres(400);
	w.vp.setVres(400);
	w.vp.setSamples(numSamples);
	w.vp.setMaxDepth(10);
	
	w.tracer = new Whitted(w);
	
	w.backgroundColor = Utility.BLACK; 
	
	Ambient ambientPtr = new Ambient();
	ambientPtr.scaleRadiance(0.25);
	w.setAmbient(ambientPtr);	


	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(-200, 100, -100);   
	pinholePtr.setLookat(0, 40, 30);    
	pinholePtr.setViewDistance(1000);
	pinholePtr.computeUVW();     
	w.setCamera(pinholePtr);
	
	
	// point light 
	
	PointLight lightPtr1 = new PointLight();
	lightPtr1.setLocation(40, 50, 0); 
	lightPtr1.scaleRadiance(5.0);
	lightPtr1.setShadows(true);
	w.addLight(lightPtr1);
	

	// point light 
	
	PointLight lightPtr2 = new PointLight();
	lightPtr2.setLocation(-10, 20, 10); 
	lightPtr2.scaleRadiance(4.0);
	lightPtr2.setShadows(true);
	w.addLight(lightPtr2);
	
	
	// directional light 
	
	Directional lightPtr3 = new Directional();
	lightPtr3.setDirection(-1, 0, 0); 
	lightPtr3.scaleRadiance(4.0);
	lightPtr3.setShadows(true);
	w.addLight(lightPtr3);
	
	
	// large emissive sphere with lines 
	// w is not a light source
	
	SphereChecker checkerPtr1 = new SphereChecker();  // can only be applied to a generic sphere
	checkerPtr1.setNumHorizontal(16);
	checkerPtr1.setNumVertical(16);    
	checkerPtr1.setHorizontalLineWidth(0.075);
	checkerPtr1.setVerticalLineWidth(0.075);
	checkerPtr1.setColor1(0.75, 1.0, 0.25);   // yellow
	checkerPtr1.setColor2(0.75, 1.0, 0.25);   // yellow
	checkerPtr1.setLineColor(Utility.BLACK);
	
	SV_Emissive svEmissivePtr = new SV_Emissive();
	svEmissivePtr.setCe(checkerPtr1);
	svEmissivePtr.scaleRadiance(1.5);      
	
		
	Sphere spherePtr1 = new Sphere();
	spherePtr1.setMaterial(svEmissivePtr);
	
	Instance spherePtr2 = new Instance(spherePtr1);
	spherePtr2.scale(new Vector3D(30));
	spherePtr2.translate(0, 50, 45);
	w.addObject(spherePtr2);
	
	
	// reflective sphere
	
	Reflective reflectivePtr = new Reflective();			
	reflectivePtr.setKa(0.0); 
	reflectivePtr.setKd(0.0); 
	reflectivePtr.setCd(new RGBColor(1.0));
	reflectivePtr.setKs(0.15);
	reflectivePtr.setExp(300.0);
	reflectivePtr.setKr(0.9);
	reflectivePtr.setCr(1.0, 0.75, 0.25);  // orange
	
	Sphere spherePtr3 = new Sphere();
	spherePtr3.setMaterial(reflectivePtr);

	// we are using a transformed generic sphere here because it will
	// have a textured material in Figure 24.15(c) and Figure 24.16.

	Instance spherePtr4 = new Instance(spherePtr3);
	spherePtr4.scale(3.0);
	spherePtr4.translate(0.0, 4.5, 0.0);
	w.addObject(spherePtr4);
	
	
	// rectangle
	
	Checker3D checkerPtr2 = new Checker3D();
	checkerPtr2.setSize(4.0);
	checkerPtr2.setColor1(0.5);  
	checkerPtr2.setColor2(Utility.WHITE);	 
	
	SV_Matte svMattePtr = new SV_Matte();		
	svMattePtr.setKa(0.5);
	svMattePtr.setKd(0.35);
	svMattePtr.setCd(checkerPtr2);		
		
	Rectangle rectanglePtr = new Rectangle(new Point3D(-20, -0.001, -100), new Vector3D(0, 0, 120), new Vector3D(40, 0, 0)); 
	rectanglePtr.setMaterial(svMattePtr);
	w.addObject(rectanglePtr);



    }
    
}
