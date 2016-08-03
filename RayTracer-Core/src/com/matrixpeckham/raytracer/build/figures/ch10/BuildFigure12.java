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
package com.matrixpeckham.raytracer.build.figures.ch10;

import com.matrixpeckham.raytracer.cameras.ThinLens;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Matte;
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
public class BuildFigure12 implements BuildWorldFunction {

    @Override
    public void build(World w) {

        int numSamples = 100;

        w.vp.setHres(450);
        w.vp.setVres(300);
        w.vp.setPixelSize(0.05);
        w.vp.setSamples(numSamples);
        w.vp.setMaxDepth(1);

        w.tracer = new RayCast(w);

        w.backgroundColor = Utility.WHITE;

        ThinLens thinLens = new ThinLens();
        thinLens.setSampler(new MultiJittered(numSamples));

        Point3D eye = new Point3D(15, 25, 50);
        Point3D lookat = new Point3D(0, 8, 0);
        Vector3D direction = (lookat.sub(eye));
        direction.normalize();
        double fd = 100;

        thinLens.setEye(15, 25, 50);
        thinLens.setLookat(0, 8, 0);
        thinLens.setViewDistance(30);
        thinLens.setFocalDistance(100);
        thinLens.setLensRadius(1);
        thinLens.computeUVW();
        w.setCamera(thinLens);

        // directional light
        Directional light = new Directional();
        light.setDirection(1, 1, -0.5);
        light.scaleRadiance(4.0);
        light.setShadows(true);
        w.addLight(light);

        Reflective reflective = new Reflective();
        reflective.setKr(0.9);
        reflective.setCr(new RGBColor(0.2, 0.9, 0.75));

        Box box = new Box(new Point3D(-17, 0, -1), new Point3D(13, 19, 0));
        box.setMaterial(reflective);
        w.addObject(box);

        // ground plane
        Checker3D checker = new Checker3D();
        checker.setSize(8.0);
        checker.setColor1(0.25);  			// gray
        checker.setColor2(Utility.WHITE);

        SV_Matte svMatte = new SV_Matte();
        svMatte.setKa(0.25);
        svMatte.setKd(0.75);
        svMatte.setCd(checker);

        Plane plane1 = new Plane(new Point3D(0.0), new Normal(0, 1, 0));
        plane1.setMaterial(svMatte);
        w.addObject(plane1);

        // focal plane plane
        Matte matte = new Matte();
        matte.setCd(1, 0, 0);
        matte.setKa(0.5);
        matte.setKd(0.5);

        Point3D point = eye.add(direction.mul(fd));
        Normal normal = new Normal(direction.neg());

        Plane plane2 = new Plane(point, normal);
        plane2.setMaterial(matte);
        w.addObject(plane2);

    }

}
