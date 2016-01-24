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
package com.matrixpeckham.raytracer.build.figures.ch31;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.CubicNoise;
import com.matrixpeckham.raytracer.textures.procedural.TurbulenceTexture;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure28 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2008.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.


// This builds the scene for Figure 31.28.

// This is now rendered with a gray w.background.
// The color is more orange than the original build function,
// which makes the images look more like the printed figures.

 												

	int numSamples = 16;
	
	w.vp.setHres(600);    
	w.vp.setVres(600);
	w.vp.setSamples(numSamples);
	
	w.backgroundColor = new RGBColor(0.5);
	w.tracer = new RayCast(w);
	
	Pinhole pinholePtr = new Pinhole();
	
	pinholePtr.setEye(0, 0, 100);
	pinholePtr.setLookat(new Point3D(0.0));
	pinholePtr.setViewDistance(9500.0); 	
	pinholePtr.computeUVW();     
	w.setCamera(pinholePtr); 
	
	
	PointLight lightPtr = new PointLight();
	lightPtr.setLocation(10, 10, 20);		
	lightPtr.scaleRadiance(2.5); 
	w.addLight(lightPtr);
	
	
	// noise:
	
	CubicNoise noisePtr = new CubicNoise();
	noisePtr.setNumOctaves(6); 
	noisePtr.setGain(0.5);
	noisePtr.setLacunarity(2.0);
	
	// texture:		
	
	TurbulenceTexture texturePtr = new TurbulenceTexture(noisePtr);			
	texturePtr.setColor(1.0, 0.7, 0.0);  	// orange 			
	texturePtr.setMinValue(0.0);  			// for Figure 31.28(a)
	texturePtr.setMaxValue(1.2);
//	texturePtr.setMinValue(0.15);  			// for Figure 31.28(b)
//	texturePtr.setMaxValue(0.75);

	// material:
	
	SV_Matte svMattePtr = new SV_Matte();		
	svMattePtr.setKa(0.25);
	svMattePtr.setKd(0.85);
	svMattePtr.setCd(texturePtr);
	
	// the sphere:
	
	Sphere spherePtr = new Sphere(new Point3D(0.0), 3.0); 
	spherePtr.setMaterial(svMattePtr);
	w.addObject(spherePtr);
}




}
