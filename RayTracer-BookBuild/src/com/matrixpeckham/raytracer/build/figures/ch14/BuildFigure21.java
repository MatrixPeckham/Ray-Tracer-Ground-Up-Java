/*
 Copyright (C) 2015 William Matrix Peckham

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.matrixpeckham.raytracer.build.figures.ch14;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure21 implements BuildWorldFunction {

    @Override
    public void build(World w) {
        int numSamples = 16;

        w.vp.setHres(400);
        w.vp.setVres(400);
        w.vp.setPixelSize(0.5);
        w.vp.setSamples(numSamples);

        w.tracer = new RayCast(w);

        Ambient ambientPtr = new Ambient();
        ambientPtr.scaleRadiance(1.0);
        w.setAmbient(ambientPtr);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(0, 0, 500);
        pinholePtr.setLookat(-5, 0, 0);
        pinholePtr.setViewDistance(850.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        PointLight lightPtr2 = new PointLight();
        lightPtr2.setLocation(100, 50, 150);
        lightPtr2.setShadows(false);
        lightPtr2.scaleRadiance(3.0);
        w.addLight(lightPtr2);

        Matte mattePtr1 = new Matte();
        mattePtr1.setKa(0.25);
        mattePtr1.setKd(0.65);
        mattePtr1.setCd(1, 1, 0);	  				// yellow
        Sphere spherePtr1 = new Sphere(new Point3D(10, -5, 0), 27);
        spherePtr1.setMaterial(mattePtr1);
        w.addObject(spherePtr1);

        Matte mattePtr2 = new Matte();
        mattePtr2.setKa(0.15);
        mattePtr2.setKd(0.85);
        mattePtr2.setCd(0.71, 0.40, 0.16);   		// brown
        Sphere spherePtr2 = new Sphere(new Point3D(-25, 10, -35), 27);
        spherePtr2.setMaterial(mattePtr2);
        w.addObject(spherePtr2);

        Matte mattePtr3 = new Matte();
        mattePtr3.setKa(0.15);
        mattePtr3.setKd(0.5);
        mattePtr3.setCd(0, 0.4, 0.2);				// dark green
        Plane planePtr = new Plane(new Point3D(0, 0, -50), new Normal(0, 0, 1));
        planePtr.setMaterial(mattePtr3);
        w.addObject(planePtr);
    }
}
