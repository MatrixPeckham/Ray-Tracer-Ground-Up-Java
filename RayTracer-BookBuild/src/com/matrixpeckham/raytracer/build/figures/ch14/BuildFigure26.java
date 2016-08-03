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
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure26 implements BuildWorldFunction {

    @Override
    public void build(World w) {

// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.
// This builds the scene for Figure 14.24
        int numSamples = 16;

        w.vp.setHres(600);
        w.vp.setVres(300);
        w.vp.setSamples(numSamples);

        w.tracer = new RayCast(w);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(0, 25, 100);
        pinholePtr.setLookat(new Point3D());
        pinholePtr.setViewDistance(6500);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        Directional lightPtr = new Directional();
        lightPtr.setDirection(200, 250, 300);
        lightPtr.setColor(1.0, 0.5, 0.0);  	// orange
        lightPtr.scaleRadiance(3.0);
        lightPtr.setShadows(true);    // see Chapter 16
        w.addLight(lightPtr);

        // four spheres centered on the x axis
        double radius = 1.0;
        double gap = 0.2;	 // gap between spheres

        Matte mattePtr1 = new Matte();
        mattePtr1.setKa(0.0);
        mattePtr1.setKd(0.75);
        mattePtr1.setCd(1, 0, 0);		// red

        Sphere spherePtr1 = new Sphere(new Point3D(-3.0 * radius - 1.5 * gap,
                0.0, 0.0), radius);
        spherePtr1.setMaterial(mattePtr1);
        w.addObject(spherePtr1);

        Matte mattePtr2 = new Matte();
        mattePtr2.setKa(0.0);
        mattePtr2.setKd(0.75);
        mattePtr2.setCd(1, 0.5, 0);		// orange

        Sphere spherePtr2 = new Sphere(
                new Point3D(-radius - 0.5 * gap, 0.0, 0.0), radius);
        spherePtr2.setMaterial(mattePtr2);
        w.addObject(spherePtr2);

        Matte mattePtr3 = new Matte();
        mattePtr3.setKa(0.0);
        mattePtr3.setKd(0.75);
        mattePtr3.setCd(1, 1, 0);		// yellow

        Sphere spherePtr3
                = new Sphere(new Point3D(radius + 0.5 * gap, 0.0, 0.0), radius);
        spherePtr3.setMaterial(mattePtr3);
        w.addObject(spherePtr3);

        Matte mattePtr4 = new Matte();
        mattePtr4.setKa(0.0);
        mattePtr4.setKd(0.75);
        mattePtr4.setCd(0, 1, 0);		// green

        Sphere spherePtr4 = new Sphere(
                new Point3D(3.0 * radius + 1.5 * gap, 0.0, 0.0), radius);
        spherePtr4.setMaterial(mattePtr4);
        w.addObject(spherePtr4);

        // ground plane
        Matte mattePtr5 = new Matte();
        mattePtr5.setKa(0.25);
        mattePtr5.setKd(0.5);
        mattePtr5.setCd(1.0);

        Plane planePtr = new Plane(new Point3D(0, -1, 0), new Normal(0, 1, 0));
        planePtr.setMaterial(mattePtr5);
        w.addObject(planePtr);

    }

}
