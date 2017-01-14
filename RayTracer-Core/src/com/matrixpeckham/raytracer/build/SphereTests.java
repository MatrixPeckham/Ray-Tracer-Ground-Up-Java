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
import com.matrixpeckham.raytracer.geometricobjects.compound.Grid;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Phong;
import com.matrixpeckham.raytracer.samplers.Regular;
import com.matrixpeckham.raytracer.samplers.Sampler;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class SphereTests implements BuildWorldFunction {

    @Override
    public void build(World w) {
        double size = 2;
        int res = 600;

        int num_samples = 1;

        Sampler uniform_ptr = new Regular(num_samples);

        w.vp.setHres(res);
        w.vp.setVres(res);
        w.vp.setPixelSize((2 * size) / res);
        w.vp.setSampler(uniform_ptr);

        w.backgroundColor = new RGBColor(1, 1, 1);
        w.tracer = new RayCast(w);

        Pinhole orthographic_ptr = new Pinhole();
        orthographic_ptr.setEye(2, 3, 3);
        orthographic_ptr.setLookat(new Point3D(0, 0, 0));
        orthographic_ptr.setViewDistance(5);

        orthographic_ptr.computeUVW();
        w.setCamera(orthographic_ptr);

        Ambient occ = new Ambient();
        occ.setColor(1, 1, 1);
        occ.scaleRadiance(0.1);

        w.ambient = occ;

        Directional light = new Directional();
        light.setColor(1, 1, 1);
        light.setDirection(0, 1, 0);
        light.scaleRadiance(1);
        w.addLight(light);

        Grid g = new Grid();

        Phong red = new Phong();
        red.setCd(1, 0, 0);
        red.setCs(1, 1, 1);
        red.setKa(1);
        red.setKd(1);
        red.setKs(1);

        Sphere s = new Sphere();
        s.setMaterial(red);
        s.setCenter(new Point3D(0, 0, 0));
        g.addObject(s);

        g.setupCells();
        w.addObject(g);

    }

}
