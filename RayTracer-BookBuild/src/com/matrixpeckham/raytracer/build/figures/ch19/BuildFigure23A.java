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
import com.matrixpeckham.raytracer.geometricobjects.compound.SolidCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure23A implements BuildWorldFunction{

    @Override
    public void build(World w) {
	int numSamples = 1;
	
	w.vp.setHres(400);	  		
	w.vp.setVres(400);
	w.vp.setSamples(numSamples);
	
	w.tracer = new RayCast(w);
	
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(0, 5, 10);
	pinholePtr.setLookat(new Point3D());	
	pinholePtr.setViewDistance(1200);	
	pinholePtr.computeUVW();     
	w.setCamera(pinholePtr);
	
	// point light
	
	PointLight lightPtr1 = new PointLight();
	lightPtr1.setLocation(10, 13, 20);
	lightPtr1.scaleRadiance(3.0); 
	lightPtr1.setShadows(true);
	w.addLight(lightPtr1);	

	
	Matte mattePtr1 = new Matte();			
	mattePtr1.setCd(1, 1, 0); // yellow
	mattePtr1.setKa(0.3);    
	mattePtr1.setKd(0.6);
	
	// solid cylinder
	// the following values for bottom, top, and radius can also be the default values
	// by w.setting them in the SolidCylinder default constructor
	
	double bottom = -1.0;
	double top = 1.0;
	double radius = 1.0;
	SolidCylinder cylinderPtr = new SolidCylinder(bottom, top, radius);
	cylinderPtr.setMaterial(mattePtr1);
	w.addObject(cylinderPtr);
	
	// ground plane
	
	Matte mattePtr2 = new Matte();			
	mattePtr2.setCd(1);				
	mattePtr2.setKa(0.25);    
	mattePtr2.setKd(1.0);
	Plane planePtr1 = new Plane(new Point3D(0, -1, 0), new Normal(0, 1, 0));
	planePtr1.setMaterial(mattePtr2);
	w.addObject(planePtr1);    }
    
}
