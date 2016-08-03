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
import com.matrixpeckham.raytracer.lights.PointLight;
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
public class BuildFigure13 implements BuildWorldFunction {

    @Override
    public void build(World w) {
int numSamples = 1;
	
	w.vp.setHres(400);	  		
	w.vp.setVres(400);
	w.vp.setSamples(numSamples);
	
	w.tracer = new RayCast(w);

	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(10, 15, 50);
	pinholePtr.setLookat(-2.75, 0.25, 0);
	pinholePtr.setViewDistance(7000); 
	pinholePtr.computeUVW();
	w.setCamera(pinholePtr);
		
	PointLight lightPtr1 = new PointLight();
	lightPtr1.setLocation(10, 15, 20);
	lightPtr1.scaleRadiance(3.0);
	lightPtr1.setShadows(true);
	w.addLight(lightPtr1);
		
	Phong phongPtr = new Phong();			
	phongPtr.setKa(0.3);    
	phongPtr.setKd(1.0);
	phongPtr.setCd(0.7, 0.5, 0);		// orange
	phongPtr.setKs(0.4);
	phongPtr.setKd(0.75);
	phongPtr.setKs(0.2);
	phongPtr.setExp(3.0);
			
	// cylinder
	
	double y0 = -0.75;
	double y1 = 1.25;
	double radius = 1.0;
	double bevelRadius = 1.0;
		
	Instance cylinderPtr = new Instance(new BeveledCylinder(y0, y1, radius, bevelRadius));
	cylinderPtr.translate(-2.75, 0.0, 0.0);
	cylinderPtr.setMaterial(phongPtr);
	w.addObject(cylinderPtr);
	
	// ground plane
	
	Matte mattePtr = new Matte();
	mattePtr.setKa(0.5);
	mattePtr.setKd(0.85);
	mattePtr.setCd(0.25);
	
	Plane planePtr = new Plane(new Point3D(0, -0.75, 0), new Normal(0, 1, 0));
	planePtr.setMaterial(mattePtr);
	w.addObject(planePtr);    }
    
}
