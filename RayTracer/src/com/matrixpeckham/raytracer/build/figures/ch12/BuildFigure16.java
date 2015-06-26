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
import com.matrixpeckham.raytracer.geometricobjects.compound.Grid;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Plane;
import com.matrixpeckham.raytracer.lights.Ambient;
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
import static java.awt.Color.white;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure16 implements BuildWorldFunction{

    @Override
    public void build(World w) {
	int numSamples = 16;
	
	w.vp.setHres(200);			
	w.vp.setVres(200);
	w.vp.setPixelSize(1);
	w.vp.setSamples(numSamples);				

	w.tracer = new RayCast(w);	
	w.backgroundColor = new RGBColor(0.0, 0.2, 0.4);
	
	Ambient ambientPtr = new Ambient();
	ambientPtr.scaleRadiance(0.567);
	w.setAmbient(ambientPtr);
			
	double fov = 200;
	
	FishEye leftFisheyePtr = new FishEye();
	leftFisheyePtr.setFov(fov);
	
	FishEye rightFisheyePtr = new FishEye();
	rightFisheyePtr.setFov(fov);
	
	StereoCamera stereoPtr = new StereoCamera(); 
	stereoPtr.setLeftCamera(leftFisheyePtr);
	stereoPtr.setRightCamera(rightFisheyePtr);
	stereoPtr.useParallelViewing();
// 	stereoPtr.useTransverseViewing();
	stereoPtr.setEye(-150, 1000, -500);
	stereoPtr.setLookat(-160, 300, -550);  
	stereoPtr.computeUVW();
	stereoPtr.setStereoAngle(5);  
	stereoPtr.setupCameras(w.vp); 	
	w.setCamera(stereoPtr);	
		
	PointLight lightPtr2 = new PointLight();
	lightPtr2.setLocation(1500, 750, 250);
	lightPtr2.scaleRadiance(4.5);
	lightPtr2.setShadows(true);
	w.addLight(lightPtr2);
	
	
	// box materials
		
	Matte mattePtr1 = new Matte();			
	mattePtr1.setCd(0, 0.5, 0.5);     // cyan
	mattePtr1.setKa(0.8); 
	mattePtr1.setKd(0.3); 
	
	Matte mattePtr2 = new Matte();			
	mattePtr2.setCd(1, 0, 0);     	  // red
	mattePtr2.setKa(0.8); 
	mattePtr2.setKd(0.3);  
	
	Matte mattePtr3 = new Matte();			
	mattePtr3.setCd(1, 1, 0);     	// yellow
	mattePtr3.setKa(0.8); 
	mattePtr3.setKd(0.3); 
	
			
	Grid gridPtr = new Grid();
	
	// first row
	
	int numBoxes = 11;
	double wx = 50;
	double wz = 50;
	double h = 500;
	double s = 100;
	
	for (int j = 0; j < numBoxes; j++) {
		Box boxPtr = new Box(	new Point3D(-wx, 0, -(j + 1) * wz - j * s), 
								new Point3D(0, h, - j * wz - j * s));
		boxPtr.setMaterial(mattePtr1);
	//	w.addObject(boxPtr);
		gridPtr.addObject(boxPtr);
	}
	
	
	// second row
	
	h = 600;
	
	for (int j = 0; j < numBoxes; j++) {
		Box boxPtr = new Box(	new Point3D(-wx -wx - s, 0, -(j + 1) * wz - j * s), 
								new Point3D(-wx - s, h, - j * wz - j * s));
		boxPtr.setMaterial(mattePtr2);
//		w.addObject(boxPtr);
		gridPtr.addObject(boxPtr);
	}
	
	
	// third row
	
	h = 750; 
	
	for (int j = 0; j < numBoxes; j++) {
		Box boxPtr = new Box(	new Point3D(-wx - 2 * wx - 2 * s, 0, -(j + 1) * wz - j * s), 
								new Point3D(-2 * wx - 2 * s, h, - j * wz - j * s));
		boxPtr.setMaterial(mattePtr3);
//		w.addObject(boxPtr);
		gridPtr.addObject(boxPtr);
	}
	
	gridPtr.setupCells();
	w.addObject(gridPtr);
	
	
	// ground plane with checker
	
	Checker3D checkerPtr = new Checker3D();
	checkerPtr.setSize(6 * wx); 
	checkerPtr.setColor1(0.7);  
	checkerPtr.setColor2(Utility.WHITE);
	
	SV_Matte svMattePtr1 = new SV_Matte();		
	svMattePtr1.setKa(0.20);
	svMattePtr1.setKd(0.50); 
	svMattePtr1.setCd(checkerPtr);	
	Plane planePtr = new Plane(new Point3D(0, 1, 0),new Normal(0, 1, 0));
	planePtr.setMaterial(svMattePtr1);
	w.addObject(planePtr);    }
    
}
