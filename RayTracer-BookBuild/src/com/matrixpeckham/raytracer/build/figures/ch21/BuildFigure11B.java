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
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.beveledobjects.BeveledBox;
import com.matrixpeckham.raytracer.geometricobjects.compound.Archway;
import com.matrixpeckham.raytracer.geometricobjects.compound.Grid;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.TInstance;
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.textures.procedural.RampFBmTexture;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.BBox;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
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
public class BuildFigure11B implements BuildWorldFunction {

    @Override
    public void build(World w) {

// This builds the scene for Figure 21.11(b)
// The archway and wall are constructed in the (x, z) plane, and then rotated and translated into position.
// The archway is a class called Archway, but the wall is a plain Grid.
// The blocks are only w.added to the wall if they are not inside the archway.
// The code here is just to get you started, with the archway testing hardwired in.
// A systematic approach would be to implement a Wall class that has a list of doors and windows that it
// can check the blocks against, using a common user interface.
// The doors and windows should be able to be transformed with the wall.
// The beveled wedges can also be used to build circular towers, again with doors and windows
        int numSamples = 1;

        w.vp.setHres(400);
        w.vp.setVres(400);
        w.vp.setSamples(numSamples);

        w.tracer = new RayCast(w);

        w.backgroundColor = new RGBColor(0.4, 0.4, 0.8);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(20, 12.5, 30);
        pinholePtr.setLookat(1, 11.6, 0.0);
        pinholePtr.setViewDistance(440);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        Directional lightPtr2 = new Directional();
        lightPtr2.setDirection(-20, 30, 30);
        lightPtr2.scaleRadiance(3);
        lightPtr2.setShadows(true);
        w.addLight(lightPtr2);

        // archway parameters
        double width = 20.0;
        double height = 25.0;
        double depth = 3.0;
        double columnWidth = 4.0;
        int numBlocks = 6;
        int numWedges = 10;
        double bevelRadius = 0.5;

        Archway archwayPtr = new Archway(width, height, depth, columnWidth,
                numBlocks, numWedges, bevelRadius);

	// put a different random sandstone texture on each archway component
        // sandstone parameters
        int numOctaves = 4;
        double lacunarity = 2.0;
        double gain = 0.5;
        double perturbation = 0.1;

        // sandstone ramp image
        Image imagePtr1 = new Image();
        String filename1
                = "C:\\Users\\Owner\\Documents\\Ground Up raytracer\\Textures\\ppm\\Sandstone_ramp1.ppm";
        try {
            imagePtr1.loadPPMFile(new File(filename1));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure11A.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

        int numObjects = archwayPtr.getNumObjects();

        for (int j = 0; j < numObjects; j++) {

            RampFBmTexture marblePtr = new RampFBmTexture(imagePtr1);
            marblePtr.setNumOctaves(numOctaves);
            marblePtr.setLacunarity(lacunarity);
            marblePtr.setGain(gain);
            marblePtr.setPerturbation(perturbation);

            // transformed marble texture
            TInstance wedgeMarblePtr = new TInstance(marblePtr);
            Utility.setRandSeed(j * 10L);
            wedgeMarblePtr.scale(0.5, 0.5, 0.5);
            wedgeMarblePtr.rotateX(20.0 * (2.0 * Utility.randDouble() - 1.0));
            wedgeMarblePtr.rotateY(30.0 * (2.0 * Utility.randDouble() - 1.0));
            wedgeMarblePtr.rotateZ(45.0 * (2.0 * Utility.randDouble() - 1.0));
            wedgeMarblePtr.translate(10.0 * (2.0 * Utility.randDouble() - 1.0),
                    20.0 * (2.0 * Utility.randDouble() - 1.0),
                    30.0 * (2.0 * Utility.randDouble() - 1.0));

            // marble material
            SV_Matte svMattePtr = new SV_Matte();
            svMattePtr.setKa(0.35);
            svMattePtr.setKd(0.85);
            svMattePtr.setCd(wedgeMarblePtr);

            archwayPtr.storeMaterial(svMattePtr, j);
        }

        archwayPtr.setupCells();

        Instance doorPtr = new Instance(archwayPtr);
        doorPtr.rotateZ(90);
        doorPtr.rotateY(90);
        doorPtr.translate(0.0, height - width / 2.0, 0.0);
        w.addObject(doorPtr);

	// build a wall of blocks that has the archway going through it
        // wall parameters
        double wallHeight = 40;   // x direction
        double wallLength = 100; 	// z direction
        double wallThickness = 3;	// y direction
        int numBlocksPerRow = 40;
        int numRows = 20;
        double offset = wallThickness / 4.0; // wall is offset back from the front of the archway

        double blockLength = wallLength / numBlocksPerRow;   // z dimension of blocks
        double blockHeight = wallHeight / numRows;			// x dimension of blocks
        double blockOffset = blockLength / 2.0;

        Grid wallPtr = new Grid();

        for (int k = 0; k < numRows / 2; k++) {
            for (int j = 0; j < numBlocksPerRow; j++) {   // first row

                Point3D p0 = new Point3D(-(height - width / 2.0) + 2 * k
                        * blockHeight,
                        -offset,
                        -wallLength / 2.0 + j * blockLength);
                Point3D p1 = new Point3D(-(height - width / 2.0) + (2 * k + 1)
                        * blockHeight,
                        -offset + wallThickness,
                        -wallLength / 2.0 + (j + 1) * blockLength);
                BeveledBox blockPtr = new BeveledBox(p0, p1, bevelRadius);

                // only w.add the block to the wall if it isn't inside the archway
                BBox bbox = blockPtr.getBoundingBox();

                boolean inRectangle = bbox.z0 < (width / 2.0 - columnWidth
                        + bevelRadius)
                        && bbox.z1 > (-width / 2.0 + columnWidth - bevelRadius)
                        && bbox.x0 < 0.0;

                double rSquared = (width / 2.0 - columnWidth + bevelRadius)
                        * (width / 2.0 - columnWidth + bevelRadius);
                double d0 = bbox.x0 * bbox.x0 + bbox.z0 * bbox.z0;	// lower left
                double d1 = bbox.x0 * bbox.x0 + bbox.z1 * bbox.z1;	// lower right
                double d2 = bbox.x1 * bbox.x1 + bbox.z1 * bbox.z1;	// upper right
                double d3 = bbox.x1 * bbox.x1 + bbox.z0 * bbox.z0;	// upper left

                boolean inCircle = d0 < rSquared || d1 < rSquared || d2
                        < rSquared || d3 < rSquared && bbox.x0 > blockHeight;

                if (!inRectangle && !inCircle) {
                    wallPtr.addObject(blockPtr);
                }
            }

            for (int j = 0; j < numBlocksPerRow; j++) {   // second row - offset in the z direction by half a block length

                Point3D p0 = new Point3D(-(height - width / 2.0) + (2 * k + 1)
                        * blockHeight,
                        -offset,
                        -wallLength / 2.0 + j * blockLength + blockOffset);
                Point3D p1 = new Point3D(-(height - width / 2.0) + (2 * k + 2)
                        * blockHeight,
                        -offset + wallThickness,
                        -wallLength / 2.0 + (j + 1) * blockLength + blockOffset);
                BeveledBox blockPtr = new BeveledBox(p0, p1, bevelRadius);

                // only w.add the block to the wall if it isn't inside the archway
                BBox bbox = blockPtr.getBoundingBox();

                boolean inRectangle = bbox.z0 < (width / 2.0 - columnWidth
                        + bevelRadius)
                        && bbox.z1 > (-width / 2.0 + columnWidth - bevelRadius)
                        && bbox.x0 < 0.0;

                double rSquared = (width / 2.0 - columnWidth + bevelRadius)
                        * (width / 2.0 - columnWidth + bevelRadius);
                double d0 = bbox.x0 * bbox.x0 + bbox.z0 * bbox.z0;	// lower left
                double d1 = bbox.x0 * bbox.x0 + bbox.z1 * bbox.z1;	// lower right
                double d2 = bbox.x1 * bbox.x1 + bbox.z1 * bbox.z1;	// upper right
                double d3 = bbox.x1 * bbox.x1 + bbox.z0 * bbox.z0;	// upper left

                boolean inCircle = d0 < rSquared || d1 < rSquared || d2
                        < rSquared || d3 < rSquared && bbox.x0 > blockHeight;

                if (!inRectangle && !inCircle) {
                    wallPtr.addObject(blockPtr);
                }
            }
        }

        // use a different random marble texture on each block
        // the marble paramters are the same as the sandstone's, except for the perturbation:
        perturbation = 3.0;

        // gray marble ramp image
        Image imagePtr2 = new Image();
        String filename2
                = "C:\\Users\\Owner\\Documents\\Ground Up raytracer\\Textures\\ppm\\GrayMarbleRamp.ppm";
        try {
            imagePtr2.loadPPMFile(new File(filename2));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure11A.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

        numObjects = wallPtr.getNumObjects();

        for (int j = 0; j < numObjects; j++) {

            RampFBmTexture marblePtr = new RampFBmTexture(imagePtr2);
            marblePtr.setNumOctaves(numOctaves);
            marblePtr.setLacunarity(lacunarity);
            marblePtr.setGain(gain);
            marblePtr.setPerturbation(perturbation);

            // transformed marble texture
            TInstance wedgeMarblePtr = new TInstance(marblePtr);
            Utility.setRandSeed(j * 10);
            wedgeMarblePtr.scale(0.25, 0.25, 0.25);
            wedgeMarblePtr.rotateX(20.0 * (2.0 * Utility.randDouble() - 1.0));
            wedgeMarblePtr.rotateY(30.0 * (2.0 * Utility.randDouble() - 1.0));
            wedgeMarblePtr.rotateZ(45.0 * (2.0 * Utility.randDouble() - 1.0));
            wedgeMarblePtr.translate(10.0 * (2.0 * Utility.randDouble() - 1.0),
                    20.0 * (2.0 * Utility.randDouble() - 1.0),
                    30.0 * (2.0 * Utility.randDouble() - 1.0));

            // marble material
            SV_Matte svMattePtr = new SV_Matte();
            svMattePtr.setKa(0.25);
            svMattePtr.setKd(0.5);
            svMattePtr.setCd(wedgeMarblePtr);

            wallPtr.storeMaterial(svMattePtr, j);
        }

        wallPtr.setupCells();

        Instance verticalWallPtr = new Instance(wallPtr);
        verticalWallPtr.rotateZ(90);
        verticalWallPtr.rotateY(90);
        verticalWallPtr.translate(0.0, height - width / 2.0, 0.0);
        w.addObject(verticalWallPtr);

        // ground plane
        Matte mattePtr = new Matte();
        mattePtr.setKa(0.25);
        mattePtr.setKd(0.5);
        mattePtr.setCd(0.35, 0.75, 0.35);

        Plane planePtr = new Plane(new Point3D(0.0), new Normal(0, 1, 0));
        planePtr.setMaterial(mattePtr);
        w.addObject(planePtr);

    }

    private static final Logger LOG
            = Logger.getLogger(BuildFigure11B.class.getName());

}
