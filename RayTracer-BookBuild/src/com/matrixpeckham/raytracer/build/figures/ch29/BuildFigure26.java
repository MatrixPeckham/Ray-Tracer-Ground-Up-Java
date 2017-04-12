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
import com.matrixpeckham.raytracer.geometricobjects.compound.ProductJar;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.textures.image.ImageTexture;
import com.matrixpeckham.raytracer.textures.image.mappings.CylindricalMap;
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
public class BuildFigure26 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 29.26
        int numSamples = 16;

        w.vp.setHres(400);
        w.vp.setVres(400);
        w.vp.setSamples(numSamples);

        w.tracer = new RayCast(w);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(10, 8, 30);
        pinholePtr.setLookat(0, 2, 0);
        pinholePtr.setViewDistance(2000.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        Directional lightPtr1 = new Directional();
        lightPtr1.setDirection(10, 10, 5);
        lightPtr1.scaleRadiance(1.5);
        lightPtr1.setShadows(true);
        w.addLight(lightPtr1);

        Directional lightPtr2 = new Directional();
        lightPtr2.setDirection(0, 0, 1);
        lightPtr2.scaleRadiance(1.5);
        lightPtr2.setShadows(false);
        w.addLight(lightPtr2);

        // product jar
        Matte mattePtr = new Matte();
        mattePtr.setKa(0.25);
        mattePtr.setKd(0.75);
        mattePtr.setCd(0.9, 1.0, 0.9);     	// pale green

        Matte bodyMattePtr = new Matte();
        bodyMattePtr.setKa(0.25);
        bodyMattePtr.setKd(0.75);
        bodyMattePtr.setCd(Utility.RED);

        // image:
        Image imagePtr = new Image();
        String imagePath
                = "resources/Textures/ppm/";
        try {
//            imagePtr.loadPPMFile(Thread.currentThread().getContextClassLoader().getResourceAsStream(imagePath+"EarthHighRes.ppm"));
            imagePtr.loadPPMFile(Thread.currentThread().getContextClassLoader().getResourceAsStream(imagePath + "label.ppm"));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure26.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

        CylindricalMap cylindicalMapPtr = new CylindricalMap();

        ImageTexture imageTexturePtr = new ImageTexture(imagePtr);
        imageTexturePtr.setMapping(cylindicalMapPtr);

        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.25);
        svMattePtr.setKd(0.75);
        svMattePtr.setCd(imageTexturePtr);

        // The product jar is centered on the y axis
        // The radius of the cap is bodyRadius - topBevelRadius
        // The texture image used here completely covers the vertical curved surface
        // of the body. This has vertical extent from bottom + bottomBevelRadius
        // to body top - bottomBevelRadius.
        // If you use the bodyMattePtr defined above for the body, it will be rendered red.
        // See extra image in the Chapter 29 download.
        double bottom = 0.0;		// along y axis
        double bodyTop = 3.6;		// along y axis
        double capTop = 4.1;		// along y axis
        double bodyRadius = 3.0;
        double bottomBevelRadius = 0.5;
        double topBevelRadius = 0.5;
        double capBevelRadius = 0.05;

        ProductJar productJarPtr = new ProductJar(bottom,
                bodyTop,
                capTop,
                bodyRadius,
                bottomBevelRadius,
                topBevelRadius,
                capBevelRadius);
        productJarPtr.setMaterial(mattePtr);
//	productJarPtr.setBodyMaterial(bodyMattePtr);		// renders vertical body surface red
        productJarPtr.setBodyMaterial(svMattePtr);

        Instance rotatedJarPtr = new Instance(productJarPtr);
//	rotatedJarPtr.rotateY(180); 		// for Figure 29.26(a)
        rotatedJarPtr.rotateY(100);  		// for Figure 29.26(b)
        w.addObject(rotatedJarPtr);

        // ground plane
        Matte mattePtr2 = new Matte();
        mattePtr2.setKa(0.25);
        mattePtr2.setKd(0.5);
        mattePtr2.setCd(0.5);

        Plane planePtr = new Plane(new Point3D(0.0), new Normal(0, 1, 0));
        planePtr.setMaterial(mattePtr2);
        w.addObject(planePtr);

    }

}
