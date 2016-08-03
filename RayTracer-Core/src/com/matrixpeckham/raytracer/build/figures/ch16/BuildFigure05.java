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
package com.matrixpeckham.raytracer.build.figures.ch16;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.compound.Grid;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
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
public class BuildFigure05 implements BuildWorldFunction{

    @Override
    public void build(World w) {
	int numSamples = 25;
	
	w.vp.setHres(600);			
	w.vp.setVres(400);
	w.vp.setSamples(numSamples);
	
	w.tracer = new RayCast(w);
			
	Pinhole cameraPtr = new Pinhole();
	
	// for Figure 16.5(a)

	cameraPtr.setEye(350, 1000, 500);   
	cameraPtr.setLookat(-175, 550, -40);  
	cameraPtr.setViewDistance(350);
	

	
	// for Figure 16.5(b)
	
	cameraPtr.setEye(1500, 750, 250);   
	cameraPtr.setLookat(-50, 300, -600);  
	cameraPtr.setViewDistance(650);
	

	cameraPtr.computeUVW(); 
	w.setCamera(cameraPtr);

	
	PointLight lightPtr = new PointLight();
	lightPtr.setLocation(1500, 750, 250);   
	lightPtr.scaleRadiance(4.5);
	lightPtr.setShadows(true);
	w.addLight(lightPtr);
	
	
	// box materials
		
	Matte mattePtr1 = new Matte();			
	mattePtr1.setCd(0, 0.5, 0.5);     // cyan
	mattePtr1.setKa(0.4); 
	mattePtr1.setKd(0.5); 
	
	Matte mattePtr2 = new Matte();			
	mattePtr2.setCd(1, 0, 0);     	  // red
	mattePtr2.setKa(0.4); 
	mattePtr2.setKd(0.5);  
	
	Matte mattePtr3 = new Matte();			
	mattePtr3.setCd(0.5, 0.6, 0);     // green
	mattePtr3.setKa(0.4); 
	mattePtr3.setKd(0.5); 
	
	
	// Construct rows of boxes stored in a grid
		
	Grid gridPtr = new Grid();
	
	// first row
	
	int numBoxes = 11;
	double wx = 50;
	double wz = 50;
	double h = 300;
	double s = 100;
	
	for (int j = 0; j < numBoxes; j++) {
		Box boxPtr = new Box(	new Point3D(-wx, 0, -(j + 1) * wz - j * s), 
								new Point3D(0, h, - j * wz - j * s));
		boxPtr.setMaterial(mattePtr1);
	//	w.addObject(boxPtr);
		gridPtr.addObject(boxPtr);
	}
	
	
	// second row
	
	h = 450;
	
	for (int j = 0; j <= numBoxes; j++) {
		Box boxPtr = new Box(	new Point3D(-wx -wx - s, 0, -(j + 1) * wz - j * s), 
								new Point3D(-wx - s, h, - j * wz - j * s));
		boxPtr.setMaterial(mattePtr2);
//		w.addObject(boxPtr);
		gridPtr.addObject(boxPtr);
	}
	
	
	// third row
	
	h = 600; 
	
	for (int j = 0; j <= numBoxes; j++) {
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
	checkerPtr.setSize(wx); 
	checkerPtr.setColor1(0.7);  
	checkerPtr.setColor2(Utility.WHITE);
	
	SV_Matte svMattePtr1 = new SV_Matte();		
	svMattePtr1.setKa(0.15);
	svMattePtr1.setKd(1.0); 
	svMattePtr1.setCd(checkerPtr);	
	Plane planePtr = new Plane(new Point3D(0, 1, 0), new Normal(0, 1, 0));
	planePtr.setMaterial(svMattePtr1);
	w.addObject(planePtr);    }
    
}
