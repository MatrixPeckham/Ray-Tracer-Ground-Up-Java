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
import com.matrixpeckham.raytracer.geometricobjects.primitives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.GlossyReflector;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
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
public class BuildFigure09 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 25.9.

	int numSamples = 256;
	
	w.vp.setHres(600);	  		
	w.vp.setVres(200);
	w.vp.setSamples(numSamples);
	w.vp.setMaxDepth(1);
	
	w.backgroundColor = Utility.BLACK;
	w.tracer = new Whitted(w);
	
	Ambient ambientPtr = new Ambient(); 
	ambientPtr.scaleRadiance(0.5);
	w.setAmbient(ambientPtr);
	
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(-2, 300, 400);
	pinholePtr.setLookat(-2, 8, 0);
	pinholePtr.setViewDistance(3500);
	pinholePtr.computeUVW();  
	w.setCamera(pinholePtr);
	
		
	PointLight lightPtr = new PointLight();
	lightPtr.setLocation(20, 30, 40); 
	lightPtr.scaleRadiance(4.0);
	lightPtr.setShadows(false);
	w.addLight(lightPtr);

	// boxes
	
	double checkerSize = 4.0;
	
	double exp1 = 100000.0;
	GlossyReflector glossyPtr1 = new GlossyReflector();
	glossyPtr1.setCd(0, 0.5, 0.25);
	glossyPtr1.setKa(0.2);
	glossyPtr1.setKd(0.3);	
	glossyPtr1.setKs(0.0);
	glossyPtr1.setExp(exp1);
	glossyPtr1.setKr(0.5);
	glossyPtr1.setSampler(new MultiJittered(numSamples), exp1);
		
	Box boxPtr1 = new Box();
	boxPtr1.setP0(-10.0 * checkerSize, 0, -0.5);
	boxPtr1.setP1(-6.0 * checkerSize, 5 * checkerSize, 0.0);
	boxPtr1.setMaterial(glossyPtr1);
	w.addObject(boxPtr1);
	
	double exp2 = 10000.0;
	GlossyReflector glossyPtr2 = new GlossyReflector();
	glossyPtr2.setCd(0, 0.5, 0.25);
	glossyPtr2.setKa(0.2);
	glossyPtr2.setKd(0.3);	
	glossyPtr2.setKs(0.0);
	glossyPtr2.setExp(exp2);
	glossyPtr2.setKr(0.5);
	glossyPtr2.setSampler(new MultiJittered(numSamples), exp2);
	
	Box boxPtr2 = new Box();
	boxPtr2.setP0(-5.0 * checkerSize, 0, -0.5);
	boxPtr2.setP1(-checkerSize, 5 * checkerSize, 0.0);
	boxPtr2.setMaterial(glossyPtr2);
	w.addObject(boxPtr2);
	
	double exp3 = 1000.0;
	GlossyReflector glossyPtr3 = new GlossyReflector();
	glossyPtr3.setCd(0, 0.5, 0.25);
	glossyPtr3.setKa(0.2);
	glossyPtr3.setKd(0.3);	
	glossyPtr3.setKs(0.0);
	glossyPtr3.setExp(exp3);
	glossyPtr3.setKr(0.5);
	glossyPtr3.setSampler(new MultiJittered(numSamples), exp3);
	
	Box boxPtr3 = new Box();
	boxPtr3.setP0(0, 0, -0.5);
	boxPtr3.setP1(4 * checkerSize, 5 * checkerSize, 0.0);
	boxPtr3.setMaterial(glossyPtr3);
	w.addObject(boxPtr3);
	
	double exp4 = 100.0;
	GlossyReflector glossyPtr4 = new GlossyReflector();
	glossyPtr4.setCd(0, 0.5, 0.25);
	glossyPtr4.setKa(0.2);
	glossyPtr4.setKd(0.3);	
	glossyPtr4.setKs(0.0);
	glossyPtr4.setExp(exp4);
	glossyPtr4.setKr(0.5);
	glossyPtr4.setSampler(new MultiJittered(numSamples), exp4);
	
	Box boxPtr4 = new Box();
	boxPtr4.setP0(5 * checkerSize, 0, -0.5);
	boxPtr4.setP1(9 * checkerSize, 5 * checkerSize, 0.0);
	boxPtr4.setMaterial(glossyPtr4);
	w.addObject(boxPtr4);
	
	
	// vertical back plane
	
	Matte mattePtr = new Matte();			
	mattePtr.setKa(0.25);    
	mattePtr.setKd(0.25);
	mattePtr.setCd(Utility.WHITE);
	
	Plane planePtr1 = new Plane(new Point3D(0, 0, -4), new Normal(0, 0, 1)); 
	planePtr1.setMaterial(mattePtr);
	w.addObject(planePtr1);
	
	
	// ground plane with checker
		
	Checker3D checkerPtr = new Checker3D();
	checkerPtr.setSize(checkerSize);
	checkerPtr.setColor1(Utility.BLACK);
	checkerPtr.setColor2(Utility.WHITE);
	 
	SV_Matte svMattePtr = new SV_Matte();		
	svMattePtr.setKa(0.8);  
	svMattePtr.setKd(0.4);
	svMattePtr.setCd(checkerPtr);
	
	Plane planePtr2 = new Plane(new Point3D(0, -0.01, 0), new Normal(0, 1, 0)); 
	planePtr2.setMaterial(svMattePtr);
	w.addObject(planePtr2);	



    }
    
}
