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
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Dielectric;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
import com.matrixpeckham.raytracer.textures.procedural.PlaneChecker;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Mesh;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
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
public class BuildFigure13 implements BuildWorldFunction {

    @Override
    public void build(World w) {

// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.
// This builds the scene for Figure 28.14
        int numSamples = 16;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setSamples(numSamples);
        w.vp.setMaxDepth(9);

        w.backgroundColor = Utility.WHITE;

        w.tracer = new Whitted(w);

        Ambient ambientPtr = new Ambient();
        ambientPtr.scaleRadiance(0.25);
        w.setAmbient(ambientPtr);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(0, 0, 100);
        pinholePtr.setLookat(0, -1.5, 0);
        pinholePtr.setViewDistance(15000.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        Directional lightPtr = new Directional();
        lightPtr.setDirection(0, 0, 1);
        lightPtr.scaleRadiance(5.0);
        lightPtr.setShadows(false);
        w.addLight(lightPtr);

        // transparent horse
        Dielectric dielectricPtr = new Dielectric();
        dielectricPtr.setKa(0.0);
        dielectricPtr.setKd(0.0);
        dielectricPtr.setKs(0.2);
        dielectricPtr.setExp(2000.0);
        dielectricPtr.setIorIn(1.5);
        dielectricPtr.setIorOut(1.0);
        dielectricPtr.setCfIn(0.35, 0.65, 0.45);   // green
        dielectricPtr.setCfOut(Utility.WHITE);

        String fileName = "Horse97K.ply";
        String path
                = "resources/Models/";

        TriangleMesh horsePtr = new TriangleMesh(new Mesh());
        try {
//	horsePtr.reverseMeshNormals();				// you must use w for the 10K model
//	horsePtr.readFlatTriangles(fileName);

            horsePtr.readSmoothTriangles(Thread.currentThread().getContextClassLoader().getResourceAsStream(path + fileName));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure13.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
        horsePtr.setMaterial(dielectricPtr);
        horsePtr.setupCells();

        Instance bigHorsePtr = new Instance(horsePtr);
        bigHorsePtr.scale(5.0);
        bigHorsePtr.translate(0, -1.5, 0.0);
        w.addObject(bigHorsePtr);

        // vertical checker plane
        Checker3D checkerPtr = new Checker3D();
        checkerPtr.setSize(1.0);
        checkerPtr.setColor1(Utility.WHITE);
        checkerPtr.setColor2(0.75);

        PlaneChecker planeCheckerPtr = new PlaneChecker();
        planeCheckerPtr.setSize(1.5);
        planeCheckerPtr.setOutlineWidth(0.2);
        planeCheckerPtr.setColor1(Utility.WHITE);
        planeCheckerPtr.setColor2(Utility.WHITE);
        planeCheckerPtr.setOutlineColor(Utility.BLACK);

        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.25);
        svMattePtr.setKd(0.65);
        svMattePtr.setCd(planeCheckerPtr);

        Instance planePtr = new Instance(new Plane(new Point3D(0), new Normal(0,
                1, 0)));
        planePtr.setMaterial(svMattePtr);
        planePtr.rotateX(90);
        planePtr.translate(0, 0, -4);
        planePtr.setShadows(false);
        w.addObject(planePtr);
    }

}
