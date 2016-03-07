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
package com.matrixpeckham.raytracer.build.figures.ch30;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.primitives.OpenCylinder;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure14 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 30.14
// With the value of eps in Listing 30.2, there are minor shading anomalies
// particularly in Figure 30.14(c). Making eps smaller, eg eps = 0.0000001,
// fixes these, but re-introduces the shading problems in Figure 30.3.
        int numSamples = 16;

        w.vp.setHres(400);
        w.vp.setVres(400);
        w.vp.setSamples(numSamples);

        w.backgroundColor = new RGBColor(0.5);
        w.tracer = new RayCast(w);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(12, 15, 30);
        pinholePtr.setLookat(0, 0, 0);
        pinholePtr.setViewDistance(3500.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        Directional lightPtr = new Directional();
        lightPtr.setDirection(10, 10, 20);
        lightPtr.scaleRadiance(3.5);
        w.addLight(lightPtr);

        Checker3D checkerPtr = new Checker3D();
//        checkerPtr.setSize(0.6708);	// For Figure 30.14(a)
//	checkerPtr.setSize(0.416);	// For Figure 30.14(b)
        checkerPtr.setSize(0.3);		// For Figure 30.14(c)
        checkerPtr.setColor1(Utility.WHITE);
        checkerPtr.setColor2(Utility.BLACK);

        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.25);
        svMattePtr.setKd(0.6);
        svMattePtr.setCd(checkerPtr);

        double bottom = -0.9;
        double top = 0.9;
        double radius = 1.5;

        OpenCylinder cylinderPtr = new OpenCylinder(bottom, top, radius);
        cylinderPtr.setMaterial(svMattePtr);
        w.addObject(cylinderPtr);
    }

}
