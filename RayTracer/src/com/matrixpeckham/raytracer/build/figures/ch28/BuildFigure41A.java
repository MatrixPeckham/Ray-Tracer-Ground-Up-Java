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
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure41A implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 28.41(a)
// This takes a huge amount of time to render
        int numSamples = 9;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setSamples(numSamples);
        w.vp.setMaxDepth(10);

        w.tracer = new Whitted(w);

        w.backgroundColor = new RGBColor(0.75);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(4.5, 6, 4);
        pinholePtr.setLookat(new Point3D(0.0));
        pinholePtr.setViewDistance(1800.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        PointLight lightPtr1 = new PointLight();
        lightPtr1.setLocation(40, 25, -10);
        lightPtr1.scaleRadiance(5.0);
        lightPtr1.setShadows(true);
        w.addLight(lightPtr1);

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
        Dielectric dielectricPtr = new Dielectric();
        dielectricPtr.setKs(0.5);
        dielectricPtr.setExp(8000);
        dielectricPtr.setIorIn(1.33); 		// water
        dielectricPtr.setIorOut(1.5); 		// glass
        dielectricPtr.setCfIn(waterColor);
        dielectricPtr.setCfOut(glassColor);

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
        fishbowlPtr.setWaterGlassMaterial(dielectricPtr);
        w.addObject(fishbowlPtr);

	// goldfish
        Phong phongPtr = new Phong();
        phongPtr.setKa(0.4);
        phongPtr.setKd(0.8);
        phongPtr.setCd(1.0, 0.15, 0.0);   	// orange 
        phongPtr.setKs(0.5);
        phongPtr.setCs(1.0, 0.35, 0.0);		// orange
        phongPtr.setExp(50.0);
        phongPtr.setShadows(false);
        //	String fileName = "goldfish_low_res.ply";		// for scene design
        String fileName = "goldfish_high_res.ply";  // for production
	String path="C:\\Users\\Owner\\Documents\\Ground Up raytracer\\Models\\";

        TriangleMesh gridPtr = new TriangleMesh(new Mesh());
        try{
        
            gridPtr.readSmoothTriangles(new File(path+fileName));
        } catch(IOException ex){
            Logger.getLogger(BuildFigure12A.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
        gridPtr.setMaterial(phongPtr);
        gridPtr.setupCells();

        Instance goldFishPtr = new Instance(gridPtr);
        goldFishPtr.scale(0.03);
        goldFishPtr.translate(0.5, 0.0, 0.0);
        w.addObject(goldFishPtr);

	// plane
        PlaneChecker checkerPtr = new PlaneChecker();
        checkerPtr.setSize(0.5);
        checkerPtr.setOutlineWidth(0.05);
        checkerPtr.setColor1(new RGBColor(0.75));
        checkerPtr.setColor2(new RGBColor(0.75));
        checkerPtr.setOutlineColor(new RGBColor(0.45));

        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.5);
        svMattePtr.setKd(0.65);
        svMattePtr.setCd(checkerPtr);

        Plane planePtr
                = new Plane(new Point3D(0, -1.51, 0), new Normal(0, 1, 0));
        planePtr.setMaterial(svMattePtr);
        w.addObject(planePtr);
    }

}
