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
import com.matrixpeckham.raytracer.geometricobjects.ClippedObject;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.SV_DoubleSidedMatte;
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
public class BuildFigure25b implements BuildWorldFunction {

    @Override
    public void build(World w) {
        int numSamples = 1;

        w.vp.setHres(377);
        w.vp.setVres(482);
        w.vp.setSamples(numSamples);

        w.tracer = new RayCast(w);

        w.backgroundColor = Utility.BLACK;

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(10, 2, 10);
        pinholePtr.setViewDistance(1600.0);
        pinholePtr.setLookat(0, 2, 0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        Directional directionalPtr = new Directional();
        directionalPtr.setDirection(0, 1, 0);
        directionalPtr.scaleRadiance(4.5);
        directionalPtr.setShadows(true);
        w.addLight(directionalPtr);

        // image:
        Image clipTexture = new Image();
        String imagePath
                = "/Textures/ppm/";
        try {
            clipTexture.loadPPMFile(getClass().getClassLoader().getResourceAsStream(imagePath + "EarthMap.ppm"));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure22.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
        Image earthTexture = new Image();
        try {
            earthTexture.loadPPMFile(getClass().getClassLoader().getResourceAsStream(imagePath + "EarthHighRes.ppm"));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure25b.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

        ImageTexture earthTexturePtr = new ImageTexture();
        earthTexturePtr.setImage(earthTexture);
        earthTexturePtr.setMapping(new SphericalMap());

        ImageTexture clipTexturePtr = new ImageTexture();
        clipTexturePtr.setImage(clipTexture);
        clipTexturePtr.setMapping(new SphericalMap());

        SV_DoubleSidedMatte svMattePtr = new SV_DoubleSidedMatte();
        svMattePtr.setKa(0.1);
        svMattePtr.setKd(0.75);
        svMattePtr.setCd(earthTexturePtr);

        Sphere sphere = new Sphere();
        sphere.setMaterial(svMattePtr);
        ClippedObject clipObj = new ClippedObject(sphere, clipTexturePtr);
        Instance inst = new Instance(clipObj);
        inst.translate(0, 2, 0);

        w.addObject(inst);

        Matte mattePtr = new Matte();
        mattePtr.setCd(1, 1, 1);
        mattePtr.setKa(0.25);
        mattePtr.setKd(0.75);

        Plane planePtr1
                = new Plane(new Point3D(0, 0, 0), new Normal(0, 1, 0));
        planePtr1.setMaterial(mattePtr);
        w.addObject(planePtr1);

    }

}
