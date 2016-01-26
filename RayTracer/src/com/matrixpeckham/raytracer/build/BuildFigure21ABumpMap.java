/*
 * Copyright (C) 2016 William Matrix Peckham
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
package com.matrixpeckham.raytracer.build;

import com.matrixpeckham.raytracer.build.figures.ch28.*;
import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.BumpedObject;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Dielectric;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.Phong;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.TInstance;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
import com.matrixpeckham.raytracer.textures.procedural.FBMBump;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure21ABumpMap implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.


// This builds the scene for Figure 28.21(a)

	int numSamples = 9;
	
	w.vp.setHres(600);
	w.vp.setVres(600);
	w.vp.setSamples(numSamples);
	w.vp.setMaxDepth(5);
	
	w.backgroundColor = new RGBColor(0.9, 0.9, 1);  // pale blue
	
	w.tracer = new Whitted(w);
	
	Ambient ambientPtr = new Ambient();
	ambientPtr.scaleRadiance(0.5);
	w.setAmbient(ambientPtr);
	
		
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(0, 1.5, 3); 
	pinholePtr.setLookat(new Point3D(0.0)); 
	pinholePtr.setViewDistance(495.0);	
	pinholePtr.computeUVW();
	w.setCamera(pinholePtr);
	
		
	PointLight lightPtr = new PointLight();
	lightPtr.setLocation(10, 20, 20);
	lightPtr.scaleRadiance(5.0);
	lightPtr.setShadows(false);
	w.addLight(lightPtr);
	
	
	// matte sphere inside cube
		
	Matte mattePtr = new Matte();			
	mattePtr.setKa(0.5); 
	mattePtr.setKd(0.5);
	mattePtr.setCd(0.0, 0.25, 1.0); 	 
	
	Sphere spherePtr = new Sphere(new Point3D(0.0, -0.25, -1.0), 0.5);
	spherePtr.setMaterial(mattePtr); 
	w.addObject(spherePtr);
	
	
	// transparent cube
	
	RGBColor glassColor=new RGBColor(0.64, 0.98, 0.88);	// light cyan
	
	Dielectric glassPtr = new Dielectric();
        glassPtr.setKs(0.8);
	glassPtr.setExp(2000.0);  
	glassPtr.setIorIn(1.5);					// glass
	glassPtr.setIorOut(1.0);				// air
	glassPtr.setCfIn(glassColor);
	glassPtr.setCfOut(Utility.WHITE); 
	glassPtr.setShadows(false);
	
        Phong phong = new Phong();
//        phong.setCd(glassColor);
        phong.setCd(new RGBColor(0.5,0.3,0.1));
        phong.setCs(Utility.WHITE);
        phong.setExp(50);
        phong.setKa(0.5);
        phong.setKd(1);
        phong.setKs(0.2);
        
        int octave = 5;
        double lacunarity = 2.0;
        double gain = 0.5;
        double pert = 1;
	Box boxPtr = new Box(new Point3D(-1.0), new Point3D(1.0));
//	boxPtr.setMaterial(glassPtr);
//	w.addObject(boxPtr);
        BumpedObject box = new BumpedObject();
        box.setObject(boxPtr);
        box.setMaterial(phong);
        TInstance tinst = new TInstance(new FBMBump(octave, lacunarity, gain,
                pert));
        tinst.scale(0.125);
        box.setBumpMap(tinst);
	w.addObject(box);
	// plane
	
	Checker3D checkerPtr = new Checker3D();
	checkerPtr.setSize(4.0); 
	checkerPtr.setColor1(1, 1, 0.4);    		// yellow
	checkerPtr.setColor2(1, 0.5, 0);   		// orange
	
	SV_Matte svMattePtr = new SV_Matte();		
	svMattePtr.setKa(0.5);
	svMattePtr.setKd(0.1);
	svMattePtr.setCd(checkerPtr);
	
	Plane planePtr = new Plane(new Point3D(0, -10.1, 0), new Normal(0, 1, 0)); 
	planePtr.setMaterial(svMattePtr);	
	w.addObject(planePtr);
}



}
