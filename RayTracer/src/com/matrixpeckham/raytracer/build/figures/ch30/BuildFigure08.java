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
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.TInstance;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure08 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 30.8
        int numSamples = 16;

        w.vp.setHres(904);
        w.vp.setVres(300);
        w.vp.setSamples(numSamples);

        w.tracer = new RayCast(w);
        w.backgroundColor = new RGBColor(0.75);

        Ambient ambientPtr = new Ambient();
        ambientPtr.scaleRadiance(0.5);
        w.setAmbient(ambientPtr);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(0, 0, 200);
        pinholePtr.setLookat(new Point3D(0.0));
        pinholePtr.setViewDistance(9600.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        PointLight lightPtr = new PointLight();
        lightPtr.setLocation(50, 100, 100);
        lightPtr.scaleRadiance(4.0);
        lightPtr.setShadows(false);
        w.addLight(lightPtr);

	// the spheres
        Checker3D checkerPtr = new Checker3D();
        checkerPtr.setSize(1.0);
        checkerPtr.setColor1(Utility.BLACK);
        checkerPtr.setColor2(Utility.WHITE);

        double radius;

	// sphere 1
        TInstance transformedCheckerPtr1 = new TInstance(checkerPtr);
        transformedCheckerPtr1.translate(0.5, 0.5, 0.0);

        SV_Matte svMattePtr1 = new SV_Matte();
        svMattePtr1.setKa(0.5);
        svMattePtr1.setKd(0.75);
        svMattePtr1.setCd(transformedCheckerPtr1);

        radius = 3.0;
        Instance spherePtr1 = new Instance(new Sphere(new Point3D(
                0.0), radius));
        spherePtr1.setMaterial(svMattePtr1);
        spherePtr1.translate(-6.25, 0.0, 0.0);
        w.addObject(spherePtr1);

	// sphere 2
        TInstance transformedCheckerPtr2 = new TInstance(checkerPtr);
        transformedCheckerPtr2.scale(0.75);
        transformedCheckerPtr2.translate(0.375, 0.375, 0.0);

        SV_Matte svMattePtr2 = new SV_Matte();
        svMattePtr2.setKa(0.5);
        svMattePtr2.setKd(0.75);
        svMattePtr2.setCd(transformedCheckerPtr2);

        radius = 2.25;
        Instance spherePtr2 = new Instance(new Sphere(new Point3D(
                0.0), radius));
        spherePtr2.setMaterial(svMattePtr2);
        spherePtr2.translate(-1.0, 0.0, 0.0);
        w.addObject(spherePtr2);

	// sphere 3
        TInstance transformedCheckerPtr3 = new TInstance(checkerPtr);
        transformedCheckerPtr3.scale(0.5833333);
        transformedCheckerPtr3.translate(0.29166, 0.29166, 0.0);

        SV_Matte svMattePtr3 = new SV_Matte();
        svMattePtr3.setKa(0.5);
        svMattePtr3.setKd(0.75);
        svMattePtr3.setCd(transformedCheckerPtr3);

        radius = 1.75;
        Instance spherePtr3 = new Instance(new Sphere(new Point3D(
                0.0), radius));
        spherePtr3.setMaterial(svMattePtr3);
        spherePtr3.translate(3.0, 0.0, 0.0);
        w.addObject(spherePtr3);

	// sphere 4
        TInstance transformedCheckerPtr4 = new TInstance(checkerPtr);
        transformedCheckerPtr4.scale(0.3333333);
        transformedCheckerPtr4.translate(0.166666, 0.166666, 0.0);

        SV_Matte svMattePtr4 = new SV_Matte();
        svMattePtr4.setKa(0.5);
        svMattePtr4.setKd(0.75);
        svMattePtr4.setCd(transformedCheckerPtr4);

        radius = 1.0;
        Instance spherePtr4 = new Instance(new Sphere(new Point3D(
                0.0), radius));
        spherePtr4.setMaterial(svMattePtr4);
        spherePtr4.translate(5.75, 0.0, 0.0);
        w.addObject(spherePtr4);

	// sphere 5
        TInstance transformedCheckerPtr5 = new TInstance(checkerPtr);
        transformedCheckerPtr5.scale(0.25);
        transformedCheckerPtr5.translate(0.125, 0.125, 0.0);

        SV_Matte svMattePtr5 = new SV_Matte();
        svMattePtr5.setKa(0.5);
        svMattePtr5.setKd(0.75);
        svMattePtr5.setCd(transformedCheckerPtr5);

        radius = 0.75;
        Instance spherePtr5 = new Instance(new Sphere(new Point3D(
                0.0), radius));
        spherePtr5.setMaterial(svMattePtr5);
        spherePtr5.translate(7.5, 0.0, 0.0);
        w.addObject(spherePtr5);

	// sphere 6
        TInstance transformedCheckerPtr6 = new TInstance(checkerPtr);
        transformedCheckerPtr6.scale(0.166666);
        transformedCheckerPtr6.translate(0.083333, 0.083333, 0.0);

        SV_Matte svMattePtr6 = new SV_Matte();
        svMattePtr6.setKa(0.5);
        svMattePtr6.setKd(0.75);
        svMattePtr6.setCd(transformedCheckerPtr6);

        radius = 0.5;
        Instance spherePtr6 = new Instance(new Sphere(new Point3D(
                0.0), radius));
        spherePtr6.setMaterial(svMattePtr6);
        spherePtr6.translate(8.75, 0.0, 0.0);
        w.addObject(spherePtr6);
    }

}
