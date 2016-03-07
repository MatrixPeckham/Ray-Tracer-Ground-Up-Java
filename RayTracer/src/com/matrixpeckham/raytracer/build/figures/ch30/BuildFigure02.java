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
import com.matrixpeckham.raytracer.geometricobjects.compound.SolidCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primitives.OpenCone;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure02 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 30.2
        int numSamples = 16;

        w.vp.setHres(880);
        w.vp.setVres(300);
        w.vp.setSamples(numSamples);

        w.tracer = new RayCast(w);
        w.backgroundColor = new RGBColor(0.5);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(100, 100, 300);
        pinholePtr.setLookat(-0.2, 1.0, 0);
        pinholePtr.setViewDistance(12000.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        Directional lightPtr = new Directional();
        lightPtr.setDirection(0, 0, 1);
        lightPtr.scaleRadiance(1.5);
        w.addLight(lightPtr);

        // sphere
        Checker3D checkerPt1 = new Checker3D();
        checkerPt1.setSize(1.0);
        checkerPt1.setColor1(Utility.BLACK);
        checkerPt1.setColor2(Utility.WHITE);

        SV_Matte svMattePtr1 = new SV_Matte();
        svMattePtr1.setKa(0.75);
        svMattePtr1.setKd(0.75);
        svMattePtr1.setCd(checkerPt1);

        Sphere spherePtr = new Sphere(new Point3D(-9.5, -1, 0),
                2.5);
        spherePtr.setMaterial(svMattePtr1);
        w.addObject(spherePtr);

        // cylinder
        Checker3D checkerPtr2 = new Checker3D();
        checkerPtr2.setSize(1.0);
        checkerPtr2.setColor1(Utility.BLACK);
        checkerPtr2.setColor2(Utility.WHITE);

        SV_Matte svMattePtr2 = new SV_Matte();
        svMattePtr2.setKa(0.75);
        svMattePtr2.setKd(0.75);
        svMattePtr2.setCd(checkerPtr2);

        Instance cylinderPtr = new Instance(new SolidCylinder(-2.5, 4.0, 1.95));
        cylinderPtr.translate(-3.5, 0, 0);
        cylinderPtr.setTransformTexture(false);
        cylinderPtr.setMaterial(svMattePtr2);
        w.addObject(cylinderPtr);

        // cone
        Checker3D checkerPtr3 = new Checker3D();
        checkerPtr3.setSize(1.0);
        checkerPtr3.setColor1(Utility.BLACK);
        checkerPtr3.setColor2(Utility.WHITE);

        SV_Matte svMattePtr3 = new SV_Matte();
        svMattePtr3.setKa(0.75);
        svMattePtr3.setKd(0.75);
        svMattePtr3.setCd(checkerPtr3);

        Instance conePtr = new Instance(new OpenCone());
        conePtr.scale(2.5);
        conePtr.translate(2.35, -1.5, 0);
        conePtr.setTransformTexture(false);
        conePtr.setMaterial(svMattePtr3);
        w.addObject(conePtr);

        // box
        Checker3D checkerPtr4 = new Checker3D();
        checkerPtr4.setSize(1.0);
        checkerPtr4.setColor1(Utility.BLACK);
        checkerPtr4.setColor2(Utility.WHITE);

        SV_Matte svMattePtr4 = new SV_Matte();
        svMattePtr4.setKa(0.75);
        svMattePtr4.setKd(0.75);
        svMattePtr4.setCd(checkerPtr4);

        Box boxPtr = new Box(new Point3D(7, -1.0, -2), new Point3D(11, 3, 2));
        boxPtr.setMaterial(svMattePtr4);
        w.addObject(boxPtr);

        // ground plane
        Checker3D checkerPtr5 = new Checker3D();
        checkerPtr5.setSize(1.5);
        checkerPtr5.setColor1(0.35);
        checkerPtr5.setColor2(0.5);

        SV_Matte svMattePtr5 = new SV_Matte();
        svMattePtr5.setKa(0.75);
        svMattePtr5.setKd(0.75);
        svMattePtr5.setCd(checkerPtr5);

        Plane planePtr = new Plane(new Point3D(0, -4.5, 0),
                new Normal(0, 1, 0));
        planePtr.setMaterial(svMattePtr5);
        w.addObject(planePtr);
    }

}
