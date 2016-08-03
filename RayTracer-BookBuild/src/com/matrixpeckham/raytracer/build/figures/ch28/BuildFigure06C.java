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
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.materials.Dielectric;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure06C implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 28.6 (c)
        int numSamples = 16;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setSamples(numSamples);
        w.vp.setMaxDepth(3);

        w.backgroundColor = Utility.WHITE;

        w.tracer = new Whitted(w);

        Ambient ambientPtr = new Ambient();
        ambientPtr.scaleRadiance(0.25);
        w.setAmbient(ambientPtr);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(0, 0, 50);
        pinholePtr.setLookat(new Point3D(0));
        pinholePtr.setViewDistance(4200.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        Dielectric dielectricPtr = new Dielectric();
        dielectricPtr.setKa(0.0);
        dielectricPtr.setKd(0.0);
        dielectricPtr.setKs(0.2);
        dielectricPtr.setExp(2000.0);
        dielectricPtr.setIorIn(1.5);
        dielectricPtr.setIorOut(1.0);
        dielectricPtr.setCfIn(Utility.WHITE);
        dielectricPtr.setCfOut(Utility.WHITE);

        Sphere spherePtr1 = new Sphere(new Point3D(0), 3);
        spherePtr1.setMaterial(dielectricPtr);
        w.addObject(spherePtr1);
    }

}
