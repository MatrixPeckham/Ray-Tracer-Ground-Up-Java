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
import com.matrixpeckham.raytracer.geometricobjects.beveledobjects.BeveledBoxShell;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Rectangle;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Dielectric;
import com.matrixpeckham.raytracer.materials.Matte;
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
public class BuildFigure20 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 27.20
// The class BeveledBoxShell is like the BeveledBox class, except that it uses part
// spheres and cylinders, instead of the whole objects.
// You will have to implement w an exercise.
// The class BeveledBox is in the code download.
// The class ConvexPartSphere is in the code download.
// You will also have to write the ConvexPartCylinder class as an exercise.
// This scene uses the Dielectric material from Chapter 28, but you can use the
// Transparent material from Chapter 27.
// Render w with different values of maxDepth, starting with 1
// Also render it with one ray per pixel, and from different camera locations
        int numSamples = 1;

        w.vp.setHres(300);
        w.vp.setVres(300);
        w.vp.setMaxDepth(10);
        w.vp.setSamples(numSamples);

        Ambient ambientPtr = new Ambient();
        ambientPtr.scaleRadiance(0.25);
        w.setAmbient(ambientPtr);

        w.backgroundColor = new RGBColor(0.0, 0.3, 0.25);

        w.tracer = new Whitted(w);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(10, 7.5, 20);
        pinholePtr.setLookat(new Point3D(0.0));
        pinholePtr.setViewDistance(2000.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        PointLight lightPtr = new PointLight();
        lightPtr.setLocation(20, 20, 15);
        lightPtr.scaleRadiance(3.25);
        lightPtr.setShadows(true);
        w.addLight(lightPtr);

        Matte matte = new Matte();
        matte.setCd(0, 0, 1);
        matte.setKa(0.1);
        matte.setKd(0.9);

        // beveled box shell
        Dielectric glassPtr = new Dielectric();
        glassPtr.setKs(0.5);
        glassPtr.setExp(10000.0);
        glassPtr.setIorIn(1.5);
        glassPtr.setIorOut(1.0);
        glassPtr.setCfIn(1.0, 1.0, 0.5);

        Point3D p0 = new Point3D(-1.0);
        Point3D p1 = new Point3D(1.0);
        double bevelRadius = 0.5;

        BeveledBoxShell boxPtr = new BeveledBoxShell(p0, p1, bevelRadius);
        boxPtr.setMaterial(glassPtr);
        //boxPtr.setMaterial(matte);
        w.addObject(boxPtr);

        // rectangle
        Checker3D checkerPtr = new Checker3D();
        checkerPtr.setSize(1.0);
        checkerPtr.setColor1(Utility.WHITE);
        checkerPtr.setColor2(0.5);

        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.8);
        svMattePtr.setKd(0.85);
        svMattePtr.setCd(checkerPtr);

        p0 = new Point3D(-4.0, -1.01, -4.0);
        Vector3D a = new Vector3D(0, 0, 8);
        Vector3D b = new Vector3D(8, 0, 0);

        Rectangle rectanglePtr = new Rectangle(p0, a, b);
        rectanglePtr.setMaterial(svMattePtr);
        w.addObject(rectanglePtr);

    }

}
