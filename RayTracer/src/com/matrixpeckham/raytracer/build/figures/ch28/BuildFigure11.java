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
import com.matrixpeckham.raytracer.geometricobjects.compound.ConcaveLens;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Dielectric;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.PlaneChecker;
import com.matrixpeckham.raytracer.tracers.Whitted;
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
public class BuildFigure11 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 28.11 and the extra images in the Chapter 28 download
        int numSamples = 25;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setMaxDepth(6);
        w.vp.setSamples(numSamples);

        w.backgroundColor = Utility.WHITE;

        w.tracer = new Whitted(w);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(10, 2, 20);
        pinholePtr.setLookat(0.25, 4, 0);
        pinholePtr.setViewDistance(1275.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        Directional lightPtr = new Directional();
        lightPtr.setDirection(2, 3, 0.5);
        lightPtr.scaleRadiance(3.0);
        lightPtr.setShadows(true);
        w.addLight(lightPtr);

        // lens
//	RGBColor glassColor=new RGBColor(0.65, 0.45, 0);   // orange 	for Figure 28.11(a)
        RGBColor glassColor = new RGBColor(0.0, 0.5, 0.5);   // cyan		for Figure 28.11(b)

        // extra imgages
//	RGBColor glassColor=new RGBColor(0.5, 0.0, 0.5);   // majenta
//	RGBColor glassColor=new RGBColor(0, 0.65, 0.35);   // blue-green
//	RGBColor glassColor=new RGBColor(0.0, 0.35, 0.65); // blue
        Dielectric glassPtr = new Dielectric();
        glassPtr.setIorIn(1.5);				// glass
        glassPtr.setIorOut(1.0);			// air
        glassPtr.setCfIn(glassColor);
        glassPtr.setCfOut(1, 1, 1);

        double radius = 4.0;
        double thickness = 2.0;
        double minDistance = 0.35;  			// for cyan
//	double minDistance = 0.1;   			// for all other colours

        Instance concaveLensPtr = new Instance(
                new ConcaveLens(radius, thickness, minDistance));
        concaveLensPtr.setMaterial(glassPtr);
        concaveLensPtr.rotateX(90);
        concaveLensPtr.translate(0.0, radius, 0.0);
        w.addObject(concaveLensPtr);

        // plane with checker
        PlaneChecker planeCheckerPtr = new PlaneChecker();
        planeCheckerPtr.setSize(3);
        planeCheckerPtr.setOutlineWidth(0.4);
        planeCheckerPtr.setColor1(Utility.WHITE);
        planeCheckerPtr.setColor2(Utility.WHITE);
        planeCheckerPtr.setOutlineColor(new RGBColor(0.25));

        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.25);
        svMattePtr.setKd(0.65);
        svMattePtr.setCd(planeCheckerPtr);

        Plane planePtr = new Plane(new Point3D(0, 0, 0), new Normal(0, 1, 0));
        planePtr.setMaterial(svMattePtr);
        planePtr.setShadows(false);
        w.addObject(planePtr);
    }

}
