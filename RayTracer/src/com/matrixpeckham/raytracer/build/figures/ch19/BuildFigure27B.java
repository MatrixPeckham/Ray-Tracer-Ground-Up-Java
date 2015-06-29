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
import com.matrixpeckham.raytracer.geometricobjects.compound.RoundRimmedBowl;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Phong;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
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
public class BuildFigure27B implements BuildWorldFunction{

    @Override
    public void build(World w) {
int numSamples = 16;
	
	w.vp.setHres(400);	  		
	w.vp.setVres(400);
	w.vp.setSamples(numSamples);
	
	w.tracer = new RayCast(w);
	
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(1, 2, 5);
	pinholePtr.setLookat(0, -0.35, 0);
	pinholePtr.setViewDistance(900); 
	pinholePtr.computeUVW();
	w.setCamera(pinholePtr);
		
	PointLight lightPtr1 = new PointLight();
	lightPtr1.setLocation(20, 15, 15);
	lightPtr1.scaleRadiance(3.0);
	lightPtr1.setShadows(true);
	w.addLight(lightPtr1);
			
	Phong phongPtr = new Phong();						
	phongPtr.setKa(0.25);    
	phongPtr.setKd(0.5);
	phongPtr.setCd(0.53, 0.67, 0.34);
	phongPtr.setKs(0.1);
	phongPtr.setExp(50.0);
	
	double innerRadius = 0.9;
	double outerRadius = 1.0;

	RoundRimmedBowl bowlPtr = new RoundRimmedBowl(innerRadius, outerRadius);
	bowlPtr.setMaterial(phongPtr);
	w.addObject(bowlPtr);
			
	Checker3D checkerPtr = new Checker3D();
	checkerPtr.setSize(1.0);		
	checkerPtr.setColor1(Utility.WHITE);   
	checkerPtr.setColor2(0.75);
	
	SV_Matte svMattePtr = new SV_Matte();		
	svMattePtr.setKa(0.35);
	svMattePtr.setKd(0.85);
	svMattePtr.setCd(checkerPtr);

	Plane planePtr1 = new Plane(new Point3D(0, -1, 0), new Normal(0, 1, 0)); 
	planePtr1.setMaterial(svMattePtr);
	w.addObject(planePtr1);	    }
    
}
