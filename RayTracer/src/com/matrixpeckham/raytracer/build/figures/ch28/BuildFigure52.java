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
import com.matrixpeckham.raytracer.geometricobjects.compound.Compound;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConcavePartSphere;
import com.matrixpeckham.raytracer.geometricobjects.primitives.CutFace;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Rectangle;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Dielectric;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import static com.matrixpeckham.raytracer.util.Utility.PI_ON_180;
import com.matrixpeckham.raytracer.util.Vector3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure52 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 28.52
// The object is a transparent cube with spherical caps cut from four faces
// There is a reflective sphere at the center
// The class CutFace is in the Chapter 28 download
// The cube has edge length 4. This should be defined as a variable
// This build function (deliberately) renders a bit darker than the image in the book
// The inspiration came from the acrylic sculpures "Titiopoli's Ligththouse" and "Dodecker" by the American
// sculptor Bruce Beasley
        int numSamples = 25;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setSamples(numSamples);
        w.vp.setMaxDepth(12);

        w.backgroundColor = new RGBColor(0.8, 0.9, 1);

        w.tracer = new Whitted(w);

        Ambient ambientPtr = new Ambient();
        ambientPtr.scaleRadiance(0.5);
        w.setAmbient(ambientPtr);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(10, -1, 4);
        pinholePtr.setLookat(0, 0, 0);
        pinholePtr.setViewDistance(700.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        PointLight lightPtr = new PointLight();
        lightPtr.setLocation(10, 30, 30);
        lightPtr.scaleRadiance(3.0);
        lightPtr.setShadows(true);
        w.addLight(lightPtr);

        // reflective unit sphere
        Reflective reflectivePtr = new Reflective();
        reflectivePtr.setKa(1.0);
        reflectivePtr.setKd(0.5);
        reflectivePtr.setCd(1, 0.5, 0);			// orange
        reflectivePtr.setKs(0.5);
        reflectivePtr.setExp(500.0);
        reflectivePtr.setKr(0.5);
        reflectivePtr.setShadows(false);

        Sphere spherePtr = new Sphere();
        spherePtr.setMaterial(reflectivePtr);

        Dielectric glassPtr = new Dielectric();
        glassPtr.setIorIn(1.5);
        glassPtr.setIorOut(1.0);
        double c = 0.9;
        glassPtr.setCfIn(0.9 * c, 1 * c, 0.85 * c);
        glassPtr.setCfOut(Utility.WHITE);

        // concave cap
        double radius = 1.5;
        double minAngle = 115;
        double maxAngle = 180;
        double displacement = -radius * cos(minAngle * PI_ON_180);
        double rimRadius = radius * sin(minAngle * PI_ON_180);
        Point3D center = new Point3D(0.0);

        ConcavePartSphere partSpherePtr = new ConcavePartSphere(center, radius,
                0, 360, minAngle, maxAngle);

        Instance concaveCapPtr = new Instance(partSpherePtr);
        concaveCapPtr.translate(0, displacement, 0);

        CutFace cutFacePtr = new CutFace(4.0, rimRadius);

        // put these two objects in a compound object so that they can be transformed together
        Compound facetPtr = new Compound();
        facetPtr.addObject(concaveCapPtr);
        facetPtr.addObject(cutFacePtr);
        facetPtr.setMaterial(glassPtr);

	// define the six cube faces
        // front face (+ve z)
        Instance frontPtr = new Instance(facetPtr);
        frontPtr.rotateX(90);
        frontPtr.translate(0, 0, 2);

        // back face (-ve z)
        Instance backPtr = new Instance(facetPtr);
        backPtr.rotateX(-90);
        backPtr.translate(0, 0, -2);

        // bottom face (-ve y)
        Instance bottomPtr = new Instance(facetPtr);
        bottomPtr.rotateZ(180);
        bottomPtr.translate(0, -2, 0);

        // top face (+ve y)
        Instance topPtr = new Instance(facetPtr);
        topPtr.translate(0, 2, 0);

        // a rectangle for the left and right cube faces
        Point3D p0 = new Point3D(-2.0, 0.0, -2.0);
        Vector3D a = new Vector3D(0.0, 0.0, 4.0);
        Vector3D b = new Vector3D(4.0, 0.0, 0.0);
        Rectangle rect = new Rectangle(p0, a, b);
        rect.setMaterial(glassPtr);

        // left face (-ve x)
        Instance leftPtr = new Instance(rect);
        leftPtr.rotateZ(90);
        leftPtr.translate(-2, 0, 0);

        // right face (+ve x)
        Instance rightPtr = new Instance(rect);
        rightPtr.rotateZ(-90);
        rightPtr.translate(2, 0, 0);

        // put all six faces into a compound object so we can rotate it
        Compound cubePtr = new Compound();
        cubePtr.addObject(frontPtr);			// facet
        cubePtr.addObject(backPtr);				// facet
        cubePtr.addObject(topPtr);				// facet
        cubePtr.addObject(bottomPtr);			// facet
        cubePtr.addObject(leftPtr);				// rectangle
        cubePtr.addObject(rightPtr);			// rectangle
        cubePtr.addObject(spherePtr);

        Instance cubePtr2 = new Instance(cubePtr);
        cubePtr2.rotateZ(45);
        cubePtr2.rotateX(35);
        w.addObject(cubePtr2);

        // ground plane with checker
        Checker3D checkerPtr = new Checker3D();
        checkerPtr.setSize(1.0);
        checkerPtr.setColor1(Utility.WHITE);
        checkerPtr.setColor2(0.5);

        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.5);
        svMattePtr.setKd(0.75);
        svMattePtr.setCd(checkerPtr);

        Plane planePtr1 = new Plane(new Point3D(0, -4.01, 0),
                new Normal(0, 1, 0));
        planePtr1.setMaterial(svMattePtr);
        w.addObject(planePtr1);
    }

}
