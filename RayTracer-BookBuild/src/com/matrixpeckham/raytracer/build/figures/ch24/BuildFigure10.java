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
import com.matrixpeckham.raytracer.geometricobjects.compound.Compound;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.textures.image.ImageTexture;
import com.matrixpeckham.raytracer.textures.image.mappings.LightProbe;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import static com.matrixpeckham.raytracer.util.Utility.PI;
import com.matrixpeckham.raytracer.util.Vector3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure10 implements BuildWorldFunction{

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the Scene for Figure 24.10
// Texture mapping is discussed in Chapter 29

	int numSamples = 16;
	
	w.vp.setHres(600);			
	w.vp.setVres(600);  
	w.vp.setSamples(numSamples);
	w.vp.setPixelSize(1.0);
	w.vp.setMaxDepth(10);
	
	w.tracer = new Whitted(w);		
			
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(400, 125, 500);   
	pinholePtr.setLookat(0.0, -50, 0);
	pinholePtr.setViewDistance(550);
	pinholePtr.computeUVW(); 
	w.setCamera(pinholePtr);
	
	
	Compound spheres = new Compound();
		
	double  kr = 0.9;
	double radius = 100.0;
	
	// reflective sphere centered at the origin

	Reflective reflectivePtrc = new Reflective();			
	reflectivePtrc.setKa(0.0); 
	reflectivePtrc.setKd(0.0);
	reflectivePtrc.setKs(0.0);
	reflectivePtrc.setCd(new RGBColor(0.0));
	reflectivePtrc.setKr(kr);
	reflectivePtrc.setCr(new RGBColor(new RGBColor(0.65, 0.75, 1.0)));   // pale blue
		
	Sphere spherePtr = new Sphere(new Point3D(0.0), radius);
	spherePtr.setShadows(false);
	spherePtr.setMaterial(reflectivePtrc);
	spheres.addObject(spherePtr);
	
	// define materials for the surrounding spheres
	
	ArrayList<Reflective> materials = new ArrayList<>();	
	
	Reflective reflectivePtr0 = new Reflective();			
	reflectivePtr0.setKa(0.0); 
	reflectivePtr0.setKd(0.0);
	reflectivePtr0.setKs(0.0);
	reflectivePtr0.setCd(new RGBColor(0.0));
	reflectivePtr0.setKr(kr);
	reflectivePtr0.setCr(new RGBColor(0.5, 1.0, 0.5));  // light green
	materials.add(reflectivePtr0);

	Reflective reflectivePtr1 = new Reflective();			
	reflectivePtr1.setKa(0.0); 
	reflectivePtr1.setKd(0.0);
	reflectivePtr1.setKs(0.0);
	reflectivePtr1.setCd(new RGBColor(0.0));
	reflectivePtr1.setKr(kr);
	reflectivePtr1.setCr(new RGBColor(0.4, 1.0, 1.0));  // cyan
	materials.add(reflectivePtr1);
	
	Reflective reflectivePtr2 = new Reflective();			
	reflectivePtr2.setKa(0.0); 
	reflectivePtr2.setKd(0.0);
	reflectivePtr2.setKs(0.0);
	reflectivePtr2.setCd(new RGBColor(0.0));
	reflectivePtr2.setKr(kr);
	reflectivePtr2.setCr(new RGBColor(1.0, 1.0, 0.4));  // lemon
	materials.add(reflectivePtr2);
	
	Reflective reflectivePtr3 = new Reflective();			
	reflectivePtr3.setKa(0.0); 
	reflectivePtr3.setKd(0.0);
	reflectivePtr3.setKs(0.0);
	reflectivePtr3.setCd(new RGBColor(0.0));
	reflectivePtr3.setKr(kr);
	reflectivePtr3.setCr(new RGBColor(1.0, 0.5, 1.0));  // mauve
	materials.add(reflectivePtr3);
	
	Reflective reflectivePtr4 = new Reflective();			
	reflectivePtr4.setKa(0.0); 
	reflectivePtr4.setKd(0.0);
	reflectivePtr4.setKs(0.0);
	reflectivePtr4.setCd(new RGBColor(0.0));
	reflectivePtr4.setKr(kr);
	reflectivePtr4.setCr(new RGBColor(1.0, 0.75, 0.25));  // orange
	materials.add(reflectivePtr4);
	
	Reflective reflectivePtr5 = new Reflective();			
	reflectivePtr5.setKa(0.0); 
	reflectivePtr5.setKd(0.0);
	reflectivePtr5.setKs(0.0);
	reflectivePtr5.setCd(new RGBColor(0.0));
	reflectivePtr5.setKr(kr);
	reflectivePtr5.setCr(new RGBColor(0.5, 0.5, 1.0));  // blue
	materials.add(reflectivePtr5);
	

	// define ring of reflective spheres that just touch the center sphere
	// these are initially in the (x, z) plane
	
	double  numSpheres = 6;
	double theta = 0.0;
	double deltaTheta = 2.0 * PI / numSpheres;
	
	for (int j = 0; j < numSpheres; j++) {
		Point3D center=new Point3D((2.0 * radius) * sin(theta), 0.0, (2.0 * radius) * cos(theta));
		Sphere spherePtr2 = new Sphere(center, radius);
		spherePtr2.setMaterial(materials.get(j));
		spheres.addObject(spherePtr2);
		theta += deltaTheta;
	}
		
	// now rotate the spheres

	Instance rotatedSpheresPtr = new Instance(spheres);
	rotatedSpheresPtr.rotateX(40.0);
	rotatedSpheresPtr.rotateZ(-40.0);
	w.addObject(rotatedSpheresPtr);

	
	// large sphere with Uffizi image
	
	Image imagePtr = new Image();
        try {

            String path = "resources/Textures/ppm/";
            //	imagePtr.readPpmFile(path+"uffizi_probe_small.ppm");   // for testing
            imagePtr.loadPPMFile(Thread.currentThread().getContextClassLoader().getResourceAsStream(path+"uffizi_probe_large.ppm"));   // for production

        } catch (IOException ex) {
            Logger.getLogger(BuildFigure10.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
	
	LightProbe lightProbePtr = new LightProbe();     		
	
	ImageTexture texturePtr = new ImageTexture(imagePtr); 
	texturePtr.setMapping(lightProbePtr);
	
	SV_Matte svMattePtr = new SV_Matte();
	svMattePtr.setKa(1.0);
	svMattePtr.setKd(0.85);
	svMattePtr.setCd(texturePtr);
	
	Sphere unitSpherePtr = new Sphere();
	unitSpherePtr.setShadows(false);	
	
	Instance spherePtr1 = new Instance(unitSpherePtr); 
	spherePtr1.setMaterial(svMattePtr);
	spherePtr1.scale(new Vector3D(1000000.0, 1000000.0, 1000000.0));
	w.addObject(spherePtr1);



    }
    
}
