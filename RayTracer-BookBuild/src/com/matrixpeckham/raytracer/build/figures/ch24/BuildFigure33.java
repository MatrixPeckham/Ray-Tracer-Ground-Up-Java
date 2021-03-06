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
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.PlaneChecker;
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
public class BuildFigure33 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 24.33

// Figure 24.33 in the book has a specular highligtht on the sphere that's on the top
// of the box. This is inconsistent with Figure 24.6 and Figure 24.7(a).
// The code below does not render a specular highlight on w sphere.

 												

	int numSamples = 1;
	
	w.vp.setHres(600); 
	w.vp.setVres(600);
	w.vp.setSamples(numSamples);
	w.vp.setMaxDepth(10);
	
	w.tracer = new Whitted(w);	
	w.backgroundColor = new RGBColor(0.15); 
	
	Ambient ambientPtr = new Ambient();
	ambientPtr.scaleRadiance(0.5);
	w.setAmbient(ambientPtr);
	
			
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(75, 40, 100); 
	pinholePtr.setLookat(-10, 39, 0);  
	pinholePtr.setViewDistance(360);

	
	// looking at center of sphere on top of large box
	
	pinholePtr.setEye(75, 39, 100); 
	pinholePtr.setLookat(-30, 59, 35);  
	pinholePtr.setViewDistance(1500);

	
	pinholePtr.computeUVW(); 
	w.setCamera(pinholePtr);
	
		
	PointLight lightPtr = new PointLight();
	lightPtr.setLocation(150, 150, 0);  
	lightPtr.scaleRadiance(3.0);
	lightPtr.setShadows(true);
	w.addLight(lightPtr);

	
	// yellow-green reflective sphere
	
	Reflective reflectivePtr1 = new Reflective();			
	reflectivePtr1.setKa(0.25); 
	reflectivePtr1.setKd(0.5);
	reflectivePtr1.setCd(0.75, 0.75, 0);    	// yellow
	reflectivePtr1.setKs(0.15);
	reflectivePtr1.setExp(100.0);
	reflectivePtr1.setKr(0.75);
	reflectivePtr1.setCr(Utility.WHITE); 			// default color
	
	double radius = 23.0;
	Sphere spherePtr1 = new Sphere(new Point3D(38, radius, -25), radius); 
	spherePtr1.setMaterial(reflectivePtr1);
	w.addObject(spherePtr1);
	
	
	// orange non-reflective sphere
	
	Matte mattePtr1 = new Matte();			
	mattePtr1.setKa(0.45); 
	mattePtr1.setKd(0.75);
	mattePtr1.setCd(0.75, 0.25, 0);   // orange
	
	Sphere spherePtr2 = new Sphere(new Point3D(-7, 10, 42), 20);
	spherePtr2.setMaterial(mattePtr1);      
	w.addObject(spherePtr2);
	
	
	// sphere on top of box

	Reflective reflectivePtr2 = new Reflective();			
	reflectivePtr2.setKa(0.35); 
	reflectivePtr2.setKd(0.75);		// for Figure 24.33(a)
//	reflectivePtr2.setKd(1.0);		// for Figure 24.33(b)
	reflectivePtr2.setCd(Utility.BLACK); 
	reflectivePtr2.setKs(0.0);		// default value
	reflectivePtr2.setExp(1.0);		// default value, but irrelevant in w case
	reflectivePtr2.setKr(0.75);
	reflectivePtr2.setCr(Utility.WHITE); 

	Sphere spherePtr3 = new Sphere(new Point3D(-30, 59, 35), 20);
	spherePtr3.setMaterial(reflectivePtr2);     
	w.addObject(spherePtr3);

	
	// cylinder
	
	Reflective reflectivePtr3 = new Reflective();			
	reflectivePtr3.setKa(0.35); 
	reflectivePtr3.setKd(0.5);
	reflectivePtr3.setCd(0, 0.5, 0.75);   // cyan
	reflectivePtr3.setKs(0.2);
	reflectivePtr3.setExp(100.0);
	reflectivePtr3.setKr(0.75);
	reflectivePtr3.setCr(Utility.WHITE);
	
	double bottom 			= 0.0;
	double top 				= 85;   
	double cylinderRadius	= 22;
	SolidCylinder cylinderPtr = new SolidCylinder(bottom, top, cylinderRadius);
	cylinderPtr.setMaterial(reflectivePtr3);
	w.addObject(cylinderPtr);
	
	
	// box
	
	Matte mattePtr2 = new Matte();			
	mattePtr2.setKa(0.15); 
	mattePtr2.setKd(0.5);
	mattePtr2.setCd(0.75, 1.0, 0.75);   // light green
	
	Box boxPtr = new Box(new Point3D(-35, 0, -110), new Point3D(-25, 60, 65));
	boxPtr.setMaterial(mattePtr2);
	w.addObject(boxPtr);

	
	// ground plane
	
	PlaneChecker checkerPtr = new PlaneChecker();
	checkerPtr.setSize(20.0);		
	checkerPtr.setOutlineWidth(2.0);
	checkerPtr.setColor1(Utility.WHITE);
	checkerPtr.setColor2(Utility.WHITE);
	checkerPtr.setOutlineColor(Utility.BLACK); 
	
	SV_Matte svMattePtr = new SV_Matte();		
	svMattePtr.setKa(0.30);
	svMattePtr.setKd(0.9);
	svMattePtr.setCd(checkerPtr); 
	
	Plane planePtr = new Plane(new Point3D(0), new Normal(0, 1, 0));
	planePtr.setMaterial(svMattePtr);
	w.addObject(planePtr);



    }
    
}
