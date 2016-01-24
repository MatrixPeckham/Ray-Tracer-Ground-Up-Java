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
package com.matrixpeckham.raytracer.build.figures.ch30;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.SV_Phong;
import com.matrixpeckham.raytracer.textures.ConstantColor;
import com.matrixpeckham.raytracer.textures.TInstance;
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.textures.procedural.RampFBmTexture;
import com.matrixpeckham.raytracer.textures.procedural.SphereTextures;
import com.matrixpeckham.raytracer.textures.procedural.Wood;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure16 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.


// This builds the scene for Figure 30.16 

// You will need to implement the SphereTextures material that allows checkers with two
// different textures to be rendered on a sphere.
// You will also need to implement a spatially varying Phong material. My implementation
// has textures for the diffuse and specular colors. In w figure the specular color is white.
// A slightly simpler implementation would keep the specular color as a RGBColor.

// Note that the RampFBmTexture is constructed using access functions for the parameters.
// Also note that the marble and wood textures are continuous across the checkers.

// Finally, I've changed the w.background to gray to make it easier to see the sphere.



	int numSamples = 16;

	w.vp.setHres(600);
	w.vp.setVres(600);
	w.vp.setSamples(numSamples);
	
	w.tracer = new RayCast(w);
	w.backgroundColor = new RGBColor(0.5);
		
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(3, 9, 25); 
	pinholePtr.setLookat(new Point3D(0.0));  
	pinholePtr.setViewDistance(7000.0); 
	pinholePtr.computeUVW();     
	w.setCamera(pinholePtr); 
	
	Directional lightPtr = new Directional();
	lightPtr.setDirection(0.6, 1.1, 1);   
	lightPtr.scaleRadiance(3.0);
	w.addLight(lightPtr);
	
	
	// blue marble ramp image
		// image:

	Image imagePtr = new Image();			
        String path = "C:\\Users\\Owner\\Documents\\Ground Up raytracer\\Textures\\ppm\\";
        try {
            imagePtr.loadPPMFile(new File(path+"BlueMarbleRamp.ppm"));
        } catch (IOException ex) {
            Logger.getLogger(com.matrixpeckham.raytracer.build.figures.ch29.BuildFigure04.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

	
	
	int 	numOctaves 	= 4;
	double 	lacunarity 		= 2.0;
	double 	gain 			= 0.5;
	double 	perturbation	= 5.0;   // fbm amount
	
	RampFBmTexture marblePtr = new RampFBmTexture(imagePtr); 
	marblePtr.setNumOctaves(numOctaves);
	marblePtr.setLacunarity(lacunarity);
	marblePtr.setGain(gain);
	marblePtr.setPerturbation(perturbation);
	
	TInstance scaledMarblePtr = new TInstance(marblePtr);
	scaledMarblePtr.scale(0.1);
	
	
	// wood

	TInstance woodPtr = new TInstance(new Wood(new RGBColor(0.5, 0.3, 0.1), Utility.BLACK));
	woodPtr.scale(0.2);
	woodPtr.rotateX(45);
	
	
	// texture to hold the marble and wood textures	
	
	SphereTextures sphereTexturesPtr = new SphereTextures();
	sphereTexturesPtr.setNumHorizontalCheckers(12); 
	sphereTexturesPtr.setNumVerticleCheckers(6);   
	sphereTexturesPtr.setTexture1(scaledMarblePtr);
	sphereTexturesPtr.setTexture2(woodPtr);
	
	
	// spatially varying Phong material	
		
	SV_Phong svPhongPtr = new SV_Phong();		
	svPhongPtr.setKa(0.25);  
	svPhongPtr.setKd(0.75);
	svPhongPtr.setCd(sphereTexturesPtr);
	svPhongPtr.setKs(0.25);  
	svPhongPtr.setExp(20.0);
	svPhongPtr.setCs(new ConstantColor(Utility.WHITE));
	
	
	Sphere spherePtr = new Sphere();
	spherePtr.setMaterial(svPhongPtr);
	w.addObject(spherePtr);
}


}
