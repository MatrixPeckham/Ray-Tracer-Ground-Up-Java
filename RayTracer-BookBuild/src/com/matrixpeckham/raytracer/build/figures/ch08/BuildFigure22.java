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
import com.matrixpeckham.raytracer.geometricobjects.compound.WireframeBox;
import com.matrixpeckham.raytracer.lights.PointLight;
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
public class BuildFigure22 implements BuildWorldFunction {

    @Override
    public void build(World w) {
        int numSamples = 16;

        w.vp.setHres(400);
        w.vp.setVres(400);
        w.vp.setSamples(numSamples);

        w.tracer = new RayCast(w);

        Pinhole pinhole = new Pinhole();

//	pinhole.setEye(0, 0, 10);       	// for Figure 8.22(a)
//	pinhole.setViewDistance(1687);
//	pinhole.setEye(0, 0, 5);       	// for Figure 8.22(b)
//	pinhole.setViewDistance(750);
        pinhole.setEye(0, 0, 2.5);       	// for Figure 8.22(c)
        pinhole.setViewDistance(280);

        pinhole.setLookat(new Point3D(0));
        pinhole.computeUVW();
        w.setCamera(pinhole);

        PointLight light = new PointLight();
        light.setLocation(10, 50, 40);
        light.scaleRadiance(3.0);
        w.addLight(light);

        Phong phong = new Phong();
        phong.setKa(0.25);
        phong.setKd(0.65);
        phong.setCd(Utility.WHITE);
        phong.setKs(0.1);
        phong.setExp(25);

        Point3D p0 = new Point3D(-1.0);
        Point3D p1 = new Point3D(1.0);
        double bevelRadius = 0.02;

        WireframeBox box = new WireframeBox(p0, p1, bevelRadius);
        box.setMaterial(phong);
        w.addObject(box);

    }

}
