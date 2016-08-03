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
package com.matrixpeckham.raytracer.build.figures.ch27;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.compound.FlatRimmedBowl;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Rectangle;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.materials.Transparent;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure19C implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 27.19(c)
// You can implement the flat-rimmed bowl object as an exercise.
// See Exercise 19.25
        int numSamples = 4;   // at maxDepth = 10, w renders very slowly

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setMaxDepth(10);
        w.vp.setSamples(numSamples);

        Ambient ambientPtr = new Ambient();
        ambientPtr.scaleRadiance(0.25);
        w.setAmbient(ambientPtr);

        w.backgroundColor = new RGBColor(0.0, 0.3, 0.25);

        w.tracer = new Whitted(w);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(1, 7.5, 20);
        pinholePtr.setLookat(0, -0.35, 0);
        pinholePtr.setViewDistance(5250.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        PointLight lightPtr = new PointLight();
        lightPtr.setLocation(20, 20, 15);
        lightPtr.scaleRadiance(2.75);
        lightPtr.setShadows(true);
        w.addLight(lightPtr);

        // transparent bowl
        Transparent glassPtr = new Transparent();
        glassPtr.setKs(0.5);
        glassPtr.setExp(2000.0);
        glassPtr.setIor(1.5);
        glassPtr.setKr(0.1);
        glassPtr.setKt(0.9);

        double innerRadius = 0.9;
        double outerRadius = 1.0;

        FlatRimmedBowl bowlPtr = new FlatRimmedBowl(innerRadius, outerRadius);
        bowlPtr.setMaterial(glassPtr);
        w.addObject(bowlPtr);

	// the two spheres in the bowl just touch its inner surface
        // Utility.RED sphere
        Reflective reflectivePtr1 = new Reflective();
        reflectivePtr1.setKa(0.6);
        reflectivePtr1.setKd(0.4);
        reflectivePtr1.setCd(Utility.RED);
        reflectivePtr1.setKs(0.5);
        reflectivePtr1.setExp(2000.0);
        reflectivePtr1.setKr(0.25);

        double radius = 0.4;
        double theta = 55.0;
        theta = Math.PI * theta / 180.0;
        double xc = -(0.9 - radius) * cos(theta);
        double yc = -(0.9 - radius) * sin(theta);
        Sphere spherePtr2 = new Sphere(new Point3D(xc, yc, 0), radius);
        spherePtr2.setMaterial(reflectivePtr1);
        w.addObject(spherePtr2);

        // yellow sphere
        Reflective reflectivePtr2 = new Reflective();
        reflectivePtr2.setKa(0.6);
        reflectivePtr2.setKd(0.4);
        reflectivePtr2.setCd(1, 1, 0);			// yellow
        reflectivePtr2.setKs(0.5);
        reflectivePtr2.setExp(2000.0);
        reflectivePtr2.setKr(0.5);

        radius = 0.35;
        theta = 35.0;
        theta = Math.PI * theta / 180.0;
        xc = (0.9 - radius) * cos(theta);
        yc = -(0.9 - radius) * sin(theta);
        Sphere spherePtr3 = new Sphere(new Point3D(xc, yc, 0), radius);
        spherePtr3.setMaterial(reflectivePtr2);
        w.addObject(spherePtr3);

        // rectangle
        Checker3D checkerPtr = new Checker3D();
        checkerPtr.setSize(1.0);
        checkerPtr.setColor1(Utility.WHITE);
        checkerPtr.setColor2(0.5);

        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.8);
        svMattePtr.setKd(0.85);
        svMattePtr.setCd(checkerPtr);

        Point3D p0 = new Point3D(-2, -1, -5);
        Vector3D a = new Vector3D(0, 0, 9);
        Vector3D b = new Vector3D(4, 0, 0);

        Rectangle rectanglePtr = new Rectangle(p0, a, b);
        rectanglePtr.setMaterial(svMattePtr);
        w.addObject(rectanglePtr);
    }

}
