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
package com.matrixpeckham.raytracer.build.figures.ch17;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.PartCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.AmbientOccluder;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure14 implements BuildWorldFunction{

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 17.14

	int numSamples = 256;
	
	w.vp.setHres(400);	  		
	w.vp.setVres(400);
	w.vp.setSamples(numSamples);  
	
	w.tracer = new RayCast(w);
		
	MultiJittered samplerPtr = new MultiJittered(numSamples); 

	
	AmbientOccluder occluderPtr = new AmbientOccluder();
	occluderPtr.scaleRadiance(1.0);
	occluderPtr.setMinAmount(0.0);
	occluderPtr.setSampler(samplerPtr);
	w.setAmbient(occluderPtr);			
			
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(-10, 2, 0);
	pinholePtr.setLookat(0.35, 0.45, 0); 
	pinholePtr.setViewDistance(1500);	
	pinholePtr.computeUVW();     
	w.setCamera(pinholePtr);
	
	
	// cylinder
	
	Matte mattePtr1 = new Matte();			
	mattePtr1.setKa(0.85);		
	mattePtr1.setKd(0.6);
	mattePtr1.setCd(1, 0.5, 0); 
	
	
	double bottom	= 0;
	double top 		= 1;
	double radius 	= 1;
	double phiMin 	= 0;
	double phiMax 	= 180;
	
	PartCylinder cylinderPtr = new PartCylinder(	bottom, 
															top, 
															radius, 
															phiMin, 
															phiMax);
	cylinderPtr.setMaterial(mattePtr1);
	w.addObject(cylinderPtr);	
	
	
	// ground plane
	
	Matte mattePtr2 = new Matte();			
	mattePtr2.setKa(0.5);		
	mattePtr2.setKd(0.75);
	mattePtr2.setCd(Utility.WHITE);   
	
	Plane planePtr = new Plane(new Point3D(0), new Normal(0, 1, 0));
	planePtr.setMaterial(mattePtr2);
	w.addObject(planePtr);	


    }
    
}
