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
package com.matrixpeckham.raytracer.build.figures;

import com.matrixpeckham.raytracer.cameras.Orthographic;
import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.primatives.OpenCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.samplers.Hammersley;
import com.matrixpeckham.raytracer.samplers.Jittered;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.samplers.NRooks;
import com.matrixpeckham.raytracer.samplers.PureRandom;
import com.matrixpeckham.raytracer.samplers.Regular;
import com.matrixpeckham.raytracer.samplers.Sampler;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure04_04 implements BuildWorldFunction {

    @Override
    public void build(World w) {
        int num_samples = 25;
        
        Sampler uniform_ptr = new MultiJittered(num_samples);

        w.vp.setHres(100);
        w.vp.setVres(100);
        w.vp.setPixelSize(0.1);
        w.vp.setSampler(uniform_ptr);

        w.backgroundColor = Utility.BLACK;
        w.tracer = new RayCast(w);

        Orthographic orthographic_ptr = new Orthographic();
        orthographic_ptr.setEye(0, 0, 100);
        orthographic_ptr.setLookat(new Point3D(0));
        w.setCamera(orthographic_ptr);

        w.ambient = new Ambient();

        PointLight light_ptr = new PointLight();
        light_ptr.setLocation(100, 100, 200);
        light_ptr.scaleRadiance(2.0);
        //light_ptr.setExp(2);
        w.addLight(light_ptr);

        Matte matte_ptr = new Matte();
        matte_ptr.setKa(0.2);
        matte_ptr.setKd(0.8);
        matte_ptr.setCd(1, 1, 0);				// yellow	

        Sphere sphere_ptr = new Sphere(new Point3D(0.0), 5.0);
        sphere_ptr.setMaterial(matte_ptr);
	w.addObject(sphere_ptr);    


    }

}
