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
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.compound.SolidCone;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
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
public class BuildFigure36 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 24.36
// This produces different reflections for parts (c) and (d),
// because I couldn't reproduce the original images in (c) and (d).
// The exact patterns aren't important. 
// It's more intructive to think about parts (a) and (b), and why there are 
// reflective patterns in (c) and (d).

 												

	int numSamples = 9;
	
	w.vp.setHres(600);	  		
	w.vp.setVres(600);
	w.vp.setPixelSize(0.00425);
	w.vp.setMaxDepth(1);
	w.vp.setSamples(numSamples);
	
	w.backgroundColor = new RGBColor(0.0, 0.3, 0.25);
	
	w.tracer = new Whitted(w);
	
	
	Orthographic orthographicPtr = new Orthographic();
	orthographicPtr.setEye(1.5, 1000.0, 0.0);     
	orthographicPtr.setLookat(1.5, 0.0, 0.0);
	orthographicPtr.computeUVW();
	w.setCamera(orthographicPtr);
	
	Directional lightPtr = new Directional();
	lightPtr.setDirection(0.0, 1.0, 0.0);
	lightPtr.scaleRadiance(2.5);
	lightPtr.setShadows(false);
	w.addLight(lightPtr);
	
		
	// cone
	
	Reflective reflectivePtr = new Reflective();
	reflectivePtr.setKa(0.0);	
	reflectivePtr.setKd(0.0);
	reflectivePtr.setCd(0, 0, 0);
	reflectivePtr.setKs(0.0);
	reflectivePtr.setExp(1.0);
	reflectivePtr.setKr(0.9);
	reflectivePtr.setCr(1.0, 0.8, 0.0);	// orange	
	
	Instance conePtr = new Instance(new SolidCone());
	conePtr.setMaterial(reflectivePtr);
//	conePtr.scale(1.0, 0.9, 1.0);			// for Figure 24.36(a)
//	conePtr.scale(1.0, 1.0, 1.0);			// for Figure 24.36(b)
//	conePtr.scale(1.0, 1.1, 1.0);			// for Figure 24.36(c)
	conePtr.scale(1.0, 3.0, 1.0);			// for Figure 24.36(d)
	conePtr.translate(1.5, -1, 0);
	w.addObject(conePtr);
	
	// ground plane with checker
		
	Checker3D checkerPtr = new Checker3D();
	checkerPtr.setSize(0.45);		
	checkerPtr.setColor1(Utility.WHITE);   
	checkerPtr.setColor2(0.5);
	
	SV_Matte svMattePtr = new SV_Matte();		
	svMattePtr.setKa(0.25);
	svMattePtr.setKd(0.85);
	svMattePtr.setCd(checkerPtr);

	Plane planePtr1 = new Plane(new Point3D(0, -1.01, 0), new Normal(0, 1, 0));  // for jug
	planePtr1.setMaterial(svMattePtr);
	w.addObject(planePtr1);


    }
    
}
