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
import com.matrixpeckham.raytracer.geometricobjects.compound.SolidCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.Phong;
import com.matrixpeckham.raytracer.tracers.RayCast;
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
public class BuildFigure08 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 15.8
        int numSamples = 16;

        w.vp.setHres(600);
        w.vp.setVres(400);
        w.vp.setSamples(numSamples);

        w.tracer = new RayCast(w);

        Ambient ambientPtr = new Ambient();
        ambientPtr.scaleRadiance(0.5);
        w.setAmbient(ambientPtr);

        double a = 0.75;
        w.backgroundColor = new RGBColor(0.0, 0.3 * a, 0.25 * a);  // torquise

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(7.5, 4, 10);
        pinholePtr.setLookat(-1, 3.7, 0);
        pinholePtr.setViewDistance(340);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        Directional lightPtr1 = new Directional();	// for Figure 15.8(a)
        lightPtr1.setDirection(15, 15, 2.5);
        lightPtr1.scaleRadiance(2.0);
	//w.addLight(lightPtr1);

        PointLight lightPtr2 = new PointLight();	// for Figure 15.8(b)
        lightPtr2.setLocation(15, 15, 2.5);
        lightPtr2.scaleRadiance(2.0);
        w.addLight(lightPtr2);

        Phong phongPtr1 = new Phong();
        phongPtr1.setKa(0.25);
        phongPtr1.setKd(0.75);
        phongPtr1.setCd(0.75, 0.75, 0);  	// dark yellow
        phongPtr1.setKs(0.25);
        phongPtr1.setExp(50);

        Phong phongPtr2 = new Phong();
        phongPtr2.setKa(0.45);
        phongPtr2.setKd(0.75);
        phongPtr2.setCd(0.75, 0.25, 0);   	// orange
        phongPtr2.setKs(0.25);
        phongPtr2.setExp(500);

        Phong phongPtr3 = new Phong();
        phongPtr3.setKa(0.4);
        phongPtr3.setKd(0.75);
        phongPtr3.setCd(1, 0.5, 1);			// mauve
        phongPtr3.setKs(0.25);
        phongPtr3.setExp(4);

        Phong phongPtr4 = new Phong();
        phongPtr4.setKa(0.15);
        phongPtr4.setKd(0.5);
        phongPtr4.setCd(0.75, 1.0, 0.75);   	// light green
        phongPtr4.setKs(0.5);
        phongPtr4.setExp(3);

        Matte mattePtr5 = new Matte();
        mattePtr5.setKa(0.20);
        mattePtr5.setKd(0.97);
        mattePtr5.setCd(Utility.WHITE);

	// spheres
        Sphere spherePtr1 = new Sphere(new Point3D(3.85, 2.3, -2.55), 2.3);
        spherePtr1.setMaterial(phongPtr1);
        w.addObject(spherePtr1);

        Sphere spherePtr2 = new Sphere(new Point3D(-0.7, 1, 4.2), 2);
        spherePtr2.setMaterial(phongPtr2);
        w.addObject(spherePtr2);

	// cylinder 
        double bottom = 0.0;
        double top = 8.5;
        double radius = 2.2;
        SolidCylinder cylinderPtr = new SolidCylinder(bottom, top, radius);
        cylinderPtr.setMaterial(phongPtr3);
        w.addObject(cylinderPtr);

	// box
        Box boxPtr = new Box(new Point3D(-3.5, 0, -11),
                new Point3D(-2.5, 6, 6.5));
        boxPtr.setMaterial(phongPtr4);
        w.addObject(boxPtr);

	// ground plane
        Plane planePtr = new Plane(new Point3D(0), new Normal(0, 1, 0));
        planePtr.setMaterial(mattePtr5);
        w.addObject(planePtr);

    }

}
