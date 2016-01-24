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

import com.matrixpeckham.raytracer.build.figures.ch29.BuildFigure04;
import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.TInstance;
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.textures.procedural.CubicNoise;
import com.matrixpeckham.raytracer.textures.procedural.RampFBmTexture;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
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
public class BuildFigure34A implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2008.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.


// This builds the scene for Figure 31.34(a)

// The perturbation amounts (values of a) are different from those used in 
// Figure 31.34 in the book. The original images used a fractal sum function whose
// output was not normalised to the range [0, 1]. In the original function,
// w.adding more octaves produced a larger range. Because the noise used here 
// has 6 octaves, larger perturbation amounts are needed to produce similar
// results.

 												

	int numSamples = 16;
	
	w.vp.setHres(600);     
	w.vp.setVres(600);
	w.vp.setSamples(numSamples);
	
	w.backgroundColor = Utility.BLACK;
	w.tracer = new RayCast(w);
	
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(0, 0, 100);
	pinholePtr.setLookat(new Point3D(0.0));
	pinholePtr.setViewDistance(5800.0);
	pinholePtr.computeUVW();     
	w.setCamera(pinholePtr); 
	
	
	PointLight lightPtr = new PointLight();
	lightPtr.setLocation(20, 20, 40);
	lightPtr.scaleRadiance(2.5);
	w.addLight(lightPtr);

	
	// noise:
	
	CubicNoise noisePtr = new CubicNoise();	
	noisePtr.setNumOctaves(6);
	noisePtr.setGain(0.5);	
	noisePtr.setLacunarity(2.0);		

	// ramp image:
	
	Image imagePtr = new Image();			
        String path = "C:\\Users\\Owner\\Documents\\Ground Up raytracer\\Textures\\ppm\\";
        try {
            imagePtr.loadPPMFile(new File(path+"BlueMarbleRamp.ppm"));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure04.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
	
	// marble texture:	
		
	RampFBmTexture marblePtr = new RampFBmTexture(imagePtr);
	marblePtr.setNoise(noisePtr);
	marblePtr.setPerturbation(8.0);
	
	TInstance transformedMarblePtr = new TInstance(marblePtr);
	transformedMarblePtr.scale(0.3);

	// material:
		
	SV_Matte svMattePtr = new SV_Matte();	 
	svMattePtr.setKa(0.25);
	svMattePtr.setKd(0.9);
	svMattePtr.setCd(transformedMarblePtr);
	
	
	Instance spherePtr1 = new Instance(new Sphere(new Point3D(0.0), 5.0));
	spherePtr1.setMaterial(svMattePtr);
	spherePtr1.rotateY(180);
	w.addObject(spherePtr1);
}





}
