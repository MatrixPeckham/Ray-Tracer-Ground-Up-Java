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

import com.matrixpeckham.raytracer.cameras.*;
import com.matrixpeckham.raytracer.geometricobjects.parametric.BezierPatch;
import com.matrixpeckham.raytracer.lights.*;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
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
public class TestBezier implements BuildWorldFunction {

    @Override
    public void build(World w) {
        int num_samples = 1;

        Sampler uniform_ptr = new MultiJittered(num_samples);

        w.vp.setHres(300);
        w.vp.setVres(300);
//        w.vp.setVres(300);
        //w.vp.setPixelSize(0.025);
        w.vp.setSampler(uniform_ptr);

        w.backgroundColor = new RGBColor(1, 1, 0);
        w.tracer = new RayCast(w);

        Orthographic orthographic_ptr = new Orthographic();
        orthographic_ptr.setEye(0, 5, 0);
        orthographic_ptr.setLookat(new Point3D(0, 0, 0));
        orthographic_ptr.computeUVW();
        //w.setCamera(orthographic_ptr);

        Pinhole pinhole = new Pinhole();
        pinhole.setEye(0, 10, 0);
        pinhole.setLookat(0, 0, 0);
        pinhole.setViewDistance(1000);
        pinhole.computeUVW();
        w.setCamera(pinhole);

// thin lens camera
        ThinLens thin_lens_ptr = new ThinLens();
        thin_lens_ptr.setEye(50, 50, 100);
        thin_lens_ptr.setLookat(0, 0, 0);
        thin_lens_ptr.setViewDistance(200.0);
        thin_lens_ptr.setFocalDistance(120.0);
        thin_lens_ptr.setLensRadius(1.0);
        thin_lens_ptr.setSampler(new MultiJittered(num_samples));
        thin_lens_ptr.computeUVW();
        //w.setCamera(thin_lens_ptr);

        AmbientOccluder occ = new AmbientOccluder();
        occ.setSampler(new MultiJittered(num_samples));
        occ.setMinAmount(0.1);
        occ.setLs(2);

        w.ambient = new Ambient();
        //w.ambient = occ;

        Directional light_ptr = new Directional();
        //light_ptr.setLocation(100, 100, 200);
        light_ptr.setDirection(1, 0.5, 1);
        light_ptr.scaleRadiance(20.0);
        //light_ptr.setExp(2);
        w.addLight(light_ptr);

        //SV_Matte matte_ptr = new SV_Matte();
        Matte matte_ptr = new Matte();
        matte_ptr.setKa(0.2);
        matte_ptr.setKd(0.8);
        //matte_ptr.setCd(new Checker3D());				// yellow
        matte_ptr.setCd(new RGBColor(1, 0, 0));

        /* Torus torus = new Torus(1, 0.5);
         * torus.setMaterial(matte_ptr);
         * Instance inst = new Instance(torus);
         * inst.translate(0, 2, 0);
         * w.addObject(inst); */
 /* double size = 0.1;
         * for(double y = 0.1; y<30; y+=size*2){
         * Torus t = new Torus(1, size);
         * t.setShadows(false);
         * t.setMaterial(matte_ptr);
         * Instance inst = new Instance(t);
         * inst.translate(0,y,0);
         * //w.addObject(inst);
         * size+=0.1;
         * } */
        BezierPatch one = new BezierPatch();
        one.setMaterial(matte_ptr);
        one.setShadows(false);
//        one.setupCells();
        w.addObject(one);

    }

}
