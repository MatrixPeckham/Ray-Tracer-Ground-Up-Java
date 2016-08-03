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
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.materials.GlossyReflector;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure07 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 25.7

	int numSamples = 100;
	
	w.vp.setHres(400);	  		
	w.vp.setVres(400);
	w.vp.setSamples(numSamples);
	w.vp.setMaxDepth(1);

	w.backgroundColor = new RGBColor(0.8, 0.9, 1);   // light blue
	
	w.tracer = new Whitted(w);	 
	
	Ambient ambientPtr = new Ambient();	
	ambientPtr.scaleRadiance(0.5);
	w.setAmbient(ambientPtr);
	
	Pinhole pinholePtr = new Pinhole();	
	pinholePtr.setEye(0, 0, 20);     	
	pinholePtr.setViewDistance(3500); 
	pinholePtr.setLookat(new Point3D(0));		
	pinholePtr.computeUVW();     
	w.setCamera(pinholePtr);
	
	
	// glossy reflector sphere	
	
	double exp = 1.0;  
	
	GlossyReflector glossyPtr = new GlossyReflector();
	glossyPtr.setSamples(numSamples, exp);			
	glossyPtr.setKa(0.0); 
	glossyPtr.setKd(0.0);
	glossyPtr.setKs(0.0);
	glossyPtr.setExp(exp);
	glossyPtr.setCd(Utility.BLACK);
	glossyPtr.setKr(0.8);
	glossyPtr.setExponent(exp);
	glossyPtr.setCr(Utility.WHITE); 
	
	Sphere spherePtr = new Sphere();  
	spherePtr.setMaterial(glossyPtr);
	w.addObject(spherePtr);


    }
    
}
