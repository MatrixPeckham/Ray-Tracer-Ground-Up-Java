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
import com.matrixpeckham.raytracer.geometricobjects.compound.Compound;
import com.matrixpeckham.raytracer.geometricobjects.primitives.OpenCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Ring;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
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
public class BuildFigure14 implements BuildWorldFunction {

    @Override
    public void build(World w) {
int numSamples = 16;
	
	w.vp.setHres(400);	  		
	w.vp.setVres(400);
	w.vp.setSamples(numSamples);
	
	w.tracer = new RayCast(w);
	
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(4, 3.25, 5);
	pinholePtr.setLookat(0.85, 0.0, 0);
	pinholePtr.setViewDistance(900); 
	pinholePtr.computeUVW();
	w.setCamera(pinholePtr);
		
	PointLight lightPtr1 = new PointLight();
	lightPtr1.setLocation(20, 10, 15);
	lightPtr1.scaleRadiance(2.0);
	lightPtr1.setShadows(true);
	w.addLight(lightPtr1);
			
	
	// ring parameters	
		
	Point3D centre=new Point3D(0);
	double y0 = -0.25;
	double y1 = 0.25;
	double innerRadius = 0.5;
	double outerRadius = 1.0;
	
	Matte mattePtr1 = new Matte();			
	mattePtr1.setKa(0.3);    
	mattePtr1.setKd(0.75);
	mattePtr1.setCd(0, 1, 1);  // cyan
	
	Ring bottomPtr = new Ring(new Point3D(0, y0, 0), new Normal(0, -1, 0), innerRadius, outerRadius); 
	bottomPtr.setMaterial(mattePtr1);
	
	Ring topPtr = new Ring(new Point3D(0, y1, 0),  new Normal(0, 1, 0), innerRadius, outerRadius); 
	topPtr.setMaterial(mattePtr1);
	
	
	Matte mattePtr2 = new Matte();			
	mattePtr2.setKa(0.4);    
	mattePtr2.setKd(0.75);
	mattePtr2.setCd(1, 1, 0);   // yellow
	
	OpenCylinder outerWallPtr = new OpenCylinder(y0, y1, outerRadius); 
	outerWallPtr.setMaterial(mattePtr2);
	
		
	Matte mattePtr3 = new Matte();			
	mattePtr3.setKa(0.4);    
	mattePtr3.setKd(0.75);
	mattePtr3.setCd(1, 1, 0);   // yellow
	
	OpenCylinder innerWallPtr = new OpenCylinder(y0, y1, innerRadius); 
	innerWallPtr.setMaterial(mattePtr3);
	
	
	// construct the ring as a compound object
	
	Compound ringPtr = new Compound();
	ringPtr.addObject(bottomPtr); 
	ringPtr.addObject(topPtr);
	ringPtr.addObject(outerWallPtr);
	ringPtr.addObject(innerWallPtr);
	
	// use nested instances for the transformed ring
	
	Instance rotatedRingPtr = new Instance(ringPtr);
	rotatedRingPtr.rotateZ(-45);
	
	Instance translatedRingPtr = new Instance(rotatedRingPtr);
	translatedRingPtr.translate(1, 0, 0);
	w.addObject(translatedRingPtr);
	
	// sphere
	
	Matte mattePtr4 = new Matte();			
	mattePtr4.setKa(0.15);    
	mattePtr4.setKd(0.9);
	mattePtr4.setCd(1, 0.75, 0);
	
	Sphere spherePtr = new Sphere(new Point3D(2, 1, 0.5), 0.2);
	spherePtr.setMaterial(mattePtr4);
	w.addObject(spherePtr);
	
	// ground plane
	
	Matte mattePtr5 = new Matte();			
	mattePtr5.setKa(0.15);    
	mattePtr5.setKd(0.75);
	mattePtr5.setCd(1.0);
	
	Plane planePtr = new Plane(new Point3D(0, -2, 0), new Normal(0, 1, 0));
	planePtr.setMaterial(mattePtr5);
	w.addObject(planePtr);    }
    
}
