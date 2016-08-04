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
package com.matrixpeckham.raytracer.build.figures.ch14;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.tonemapping.ClampToColor;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure22 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 14.22
// In part (a), where the light's radiance is scaled by 3.0, there is no overflow
// In part (b), where the radiance is scaled by 4.5, there is overflow, but nothing
// is done about it
// This is arranged by inserting the statement
//		mappedColor = rawColor;
// in the function World::displayPixel in Listing 14.18, just before the gamma correction
        //NOTE: JAVA COLOR THROWS AN EXCEPTION ON OVERFLOW (B) WILL NOT LOOK
        //THE SAME, RenderPixel CHECKS FOR OVERFLOW TO PREVENT THIS, AND CLAMPS
        int numSamples = 16;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setPixelSize(0.5);
        w.vp.setSamples(numSamples);
        w.vp.setToneMapper(new ClampToColor());
        w.tracer = new RayCast(w);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(0, 0, 10000);
        pinholePtr.setLookat(new Point3D(0, 0, 0));
        pinholePtr.setViewDistance(15000);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        Directional lightPtr = new Directional();
        lightPtr.setDirection(100, 100, 200);
        lightPtr.setColor(1.0, 1.0, 1.0);
        lightPtr.setShadows(false);
//        lightPtr.scaleRadiance(3.0);        	// for Figure 14.22 (a)
        lightPtr.scaleRadiance(4.5); 	  	// for Figure 14.22 (b)
        w.addLight(lightPtr);

        // colors
        RGBColor yellow = new RGBColor(1, 1, 0);										// yellow
        RGBColor brown = new RGBColor(0.71, 0.40, 0.16);								// brown
        RGBColor darkGreen = new RGBColor(0.0, 0.41, 0.41);							// darkGreen
        RGBColor orange = new RGBColor(1, 0.75, 0);									// orange
        RGBColor green = new RGBColor(0, 0.6, 0.3);									// green
        RGBColor lightGreen = new RGBColor(0.65, 1, 0.30);							// light green
        RGBColor darkYellow = new RGBColor(0.61, 0.61, 0);							// dark yellow
        RGBColor lightPurple = new RGBColor(0.65, 0.3, 1);							// light purple
        RGBColor darkPurple = new RGBColor(0.5, 0, 1);								// dark purple

        // Matt material reflection coefficients
        double ka = 0.25;
        double kd = 0.75;

        // spheres
        Matte mattePtr1 = new Matte();
        mattePtr1.setKa(ka);
        mattePtr1.setKd(kd);
        mattePtr1.setCd(yellow);
        Sphere spherePtr1 = new Sphere(new Point3D(5, 3, 0), 30);
        spherePtr1.setMaterial(mattePtr1);	   							// yellow
        w.addObject(spherePtr1);

        Matte mattePtr2 = new Matte();
        mattePtr2.setKa(ka);
        mattePtr2.setKd(kd);
        mattePtr2.setCd(brown);
        Sphere spherePtr2 = new Sphere(new Point3D(45, -7, -60), 20);
        spherePtr2.setMaterial(mattePtr2);								// brown
        w.addObject(spherePtr2);

        Matte mattePtr3 = new Matte();
        mattePtr3.setKa(ka);
        mattePtr3.setKd(kd);
        mattePtr3.setCd(darkGreen);
        Sphere spherePtr3 = new Sphere(new Point3D(40, 43, -100), 17);
        spherePtr3.setMaterial(mattePtr3);								// dark green
        w.addObject(spherePtr3);

        Matte mattePtr4 = new Matte();
        mattePtr4.setKa(ka);
        mattePtr4.setKd(kd);
        mattePtr4.setCd(orange);
        Sphere spherePtr4 = new Sphere(new Point3D(-20, 28, -15), 20);
        spherePtr4.setMaterial(mattePtr4);								// orange
        w.addObject(spherePtr4);

        Matte mattePtr5 = new Matte();
        mattePtr5.setKa(ka);
        mattePtr5.setKd(kd);
        mattePtr5.setCd(green);
        Sphere spherePtr5 = new Sphere(new Point3D(-25, -7, -35), 27);
        spherePtr5.setMaterial(mattePtr5);								// green
        w.addObject(spherePtr5);

        Matte mattePtr6 = new Matte();
        mattePtr6.setKa(ka);
        mattePtr6.setKd(kd);
        mattePtr6.setCd(lightGreen);
        Sphere spherePtr6 = new Sphere(new Point3D(20, -27, -35), 25);
        spherePtr6.setMaterial(mattePtr6);								// light green
        w.addObject(spherePtr6);

        Matte mattePtr7 = new Matte();
        mattePtr7.setKa(ka);
        mattePtr7.setKd(kd);
        mattePtr7.setCd(green);
        Sphere spherePtr7 = new Sphere(new Point3D(35, 18, -35), 22);
        spherePtr7.setMaterial(mattePtr7);   							// green
        w.addObject(spherePtr7);

        Matte mattePtr8 = new Matte();
        mattePtr8.setKa(ka);
        mattePtr8.setKd(kd);
        mattePtr8.setCd(brown);
        Sphere spherePtr8 = new Sphere(new Point3D(-57, -17, -50), 15);
        spherePtr8.setMaterial(mattePtr8);								// brown
        w.addObject(spherePtr8);

        Matte mattePtr9 = new Matte();
        mattePtr9.setKa(ka);
        mattePtr9.setKd(kd);
        mattePtr9.setCd(lightGreen);
        Sphere spherePtr9 = new Sphere(new Point3D(-47, 16, -80), 23);
        spherePtr9.setMaterial(mattePtr9);								// light green
        w.addObject(spherePtr9);

        Matte mattePtr10 = new Matte();
        mattePtr10.setKa(ka);
        mattePtr10.setKd(kd);
        mattePtr10.setCd(darkGreen);
        Sphere spherePtr10 = new Sphere(new Point3D(-15, -32, -60), 22);
        spherePtr10.setMaterial(mattePtr10);     						// dark green
        w.addObject(spherePtr10);

        Matte mattePtr11 = new Matte();
        mattePtr11.setKa(ka);
        mattePtr11.setKd(kd);
        mattePtr11.setCd(darkYellow);
        Sphere spherePtr11 = new Sphere(new Point3D(-35, -37, -80), 22);
        spherePtr11.setMaterial(mattePtr11);							// dark yellow
        w.addObject(spherePtr11);

        Matte mattePtr12 = new Matte();
        mattePtr12.setKa(ka);
        mattePtr12.setKd(kd);
        mattePtr12.setCd(darkYellow);
        Sphere spherePtr12 = new Sphere(new Point3D(10, 43, -80), 22);
        spherePtr12.setMaterial(mattePtr12);							// dark yellow
        w.addObject(spherePtr12);

        Matte mattePtr13 = new Matte();
        mattePtr13.setKa(ka);
        mattePtr13.setKd(kd);
        mattePtr13.setCd(darkYellow);
        Sphere spherePtr13 = new Sphere(new Point3D(30, -7, -80), 10);
        spherePtr13.setMaterial(mattePtr13);
        w.addObject(spherePtr13);											// dark yellow (hidden)

        Matte mattePtr14 = new Matte();
        mattePtr14.setKa(ka);
        mattePtr14.setKd(kd);
        mattePtr14.setCd(darkGreen);
        Sphere spherePtr14 = new Sphere(new Point3D(-40, 48, -110), 18);
        spherePtr14.setMaterial(mattePtr14); 							// dark green
        w.addObject(spherePtr14);

        Matte mattePtr15 = new Matte();
        mattePtr15.setKa(ka);
        mattePtr15.setKd(kd);
        mattePtr15.setCd(brown);
        Sphere spherePtr15 = new Sphere(new Point3D(-10, 53, -120), 18);
        spherePtr15.setMaterial(mattePtr15); 							// brown
        w.addObject(spherePtr15);

        Matte mattePtr16 = new Matte();
        mattePtr16.setKa(ka);
        mattePtr16.setKd(kd);
        mattePtr16.setCd(lightPurple);
        Sphere spherePtr16 = new Sphere(new Point3D(-55, -52, -100), 10);
        spherePtr16.setMaterial(mattePtr16);							// light purple
        w.addObject(spherePtr16);

        Matte mattePtr17 = new Matte();
        mattePtr17.setKa(ka);
        mattePtr17.setKd(kd);
        mattePtr17.setCd(brown);
        Sphere spherePtr17 = new Sphere(new Point3D(5, -52, -100), 15);
        spherePtr17.setMaterial(mattePtr17);							// browm
        w.addObject(spherePtr17);

        Matte mattePtr18 = new Matte();
        mattePtr18.setKa(ka);
        mattePtr18.setKd(kd);
        mattePtr18.setCd(darkPurple);
        Sphere spherePtr18 = new Sphere(new Point3D(-20, -57, -120), 15);
        spherePtr18.setMaterial(mattePtr18);							// dark purple
        w.addObject(spherePtr18);

        Matte mattePtr19 = new Matte();
        mattePtr19.setKa(ka);
        mattePtr19.setKd(kd);
        mattePtr19.setCd(darkGreen);
        Sphere spherePtr19 = new Sphere(new Point3D(55, -27, -100), 17);
        spherePtr19.setMaterial(mattePtr19);							// dark green
        w.addObject(spherePtr19);

        Matte mattePtr20 = new Matte();
        mattePtr20.setKa(ka);
        mattePtr20.setKd(kd);
        mattePtr20.setCd(brown);
        Sphere spherePtr20 = new Sphere(new Point3D(50, -47, -120), 15);
        spherePtr20.setMaterial(mattePtr20);							// browm
        w.addObject(spherePtr20);

        Matte mattePtr21 = new Matte();
        mattePtr21.setKa(ka);
        mattePtr21.setKd(kd);
        mattePtr21.setCd(lightPurple);
        Sphere spherePtr21 = new Sphere(new Point3D(70, -42, -150), 10);
        spherePtr21.setMaterial(mattePtr21);							// light purple
        w.addObject(spherePtr21);

        Matte mattePtr22 = new Matte();
        mattePtr22.setKa(ka);
        mattePtr22.setKd(kd);
        mattePtr22.setCd(lightPurple);
        Sphere spherePtr22 = new Sphere(new Point3D(5, 73, -130), 12);
        spherePtr22.setMaterial(mattePtr22);							// light purple
        w.addObject(spherePtr22);

        Matte mattePtr23 = new Matte();
        mattePtr23.setKa(ka);
        mattePtr23.setKd(kd);
        mattePtr23.setCd(darkPurple);
        Sphere spherePtr23 = new Sphere(new Point3D(66, 21, -130), 13);
        spherePtr23.setMaterial(mattePtr23);							// dark purple
        w.addObject(spherePtr23);

        Matte mattePtr24 = new Matte();
        mattePtr24.setKa(ka);
        mattePtr24.setKd(kd);
        mattePtr24.setCd(lightPurple);
        Sphere spherePtr24 = new Sphere(new Point3D(72, -12, -140), 12);
        spherePtr24.setMaterial(mattePtr24);							// light purple
        w.addObject(spherePtr24);

        Matte mattePtr25 = new Matte();
        mattePtr25.setKa(ka);
        mattePtr25.setKd(kd);
        mattePtr25.setCd(green);
        Sphere spherePtr25 = new Sphere(new Point3D(64, 5, -160), 11);
        spherePtr25.setMaterial(mattePtr25);					 		// green
        w.addObject(spherePtr25);

        Matte mattePtr26 = new Matte();
        mattePtr26.setKa(ka);
        mattePtr26.setKd(kd);
        mattePtr26.setCd(lightPurple);
        Sphere spherePtr26 = new Sphere(new Point3D(55, 38, -160), 12);
        spherePtr26.setMaterial(mattePtr26);							// light purple
        w.addObject(spherePtr26);

        Matte mattePtr27 = new Matte();
        mattePtr27.setKa(ka);
        mattePtr27.setKd(kd);
        mattePtr27.setCd(lightPurple);
        Sphere spherePtr27 = new Sphere(new Point3D(-73, -2, -160), 12);
        spherePtr27.setMaterial(mattePtr27);							// light purple
        w.addObject(spherePtr27);

        Matte mattePtr28 = new Matte();
        mattePtr28.setKa(ka);
        mattePtr28.setKd(kd);
        mattePtr28.setCd(darkPurple);
        Sphere spherePtr28 = new Sphere(new Point3D(30, -62, -140), 15);
        spherePtr28.setMaterial(mattePtr28); 							// dark purple
        w.addObject(spherePtr28);

        Matte mattePtr29 = new Matte();
        mattePtr29.setKa(ka);
        mattePtr29.setKd(kd);
        mattePtr29.setCd(darkPurple);
        Sphere spherePtr29 = new Sphere(new Point3D(25, 63, -140), 15);
        spherePtr29.setMaterial(mattePtr29);							// dark purple
        w.addObject(spherePtr29);

        Matte mattePtr30 = new Matte();
        mattePtr30.setKa(ka);
        mattePtr30.setKd(kd);
        mattePtr30.setCd(darkPurple);
        Sphere spherePtr30 = new Sphere(new Point3D(-60, 46, -140), 15);
        spherePtr30.setMaterial(mattePtr30); 							// dark purple
        w.addObject(spherePtr30);

        Matte mattePtr31 = new Matte();
        mattePtr31.setKa(ka);
        mattePtr31.setKd(kd);
        mattePtr31.setCd(lightPurple);
        Sphere spherePtr31 = new Sphere(new Point3D(-30, 68, -130), 12);
        spherePtr31.setMaterial(mattePtr31); 							// light purple
        w.addObject(spherePtr31);

        Matte mattePtr32 = new Matte();
        mattePtr32.setKa(ka);
        mattePtr32.setKd(kd);
        mattePtr32.setCd(green);
        Sphere spherePtr32 = new Sphere(new Point3D(58, 56, -180), 11);
        spherePtr32.setMaterial(mattePtr32);							//  green
        w.addObject(spherePtr32);

        Matte mattePtr33 = new Matte();
        mattePtr33.setKa(ka);
        mattePtr33.setKd(kd);
        mattePtr33.setCd(green);
        Sphere spherePtr33 = new Sphere(new Point3D(-63, -39, -180), 11);
        spherePtr33.setMaterial(mattePtr33);							// green
        w.addObject(spherePtr33);

        Matte mattePtr34 = new Matte();
        mattePtr34.setKa(ka);
        mattePtr34.setKd(kd);
        mattePtr34.setCd(lightPurple);
        Sphere spherePtr34 = new Sphere(new Point3D(46, 68, -200), 10);
        spherePtr34.setMaterial(mattePtr34);							// light purple
        w.addObject(spherePtr34);

        Matte mattePtr35 = new Matte();
        mattePtr35.setKa(ka);
        mattePtr35.setKd(kd);
        mattePtr35.setCd(lightPurple);
        Sphere spherePtr35 = new Sphere(new Point3D(-3, -72, -130), 12);
        spherePtr35.setMaterial(mattePtr35);							// light purple
        w.addObject(spherePtr35);

    }

}
