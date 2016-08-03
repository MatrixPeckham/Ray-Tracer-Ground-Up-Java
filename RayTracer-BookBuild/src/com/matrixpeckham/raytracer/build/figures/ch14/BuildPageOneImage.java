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
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildPageOneImage implements BuildWorldFunction {

    @Override
    public void build(World w) {

        int numSamples = 1;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setPixelSize(0.5);
        w.vp.setSamples(numSamples);

        w.tracer = new RayCast(w);

        Pinhole pinhole = new Pinhole();
        pinhole.setEye(0, 0, 10000);
        pinhole.setLookat(new Point3D(0.0));
        pinhole.setViewDistance(15000);
        pinhole.computeUVW();
        w.setCamera(pinhole);

        Directional light = new Directional();
        light.setDirection(100, 100, 200);
        light.scaleRadiance(3.0);
        w.addLight(light);

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
        Matte matte1 = new Matte();
        matte1.setKa(ka);
        matte1.setKd(kd);
        matte1.setCd(yellow);
        Sphere sphere1 = new Sphere(new Point3D(5, 3, 0), 30);
        sphere1.setMaterial(matte1);
        sphere1.setShadows(false);
        w.addObject(sphere1);

        Matte matte2 = new Matte();
        matte2.setKa(ka);
        matte2.setKd(kd);
        matte2.setCd(brown);
        Sphere sphere2 = new Sphere(new Point3D(45, -7, -60), 20);
        sphere2.setMaterial(matte2);
        sphere2.setShadows(false);
        w.addObject(sphere2);

        Matte matte3 = new Matte();
        matte3.setKa(ka);
        matte3.setKd(kd);
        matte3.setCd(darkGreen);
        Sphere sphere3 = new Sphere(new Point3D(40, 43, -100), 17);
        sphere3.setMaterial(matte3);
        sphere3.setShadows(false);								// dark green
        w.addObject(sphere3);

        Matte matte4 = new Matte();
        matte4.setKa(ka);
        matte4.setKd(kd);
        matte4.setCd(orange);
        Sphere sphere4 = new Sphere(new Point3D(-20, 28, -15), 20);
        sphere4.setMaterial(matte4);
        sphere4.setShadows(false);								// orange
        w.addObject(sphere4);

        Matte matte5 = new Matte();
        matte5.setKa(ka);
        matte5.setKd(kd);
        matte5.setCd(green);
        Sphere sphere5 = new Sphere(new Point3D(-25, -7, -35), 27);
        sphere5.setMaterial(matte5);
        sphere5.setShadows(false);								// green
        w.addObject(sphere5);

        Matte matte6 = new Matte();
        matte6.setKa(ka);
        matte6.setKd(kd);
        matte6.setCd(lightGreen);
        Sphere sphere6 = new Sphere(new Point3D(20, -27, -35), 25);
        sphere6.setMaterial(matte6);
        sphere6.setShadows(false);								// light green
        w.addObject(sphere6);

        Matte matte7 = new Matte();
        matte7.setKa(ka);
        matte7.setKd(kd);
        matte7.setCd(green);
        Sphere sphere7 = new Sphere(new Point3D(35, 18, -35), 22);
        sphere7.setMaterial(matte7);
        sphere7.setShadows(false);   							// green
        w.addObject(sphere7);

        Matte matte8 = new Matte();
        matte8.setKa(ka);
        matte8.setKd(kd);
        matte8.setCd(brown);
        Sphere sphere8 = new Sphere(new Point3D(-57, -17, -50), 15);
        sphere8.setMaterial(matte8);
        sphere8.setShadows(false);								// brown
        w.addObject(sphere8);

        Matte matte9 = new Matte();
        matte9.setKa(ka);
        matte9.setKd(kd);
        matte9.setCd(lightGreen);
        Sphere sphere9 = new Sphere(new Point3D(-47, 16, -80), 23);
        sphere9.setMaterial(matte9);
        sphere9.setShadows(false);								// light green
        w.addObject(sphere9);

        Matte matte10 = new Matte();
        matte10.setKa(ka);
        matte10.setKd(kd);
        matte10.setCd(darkGreen);
        Sphere sphere10 = new Sphere(new Point3D(-15, -32, -60), 22);
        sphere10.setMaterial(matte10);
        sphere10.setShadows(false);     						// dark green
        w.addObject(sphere10);

        Matte matte11 = new Matte();
        matte11.setKa(ka);
        matte11.setKd(kd);
        matte11.setCd(darkYellow);
        Sphere sphere11 = new Sphere(new Point3D(-35, -37, -80), 22);
        sphere11.setMaterial(matte11);
        sphere11.setShadows(false);							// dark yellow
        w.addObject(sphere11);

        Matte matte12 = new Matte();
        matte12.setKa(ka);
        matte12.setKd(kd);
        matte12.setCd(darkYellow);
        Sphere sphere12 = new Sphere(new Point3D(10, 43, -80), 22);
        sphere12.setMaterial(matte12);
        sphere12.setShadows(false);							// dark yellow
        w.addObject(sphere12);

        Matte matte13 = new Matte();
        matte13.setKa(ka);
        matte13.setKd(kd);
        matte13.setCd(darkYellow);
        Sphere sphere13 = new Sphere(new Point3D(30, -7, -80), 10);
        sphere13.setMaterial(matte13);
        sphere13.setShadows(false);
        w.addObject(sphere13);											// dark yellow (hidden)

        Matte matte14 = new Matte();
        matte14.setKa(ka);
        matte14.setKd(kd);
        matte14.setCd(darkGreen);
        Sphere sphere14 = new Sphere(new Point3D(-40, 48, -110), 18);
        sphere14.setMaterial(matte14);
        sphere14.setShadows(false); 							// dark green
        w.addObject(sphere14);

        Matte matte15 = new Matte();
        matte15.setKa(ka);
        matte15.setKd(kd);
        matte15.setCd(brown);
        Sphere sphere15 = new Sphere(new Point3D(-10, 53, -120), 18);
        sphere15.setMaterial(matte15);
        sphere15.setShadows(false); 							// brown
        w.addObject(sphere15);

        Matte matte16 = new Matte();
        matte16.setKa(ka);
        matte16.setKd(kd);
        matte16.setCd(lightPurple);
        Sphere sphere16 = new Sphere(new Point3D(-55, -52, -100), 10);
        sphere16.setMaterial(matte16);
        sphere16.setShadows(false);							// light purple
        w.addObject(sphere16);

        Matte matte17 = new Matte();
        matte17.setKa(ka);
        matte17.setKd(kd);
        matte17.setCd(brown);
        Sphere sphere17 = new Sphere(new Point3D(5, -52, -100), 15);
        sphere17.setMaterial(matte17);
        sphere17.setShadows(false);							// browm
        w.addObject(sphere17);

        Matte matte18 = new Matte();
        matte18.setKa(ka);
        matte18.setKd(kd);
        matte18.setCd(darkPurple);
        Sphere sphere18 = new Sphere(new Point3D(-20, -57, -120), 15);
        sphere18.setMaterial(matte18);
        sphere18.setShadows(false);							// dark purple
        w.addObject(sphere18);

        Matte matte19 = new Matte();
        matte19.setKa(ka);
        matte19.setKd(kd);
        matte19.setCd(darkGreen);
        Sphere sphere19 = new Sphere(new Point3D(55, -27, -100), 17);
        sphere19.setMaterial(matte19);
        sphere19.setShadows(false);							// dark green
        w.addObject(sphere19);

        Matte matte20 = new Matte();
        matte20.setKa(ka);
        matte20.setKd(kd);
        matte20.setCd(brown);
        Sphere sphere20 = new Sphere(new Point3D(50, -47, -120), 15);
        sphere20.setMaterial(matte20);
        sphere20.setShadows(false);							// browm
        w.addObject(sphere20);

        Matte matte21 = new Matte();
        matte21.setKa(ka);
        matte21.setKd(kd);
        matte21.setCd(lightPurple);
        Sphere sphere21 = new Sphere(new Point3D(70, -42, -150), 10);
        sphere21.setMaterial(matte21);
        sphere21.setShadows(false);							// light purple
        w.addObject(sphere21);

        Matte matte22 = new Matte();
        matte22.setKa(ka);
        matte22.setKd(kd);
        matte22.setCd(lightPurple);
        Sphere sphere22 = new Sphere(new Point3D(5, 73, -130), 12);
        sphere22.setMaterial(matte22);
        sphere22.setShadows(false);							// light purple
        w.addObject(sphere22);

        Matte matte23 = new Matte();
        matte23.setKa(ka);
        matte23.setKd(kd);
        matte23.setCd(darkPurple);
        Sphere sphere23 = new Sphere(new Point3D(66, 21, -130), 13);
        sphere23.setMaterial(matte23);
        sphere23.setShadows(false);							// dark purple
        w.addObject(sphere23);

        Matte matte24 = new Matte();
        matte24.setKa(ka);
        matte24.setKd(kd);
        matte24.setCd(lightPurple);
        Sphere sphere24 = new Sphere(new Point3D(72, -12, -140), 12);
        sphere24.setMaterial(matte24);
        sphere24.setShadows(false);							// light purple
        w.addObject(sphere24);

        Matte matte25 = new Matte();
        matte25.setKa(ka);
        matte25.setKd(kd);
        matte25.setCd(green);
        Sphere sphere25 = new Sphere(new Point3D(64, 5, -160), 11);
        sphere25.setMaterial(matte25);
        sphere25.setShadows(false);					 		// green
        w.addObject(sphere25);

        Matte matte26 = new Matte();
        matte26.setKa(ka);
        matte26.setKd(kd);
        matte26.setCd(lightPurple);
        Sphere sphere26 = new Sphere(new Point3D(55, 38, -160), 12);
        sphere26.setMaterial(matte26);
        sphere26.setShadows(false);							// light purple
        w.addObject(sphere26);

        Matte matte27 = new Matte();
        matte27.setKa(ka);
        matte27.setKd(kd);
        matte27.setCd(lightPurple);
        Sphere sphere27 = new Sphere(new Point3D(-73, -2, -160), 12);
        sphere27.setMaterial(matte27);
        sphere27.setShadows(false);							// light purple
        w.addObject(sphere27);

        Matte matte28 = new Matte();
        matte28.setKa(ka);
        matte28.setKd(kd);
        matte28.setCd(darkPurple);
        Sphere sphere28 = new Sphere(new Point3D(30, -62, -140), 15);
        sphere28.setMaterial(matte28);
        sphere28.setShadows(false); 							// dark purple
        w.addObject(sphere28);

        Matte matte29 = new Matte();
        matte29.setKa(ka);
        matte29.setKd(kd);
        matte29.setCd(darkPurple);
        Sphere sphere29 = new Sphere(new Point3D(25, 63, -140), 15);
        sphere29.setMaterial(matte29);
        sphere29.setShadows(false);							// dark purple
        w.addObject(sphere29);

        Matte matte30 = new Matte();
        matte30.setKa(ka);
        matte30.setKd(kd);
        matte30.setCd(darkPurple);
        Sphere sphere30 = new Sphere(new Point3D(-60, 46, -140), 15);
        sphere30.setMaterial(matte30);
        sphere30.setShadows(false); 							// dark purple
        w.addObject(sphere30);

        Matte matte31 = new Matte();
        matte31.setKa(ka);
        matte31.setKd(kd);
        matte31.setCd(lightPurple);
        Sphere sphere31 = new Sphere(new Point3D(-30, 68, -130), 12);
        sphere31.setMaterial(matte31);
        sphere31.setShadows(false); 							// light purple
        w.addObject(sphere31);

        Matte matte32 = new Matte();
        matte32.setKa(ka);
        matte32.setKd(kd);
        matte32.setCd(green);
        Sphere sphere32 = new Sphere(new Point3D(58, 56, -180), 11);
        sphere32.setMaterial(matte32);
        sphere32.setShadows(false);							//  green
        w.addObject(sphere32);

        Matte matte33 = new Matte();
        matte33.setKa(ka);
        matte33.setKd(kd);
        matte33.setCd(green);
        Sphere sphere33 = new Sphere(new Point3D(-63, -39, -180), 11);
        sphere33.setMaterial(matte33);
        sphere33.setShadows(false);							// green
        w.addObject(sphere33);

        Matte matte34 = new Matte();
        matte34.setKa(ka);
        matte34.setKd(kd);
        matte34.setCd(lightPurple);
        Sphere sphere34 = new Sphere(new Point3D(46, 68, -200), 10);
        sphere34.setMaterial(matte34);
        sphere34.setShadows(false);							// light purple
        w.addObject(sphere34);

        Matte matte35 = new Matte();
        matte35.setKa(ka);
        matte35.setKd(kd);
        matte35.setCd(lightPurple);
        Sphere sphere35 = new Sphere(new Point3D(-3, -72, -130), 12);
        sphere35.setMaterial(matte35);
        sphere35.setShadows(false);							// light purple
        w.addObject(sphere35);

    }

}
