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
package com.matrixpeckham.raytracer.build.figures.ch08;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.geometricobjects.triangles.Triangle;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.Phong;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure16 implements BuildWorldFunction {

    @Override
    public void build(World w) {
        int numSamples = 1;

        w.vp.setHres(400);
        w.vp.setVres(400);
        w.vp.setSamples(numSamples);

        w.backgroundColor = Utility.BLACK;
        w.tracer = new RayCast(w);

        Pinhole camera = new Pinhole();
        camera.setEye(0, 0, 250);
        camera.setLookat(new Point3D(0));
        camera.setViewDistance(200);  	// for Figure 8.16(a)	
//	camera.setViewDistance(450);  	// for Figure 8.16(b)		
//	camera.setViewDistance(1000);  	// for Figure 8.16(c)
        camera.computeUVW();
        w.setCamera(camera);

        PointLight light1 = new PointLight();
        light1.setLocation(50, 150, 200);
        light1.scaleRadiance(6.0);
        light1.setShadows(true);
        w.addLight(light1);

	// sphere
        Phong phong1 = new Phong();
        phong1.setKa(0.5);
        phong1.setKd(0.4);
        phong1.setCd(0.5, 0.6, 0);  	// green
        phong1.setKs(0.05);
        phong1.setExp(25);

        Sphere sphere1 = new Sphere(new Point3D(-45, 45, 40), 50);
        sphere1.setMaterial(phong1);
        w.addObject(sphere1);

	// box
        Matte matte = new Matte();
        matte.setKa(0.4);
        matte.setKd(0.3);
        matte.setCd(0.8, 0.5, 0);  	// orange

        Box box1
                = new Box(new Point3D(20, -101, -100), new Point3D(90, 100, 20));
        box1.setMaterial(matte);
        w.addObject(box1);

	// triangle
        Phong phong2 = new Phong();
        phong2.setKa(0.25);
        phong2.setKd(0.5);
        phong2.setCd(0, 0.5, 0.5);     // cyan
        phong2.setKs(0.05);
        phong2.setExp(50);

        Triangle triangle1 = new Triangle(new Point3D(-110, -85, 80),
                new Point3D(120, 10, 20), new Point3D(-40, 50, -30));
        triangle1.setMaterial(phong2);
        w.addObject(triangle1);

    }

}
