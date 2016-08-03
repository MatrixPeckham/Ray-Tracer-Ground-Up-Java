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
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure29A implements BuildWorldFunction{

    @Override
    public void build(World w) {
	int numSamples = 16;
	
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
		
	PointLight lightPtr = new PointLight();
	lightPtr.setLocation(10, 13, 20);
	lightPtr.scaleRadiance(3.0); 
	lightPtr.setShadows(true);
	w.addLight(lightPtr);	

	Matte mattePtr1 = new Matte();		
	mattePtr1.setKa(0.3);    
	mattePtr1.setKd(0.6);
	mattePtr1.setCd(0, 1, 1);   // cyan
	
	Matte mattePtr2 = new Matte();			
	mattePtr2.setKa(0.3);    
	mattePtr2.setKd(0.6);
	mattePtr2.setCd(1, 1, 0);  // yellow
	
	double bottom = -1.0;
	double top = 1.0;
	double radius = 1.0;
	SolidCylinder cylinderPtr = new SolidCylinder(bottom, top, radius);
	cylinderPtr.setBottomMaterial(mattePtr1); 	// cyan
	cylinderPtr.setTopMaterial(mattePtr1); 	// cyan
	cylinderPtr.setWallMaterial(mattePtr2); 	// yellow
	w.addObject(cylinderPtr);
	
	
	Matte mattePtr3 = new Matte();			
	mattePtr3.setKa(0.25);    
	mattePtr3.setKd(1.0);
	mattePtr3.setCd(Utility.WHITE);
	
	Plane planePtr = new Plane(new Point3D(0, -1, 0), new Normal(0, 1, 0));
	planePtr.setMaterial(mattePtr3);
	w.addObject(planePtr);    }
    
}
