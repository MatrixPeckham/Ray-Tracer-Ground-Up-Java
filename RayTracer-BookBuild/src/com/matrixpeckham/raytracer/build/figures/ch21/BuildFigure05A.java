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
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Phong;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure05A implements BuildWorldFunction{

    @Override
    public void build(World w) {
       int numSamples = 16;
	
	w.vp.setHres(400);
	w.vp.setVres(400);
	w.vp.setSamples(numSamples);
	
	w.tracer = new RayCast(w);
	
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(100, 0, 100);  
	pinholePtr.setLookat(0, 1, 0); 	 
	pinholePtr.setViewDistance(8000);	
	pinholePtr.computeUVW();
	w.setCamera(pinholePtr);

	PointLight lightPtr = new PointLight();
	lightPtr.setLocation(50, 50, 1);
	lightPtr.scaleRadiance(3.0);  
	w.addLight(lightPtr);
	
	Phong phongPtr = new Phong();			
	phongPtr.setKa(0.25); 
	phongPtr.setKd(0.8);
	phongPtr.setCd(new RGBColor(0.75)); 
	phongPtr.setKs(0.15); 
	phongPtr.setExp(50.0);  
	
	Sphere spherePtr = new Sphere();
	spherePtr.setMaterial(phongPtr);
	w.addObject(spherePtr);
    }
}
