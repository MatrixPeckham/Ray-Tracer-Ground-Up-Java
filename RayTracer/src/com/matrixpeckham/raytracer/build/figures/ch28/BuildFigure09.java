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
import com.matrixpeckham.raytracer.geometricobjects.primitives.Rectangle;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Dielectric;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure09 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 28.9
        int numSamples = 16;

        w.vp.setHres(600);
        w.vp.setVres(400);
        w.vp.setSamples(numSamples);
        w.vp.setMaxDepth(6);

        w.backgroundColor = Utility.WHITE;

        w.tracer = new Whitted(w);

        Ambient ambientPtr = new Ambient();
        ambientPtr.scaleRadiance(0.25);
        w.setAmbient(ambientPtr);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(2.8125, 0, 1000);
        pinholePtr.setLookat(2.8125, 0, 0);
        pinholePtr.setViewDistance(50000.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        Dielectric dielectricPtr = new Dielectric();
        dielectricPtr.setKa(0.0);
        dielectricPtr.setKd(0.0);
        dielectricPtr.setKs(0.0);
        dielectricPtr.setExp(2000);
        dielectricPtr.setIorIn(1.5);
        dielectricPtr.setIorOut(1.0);
        dielectricPtr.setCfIn(0.65, 0.45, 0);   // orange
        dielectricPtr.setCfOut(1.0);

	// a row of spheres
        Sphere spherePtr1 = new Sphere(new Point3D(0), 3.0);
        spherePtr1.setMaterial(dielectricPtr);
        w.addObject(spherePtr1);

        Sphere spherePtr2 = new Sphere(new Point3D(4.5, 0, 0), 1.5);
        spherePtr2.setMaterial(dielectricPtr);
        w.addObject(spherePtr2);

        Sphere spherePtr3 = new Sphere(new Point3D(6.75, 0, 0), 0.75);
        spherePtr3.setMaterial(dielectricPtr);
        w.addObject(spherePtr3);

        Sphere spherePtr4 = new Sphere(new Point3D(7.875, 0, 0), 0.375);
        spherePtr4.setMaterial(dielectricPtr);
        w.addObject(spherePtr4);

        Sphere spherePtr5 = new Sphere(new Point3D(8.4375, 0, 0), 0.1875);
        spherePtr5.setMaterial(dielectricPtr);
        w.addObject(spherePtr5);
    }

}
