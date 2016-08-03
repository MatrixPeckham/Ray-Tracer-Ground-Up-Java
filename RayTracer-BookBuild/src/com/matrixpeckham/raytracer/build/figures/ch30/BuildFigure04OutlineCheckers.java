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
import com.matrixpeckham.raytracer.geometricobjects.compound.Compound;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Disk;
import com.matrixpeckham.raytracer.geometricobjects.primitives.OpenCone;
import com.matrixpeckham.raytracer.geometricobjects.primitives.OpenCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Rectangle;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.ConeChecker;
import com.matrixpeckham.raytracer.textures.procedural.CylinderChecker;
import com.matrixpeckham.raytracer.textures.procedural.DiskChecker;
import com.matrixpeckham.raytracer.textures.procedural.PlaneChecker;
import com.matrixpeckham.raytracer.textures.procedural.RectangleChecker;
import com.matrixpeckham.raytracer.textures.procedural.SphereChecker;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure04OutlineCheckers implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 30.4
// The objects are a sphere, cylinder, cone, box and plane with 2D checker textures.
// These textures can only be applied to generic objects.
// In w scene the line widths are all zero so that the checkers are not outlined.
// The Chapter 30 download has a version of w scene where the checkers rendered with colored outlines.
        int numSamples = 16;

        w.vp.setHres(880);
        w.vp.setVres(300);
        w.vp.setSamples(numSamples);

        w.tracer = new RayCast(w);
        w.backgroundColor = new RGBColor(0.5);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(100, 100, 300);
        pinholePtr.setLookat(-0.2, 1.0, 0);
        pinholePtr.setViewDistance(12000.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        Directional lightPtr1 = new Directional();
        lightPtr1.setDirection(0, 0, 1);
        lightPtr1.scaleRadiance(1.5);
        w.addLight(lightPtr1);

        // sphere
        SphereChecker sphereCheckerPtr = new SphereChecker();
        sphereCheckerPtr.setNumHorizontalCheckers(16);
        sphereCheckerPtr.setNumVerticalCheckers(8);
        sphereCheckerPtr.setHorizontalLineWidth(0.1);
        sphereCheckerPtr.setVerticalLineWidth(0.25);
        sphereCheckerPtr.setColor1(Utility.WHITE);
        sphereCheckerPtr.setColor2(Utility.BLACK);
        sphereCheckerPtr.setLineColor(Utility.RED);

        SV_Matte svMattePtr1 = new SV_Matte();
        svMattePtr1.setKa(0.75);
        svMattePtr1.setKd(0.75);
        svMattePtr1.setCd(sphereCheckerPtr);

        Instance spherePtr = new Instance(new Sphere());
        spherePtr.setMaterial(svMattePtr1);
        spherePtr.scale(2.5);
        spherePtr.translate(-9.5, -1, 0);
        w.addObject(spherePtr);

	// cylinder
        // This must be built out of separate parts so that we can have different textures
        // on the top, bottom, and curved surfaces
        // material for the curved surface
        CylinderChecker cylinderCheckerPtr = new CylinderChecker();
        cylinderCheckerPtr.setNumHorizontalCheckers(12);
        cylinderCheckerPtr.setNumVerticalCheckers(6);
        cylinderCheckerPtr.setHorizontalLineWidth(0.1);
        cylinderCheckerPtr.setVerticalLineWidth(0.2);
        cylinderCheckerPtr.setColor1(Utility.WHITE);
        cylinderCheckerPtr.setColor2(Utility.BLACK);
        cylinderCheckerPtr.setLineColor(Utility.RED);

        SV_Matte svMattePtr2 = new SV_Matte();
        svMattePtr2.setKa(0.75);
        svMattePtr2.setKd(0.75);
        svMattePtr2.setCd(cylinderCheckerPtr);

        OpenCylinder curvedSurfacePtr = new OpenCylinder();
        curvedSurfacePtr.setMaterial(svMattePtr2);

        // material for the top surface
        DiskChecker diskCheckerPtr = new DiskChecker();
        diskCheckerPtr.setNumAngularCheckers(12);
        diskCheckerPtr.setNumRadialCheckers(3);
        diskCheckerPtr.setAngularLineWidth(0.1);
        diskCheckerPtr.setRadialLineWidth(0.2);
        diskCheckerPtr.setColor1(Utility.WHITE);
        diskCheckerPtr.setColor2(Utility.BLACK);
        diskCheckerPtr.setLineColor(Utility.GREEN);

        SV_Matte svMattePtr5 = new SV_Matte();
        svMattePtr5.setKa(0.75);
        svMattePtr5.setKd(0.75);
        svMattePtr5.setCd(diskCheckerPtr);

        Instance topPtr = new Instance(new Disk());
        topPtr.setMaterial(svMattePtr5);
        topPtr.translate(0, 1, 0);

        // I haven't included the bottom of the cylinder
        Compound genericCylinderPtr = new Compound();
        genericCylinderPtr.addObject(curvedSurfacePtr);
        genericCylinderPtr.addObject(topPtr);

        Instance cylinderPtr = new Instance(genericCylinderPtr);
        cylinderPtr.scale(1.95, 2.95, 1.95);
        cylinderPtr.translate(-3.5, 0.5, 0);
        w.addObject(cylinderPtr);

        // cone
        ConeChecker coneCheckerPtr = new ConeChecker();
        coneCheckerPtr.setNumHorizontalCheckers(12);
        coneCheckerPtr.setNumVerticalCheckers(8);
        coneCheckerPtr.setHorizontalLineWidth(0.25);
        coneCheckerPtr.setVerticalLineWidth(0.1);
        coneCheckerPtr.setColor1(Utility.WHITE);
        coneCheckerPtr.setColor2(Utility.BLACK);
        coneCheckerPtr.setLineColor(Utility.RED);

        SV_Matte svMattePtr3 = new SV_Matte();
        svMattePtr3.setKa(0.75);
        svMattePtr3.setKd(0.75);
        svMattePtr3.setCd(coneCheckerPtr);

        // I haven't included the bottom of the cone
        Instance conePtr = new Instance(new OpenCone());
        conePtr.setMaterial(svMattePtr3);
        conePtr.scale(2.5);
        conePtr.translate(2.35, -1.5, 0);
        w.addObject(conePtr);

	// box
        // This has to have a separate Rectangle object for each face.
        // A default rectangle is (x, z) in the w.set [-1, 1] X [-1, 1], y = 0.
        // We need separate checker textures on each face to make the colors and the
        // line thicknesses match up
        // front (+ve z) face
        RectangleChecker rectangleCheckerPtr1 = new RectangleChecker();
        rectangleCheckerPtr1.setNumXCheckers(4);
        rectangleCheckerPtr1.setNumZCheckers(4);
        rectangleCheckerPtr1.setXLineWidth(0.15);
        rectangleCheckerPtr1.setZLineWidth(0.25);
        rectangleCheckerPtr1.setColor1(Utility.WHITE);
        rectangleCheckerPtr1.setColor2(Utility.BLACK);
        rectangleCheckerPtr1.setLineColor(Utility.RED);

        SV_Matte svMattePtr4 = new SV_Matte();
        svMattePtr4.setKa(0.75);
        svMattePtr4.setKd(0.75);
        svMattePtr4.setCd(rectangleCheckerPtr1);

        Instance frontFacePtr = new Instance(new Rectangle());
        frontFacePtr.setMaterial(svMattePtr4);
        frontFacePtr.rotateX(90);
        frontFacePtr.translate(0, 0, 1);

        // top (+ve y) face
        RectangleChecker rectangleCheckerPtr2 = new RectangleChecker();
        rectangleCheckerPtr2.setNumXCheckers(4);
        rectangleCheckerPtr2.setNumZCheckers(4);
        rectangleCheckerPtr2.setXLineWidth(0.15);
        rectangleCheckerPtr2.setZLineWidth(0.15);
        rectangleCheckerPtr2.setColor1(Utility.BLACK);
        rectangleCheckerPtr2.setColor2(Utility.WHITE);
        rectangleCheckerPtr2.setLineColor(Utility.GREEN);

        SV_Matte svMattePtr6 = new SV_Matte();
        svMattePtr6.setKa(0.75);
        svMattePtr6.setKd(0.75);
        svMattePtr6.setCd(rectangleCheckerPtr2);

        Instance topFacePtr = new Instance(new Rectangle());
        topFacePtr.setMaterial(svMattePtr6);
        topFacePtr.translate(0, 1, 0);

        // right (+ve x) face
        RectangleChecker rectangleCheckerPtr3 = new RectangleChecker();
        rectangleCheckerPtr3.setNumXCheckers(4);
        rectangleCheckerPtr3.setNumZCheckers(4);
        rectangleCheckerPtr3.setXLineWidth(0.25);
        rectangleCheckerPtr3.setZLineWidth(0.15);
        rectangleCheckerPtr3.setColor1(Utility.WHITE);
        rectangleCheckerPtr3.setColor2(Utility.BLACK);
        rectangleCheckerPtr3.setLineColor(Utility.BLUE);

        SV_Matte svMattePtr7 = new SV_Matte();
        svMattePtr7.setKa(0.75);
        svMattePtr7.setKd(0.75);
        svMattePtr7.setCd(rectangleCheckerPtr3);

        Instance rightFacePtr = new Instance(new Rectangle());
        rightFacePtr.setMaterial(svMattePtr7);
        rightFacePtr.rotateZ(-90);
        rightFacePtr.translate(1, 0, 0);

        // you can construct the left, back, and bottom faces in a similar way
        Compound genericBoxPtr = new Compound();
        genericBoxPtr.addObject(frontFacePtr);
        genericBoxPtr.addObject(topFacePtr);
        genericBoxPtr.addObject(rightFacePtr);

        Instance boxPtr = new Instance(genericBoxPtr);
        boxPtr.scale(2.0);
        boxPtr.translate(9, 1, 0);
        w.addObject(boxPtr);

        // ground plane
        PlaneChecker planeCheckerPtr = new PlaneChecker();
        planeCheckerPtr.setSize(1.5);
        planeCheckerPtr.setOutlineWidth(0.0);
        planeCheckerPtr.setColor1(new RGBColor(0.35));
        planeCheckerPtr.setColor2(new RGBColor(0.5));
        planeCheckerPtr.setOutlineColor(Utility.YELLOW);

        SV_Matte svMattePtr8 = new SV_Matte();
        svMattePtr8.setKa(0.75);
        svMattePtr8.setKd(0.75);
        svMattePtr8.setCd(planeCheckerPtr);

        Instance planePtr = new Instance(new Plane()); // a generic plane is the (x, z) plane
        planePtr.setMaterial(svMattePtr8);
        planePtr.translate(0, -4.5, 0);
        w.addObject(planePtr);
    }

}
