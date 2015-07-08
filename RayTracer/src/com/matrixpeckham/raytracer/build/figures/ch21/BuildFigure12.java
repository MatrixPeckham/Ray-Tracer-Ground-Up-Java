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
package com.matrixpeckham.raytracer.build.figures.ch21;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.beveledobjects.BeveledCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.Phong;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Normal;
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
int numSamples = 16;
	
	w.vp.setHres(600);	  		
	w.vp.setVres(350);
	w.vp.setSamples(numSamples);
	
	w.tracer = new RayCast(w);
		
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(0, 15.75, 50);
	pinholePtr.setLookat(0, 2, 0);	
	pinholePtr.setViewDistance(3500); 
	pinholePtr.computeUVW();
	w.setCamera(pinholePtr);

	Directional lightPtr = new Directional();
	lightPtr.setDirection(10, 15, 20);
	lightPtr.scaleRadiance(3.0);
	lightPtr.setShadows(true);
	w.addLight(lightPtr);
		
	Phong phongPtr = new Phong();			
	phongPtr.setKa(0.3);    
	phongPtr.setKd(0.75);
	phongPtr.setCd(0.7, 0.5, 0);		// orange
	phongPtr.setKs(0.15);
	phongPtr.setExp(3.0);
	
	
	// commmon cylinder parameters
	
	double radius 		= 1.0;
	double bevelRadius 	= 0.25;
		
	// short cylinder
	
	double y0 = 0.0;
	double y1 = 2.0;
	
	Instance cylinderPtr1 = new Instance(new BeveledCylinder(y0, y1, radius, bevelRadius));
	cylinderPtr1.translate(-2.75, 0, 0);
	cylinderPtr1.setMaterial(phongPtr);
	w.addObject(cylinderPtr1);
	
	// tall cylinder
	
	y0 = 0.0;
	y1 = 4.0;
	
	BeveledCylinder cylinderPtr2 = new BeveledCylinder(y0, y1, radius, bevelRadius);
	cylinderPtr2.setMaterial(phongPtr);
	w.addObject(cylinderPtr2);
	
	// scaled cylinder
	
	y0 = 0.0;
	y1 = 2.0;
	
	Instance cylinderPtr3 = new Instance(new BeveledCylinder(y0, y1, radius, bevelRadius));
	cylinderPtr3.scale(1, 2, 1);
	cylinderPtr3.translate(2.75, 0, 0);
	cylinderPtr3.setMaterial(phongPtr);
	w.addObject(cylinderPtr3);

	// ground plane
	
	Matte mattePtr = new Matte();
	mattePtr.setCd(1);
	mattePtr.setKa(0.25);
	mattePtr.setKd(1);
	
	Plane planePtr = new Plane(new Point3D(0), new Normal(0, 1, 0));
	planePtr.setMaterial(mattePtr);
	w.addObject(planePtr);    }
    
}
