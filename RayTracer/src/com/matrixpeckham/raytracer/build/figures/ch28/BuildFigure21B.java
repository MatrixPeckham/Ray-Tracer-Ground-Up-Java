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
import com.matrixpeckham.raytracer.geometricobjects.primitives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
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
public class BuildFigure21B implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 28.21(b)
// In w build function the radiance of the point light is half the value
// in Figure 28.21(a), and the diffuse reflection of
// the plane is twice as large. This keeps the diffuse reflections of the plane,
// and the sphere in the box, approximately the same in both figures.
// The light's radiance has to be adjusted because of the "eta squares" factor
// in Equation 27.8 is different in each figure. This affects the shading of the
// part of the sphere that's inside the box.
// This also affects the sphere inside the sphere in Figures 28.28 and 28.29, and
// 28.47, the straw in Figure 28.38, and the goldfish in Figure 28.41.
        int numSamples = 25;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setSamples(numSamples);
        w.vp.setMaxDepth(5);

        w.backgroundColor = new RGBColor(0.9, 0.9, 1);  // pale blue

        w.tracer = new Whitted(w);

        Ambient ambientPtr = new Ambient();
        ambientPtr.scaleRadiance(0.5);
        w.setAmbient(ambientPtr);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(0, 1.5, 4);
        pinholePtr.setLookat(new Point3D(0.0));
        pinholePtr.setViewDistance(675.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        PointLight lightPtr = new PointLight();
        lightPtr.setLocation(10, 20, 20);
        lightPtr.scaleRadiance(7.5);
        lightPtr.setShadows(false);
        w.addLight(lightPtr);

        // matte sphere inside cube
        Matte mattePtr = new Matte();
        mattePtr.setKa(0.5);
        mattePtr.setKd(0.5);
        mattePtr.setCd(0.0, 0.25, 1.0);

        Sphere spherePtr = new Sphere(new Point3D(0.0, -0.25, -1.0), 0.5);
        spherePtr.setMaterial(mattePtr);
        w.addObject(spherePtr);

        // transparent cube
        RGBColor glassColor = new RGBColor(0.64, 0.98, 0.88);	// light cyan

        Dielectric glassPtr = new Dielectric();
        glassPtr.setExp(2000.0);
        glassPtr.setIorIn(1.5);					// glass
        glassPtr.setIorOut(1.33);				// water
        glassPtr.setCfIn(glassColor);
        glassPtr.setCfOut(Utility.WHITE);
        glassPtr.setShadows(false);

        Box boxPtr = new Box(new Point3D(-1.0), new Point3D(1.0));
        boxPtr.setMaterial(glassPtr);
        w.addObject(boxPtr);

        // plane
        Checker3D checkerPtr = new Checker3D();
        checkerPtr.setSize(4.0);
        checkerPtr.setColor1(1, 1, 0.4);    		// yellow
        checkerPtr.setColor2(1, 0.5, 0);   		// orange

        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.5);
        svMattePtr.setKd(0.2);
        svMattePtr.setCd(checkerPtr);

        Plane planePtr
                = new Plane(new Point3D(0, -10.1, 0), new Normal(0, 1, 0));
        planePtr.setMaterial(svMattePtr);
        w.addObject(planePtr);
    }

}
