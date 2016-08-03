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
import com.matrixpeckham.raytracer.geometricobjects.compound.ThickRing;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure29B implements BuildWorldFunction{

    @Override
    public void build(World w) {
	int numSamples = 16;
	
	w.vp.setHres(400);	  		
	w.vp.setVres(400);
	w.vp.setSamples(numSamples);
	
	w.tracer = new RayCast(w);
	
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(0, 3.25, 5);
	pinholePtr.setLookat(new Point3D());
	pinholePtr.setViewDistance(900); 
	pinholePtr.computeUVW();
	w.setCamera(pinholePtr);
		
	PointLight lightPtr1 = new PointLight();
	lightPtr1.setLocation(20, 15, 15);
	lightPtr1.scaleRadiance(3.0);
	lightPtr1.setShadows(false);
	w.addLight(lightPtr1);

	
	Matte mattePtr1 = new Matte();			
	mattePtr1.setKa(0.3);    
	mattePtr1.setKd(0.75);
	mattePtr1.setCd(1, 1, 0.25);  	// lemon
	
	Matte mattePtr2 = new Matte();			
	mattePtr2.setKa(0.4);    
	mattePtr2.setKd(0.75);
	mattePtr2.setCd(0.5);   			// gray
	
	Matte mattePtr3 = new Matte();			
	mattePtr3.setKa(0.4);    
	mattePtr3.setKd(0.75);
	mattePtr3.setCd(0.25, 1, 1); 	// cyan
	
	// ring parameters	
		
	double y0 = -0.25;
	double y1 = 0.25;
	double innerRadius = 0.5;
	double outerRadius = 1.0;
	
	ThickRing ringPtr = new ThickRing(y0, y1, innerRadius, outerRadius); 
	ringPtr.setBottomMaterial(mattePtr1);		// lemon
	ringPtr.setTopMaterial(mattePtr1);			// lemon
	ringPtr.setInnerWallMaterial(mattePtr2);	// gray
	ringPtr.setOuterWallMaterial(mattePtr3);	// cyan
	w.addObject(ringPtr);    }
    
}
