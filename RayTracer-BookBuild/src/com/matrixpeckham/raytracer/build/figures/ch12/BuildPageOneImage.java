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
package com.matrixpeckham.raytracer.build.figures.ch12;

import com.matrixpeckham.raytracer.cameras.FishEye;
import com.matrixpeckham.raytracer.cameras.StereoCamera;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.compound.Grid;
import com.matrixpeckham.raytracer.geometricobjects.compound.SolidCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
import com.matrixpeckham.raytracer.tracers.RayCast;
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
public class BuildPageOneImage implements BuildWorldFunction{

    @Override
    public void build(World w) {
	int numSamples = 16;
	
	w.vp.setHres(300);
	w.vp.setVres(300);
	w.vp.setSamples(numSamples);
	
	w.tracer = new RayCast(w);
	w.backgroundColor =new RGBColor(0.0, 0.2, 0.4);
	
	// stereo camera w.setup using fisheye cameras
	
	double fov = 130.0;

	FishEye leftCameraPtr = new FishEye();
	leftCameraPtr.setFov(fov);
	
	FishEye rightCameraPtr = new FishEye();
	rightCameraPtr.setFov(fov);

	StereoCamera stereoPtr = new StereoCamera();	
	stereoPtr.setLeftCamera(leftCameraPtr);
	stereoPtr.setRightCamera(rightCameraPtr);
	stereoPtr.useParallelViewing();
	//stereoPtr.useTransverseViewing();
	stereoPtr.setPixelGap(5);       // in pixels
	stereoPtr.setEye(100.0, 750.0, 0.0);
	stereoPtr.setLookat(new Point3D(0.0));
	stereoPtr.computeUVW();
	stereoPtr.setStereoAngle(5.0);  // in degrees
	stereoPtr.setupCameras(w.vp); 
	w.setCamera(stereoPtr);
	
		
	PointLight lightPtr1 = new PointLight();
	lightPtr1.setLocation(0, 350, 0);   
	lightPtr1.scaleRadiance(3.0);
	lightPtr1.setShadows(true);
	w.addLight(lightPtr1);
	
	
	PointLight lightPtr2 = new PointLight();
	lightPtr2.setLocation(0, 2000, 0);  
	lightPtr2.scaleRadiance(2.0);
	lightPtr2.scaleRadiance(1.0);
	lightPtr2.setShadows(true);
	w.addLight(lightPtr2);
	
	
	// cylinder materials
		
	Matte mattePtr1 = new Matte();			
	mattePtr1.setCd(1, 1, 0);     	// yellow
	mattePtr1.setKa(0.4); 
	mattePtr1.setKd(0.5); 
	
	Matte mattePtr2 = new Matte();			
	mattePtr2.setCd(1, 0.35, 0);     // red
	mattePtr2.setKa(0.4); 
	mattePtr2.setKd(0.5);  
	
	Matte mattePtr3 = new Matte();			
	mattePtr3.setCd(0, 0.5, 0.5);    // cyan
	mattePtr3.setKa(0.4); 
	mattePtr3.setKd(0.5); 
	
	
	// construct rings of cylinders stored in a grid
		
	Grid gridPtr = new Grid();
	
	int numCylinders = 12;
	double bottom = 0.0;
	double top = 500.0;
	double radius = 25.0;
	double ringRadius = 150.0;
	double deltaPhi = 360.0 / numCylinders;  // in degrees
	
	// inner ring
	
	for (int j = 0; j < numCylinders; j++) {
		Instance cylinderPtr = new Instance (new SolidCylinder(bottom, top, radius));
		cylinderPtr.setMaterial(mattePtr1);
		cylinderPtr.translate(0, 0, ringRadius);
		cylinderPtr.rotateY(j * deltaPhi);
		cylinderPtr.computeBoundingBox();
//		w.addObject(cylinderPtr);
		gridPtr.addObject(cylinderPtr);
	}
		
	
	// middle ring
	
	numCylinders = 16;
	top = 450.0;
	ringRadius = 350.0;
	deltaPhi = 360.0 / numCylinders;  // in degrees
	
	for (int j = 0; j < numCylinders; j++) {
		Instance cylinderPtr = new Instance (new SolidCylinder(bottom, top, radius));
		cylinderPtr.setMaterial(mattePtr3);
		cylinderPtr.translate(0, 0, ringRadius);
		cylinderPtr.rotateY(j * deltaPhi);
		cylinderPtr.computeBoundingBox();
//		w.addObject(cylinderPtr);
		gridPtr.addObject(cylinderPtr);
	}
	
	
	// outer ring
	
	numCylinders = 16;
	top = 325.0;
	ringRadius = 500.0;
	deltaPhi = 360.0 / numCylinders;  // in degrees
	
	for (int j = 0; j < numCylinders; j++) {
		Instance cylinderPtr = new Instance (new SolidCylinder(bottom, top, radius));
		cylinderPtr.setMaterial(mattePtr2);
		cylinderPtr.translate(0, 0, ringRadius);
		cylinderPtr.rotateY(j * deltaPhi + 10);
		cylinderPtr.computeBoundingBox();
//		w.addObject(cylinderPtr);
		gridPtr.addObject(cylinderPtr);
	}
	
	gridPtr.setupCells();
	w.addObject(gridPtr);
	
	
	// ground plane with checkers:
	
	Checker3D checkerPtr = new Checker3D();
	checkerPtr.setSize(200.0); 
	checkerPtr.setColor1(0.5);   
	checkerPtr.setColor2(Utility.WHITE);
	
	SV_Matte svMattePtr = new SV_Matte();		
	svMattePtr.setKa(0.20);
	svMattePtr.setKd(0.50); 
	svMattePtr.setCd(checkerPtr);	
	Plane planePtr = new Plane(new Point3D(0, 1, 0), new Normal(0, 1, 0));
	planePtr.setMaterial(svMattePtr);
	w.addObject(planePtr);    }
    
}
