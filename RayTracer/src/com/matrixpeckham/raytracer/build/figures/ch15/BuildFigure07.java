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
package com.matrixpeckham.raytracer.build.figures.ch15;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.beveledobjects.BeveledCylinder;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Phong;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure07 implements BuildWorldFunction {

    @Override
    public void build(World w) {
	// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 15.7
// Instances are discussed in Section 21.4
// Beveled cylinders are discussed in Section 21.5
        int numSamples = 1;

        w.vp.setHres(650);
        w.vp.setVres(300);
        w.vp.setSamples(numSamples);

        w.tracer = new RayCast(w);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(0, 0, 100);
        pinholePtr.setLookat(0, 0, 0);
        pinholePtr.setViewDistance(6000);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        Directional lightPtr2 = new Directional();
        lightPtr2.setDirection(20, 0, 20);
        lightPtr2.scaleRadiance(3.0);
        w.addLight(lightPtr2);

	// beveled cylinder
        double bottom = -2.0;
        double top = 2.0;
        double radius = 1.0;
        double bevelRadius = 0.2;

        BeveledCylinder cylinderPtr1 = new BeveledCylinder(bottom, top, radius,
                bevelRadius);

        for (int j = 0; j < 4; j++) {
            float exp=1;

            if (j == 0) {
                exp = 5;
            }
            if (j == 1) {
                exp = 20;
            }
            if (j == 2) {
                exp = 100;
            }
            if (j == 3) {
                exp = 1000;
            }

            Phong phongPtr = new Phong();
            phongPtr.setKa(0.25);
            phongPtr.setKd(0.6);
            phongPtr.setCd(new RGBColor(0.5));
            phongPtr.setKs(0.2);
            phongPtr.setExp(exp);

            Instance cylinderPtr2 = new Instance(cylinderPtr1);
            cylinderPtr2.translate(-3.75 + 2.5 * j, 0, 0);
            cylinderPtr2.setMaterial(phongPtr);
            w.addObject(cylinderPtr2);
        }

    }

}
