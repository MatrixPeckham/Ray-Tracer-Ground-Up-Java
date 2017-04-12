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
package com.matrixpeckham.raytracer.build.figures.ch29;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.SV_Phong;
import com.matrixpeckham.raytracer.textures.ConstantColor;
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.textures.image.ImageTexture;
import com.matrixpeckham.raytracer.textures.image.mappings.SphericalMap;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
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
public class BuildFigure11B implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 29.11(b)
// Inspite of my best efforts, my ray tracer now renders the BilliardBall.ppm file shown
// in Figure 29.11(a) in cyan instead of majenta. The color is majenta when the file is
// opened in GraphicConverter, and the thumbnail is also majenta.
// Cyan is the wrong color for the number 9 ball.
        int numSamples = 16;

        w.vp.setHres(400);
        w.vp.setVres(400);
        w.vp.setSamples(numSamples);

        w.backgroundColor = Utility.BLACK;

        w.tracer = new RayCast(w);

        Ambient ambientPtr = new Ambient();
        ambientPtr.scaleRadiance(0.5);
        w.setAmbient(ambientPtr);

        Pinhole cameraPtr = new Pinhole();
        cameraPtr.setEye(50, 10, 50);
        cameraPtr.setLookat(new Point3D(0.0));
        cameraPtr.setViewDistance(11000.0);
        cameraPtr.computeUVW();
        w.setCamera(cameraPtr);

        PointLight lightPtr1 = new PointLight();
        lightPtr1.setLocation(100, 50, 50);
        lightPtr1.scaleRadiance(1.5);
        lightPtr1.setShadows(true);
        w.addLight(lightPtr1);

        PointLight lightPtr2 = new PointLight();
        lightPtr2.setLocation(100, 200, -100);
        lightPtr2.scaleRadiance(1.5);
        lightPtr2.setShadows(true);
        w.addLight(lightPtr2);

        // image:
        Image imagePtr = new Image();
        String path

                = "resources/Textures/ppm/";
        try {
            imagePtr.loadPPMFile(Thread.currentThread().getContextClassLoader().getResourceAsStream(path + "BilliardBall.ppm"));

        } catch (IOException ex) {
            Logger.getLogger(BuildFigure11B.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

        // mapping:
        SphericalMap mapPtr = new SphericalMap();

        // image based texture:
        ImageTexture texturePtr = new ImageTexture();
        texturePtr.setImage(imagePtr);
        texturePtr.setMapping(mapPtr);

        // textured material:
        SV_Phong svPhongPtr = new SV_Phong();
        svPhongPtr.setKa(0.5);
        svPhongPtr.setKd(0.8);
        svPhongPtr.setCd(texturePtr);
        svPhongPtr.setKs(0.25);
        svPhongPtr.setExp(500.0);
        svPhongPtr.setCs(new ConstantColor());

        // generic sphere:
        Sphere spherePtr = new Sphere();
        spherePtr.setMaterial(svPhongPtr);

        // rotated sphere
        Instance billardBallPtr = new Instance(spherePtr);
        billardBallPtr.rotateY(-30);
        w.addObject(billardBallPtr);

        // plane
        Matte mattePtr = new Matte();
        mattePtr.setKa(0.5);
        mattePtr.setKd(0.5);
        mattePtr.setCd(0.2, 0.75, 0.2);

        Plane planePtr = new Plane(new Point3D(0, -1, 0), new Normal(0, 1, 0));
        planePtr.setMaterial(mattePtr);
        w.addObject(planePtr);

    }

}
