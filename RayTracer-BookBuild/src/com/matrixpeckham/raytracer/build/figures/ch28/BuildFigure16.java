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
import com.matrixpeckham.raytracer.geometricobjects.primitives.Rectangle;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.AreaLight;
import com.matrixpeckham.raytracer.materials.Dielectric;
import com.matrixpeckham.raytracer.materials.Emissive;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.textures.procedural.PlaneChecker;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Mesh;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
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
public class BuildFigure16 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 28.16
        int numSamples = 100;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setSamples(numSamples);
        w.vp.setMaxDepth(9);

        w.backgroundColor = new RGBColor(0.5);

        w.tracer = new Whitted(w);

        Ambient ambientPtr = new Ambient();
        w.setAmbient(ambientPtr);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(-4, 4, 20);
        pinholePtr.setLookat(-0.2, -0.5, 0);
        pinholePtr.setViewDistance(6000.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        // rectangular area light
        Emissive emissivePtr = new Emissive();
        emissivePtr.scaleRadiance(40.0);
        emissivePtr.setCe(Utility.WHITE);

        Point3D center = new Point3D(5, 6, 10);
        double width = 4.0;
        double height = 5.0;

        Point3D p0
                = new Point3D(-0.5 * width, center.y - 0.5 * height, center.z);
        Vector3D a = new Vector3D(width, 0.0, 0.0);
        Vector3D b = new Vector3D(0.0, height, 0.0);
        Normal normal = new Normal(0, 0, -1);

        Rectangle rectanglePtr = new Rectangle(p0, a, b, normal);
        rectanglePtr.setMaterial(emissivePtr);
        rectanglePtr.setSampler(new MultiJittered(numSamples));
        rectanglePtr.setShadows(false);
        w.addObject(rectanglePtr);

        AreaLight areaLightPtr = new AreaLight();
        areaLightPtr.setObject(rectanglePtr);
        areaLightPtr.setShadows(true);
        w.addLight(areaLightPtr);

        // transparent bunny
        Dielectric dielectricPtr = new Dielectric();
        dielectricPtr.setKa(0.0);
        dielectricPtr.setKd(0.0);
        dielectricPtr.setKs(0.2);
        dielectricPtr.setExp(2000.0);
        dielectricPtr.setIorIn(1.5);
        dielectricPtr.setIorOut(1.0);
        dielectricPtr.setCfIn(0.75, 0.45, 0);   // orange
        dielectricPtr.setCfOut(Utility.WHITE);

        String fileName = "Bunny4K.ply";
        String path
                = "/Models/Stanford Bunny/";

        TriangleMesh bunnyPtr = new TriangleMesh(new Mesh());
        try {
//	bunnyPtr.reverseMeshNormals();				// you must use w for the 10K model
//	bunnyPtr.readFlatTriangles(fileName);

            bunnyPtr.readSmoothTriangles(getClass().getClassLoader().getResourceAsStream(path + fileName));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure12A.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
        bunnyPtr.setMaterial(dielectricPtr);
        bunnyPtr.setupCells();

        Instance bigBunnyPtr = new Instance(bunnyPtr);
        bigBunnyPtr.scale(10.0);
        bigBunnyPtr.translate(0, -1.5, 0.0);
        w.addObject(bigBunnyPtr);

        PlaneChecker planeCheckerPtr = new PlaneChecker();
        planeCheckerPtr.setSize(0.25);
        planeCheckerPtr.setOutlineWidth(0.02);
        planeCheckerPtr.setColor1(new RGBColor(0.75));
        planeCheckerPtr.setColor2(new RGBColor(0.75));
        planeCheckerPtr.setOutlineColor(Utility.BLACK);

        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.15);
        svMattePtr.setKd(0.65);
        svMattePtr.setCd(planeCheckerPtr);

        // ground plane
        Plane planePtr1 = new Plane(new Point3D(0, -1.175, 0), new Normal(0, 1,
                0));
        planePtr1.setMaterial(svMattePtr);
        planePtr1.setShadows(false);
        w.addObject(planePtr1);

        // back plane
        Instance planePtr2 = new Instance(new Plane(new Point3D(0),
                new Normal(0, 1, 0)));
        planePtr2.setMaterial(svMattePtr);
        planePtr2.rotateX(90);
        planePtr2.translate(0, 0, -2);
        planePtr2.setShadows(false);
        w.addObject(planePtr2);

    }

}
