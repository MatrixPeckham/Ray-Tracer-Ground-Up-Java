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

import com.matrixpeckham.raytracer.cameras.QuadOrthographic;
import com.matrixpeckham.raytracer.geometricobjects.ClippedObject;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.text.TextQuad;
import com.matrixpeckham.raytracer.geometricobjects.math.RayPrimitive;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.samplers.Regular;
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
public class TestRayView2 implements BuildWorldFunction {

    @Override
    public void build(World w) {
        double size = 1;
        int res = 600;

        int num_samples = 16;

        Sampler uniform_ptr = new Regular(num_samples);

        w.vp.setHres(res);
        w.vp.setVres(res);
        w.vp.setPixelSize((2 * size) / res);
        w.vp.setSampler(uniform_ptr);

        w.backgroundColor = new RGBColor(1, 1, 1);
        w.tracer = new RayCast(w);

        //Orthographic orthographic_ptr = new Orthographic();
        //orthographic_ptr.setEye(0.5, 0.5, 0.5);
        //orthographic_ptr.setLookat(new Point3D(0.5, 0.5, 0));
        //orthographic_ptr.computeUVW();
        //w.setCamera(orthographic_ptr);
        QuadOrthographic orthographic_ptr = new QuadOrthographic();
        orthographic_ptr.setEye(2, 3, 3);
        orthographic_ptr.setLookat(new Point3D(0, 0, 0));
        orthographic_ptr.computeUVW();
        orthographic_ptr.setupCameras(w.vp);
        w.setCamera(orthographic_ptr);

        Ambient occ = new Ambient();
        occ.setColor(1, 1, 1);
        occ.scaleRadiance(1);

        w.ambient = occ;

        RayPrimitive yAxis = new RayPrimitive(new Vector3D(0, 2, 0),
                new Vector3D(0, 0, 0), new RGBColor(0, 1, 0), new RGBColor(0, 1,
                        0));
        w.addObject(yAxis);

        RayPrimitive zAxis = new RayPrimitive(new Vector3D(0, 0, 2),
                new Vector3D(0, 0, 0), new RGBColor(0, 0, 1), new RGBColor(0, 0,
                        1));
        w.addObject(zAxis);

        RayPrimitive xAxis = new RayPrimitive(new Vector3D(2, 0, 0),
                new Vector3D(0, 0, 0), new RGBColor(1, 0, 0), new RGBColor(1, 0,
                        0));
        w.addObject(xAxis);

        RayPrimitive vec = new RayPrimitive(new Vector3D(2, 2, 2),
                new Vector3D(0, 0, 0), new RGBColor(1, 0, 1), new RGBColor(1, 0,
                        1));
        w.addObject(vec);

        RayPrimitive vec1 = new RayPrimitive(new Vector3D(-2, 2, 2),
                new Vector3D(0, 0, 0), new RGBColor(1, 1, 0), new RGBColor(1, 1,
                        0));
        w.addObject(vec1);

        RayPrimitive vec2 = new RayPrimitive(new Vector3D(-2, -2, 1),
                new Vector3D(1, 1, 1), new RGBColor(1, 0, 1), new RGBColor(1, 0,
                        1));
        w.addObject(vec2);

        PlaneChecker clipchecker = new PlaneChecker(1, 0.02, Utility.WHITE,
                Utility.WHITE,
                Utility.BLACK);
        SV_Matte matteChecker = new SV_Matte();
        matteChecker.setCd(clipchecker);
        matteChecker.setKa(1);

        Plane planeshape = new Plane(new Point3D(), new Normal(0, 1, 0));
        planeshape.setMaterial(matteChecker);
        ClippedObject plane = new ClippedObject(planeshape, clipchecker);

        Instance xyPlane = new Instance(plane);
        xyPlane.rotateX(90);
        Instance zyPlane = new Instance(plane);
        zyPlane.rotateZ(90);
        Instance xzPlane = new Instance(plane);

        w.addObject(xyPlane);
        w.addObject(xzPlane);
        TextQuad xy = new TextQuad(TextQuad.DEFAULT_STRING, 2);
//        TextQuad xy = new TextQuad("XY Plane");
        w.addObject(xy);
        //w.addObject(zyPlane);
    }

}
