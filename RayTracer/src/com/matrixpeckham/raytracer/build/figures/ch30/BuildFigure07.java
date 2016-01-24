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
public class BuildFigure07 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 30.7
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

        TInstance translatedCheckerPtr = new TInstance(checkerPtr);
        translatedCheckerPtr.translate(0.5, 0.5, 0.0);

        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.5);
        svMattePtr.setKd(0.75);
        svMattePtr.setCd(translatedCheckerPtr);

        double radius;

	// sphere 1	
        radius = 3.0;
        Instance spherePtr1 = new Instance(new Sphere(new Point3D(
                0.0), radius));
        spherePtr1.setMaterial(svMattePtr);
        spherePtr1.translate(-6.25, 0.0, 0.0);
        w.addObject(spherePtr1);

	// sphere 2
        radius = 2.25;
        Instance spherePtr2 = new Instance(new Sphere(new Point3D(
                0.0), radius));
        spherePtr2.setMaterial(svMattePtr);
        spherePtr2.translate(-1.0, 0.0, 0.0);
        w.addObject(spherePtr2);

	// sphere 3
        radius = 1.75;
        Instance spherePtr3 = new Instance(new Sphere(new Point3D(
                0.0), radius));
        spherePtr3.setMaterial(svMattePtr);
        spherePtr3.translate(3.0, 0.0, 0.0);
        w.addObject(spherePtr3);

	// sphere 4
        radius = 1.0;
        Instance spherePtr4 = new Instance(new Sphere(new Point3D(
                0.0), radius));
        spherePtr4.setMaterial(svMattePtr);
        spherePtr4.translate(5.75, 0.0, 0.0);
        w.addObject(spherePtr4);

	// sphere 5
        radius = 0.75;
        Instance spherePtr5 = new Instance(new Sphere(new Point3D(
                0.0), radius));
        spherePtr5.setMaterial(svMattePtr);
        spherePtr5.translate(7.5, 0.0, 0.0);
        w.addObject(spherePtr5);

	// sphere 6
        radius = 0.5;
        Instance spherePtr6 = new Instance(new Sphere(new Point3D(
                0.0), radius));
        spherePtr6.setMaterial(svMattePtr);
        spherePtr6.translate(8.75, 0.0, 0.0);
        w.addObject(spherePtr6);
    }

}
