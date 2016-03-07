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
import com.matrixpeckham.raytracer.geometricobjects.primitives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.materials.Dielectric;
import com.matrixpeckham.raytracer.materials.Emissive;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure25 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 28.25
        int numSamples = 1;  // w figure does not need antialiasing

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setSamples(numSamples);
        w.vp.setMaxDepth(10);

        w.backgroundColor = Utility.BLACK;

        w.tracer = new Whitted(w);

        Ambient ambientPtr = new Ambient();
        ambientPtr.scaleRadiance(0.25);
        w.setAmbient(ambientPtr);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(0, 1000, 0);
        pinholePtr.setLookat(new Point3D(0));
        pinholePtr.setViewDistance(200000.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        // transparent box
        Dielectric dielectricPtr = new Dielectric();
        dielectricPtr.setIorIn(1.5);
        dielectricPtr.setIorOut(1.0);
        dielectricPtr.setCfIn(Utility.WHITE);
        dielectricPtr.setCfOut(Utility.WHITE);

        Point3D p0 = new Point3D(-1.0, 0.0, -1.0);
        Point3D p1 = new Point3D(1.0, 0.1, 1.0);

        Box boxPtr1 = new Box(p0, p1);
        boxPtr1.setMaterial(dielectricPtr);
        w.addObject(boxPtr1);

        // plane
        Emissive emissivePtr = new Emissive();
        emissivePtr.setCe(Utility.WHITE);
        emissivePtr.scaleRadiance(1.0);

        Plane planePtr = new Plane(new Point3D(0.0, -4.0, 0.0), new Normal(0, 1,
                0));
        planePtr.setMaterial(emissivePtr);
        w.addObject(planePtr);
    }

}
