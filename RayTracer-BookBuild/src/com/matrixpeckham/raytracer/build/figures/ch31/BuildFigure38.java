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
package com.matrixpeckham.raytracer.build.figures.ch31;

import com.matrixpeckham.raytracer.build.figures.ch29.BuildFigure04;
import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.compound.SolidCylinder;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.TInstance;
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.textures.procedural.RampFBmTexture;
import com.matrixpeckham.raytracer.tracers.RayCast;
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
public class BuildFigure38 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2008.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 31.38.
// This was re-rendered from scratch, and the instrinsic sandstone transformations
// are different.
// The cylinders are also slightly different.
// There is no ground plane.
        int numSamples = 16;

        w.vp.setHres(800);
        w.vp.setVres(400);
        w.vp.setSamples(numSamples);

        Ambient ambientPtr = new Ambient();
        ambientPtr.scaleRadiance(0.5);
        w.setAmbient(ambientPtr);

        w.backgroundColor = Utility.BLACK;
        w.tracer = new RayCast(w);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(0, 25, 50);
        pinholePtr.setLookat(new Point3D(0.0));
        pinholePtr.setViewDistance(5320.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        PointLight lightPtr = new PointLight();
        lightPtr.setLocation(30, 40, 35);
        lightPtr.scaleRadiance(5.0);
        w.addLight(lightPtr);

        // noise parameters
        // gain = 0.5, lacunarity = 2.0 by default
        int numOctaves = 4;
        double fbmAmount = 0.05;

        // cylinder parameters
        double bottom = -1.75;
        double top = 1.75;
        double radius = 0.9;

        // far left cylinder:
        Image imagePtr1 = new Image();
        String path

                = "resources/Textures/ppm/";
        try {
            imagePtr1.loadPPMFile(Thread.currentThread().getContextClassLoader().getResourceAsStream(path + "sandstone_ramp1.ppm"));

        } catch (IOException ex) {
            Logger.getLogger(BuildFigure04.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

        TInstance sandstonePtr1 = new TInstance(new RampFBmTexture(imagePtr1,
                numOctaves, fbmAmount));
        sandstonePtr1.scale(1.0, 2.0, 1.0);
        sandstonePtr1.rotateZ(110.0);
        sandstonePtr1.translate(1.0, 4.0, 0.0);

        SV_Matte svMattePtr1 = new SV_Matte();
        svMattePtr1.setKa(0.35);
        svMattePtr1.setKd(0.75);
        svMattePtr1.setCd(sandstonePtr1);

        Instance cylinderPtr1 = new Instance(new SolidCylinder(bottom, top,
                radius));
        cylinderPtr1.setMaterial(svMattePtr1);
        cylinderPtr1.rotateY(-30);
        cylinderPtr1.translate(-3.0, 0.0, 0.0);
        w.addObject(cylinderPtr1);

        // left middle cylinder:
        Image imagePtr2 = new Image();
        try {

            imagePtr2.loadPPMFile(Thread.currentThread().getContextClassLoader().getResourceAsStream(path + "sandstone_ramp2.ppm"));

        } catch (IOException ex) {
            Logger.getLogger(BuildFigure04.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

        TInstance sandstonePtr2 = new TInstance(new RampFBmTexture(imagePtr2,
                numOctaves, fbmAmount));
        sandstonePtr2.scale(1.0, 2.0, 1.0);
        sandstonePtr2.rotateZ(110.0);
        sandstonePtr2.translate(0.5, 0.0, 0.0);

        SV_Matte svMattePtr2 = new SV_Matte();
        svMattePtr2.setKa(0.35);
        svMattePtr2.setKd(0.75);
        svMattePtr2.setCd(sandstonePtr2);

        Instance cylinderPtr2 = new Instance(new SolidCylinder(bottom, top,
                radius));
        cylinderPtr2.setMaterial(svMattePtr2);
        cylinderPtr2.rotateY(-30);
        cylinderPtr2.translate(-1.0, 0.0, 0.0);
        w.addObject(cylinderPtr2);

        // right middle cylinder:
        Image imagePtr3 = new Image();
        try {

            imagePtr3.loadPPMFile(Thread.currentThread().getContextClassLoader().getResourceAsStream(path + "sandstone_ramp3.ppm"));

        } catch (IOException ex) {
            Logger.getLogger(BuildFigure04.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

        TInstance sandstonePtr3 = new TInstance(new RampFBmTexture(imagePtr3,
                numOctaves, fbmAmount));
        sandstonePtr3.scale(1.0, 2.0, 1.0);
        sandstonePtr3.rotateZ(110.0);
        sandstonePtr3.translate(0.5, -2.0, 0.0);

        SV_Matte svMattePtr3 = new SV_Matte();
        svMattePtr3.setKa(0.35);
        svMattePtr3.setKd(0.75);
        svMattePtr3.setCd(sandstonePtr3);

        Instance cylinderPtr3 = new Instance(new SolidCylinder(bottom, top,
                radius));
        cylinderPtr3.setMaterial(svMattePtr3);
        cylinderPtr3.rotateY(-30);
        cylinderPtr3.translate(1.0, 0.0, 0.0);
        w.addObject(cylinderPtr3);

        // far right cylinder:
        // right middle cylinder:
        Image imagePtr4 = new Image();
        try {

            imagePtr4.loadPPMFile(Thread.currentThread().getContextClassLoader().getResourceAsStream(path + "sandstone_ramp4.ppm"));

        } catch (IOException ex) {
            Logger.getLogger(BuildFigure04.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

        TInstance sandstonePtr4 = new TInstance(new RampFBmTexture(imagePtr4,
                numOctaves, fbmAmount));
        sandstonePtr4.scale(1.0, 2.0, 1.0);
        sandstonePtr4.rotateZ(110.0);
        sandstonePtr4.translate(0.0, -1.0, 0.0);

        SV_Matte svMattePtr4 = new SV_Matte();
        svMattePtr4.setKa(0.35);
        svMattePtr4.setKd(0.75);
        svMattePtr4.setCd(sandstonePtr4);

        Instance cylinderPtr4 = new Instance(new SolidCylinder(bottom, top,
                radius));
        cylinderPtr4.setMaterial(svMattePtr4);
        cylinderPtr4.rotateY(-30);
        cylinderPtr4.translate(3.0, 0.0, 0.0);
        w.addObject(cylinderPtr4);
    }

}
