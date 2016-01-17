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
import com.matrixpeckham.raytracer.geometricobjects.compound.FishBowl;
import com.matrixpeckham.raytracer.geometricobjects.compound.Grid;
import com.matrixpeckham.raytracer.geometricobjects.compound.TriangleMesh;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConvexPartCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Dielectric;
import com.matrixpeckham.raytracer.materials.Phong;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.PlaneChecker;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Mesh;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.sqrt;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure41B implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the Scene for Figure 28.41(b)
// This takes a huge time to render
// try 150 x 150 pixels, one sample per pixel, and maxDepth = 6.
        int numSamples = 1;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setSamples(numSamples);
        w.vp.setMaxDepth(6);

        w.tracer = new Whitted(w);

        w.backgroundColor = new RGBColor(0.9);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(-0.5, -1.0, 4);
        pinholePtr.setLookat(new Point3D(0.0));
        pinholePtr.setViewDistance(900.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        PointLight lightPtr1 = new PointLight();
        lightPtr1.setLocation(40, 25, 10);
        lightPtr1.scaleRadiance(2.5);
        lightPtr1.setShadows(true);
        w.addLight(lightPtr1);

        Directional lightPtr2 = new Directional();
        lightPtr2.setDirection(-1, 0, 0);
        lightPtr2.scaleRadiance(2.5);
        lightPtr2.setShadows(true);
        w.addLight(lightPtr2);

	// fishbowl
	// glass-air interface
        double c = 2;
        RGBColor glassColor = new RGBColor(0.27 * c, 0.49 * c, 0.42 * c);
        RGBColor waterColor = new RGBColor(0.75, 1, 0.75);

        Dielectric glassPtr = new Dielectric();
        glassPtr.setKs(0.5);
        glassPtr.setExp(8000.0);
        glassPtr.setIorIn(1.50);			// glass
        glassPtr.setIorOut(1.0);			// air
        glassPtr.setCfIn(glassColor);
        glassPtr.setCfOut(Utility.WHITE);

	// water-air interface
        Dielectric waterPtr = new Dielectric();
        waterPtr.setKs(0.5);
        waterPtr.setExp(8000);
        waterPtr.setIorIn(1.33);			// water
        waterPtr.setIorOut(1.0);			// air
        waterPtr.setCfIn(waterColor);
        waterPtr.setCfOut(Utility.WHITE);

	// water-glass interface
        Dielectric dielectricPtr1 = new Dielectric();
        dielectricPtr1.setKs(0.5);
        dielectricPtr1.setExp(8000);
        dielectricPtr1.setIorIn(1.33); 		// water
        dielectricPtr1.setIorOut(1.5); 		// glass
        dielectricPtr1.setCfIn(waterColor);
        dielectricPtr1.setCfOut(glassColor);

	// physical bowl parameters (also the defaults)
        double innerRadius = 1.0;
        double glassThickness = 0.1;
        double waterDepth = 1.25;
        double meniscusRadius = 0.05;
        double openingAngle = 90.0;

        FishBowl fishbowlPtr = new FishBowl(innerRadius,
                glassThickness,
                waterDepth,
                meniscusRadius,
                openingAngle);
        fishbowlPtr.setGlassAirMaterial(glassPtr);
        fishbowlPtr.setWaterAirMaterial(waterPtr);
        fishbowlPtr.setWaterGlassMaterial(dielectricPtr1);
        w.addObject(fishbowlPtr);

	// goldfish
        Phong phongPtr1 = new Phong();
        phongPtr1.setKa(0.4);
        phongPtr1.setKd(0.8);
        phongPtr1.setCd(1.0, 0.15, 0.0);   	// orange 
        phongPtr1.setKs(0.5);
        phongPtr1.setCs(1.0, 0.35, 0.0);		// orange
        phongPtr1.setExp(50.0);
        phongPtr1.setShadows(false);

        // we read the fish file once, and instance it       //	String fileName = "goldfish_low_res.ply";		// for scene design
        String fileName = "goldfish_high_res.ply";  // for production
        String path
                = "C:\\Users\\Owner\\Documents\\Ground Up raytracer\\Models\\";

        TriangleMesh gridPtr = new TriangleMesh(new Mesh());
        try {

            gridPtr.readSmoothTriangles(new File(path + fileName));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure12A.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
        gridPtr.setMaterial(phongPtr1);
        gridPtr.setupCells();

        Instance goldFishPtr1 = new Instance(gridPtr);
        goldFishPtr1.scale(0.03);
        goldFishPtr1.rotateY(-45);
        goldFishPtr1.translate(0.5, 0.0, 0.0);
        w.addObject(goldFishPtr1);

        Instance goldfishPtr2 = new Instance(gridPtr);
        goldfishPtr2.scale(0.02);
        goldfishPtr2.rotateY(90);
        goldfishPtr2.translate(-0.75, 0.0, 0.0);
        goldfishPtr2.rotateY(-60);
        w.addObject(goldfishPtr2);

        Instance goldfishPtr3 = new Instance(gridPtr);
        goldfishPtr3.scale(0.02);
        goldfishPtr3.rotateX(20);
        goldfishPtr3.rotateY(-45);
        goldfishPtr3.translate(-0.1, -0.4, 0.0);
        w.addObject(goldfishPtr3);

	// cylinder under the bowl
        Phong phongPtr2 = new Phong();
        phongPtr2.setKa(0.4);
        phongPtr2.setKd(0.8);
        phongPtr2.setCd(0.05);
        phongPtr2.setKs(0.2);
        phongPtr2.setExp(100.0);

        double bottom = -1.2;
        double radius = 0.5;
        double top = -sqrt(1.1 * 1.1 - radius * radius);

        ConvexPartCylinder cylinderPtr = new ConvexPartCylinder(bottom, top,
                radius);
        cylinderPtr.setMaterial(phongPtr2);
        w.addObject(cylinderPtr);

	// single air bubble
        Dielectric dielectricPtr2 = new Dielectric();
        dielectricPtr2.setIorIn(1.0); 		// air
        dielectricPtr2.setIorOut(1.33); 	// water
        dielectricPtr2.setCfIn(Utility.WHITE);
        dielectricPtr2.setCfOut(waterColor);

        Sphere bubblePtr1 = new Sphere(new Point3D(0.2, 0.2, 0.2), 0.05);
        bubblePtr1.setMaterial(dielectricPtr2);
        w.addObject(bubblePtr1);

	// streams of air bubbles
        Utility.setRandSeed(1000);

        double bubbleRadius = 0.045;
        double ycBottom = -0.9;    			// height of bottom bubble center
        double ycTop = 0.2;    			// height of top bubble center
        double numBubbles = 8;				// number of bubbles in stream
        double spacing = (ycTop - ycBottom) / numBubbles; // vertical spacing between bubble centers
        double translationFactor = bubbleRadius / 2.0;
        double min = 0.9;   			// minimum bubble scaling
        double max = 1.1;				// maximum bubble scaling
        double xc = -0.1;   			// center x 
        double zc = 0.3;				// center y

	// bubble stream 1
        Grid bubbleStreamPtr1 = new Grid();

        for (int j = 0; j <= numBubbles; j++) {
            Instance bubblePtr2 = new Instance(new Sphere());

            bubblePtr2.scale(min + Utility.randDouble() * (max - min),
                    min + Utility.randDouble() * (max - min),
                    min + Utility.randDouble() * (max - min));

            bubblePtr2.scale(bubbleRadius);

            bubblePtr2.rotateX(360.0 * Utility.randDouble());
            bubblePtr2.rotateY(360.0 * Utility.randDouble());
            bubblePtr2.rotateZ(360.0 * Utility.randDouble());

            bubblePtr2.translate(xc + (2.0 * Utility.randDouble() - 1.0)
                    * translationFactor,
                    ycBottom + j * spacing + (2.0 * Utility.randDouble() - 1.0)
                    * translationFactor,
                    zc + (2.0 * Utility.randDouble() - 1.0) * translationFactor);

            bubblePtr2.setMaterial(dielectricPtr2);
            bubbleStreamPtr1.addObject(bubblePtr2);
        }

        bubbleStreamPtr1.setupCells();
        w.addObject(bubbleStreamPtr1);

	// bubble stream 2 
        numBubbles = 7;
        xc = 0.075;
        zc = 0.1;

        Grid bubbleStreamPtr2 = new Grid();

        for (int j = 0; j <= numBubbles; j++) {
            Instance bubblePtr = new Instance(new Sphere());

            bubblePtr.scale(min + Utility.randDouble() * (max - min),
                    min + Utility.randDouble() * (max - min),
                    min + Utility.randDouble() * (max - min));

            bubblePtr.scale(bubbleRadius);

            bubblePtr.rotateX(360.0 * Utility.randDouble());
            bubblePtr.rotateY(360.0 * Utility.randDouble());
            bubblePtr.rotateZ(360.0 * Utility.randDouble());

            bubblePtr.translate(xc + (2.0 * Utility.randDouble() - 1.0)
                    * translationFactor,
                    ycBottom + j * spacing + (2.0 * Utility.randDouble() - 1.0)
                    * translationFactor,
                    zc + (2.0 * Utility.randDouble() - 1.0) * translationFactor);

            bubblePtr.setMaterial(dielectricPtr2);
            bubbleStreamPtr2.addObject(bubblePtr);
        }

        bubbleStreamPtr2.setupCells();
        w.addObject(bubbleStreamPtr2);

	// bubble stream 3 
        numBubbles = 9;
        xc = -0.15;
        zc = -0.3;

        Grid bubbleStreamPtr3 = new Grid();

        for (int j = 0; j <= numBubbles; j++) {
            Instance bubblePtr = new Instance(new Sphere());

            bubblePtr.scale(min + Utility.randDouble() * (max - min),
                    min + Utility.randDouble() * (max - min),
                    min + Utility.randDouble() * (max - min));

            bubblePtr.scale(bubbleRadius);

            bubblePtr.rotateX(360.0 * Utility.randDouble());
            bubblePtr.rotateY(360.0 * Utility.randDouble());
            bubblePtr.rotateZ(360.0 * Utility.randDouble());

            bubblePtr.translate(xc + (2.0 * Utility.randDouble() - 1.0)
                    * translationFactor,
                    ycBottom + j * spacing + (2.0 * Utility.randDouble() - 1.0)
                    * translationFactor,
                    zc + (2.0 * Utility.randDouble() - 1.0) * translationFactor);

            bubblePtr.setMaterial(dielectricPtr2);
            bubbleStreamPtr3.addObject(bubblePtr);
        }

        bubbleStreamPtr3.setupCells();
        w.addObject(bubbleStreamPtr3);

	// plane
        PlaneChecker checkerPtr = new PlaneChecker();
        checkerPtr.setSize(0.5);
        checkerPtr.setOutlineWidth(0.05);
        checkerPtr.setColor1(new RGBColor(0.75));
        checkerPtr.setColor2(new RGBColor(0.75));
        checkerPtr.setOutlineColor(new RGBColor(0.45));

        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.5);
        svMattePtr.setKd(0.5);
        svMattePtr.setCd(checkerPtr);

        Plane planePtr = new Plane(new Point3D(0, -1.2, 0), new Normal(0, 1, 0));
        planePtr.setMaterial(svMattePtr);

        Instance planePtr2 = new Instance(planePtr); // to adjust the reflection of the grid lines off the top of the water
        planePtr2.rotateY(30);
        planePtr2.translate(0.25, 0, 0.15);
        w.addObject(planePtr2);
    }

}
