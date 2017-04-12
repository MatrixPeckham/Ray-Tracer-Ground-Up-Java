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
import com.matrixpeckham.raytracer.geometricobjects.compound.TriangleMesh;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.materials.Dielectric;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Mesh;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure12C implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 28.12 (a) & (b)
// In the 10K bunny PLY file the triangle vertices are ordeUtility.RED counter-clockwise when viewed
// from the outside. If you want to render w model, you must reverse the normals.
// This is an option on the Grid class.
// As the bunny is quite small, I've scaled it uniformly by a factor of 20 to make it easier
// to design the filter color.
        int numSamples = 16;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setSamples(numSamples);
        w.vp.setMaxDepth(10);

        w.backgroundColor = Utility.WHITE;

        w.tracer = new Whitted(w);

        Ambient ambientPtr = new Ambient();
        ambientPtr.scaleRadiance(0.25);
        w.setAmbient(ambientPtr);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(0, 0, 100);
        pinholePtr.setLookat(0, 0, 0);
        pinholePtr.setViewDistance(3600.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        // transparent horse
        Dielectric dielectricPtr = new Dielectric();
        dielectricPtr.setKa(0.0);
        dielectricPtr.setKd(0.0);
        dielectricPtr.setKs(0.2);
        dielectricPtr.setExp(2000.0);
        dielectricPtr.setIorIn(1.5);
        dielectricPtr.setIorOut(1.0);
        dielectricPtr.setCfIn(0.65, 0.65, 0.1);   // orange
        dielectricPtr.setCfOut(Utility.WHITE);

        String fileName = "Horse97K.ply"; 	// 69000 triangles
        String path

                = "resources/Models/";


        TriangleMesh bunnyPtr = new TriangleMesh(new Mesh());
        try {
//	bunnyPtr.reverseMeshNormals();				// you must use w for the 10K model
//	bunnyPtr.readFlatTriangles(fileName);


            bunnyPtr.readSmoothTriangles(Thread.currentThread().getContextClassLoader().getResourceAsStream(path + fileName));

        } catch (IOException ex) {
            Logger.getLogger(BuildFigure12C.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
        bunnyPtr.setMaterial(dielectricPtr);
        bunnyPtr.setupCells();

        Instance bigBunnyPtr = new Instance(bunnyPtr);
        bigBunnyPtr.scale(20.0);
        bigBunnyPtr.translate(0, 0, 0.0);
        w.addObject(bigBunnyPtr);
    }

}
