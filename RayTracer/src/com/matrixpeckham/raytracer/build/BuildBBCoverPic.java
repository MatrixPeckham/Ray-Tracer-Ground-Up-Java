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
package com.matrixpeckham.raytracer.build;

import com.matrixpeckham.raytracer.geometricobjects.primatives.Sphere;
import com.matrixpeckham.raytracer.tracers.MultipleObjects;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildBBCoverPic implements BuildWorldFunction {

    @Override
    public void build(World w) {
        w.vp.hRes=400;
	w.vp.hRes=400;
	w.vp.s=0.5f;
	
	w.backgroundColor = new RGBColor(0.0f);
	w.tracer = new MultipleObjects(w);  
	
	// colours

	RGBColor yellow=new RGBColor(1, 1, 0);										// yellow
	RGBColor brown=new RGBColor(0.71f, 0.40f, 0.16f);								// brown
	RGBColor dark_green=new RGBColor(0.0f, 0.41f, 0.41f);							// dark_green
	RGBColor orange=new RGBColor(1, 0.75f, 0);									// orange
	RGBColor green=new RGBColor(0, 0.6f, 0.3f);									// green
	RGBColor light_green=new RGBColor(0.65f, 1, 0.30f);							// light green
	RGBColor dark_yellow=new RGBColor(0.61f, 0.61f, 0);							// dark yellow
	RGBColor light_purple=new RGBColor(0.65f, 0.3f, 1);							// light purple
	RGBColor dark_purple=new RGBColor(0.5f, 0, 1);								// dark purple
	
	// spheres
				
	Sphere	sphere_ptr1 = new Sphere(new Point3D(5, 3, 0), 30); 
	sphere_ptr1.setColor(yellow);	   								// yellow
	w.addObject(sphere_ptr1);
	
	Sphere	sphere_ptr2 = new Sphere(new Point3D(45, -7, -60), 20); 
	sphere_ptr2.setColor(brown);									// brown
	w.addObject(sphere_ptr2);
		
	Sphere	sphere_ptr3 = new Sphere(new Point3D(40, 43, -100), 17); 
	sphere_ptr3.setColor(dark_green);								// dark green
	w.addObject(sphere_ptr3);
	
	Sphere	sphere_ptr4 = new Sphere(new Point3D(-20, 28, -15), 20); 
	sphere_ptr4.setColor(orange);									// orange
	w.addObject(sphere_ptr4);
	
	Sphere	sphere_ptr5 = new Sphere(new Point3D(-25, -7, -35), 27); 			
	sphere_ptr5.setColor(green);									// green
	w.addObject(sphere_ptr5);
	
	Sphere	sphere_ptr6 = new Sphere(new Point3D(20, -27, -35), 25); 
	sphere_ptr6.setColor(light_green);								// light green
	w.addObject(sphere_ptr6);
	
	Sphere	sphere_ptr7 = new Sphere(new Point3D(35, 18, -35), 22); 
	sphere_ptr7.setColor(green);   								// green
	w.addObject(sphere_ptr7);
	
	Sphere	sphere_ptr8 = new Sphere(new Point3D(-57, -17, -50), 15);  
	sphere_ptr8.setColor(brown);									// brown
	w.addObject(sphere_ptr8);
	
	Sphere	sphere_ptr9 = new Sphere(new Point3D(-47, 16, -80), 23); 
	sphere_ptr9.setColor(light_green);								// light green
	w.addObject(sphere_ptr9);
		
	Sphere	sphere_ptr10 = new Sphere(new Point3D(-15, -32, -60), 22); 
	sphere_ptr10.setColor(dark_green);     						// dark green
	w.addObject(sphere_ptr10);
	
	Sphere	sphere_ptr11 = new Sphere(new Point3D(-35, -37, -80), 22); 
	sphere_ptr11.setColor(dark_yellow);							// dark yellow
	w.addObject(sphere_ptr11);
	
	Sphere	sphere_ptr12 = new Sphere(new Point3D(10, 43, -80), 22); 
	sphere_ptr12.setColor(dark_yellow);							// dark yellow
	w.addObject(sphere_ptr12);
			
	Sphere	sphere_ptr13 = new Sphere(new Point3D(30, -7, -80), 10); 
	sphere_ptr13.setColor(dark_yellow);
	w.addObject(sphere_ptr13);										// dark yellow (hidden)
		
	Sphere	sphere_ptr14 = new Sphere(new Point3D(-40, 48, -110), 18); 
	sphere_ptr14.setColor(dark_green); 							// dark green
	w.addObject(sphere_ptr14);
		
	Sphere	sphere_ptr15 = new Sphere(new Point3D(-10, 53, -120), 18); 
	sphere_ptr15.setColor(brown); 								// brown
	w.addObject(sphere_ptr15);
	
	Sphere	sphere_ptr16 = new Sphere(new Point3D(-55, -52, -100), 10); 
	sphere_ptr16.setColor(light_purple);							// light purple
	w.addObject(sphere_ptr16);
	
	Sphere	sphere_ptr17 = new Sphere(new Point3D(5, -52, -100), 15); 		
	sphere_ptr17.setColor(brown);									// browm
	w.addObject(sphere_ptr17);
	
	Sphere	sphere_ptr18 = new Sphere(new Point3D(-20, -57, -120), 15); 
	sphere_ptr18.setColor(dark_purple);							// dark purple
	w.addObject(sphere_ptr18);
	
	Sphere	sphere_ptr19 = new Sphere(new Point3D(55, -27, -100), 17); 
	sphere_ptr19.setColor(dark_green);								// dark green
	w.addObject(sphere_ptr19);

	Sphere	sphere_ptr20 = new Sphere(new Point3D(50, -47, -120), 15); 
	sphere_ptr20.setColor(brown);									// browm
	w.addObject(sphere_ptr20);
	 	
	Sphere	sphere_ptr21 = new Sphere(new Point3D(70, -42, -150), 10); 
	sphere_ptr21.setColor(light_purple);							// light purple
	w.addObject(sphere_ptr21);
	
	Sphere	sphere_ptr22 = new Sphere(new Point3D(5, 73, -130), 12); 
	sphere_ptr22.setColor(light_purple);							// light purple
	w.addObject(sphere_ptr22);
	
	Sphere	sphere_ptr23 = new Sphere(new Point3D(66, 21, -130), 13); 			
	sphere_ptr23.setColor(dark_purple);							// dark purple
	w.addObject(sphere_ptr23);	
	  
	Sphere	sphere_ptr24 = new Sphere(new Point3D(72, -12, -140), 12); 
	sphere_ptr24.setColor(light_purple);							// light purple
	w.addObject(sphere_ptr24);
	
	Sphere	sphere_ptr25 = new Sphere(new Point3D(64, 5, -160), 11); 			
	sphere_ptr25.setColor(green);					 				// green
	w.addObject(sphere_ptr25);
	  
	Sphere	sphere_ptr26 = new Sphere(new Point3D(55, 38, -160), 12); 		
	sphere_ptr26.setColor(light_purple);							// light purple
	w.addObject(sphere_ptr26);
	
	Sphere	sphere_ptr27 = new Sphere(new Point3D(-73, -2, -160), 12); 		
	sphere_ptr27.setColor(light_purple);							// light purple
	w.addObject(sphere_ptr27);
	 
	Sphere	sphere_ptr28 = new Sphere(new Point3D(30, -62, -140), 15); 
	sphere_ptr28.setColor(dark_purple); 							// dark purple
	w.addObject(sphere_ptr28);
	
	Sphere	sphere_ptr29 = new Sphere(new Point3D(25, 63, -140), 15); 
	sphere_ptr29.setColor(dark_purple);							// dark purple
	w.addObject(sphere_ptr29);
	
	Sphere	sphere_ptr30 = new Sphere(new Point3D(-60, 46, -140), 15);  
	sphere_ptr30.setColor(dark_purple); 							// dark purple
	w.addObject(sphere_ptr30);
	
	Sphere	sphere_ptr31 = new Sphere(new Point3D(-30, 68, -130), 12); 
	sphere_ptr31.setColor(light_purple); 							// light purple
	w.addObject(sphere_ptr31);
	
	Sphere	sphere_ptr32 = new Sphere(new Point3D(58, 56, -180), 11);   
	sphere_ptr32.setColor(green);									//  green
	w.addObject(sphere_ptr32);
	
	Sphere	sphere_ptr33 = new Sphere(new Point3D(-63, -39, -180), 11); 
	sphere_ptr33.setColor(green);									// green 
	w.addObject(sphere_ptr33);
	
	Sphere	sphere_ptr34 = new Sphere(new Point3D(46, 68, -200), 10); 	
	sphere_ptr34.setColor(light_purple);							// light purple
	w.addObject(sphere_ptr34);
	
	Sphere	sphere_ptr35 = new Sphere(new Point3D(-3, -72, -130), 12); 
	sphere_ptr35.setColor(light_purple);							// light purple
	w.addObject(sphere_ptr35);
    }
    
}
