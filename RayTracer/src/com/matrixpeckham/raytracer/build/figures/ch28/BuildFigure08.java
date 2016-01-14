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

import com.matrixpeckham.raytracer.cameras.Orthographic;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.compound.SolidCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.materials.Dielectric;
import com.matrixpeckham.raytracer.materials.Emissive;
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
public class BuildFigure08 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 28.8
        int numSamples = 9;

        w.vp.setHres(400);
        w.vp.setVres(400);
        w.vp.setMaxDepth(6);   // depth 6 is necessary to get rays through the three cylinders
        w.vp.setPixelSize(0.012);
        w.vp.setSamples(numSamples);

        w.backgroundColor = Utility.BLACK;

        w.tracer = new Whitted(w);

	// camera looks down y axis because disks have vertical axes	
        Orthographic orthographicPtr = new Orthographic();
        orthographicPtr.setEye(0.23, 100, 0);
        orthographicPtr.setLookat(0.23, 0, 0);
        orthographicPtr.computeUVW();
        w.setCamera(orthographicPtr);

	// top cylinder (in image)
        double top1 = 0.5;
        double bottom1 = 0.0;
        double radius = 1.25;

        RGBColor glassColor1=new RGBColor(0, 1, 1);    // cyan
	
	Dielectric glassPtr1 = new Dielectric();
        glassPtr1.setIorIn(1.5);		// glass
        glassPtr1.setIorOut(1.0);		// air
        glassPtr1.setCfIn(glassColor1);
        glassPtr1.setCfOut(Utility.WHITE);

        Instance cylinderPtr1 = new Instance(new SolidCylinder(top1, bottom1,
                radius));
        cylinderPtr1.translate(1, 0, 0);
        cylinderPtr1.setMaterial(glassPtr1);
        w.addObject(cylinderPtr1);

	// left cylinder
        double top2 = 1.5;
        double bottom2 = 1;

        RGBColor glassColor2=new RGBColor(1, 1, 0);   // yellow
	
	Dielectric glassPtr2 = new Dielectric();
        glassPtr2.setIorIn(1.5);		// glass
        glassPtr2.setIorOut(1.0);		// air
        glassPtr2.setCfIn(glassColor2);
        glassPtr2.setCfOut(Utility.WHITE);

        Instance cylinderPtr2 = new Instance(new SolidCylinder(top2, bottom2,
                radius));
        cylinderPtr2.translate(-0.5, 0, -0.866);
        cylinderPtr2.setMaterial(glassPtr2);
        w.addObject(cylinderPtr2);

	// right cylinder
        double top3 = 2.5;
        double bottom3 = 2;

        RGBColor glassColor3=new RGBColor(1, 0, 1);    // majenta
	
	Dielectric glassPtr3 = new Dielectric();
        glassPtr3.setIorIn(1.5);		// glass
        glassPtr3.setIorOut(1.0);		// air
        glassPtr3.setCfIn(glassColor3);
        glassPtr3.setCfOut(Utility.WHITE);

        Instance cylinderPtr3 = new Instance(new SolidCylinder(top3, bottom3,
                radius));
        cylinderPtr3.translate(-0.5, 0, 0.866);
        cylinderPtr3.setMaterial(glassPtr3);
        w.addObject(cylinderPtr3);

	// Utility.WHITE plane below disks
        Emissive emissivePtr = new Emissive();
        emissivePtr.scaleRadiance(1.0);		// default
        emissivePtr.setCe(Utility.WHITE);			// default

        Plane planePtr = new Plane(new Point3D(0, -1, 0),new Normal(0, 1, 0));
        planePtr.setMaterial(emissivePtr);
        w.addObject(planePtr);
    }

}
