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
import com.matrixpeckham.raytracer.geometricobjects.compound.WireframeBox;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Sphere;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Phong;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure13 implements BuildWorldFunction {

    @Override
    public void build(World w) {
	int numSamples = 16;
	
	w.vp.setHres(200);	  		
	w.vp.setVres(200);
	w.vp.setSamples(numSamples);
	w.vp.setPixelSize(0.0325); 
	
	w.tracer = new RayCast(w);	
	w.backgroundColor = Utility.BLACK;
	
	double vpd = 10.0; 
	
	Pinhole leftCameraPtr = new Pinhole();
	leftCameraPtr.setViewDistance(vpd);   
	w.setCamera(leftCameraPtr);
	
	Pinhole rightCameraPtr = new Pinhole();
	rightCameraPtr.setViewDistance(vpd);   
	w.setCamera(rightCameraPtr);
	
	StereoCamera stereoPtr = new StereoCamera(leftCameraPtr, rightCameraPtr);
	stereoPtr.useParallelViewing();
// 	stereoPtr.useTransverseViewing();
	stereoPtr.setPixelGap(5);
	stereoPtr.setEye(-2, 0, 5);    
	stereoPtr.setLookat(new Point3D());
	stereoPtr.computeUVW();
	stereoPtr.setStereoAngle(5.0);  // in degrees
	stereoPtr.setupCameras(w.vp); 
	w.setCamera(stereoPtr);
	
	Directional lightPtr = new Directional();
	lightPtr.setDirection(new Vector3D(20, 30, 40)); 
	lightPtr.scaleRadiance(2.5);
	lightPtr.setShadows(true);
	w.addLight(lightPtr);
			
	Phong phongPtr = new Phong();			
	phongPtr.setCd(new RGBColor(0.7));					
	phongPtr.setKa(0.3);    
	phongPtr.setKd(1);
	phongPtr.setKs(0.3);
	phongPtr.setExp(50);
	
	Point3D p0 =new Point3D(-1.0);
	Point3D p1=new Point3D(1.0);
	double bevelRadius = 0.04;
	
	WireframeBox boxPtr = new WireframeBox(p0, p1, bevelRadius);
	boxPtr.setMaterial(phongPtr);
	w.addObject(boxPtr);
    }
    
}
