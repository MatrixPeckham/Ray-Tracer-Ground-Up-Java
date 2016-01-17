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
import com.matrixpeckham.raytracer.geometricobjects.primitives.ConcaveSphere;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.materials.Dielectric;
import com.matrixpeckham.raytracer.materials.SV_Emissive;
import com.matrixpeckham.raytracer.textures.procedural.SphereChecker;
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
public class BuildFigure50 implements BuildWorldFunction{

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.


// This builds the scene for Figure 28.50

	int numSamples = 16;
	
	w.vp.setHres(600);	  		
	w.vp.setVres(600);
	w.vp.setSamples(numSamples);
	w.vp.setMaxDepth(15);
	
	w.tracer = new Whitted(w);
	
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(1, 0.5, 4);      
	pinholePtr.setLookat(-3, -10, 0); 	   
	pinholePtr.setViewDistance(200.0);  
	pinholePtr.setExposureTime(0.17);
	pinholePtr.computeUVW();     
	w.setCamera(pinholePtr);
	
	
	// transparent sphere
	
	Dielectric dielectricPtr = new Dielectric();
	dielectricPtr.setIorIn(2.42);		// diamond
	dielectricPtr.setIorOut(1.0);		// air
	dielectricPtr.setCfIn(Utility.WHITE);
	dielectricPtr.setCfOut(Utility.WHITE); 
		
	Sphere spherePtr1 = new Sphere(new Point3D(0.0), 4.0);
	spherePtr1.setMaterial(dielectricPtr);
		
	
	// scale the sphere into an ellipsoid
	
	Instance ellipsoidPtr = new Instance(spherePtr1);
	ellipsoidPtr.scale(1.0, 0.75, 2.0);
	w.addObject(ellipsoidPtr);
		
	
	// emissive concave sphere with checkers
	
	RGBColor c1=new RGBColor(0, 0.25, 0.35);
	RGBColor c2=new RGBColor(1, 1, 0.5);
	
	SphereChecker checkerPtr = new SphereChecker();
	checkerPtr.setNumHorizontal(12);  
	checkerPtr.setNumVertical(6);    
	checkerPtr.setHorizontalLineWidth(0.0);
	checkerPtr.setVerticalLineWidth(0.0);
	checkerPtr.setColor1(c1);   
	checkerPtr.setColor2(c2); 
	checkerPtr.setLineColor(Utility.BLACK);  
		
	SV_Emissive svEmissivePtr = new SV_Emissive();
	svEmissivePtr.scaleRadiance(1.0);
	svEmissivePtr.setCe(checkerPtr);

	ConcaveSphere spherePtr2 = new ConcaveSphere();  // you will have to implement w
	spherePtr2.setMaterial(svEmissivePtr);		 // or use a default ConcavePartSphere
	
	Instance spherePtr3 = new Instance(spherePtr2);
	spherePtr3.scale(100.0);
	w.addObject(spherePtr3);
}


}
