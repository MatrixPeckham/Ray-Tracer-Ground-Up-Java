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
import com.matrixpeckham.raytracer.geometricobjects.compound.TriangleMesh;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Rectangle;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.AmbientOccluder;
import com.matrixpeckham.raytracer.lights.AreaLight;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Emissive;
import com.matrixpeckham.raytracer.materials.Phong;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Mesh;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure09B_Extra implements BuildWorldFunction {

    @Override
    public void build(World w) {
        int numSamples = 16;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setSamples(numSamples);
        w.vp.setMaxDepth(5);

        w.tracer = new Whitted(w);
        w.backgroundColor = Utility.BLACK;

        MultiJittered sampler = new MultiJittered(numSamples);

        Ambient ambient = new Ambient();
        ambient.scaleRadiance(1.0);
//	w.setAmbient(ambient);

        AmbientOccluder occluder = new AmbientOccluder();
        occluder.setMinAmount(0.1);
        occluder.setSampler(sampler);
        w.setAmbient(occluder);

        Pinhole pinhole = new Pinhole();
        pinhole.setEye(-6, 5, 11);
        pinhole.setLookat(-0.009, 0.11, 0);
        pinhole.setViewDistance(37500);
        pinhole.computeUVW();
        w.setCamera(pinhole);

        Directional light = new Directional();
        light.setDirection(0.5, 1, 0.75);
        light.scaleRadiance(1.0);
        light.setShadows(true);
        //w.addLight(light);

        Rectangle lightShape = new Rectangle(new Point3D(0.5, 1,
                0.75), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1));
        Emissive lightMat = new Emissive();
        lightMat.scaleRadiance(10);
        lightMat.setCe(1, 1, 1);
        lightShape.setMaterial(lightMat);
        lightShape.setSampler(new MultiJittered(numSamples));

        AreaLight areaLight = new AreaLight();
        areaLight.setObject(lightShape);
        w.addLight(areaLight);
        w.addObject(lightShape);

        Phong phong = new Phong();
        phong.setKa(0.2);
        phong.setKd(0.95);
        phong.setCd(1, 0.6, 0);   // orange
        phong.setKs(0.5);
        phong.setExp(20);

        Reflective reflective1 = new Reflective();
        reflective1.setKa(0.2);
        reflective1.setKd(0.75);
        reflective1.setCd(new RGBColor(1, 0.6, 0.0));
        reflective1.setKs(0.2);
        reflective1.setExp(20);
        reflective1.setKr(0.75);
        reflective1.setCr(new RGBColor(1, 0.6, 0));

        String fileName
                = "C:\\Users\\Owner\\Documents\\Ground Up raytracer\\Models\\Dragon2.ply";
        TriangleMesh dragon = new TriangleMesh(new Mesh());
        try {
//            dragon.readFlatTriangles(new File(fileName));
            dragon.readSmoothTriangles(new File(fileName));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure09B_Extra.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        dragon.setMaterial(reflective1);
        dragon.setupCells();
        w.addObject(dragon);

        Reflective reflective = new Reflective();
        reflective.setKa(0.2);
        reflective.setKd(0.75);
        reflective.setCd(new RGBColor(0.5));
        reflective.setKs(0.1);
        reflective.setExp(20);
        reflective.setKr(0.5);
        reflective.setCr(new RGBColor(0.8, 1.0, 0.8));

        Plane plane = new Plane(new Point3D(0, 0.055, 0), new Normal(0, 1, 0));
        plane.setMaterial(reflective);
        w.addObject(plane);

    }

}
