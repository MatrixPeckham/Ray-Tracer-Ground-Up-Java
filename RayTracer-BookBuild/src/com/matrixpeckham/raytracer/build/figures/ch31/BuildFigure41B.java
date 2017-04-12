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
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConcavePartSphere;
import com.matrixpeckham.raytracer.geometricobjects.primitives.CutCube;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.TInstance;
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.textures.procedural.CubicNoise;
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
public class BuildFigure41B implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2008.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 31.41(b)
// The box with the curved surface cut from it is modeled as follows:
// A CutCube object is defined, which is an axis aligned cube where a hit is only recorded
// when a ray hits the cube AND the hit point is outside a sphere centered on p1 -
// the (+x, +y, +z) vertex.
// You can implement w by starting with an axis aligned box and putting the sphere
// test in the hit and shadowHit functions, just before they return true.
// I've left w as an exercise.
// The hole is then filled with a concave part sphere.
// If the sphere radius is less than the edge length of the cube, the sphere intersects
// the cube faces at 90 degress, and it's simple to specify the angular ranges - which are
// independent of the radius.
// A more flexible way to render w type of object is with constructive solid
// geometry (CSG), where you just subtract a sphere from a cube.
// The three textures in Figure 30.1 are noise based. The wrapped texture in Figure 30.1(a)
// and the marble texture in Figure 30.1(b) are discussed in Chapter 31. The wood texture in
// Figure 30.1(c) isn't discussed in Chapter 31, but I've included the Wood class and some
// sample images in the Chapter 31 download.
// As I have had to re-render these three images from scatch, the texture's details are
// different, as are the box, the lighting, and the viewing.
        int numSamples = 16;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setSamples(numSamples);

        w.tracer = new RayCast(w);
        w.backgroundColor = Utility.BLACK;

        Ambient ambientPtr = new Ambient();
        ambientPtr.scaleRadiance(0.5);
        w.setAmbient(ambientPtr);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(6, 8, 12);
        pinholePtr.setLookat(0.0, -0.1, 0.0);
        pinholePtr.setViewDistance(2850.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        PointLight lightPtr = new PointLight();
        lightPtr.setLocation(-2, 4, 10);
        lightPtr.scaleRadiance(4.5);
        lightPtr.setShadows(false);
        w.addLight(lightPtr);

        // noise:
        CubicNoise noisePtr = new CubicNoise();
        noisePtr.setNumOctaves(6);
        noisePtr.setGain(0.5);
        noisePtr.setLacunarity(2.0);

        // image:
        // image:
        Image imagePtr = new Image();
        String path
                = "resources/Textures/ppm/";
        try {
            imagePtr.loadPPMFile(Thread.currentThread().getContextClassLoader().getResourceAsStream(path + "sandstone_ramp1.ppm"));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure04.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

        // sandstone texture:
        RampFBmTexture sandstonePtr = new RampFBmTexture(imagePtr);
        sandstonePtr.setNoise(noisePtr);
        sandstonePtr.setPerturbation(6.0);

        TInstance transformedSandstonePtr = new TInstance(sandstonePtr);
        transformedSandstonePtr.rotateZ(90);
        transformedSandstonePtr.rotateY(45);
        transformedSandstonePtr.translate(-6.0, 0.0, 1.0);

        // material:
        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.5);
        svMattePtr.setKd(0.85);
        svMattePtr.setCd(transformedSandstonePtr);

        // cut cube parameters:
        Point3D p0 = new Point3D(-1.0);
        Point3D p1 = new Point3D(1.0);
        double sphereRadius = 1.5;

        CutCube cutCubePtr = new CutCube(p0, p1, sphereRadius);
        cutCubePtr.setMaterial(svMattePtr);
        w.addObject(cutCubePtr);

        // concave part sphere parameters:
        Point3D center = new Point3D(p1);
        double radius = sphereRadius;
        double phiMin = 180.0;
        double phiMax = 270.0;
        double thetaMin = 90.0;
        double thetaMax = 180.0;

        ConcavePartSphere partSpherePtr = new ConcavePartSphere(center,
                radius,
                phiMin,
                phiMax,
                thetaMin,
                thetaMax);
        partSpherePtr.setMaterial(svMattePtr);
        w.addObject(partSpherePtr);
    }

}
