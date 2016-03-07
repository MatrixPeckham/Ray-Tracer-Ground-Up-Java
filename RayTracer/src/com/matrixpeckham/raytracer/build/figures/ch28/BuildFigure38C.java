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
import com.matrixpeckham.raytracer.geometricobjects.compound.GlassOfWater;
import com.matrixpeckham.raytracer.geometricobjects.primitives.OpenCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Dielectric;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
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
public class BuildFigure38C implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 28.38(c)
// This takes a long time to render
        int numSamples = 9;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setSamples(numSamples);
        w.vp.setMaxDepth(10);

        w.backgroundColor = new RGBColor(0.5);

        w.tracer = new Whitted(w);

        Ambient ambientPtr = new Ambient();
        ambientPtr.scaleRadiance(0.5);
        w.setAmbient(ambientPtr);

        Pinhole pinholePtr = new Pinhole();

        pinholePtr.setEye(5, 6, 10);
        pinholePtr.setLookat(0, 1, 0);
        pinholePtr.setViewDistance(2000.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        PointLight lightPtr1 = new PointLight();
        lightPtr1.setLocation(40, 50, 30);
        lightPtr1.scaleRadiance(3.0);
        lightPtr1.setShadows(true);
        w.addLight(lightPtr1);

	// materials for the glass of water
        // glass-air boundary
        RGBColor glassColor = new RGBColor(0.65, 1, 0.75);
        RGBColor waterColor = new RGBColor(1, 0.25, 1);

        Dielectric glassPtr = new Dielectric();
        glassPtr.setIorIn(1.50);			// glass
        glassPtr.setIorOut(1.0);			// air
        glassPtr.setCfIn(glassColor);
        glassPtr.setCfOut(Utility.WHITE);

        // water-air boundary
        Dielectric waterPtr = new Dielectric();
        waterPtr.setIorIn(1.33);			// water
        waterPtr.setIorOut(1.0);			// air
        waterPtr.setCfIn(waterColor);
        waterPtr.setCfOut(Utility.WHITE);

        // water-glass boundary
        Dielectric dielectricPtr = new Dielectric();
        dielectricPtr.setIorIn(1.33); 		// water
        dielectricPtr.setIorOut(1.50); 		// glass
        dielectricPtr.setCfIn(waterColor);
        dielectricPtr.setCfOut(glassColor);

        // Define the GlassOfWater object
        // The parameters below are the default values, but using the constructor that
        // takes these as arguments makes it easier to experiment with different values
        double height = 2.0;
        double innerRadius = 0.9;
        double wallThickness = 0.1;
        double baseThickness = 0.3;
        double waterHeight = 1.5;
        double meniscusRadius = 0.1;

        GlassOfWater glassOfWaterPtr = new GlassOfWater(height,
                innerRadius,
                wallThickness,
                baseThickness,
                waterHeight,
                meniscusRadius);

        glassOfWaterPtr.setGlassAirMaterial(glassPtr);
        glassOfWaterPtr.setWaterAirMaterial(waterPtr);
        glassOfWaterPtr.setWaterGlassMaterial(dielectricPtr);
        w.addObject(glassOfWaterPtr);

        // define the straw
        Matte mattePtr = new Matte();
        mattePtr.setCd(1, 1, 0);
        mattePtr.setKa(0.25);
        mattePtr.setKd(0.65);
        mattePtr.setShadows(false);  // there are no shadows cast on the straw

        Instance strawPtr = new Instance(new OpenCylinder(-1.2, 1.7, 0.05));
        strawPtr.setMaterial(mattePtr);
        strawPtr.rotateZ(40);
        strawPtr.translate(0, 1.25, 0);
        w.addObject(strawPtr);

        // ground plane
        Checker3D checkerPtr = new Checker3D();
        checkerPtr.setSize(0.5);
        checkerPtr.setColor1(0.75);
        checkerPtr.setColor2(Utility.WHITE);

        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.5);
        svMattePtr.setKd(0.75);
        svMattePtr.setCd(checkerPtr);

        Plane planePtr
                = new Plane(new Point3D(0, -0.01, 0), new Normal(0, 1, 0));
        planePtr.setMaterial(svMattePtr);
        w.addObject(planePtr);
    }

}
