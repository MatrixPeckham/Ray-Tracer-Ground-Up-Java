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
package com.matrixpeckham.raytracer.build.figures.ch25;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.compound.SolidCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.GlossyReflector;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.samplers.Hammersley;
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.textures.image.ImageTexture;
import com.matrixpeckham.raytracer.textures.image.mappings.LightProbe;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Point3D;
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
public class BuildFigure13 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 25.13.

	int numSamples = 1;
	
	w.vp.setHres(600);			
	w.vp.setVres(600); 
	w.vp.setSamples(numSamples);
	w.vp.setMaxDepth(1);
		
	w.tracer = new Whitted(w);	
				
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(-150, 75, 500); 
	pinholePtr.setLookat(-6, 50, 0);
	pinholePtr.setViewDistance(3000);	
	pinholePtr.computeUVW(); 
	w.setCamera(pinholePtr);
	
	
	PointLight lightPtr1 = new PointLight();
	lightPtr1.setLocation(250, 500, 250); 
	lightPtr1.scaleRadiance(2.0);   
	lightPtr1.setShadows(true); 
    w.addLight(lightPtr1);

	double exp = 1.0;  	
	GlossyReflector glossyPtr = new GlossyReflector();
//	glossyPtr.setSampler(new PureRandom(numSamples), exp);		// for Figure 25.13(a)
//	glossyPtr.setSampler(new Regular(numSamples), exp);			// for Figure 25.13(b)
	glossyPtr.setSampler(new Hammersley(numSamples), exp);		// for Figure 25.13(c)		
	glossyPtr.setKa(0.0); 
	glossyPtr.setKd(0.0);
	glossyPtr.setKs(0.0);
	glossyPtr.setExp(exp);
	glossyPtr.setCd(1.0, 1.0, 0.3);
	glossyPtr.setKr(0.9);
	glossyPtr.setExponent(exp);
	glossyPtr.setCr(1.0, 1.0, 0.3);  // lemon
	
	Sphere spherePtr1 = new Sphere(new Point3D(-6, 55, 0), 40);
	spherePtr1.setMaterial(glossyPtr);
	w.addObject(spherePtr1);
		
	
	//cylinder
	
	Matte mattePtr = new Matte();		
	mattePtr.setKa(0.15);
	mattePtr.setKd(0.75);
	mattePtr.setCd(0.5, 1.0, 0.5);   // green

	double bottom = -100;
	double top = 15;       
	double radius = 30;
	Instance cylinderPtr = new Instance(new SolidCylinder(bottom, top, radius));
	cylinderPtr.translate(-6, 0, 0);
	cylinderPtr.setMaterial(mattePtr);
	w.addObject(cylinderPtr);
	

	            String path = "resources/Textures/ppm/";

	Image imagePtr = new Image();
        try {
            imagePtr.loadPPMFile(Thread.currentThread().getContextClassLoader().getResourceAsStream(path+"uffizi_probe_small.ppm"));  // for testing
//	imagePtr.loadPPMFile(Thread.currentThread().getContextClassLoader().getResourceAsStream(path+"uffizi_probe_large.ppm"));  // for production

        } catch (IOException ex) {
            Logger.getLogger(BuildFigure13.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

	
	LightProbe lightProbePtr = new LightProbe(); 
	lightProbePtr.makePanoramic();   		
	
	ImageTexture imageTexturePtr = new ImageTexture(imagePtr); 
	imageTexturePtr.setMapping(lightProbePtr);
	
	SV_Matte svMattePtr = new SV_Matte();		
	svMattePtr.setKa(1);
	svMattePtr.setKd(0.85);  
	svMattePtr.setCd(imageTexturePtr);
	
	Sphere unitSpherePtr = new Sphere();
	unitSpherePtr.setShadows(false);	
	
	Instance spherePtr2 = new Instance(unitSpherePtr); 
	spherePtr2.scale(1000000.0);
	spherePtr2.setMaterial(svMattePtr);
	w.addObject(spherePtr2);



    }
    
}
