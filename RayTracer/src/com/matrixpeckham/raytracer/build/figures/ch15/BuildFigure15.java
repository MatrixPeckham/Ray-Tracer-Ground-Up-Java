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
package com.matrixpeckham.raytracer.build.figures.ch15;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Sphere;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Phong;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure15 implements BuildWorldFunction {

    @Override
    public void build(World w) {
	// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 15.15
        int numSamples = 1;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setPixelSize(0.5);
        w.vp.setSamples(numSamples);

        w.tracer = new RayCast(w);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(0, 0, 10000);
        pinholePtr.setLookat(new Point3D());
        pinholePtr.setViewDistance(15000);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        Directional lightPtr = new Directional();
        lightPtr.setDirection(100, 100, 200);
        lightPtr.scaleRadiance(3.0);
        lightPtr.setShadows(false);
        w.addLight(lightPtr);

	// colors
        double a = 0.75;  // scaling factor for yellow, orange, and light green

        RGBColor yellow = new RGBColor(a * 1, a * 1, 0);								// yellow
        RGBColor brown = new RGBColor(0.71, 0.40, 0.16);								// brown
        RGBColor darkGreen = new RGBColor(0.0, 0.41, 0.41);							// darkGreen
        RGBColor orange = new RGBColor(a * 1, a * 0.75, 0);							// orange
        RGBColor green = new RGBColor(0, 0.6, 0.3);									// green
        RGBColor lightGreen = new RGBColor(a * 0.65, a * 1, a * 0.30);				// light green
        RGBColor darkYellow = new RGBColor(0.61, 0.61, 0);							// dark yellow
        RGBColor lightPurple = new RGBColor(0.65, 0.3, 1);							// light purple
        RGBColor darkPurple = new RGBColor(0.5, 0, 1);								// dark purple

	// Phong materials reflection coefficients
        double ka = 0.25;
        double kd = 0.75;
        double ks = 0.1;
        double exp = 0.25;

	// spheres
        Phong phongPtr1 = new Phong();
        phongPtr1.setKa(ka);
        phongPtr1.setKd(kd);
        phongPtr1.setKs(ks);
        phongPtr1.setExp(exp);
        phongPtr1.setCd(yellow);

        Sphere spherePtr1 = new Sphere(new Point3D(5, 3, 0), 30);
        spherePtr1.setMaterial(phongPtr1);	   							// yellow
        w.addObject(spherePtr1);

        Phong phongPtr2 = new Phong();
        phongPtr2.setKa(ka);
        phongPtr2.setKd(kd);
        phongPtr2.setKs(ks);
        phongPtr2.setExp(exp);
        phongPtr2.setCd(brown);

        Sphere spherePtr2 = new Sphere(new Point3D(45, -7, -60), 20);
        spherePtr2.setMaterial(phongPtr2);								// brown
        w.addObject(spherePtr2);

        Phong phongPtr3 = new Phong();
        phongPtr3.setKa(ka);
        phongPtr3.setKd(kd);
        phongPtr3.setKs(ks);
        phongPtr3.setExp(exp);
        phongPtr3.setCd(darkGreen);

        Sphere spherePtr3 = new Sphere(new Point3D(40, 43, -100), 17);
        spherePtr3.setMaterial(phongPtr3);								// dark green
        w.addObject(spherePtr3);

        Phong phongPtr4 = new Phong();
        phongPtr4.setKa(ka);
        phongPtr4.setKd(kd);
        phongPtr4.setKs(ks);
        phongPtr4.setExp(exp);
        phongPtr4.setCd(orange);

        Sphere spherePtr4 = new Sphere(new Point3D(-20, 28, -15), 20);
        spherePtr4.setMaterial(phongPtr4);								// orange
        w.addObject(spherePtr4);

        Phong phongPtr5 = new Phong();
        phongPtr5.setKa(ka);
        phongPtr5.setKd(kd);
        phongPtr5.setKs(ks);
        phongPtr5.setExp(exp);
        phongPtr5.setCd(green);

        Sphere spherePtr5 = new Sphere(new Point3D(-25, -7, -35), 27);
        spherePtr5.setMaterial(phongPtr5);								// green
        w.addObject(spherePtr5);

        Phong phongPtr6 = new Phong();
        phongPtr6.setKa(ka);
        phongPtr6.setKd(kd);
        phongPtr6.setKs(ks);
        phongPtr6.setExp(exp);
        phongPtr6.setCd(lightGreen);

        Sphere spherePtr6 = new Sphere(new Point3D(20, -27, -35), 25);
        spherePtr6.setMaterial(phongPtr6);								// light green
        w.addObject(spherePtr6);

        Phong phongPtr7 = new Phong();
        phongPtr7.setKa(ka);
        phongPtr7.setKd(kd);
        phongPtr7.setKs(ks);
        phongPtr7.setExp(exp);
        phongPtr7.setCd(green);

        Sphere spherePtr7 = new Sphere(new Point3D(35, 18, -35), 22);
        spherePtr7.setMaterial(phongPtr7);   							// green
        w.addObject(spherePtr7);

        Phong phongPtr8 = new Phong();
        phongPtr8.setKa(ka);
        phongPtr8.setKd(kd);
        phongPtr8.setKs(ks);
        phongPtr8.setExp(exp);
        phongPtr8.setCd(brown);

        Sphere spherePtr8 = new Sphere(new Point3D(-57, -17, -50), 15);
        spherePtr8.setMaterial(phongPtr8);								// brown
        w.addObject(spherePtr8);

        Phong phongPtr9 = new Phong();
        phongPtr9.setKa(ka);
        phongPtr9.setKd(kd);
        phongPtr9.setKs(ks);
        phongPtr9.setExp(exp);
        phongPtr9.setCd(lightGreen);

        Sphere spherePtr9 = new Sphere(new Point3D(-47, 16, -80), 23);
        spherePtr9.setMaterial(phongPtr9);								// light green
        w.addObject(spherePtr9);

        Phong phongPtr10 = new Phong();
        phongPtr10.setKa(ka);
        phongPtr10.setKd(kd);
        phongPtr10.setKs(ks);
        phongPtr10.setExp(exp);
        phongPtr10.setCd(darkGreen);

        Sphere spherePtr10 = new Sphere(new Point3D(-15, -32, -60), 22);
        spherePtr10.setMaterial(phongPtr10);     						// dark green
        w.addObject(spherePtr10);

        Phong phongPtr11 = new Phong();
        phongPtr11.setKa(ka);
        phongPtr11.setKd(kd);
        phongPtr11.setKs(ks);
        phongPtr11.setExp(exp);
        phongPtr11.setCd(darkYellow);

        Sphere spherePtr11 = new Sphere(new Point3D(-35, -37, -80), 22);
        spherePtr11.setMaterial(phongPtr11);							// dark yellow
        w.addObject(spherePtr11);

        Phong phongPtr12 = new Phong();
        phongPtr12.setKa(ka);
        phongPtr12.setKd(kd);
        phongPtr12.setKs(ks);
        phongPtr12.setExp(exp);
        phongPtr12.setCd(darkYellow);

        Sphere spherePtr12 = new Sphere(new Point3D(10, 43, -80), 22);
        spherePtr12.setMaterial(phongPtr12);							// dark yellow
        w.addObject(spherePtr12);

        Phong phongPtr13 = new Phong();
        phongPtr13.setKa(ka);
        phongPtr13.setKd(kd);
        phongPtr13.setKs(ks);
        phongPtr13.setExp(exp);
        phongPtr13.setCd(darkYellow);

        Sphere spherePtr13 = new Sphere(new Point3D(30, -7, -80), 10);
        spherePtr13.setMaterial(phongPtr13);
        w.addObject(spherePtr13);											// dark yellow (hidden)

        Phong phongPtr14 = new Phong();
        phongPtr14.setKa(ka);
        phongPtr14.setKd(kd);
        phongPtr14.setKs(ks);
        phongPtr14.setExp(exp);
        phongPtr14.setCd(darkGreen);

        Sphere spherePtr14 = new Sphere(new Point3D(-40, 48, -110), 18);
        spherePtr14.setMaterial(phongPtr14); 							// dark green
        w.addObject(spherePtr14);

        Phong phongPtr15 = new Phong();
        phongPtr15.setKa(ka);
        phongPtr15.setKd(kd);
        phongPtr15.setKs(ks);
        phongPtr15.setExp(exp);
        phongPtr15.setCd(brown);

        Sphere spherePtr15 = new Sphere(new Point3D(-10, 53, -120), 18);
        spherePtr15.setMaterial(phongPtr15); 							// brown
        w.addObject(spherePtr15);

        Phong phongPtr16 = new Phong();
        phongPtr16.setKa(ka);
        phongPtr16.setKd(kd);
        phongPtr16.setKs(ks);
        phongPtr16.setExp(exp);
        phongPtr16.setCd(lightPurple);

        Sphere spherePtr16 = new Sphere(new Point3D(-55, -52, -100), 10);
        spherePtr16.setMaterial(phongPtr16);							// light purple
        w.addObject(spherePtr16);

        Phong phongPtr17 = new Phong();
        phongPtr17.setKa(ka);
        phongPtr17.setKd(kd);
        phongPtr17.setKs(ks);
        phongPtr17.setExp(exp);
        phongPtr17.setCd(brown);

        Sphere spherePtr17 = new Sphere(new Point3D(5, -52, -100), 15);
        spherePtr17.setMaterial(phongPtr17);							// browm
        w.addObject(spherePtr17);

        Phong phongPtr18 = new Phong();
        phongPtr18.setKa(ka);
        phongPtr18.setKd(kd);
        phongPtr18.setKs(ks);
        phongPtr18.setExp(exp);
        phongPtr18.setCd(darkPurple);

        Sphere spherePtr18 = new Sphere(new Point3D(-20, -57, -120), 15);
        spherePtr18.setMaterial(phongPtr18);							// dark purple
        w.addObject(spherePtr18);

        Phong phongPtr19 = new Phong();
        phongPtr19.setKa(ka);
        phongPtr19.setKd(kd);
        phongPtr19.setKs(ks);
        phongPtr19.setExp(exp);
        phongPtr19.setCd(darkGreen);

        Sphere spherePtr19 = new Sphere(new Point3D(55, -27, -100), 17);
        spherePtr19.setMaterial(phongPtr19);							// dark green
        w.addObject(spherePtr19);

        Phong phongPtr20 = new Phong();
        phongPtr20.setKa(ka);
        phongPtr20.setKd(kd);
        phongPtr20.setKs(ks);
        phongPtr20.setExp(exp);
        phongPtr20.setCd(brown);

        Sphere spherePtr20 = new Sphere(new Point3D(50, -47, -120), 15);
        spherePtr20.setMaterial(phongPtr20);							// browm
        w.addObject(spherePtr20);

        Phong phongPtr21 = new Phong();
        phongPtr21.setKa(ka);
        phongPtr21.setKd(kd);
        phongPtr21.setKs(ks);
        phongPtr21.setExp(exp);
        phongPtr21.setCd(lightPurple);

        Sphere spherePtr21 = new Sphere(new Point3D(70, -42, -150), 10);
        spherePtr21.setMaterial(phongPtr21);							// light purple
        w.addObject(spherePtr21);

        Phong phongPtr22 = new Phong();
        phongPtr22.setKa(ka);
        phongPtr22.setKd(kd);
        phongPtr22.setKs(ks);
        phongPtr22.setExp(exp);
        phongPtr22.setCd(lightPurple);

        Sphere spherePtr22 = new Sphere(new Point3D(5, 73, -130), 12);
        spherePtr22.setMaterial(phongPtr22);							// light purple
        w.addObject(spherePtr22);

        Phong phongPtr23 = new Phong();
        phongPtr23.setKa(ka);
        phongPtr23.setKd(kd);
        phongPtr23.setKs(ks);
        phongPtr23.setExp(exp);
        phongPtr23.setCd(darkPurple);

        Sphere spherePtr23 = new Sphere(new Point3D(66, 21, -130), 13);
        spherePtr23.setMaterial(phongPtr23);							// dark purple
        w.addObject(spherePtr23);

        Phong phongPtr24 = new Phong();
        phongPtr24.setKa(ka);
        phongPtr24.setKd(kd);
        phongPtr24.setKs(ks);
        phongPtr24.setExp(exp);
        phongPtr24.setCd(lightPurple);

        Sphere spherePtr24 = new Sphere(new Point3D(72, -12, -140), 12);
        spherePtr24.setMaterial(phongPtr24);							// light purple
        w.addObject(spherePtr24);

        Phong phongPtr25 = new Phong();
        phongPtr25.setKa(ka);
        phongPtr25.setKd(kd);
        phongPtr25.setKs(ks);
        phongPtr25.setExp(exp);
        phongPtr25.setCd(green);

        Sphere spherePtr25 = new Sphere(new Point3D(64, 5, -160), 11);
        spherePtr25.setMaterial(phongPtr25);					 		// green
        w.addObject(spherePtr25);

        Phong phongPtr26 = new Phong();
        phongPtr26.setKa(ka);
        phongPtr26.setKd(kd);
        phongPtr26.setKs(ks);
        phongPtr26.setExp(exp);
        phongPtr26.setCd(lightPurple);

        Sphere spherePtr26 = new Sphere(new Point3D(55, 38, -160), 12);
        spherePtr26.setMaterial(phongPtr26);							// light purple
        w.addObject(spherePtr26);

        Phong phongPtr27 = new Phong();
        phongPtr27.setKa(ka);
        phongPtr27.setKd(kd);
        phongPtr27.setKs(ks);
        phongPtr27.setExp(exp);
        phongPtr27.setCd(lightPurple);

        Sphere spherePtr27 = new Sphere(new Point3D(-73, -2, -160), 12);
        spherePtr27.setMaterial(phongPtr27);							// light purple
        w.addObject(spherePtr27);

        Phong phongPtr28 = new Phong();
        phongPtr28.setKa(ka);
        phongPtr28.setKd(kd);
        phongPtr28.setKs(ks);
        phongPtr28.setExp(exp);
        phongPtr28.setCd(darkPurple);

        Sphere spherePtr28 = new Sphere(new Point3D(30, -62, -140), 15);
        spherePtr28.setMaterial(phongPtr28); 							// dark purple
        w.addObject(spherePtr28);

        Phong phongPtr29 = new Phong();
        phongPtr29.setKa(ka);
        phongPtr29.setKd(kd);
        phongPtr29.setKs(ks);
        phongPtr29.setExp(exp);
        phongPtr29.setCd(darkPurple);

        Sphere spherePtr29 = new Sphere(new Point3D(25, 63, -140), 15);
        spherePtr29.setMaterial(phongPtr29);							// dark purple
        w.addObject(spherePtr29);

        Phong phongPtr30 = new Phong();
        phongPtr30.setKa(ka);
        phongPtr30.setKd(kd);
        phongPtr30.setKs(ks);
        phongPtr30.setExp(exp);
        phongPtr30.setCd(darkPurple);

        Sphere spherePtr30 = new Sphere(new Point3D(-60, 46, -140), 15);
        spherePtr30.setMaterial(phongPtr30); 							// dark purple
        w.addObject(spherePtr30);

        Phong phongPtr31 = new Phong();
        phongPtr31.setKa(ka);
        phongPtr31.setKd(kd);
        phongPtr31.setKs(ks);
        phongPtr31.setExp(exp);
        phongPtr31.setCd(lightPurple);

        Sphere spherePtr31 = new Sphere(new Point3D(-30, 68, -130), 12);
        spherePtr31.setMaterial(phongPtr31); 							// light purple
        w.addObject(spherePtr31);

        Phong phongPtr32 = new Phong();
        phongPtr32.setKa(ka);
        phongPtr32.setKd(kd);
        phongPtr32.setKs(ks);
        phongPtr32.setExp(exp);
        phongPtr32.setCd(green);

        Sphere spherePtr32 = new Sphere(new Point3D(58, 56, -180), 11);
        spherePtr32.setMaterial(phongPtr32);							//  green
        w.addObject(spherePtr32);

        Phong phongPtr33 = new Phong();
        phongPtr33.setKa(ka);
        phongPtr33.setKd(kd);
        phongPtr33.setKs(ks);
        phongPtr33.setExp(exp);
        phongPtr33.setCd(green);

        Sphere spherePtr33 = new Sphere(new Point3D(-63, -39, -180), 11);
        spherePtr33.setMaterial(phongPtr33);							// green 
        w.addObject(spherePtr33);

        Phong phongPtr34 = new Phong();
        phongPtr34.setKa(ka);
        phongPtr34.setKd(kd);
        phongPtr34.setKs(ks);
        phongPtr34.setExp(exp);
        phongPtr34.setCd(lightPurple);

        Sphere spherePtr34 = new Sphere(new Point3D(46, 68, -200), 10);
        spherePtr34.setMaterial(phongPtr34);							// light purple
        w.addObject(spherePtr34);

        Phong phongPtr35 = new Phong();
        phongPtr35.setKa(ka);
        phongPtr35.setKd(kd);
        phongPtr35.setKs(ks);
        phongPtr35.setExp(exp);
        phongPtr35.setCd(lightPurple);

        Sphere spherePtr35 = new Sphere(new Point3D(-3, -72, -130), 12);
        spherePtr35.setMaterial(phongPtr35);							// light purple
        w.addObject(spherePtr35);

    }

}
