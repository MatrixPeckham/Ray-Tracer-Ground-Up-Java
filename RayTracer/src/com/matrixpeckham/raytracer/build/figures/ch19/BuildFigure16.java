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
package com.matrixpeckham.raytracer.build.figures.ch19;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.parametric.Torus;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Phong;
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
public class BuildFigure16 implements BuildWorldFunction{

    @Override
    public void build(World w) {
	int numSamples = 16;
	
	w.vp.setHres(400);
	w.vp.setVres(400);
	w.vp.setSamples(numSamples);	
	
	w.tracer = new RayCast(w);
	
	Pinhole camera = new Pinhole();
	camera.setEye(5, 25, 20); 
	camera.setLookat(0, 0, 0); 
	camera.setViewDistance(1500); 
	camera.computeUVW();     
	w.setCamera(camera); 

	w.backgroundColor = Utility.BLACK;
		
	Directional lightPtr2 = new Directional();
	lightPtr2.setDirection(200, 75, 100);
	lightPtr2.scaleRadiance(4.0);
	lightPtr2.setShadows(true);
	w.addLight(lightPtr2);
	
	Phong phongPtr2 = new Phong();
	phongPtr2.setKa(0.25);
	phongPtr2.setKd(0.5);
	phongPtr2.setCd(1, 1, 0.45);  // lemon
	phongPtr2.setKs(0.05);
	phongPtr2.setExp(5);
	
	double a = 2.0;	 	// for all parts
	double b = 0.15;		// for Figure 19.16(a)
//	double b = 0.5;	   	// for Figure 19.16(b)  default torus
//	double b = 2;      	// for Figure 19.16(c)
																
	Torus torusPtr = new Torus(a, b);												
	torusPtr.setMaterial(phongPtr2);
	w.addObject(torusPtr);
	
	
	// ground plane with checker:
	
	Checker3D checkerPtr = new Checker3D();
	checkerPtr.setSize(1.0); 
	checkerPtr.setColor1(0.8);  
	checkerPtr.setColor2(1);
	
	SV_Matte svMattePtr = new SV_Matte();		
	svMattePtr.setKa(0.30);
	svMattePtr.setKd(0.6);  
	svMattePtr.setCd(checkerPtr);
	
	Plane planePtr = new Plane(new Point3D(0, -2, 0), new Normal(0, 1, 0));
	planePtr.setMaterial(svMattePtr);
	w.addObject(planePtr);    }
    
}
