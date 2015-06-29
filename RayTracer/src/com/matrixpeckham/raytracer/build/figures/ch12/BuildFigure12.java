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

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.cameras.StereoCamera;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Phong;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure12 implements BuildWorldFunction {

    @Override
    public void build(World w) {
int numSamples = 1;

	w.vp.setHres(200);	
	w.vp.setVres(200);
	w.vp.setPixelSize(0.05);		
	w.vp.setSamples(numSamples);	
	
	w.tracer = new RayCast(w);			

	float vpd = 100;  // view plane distance for 200 x 200 pixel images

	Pinhole leftCameraPtr = new Pinhole();  
	leftCameraPtr.setViewDistance(vpd);
	
	Pinhole rightCameraPtr = new Pinhole(); 
	rightCameraPtr.setViewDistance(vpd);
	
	StereoCamera stereoPtr = new StereoCamera();		
	stereoPtr.setLeftCamera(leftCameraPtr);
	stereoPtr.setRightCamera(rightCameraPtr);
	stereoPtr.useParallelViewing();
//	stereoPtr.useTransverseViewing();
	stereoPtr.setPixelGap(5);       // in pixels
	stereoPtr.setEye(5, 0, 100);
	stereoPtr.setLookat(new Point3D());
	stereoPtr.computeUVW();
	stereoPtr.setStereoAngle(0.75);  // in degrees
	stereoPtr.setupCameras(w.vp);
	w.setCamera(stereoPtr);
	
	PointLight lightPtr = new PointLight();
	lightPtr.setLocation(100, 100, 100);
	lightPtr.scaleRadiance(3);
	lightPtr.setShadows(true);
	w.addLight(lightPtr);
	
	
	// sphere materials
	
	Phong phongPtr1 = new Phong();			
	phongPtr1.setCd(0, 0.5, 0.5);   	// cyan
	phongPtr1.setKa(0.4); 
	phongPtr1.setKd(0.6);
	phongPtr1.setKs(0.2); 
	phongPtr1.setExp(20); 
	
	Phong phongPtr2 = new Phong();
	phongPtr2.setCd(0.85, 0.6, 0.2); 	// brown				
	phongPtr2.setKa(0.3); 
	phongPtr2.setKd(0.7);
	phongPtr2.setKs(0.08);
	phongPtr2.setExp(20);  
	
	Phong phongPtr3 = new Phong();			
	phongPtr3.setCd(1, 1, 0);     		// yellow
	phongPtr3.setKa(0.2); 
	phongPtr3.setKd(0.6);
	phongPtr3.setKs(0.08); 
	phongPtr3.setExp(20);   
	
	// the spheres
	
	Sphere sphere1 = new Sphere(new Point3D(0, 0, 35), 0.75);
	sphere1.setMaterial(phongPtr1);
	w.addObject(sphere1);
	
	Sphere sphere2 = new Sphere(new Point3D(0), 2);
	sphere2.setMaterial(phongPtr2);
	w.addObject(sphere2);
	
	Sphere sphere3 = new Sphere(new Point3D(1.5, 0, -80), 2);
	sphere3.setMaterial(phongPtr3);
	w.addObject(sphere3);	
    }
    
}
