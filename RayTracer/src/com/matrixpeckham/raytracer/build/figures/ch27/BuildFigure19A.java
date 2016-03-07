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
package com.matrixpeckham.raytracer.build.figures.ch27;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.compound.SolidCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.materials.Transparent;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
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
public class BuildFigure19A implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 27.19(a).
// You can implement the solid cylinder as an exercise.
// See Exercise 19.22
        int numSamples = 16;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setMaxDepth(10);
        w.vp.setSamples(numSamples);

        w.backgroundColor = new RGBColor(0.0, 0.3, 0.25);

        Ambient ambientPtr = new Ambient();
        ambientPtr.scaleRadiance(0.25);
        w.setAmbient(ambientPtr);

        w.tracer = new Whitted(w);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(0, 1, 4);
        pinholePtr.setLookat(0.25, 1.25, 0.0);
        pinholePtr.setViewDistance(500.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        PointLight lightPtr1 = new PointLight();
        lightPtr1.setLocation(20, 20, 5);
        lightPtr1.scaleRadiance(2.75);
        lightPtr1.setShadows(true);
        w.addLight(lightPtr1);

        Directional lightPtr2 = new Directional();
        lightPtr2.setDirection(-1, 0, 0);
        lightPtr2.scaleRadiance(3.0);
        lightPtr2.setShadows(true);
        w.addLight(lightPtr2);

        // solid cylinder
        Transparent glassPtr = new Transparent();
        glassPtr.setKs(0.15);
        glassPtr.setExp(2000.0);
        glassPtr.setIor(1.5);
        glassPtr.setKr(0.1);
        glassPtr.setKt(0.9);

        double bottom = 0.0;
        double top = 2.25;
        double radius = 1.5;

        SolidCylinder cylinderPtr = new SolidCylinder(bottom, top, radius);
        cylinderPtr.setMaterial(glassPtr);
        w.addObject(cylinderPtr);

        // Utility.RED sphere
        Reflective phongPtr = new Reflective();
        phongPtr.setCd(Utility.RED);
        phongPtr.setKa(0.3);
        phongPtr.setKd(0.9);
        phongPtr.setKs(0.25);
        phongPtr.setExp(1000);
        phongPtr.setKr(0.25);

        Sphere spherePtr2 = new Sphere(new Point3D(2.0, 1.5, -2.5), 1.5);
        spherePtr2.setMaterial(phongPtr);
        w.addObject(spherePtr2);

        // plane with checker
        Checker3D checkerPtr = new Checker3D();
        checkerPtr.setSize(1.0);
        checkerPtr.setColor1(Utility.WHITE);
        checkerPtr.setColor2(0.5);

        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.8);
        svMattePtr.setKd(0.85);
        svMattePtr.setCd(checkerPtr);

        Plane planePtr = new Plane(new Point3D(0.0), new Normal(0, 1, 0));
        planePtr.setMaterial(svMattePtr);
        w.addObject(planePtr);
    }

}
