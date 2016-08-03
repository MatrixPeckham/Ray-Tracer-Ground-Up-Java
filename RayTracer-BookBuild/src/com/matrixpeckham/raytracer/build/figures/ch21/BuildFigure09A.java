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
import com.matrixpeckham.raytracer.geometricobjects.beveledobjects.BeveledWedge;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure09A implements BuildWorldFunction{

    @Override
    public void build(World w) {
       int numSamples = 16;

	w.vp.setHres(500);	  		
	w.vp.setVres(400);
	w.vp.setSamples(numSamples);
	
	w.tracer = new RayCast(w);
	
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(50, 40, 30);			
	pinholePtr.setLookat(0.25, 0.25, 0);
	pinholePtr.setViewDistance(4000);   
	pinholePtr.computeUVW();
	w.setCamera(pinholePtr);
	
	Directional lightPtr2 = new Directional();
	lightPtr2.setDirection(20, 30, 30);
	lightPtr2.scaleRadiance(2.5);   
	lightPtr2.setShadows(true);
	w.addLight(lightPtr2);
	
	Matte mattePtr1 = new Matte();			
	mattePtr1.setKa(0.25);    
	mattePtr1.setKd(0.65);
	mattePtr1.setCd(0.5, 1, 0.5);	  // green
	
	// wedge1 parameters
	
	double y0 = -1.0;		// minimum y value
	double y1 = 2;			// maximum y value
	double r0 = 1.5;			// inner radius
	double r1 = 3;			// outer radius
	double rb = 0.25;		// bevel radius
	double phi0 = 140;		// minimum azimuth angle in degrees
	double phi1 = 350;		// maximum azimuth angle in degrees
	
	BeveledWedge wedgePtr1 = new BeveledWedge(y0, y1, r0, r1, rb, phi0, phi1);
	wedgePtr1.setMaterial(mattePtr1);
	w.addObject(wedgePtr1);
	
		
	Matte mattePtr2 = new Matte();			
	mattePtr2.setKa(0.25);    
	mattePtr2.setKd(0.85);
	mattePtr2.setCd(1.0, 0.5, 0.0);	// orange
	
	// wedge2 parameters
	
	y0 = -1.5;		// minimum y value
	y1 = 1.25;		// minimum y value
	r0 = 0.5;		// inner radius
	r1 = 4.0;		// outer radius
	rb = 0.075;		// bevel radius
	phi0 = 110;		// minimum azimuth angle in degrees
	phi1 = 130;		// maximum azimuth angle in degrees
		
	BeveledWedge wedgePtr2 = new BeveledWedge(y0, y1, r0, r1, rb, phi0, phi1);
	wedgePtr2.setMaterial(mattePtr2);
	w.addObject(wedgePtr2);	
		
	
	Matte mattePtr3 = new Matte();			
	mattePtr3.setCd(1, 1, 0.0);	// yellow
	mattePtr3.setKa(0.25);    
	mattePtr3.setKd(0.85);
	
	// wedge3 parameters
	
	y0 = -0.75;		// minimum y value
	y1 = 0.5;		// minimum y value
	r0 = 1.25;		// inner radius
	r1 = 3.75;		// outer radius
	rb = 0.1;		// bevel radius
	phi0 = 0;		// minimum azimuth angle in degrees
	phi1 = 90;		// maximum azimuth angle in degrees
		
	BeveledWedge wedgePtr3 = new BeveledWedge(y0, y1, r0, r1, rb, phi0, phi1);
	wedgePtr3.setMaterial(mattePtr3);
	w.addObject(wedgePtr3);		    }
}
