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
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.Phong;
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
public class BuildFigure15 implements BuildWorldFunction{

    @Override
    public void build(World w) {
	int numSamples = 16;
	
	w.vp.setHres(600);
	w.vp.setVres(400); 
	w.vp.setSamples(numSamples);	
	
	Ambient ambientPtr = new Ambient();
	ambientPtr.scaleRadiance(0.5);
	w.setAmbient(ambientPtr);
	
	w.tracer = new RayCast(w);
	
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(10, 15, 20);
	pinholePtr.setLookat(-0.5, -0.5, 0); 
	pinholePtr.setViewDistance(800);
	pinholePtr.computeUVW();
	w.setCamera(pinholePtr);
	
	PointLight lightPtr = new PointLight();
	lightPtr.setLocation(30, 30, 25);  
	lightPtr.scaleRadiance(3.0);  
	lightPtr.setShadows(true);
	w.addLight(lightPtr);
	
	
	// ellipsoids
				
	double 	x0 					= -5.0;					// minimum x center coordinate
	double 	z0 					= -5.0;					// minimum z center coordinate
	double 	x1 					= 5.0;					// maximum x center coordinate
	double 	z1 					= 5.0;					// maximum z center coordinate
	int 	numXEllipsoids	= 5;					// number of ellipsoids in the x direction
	int 	numZEllipsoids	= 5;  					// number of ellipsoids in the z direction
	double 	radius 				= 1.0;   				// common sphere radius
	double	xSpacing			= (x1 - x0) / (numXEllipsoids - 1); // center spacing in x direction
	double	zSpacing			= (z1 - z0) / (numZEllipsoids - 1); // center spacing in x direction
	
	Sphere spherePtr = new Sphere();
	Utility.setRandSeed(1000);
	
	for (int iz = 0; iz < numZEllipsoids; iz++) {
		for (int ix = 0; ix < numXEllipsoids; ix++) {
			Phong phongPtr = new Phong();	
			phongPtr.setKa(0.35);  
			phongPtr.setKd(0.75);
			phongPtr.setKs(0.1);  
			phongPtr.setExp(20.0);
			phongPtr.setCd(Utility.randDouble(), Utility.randDouble(), Utility.randDouble());
			
			double xc = x0 + ix * xSpacing;  	// ellipsoid center x coordinate
			double zc = z0 + iz * zSpacing;		// ellipsoid center z coordinate
			
			Instance ellipsoidPtr = new Instance(spherePtr);
			ellipsoidPtr.scale(1.0, 4.0 * Utility.randDouble(), 1.0);
			ellipsoidPtr.translate(xc, 0, zc);
			ellipsoidPtr.setMaterial(phongPtr);
			w.addObject(ellipsoidPtr);
		}
	}
	
	// ground plane 
	
	Matte mattePtr = new Matte();		
	mattePtr.setKa(0.75);
	mattePtr.setKd(0.5);
	mattePtr.setCd(0.85);  
	
	Plane planePtr = new Plane(new Point3D(0, -1, 0), new Normal(0, 1, 0));
	planePtr.setMaterial(mattePtr);
	w.addObject(planePtr);
    }
    
}
