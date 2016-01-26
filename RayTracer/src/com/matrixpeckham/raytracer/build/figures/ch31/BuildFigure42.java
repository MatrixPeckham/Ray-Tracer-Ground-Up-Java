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

import com.matrixpeckham.raytracer.build.figures.ch28.BuildFigure12A;
import com.matrixpeckham.raytracer.build.figures.ch29.BuildFigure04;
import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.compound.TriangleMesh;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.TInstance;
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.textures.procedural.CubicNoise;
import com.matrixpeckham.raytracer.textures.procedural.WrappedRamp;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Mesh;
import com.matrixpeckham.raytracer.util.Normal;
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
public class BuildFigure42 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2008.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 31.42
        int numSamples = 16;

        w.vp.setHres(800);
        w.vp.setVres(400);
        w.vp.setMaxDepth(1);
        w.vp.setSamples(numSamples);

        w.backgroundColor = Utility.BLACK;
        w.tracer = new Whitted(w);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(0, 30, 100);
        pinholePtr.setLookat(-0.01, 0.09, 0);
        pinholePtr.setViewDistance(173333.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        Directional lightPtr = new Directional();
        lightPtr.setDirection(1, 1, 4);
        lightPtr.scaleRadiance(3.0);
        w.addLight(lightPtr);

        //	String fileName = "Bunny4K.ply"; 		// 4000 triangles
        //	String fileName = "Bunny10K.ply"; 	// 10000 triangles
        //	String fileName = "Bunny16K.ply"; 	// 16000 triangles
        String fileName = "Bunny69K.ply"; 	// 69000 triangles
        String meshpath
                = "C:\\Users\\Owner\\Documents\\Ground Up raytracer\\Models\\Stanford Bunny\\";

        TriangleMesh gridPtr = new TriangleMesh(new Mesh());
        try {
//	bunnyPtr.reverseMeshNormals();				// you must use w for the 10K model
//	bunnyPtr.readFlatTriangles(fileName);	

            gridPtr.readSmoothTriangles(new File(meshpath + fileName));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure12A.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

        gridPtr.setupCells();

        // center bunny:
        // image:
        Image imagePtr1 = new Image();
        String path
                = "C:\\Users\\Owner\\Documents\\Ground Up raytracer\\Textures\\ppm\\";
        try {
            imagePtr1.loadPPMFile(new File(path + "TurquoiseAndBrownRamp.ppm"));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure04.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

	// noise:
        CubicNoise noisePtr1 = new CubicNoise();
        noisePtr1.setNumOctaves(4);
        noisePtr1.setGain(0.5);
        noisePtr1.setLacunarity(2.0);

	// texture:
        WrappedRamp wrappedPtr1 = new WrappedRamp(imagePtr1);
        wrappedPtr1.setNoise(noisePtr1);
        wrappedPtr1.setWrapNumber(8.0);

        TInstance texturePtr1 = new TInstance(wrappedPtr1);
        texturePtr1.scale(0.1);

	// material:
        SV_Matte svMattePtr1 = new SV_Matte();
        svMattePtr1.setKa(0.15);
        svMattePtr1.setKd(0.85);
        svMattePtr1.setCd(texturePtr1);

        Instance bunnyPtr1 = new Instance(gridPtr);
        bunnyPtr1.setMaterial(svMattePtr1);
        bunnyPtr1.rotateY(25);
        w.addObject(bunnyPtr1);

	// left bunny 
        Image imagePtr2 = new Image();
        try {
            imagePtr2.loadPPMFile(new File(path + "BlueAndBuffRamp.ppm"));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure04.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

	// noise:
        CubicNoise noisePtr2 = new CubicNoise();
        noisePtr2.setNumOctaves(4);
        noisePtr2.setGain(0.5);
        noisePtr2.setLacunarity(3.0);

	// texture:
        WrappedRamp wrappedPtr2 = new WrappedRamp(imagePtr2);
        wrappedPtr2.setNoise(noisePtr2);
        wrappedPtr2.setWrapNumber(8.0);

        TInstance texturePtr2 = new TInstance(wrappedPtr2);
        texturePtr2.scale(0.075);

	// material:
        SV_Matte svMattePtr2 = new SV_Matte();
        svMattePtr2.setKa(0.15);
        svMattePtr2.setKd(0.85);
        svMattePtr2.setCd(texturePtr2);

        Instance bunnyPtr2 = new Instance(gridPtr);
        bunnyPtr2.setMaterial(svMattePtr2);
        bunnyPtr2.rotateY(25);
        bunnyPtr2.translate(-0.15, 0.0, 0.0);
        w.addObject(bunnyPtr2);

	// right bunny:
        Image imagePtr3 = new Image();
        try {
            imagePtr3.loadPPMFile(new File(path + "BrownRamp.ppm"));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure04.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

	// noise:
        CubicNoise noisePtr3 = new CubicNoise();
        noisePtr3.setNumOctaves(4);
        noisePtr3.setGain(0.5);
        noisePtr3.setLacunarity(5.0);

	// texture:
        WrappedRamp wrappedPtr3 = new WrappedRamp(imagePtr3);
        wrappedPtr3.setNoise(noisePtr3);
        wrappedPtr3.setWrapNumber(8.0);

        TInstance texturePtr3 = new TInstance(wrappedPtr3);
        texturePtr3.scale(0.3);

	// material:
        SV_Matte svMattePtr3 = new SV_Matte();
        svMattePtr3.setKa(0.15);
        svMattePtr3.setKd(0.85);
        svMattePtr3.setCd(texturePtr3);

        Instance bunnyPtr3 = new Instance(gridPtr);
        bunnyPtr3.setMaterial(svMattePtr3);
        bunnyPtr3.rotateY(25);
        bunnyPtr3.translate(0.15, 0.0, 0.0);
        w.addObject(bunnyPtr3);

	// reflective ground plane
        Reflective reflectivePtr = new Reflective();
        reflectivePtr.setKa(0.2);
        reflectivePtr.setKd(0.75);
        reflectivePtr.setCd(Utility.BLACK);
        reflectivePtr.setKs(0.0);
        reflectivePtr.setExp(20);
        reflectivePtr.setKr(0.5);
        reflectivePtr.setCr(0.8, 1.0, 0.8);

        Plane planePtr = new Plane(new Point3D(0, 0.033, 0),new Normal(0, 1, 0));
        planePtr.setMaterial(reflectivePtr);
        w.addObject(planePtr);

    }

}
