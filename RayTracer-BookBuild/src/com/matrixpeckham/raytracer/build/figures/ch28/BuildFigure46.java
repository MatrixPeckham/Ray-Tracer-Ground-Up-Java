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
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Dielectric;
import com.matrixpeckham.raytracer.materials.DoubleSidedEmissive;
import com.matrixpeckham.raytracer.materials.SV_Emissive;
import com.matrixpeckham.raytracer.materials.SphereMaterials;
import com.matrixpeckham.raytracer.textures.procedural.SphereChecker;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure46 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 28.26
        int numSamples = 16;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setSamples(numSamples);
        w.vp.setMaxDepth(10);

        w.backgroundColor = Utility.BLACK;

        w.tracer = new Whitted(w);

        Ambient ambientPtr = new Ambient();
        ambientPtr.scaleRadiance(0.25);
        w.setAmbient(ambientPtr);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(-1.45, 25, 22.5);
        pinholePtr.setLookat(new Point3D(0.0));
        pinholePtr.setViewDistance(3000);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        // point light
        PointLight lightPtr1 = new PointLight();
        lightPtr1.setLocation(40, 50, 0);
        lightPtr1.scaleRadiance(5.0);
        lightPtr1.setShadows(true);
        w.addLight(lightPtr1);

        // point light
        PointLight lightPtr2 = new PointLight();
        lightPtr2.setLocation(-10, 20, 10);
        lightPtr2.scaleRadiance(4.0);
        lightPtr2.setShadows(true);
        w.addLight(lightPtr2);

        // directional light
        Directional lightPtr3 = new Directional();
        lightPtr3.setDirection(-1, 0, 0);
        lightPtr3.scaleRadiance(4.0);
        lightPtr3.setShadows(true);
        w.addLight(lightPtr3);

	// checker sphere
        // 2D sphere checker texture
        SphereChecker checkerPtr1 = new SphereChecker();
        checkerPtr1.setNumHorizontal(12);
        checkerPtr1.setNumVertical(12);
        checkerPtr1.setHorizontalLineWidth(0.075);
        checkerPtr1.setVerticalLineWidth(0.075);
        checkerPtr1.setColor1(Utility.WHITE);
        checkerPtr1.setColor2(new RGBColor(0.75));
        checkerPtr1.setLineColor(Utility.BLACK);

        // spatially varying self-emissive material
        SV_Emissive svEmissivePtr = new SV_Emissive();
        svEmissivePtr.scaleRadiance(20.0);
        svEmissivePtr.setCe(checkerPtr1);

        Sphere spherePtr1 = new Sphere();
        spherePtr1.setMaterial(svEmissivePtr);

        Instance bigSpherePtr = new Instance(spherePtr1);
        bigSpherePtr.scale(30.0);
        bigSpherePtr.translate(0, 50, 45);
        w.addObject(bigSpherePtr);

	// transparent sphere with grid lines
        // emissive material for the grid lines
        DoubleSidedEmissive emissivePtr = new DoubleSidedEmissive();
        emissivePtr.scaleRadiance(0.85);
        emissivePtr.setCe(1, 0.7, 0);		// orange

        // dielectric material for the checkers
        Dielectric glassPtr = new Dielectric();
        glassPtr.setKs(0.2);
        glassPtr.setExp(1000.0);
        glassPtr.setIorIn(1.5);
        glassPtr.setIorOut(1.0);
        glassPtr.setCfIn(0.5, 0.95, 0.92);
        glassPtr.setCfOut(Utility.WHITE);

        // SphereMaterials material to store the grid lines and the transparent checkers
        // See the Figure 24.15(c) build function for information on w material
        SphereMaterials sphereMaterialsPtr = new SphereMaterials();
        sphereMaterialsPtr.setNumHorizontal(12);
        sphereMaterialsPtr.setNumVertical(6);
        sphereMaterialsPtr.setLineWidth(0.03);
        sphereMaterialsPtr.setChecker1Material(glassPtr);
        sphereMaterialsPtr.setChecker2Material(glassPtr);
        sphereMaterialsPtr.setLineMaterial(emissivePtr);

        Sphere spherePtr3 = new Sphere();
        spherePtr3.setMaterial(sphereMaterialsPtr);

        Instance spherePtr4 = new Instance(spherePtr3);
        spherePtr4.scale(3.0);
        w.addObject(spherePtr4);
    }

}
