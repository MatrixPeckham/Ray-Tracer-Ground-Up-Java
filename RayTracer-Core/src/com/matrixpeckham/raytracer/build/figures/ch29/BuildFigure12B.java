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
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.textures.image.ImageTexture;
import com.matrixpeckham.raytracer.textures.image.mappings.SphericalMap;
import com.matrixpeckham.raytracer.tracers.RayCast;
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
public class BuildFigure12B implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 29.12(b)
        int numSamples = 16;

        w.vp.setHres(700);
        w.vp.setVres(700);
        w.vp.setSamples(numSamples);

        w.backgroundColor = Utility.BLACK;

        w.tracer = new RayCast(w);

        Pinhole cameraPtr = new Pinhole();
        cameraPtr.setEye(0, 0, 65);
        cameraPtr.setLookat(new Point3D(0.0));
        cameraPtr.setViewDistance(21000.0);
        cameraPtr.computeUVW();
        w.setCamera(cameraPtr);

        Directional lightPtr = new Directional();
        lightPtr.setDirection(-0.25, 0.4, 1);
        lightPtr.scaleRadiance(2.5);
        w.addLight(lightPtr);

        // image:
        Image imagePtr = new Image();
        String path
                = "C:\\Users\\Owner\\Documents\\Ground Up raytracer\\Textures\\ppm\\";
        try {
//            imagePtr.loadPPMFile(new File(path+"EarthLowRes.ppm"));
            imagePtr.loadPPMFile(new File(path + "EarthHighRes.ppm"));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure12B.class.getName()).
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
        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.2);
        svMattePtr.setKd(0.8);
        svMattePtr.setCd(texturePtr);

        // generic sphere:
        Sphere spherePtr = new Sphere();
        spherePtr.setMaterial(svMattePtr);

        // rotated sphere
        Instance earthPtr = new Instance(spherePtr);
        earthPtr.rotateY(60);
        w.addObject(earthPtr);

    }

}
