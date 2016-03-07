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
package com.matrixpeckham.raytracer.build.figures.ch05;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Rectangle;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.AreaLight;
import com.matrixpeckham.raytracer.materials.Emissive;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure13 implements BuildWorldFunction {

    @Override
    public void build(World w) {
        int numSamples = 100;
        //Sampler sampler = new MultiJittered(numSamples);

        w.vp.setHres(400);
        w.vp.setVres(400);
        w.vp.setMaxDepth(5);
        //w.vp.setSampler(sampler);
        w.vp.setSamples(numSamples);

        w.backgroundColor = Utility.BLACK;

        w.tracer = new RayCast(w);

        Pinhole pinhole = new Pinhole();
        pinhole.setEye(30, 13, 20);
        pinhole.setLookat(0, -2, 0);
        pinhole.setViewDistance(12000);
        pinhole.computeUVW();
        w.setCamera(pinhole);

        Emissive emissive = new Emissive();
        emissive.scaleRadiance(3.0);
        emissive.setCe(Utility.WHITE);

        Rectangle rectangle = new Rectangle(new Point3D(-1, -0.5, -1),
                new Vector3D(2, 0, 0), new Vector3D(0, 0, 2), new Normal(0, -1,
                        0));
        rectangle.setMaterial(emissive);
        //rectangle.setSampler(sampler);
        rectangle.setSampler(new MultiJittered(numSamples));
        w.addObject(rectangle);

        AreaLight rectangularLight = new AreaLight();
        rectangularLight.setObject(rectangle);
        rectangularLight.setShadows(true);
        w.addLight(rectangularLight);

        Reflective reflective1 = new Reflective();
        reflective1.setKa(0.2);
        reflective1.setKd(0.1);
        reflective1.setCd(0, 1, 0.2);  // green
        reflective1.setKs(0.0);
        reflective1.setExp(1);
        reflective1.setKr(0.85);
        reflective1.setCr(new RGBColor(0.75, 0.75, 1));  // blue

        Sphere sphere1 = new Sphere(new Point3D(0, -2, 0), 0.5);
        sphere1.setMaterial(reflective1);
        w.addObject(sphere1);

        Checker3D checker = new Checker3D();
        checker.setSize(1);
        checker.setColor1(1.0);
        checker.setColor2(0.9);

        SV_Matte svMatte = new SV_Matte();
        svMatte.setKa(0.25);
        svMatte.setKd(0.75);
        svMatte.setCd(checker);

        Plane plane = new Plane(new Point3D(0, -2.75, 0), new Normal(0, 1, 0));
        plane.setMaterial(svMatte);
        w.addObject(plane);

    }

}
