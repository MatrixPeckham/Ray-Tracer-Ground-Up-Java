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

import com.matrixpeckham.raytracer.cameras.Orthographic;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure40B implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 24.40(b).

 												

	int numSamples = 16;
	
	w.vp.setHres(600);	  		
	w.vp.setVres(600);
	w.vp.setMaxDepth(1); 
	w.vp.setPixelSize(0.0073);
	w.vp.setSamples(numSamples);
	
	w.backgroundColor = Utility.WHITE;
	
	w.tracer = new Whitted(w);
	
	Orthographic orthographicPtr = new Orthographic();
	orthographicPtr.setEye(0.0, 0.866, 100.0);    
	orthographicPtr.setLookat(0.0, 0.866, 0.0);  
	orthographicPtr.computeUVW();     
	w.setCamera(orthographicPtr);

	
	double kr = 1.0;
	
	// top sphere 
	
	Reflective reflectivePtr1 = new Reflective();
	reflectivePtr1.setKa(0.0); 
	reflectivePtr1.setKd(0.0); 
	reflectivePtr1.setCd(Utility.BLACK);
	reflectivePtr1.setKs(0.0);
	reflectivePtr1.setExp(1.0);			
	reflectivePtr1.setKr(kr);
	reflectivePtr1.setCr(1, 0, 0);  // red
	
	Sphere spherePtr1 = new Sphere(new Point3D(0.0, 1.732, 0.0), 1.0);
	spherePtr1.setMaterial(reflectivePtr1);
	w.addObject(spherePtr1);
	
	// left bottom sphere 
	
	Reflective reflectivePtr2 = new Reflective();
	reflectivePtr2.setKa(0.0); 
	reflectivePtr2.setKd(0.0); 
	reflectivePtr2.setCd(Utility.BLACK);
	reflectivePtr2.setKs(0.0);
	reflectivePtr2.setExp(1.0);				
	reflectivePtr2.setKr(kr);
	reflectivePtr2.setCr(0, 1, 0);  // green
	
	Sphere spherePtr2 = new Sphere(new Point3D(-1.0, 0.0, 0.0), 1.0);
	spherePtr2.setMaterial(reflectivePtr2);
	w.addObject(spherePtr2);
	
	// right bottom sphere 
	
	Reflective reflectivePtr3 = new Reflective();
	reflectivePtr3.setKa(0.0); 
	reflectivePtr3.setKd(0.0); 
	reflectivePtr3.setCd(Utility.BLACK);
	reflectivePtr3.setKs(0.0);
	reflectivePtr3.setExp(1.0);			
	reflectivePtr3.setKr(kr);
	reflectivePtr3.setCr(0, 0, 1);  // blue
	
	Sphere spherePtr3 = new Sphere(new Point3D(1.0, 0.0, 0.0), 1.0);
	spherePtr3.setMaterial(reflectivePtr3);
	w.addObject(spherePtr3);
	
	
	
    }
    
}
