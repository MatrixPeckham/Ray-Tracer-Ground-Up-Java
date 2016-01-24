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
package com.matrixpeckham.raytracer.build.figures.ch30;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.compound.SolidCylinder;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;
import static java.awt.Color.white;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure12 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 30.12
        int numSamples = 16;

        w.vp.setHres(904);
        w.vp.setVres(300);
        w.vp.setSamples(numSamples);

        w.tracer = new RayCast(w);
        w.backgroundColor = new RGBColor(0.5);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(0, 125, 200);
        pinholePtr.setLookat(new Point3D(0.0));
        pinholePtr.setViewDistance(4400.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        Directional lightPtr = new Directional();
        lightPtr.setDirection(1, 1, 0.5);
        lightPtr.scaleRadiance(4.5);
        w.addLight(lightPtr);

	// middle cylinder (generic)
        Checker3D checkerPtr = new Checker3D();
        checkerPtr.setSize(1.02);
        checkerPtr.setColor1(Utility.BLACK);
        checkerPtr.setColor2(Utility.WHITE);

        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.4);
        svMattePtr.setKd(0.6);
        svMattePtr.setCd(checkerPtr);

        SolidCylinder cylinderPtr1 = new SolidCylinder();
        cylinderPtr1.setMaterial(svMattePtr);
        w.addObject(cylinderPtr1);

	// right cylinder (cylinder and checkers are transformed)
        Instance transformedCylinderPtr1 = new Instance(new SolidCylinder());
        transformedCylinderPtr1.setMaterial(svMattePtr);
        transformedCylinderPtr1.scale(10.0, 0.5, 10.0);
        transformedCylinderPtr1.translate(13, 0, 0);
        w.addObject(transformedCylinderPtr1);

	// left cylinder (only the cylinder is transformed)
        Instance transformedCylinderPtr2 = new Instance(new SolidCylinder());
        transformedCylinderPtr2.setMaterial(svMattePtr);
        transformedCylinderPtr2.scale(10.0, 0.5, 10.0);
        transformedCylinderPtr2.translate(-13, 0, 0);
        transformedCylinderPtr2.setTransformTexture(false);
        w.addObject(transformedCylinderPtr2);
    }

}
