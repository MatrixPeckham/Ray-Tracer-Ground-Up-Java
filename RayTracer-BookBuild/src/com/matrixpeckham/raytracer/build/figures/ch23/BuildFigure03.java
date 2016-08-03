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
package com.matrixpeckham.raytracer.build.figures.ch23;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.compound.TriangleMesh;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Phong;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure03 implements BuildWorldFunction{

    @Override
    public void build(World w) {
	int numSamples = 16;
	
	w.vp.setHres(400);
	w.vp.setVres(400);
	w.vp.setSamples(numSamples);

	w.backgroundColor = new RGBColor(0.9);
	
	w.tracer = new RayCast(w);
	
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(10, 12, 18); 
	pinholePtr.setLookat(new Point3D(0));
	pinholePtr.setViewDistance(4000); 
	pinholePtr.computeUVW();
	w.setCamera(pinholePtr);
			

	Directional lightPtr = new Directional();
	lightPtr.setDirection(20, 25, 5);
	lightPtr.scaleRadiance(3.0);   
	lightPtr.setShadows(false);
	w.addLight(lightPtr);	
	
	
	Phong phongPtr = new Phong();
	phongPtr.setKa(0.25);
	phongPtr.setKd(0.75);
	phongPtr.setCd(0.2, 0.5, 0.4);
	phongPtr.setKs(0.2);
	phongPtr.setExp(20.0);
	
	// 360 must be divisible by numHorizontalSteps
	// 180 must be divisible by numVerticalSteps
	
	int numHorizontalSteps = 3;    	// for Figure 23.1(a)
	int numVerticalSteps = 2;
	
//	int numHorizontalSteps = 4;		// for Figure 23.1(b)
//	int numVerticalSteps = 2;
	
//	int numHorizontalSteps = 10;		// for Figure 23.1(c)
//	int numVerticalSteps = 5;
	
//	int numHorizontalSteps = 100;		// for Figure 23.1(d)
//	int numVerticalSteps = 50;
		
	TriangleMesh gridPtr = new TriangleMesh();
	gridPtr.tessellateSmoothSphere(numHorizontalSteps, numVerticalSteps); 
	gridPtr.setMaterial(phongPtr);
	gridPtr.setupCells();
	w.addObject(gridPtr);	


    }
    
}
