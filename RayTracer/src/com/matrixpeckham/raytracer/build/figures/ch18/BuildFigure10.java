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
package com.matrixpeckham.raytracer.build.figures.ch18;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.primatives.ConcaveSphere;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Sphere;
import com.matrixpeckham.raytracer.lights.AmbientOccluder;
import com.matrixpeckham.raytracer.lights.EnvironmentLight;
import com.matrixpeckham.raytracer.materials.Emissive;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.samplers.Hammersley;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure10 implements BuildWorldFunction{

    @Override
    public void build(World w) {
        int numSamples = 16;    // for Figure 18.10 (a) & (b)
	//int numSamples = 256;   // for Figure 18.10 (c) & (d)
		  
	w.vp.setHres(600);
	w.vp.setVres(600);
	w.vp.setPixelSize(0.5);
	w.vp.setSamples(numSamples);  
		
	w.tracer = new Whitted(w);	
		
	AmbientOccluder ambientOccluderPtr = new AmbientOccluder();
	ambientOccluderPtr.setSampler(new MultiJittered(numSamples));  	// for Figure 18.10 (a) & (c)
	//ambientOccluderPtr.setSampler(new Hammersley(numSamples));		// for Figure 18.10 (b) & (d)
	ambientOccluderPtr.setMinAmount(0.5);
	w.setAmbient(ambientOccluderPtr);
			
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(0, 0, -10000);
	pinholePtr.setLookat(new Point3D(0.0));   
	pinholePtr.setViewDistance(15000);		
	pinholePtr.computeUVW(); 
	w.setCamera(pinholePtr);
	
	
	Emissive emissivePtr = new Emissive();
	emissivePtr.scaleRadiance(0.90);
	emissivePtr.setCe(Utility.WHITE);   

	
	ConcaveSphere spherePtr = new ConcaveSphere();
	spherePtr.setRadius(1000000.0);
	spherePtr.setMaterial(emissivePtr);
	spherePtr.setShadows(false);
	w.addObject(spherePtr);
	
	EnvironmentLight environmentLightPtr = new EnvironmentLight();
	environmentLightPtr.setMaterial(emissivePtr);
	environmentLightPtr.setSampler(new MultiJittered(numSamples));	// for Figure 18.10 (a) & (c)
	//environmentLightPtr.setSampler(new Hammersley(numSamples));	// for Figure 18.10 (b) & (d)
	environmentLightPtr.setShadows(true);
	w.addLight(environmentLightPtr);
	
	
	// common Matte material reflection coefficients
	
	double ka = 0.25;
	double kd = 0.75;
	double min = 0.25;
	double max = 0.8;
	
	Utility.setRandSeed(1000);
	
	
	// the spheres
	
	Matte mattePtr1 = new Matte();
	mattePtr1.setKa(ka);	
	mattePtr1.setKd(kd);
	mattePtr1.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));				
	Sphere spherePtr1 = new Sphere(new Point3D(5, 3, 0), 30); 
	spherePtr1.setMaterial(mattePtr1);	   							
	w.addObject(spherePtr1);
	
	Matte mattePtr2 = new Matte();
	mattePtr2.setKa(ka);	
	mattePtr2.setKd(kd);
	mattePtr2.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr2 = new Sphere(new Point3D(45, -7, -60), 20); 
	spherePtr2.setMaterial(mattePtr2);								
	w.addObject(spherePtr2);
	

	Matte mattePtr3 = new Matte();
	mattePtr3.setKa(ka);	
	mattePtr3.setKd(kd);
	mattePtr3.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));	
	Sphere spherePtr3 = new Sphere(new Point3D(40, 43, -100), 17); 
	spherePtr3.setMaterial(mattePtr3);								
	w.addObject(spherePtr3);
	
	Matte mattePtr4 = new Matte();
	mattePtr4.setKa(ka);	
	mattePtr4.setKd(kd);
	mattePtr4.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr4 = new Sphere(new Point3D(-20, 28, -15), 20); 
	spherePtr4.setMaterial(mattePtr4);								
	w.addObject(spherePtr4);
	
	Matte mattePtr5 = new Matte();
	mattePtr5.setKa(ka);	
	mattePtr5.setKd(kd);
	mattePtr5.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr5 = new Sphere(new Point3D(-25, -7, -35), 27); 			
	spherePtr5.setMaterial(mattePtr5);								
	w.addObject(spherePtr5);
	
	Matte mattePtr6 = new Matte();
	mattePtr6.setKa(ka);	
	mattePtr6.setKd(kd);
	mattePtr6.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr6 = new Sphere(new Point3D(20, -27, -35), 25); 
	spherePtr6.setMaterial(mattePtr6);								
	w.addObject(spherePtr6);
	
	Matte mattePtr7 = new Matte();
	mattePtr7.setKa(ka);	
	mattePtr7.setKd(kd);
	mattePtr7.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr7 = new Sphere(new Point3D(35, 18, -35), 22); 
	spherePtr7.setMaterial(mattePtr7);   							
	w.addObject(spherePtr7);
	
	Matte mattePtr8 = new Matte();
	mattePtr8.setKa(ka);	
	mattePtr8.setKd(kd);
	mattePtr8.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr8 = new Sphere(new Point3D(-57, -17, -50), 15);  
	spherePtr8.setMaterial(mattePtr8);								
	w.addObject(spherePtr8);
	
	Matte mattePtr9 = new Matte();
	mattePtr9.setKa(ka);	
	mattePtr9.setKd(kd);
	mattePtr9.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr9 = new Sphere(new Point3D(-47, 16, -80), 23); 
	spherePtr9.setMaterial(mattePtr9);								
	w.addObject(spherePtr9);
		
	Matte mattePtr10 = new Matte();
	mattePtr10.setKa(ka);	
	mattePtr10.setKd(kd);
	mattePtr10.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));	
	Sphere spherePtr10 = new Sphere(new Point3D(-15, -32, -60), 22); 
	spherePtr10.setMaterial(mattePtr10);     						
	w.addObject(spherePtr10);
	
	Matte mattePtr11 = new Matte();
	mattePtr11.setKa(ka);	
	mattePtr11.setKd(kd);
	mattePtr11.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr11 = new Sphere(new Point3D(-35, -37, -80), 22); 
	spherePtr11.setMaterial(mattePtr11);							
	w.addObject(spherePtr11);
	
	Matte mattePtr12 = new Matte();
	mattePtr12.setKa(ka);	
	mattePtr12.setKd(kd);
	mattePtr12.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr12 = new Sphere(new Point3D(10, 43, -80), 22); 
	spherePtr12.setMaterial(mattePtr12);							
	w.addObject(spherePtr12);
	
	Matte mattePtr13 = new Matte();
	mattePtr13.setKa(ka);	
	mattePtr13.setKd(kd);
	mattePtr13.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));		
	Sphere spherePtr13 = new Sphere(new Point3D(30, -7, -80), 10); 
	spherePtr13.setMaterial(mattePtr13);
	w.addObject(spherePtr13);											
	
	Matte mattePtr14 = new Matte();
	mattePtr14.setKa(ka);	
	mattePtr14.setKd(kd);
	mattePtr14.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));	
	Sphere spherePtr14 = new Sphere(new Point3D(-40, 48, -110), 18); 
	spherePtr14.setMaterial(mattePtr14); 							
	w.addObject(spherePtr14);
	
	Matte mattePtr15 = new Matte();
	mattePtr15.setKa(ka);	
	mattePtr15.setKd(kd);
	mattePtr15.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));	
	Sphere spherePtr15 = new Sphere(new Point3D(-10, 53, -120), 18); 
	spherePtr15.setMaterial(mattePtr15); 							
	w.addObject(spherePtr15);
	
	Matte mattePtr16 = new Matte();
	mattePtr16.setKa(ka);	
	mattePtr16.setKd(kd);
	mattePtr16.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr16 = new Sphere(new Point3D(-55, -52, -100), 10); 
	spherePtr16.setMaterial(mattePtr16);							
	w.addObject(spherePtr16);
	
	Matte mattePtr17 = new Matte();
	mattePtr17.setKa(ka);	
	mattePtr17.setKd(kd);
	mattePtr17.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr17 = new Sphere(new Point3D(5, -52, -100), 15); 		
	spherePtr17.setMaterial(mattePtr17);							
	w.addObject(spherePtr17);
	
	Matte mattePtr18 = new Matte();
	mattePtr18.setKa(ka);	
	mattePtr18.setKd(kd);
	mattePtr18.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr18 = new Sphere(new Point3D(-20, -57, -120), 15); 
	spherePtr18.setMaterial(mattePtr18);							
	w.addObject(spherePtr18);
	
	Matte mattePtr19 = new Matte();
	mattePtr19.setKa(ka);	
	mattePtr19.setKd(kd);
	mattePtr19.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr19 = new Sphere(new Point3D(55, -27, -100), 17); 
	spherePtr19.setMaterial(mattePtr19);							
	w.addObject(spherePtr19);

	Matte mattePtr20 = new Matte();
	mattePtr20.setKa(ka);	
	mattePtr20.setKd(kd);
	mattePtr20.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr20 = new Sphere(new Point3D(50, -47, -120), 15); 
	spherePtr20.setMaterial(mattePtr20);							
	w.addObject(spherePtr20);
	 
	Matte mattePtr21 = new Matte();
	mattePtr21.setKa(ka);	
	mattePtr21.setKd(kd);
	mattePtr21.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min)); 	
	Sphere spherePtr21 = new Sphere(new Point3D(70, -42, -150), 10); 
	spherePtr21.setMaterial(mattePtr21);							
	w.addObject(spherePtr21);
	
	Matte mattePtr22 = new Matte();
	mattePtr22.setKa(ka);	
	mattePtr22.setKd(kd);
	mattePtr22.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr22 = new Sphere(new Point3D(5, 73, -130), 12); 
	spherePtr22.setMaterial(mattePtr22);							
	w.addObject(spherePtr22);
	
	Matte mattePtr23 = new Matte();
	mattePtr23.setKa(ka);	
	mattePtr23.setKd(kd);
	mattePtr23.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr23 = new Sphere(new Point3D(66, 21, -130), 13); 			
	spherePtr23.setMaterial(mattePtr23);							
	w.addObject(spherePtr23);	
	
	Matte mattePtr24 = new Matte();
	mattePtr24.setKa(ka);	
	mattePtr24.setKd(kd);
	mattePtr24.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));  
	Sphere spherePtr24 = new Sphere(new Point3D(72, -12, -140), 12); 
	spherePtr24.setMaterial(mattePtr24);							
	w.addObject(spherePtr24);
	
	Matte mattePtr25 = new Matte();
	mattePtr25.setKa(ka);	
	mattePtr25.setKd(kd);
	mattePtr25.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr25 = new Sphere(new Point3D(64, 5, -160), 11); 			
	spherePtr25.setMaterial(mattePtr25);					 		
	w.addObject(spherePtr25);
	  
	Matte mattePtr26 = new Matte();
	mattePtr26.setKa(ka);	
	mattePtr26.setKd(kd);
	mattePtr26.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr26 = new Sphere(new Point3D(55, 38, -160), 12); 		
	spherePtr26.setMaterial(mattePtr26);							
	w.addObject(spherePtr26);
	
	Matte mattePtr27 = new Matte();
	mattePtr27.setKa(ka);	
	mattePtr27.setKd(kd);
	mattePtr27.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr27 = new Sphere(new Point3D(-73, -2, -160), 12); 		
	spherePtr27.setMaterial(mattePtr27);							
	w.addObject(spherePtr27);
	 
	Matte mattePtr28 = new Matte();
	mattePtr28.setKa(ka);	
	mattePtr28.setKd(kd);
	mattePtr28.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr28 = new Sphere(new Point3D(30, -62, -140), 15); 
	spherePtr28.setMaterial(mattePtr28); 							
	w.addObject(spherePtr28);
	
	Matte mattePtr29 = new Matte();
	mattePtr29.setKa(ka);	
	mattePtr29.setKd(kd);
	mattePtr29.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr29 = new Sphere(new Point3D(25, 63, -140), 15); 
	spherePtr29.setMaterial(mattePtr29);							
	w.addObject(spherePtr29);
	
	Matte mattePtr30 = new Matte();
	mattePtr30.setKa(ka);	
	mattePtr30.setKd(kd);
	mattePtr30.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr30 = new Sphere(new Point3D(-60, 46, -140), 15);  
	spherePtr30.setMaterial(mattePtr30); 							
	w.addObject(spherePtr30);
	
	Matte mattePtr31 = new Matte();
	mattePtr31.setKa(ka);	
	mattePtr31.setKd(kd);
	mattePtr31.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr31 = new Sphere(new Point3D(-30, 68, -130), 12); 
	spherePtr31.setMaterial(mattePtr31); 							
	w.addObject(spherePtr31);
	
	Matte mattePtr32 = new Matte();
	mattePtr32.setKa(ka);	
	mattePtr32.setKd(kd);
	mattePtr32.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr32 = new Sphere(new Point3D(58, 56, -180), 11);   
	spherePtr32.setMaterial(mattePtr32);							
	w.addObject(spherePtr32);
	
	Matte mattePtr33 = new Matte();
	mattePtr33.setKa(ka);	
	mattePtr33.setKd(kd);
	mattePtr33.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr33 = new Sphere(new Point3D(-63, -39, -180), 11); 
	spherePtr33.setMaterial(mattePtr33);							 
	w.addObject(spherePtr33);
	
	Matte mattePtr34 = new Matte();
	mattePtr34.setKa(ka);	
	mattePtr34.setKd(kd);
	mattePtr34.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr34 = new Sphere(new Point3D(46, 68, -200), 10); 	
	spherePtr34.setMaterial(mattePtr34);							
	w.addObject(spherePtr34);
	
	Matte mattePtr35 = new Matte();
	mattePtr35.setKa(ka);	
	mattePtr35.setKd(kd);
	mattePtr35.setCd(min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min), min + Utility.randDouble() * (max - min));
	Sphere spherePtr35 = new Sphere(new Point3D(-3, -72, -130), 12); 
	spherePtr35.setMaterial(mattePtr35);							
	w.addObject(spherePtr35);    }
    
}
