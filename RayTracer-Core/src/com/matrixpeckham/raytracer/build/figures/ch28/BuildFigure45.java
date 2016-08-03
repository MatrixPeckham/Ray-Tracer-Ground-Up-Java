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
import com.matrixpeckham.raytracer.geometricobjects.primitives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Dielectric;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure45 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 28.45
        int numSamples = 16;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setSamples(numSamples);
        w.vp.setMaxDepth(4);

        w.tracer = new Whitted(w);

        w.backgroundColor = new RGBColor(0.75);

        Ambient ambientPtr = new Ambient();
        ambientPtr.scaleRadiance(1.0);
        w.setAmbient(ambientPtr);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(10, 12, 20);
        pinholePtr.setLookat(-3.75, 3, 0);
        pinholePtr.setViewDistance(1500.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        PointLight lightPtr1 = new PointLight();
        lightPtr1.setLocation(20, 25, -20);
        lightPtr1.scaleRadiance(3.0);
        lightPtr1.setShadows(true);
        w.addLight(lightPtr1);

        double c = 1.75;  // w allows us to adjust the filter color without changing the hue
        RGBColor glassColor = new RGBColor(0.27 * c, 0.49 * c, 0.42 * c);

        Dielectric glassPtr = new Dielectric();
        glassPtr.setIorIn(1.50);		// glass
        glassPtr.setIorOut(1.0);		// air
        glassPtr.setCfIn(glassColor);
        glassPtr.setCfOut(Utility.WHITE);

        double thickness = 0.25;
        double height = 4.0;
        double delta = 1.0;			// length change of each box

        int numBoxes = 10;
        double xMin = -10.0;		// where the boxes start in the x direction
        double gap = 0.5;   		// gap between the boxes

        for (int j = 0; j < numBoxes; j++) {
            double length = thickness + j * delta;
            Point3D p0 = new Point3D(xMin + j * (thickness + gap), 0.0, -length
            );
            Point3D p1 = new Point3D(xMin + j * (thickness + gap) + thickness,
                    height, 0.0);

            Box boxPtr = new Box(p0, p1);
            boxPtr.setMaterial(glassPtr);
            w.addObject(boxPtr);
        }

        // plane
        Matte mattePtr = new Matte();
        mattePtr.setKa(0.5);
        mattePtr.setKd(0.65);
        mattePtr.setCd(0.75);

        Plane planePtr = new Plane(new Point3D(0.0), new Normal(0, 1, 0));
        planePtr.setMaterial(mattePtr);
        w.addObject(planePtr);
    }

}
