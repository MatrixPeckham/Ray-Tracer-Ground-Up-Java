/*
 Copyright (C) 2015 William Matrix Peckham
 *
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.
 *
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 *
 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.matrixpeckham.raytracer.build.figures.ch16;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.compound.SolidCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.geometricobjects.triangles.Triangle;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure01 implements BuildWorldFunction {

    @Override
    public void build(World w) {
        int numSamples = 1;

        w.vp.setHres(300);
        w.vp.setVres(200);
        w.vp.setSamples(numSamples);

        w.tracer = new RayCast(w);

        Pinhole cameraPtr = new Pinhole();
        cameraPtr.setEye(2, 2.5, 15);
        cameraPtr.setLookat(0 + 3, 2.5, 0);
        cameraPtr.setViewDistance(400);
        cameraPtr.computeUVW();
        w.setCamera(cameraPtr);

        Directional lightPtr1 = new Directional();
        lightPtr1.setDirection(200, 150, 125);
        lightPtr1.scaleRadiance(4.0);
//	lightPtr1.setShadows(true);				// for Figure 16.1(b)
        w.addLight(lightPtr1);

        PointLight lightPtr2 = new PointLight();
        lightPtr2.setLocation(-12, 15, 30);
        lightPtr2.scaleRadiance(4.0);
//	lightPtr2.setShadows(true);				// for Figure 16.1(b)
        w.addLight(lightPtr2);

	// sphere
        Matte mattePtr1 = new Matte();
        mattePtr1.setKa(0.3);
        mattePtr1.setKd(0.3);
        mattePtr1.setCd(0.5, 0.6, 0);

        Sphere spherePtr1 = new Sphere(new Point3D(0.0, 2.4, 0), 1.5);
        spherePtr1.setMaterial(mattePtr1);
        w.addObject(spherePtr1);

	// box
        Matte mattePtr2 = new Matte();
        mattePtr2.setKa(0.2);
        mattePtr2.setKd(0.3);
        mattePtr2.setCd(0.8, 0.5, 0);

        Box boxPtr1 = new Box(new Point3D(5.4, -0.5, -3), new Point3D(7.5, 4.75,
                0.60));
        boxPtr1.setMaterial(mattePtr2);
        w.addObject(boxPtr1);

	// triangle
        Matte mattePtr3 = new Matte();
        mattePtr3.setKa(0.35);
        mattePtr3.setKd(0.50);
        mattePtr3.setCd(0, 0.5, 0.5);      // cyan

        Triangle trianglePtr1 = new Triangle(new Point3D(1.5, -0.5, 1.8), // front
                new Point3D(7.5, -0.5, -9.00), // back
                new Point3D(2.35, 5.8, 1.4));		// top									
        trianglePtr1.setMaterial(mattePtr3);
        w.addObject(trianglePtr1);

	// cylinder
        double bottom = -0.5;
        double top = 1.0;
        double radius = 1.0;

        SolidCylinder cylinderPtr = new SolidCylinder(bottom, top, radius);
        cylinderPtr.setMaterial(mattePtr2);
        w.addObject(cylinderPtr);

	// ground plane
        Matte mattePtr4 = new Matte();
        mattePtr4.setKa(0.1);
        mattePtr4.setKd(0.2);
        mattePtr4.setCd(Utility.WHITE);

        Plane planePtr = new Plane(new Point3D(0, -0.5, 0), new Normal(0, 1, 0));
        planePtr.setMaterial(mattePtr4);
        w.addObject(planePtr);
    }

}
