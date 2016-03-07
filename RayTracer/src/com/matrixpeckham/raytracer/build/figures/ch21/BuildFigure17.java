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
package com.matrixpeckham.raytracer.build.figures.ch21;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.compound.Rosette;
import com.matrixpeckham.raytracer.lights.Directional;
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
public class BuildFigure17 implements BuildWorldFunction {

    @Override
    public void build(World w) {
        int numSamples = 1;

        w.vp.setHres(600);
        w.vp.setVres(400);
        w.vp.setSamples(numSamples);

        w.tracer = new RayCast(w);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(50, 50, 30);
        pinholePtr.setLookat(new Point3D());
        pinholePtr.setViewDistance(5000);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        Directional lightPtr2 = new Directional();
        lightPtr2.setDirection(20, 30, 30);
        lightPtr2.scaleRadiance(2.0);
        lightPtr2.setShadows(true);
        w.addLight(lightPtr2);

	// rosette parameters
        int numRings = 3;		// maximum of 6
        double holeRadius = 0.75;
        double ringWidth = 1.0;
        double rb = 0.1;			// bevel radius
        double y0 = -0.25;			// minimum y value
        double y1 = 0.25;			// minimum y value

        Rosette rosettePtr
                = new Rosette(numRings, holeRadius, ringWidth, rb, y0, y1);

	// put a different random marble texture on each wedge
	// blue marble ramp image
        Image imagePtr1 = new Image();
        String filename
                = "C:\\Users\\Owner\\Documents\\Ground Up raytracer\\Textures\\ppm\\BlueMarbleRamp.ppm";
        try {
            imagePtr1.loadPPMFile(new File(filename));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure11A.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

	// marble texture parameters
        int numOctaves = 4;
        double lacunarity = 2.0;
        double gain = 0.5;
        double perturbation = 3.0;

        int numObjects = rosettePtr.getNumObjects();

        for (int j = 0; j < numObjects; j++) {

            RampFBmTexture blueMarblePtr = new RampFBmTexture(imagePtr1);   // blue marble
            blueMarblePtr.setNumOctaves(numOctaves);
            blueMarblePtr.setLacunarity(lacunarity);
            blueMarblePtr.setGain(gain);
            blueMarblePtr.setPerturbation(perturbation);

		// transformed marble texture
            TInstance wedgeMarblePtr = new TInstance(blueMarblePtr);
            Utility.setRandSeed(j * 10L);
            wedgeMarblePtr.scale(0.25, 0.25, 0.25);
            wedgeMarblePtr.rotateX(20.0 * (2.0 * Utility.randDouble() - 1.0));
            wedgeMarblePtr.rotateY(30.0 * (2.0 * Utility.randDouble() - 1.0));
            wedgeMarblePtr.rotateZ(45.0 * (2.0 * Utility.randDouble() - 1.0));
            wedgeMarblePtr.translate(10.0 * (2.0 * Utility.randDouble() - 1.0),
                    20.0 * (2.0 * Utility.randDouble() - 1.0), 30.0 * (2.0
                    * Utility.randDouble() - 1.0));

		// marble material
            SV_Matte svMattePtr = new SV_Matte();
            svMattePtr.setKa(0.35);
            svMattePtr.setKd(0.75);
            svMattePtr.setCd(wedgeMarblePtr);

            rosettePtr.storeMaterial(svMattePtr, j);	// store material in the specified wedge
        }

        rosettePtr.setupCells();
        w.addObject(rosettePtr);
    }

}
