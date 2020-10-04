/*
 * Copyright (C) 2015 William Matrix Peckham
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
package com.matrixpeckham.raytracer.build;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.compound.Grid;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.AmbientOccluder;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Vector3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class SphereGrid implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 27.19(b)
// You can implement the thick ring object as an exercise.
// See Exercise 19.24
        int numSamples = 256;

        int gridWid = 4;
        int gridHig = 4;
        int gridLen = 4;

        RGBColor sphereColor = new RGBColor(1, 0.5, 0);
        RGBColor gridColor1 = new RGBColor(1, 0.5, 0);
        RGBColor gridColor2 = new RGBColor(1, 0.5, 0);

        double sphereRad = 1;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setMaxDepth(3);
        w.vp.setSamples(numSamples);

        w.backgroundColor = new RGBColor(0.0, 0.3, 0.25);

        AmbientOccluder ambientPtr = new AmbientOccluder();
        ambientPtr.setSampler(new MultiJittered(numSamples));
        ambientPtr.setMinAmount(0);
        ambientPtr.scaleRadiance(1);
        w.setAmbient(ambientPtr);

        w.tracer = new Whitted(w);

        Pinhole pinholePtr = new Pinhole();
        Vector3D vec = new Vector3D(2 * gridWid, 4 * gridHig, 4 * gridLen);
        System.out.println(vec.length());
        pinholePtr.setEye(vec.x, vec.y, vec.z);
        pinholePtr.setLookat(0, gridHig * sphereRad, 0);
        pinholePtr.setViewDistance(500);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        PointLight lightPtr1 = new PointLight();
        lightPtr1.setLocation(vec.z, vec.y, vec.y);
        lightPtr1.scaleRadiance(2.75);
        lightPtr1.setShadows(true);
        w.addLight(lightPtr1);

        // Utility.RED sphere
        Matte phongPtr2 = new Matte();
        phongPtr2.setCd(sphereColor);
        phongPtr2.setKa(0.25);
        phongPtr2.setKd(0.75);
        Grid grid = new Grid();
        Sphere spherePtr = new Sphere(new Point3D(0, 0, 0), sphereRad);
        spherePtr.setMaterial(phongPtr2);
        //w.addObject(spherePtr);

        double skipDist = sphereRad * 2;
        double startY = sphereRad;
        double startX = -skipDist * (gridWid - 1) / 2.0;
        double startZ = -skipDist * (gridLen - 1) / 2.0;

        for (int x = 0; x < gridWid; x++) {
            for (int y = 0; y < gridHig; y++) {
                for (int z = 0; z < gridLen; z++) {
                    Instance inst = new Instance(spherePtr);
                    inst.translate(startX + x * skipDist, startY + y * skipDist,
                            startZ + z * skipDist);
                    inst.computeBoundingBox();
                    grid.addObject(inst);
                }
            }
        }
        grid.setupCells();
        w.addObject(grid);
        // plane with checker
        Checker3D checkerPtr = new Checker3D();
        checkerPtr.setSize(1.0);
        checkerPtr.setColor1(gridColor1);
        checkerPtr.setColor2(gridColor2);

        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.25);
        svMattePtr.setKd(0.75);
        svMattePtr.setCd(checkerPtr);

        Plane planePtr = new Plane(new Point3D(0.0), new Normal(0, 1, 0));
        planePtr.setMaterial(svMattePtr);
        w.addObject(planePtr);
    }

}
