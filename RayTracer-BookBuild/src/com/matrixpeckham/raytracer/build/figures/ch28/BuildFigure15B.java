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
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Dielectric;
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
public class BuildFigure15B implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 28.15(b)
        int numSamples = 25;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setSamples(numSamples);
        w.vp.setMaxDepth(7);

        w.backgroundColor = new RGBColor(0.0, 0.3, 0.25);  // blue green

        w.tracer = new Whitted(w);

        Ambient ambientPtr = new Ambient();
        ambientPtr.scaleRadiance(0.25);
        w.setAmbient(ambientPtr);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(0.5, 40.0, 0.5);
        pinholePtr.setLookat(0.5, 0.0, 0.5);
        pinholePtr.setViewDistance(3000.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        Directional lightPtr = new Directional();
        lightPtr.setDirection(0, 1, 0);      // straight down
        lightPtr.scaleRadiance(7.5);
        lightPtr.setShadows(false);
        w.addLight(lightPtr);

        // nested spheres
        RGBColor glassColor = new RGBColor(0.95, 0.95, 1);  	// faint blue
        RGBColor diamondColor = new RGBColor(1, 1, 0.8);  	// lemon
        RGBColor waterColor = new RGBColor(1, 0.5, 1);  		// mauve

        Point3D center = new Point3D(0.5, 0, 0.5);  			// common centre

        // outer sphere - glass
        Dielectric glassPtr = new Dielectric();
        glassPtr.setKs(0.1);
        glassPtr.setExp(2000.0);
        glassPtr.setIorIn(1.5); 			// water
        glassPtr.setIorOut(1.0);			// air
        glassPtr.setCfIn(glassColor);
        glassPtr.setCfOut(Utility.WHITE);

        Sphere spherePtr1 = new Sphere(center, 3.5);
        spherePtr1.setMaterial(glassPtr);
        w.addObject(spherePtr1);

        // middle sphere - diamond
        Dielectric diamondPtr = new Dielectric();
        diamondPtr.setKs(0.1);
        diamondPtr.setExp(2000.0);
        diamondPtr.setIorIn(2.42); 			// diamond
        diamondPtr.setIorOut(1.5);			// glass
        diamondPtr.setCfIn(diamondColor);
        diamondPtr.setCfOut(glassColor);

        Sphere spherePtr2 = new Sphere(center, 2.0);
        spherePtr2.setMaterial(diamondPtr);
        w.addObject(spherePtr2);

        // inner sphere - water
        Dielectric waterPtr = new Dielectric();
        waterPtr.setKs(0.1);
        waterPtr.setExp(2000.0);
        waterPtr.setIorIn(1.33); 			// water
        waterPtr.setIorOut(2.42); 			// diamond
        waterPtr.setCfIn(waterColor);
        waterPtr.setCfOut(diamondColor);

        Sphere spherePtr3 = new Sphere(center, 0.6);
        spherePtr3.setMaterial(waterPtr);
        w.addObject(spherePtr3);

        // ground plane
        Checker3D checkerPtr = new Checker3D();
        checkerPtr.setSize(2.0);
        checkerPtr.setColor1(0.25);
        checkerPtr.setColor2(Utility.WHITE);

        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.5);
        svMattePtr.setKd(0.35);
        svMattePtr.setCd(checkerPtr);

        Plane planePtr = new Plane(new Point3D(0, -6.5, 0), new Normal(0, 1, 0));
        planePtr.setMaterial(svMattePtr);
        w.addObject(planePtr);
    }

}
