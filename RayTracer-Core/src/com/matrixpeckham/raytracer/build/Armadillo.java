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
package com.matrixpeckham.raytracer.build;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.compound.TriangleMesh;
import com.matrixpeckham.raytracer.lights.AmbientOccluder;
import com.matrixpeckham.raytracer.materials.Dielectric;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.textures.TInstance;
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.textures.procedural.*;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.*;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author William Matrix Peckham
 */
public class Armadillo implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 28.12 (a) & (b)
// In the 10K bunny PLY file the triangle vertices are ordeUtility.RED counter-clockwise when viewed
// from the outside. If you want to render w model, you must reverse the normals.
// This is an option on the Grid class.
// As the bunny is quite small, I've scaled it uniformly by a factor of 20 to make it easier
// to design the filter color.
        int numSamples = 9;

        w.vp.setHres(1200);
        w.vp.setVres(1200);
        w.vp.setSamples(numSamples);
        w.vp.setMaxDepth(5);

        w.backgroundColor = Utility.BLACK;

        w.tracer = new Whitted(w);

        AmbientOccluder ambientPtr = new AmbientOccluder();
        ambientPtr.scaleRadiance(1);
        ambientPtr.setSampler(new MultiJittered(numSamples));
        w.setAmbient(ambientPtr);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(-50, 50, -100);
        pinholePtr.setLookat(0, 0, 0);
        pinholePtr.setViewDistance(150.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        // transparent horse
        Dielectric dielectricPtr = new Dielectric();
        dielectricPtr.setKa(0.0);
        dielectricPtr.setKd(0.0);
        dielectricPtr.setKs(0.2);
        dielectricPtr.setExp(2000.0);
        dielectricPtr.setIorIn(1.5);
        dielectricPtr.setIorOut(1.0);
        dielectricPtr.setCfIn(0.65, 0.65, 0.1);   // orange
        dielectricPtr.setCfOut(Utility.WHITE);

        // marble
        // ramp image
        Image imagePtr = new Image();
        String path
                = "resources/Textures/ppm/";
        try {
            imagePtr.loadPPMFile(Thread.currentThread().getContextClassLoader().
                    getResourceAsStream(path + "PaleminoRamp.ppm"));

        } catch (IOException ex) {
            Logger.getLogger(Armadillo.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

        // marble exture
        // The following RampFBmTexture constructor w.sets lacunarity = 2 and gain = 0.5.
        // These are the default values for LatticeNoise. See Chapter 31.
        int numOctaves = 1;
        double fbmAmount = 8.0;

        // noise:
        CubicNoise noisePtr = new CubicNoise();
        noisePtr.setNumOctaves(1);
        noisePtr.setGain(0.5);
        noisePtr.setLacunarity(4.0);
        RGBColor color1 = new RGBColor(201.0 / 255.0, 103.0 / 255.0, 7.0 / 255.0);
        RGBColor color2 = new RGBColor(1);
        FBMTwoColors texturePtr = new FBMTwoColors(color1, color2, 0,
                1, 1, noisePtr);
        RampFBmTexture marblePtr = new RampFBmTexture(imagePtr, numOctaves,
                fbmAmount);

        TInstance transformedMarblePtr = new TInstance(texturePtr);
        transformedMarblePtr.scale(new Vector3D(5.1));
        transformedMarblePtr.rotateZ(80);

        // material
        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(1);
        svMattePtr.setKd(0);
        svMattePtr.setCd(transformedMarblePtr);

        String fileName = "Armadillo.ply"; 	// 69000 triangles
        String path1
                = "resources/Models/";

        TriangleMesh bunnyPtr = new TriangleMesh(new Mesh());
        try {
//	bunnyPtr.reverseMeshNormals();				// you must use w for the 10K model
//	bunnyPtr.readFlatTriangles(fileName);

            bunnyPtr.readSmoothTriangles(Thread.currentThread().
                    getContextClassLoader().
                    getResourceAsStream(path1 + fileName));
        } catch (IOException ex) {
            Logger.getLogger(Armadillo.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
        //bunnyPtr.setMaterial(dielectricPtr);
        bunnyPtr.setMaterial(svMattePtr);
        bunnyPtr.setupCells();
        System.out.println("Bounding Box: " + bunnyPtr.getBoundingBox());

        Instance bigBunnyPtr = new Instance(bunnyPtr);
        bigBunnyPtr.scale(1.0);
        bigBunnyPtr.translate(0, 0, 0.0);
        w.addObject(bigBunnyPtr);
    }

}
