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
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Rectangle;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure18A implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 24.18(a)

 												

	int numSamples = 16;
	
	w.vp.setHres(600);	  		
	w.vp.setVres(600);        
	w.vp.setSamples(numSamples);
	w.vp.setMaxDepth(19);	
	
	w.tracer = new Whitted(w);
			
	Pinhole pinholePtr = new Pinhole();
	
	pinholePtr.setEye(7.5, 3, 9.5);
	pinholePtr.setLookat(new Point3D());
	pinholePtr.setViewDistance(300);
	pinholePtr.computeUVW(); 
	w.setCamera(pinholePtr);
	
	
	// four point lights near the ceiling
	// these don't use distance attenuation

	PointLight lightPtr1 = new PointLight();
	lightPtr1.setLocation(10, 10, 0); 
	lightPtr1.scaleRadiance(2.0); 
	lightPtr1.setShadows(true); 
    w.addLight(lightPtr1);
    
    PointLight lightPtr2 = new PointLight();
	lightPtr2.setLocation(0, 10, 10); 
	lightPtr2.scaleRadiance(2.0); 
	lightPtr2.setShadows(true); 
    w.addLight(lightPtr2);
    
    PointLight lightPtr3 = new PointLight();
	lightPtr3.setLocation(-10, 10, 0); 
	lightPtr3.scaleRadiance(2.0); 
	lightPtr3.setShadows(true); 
    w.addLight(lightPtr3);
    
    PointLight lightPtr4 = new PointLight();
	lightPtr4.setLocation(0, 10, -10); 
	lightPtr4.scaleRadiance(2.0); 
	lightPtr4.setShadows(true); 
    w.addLight(lightPtr4);

		
	// sphere
	// w is the only reflective object with a direct illumination shading component
	
	Reflective reflectivePtr1 = new Reflective();			
	reflectivePtr1.setKa(0.1); 
	reflectivePtr1.setKd(0.4); 
	reflectivePtr1.setCd(0, 0, 1);   	 // blue
	reflectivePtr1.setKs(0.25);
	reflectivePtr1.setExp(100.0);
	reflectivePtr1.setKr(0.85); 
	reflectivePtr1.setCr(0.75, 0.75, 1);  // blue 
	
	Sphere spherePtr1 = new Sphere(new Point3D(0, 0.5, 0), 4); 
	spherePtr1.setMaterial(reflectivePtr1);
	w.addObject(spherePtr1);
		
	
	// the walls, the ceiling, and the floor of the room are defined as planes
	// the shape is a cube
	
	double roomSize = 11.0;
	
	// floor  (-ve yw)
	
	Matte mattePtr1 = new Matte();
	mattePtr1.setKa(0.1);   
	mattePtr1.setKd(0.50);
	mattePtr1.setCd(0.25);     // medium grey
	
	Plane floorPtr = new Plane(new Point3D(0, -roomSize,  0), new Normal(0, 1, 0));
	floorPtr.setMaterial(mattePtr1);        
	w.addObject(floorPtr);
	
	
	// ceiling  (+ve yw)
	
	Matte mattePtr2 = new Matte();   
	mattePtr2.setKa(0.35);   
	mattePtr2.setKd(0.50);
	mattePtr2.setCd(Utility.WHITE);
	
	Plane ceilingPtr = new Plane(new Point3D(0, roomSize,  0), new Normal(0, -1, 0));
	ceilingPtr.setMaterial(mattePtr2);        
	w.addObject(ceilingPtr);
	
	
	// back wall  (-ve zw)
	
	Matte mattePtr3 = new Matte();
	mattePtr3.setKa(0.15); 
	mattePtr3.setKd(0.60);
	mattePtr3.setCd(0.5, 0.75, 0.75);     // cyan
	
	Plane backWallPtr = new Plane(new Point3D(0, 0,  -roomSize), new Normal(0, 0, 1));
	backWallPtr.setMaterial(mattePtr3);        
	w.addObject(backWallPtr);
	
	// front wall  (+ve zw)
	
	Plane frontWallPtr = new Plane(new Point3D(0, 0,  roomSize), new Normal(0, 0, -1));
	frontWallPtr.setMaterial(mattePtr3);        
	w.addObject(frontWallPtr);
	
	// left wall  (-ve xw)
	
	Matte mattePtr4 = new Matte();
	mattePtr4.setKa(0.15); 
	mattePtr4.setKd(0.60);
	mattePtr4.setCd(0.71, 0.40, 0.20);   // orange
	
	Plane leftWallPtr = new Plane(new Point3D(-roomSize, 0, 0), new Normal(1, 0, 0));
	leftWallPtr.setMaterial(mattePtr4);        
	w.addObject(leftWallPtr);
	
	// right wall  (+ve xw)
	
	Plane rightWallPtr = new Plane(new Point3D(roomSize, 0, 0), new Normal(-1, 0, 0));
	rightWallPtr.setMaterial(mattePtr4);        
	w.addObject(rightWallPtr);
	
	
	// mirrors on the walls
	// the right wall has no mirror
	
	double mirrorSize 	= 8;  	// the mirror size
	double offset 		= 1.0;  // the mirror offset from the walls
	
	// mirror material
	// w has no direct illumination and a slight green tint
	
	Reflective reflectivePtr2 = new Reflective();			
	reflectivePtr2.setKa(0); 
	reflectivePtr2.setKd(0);
	reflectivePtr2.setCd(Utility.BLACK); 
	reflectivePtr2.setKs(0);
	reflectivePtr2.setKr(0.9);
	reflectivePtr2.setCr(0.9, 1.0, 0.9);  // light green
	
	// back wall mirror  (-ve zw)

	Point3D p0;
	Vector3D a, b;
	
	p0 = new Point3D(-mirrorSize, -mirrorSize, -(roomSize - offset));
	a = new Vector3D(2.0 * mirrorSize, 0, 0);
	b = new Vector3D(0, 2.0 * mirrorSize, 0);
	Normal n=new Normal(0, 0, 1);
	Rectangle rectanglePtr1 = new Rectangle(p0, a, b, n);
	rectanglePtr1.setMaterial(reflectivePtr2); 
	w.addObject(rectanglePtr1);
	
	
	// front wall mirror  (+ve zw)
	
	p0 = new Point3D(-mirrorSize, -mirrorSize, +(roomSize - offset));
	n = new Normal(0, 0, -1);
	Rectangle rectanglePtr2 = new Rectangle(p0, a, b, n);
	rectanglePtr2.setMaterial(reflectivePtr2); 
	w.addObject(rectanglePtr2);
	
	
	// left wall mirror  (-ve xw)
	
	p0 = new Point3D(-(roomSize - offset), -mirrorSize, +mirrorSize);
	a = new Vector3D(0, 0, -2.0 * mirrorSize);
	n = new Normal(1, 0, 0);
	Rectangle rectanglePtr3 = new Rectangle(p0, a, b, n);
	rectanglePtr3.setMaterial(reflectivePtr2); 
	w.addObject(rectanglePtr3);


	// horizontal mirror underneath the sphere
	// w has no direct illumination and a lemon color
	
	Reflective reflectivePtr3 = new Reflective();			
	reflectivePtr3.setKa(0); 
	reflectivePtr3.setKd(0);
	reflectivePtr3.setCd(Utility.BLACK); 
	reflectivePtr3.setKs(0);
	reflectivePtr3.setKr(1);
	reflectivePtr3.setCr(1, 1, 0.5);  // lemon
	
	double yw = -4.0;   // the yw location of the mirror
	
	p0 = new Point3D(-mirrorSize, yw, -mirrorSize);
	a = new Vector3D(0, 0, 2.0 * mirrorSize);
	b = new Vector3D(2.0 * mirrorSize, 0, 0);
	n = new Normal(0, 1, 0);
	Rectangle rectanglePtr4 = new Rectangle(p0, a, b, n);
	rectanglePtr4.setMaterial(reflectivePtr3); 
	w.addObject(rectanglePtr4);




    }
    
}
