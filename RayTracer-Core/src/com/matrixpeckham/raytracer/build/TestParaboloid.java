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
package com.matrixpeckham.raytracer.build;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Paraboloid;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
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
public class TestParaboloid implements BuildWorldFunction {

    @Override
    public void build(World w) {
        int numSamples = 16;

        w.vp.setHres(400);
        w.vp.setVres(400);
        w.vp.s = 0.01;
        w.vp.setMaxDepth(15);
        w.vp.setSamples(numSamples);

        w.backgroundColor = Utility.WHITE;

        w.tracer = new RayCast(w);

        Pinhole pinhole = new Pinhole();
        pinhole.setEye(10, 20, 10);
        pinhole.setLookat(0, 0, 0);
//        pinhole.setRoll(-90 * Utility.PI_ON_180);
        pinhole.setViewDistance(12);

        pinhole.computeUVW();
        w.setCamera(pinhole);

        Directional light = new Directional();
        light.setDirection(10, 20, 10);

        w.addLight(light);

        Reflective reflective1 = new Reflective();
        reflective1.setKa(0);
        reflective1.setKd(0);
        reflective1.setCd(0, 1, 0.2);  // green
        reflective1.setKr(1);
        reflective1.setCr(Utility.WHITE);

        Paraboloid loid = new Paraboloid(5, 5, 1, 0, 1, false);
        loid.setMaterial(reflective1);
        Instance inst = new Instance(loid);
        inst.rotateX(-90);
        w.addObject(inst);

        Paraboloid top = new Paraboloid(5, 5, -1,
                -1, -0.2, false);
        top.setMaterial(reflective1);
        Instance inst1 = new Instance(top);
        inst1.rotateX(-90);
        inst1.translate(0, 2, 0);
        w.addObject(inst1);

        Checker3D checker = new Checker3D();
        checker.setSize(1);
        checker.setColor1(1.0);
        checker.setColor2(0.9);

        SV_Matte svMatte = new SV_Matte();
        svMatte.setKa(0.25);
        svMatte.setKd(0.75);
        svMatte.setCd(checker);

        Plane plane = new Plane(new Point3D(0, 0, 0), new Normal(0, 1, 0));
        plane.setMaterial(svMatte);
        w.addObject(plane);

        Matte matte1 = new Matte();
        matte1.setCd(Utility.YELLOW);
        matte1.setKa(0.2);
        matte1.setKd(0.8);

        Sphere sphere = new Sphere(new Point3D(0, .5, 0), .5);
        sphere.setMaterial(matte1);
        w.addObject(sphere);

        Plane plane2 = new Plane(new Point3D(0, 0, 0), new Normal(0, 0, 1));
        plane2.setMaterial(svMatte);
        //w.addObject(plane2);

    }

}
