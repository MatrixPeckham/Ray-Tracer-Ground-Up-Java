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

import com.matrixpeckham.raytracer.cameras.FishEye;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.textures.image.ImageTexture;
import com.matrixpeckham.raytracer.textures.image.mappings.LightProbe;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
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
public class BuildFigure16 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 29.16
// As w build function does not use the "panoramic" option for the
// LightProbe mapping, the resulting image is the mirror image of Figure 11.8(b).
        int numSamples = 16;

        w.vp.setHres(900);
        w.vp.setVres(900);
        w.vp.setSamples(numSamples);
        w.vp.setPixelSize(1.0);

        w.tracer = new RayCast(w);

        FishEye fisheyePtr = new FishEye();
        fisheyePtr.setEye(new Point3D(0.0));
        fisheyePtr.setLookat(0, 0, -100);
        fisheyePtr.setFov(360);
        fisheyePtr.computeUVW();
        w.setCamera(fisheyePtr);

        // image:
        Image imagePtr = new Image();
        String path
                = "C:\\Users\\Owner\\Documents\\Ground Up raytracer\\Textures\\ppm\\";
        try {
//            imagePtr.loadPPMFile(new File(path+"uffizi_probe_large.ppm"));
            imagePtr.loadPPMFile(new File(path + "uffizi_probe_small.ppm"));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure16.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
        LightProbe lightProbePtr = new LightProbe();

        ImageTexture imageTexturePtr = new ImageTexture(imagePtr);
        imageTexturePtr.setMapping(lightProbePtr);

        SV_Matte svMattePtr = new SV_Matte();	// ka + kd > 1
        svMattePtr.setKa(1.0);
        svMattePtr.setKd(0.85);
        svMattePtr.setCd(imageTexturePtr);

        Sphere unitSpherePtr = new Sphere();
        unitSpherePtr.setShadows(false);

        Instance spherePtr = new Instance(unitSpherePtr);
        spherePtr.scale(1000000.0);
        spherePtr.setMaterial(svMattePtr);
        w.addObject(spherePtr);

    }

}
