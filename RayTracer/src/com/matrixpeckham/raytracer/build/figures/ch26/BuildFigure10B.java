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
package com.matrixpeckham.raytracer.build.figures.ch26;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Rectangle;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.AreaLight;
import com.matrixpeckham.raytracer.materials.Emissive;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
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
public class BuildFigure10B implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.


// This builds the scene for Figure 26.10(b)

	int numSamples = 1;
		
	w.vp.setHres(300);	  		
	w.vp.setVres(300);
	w.vp.setSamples(numSamples); 
	w.vp.setMaxDepth(0);
	
	w.backgroundColor = Utility.BLACK;
	
	w.tracer = new Whitted(w);
	
	Ambient ambientPtr = new Ambient();
	ambientPtr.scaleRadiance(0.0);
	w.setAmbient(ambientPtr);
	
		
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(27.6, 27.4, -80.0);
	pinholePtr.setLookat(27.6, 27.4, 0.0);
	pinholePtr.setViewDistance(400);      
	pinholePtr.computeUVW();     
	w.setCamera(pinholePtr);
	
	
	Point3D p0;
	Vector3D a, b;
	Normal normal;
	
	// box dimensions
	
	double width 	= 55.28;   	// x direction
	double height 	= 54.88;  	// y direction
	double depth 	= 55.92;	// z direction
	
		
	Emissive emissivePtr = new Emissive();
	emissivePtr.setCe(1.0, 0.73, 0.4);   
	emissivePtr.scaleRadiance(100.0);
	
	p0 = new Point3D(21.3, height - 0.001, 22.7);
	a = new Vector3D(0.0, 0.0, 10.5);
	b = new Vector3D(13.0, 0.0, 0.0);
	normal = new Normal(0.0, -1.0, 0.0);
	
	Rectangle lightPtr = new Rectangle(p0, a, b, normal);
	lightPtr.setMaterial(emissivePtr);
	lightPtr.setSampler(new MultiJittered(numSamples));
	lightPtr.setShadows(false);
	w.addObject(lightPtr);
	
	AreaLight ceilingLightPtr = new AreaLight();   
	ceilingLightPtr.setObject(lightPtr);		
	ceilingLightPtr.setShadows(true);
	w.addLight(ceilingLightPtr);
	
		
	// left wall
	
	Matte mattePtr1 = new Matte();
	mattePtr1.setKa(0.0);
	mattePtr1.setKd(0.6); 
	mattePtr1.setCd(0.57, 0.025, 0.025);	 // red
	
	p0 = new Point3D(width, 0.0, 0.0);
	a = new Vector3D(0.0, 0.0, depth);
	b = new Vector3D(0.0, height, 0.0);
	normal = new Normal(-1.0, 0.0, 0.0);
	Rectangle leftWallPtr = new Rectangle(p0, a, b, normal);
	leftWallPtr.setMaterial(mattePtr1);
	w.addObject(leftWallPtr);
	
	
	// right wall
	
	Matte mattePtr2 = new Matte();
	mattePtr2.setKa(0.0);
	mattePtr2.setKd(0.6); 
	mattePtr2.setCd(0.37, 0.59, 0.2);	 // green  
	
	p0 = new Point3D(0.0, 0.0, 0.0);
	a = new Vector3D(0.0, 0.0, depth);
	b = new Vector3D(0.0, height, 0.0);
	normal = new Normal(1.0, 0.0, 0.0);
	Rectangle rightWallPtr = new Rectangle(p0, a, b, normal);
	rightWallPtr.setMaterial(mattePtr2);
	w.addObject(rightWallPtr);
	
	
	// back wall
	
	Matte mattePtr3 = new Matte();
	mattePtr3.setKa(0.0);
	mattePtr3.setKd(0.6); 
	mattePtr3.setCd(Utility.WHITE);
	
	p0 = new Point3D(0.0, 0.0, depth);
	a = new Vector3D(width, 0.0, 0.0);
	b = new Vector3D(0.0, height, 0.0);
	normal = new Normal(0.0, 0.0, -1.0);
	Rectangle backWallPtr = new Rectangle(p0, a, b, normal);
	backWallPtr.setMaterial(mattePtr3);
	w.addObject(backWallPtr);
	
	
	// floor
	
	p0 = new Point3D(0.0, 0.0, 0.0);
	a = new Vector3D(0.0, 0.0, depth);
	b = new Vector3D(width, 0.0, 0.0);
	normal = new Normal(0.0, 1.0, 0.0);
	Rectangle floorPtr = new Rectangle(p0, a, b, normal);
	floorPtr.setMaterial(mattePtr3);
	w.addObject(floorPtr);
	
	
	// ceiling
	
	p0 = new Point3D(0.0, height, 0.0);
	a = new Vector3D(0.0, 0.0, depth);
	b = new Vector3D(width, 0.0, 0.0);
	normal = new Normal(0.0, -1.0, 0.0);
	Rectangle ceilingPtr = new Rectangle(p0, a, b, normal);
	ceilingPtr.setMaterial(mattePtr3);
	w.addObject(ceilingPtr);

	
	// the two boxes defined as 5 rectangles each
	
	// short box
	
	// top
	
	p0 = new Point3D(13.0, 16.5, 6.5);
	a = new Vector3D(-4.8, 0.0, 16.0);
	b = new Vector3D(16.0, 0.0, 4.9);
	normal = new Normal(0.0, 1.0, 0.0);
	Rectangle shortTopPtr = new Rectangle(p0, a, b, normal);
	shortTopPtr.setMaterial(mattePtr3);
	w.addObject(shortTopPtr);
	
	
	// side 1
	
	p0 = new Point3D(13.0, 0.0, 6.5);
	a = new Vector3D(-4.8, 0.0, 16.0);
	b = new Vector3D(0.0, 16.5, 0.0);
	Rectangle shortSidePtr1 = new Rectangle(p0, a, b);
	shortSidePtr1.setMaterial(mattePtr3);
	w.addObject(shortSidePtr1);
	
	
	// side 2
	
	p0 = new Point3D(8.2, 0.0, 22.5);
	a = new Vector3D(15.8, 0.0, 4.7);
	Rectangle shortSidePtr2 = new Rectangle(p0, a, b);
	shortSidePtr2.setMaterial(mattePtr3);
	w.addObject(shortSidePtr2);
	
	
	// side 3
	
	p0 = new Point3D(24.2, 0.0, 27.4);
	a = new Vector3D(4.8, 0.0, -16.0);
	Rectangle shortSidePtr3 = new Rectangle(p0, a, b);
	shortSidePtr3.setMaterial(mattePtr3);
	w.addObject(shortSidePtr3);
	
	
	// side 4
	
	p0 = new Point3D(29.0, 0.0, 11.4);
	a = new Vector3D(-16.0, 0.0, -4.9);
	Rectangle shortSidePtr4 = new Rectangle(p0, a, b);
	shortSidePtr4.setMaterial(mattePtr3);
	w.addObject(shortSidePtr4);
	
	
	
	// tall box
	
	// top
	
	p0 = new Point3D(42.3, 33.0, 24.7);
	a = new Vector3D(-15.8, 0.0, 4.9);
	b = new Vector3D(4.9, 0.0, 15.9);
	normal = new Normal(0.0, 1.0, 0.0);
	Rectangle tallTopPtr = new Rectangle(p0, a, b, normal);
	tallTopPtr.setMaterial(mattePtr3);
	w.addObject(tallTopPtr);
	
	
	// side 1
	
	p0 = new Point3D(42.3, 0.0, 24.7);
	a = new Vector3D(-15.8, 0.0, 4.9);
	b = new Vector3D(0.0, 33.0, 0.0);
	Rectangle tallSidePtr1 = new Rectangle(p0, a, b);
	tallSidePtr1.setMaterial(mattePtr3);
	w.addObject(tallSidePtr1);
	
	
	// side 2
	
	p0 = new Point3D(26.5, 0.0, 29.6);
	a = new Vector3D(4.9, 0.0, 15.9);
	Rectangle tallSidePtr2 = new Rectangle(p0, a, b);
	tallSidePtr2.setMaterial(mattePtr3);
	w.addObject(tallSidePtr2);
	
	
	// side 3
	
	p0 = new Point3D(31.4, 0.0, 45.5);
	a = new Vector3D(15.8, 0.0, -4.9);
	Rectangle tallSidePtr3 = new Rectangle(p0, a, b);
	tallSidePtr3.setMaterial(mattePtr3);
	w.addObject(tallSidePtr3);
	
	
	// side 4
	
	p0 = new Point3D(47.2, 0.0, 40.6);
	a = new Vector3D(-4.9, 0.0, -15.9);
	Rectangle tallSidePtr4 = new Rectangle(p0, a, b);
	tallSidePtr4.setMaterial(mattePtr3);
	w.addObject(tallSidePtr4);
    }
    
}
