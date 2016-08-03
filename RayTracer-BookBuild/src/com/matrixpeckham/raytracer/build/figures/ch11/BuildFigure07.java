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
package com.matrixpeckham.raytracer.build.figures.ch11;

import com.matrixpeckham.raytracer.cameras.FishEye;
import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.compound.Grid;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.textures.image.ImageTexture;
import com.matrixpeckham.raytracer.textures.image.mappings.SphericalMap;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
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
public class BuildFigure07 implements BuildWorldFunction {

    @Override
    public void build(World w) {
        int numSamples = 16;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setSamples(numSamples);

        w.tracer = new RayCast(w);

        // pinhole camera for Figure 11.7(a)
        Pinhole pinHole = new Pinhole();
        pinHole.setEye(250, 300, 150);
        pinHole.setLookat(-20, 300, -110);
        pinHole.setViewDistance(250);
        pinHole.computeUVW();
	//w.setCamera(pinHole);

        // fisheye camera for the other parts
        FishEye fisheye = new FishEye();

        // for parts (b), (c), (d)
        fisheye.setEye(250, 300, 150);
        fisheye.setLookat(-20, 300, -110);
        fisheye.setFov(120d);  // part (b)
//	fisheye.setFov(180);  // part (c)
//	fisheye.setFov(360);  // part (d)


        /*

         // for part (e)

         fisheye.setEye(250, 450, 150);
         fisheye.setLookat(-20, 250, -110);
         fisheye.setFov(360);

         */
        /*
         // for part (f)
         // for this image the skydome is the only object in the scene
         // you need to comment out the two statements:
         // w.addObject(grid);
         // w.addObject(plane);

         fisheye.setEye(0, 0, 0);
         fisheye.setLookat(0, 1, 0);
         fisheye.setFov(180);
         */
        fisheye.computeUVW();
        w.setCamera(fisheye);

        PointLight light1 = new PointLight();
        light1.setLocation(150, 200, 65);
        light1.scaleRadiance(5.25);
        light1.setShadows(true);
        w.addLight(light1);

        // box materials
        Matte matte1 = new Matte();
        matte1.setCd(0, 0.5, 0.5);     // cyan
        matte1.setKa(0.4);
        matte1.setKd(0.5);

        Matte matte2 = new Matte();
        matte2.setCd(1, 0, 0);     	  // red
        matte2.setKa(0.4);
        matte2.setKd(0.5);

        Matte matte3 = new Matte();
        matte3.setCd(0.5, 0.6, 0);     // green
        matte3.setKa(0.4);
        matte3.setKd(0.5);

        // Construct rows of boxes stored in a grid
        Grid grid = new Grid();

        // first row
        int numBoxes = 40;
        double wx = 50;
        double wz = 50;
        double h = 150;
        double s = 100;

        for (int j = 0; j < numBoxes; j++) {
            Box box = new Box(new Point3D(-wx, 0, -(j + 1) * wz - j * s),
                    new Point3D(0, h, -j * wz - j * s));
            box.setMaterial(matte1);
            // w.addObject(box);
            grid.addObject(box);
        }

        // second row
        h = 300;

        for (int j = 0; j < numBoxes; j++) {
            Box box = new Box(
                    new Point3D(-wx - wx - s, 0, -(j + 1) * wz - j * s),
                    new Point3D(-wx - s, h, -j * wz - j * s));
            box.setMaterial(matte2);
//	 w.addObject(box);
            grid.addObject(box);
        }

        // third row
        h = 850;

        for (int j = 0; j < numBoxes; j++) {
            Box box = new Box(new Point3D(-wx - 2 * wx - 2 * s, 0, -(j + 1) * wz
                    - j * s),
                    new Point3D(-2 * wx - 2 * s, h, -j * wz - j * s));
            box.setMaterial(matte3);
//	 w.addObject(box);
            grid.addObject(box);
        }

        // a column
        h = 150;

        for (int j = 0; j < numBoxes; j++) {
            Box box = new Box(new Point3D(-3 * (wx + s) - (j + 1) * wz - j * s,
                    0, -wx),
                    new Point3D(-3 * (wx + s) - j * wz - j * s, h, 0));
            box.setMaterial(matte1);
//	 w.addObject(box);
            grid.addObject(box);
        }

        grid.setupCells();
        w.addObject(grid);

        // ground plane with checker
        Checker3D checker = new Checker3D();
        checker.setSize(2 * wx);
        checker.setColor1(0.7);
        checker.setColor2(Utility.WHITE);

        SV_Matte svMatte1 = new SV_Matte();
        svMatte1.setKa(0.20);
        svMatte1.setKd(0.50);
        svMatte1.setCd(checker);
        Plane plane = new Plane(new Point3D(0, 1, 0), new Normal(0, 1, 0));
        plane.setMaterial(svMatte1);
        w.addObject(plane);

        // skydome with clouds
        Image image = new Image();
        try {
            image.loadPPMFile(new File(
                    "C:\\Users\\Owner\\Documents\\Ground Up raytracer\\Textures\\ppm\\CloudsLowRes.ppm"));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure07.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

        SphericalMap sphericalMap = new SphericalMap();

        ImageTexture imageTexture = new ImageTexture(image);
        imageTexture.setMapping(sphericalMap);

        SV_Matte svMatte2 = new SV_Matte();
        svMatte2.setKa(1);
        svMatte2.setKd(0.85);
        svMatte2.setCd(imageTexture);

        Instance sphere1 = new Instance(new Sphere());
        sphere1.scale(new Vector3D(1000000));
        sphere1.setMaterial(svMatte2);
        w.addObject(sphere1);
    }

}
