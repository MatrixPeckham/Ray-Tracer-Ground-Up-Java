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
import com.matrixpeckham.raytracer.geometricobjects.Instance;
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
public class BuildFigure49 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 28.49
        int numSamples = 16;

        w.vp.setHres(600);
        w.vp.setVres(350);
        w.vp.setSamples(numSamples);
        w.vp.setMaxDepth(6);

        w.backgroundColor = Utility.WHITE;

        Ambient ambientPtr = new Ambient();
        ambientPtr.scaleRadiance(0.5);
        w.setAmbient(ambientPtr);

        w.tracer = new Whitted(w);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(0, 0, 1000);
        pinholePtr.setLookat(0, 0, 0);
        pinholePtr.setViewDistance(35000.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        Dielectric dielectricPtr = new Dielectric();
        dielectricPtr.setIorIn(1.5);
        dielectricPtr.setIorOut(1.0);
        dielectricPtr.setCfIn(0.9, 0.7, 0);   // orange
        dielectricPtr.setCfOut(1.0);

        Instance spherePtr1 = new Instance(new Sphere());
        spherePtr1.setMaterial(dielectricPtr);
        spherePtr1.scale(4.0);
        spherePtr1.translate(-4.2, 0.0, 0.0);
        w.addObject(spherePtr1);

        Sphere spherePtr2 = new Sphere(new Point3D(4.2, 0, 0), 4);
        spherePtr2.setMaterial(dielectricPtr);
        w.addObject(spherePtr2);
    }

}
