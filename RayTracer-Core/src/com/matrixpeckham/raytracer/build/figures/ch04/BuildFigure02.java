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
package com.matrixpeckham.raytracer.build.figures.ch04;

import com.matrixpeckham.raytracer.cameras.Orthographic;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.FunctionalTexture;
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
public class BuildFigure02 implements BuildWorldFunction {

    @Override
    public void build(World w) {
        int num_samples = 1;

        int res = 512;

        //double size = 3.79;
        double size = 10.83;

        w.vp.setHres(res);
        w.vp.setVres(res);
        w.vp.setPixelSize(size / res);
        w.vp.setSamples(num_samples);

        w.backgroundColor = Utility.BLACK;
        w.tracer = new RayCast(w);

        Orthographic orthographic_ptr = new Orthographic();
        orthographic_ptr.setEye(size / 2, size / 2, 100);
        orthographic_ptr.setLookat(new Point3D(size / 2, size / 2, 0));
        w.setCamera(orthographic_ptr);

        w.ambient = new Ambient();

        SV_Matte matte_ptr = new SV_Matte();
        matte_ptr.setKa(1);
        matte_ptr.setKd(0);
        FunctionalTexture texture = new FunctionalTexture((x, y) -> 0.5 * (Math.
                sin(x * x * y * y) + 1));
        matte_ptr.setCd(texture);				// yellow

        Plane sphere_ptr = new Plane(new Point3D(0.0), new Normal(0, 0, 1));
        sphere_ptr.setMaterial(matte_ptr);
        w.addObject(sphere_ptr);
    }

}
