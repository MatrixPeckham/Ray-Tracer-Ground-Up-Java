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
package com.matrixpeckham.raytracer.build.figures.ch14;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.compound.SolidCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.tracers.RayCast;
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
public class BuildFigure05 implements BuildWorldFunction {

    @Override
    public void build(World w) {
        int numSamples = 16;

        w.vp.setHres(600);
        w.vp.setVres(400);
        w.vp.setSamples(numSamples);

        w.tracer = new RayCast(w);

        Ambient ambient = new Ambient();
        ambient.scaleRadiance(0.5);
        w.setAmbient(ambient);

        double a = 0.75;
        w.backgroundColor = new RGBColor(0.0, 0.3 * a, 0.25 * a);  // torquise

        Pinhole pinhole = new Pinhole();
        pinhole.setEye(7.5, 4, 10);
        pinhole.setLookat(-1, 3.7, 0);
        pinhole.setViewDistance(340);
        pinhole.computeUVW();
        w.setCamera(pinhole);

        Matte matte1 = new Matte();
        matte1.setKa(0.25);
        matte1.setKd(0.75);
        matte1.setCd(0.75, 0.75, 0);    	// dark yellow

        Matte matte2 = new Matte();
        matte2.setKa(0.45);
        matte2.setKd(0.75);
        matte2.setCd(0.75, 0.25, 0);  	 // orange

        Matte matte3 = new Matte();
        matte3.setKa(0.4);
        matte3.setKd(0.75);
        matte3.setCd(1, 0.5, 1);  		// mauve

        Matte matte4 = new Matte();
        matte4.setKa(0.15);
        matte4.setKd(0.5);
        matte4.setCd(0.75, 1.0, 0.75);   	// light green

        Matte matte5 = new Matte();
        matte5.setKa(0.20);
        matte5.setKd(0.97);
        matte5.setCd(Utility.WHITE);

        // spheres
        Sphere sphere1 = new Sphere(new Point3D(3.85, 2.3, -2.55), 2.3);
        sphere1.setMaterial(matte1);
        sphere1.setShadows(false);
        w.addObject(sphere1);

        Sphere sphere2 = new Sphere(new Point3D(-0.7, 1, 4.2), 2);
        sphere2.setMaterial(matte2);
        sphere2.setShadows(false);
        w.addObject(sphere2);

        // cylinder
        double bottom = 0.0;
        double top = 8.5;
        double radius = 2.2;
        SolidCylinder cylinder = new SolidCylinder(bottom, top, radius);
        cylinder.setMaterial(matte3);
        cylinder.setShadows(false);
        w.addObject(cylinder);

        // box
        Box box = new Box(new Point3D(-3.5, 0, -11), new Point3D(-2.5, 6, 6.5));
        box.setMaterial(matte4);
        box.setShadows(false);
        w.addObject(box);

        // ground plane
        Plane plane = new Plane(new Point3D(0), new Normal(0, 1, 0));
        plane.setMaterial(matte5);
        plane.setShadows(false);
        w.addObject(plane);

    }

}
