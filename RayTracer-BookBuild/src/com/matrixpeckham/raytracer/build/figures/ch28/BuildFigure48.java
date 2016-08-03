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
package com.matrixpeckham.raytracer.build.figures.ch28;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConvexPartSphere;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Dielectric;
import com.matrixpeckham.raytracer.materials.Phong;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import static com.matrixpeckham.raytracer.util.Utility.PI;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure48 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 28.48
        int numSamples = 9;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setSamples(numSamples);
        w.vp.setMaxDepth(8);

        w.backgroundColor = new RGBColor(0.5, 0.5, 1.0);  // light blue

        w.tracer = new Whitted(w);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(0, -2, 0);
        pinholePtr.setLookat(0, -10, 0);
        pinholePtr.setViewDistance(200.0);
        pinholePtr.setExposureTime(0.17);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        Directional lightPtr1 = new Directional();
        lightPtr1.setDirection(0, 1, 0);    // straight down
        lightPtr1.scaleRadiance(3.0);
        lightPtr1.setShadows(true);
        w.addLight(lightPtr1);

        Directional lightPtr2 = new Directional();
        lightPtr2.setDirection(0, -1, 0);    // straight up
        lightPtr2.scaleRadiance(2.5);
        lightPtr2.setShadows(false);
        w.addLight(lightPtr2);

        // transparent sphere
        Dielectric dielectricPtr = new Dielectric();
        dielectricPtr.setIorIn(2.42);		// diamond
        dielectricPtr.setIorOut(1.0);		// air
        dielectricPtr.setCfIn(Utility.WHITE);
        dielectricPtr.setCfOut(Utility.WHITE);

        Sphere spherePtr1 = new Sphere(new Point3D(0.0), 4.0);
        spherePtr1.setMaterial(dielectricPtr);
        w.addObject(spherePtr1);

        // a ring of spheres around the transparent sphere
        double scalingFactor = 2.0;  	// used to scale the radii of the ring and spheres
        double sphereRadius = 1.75 * scalingFactor;
        double ringRadius = 10.0 * scalingFactor;
        int numSpheres = 20;
        int deltaTheta = 20;

        RGBColor color1 = new RGBColor(1, 0.5, 0);          // orange
        RGBColor color2 = new RGBColor(0.0, 0.5, 0.25);	  // cyan

        // Phong material for top half of each sphere
        Phong phongPtr1 = new Phong();
        phongPtr1.setKa(0.2);
        phongPtr1.setKd(1.0);
        phongPtr1.setCd(color1);
        phongPtr1.setExp(100.0);
        phongPtr1.setKs(0.5);
        phongPtr1.setCs(1, 1, 0);

        // reflective material for bottom half of each sphere
        Reflective reflectivePtr2 = new Reflective();
        reflectivePtr2.setKa(0.2);
        reflectivePtr2.setKd(1.0);
        reflectivePtr2.setCd(color2);
        reflectivePtr2.setExp(100.0);
        reflectivePtr2.setKs(0.5);
        reflectivePtr2.setCs(1, 1, 0);
        reflectivePtr2.setKr(0.2);

        for (int j = 0; j < numSpheres; j++) {
            double xc = ringRadius * sin(j * deltaTheta * PI / 180.0);
            double zc = ringRadius * cos(j * deltaTheta * PI / 180.0);
            Point3D center = new Point3D(xc, 0, zc);

            ConvexPartSphere topHalfPtr = new ConvexPartSphere(center,
                    sphereRadius, 0, 360, 0, 90);
            topHalfPtr.setMaterial(phongPtr1);
            w.addObject(topHalfPtr);

            ConvexPartSphere bottomHalfPtr = new ConvexPartSphere(center,
                    sphereRadius, 0, 360, 90, 180);
            bottomHalfPtr.setMaterial(reflectivePtr2);
            w.addObject(bottomHalfPtr);
        }

        // reflective ground plane
        Reflective reflectivePtr = new Reflective();
        reflectivePtr.setKa(0.0);
        reflectivePtr.setKd(0.1);
        reflectivePtr.setCd(1, 1, 0);
        reflectivePtr.setKr(1.0);
        reflectivePtr.setCr(Utility.WHITE);

        Plane planePtr = new Plane(new Point3D(0, -4.5, 0), new Normal(0, 1, 0));
        planePtr.setMaterial(reflectivePtr);
        w.addObject(planePtr);
    }

}
