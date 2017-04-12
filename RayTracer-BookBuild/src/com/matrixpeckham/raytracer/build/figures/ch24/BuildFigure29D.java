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
package com.matrixpeckham.raytracer.build.figures.ch24;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.beveledobjects.BeveledBox;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.textures.image.ImageTexture;
import com.matrixpeckham.raytracer.textures.image.mappings.LightProbe;
import com.matrixpeckham.raytracer.tracers.Whitted;
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
public class BuildFigure29D implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 24.29(d)

// This close-up view of a corner of the beveled box in Figure 24.29(c) is 
// rendered with Utility.random colors on each component to highlight the boundaries.
// To use Utility.random colors you need to place the following code at the end of the
// BeveledBox constructor that's used below:

/*
		Utility.setRandSeed(1000);
		double c = 0.25;
		
		for (int j = 0; j < objects.size(); j++) {
			Reflective reflectivePtr = new Reflective();			
			reflectivePtr.setKa(0.0); 
			reflectivePtr.setKd(0.0);
			reflectivePtr.setKs(0.0);
			reflectivePtr.setCd(new new RGBColor(0));
			reflectivePtr.setKr(0.9);
			reflectivePtr.setCr(1.0, 0.75 + c * (-1.0 + 2.0 * Utility.randDouble()) , 0.25 + c * (-1.0 + 2.0 * Utility.randDouble()));		
			objects[j].setMaterial(reflectivePtr);
		}
*/

// To get a clean compile of BeveledBox you will also need to #include
// Reflective.h and Maths.h in BeveledBox.cpp

// This build function should not w.set a material for the beveled box.

// panoramic is declared in LightProbe.h, which is #included in World.h

 												

	int numSamples = 1;
	
	w.vp.setHres(600);			
	w.vp.setVres(600); 
	w.vp.setSamples(numSamples);
	w.vp.setMaxDepth(1);
	
	w.tracer = new Whitted(w);	
				
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(100, -15, 500); 
	pinholePtr.setLookat(30, 5, 0);	
	pinholePtr.setViewDistance(2400);	
	pinholePtr.computeUVW(); 
	w.setCamera(pinholePtr);
		
	
	Reflective reflectivePtr1 = new Reflective();			
	reflectivePtr1.setKa(0.0); 
	reflectivePtr1.setKd(0.0);
	reflectivePtr1.setKs(0.0);
	reflectivePtr1.setCd(new RGBColor(0));
	reflectivePtr1.setKr(0.9);
	reflectivePtr1.setCr(1.0, 0.75, 0.25);  // orange

	
	// beveled box
	
	Point3D p0=new Point3D(-100);
	Point3D p1=new Point3D(100);	
	double bevelRadius = 50;
	
	BeveledBox beveledBoxPtr = new BeveledBox(p0, p1, bevelRadius);	
//	beveledBoxPtr.setMaterial(reflectivePtr);    // don't use w
        Utility.setRandSeed(1000);
        double c = 0.25;
        for(int i = 0; i<beveledBoxPtr.getNumObjects(); i++){
			Reflective reflectivePtr = new Reflective();			
			reflectivePtr.setKa(0.0); 
			reflectivePtr.setKd(0.0);
			reflectivePtr.setKs(0.0);
			reflectivePtr.setCd(new RGBColor(0));
			reflectivePtr.setKr(0.9);
			reflectivePtr.setCr(1.0, 0.75 + c * (-1.0 + 2.0 * Utility.randDouble()) , 0.25 + c * (-1.0 + 2.0 * Utility.randDouble()));		
			beveledBoxPtr.setMaterial(reflectivePtr,i);
        }
        
	Instance rotatedBeveledBoxPtr = new Instance(beveledBoxPtr);
	rotatedBeveledBoxPtr.rotateY(-20);
	rotatedBeveledBoxPtr.rotateX(39);
	w.addObject(rotatedBeveledBoxPtr);
	
	
	// large sphere with Uffizi image
	
	Image imagePtr = new Image();

        String path = "resources/Textures/ppm/";

        try {
//            imagePtr.loadPPMFile(Thread.currentThread().getContextClassLoader().getResourceAsStream(path+"uffizi_probe_small.ppm"));   // for testing
	imagePtr.loadPPMFile(Thread.currentThread().getContextClassLoader().getResourceAsStream(path+"uffizi_probe_large.ppm"));   // for production

        } catch (IOException ex) {
            Logger.getLogger(BuildFigure29A.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
	
	LightProbe lightProbePtr = new LightProbe(); 
	lightProbePtr.makePanoramic();      		
	
	ImageTexture texturePtr = new ImageTexture(imagePtr); 
	texturePtr.setMapping(lightProbePtr);
	
	SV_Matte svMattePtr = new SV_Matte();
	svMattePtr.setKa(1.0);
	svMattePtr.setKd(0.85);
	svMattePtr.setCd(texturePtr);
	
	Sphere unitSpherePtr = new Sphere();
	unitSpherePtr.setShadows(false);	
	
	Instance spherePtr = new Instance(unitSpherePtr); 
	spherePtr.setMaterial(svMattePtr);
	spherePtr.scale(1000000.0);
	w.addObject(spherePtr);



    }
    
}
