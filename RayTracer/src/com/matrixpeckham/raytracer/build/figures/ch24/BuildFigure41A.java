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
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
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
public class BuildFigure41A implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 24.41(a).

 												

	int numSamples = 25;
	
	w.vp.setHres(600);      
	w.vp.setVres(600);
	w.vp.setPixelSize(0.004);     
	w.vp.setMaxDepth(1);
	w.vp.setSamples(numSamples);
	
	w.backgroundColor = new RGBColor(0.0, 0.25, 0.0);   // dark green
	
	w.tracer = new Whitted(w);	
	
	Orthographic orthoPtr = new Orthographic();
	orthoPtr.setEye(0, 10, 0);  
	orthoPtr.setLookat(new Point3D(0));
	orthoPtr.computeUVW();     
	w.setCamera(orthoPtr);
	 
		
	Directional lightPtr = new Directional();
	lightPtr.setDirection(0, 1, 0);
	lightPtr.scaleRadiance(2.5);
	lightPtr.setShadows(true);
	w.addLight(lightPtr);
	
	
	// sphere
		
	Reflective reflectivePtr = new Reflective();
	reflectivePtr.setKa(0.0); 
	reflectivePtr.setKd(0.0); 
	reflectivePtr.setCd(Utility.BLACK);
	reflectivePtr.setKs(0.0);
	reflectivePtr.setExp(1.0);			
	reflectivePtr.setKr(0.9);
	reflectivePtr.setCr(1.0, 1.0, 0);  // yellow
	
	Sphere spherePtr = new Sphere(new Point3D(0.0, 5.0, 0.0), 1);
	spherePtr.setMaterial(reflectivePtr);
	w.addObject(spherePtr);
	
	
	// plane with checker
	
	Checker3D checkerPtr = new Checker3D();
	checkerPtr.setSize(2);
	checkerPtr.setColor1(0.0);
	checkerPtr.setColor2(1.0);
	
	SV_Matte svMattePtr = new SV_Matte();		
	svMattePtr.setKa(0.33);
	svMattePtr.setKd(0.75);
	svMattePtr.setCd(checkerPtr);

	Plane planePtr = new Plane(new Point3D(0.0), new Normal(0, 1, 0));
	planePtr.setMaterial(svMattePtr);
	planePtr.setShadows(false);
	w.addObject(planePtr);	




    }
    
}
