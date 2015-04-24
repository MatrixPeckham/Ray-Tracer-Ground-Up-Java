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
package com.matrixpeckham.raytracer.build.figures.ch20;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Sphere;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.SphereChecker;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure06C implements BuildWorldFunction{

    @Override
    public void build(World w) {
	int numSamples = 25;
	
	w.vp.setHres(600);	  		
	w.vp.setVres(600);
	w.vp.setSamples(numSamples);
	
	w.tracer = new RayCast(w);
	
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(60, 40, 80);
	pinholePtr.setLookat(new Point3D());
	pinholePtr.setViewDistance(12000); 
	pinholePtr.computeUVW();
	w.setCamera(pinholePtr);
		
	PointLight lightPtr = new PointLight();
	lightPtr.setLocation(100, 100, 60);
	lightPtr.scaleRadiance(3.0);
	lightPtr.setShadows(true);
	w.addLight(lightPtr);
	
	SphereChecker checkerPtr = new SphereChecker();
	checkerPtr.setNumVerticleCheckers(20);
	checkerPtr.setNumHorizontalCheckers(20);
	checkerPtr.setVerticleLineWidth(0.15);
	checkerPtr.setHorizontalLineWidth(0.04);
	checkerPtr.setColor1(Utility.WHITE);
	checkerPtr.setColor2(Utility.WHITE);
	checkerPtr.setLineColor(Utility.BLACK);
			
	SV_Matte svMattePtr = new SV_Matte();						
	svMattePtr.setKa(0.2);    
	svMattePtr.setKd(0.6);
	svMattePtr.setCd(checkerPtr);
	
	Sphere spherePtr = new Sphere();
	spherePtr.setMaterial(svMattePtr);
        
        Instance inst = new Instance(spherePtr);
        inst.scale(0.25, 2.5, 0.25);
	w.addObject(inst);   
    }
    
}
