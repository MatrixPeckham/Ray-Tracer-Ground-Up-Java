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

import com.matrixpeckham.raytracer.cameras.FishEye;
import com.matrixpeckham.raytracer.geometricobjects.compound.SolidCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Dielectric;
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
public class BuildFigure07 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 28.7
        int numSamples = 25;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setMaxDepth(3);
        w.vp.setSamples(numSamples);

        w.backgroundColor = new RGBColor(0.5, 0.6666, 0.5);  // light green

        w.tracer = new Whitted(w);

        FishEye fisheyePtr = new FishEye();
        fisheyePtr.setEye(0.0, 0.1, 0.0);
        fisheyePtr.setLookat(0, 10, 0);
        fisheyePtr.setRectangular(true);
        fisheyePtr.setFov(265.0);
        fisheyePtr.computeUVW();
        w.setCamera(fisheyePtr);

        Directional lightPtr1 = new Directional();
        lightPtr1.setDirection(0, 1, 0);
        lightPtr1.scaleRadiance(5.0);
        lightPtr1.setShadows(false);
        w.addLight(lightPtr1);

        Transparent glassPtr = new Transparent();
        glassPtr.setKs(0.0);
        glassPtr.setExp(1.0);
        glassPtr.setIor(1.5);
        glassPtr.setKr(0.1);
        glassPtr.setKt(0.9);

        Dielectric dielectricPtr = new Dielectric();
        dielectricPtr.setCd(0);
        dielectricPtr.setKa(0.0);
        dielectricPtr.setKd(0.0);
        dielectricPtr.setKs(0.0);
        dielectricPtr.setExp(1.0);
        dielectricPtr.setIorIn(1.5);
        dielectricPtr.setIorOut(1.0);

        double radius = 250.0;
        double bottom = 10.0;
        double top = 10.1;
        SolidCylinder cylinderPtr = new SolidCylinder(bottom, top, radius);
//        cylinderPtr.setMaterial(glassPtr);				// for Figure 28.7(a)
        cylinderPtr.setMaterial(dielectricPtr);			// for Figure 28.7(b)
        w.addObject(cylinderPtr);

        // plane with checker
        Checker3D checkerPtr = new Checker3D();
        checkerPtr.setSize(10.0);
        checkerPtr.setColor1(Utility.BLACK);
        checkerPtr.setColor2(1.0, 0.7, 0.2);  // orange

        SV_Matte svMattePtr5 = new SV_Matte();
        svMattePtr5.setKa(0.25);
        svMattePtr5.setKd(0.5);
        svMattePtr5.setCd(checkerPtr);

        Plane planePtr2 = new Plane(new Point3D(0.0), new Normal(0, 1, 0));
        planePtr2.setMaterial(svMattePtr5);
        planePtr2.setShadows(false);
        w.addObject(planePtr2);
    }

}
