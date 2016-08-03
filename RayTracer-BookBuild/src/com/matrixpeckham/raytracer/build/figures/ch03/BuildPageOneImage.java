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
package com.matrixpeckham.raytracer.build.figures.ch03;

import com.matrixpeckham.raytracer.cameras.Camera;
import com.matrixpeckham.raytracer.cameras.Orthographic;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.tracers.MultipleObjects;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildPageOneImage implements BuildWorldFunction {

    @Override
    public void build(World w) {

	w.vp.setHres(400);
	w.vp.setVres(400);
	w.vp.setPixelSize(0.5);
        w.vp.setSamples(1);
	
	w.backgroundColor = Utility.BLACK;
	w.tracer = new MultipleObjects(w);  
        Camera cam = new Orthographic();
        cam.setEye(0, 0, 100);
        cam.setLookat(0, 0, 0);
	w.setCamera(cam);
	// colours

	RGBColor yellow=new RGBColor(1, 1, 0);										// yellow
	RGBColor brown=new RGBColor(0.71, 0.40, 0.16);								// brown
	RGBColor darkGreen=new RGBColor(0.0, 0.41, 0.41);							// darkGreen
	RGBColor orange=new RGBColor(1.0, 0.75, 0.0);								// orange
	RGBColor green=new RGBColor(0.0, 0.6, 0.3);									// green
	RGBColor lightGreen=new RGBColor(0.65, 1.0, 0.30);							// light green
	RGBColor darkYellow=new RGBColor(0.61, 0.61, 0.0);							// dark yellow
	RGBColor lightPurple=new RGBColor(0.65, 0.3, 1.0);							// light purple
	RGBColor darkPurple=new RGBColor(0.5, 0.0, 1.0);							// dark purple
	
	// spheres
				
	Sphere sphere1 = new Sphere(new Point3D(5, 3, 0), 30); 
	sphere1.setColor(yellow);	   								// yellow
	w.addObject(sphere1);
	
	Sphere sphere2 = new Sphere(new Point3D(45, -7, -60), 20); 
	sphere2.setColor(brown);									// brown
	w.addObject(sphere2);
		
	Sphere sphere3 = new Sphere(new Point3D(40, 43, -100), 17); 
	sphere3.setColor(darkGreen);								// dark green
	w.addObject(sphere3);
	
	Sphere sphere4 = new Sphere(new Point3D(-20, 28, -15), 20); 
	sphere4.setColor(orange);									// orange
	w.addObject(sphere4);
	
	Sphere sphere5 = new Sphere(new Point3D(-25, -7, -35), 27); 			
	sphere5.setColor(green);									// green
	w.addObject(sphere5);
	
	Sphere sphere6 = new Sphere(new Point3D(20, -27, -35), 25); 
	sphere6.setColor(lightGreen);							// light green
	w.addObject(sphere6);
	
	Sphere sphere7 = new Sphere(new Point3D(35, 18, -35), 22); 
	sphere7.setColor(green);   								// green
	w.addObject(sphere7);
	
	Sphere sphere8 = new Sphere(new Point3D(-57, -17, -50), 15);  
	sphere8.setColor(brown);									// brown
	w.addObject(sphere8);
	
	Sphere sphere9 = new Sphere(new Point3D(-47, 16, -80), 23); 
	sphere9.setColor(lightGreen);							// light green
	w.addObject(sphere9);
		
	Sphere sphere10 = new Sphere(new Point3D(-15, -32, -60), 22); 
	sphere10.setColor(darkGreen);     						// dark green
	w.addObject(sphere10);
	
	Sphere sphere11 = new Sphere(new Point3D(-35, -37, -80), 22); 
	sphere11.setColor(darkYellow);							// dark yellow
	w.addObject(sphere11);
	
	Sphere sphere12 = new Sphere(new Point3D(10, 43, -80), 22); 
	sphere12.setColor(darkYellow);							// dark yellow
	w.addObject(sphere12);
			
	Sphere sphere13 = new Sphere(new Point3D(30, -7, -80), 10); 
	sphere13.setColor(darkYellow);
	w.addObject(sphere13);										// dark yellow (hidden)
		
	Sphere sphere14 = new Sphere(new Point3D(-40, 48, -110), 18); 
	sphere14.setColor(darkGreen); 							// dark green
	w.addObject(sphere14);
		
	Sphere sphere15 = new Sphere(new Point3D(-10, 53, -120), 18); 
	sphere15.setColor(brown); 								// brown
	w.addObject(sphere15);
	
	Sphere sphere16 = new Sphere(new Point3D(-55, -52, -100), 10); 
	sphere16.setColor(lightPurple);							// light purple
	w.addObject(sphere16);
	
	Sphere sphere17 = new Sphere(new Point3D(5, -52, -100), 15); 		
	sphere17.setColor(brown);									// browm
	w.addObject(sphere17);
	
	Sphere sphere18 = new Sphere(new Point3D(-20, -57, -120), 15); 
	sphere18.setColor(darkPurple);							// dark purple
	w.addObject(sphere18);
	
	Sphere sphere19 = new Sphere(new Point3D(55, -27, -100), 17); 
	sphere19.setColor(darkGreen);							// dark green
	w.addObject(sphere19);

	Sphere sphere20 = new Sphere(new Point3D(50, -47, -120), 15); 
	sphere20.setColor(brown);									// browm
	w.addObject(sphere20);
	 	
	Sphere sphere21 = new Sphere(new Point3D(70, -42, -150), 10); 
	sphere21.setColor(lightPurple);							// light purple
	w.addObject(sphere21);
	
	Sphere sphere22 = new Sphere(new Point3D(5, 73, -130), 12); 
	sphere22.setColor(lightPurple);							// light purple
	w.addObject(sphere22);
	
	Sphere sphere23 = new Sphere(new Point3D(66, 21, -130), 13); 			
	sphere23.setColor(darkPurple);							// dark purple
	w.addObject(sphere23);	
	  
	Sphere sphere24 = new Sphere(new Point3D(72, -12, -140), 12); 
	sphere24.setColor(lightPurple);							// light purple
	w.addObject(sphere24);
	
	Sphere sphere25 = new Sphere(new Point3D(64, 5, -160), 11); 			
	sphere25.setColor(green);					 				// green
	w.addObject(sphere25);
	  
	Sphere sphere26 = new Sphere(new Point3D(55, 38, -160), 12); 		
	sphere26.setColor(lightPurple);							// light purple
	w.addObject(sphere26);
	
	Sphere sphere27 = new Sphere(new Point3D(-73, -2, -160), 12); 		
	sphere27.setColor(lightPurple);							// light purple
	w.addObject(sphere27);
	 
	Sphere sphere28 = new Sphere(new Point3D(30, -62, -140), 15); 
	sphere28.setColor(darkPurple); 							// dark purple
	w.addObject(sphere28);
	
	Sphere sphere29 = new Sphere(new Point3D(25, 63, -140), 15); 
	sphere29.setColor(darkPurple);							// dark purple
	w.addObject(sphere29);
	
	Sphere sphere30 = new Sphere(new Point3D(-60, 46, -140), 15);  
	sphere30.setColor(darkPurple); 							// dark purple
	w.addObject(sphere30);
	
	Sphere sphere31 = new Sphere(new Point3D(-30, 68, -130), 12); 
	sphere31.setColor(lightPurple); 							// light purple
	w.addObject(sphere31);
	
	Sphere sphere32 = new Sphere(new Point3D(58, 56, -180), 11);   
	sphere32.setColor(green);									//  green
	w.addObject(sphere32);
	
	Sphere sphere33 = new Sphere(new Point3D(-63, -39, -180), 11); 
	sphere33.setColor(green);									// green 
	w.addObject(sphere33);
	
	Sphere sphere34 = new Sphere(new Point3D(46, 68, -200), 10); 	
	sphere34.setColor(lightPurple);							// light purple
	w.addObject(sphere34);
	
	Sphere sphere35 = new Sphere(new Point3D(-3, -72, -130), 12); 
	sphere35.setColor(lightPurple);							// light purple
	w.addObject(sphere35);    }
    
}
