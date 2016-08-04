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
package com.matrixpeckham.raytracer.build.figures.ch27;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Dielectric;
import com.matrixpeckham.raytracer.materials.SV_Matte;
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
public class BuildFigure23A implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 27.23(a)
// This uses the Dielectric material from Chapter 28
        int numSamples = 25;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setMaxDepth(3);
        w.vp.setSamples(numSamples);

        w.backgroundColor = new RGBColor(0.75);

        Ambient ambientPtr = new Ambient();
        ambientPtr.scaleRadiance(0.5);
        w.setAmbient(ambientPtr);

        w.tracer = new Whitted(w);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(0, 0, 20);
        pinholePtr.setLookat(new Point3D(0.0));
        pinholePtr.setViewDistance(4500);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        PointLight lightPtr1 = new PointLight();
        lightPtr1.setLocation(0, 50, 0);
        lightPtr1.scaleRadiance(4.5);
        lightPtr1.setShadows(false);
        w.addLight(lightPtr1);

        // transparent sphere
        Dielectric glassPtr = new Dielectric();
        glassPtr.setKs(0.15);
        glassPtr.setExp(100.0);
        glassPtr.setIorIn(1.5);
        glassPtr.setIorOut(1.0);
        glassPtr.setCfIn(0.47, 0.86, 0.74);
        glassPtr.setCfOut(Utility.WHITE);

        Sphere spherePtr = new Sphere();
        spherePtr.setMaterial(glassPtr);
        w.addObject(spherePtr);

        // plane with checker
        Checker3D checkerPtr = new Checker3D();
        checkerPtr.setSize(4.0);
        checkerPtr.setColor1(Utility.WHITE);
        checkerPtr.setColor2(Utility.BLACK);

        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.25);
        svMattePtr.setKd(0.65);
        svMattePtr.setCd(checkerPtr);

        Plane planePtr = new Plane(new Point3D(0, -5, 0), new Normal(0, 1, 0));
        planePtr.setMaterial(svMattePtr);
        w.addObject(planePtr);

    }

}
