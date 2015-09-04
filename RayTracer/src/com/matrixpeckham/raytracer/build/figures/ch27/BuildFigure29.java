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
package com.matrixpeckham.raytracer.build.figures.ch27;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.parametric.Torus;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Rectangle;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.materials.Transparent;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
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
public class BuildFigure29 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.


// This builds the scene for Figure 27.29
	int numSamples = 9;
	
	w.vp.setHres(600);	  		
	w.vp.setVres(600);
	w.vp.setSamples(numSamples);		
	w.vp.setMaxDepth(5);		
	
	w.backgroundColor = new RGBColor(0.0, 0.3, 0.25);
	
	w.tracer = new Whitted(w);
	
	Ambient ambientPtr = new Ambient();
	ambientPtr.scaleRadiance(0.25);
	w.setAmbient(ambientPtr);
		
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(-8, 5.5, 20);      
	pinholePtr.setLookat(0, 4.5, 0);     
	pinholePtr.setViewDistance(1400.0);  
	pinholePtr.computeUVW();     
	w.setCamera(pinholePtr);
	
	
	// point light 
	
	PointLight lightPtr1 = new PointLight();
	lightPtr1.setLocation(40, 50, 0); 
	lightPtr1.scaleRadiance(4.5);
	lightPtr1.setShadows(true);
	w.addLight(lightPtr1);
	

	// point light 
	
	PointLight lightPtr2 = new PointLight();
	lightPtr2.setLocation(-10, 20, 10); 
	lightPtr2.scaleRadiance(4.5);
	lightPtr2.setShadows(true);
	w.addLight(lightPtr2);
	
	
	// directional light 
	
	Directional lightPtr3 = new Directional();
	lightPtr3.setDirection(-1, 0, 0); 
	lightPtr3.scaleRadiance(4.5);
	lightPtr3.setShadows(true);
	w.addLight(lightPtr3);
	
	
	// transparent torus
	
	Transparent glassPtr = new Transparent();
	glassPtr.setKs(0.2);
	glassPtr.setExp(2000.0);
	glassPtr.setIor(1.5);		
	glassPtr.setKr(0.1);
	glassPtr.setKt(0.9);
	
	Instance torusPtr2 = new Instance (new Torus(3.0, 0.8));
	torusPtr2.setMaterial(glassPtr);
	torusPtr2.rotateX(90);
	torusPtr2.rotateY(20);
	torusPtr2.translate(0, 4.5, 0);
	w.addObject(torusPtr2);
	
	
	// Utility.RED sphere
		
	Reflective	reflectivePtr = new Reflective();
	reflectivePtr.setKa(0.3);
	reflectivePtr.setKd(0.3); 
	reflectivePtr.setCd(Utility.RED); 
	reflectivePtr.setKs(0.2);
	reflectivePtr.setExp(2000.0);
	reflectivePtr.setKr(0.25);
	
	Sphere spherePtr2 = new Sphere(new Point3D(4, 4, -6), 3);
	spherePtr2.setMaterial(reflectivePtr);
	w.addObject(spherePtr2);

		
	Checker3D checkerPtr = new Checker3D();
	checkerPtr.setSize(4.0);
	checkerPtr.setColor1(0.75);  
	checkerPtr.setColor2(Utility.WHITE);	
	
	SV_Matte svMattePtr = new SV_Matte();		
	svMattePtr.setKa(0.5);
	svMattePtr.setKd(0.35);
	svMattePtr.setCd(checkerPtr);	
	
	
	// rectangle
	
	Point3D p0=new Point3D(-20, 0, -100);
	Vector3D a=new Vector3D(0, 0, 120);
	Vector3D b=new Vector3D(40, 0, 0);
	
	Rectangle rectanglePtr = new Rectangle(p0, a, b); 
	rectanglePtr.setMaterial(svMattePtr);
	w.addObject(rectanglePtr);	
}


    
}
