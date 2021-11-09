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

import com.matrixpeckham.raytracer.cameras.Orthographic;
import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.cameras.ThinLens;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.text.TextQuad;
import com.matrixpeckham.raytracer.geometricobjects.math.RayPrimitive;
import com.matrixpeckham.raytracer.geometricobjects.parametric.Torus;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.PartCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.AmbientOccluder;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.GlossyReflector;
import com.matrixpeckham.raytracer.materials.Phong;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.samplers.Sampler;
import com.matrixpeckham.raytracer.textures.procedural.PlaneChecker;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.*;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class TestText implements BuildWorldFunction {

    @Override
    public void build(World w) {
        int num_samples = 25;

        int maxDepth = 5;

        Sampler uniform_ptr = new MultiJittered(num_samples);

        w.vp.setHres(800);
        w.vp.setVres(800);
//        w.vp.setVres(300);
        //w.vp.setPixelSize(0.025);
        w.vp.setSampler(uniform_ptr);
        w.vp.setMaxDepth(maxDepth);

        w.backgroundColor = new RGBColor(1, 1, 0);
        w.tracer = new RayCast(w);

        Orthographic orthographic_ptr = new Orthographic();
        orthographic_ptr.setEye(0, 3, 0);
        orthographic_ptr.setLookat(new Point3D(0, 0, 0));
        orthographic_ptr.computeUVW();
        //w.setCamera(orthographic_ptr);

        Pinhole pinhole = new Pinhole();
        pinhole.setEye(0, 2, 5);
        pinhole.setLookat(0, 0, 0);
        pinhole.setViewDistance(600);
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
        occ.setMinAmount(0);
        occ.setLs(1);

        //w.ambient = new Ambient();
        w.ambient = occ;

        Directional light_ptr = new Directional();
        //light_ptr.setLocation(100, 100, 200);
        light_ptr.setDirection(1, 0.5, 0);
        light_ptr.scaleRadiance(4.0);
        //light_ptr.setExp(2);
        w.addLight(light_ptr);

        PlaneChecker checker = new PlaneChecker(1, 0, Utility.BLACK,
                Utility.WHITE, Utility.RED);

        SV_Matte check = new SV_Matte();
        check.setCd(checker);
        //check.setCs(new ConstantColor(Utility.WHITE));
        check.setKa(1);
        check.setKd(1);
        //check.setKs(1);
        //check.setExp(2);

        Plane ground = new Plane(new Point3D(), new Normal(0, 1, 0));
        ground.setMaterial(check);
        w.addObject(ground);

        //SV_Matte matte_ptr = new SV_Matte();
        Phong matte_ptr = new Phong();
        matte_ptr.setKa(1);
        matte_ptr.setKd(1);
        matte_ptr.setKa(1);
        matte_ptr.setExp(2);
        //matte_ptr.setCd(new Checker3D());				// yellow
        matte_ptr.setCd(new RGBColor(1, 0, 0));
        matte_ptr.setCs(new RGBColor(1, 0, 0));
        Torus one = new Torus(0.9, 0.1);
        one.setMaterial(matte_ptr);
        one.setupCells();
        Instance inst = new Instance(one);
        inst.translate(0, 0.1, 0);
        w.addObject(inst);

        TextQuad text = new TextQuad("This is text\nwith more\nthan one\nline.",
                2, Utility.BLUE);
        Instance textInst = new Instance(text);
        textInst.translate(-1, 0, 1);
        w.addObject(textInst);

        GlossyReflector reflective = new GlossyReflector();
        reflective.setCd(Utility.WHITE);
        reflective.setCr(new RGBColor(0.3, 1, 0.3));
        reflective.setKr(0.6);
        reflective.setKa(0.1);
        reflective.setKd(0.3);
        reflective.setKs(0);
        reflective.setSamples(num_samples, 1000000);
        reflective.setCs(Utility.WHITE);

        PartCylinder cyl = new PartCylinder(0, 1.5,
                5, 0, 360);
        cyl.setMaterial(reflective);
        w.addObject(cyl);

        RayPrimitive ray = new RayPrimitive(new Vector3D(0.25, 0.25, 0.25),
                new Vector3D(), Utility.RED, Utility.RED);
        Instance rayInst = new Instance(ray);
        rayInst.scale(4, 4, 4);
        w.addObject(rayInst);

        TextQuad vec = new TextQuad("(1,1,1)", 1, Utility.GREEN);
        Instance vecInst = new Instance(vec);
        vecInst.translate(1, 1, 1);
        w.addObject(vecInst);

    }

}
