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
import static com.matrixpeckham.raytracer.util.Utility.setRandSeed;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure11E implements BuildWorldFunction{
 
    @Override
    public void build(World w) {
	int numSamples = 1;
	
	w.vp.setHres(400);
	w.vp.setVres(400);
	w.vp.setSamples(numSamples);
	
	w.tracer = new RayCast(w);
	
	Pinhole pinhole = new Pinhole();
	pinhole.setEye(10, 15, 30);
	pinhole.setLookat(0, 0, 0);
	pinhole.setViewDistance(4600); 
	pinhole.computeUVW();
	w.setCamera(pinhole);
	
	Directional light = new Directional();   
	light.setDirection(-10, 20, 20);				
	light.scaleRadiance(3.0);   
	light.setShadows(true);
	w.addLight(light);
	
	int numSpheres = 1000;			// for Figure 22.11(a)
//	int numSpheres = 10000;		// for Figure 22.11(b)
//	int numSpheres = 100000;		// for Figure 22.11(c)
//	int numSpheres = 1000000;		// for Figure 22.11(d)			
	
	double volume = 0.1 / numSpheres;
	double radius = Math.pow(0.75 * volume / 3.14159, 0.333333);
	
	setRandSeed(15);
	
	Grid grid = new Grid();  
	
	for (int j = 0; j < numSpheres; j++) {
		Matte matte = new Matte();
		matte.setKa(0.25);
		matte.setKd(0.75);
		matte.setCd(Utility.randDouble(), Utility.randDouble(), Utility.randDouble());
		
		Sphere	sphere = new Sphere(); 
		sphere.setRadius(radius);
		sphere.setCenter(	new Point3D(1.0 - 2.0 * Utility.randDouble(), 
								1.0 - 2.0 * Utility.randDouble(), 
								1.0 - 2.0 * Utility.randDouble()));	
		sphere.setMaterial(matte);
		grid.addObject(sphere);
//                w.addObject(sphere);
	}	

	grid.setupCells();
	w.addObject(grid);


    }
    
    
}
