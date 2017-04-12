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
import com.matrixpeckham.raytracer.geometricobjects.beveledobjects.BeveledCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.TInstance;
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.textures.procedural.RampFBmTexture;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
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

// This builds the scene for Figures 30.13(a) and 30.13(b).
// The build function is the same for both figures.
// The difference is in the BeveledCylinder contructor, as discussed on page 685.
        int numSamples = 16;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setSamples(numSamples);

        w.tracer = new RayCast(w);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(20, 10, 40);
        pinholePtr.setLookat(0, 1, 0);
        pinholePtr.setViewDistance(9000.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        Directional lightPtr1 = new Directional();
        lightPtr1.setDirection(30, 40, 20);
        lightPtr1.scaleRadiance(2.5);
        lightPtr1.setShadows(true);
        w.addLight(lightPtr1);

        // image:
        Image imagePtr = new Image();
        String path

                = "resources/Textures/ppm/";
        try {
            imagePtr.loadPPMFile(Thread.currentThread().getContextClassLoader().getResourceAsStream(path + "BlueMarbleRamp.ppm"));

        } catch (IOException ex) {
            Logger.getLogger(
                    com.matrixpeckham.raytracer.build.figures.ch29.BuildFigure04.class.
                    getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

        // marble parameters
        int numOctaves = 6;
        double fbmAmount = 6.0;
        RampFBmTexture rampMarblePtr = new RampFBmTexture(imagePtr, numOctaves,
                fbmAmount);

        // transformed marble texture
        // These intrinsic texture transformations are only to make the marble look "good"
        // on the cylinder.
        // They are unrelated to the translation in the y direction that's applied to the
        // marble on the top and bottom cylinder bevels in Figure 30.13(a).
        TInstance marblePtr = new TInstance();
        marblePtr.setTexture(rampMarblePtr);
        marblePtr.scale(0.4);
        marblePtr.rotateX(270);
        marblePtr.rotateY(30);
        marblePtr.rotateZ(30);

        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.35);
        svMattePtr.setKd(0.85);
        svMattePtr.setCd(marblePtr);

        // cylinder parameters
        double bottom = 0.0;
        double top = 2.0;
        double radius = 1.0;
        double bevelRadius = 0.5;

        BeveledCylinder cylinderPtr = new BeveledCylinder(bottom, top, radius,
                bevelRadius);
        cylinderPtr.setMaterial(svMattePtr);
        w.addObject(cylinderPtr);

        // ground plane
        Matte mattePtr1 = new Matte();
        mattePtr1.setKa(0.3);
        mattePtr1.setKd(0.85);
        mattePtr1.setCd(0.75);

        Plane planePtr1
                = new Plane(new Point3D(0, 0.01, 1), new Normal(0, 1, 0));
        planePtr1.setMaterial(mattePtr1);
        w.addObject(planePtr1);

        // plane perpendicular to x axis
        Matte mattePtr2 = new Matte();
        mattePtr2.setKa(0.25);
        mattePtr2.setKd(0.75);
        mattePtr2.setCd(0.75);

        Plane planePtr2
                = new Plane(new Point3D(-1.5, 0, 0), new Normal(1, 0, 0));
        planePtr2.setMaterial(mattePtr2);
        w.addObject(planePtr2);

        // plane perpendicular to z axis
        Matte mattePtr3 = new Matte();
        mattePtr3.setCd(0.6);
        mattePtr3.setKa(0.25);
        mattePtr3.setKd(0.5);

        Plane planePtr3
                = new Plane(new Point3D(0, 0, -1.5), new Normal(0, 0, 1));
        planePtr3.setMaterial(mattePtr3);
        w.addObject(planePtr3);
    }

}
