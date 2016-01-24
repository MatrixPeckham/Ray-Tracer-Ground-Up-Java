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
import com.matrixpeckham.raytracer.textures.TInstance;
import com.matrixpeckham.raytracer.textures.procedural.CubicNoise;
import com.matrixpeckham.raytracer.textures.procedural.NestedNoisesTexture;
import com.matrixpeckham.raytracer.textures.procedural.WrappedTwoColors;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure40 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2008.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.


// This builds the scene for Figure 31.40

// The class NestedNoisesTexture is just a wrapped fBm texture class like WrappedTwoColors,
// except that it stores one color and a texture pointer, instead of two colors. As such, it's
// not particularly interesting. 
// A better design would store two texture pointers.
// Here is the getColor function, where wrapFactor is the same as expansionNumber.

/*

RGBColor														
NestedNoisesTexture::getColor(const ShadeRec& sr) const {
	float noise = wrapFactor * noisePtr.valueFbm(sr.localHitPoint);
	float value = noise - floor(noise);
	value = (maxValue - minValue) * value + minValue;
	
	if (noise < 1.0)
		return (value * color);
	else
		return (value * texturePtr.getColor(sr));
}

*/

 												

	int numSamples = 16;
	
	w.vp.setHres(600);    
	w.vp.setVres(600);
	w.vp.setSamples(numSamples);
	
	w.backgroundColor = new RGBColor(0.5);
	w.tracer = new RayCast(w);
	
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(0, 0, 100);
	pinholePtr.setLookat(new Point3D(0.0));
	pinholePtr.setViewDistance(9500);  
	pinholePtr.computeUVW();     
	w.setCamera(pinholePtr); 
	
	
	PointLight lightPtr2 = new PointLight();
	lightPtr2.setLocation(5, 5, 20);		
	lightPtr2.scaleRadiance(3.0);
	w.addLight(lightPtr2);
	
	
	// noise 1:
	
	CubicNoise noisePtr1 = new CubicNoise();	
	noisePtr1.setNumOctaves(6);
	noisePtr1.setGain(0.5);	
	noisePtr1.setLacunarity(4.0);		
	
	WrappedTwoColors texturePtr = new WrappedTwoColors(noisePtr1);	
	texturePtr.setColor1(1.0, 0.8, 0.0);		// gold
	texturePtr.setColor2(0.5, 0.75, 1.0);  	// light blue  
	texturePtr.setExpansionNumber(2.0);	
	
	TInstance transformedWrappedPtr = new TInstance(texturePtr);
	transformedWrappedPtr.scale(0.25);


	// noise 2:
	
	CubicNoise noisePtr2 = new CubicNoise();	
	noisePtr2.setNumOctaves(3);
	noisePtr2.setGain(0.5);	
	noisePtr2.setLacunarity(5.0);
	
	
	// nested noises texture:	

	NestedNoisesTexture nestedTexturesPtr = new NestedNoisesTexture(noisePtr2);	
	nestedTexturesPtr.setColor(0.25, 1.0, 0.1);   // bright green		
	nestedTexturesPtr.setTexture(transformedWrappedPtr);
	nestedTexturesPtr.setMinValue(0.0);  
	nestedTexturesPtr.setMaxValue(1.0);
	nestedTexturesPtr.setWrapFactor(3.0);    

	
	// material:
	
	SV_Matte svMattePtr = new SV_Matte();		
	svMattePtr.setKa(0.35);
	svMattePtr.setKd(1.0);
	svMattePtr.setCd(nestedTexturesPtr);

	
	Sphere spherePtr1 = new Sphere(new Point3D(0.0), 3); 
	spherePtr1.setMaterial(svMattePtr);
	w.addObject(spherePtr1);
}



}
