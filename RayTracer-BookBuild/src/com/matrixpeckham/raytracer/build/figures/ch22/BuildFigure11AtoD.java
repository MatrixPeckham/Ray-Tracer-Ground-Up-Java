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
package com.matrixpeckham.raytracer.build.figures.ch22;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.compound.Grid;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;
import static java.lang.Math.pow;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure11AtoD implements BuildWorldFunction {

    @Override
    public void build(World w) {
int numSamples = 1;
	
	w.vp.setHres(400);
	w.vp.setVres(400);
	w.vp.setSamples(numSamples);
	
	w.tracer = new RayCast(w);
	
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(0, 0, 20);
	pinholePtr.setLookat(0, 0, 0);
	pinholePtr.setViewDistance(3600); 
	pinholePtr.computeUVW();
	w.setCamera(pinholePtr);
	
	Directional lightPtr = new Directional();   
	lightPtr.setDirection(-10, 20, 20);				
	lightPtr.scaleRadiance(3.0);   
	lightPtr.setShadows(false);
	w.addLight(lightPtr);
	
//	int numSpheres = 1000;			// for Figure 22.11(a)
//	int numSpheres = 10000;		// for Figure 22.11(b)
//	int numSpheres = 100000;		// for Figure 22.11(c)
	int numSpheres = 1000000;		// for Figure 22.11(d)			
	
	double volume = 0.1 / numSpheres;
	double radius = pow(0.75 * volume / Math.PI, 0.333333);
	
	Utility.setRandSeed(15);
	
	Grid gridPtr = new Grid();  
	
	for (int j = 0; j < numSpheres; j++) {
		Matte mattePtr = new Matte();
		mattePtr.setKa(0.25);
		mattePtr.setKd(0.75);
		mattePtr.setCd(Utility.randDouble(), Utility.randDouble(), Utility.randDouble());
		
		Sphere spherePtr = new Sphere(); 
		spherePtr.setRadius(radius);
		spherePtr.setCenter(new Point3D(	1.0 - 2.0 * Utility.randDouble(), 
								1.0 - 2.0 * Utility.randDouble(), 
								1.0 - 2.0 * Utility.randDouble()));	
		spherePtr.setMaterial(mattePtr);
		gridPtr.addObject(spherePtr);
	}	

	gridPtr.setupCells();
	w.addObject(gridPtr);
    }   
}
