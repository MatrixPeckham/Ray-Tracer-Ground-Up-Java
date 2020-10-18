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
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.compound.Teapot;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.*;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.samplers.Sampler;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.*;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class TestTeapot implements BuildWorldFunction {

    @Override
    public void build(World w) {
        Point3D eye = new Point3D(0, 10, 10);
//        Point3D eye = new Point3D(0.18, 10, 0.18);
        Point3D view = new Point3D(0, 0, 0);
//        Point3D view = new Point3D(0.18, 0, 0.18);
        int num_samples = 16;

        Sampler uniform_ptr = new MultiJittered(num_samples);

        w.vp.setHres(1200);
        w.vp.setVres(1200);
        w.vp.maxDepth = 1;
//        w.vp.setVres(300);
        //w.vp.setPixelSize(0.025);
        w.vp.setSampler(uniform_ptr);

        w.backgroundColor = new RGBColor(1, 1, 1);
        w.tracer = new RayCast(w);

        Orthographic orthographic_ptr = new Orthographic();
        orthographic_ptr.setEye(eye);
        orthographic_ptr.setLookat(view);
        orthographic_ptr.computeUVW();
        //w.setCamera(orthographic_ptr);

        Pinhole pinhole = new Pinhole();
        pinhole.setEye(eye);
        pinhole.setLookat(view);
        pinhole.setViewDistance(1200);
//        pinhole.setZoom(30);
        pinhole.computeUVW();
        w.setCamera(pinhole);

// thin lens camera
        ThinLens thin_lens_ptr = new ThinLens();
        thin_lens_ptr.setEye(eye);
        thin_lens_ptr.setLookat(view);
        thin_lens_ptr.setViewDistance(200.0);
        thin_lens_ptr.setFocalDistance(120.0);
        thin_lens_ptr.setLensRadius(1.0);
        thin_lens_ptr.setSampler(new MultiJittered(num_samples));
        thin_lens_ptr.computeUVW();
        //w.setCamera(thin_lens_ptr);

        AmbientOccluder occ = new AmbientOccluder();
        occ.setSampler(new MultiJittered(num_samples));
        occ.setMinAmount(0.25);
        occ.setLs(1);

        Ambient amb = new Ambient();
        amb.setColor(0);

        //w.ambient = new Ambient();
        w.ambient = amb;//occ;

        Directional light_ptr = new Directional();
        //light_ptr.setLocation(100, 100, 200);
        //light_ptr.setDirection(0, 1, 0);
        light_ptr.setDirection(new Vector3D(eye));
        light_ptr.scaleRadiance(10.0);
        //light_ptr.setExp(2);
        w.addLight(light_ptr);

        //SV_Matte matte_ptr = new SV_Matte();
        //GlossyReflector matte_ptr = new GlossyReflector();
        Matte matte_ptr = new Matte();
        //matte_ptr.setKa(0.2);
        double refExp = 500.0;
        matte_ptr.setKa(0.5);
        //matte_ptr.setSamples(num_samples, refExp);
//        matte_ptr.setCs(Utility.WHITE);
        //      matte_ptr.setKs(0.25);
        //    matte_ptr.setKr(0.25);
        //  matte_ptr.setExp(refExp);
        matte_ptr.setKd(0.5);
        //matte_ptr.setCd(new Checker3D());				// yellow
        matte_ptr.setCd(new RGBColor(1, 0, 0));

        Teapot one = new Teapot();
        one.setMaterial(matte_ptr);
        one.setShadows(false);
        one.setupCells();
        Instance ione = new Instance(one);
        ione.rotateX(-90);
        ione.translate(0, 1, 0);
        w.addObject(ione);

        RGBColor gridColor1 = new RGBColor(1, 1, 1);
        RGBColor gridColor2 = new RGBColor(0, 0, 0);

        Checker3D checkerPtr = new Checker3D();
        checkerPtr.setSize(1.0);
        checkerPtr.setColor1(gridColor1);
        checkerPtr.setColor2(gridColor2);

        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.25);
        svMattePtr.setKd(0.75);
        svMattePtr.setCd(checkerPtr);

        Plane planePtr = new Plane(new Point3D(0.0), new Normal(0, 1, 0));
        planePtr.setMaterial(svMattePtr);
        w.addObject(planePtr);
    }

}
