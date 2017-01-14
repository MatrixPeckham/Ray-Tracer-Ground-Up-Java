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
import com.matrixpeckham.raytracer.geometricobjects.compound.SolidCylinder;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConcaveHemisphere;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.AmbientOccluder;
import com.matrixpeckham.raytracer.lights.EnvironmentLight;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.materials.SV_Emissive;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.textures.image.ImageTexture;
import com.matrixpeckham.raytracer.textures.image.mappings.HemisphericalMap;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Normal;
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
public class BuildFigure27 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 29.27
// The sky images are courtesy of Lopez-Fabrega Design, http://www.lfgrafix.com.
// You can download JPEG images of the skies from w site.
// PPM files of the skies are in the TextureFiles.zip download.
        int numSamples = 16;

        w.vp.setHres(600);
        w.vp.setVres(400);
        w.vp.setSamples(numSamples);
        w.vp.setMaxDepth(5);

        w.tracer = new Whitted(w);

        AmbientOccluder ambientOccluderPtr = new AmbientOccluder();
        ambientOccluderPtr.setSampler(new MultiJittered(numSamples));
        ambientOccluderPtr.setMinAmount(0.5);
        w.setAmbient(ambientOccluderPtr);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(100, 45, 100);
        pinholePtr.setLookat(-10, 40, 0);
        pinholePtr.setViewDistance(400);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        PointLight lightPtr = new PointLight();
        lightPtr.setLocation(150, 250, -150);
        lightPtr.scaleRadiance(1.5);
        lightPtr.setShadows(true);
        w.addLight(lightPtr);

        // image:
        Image imagePtr = new Image();
        String imagePath
                = "/Textures/ppm/";
        try {
            imagePtr.loadPPMFile(getClass().getClassLoader().getResourceAsStream(imagePath + "MorningSky.ppm"));
//            imagePtr.loadPPMFile(getClass().getClassLoader().getResourceAsStream(imagePath + "EveningSky.ppm"));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure27.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

        // image texture
        HemisphericalMap mapPtr = new HemisphericalMap();
        ImageTexture texturePtr = new ImageTexture();
        texturePtr.setImage(imagePtr);
        texturePtr.setMapping(mapPtr);

        // spatially varying material
        SV_Emissive svEmissivePtr = new SV_Emissive();
        svEmissivePtr.setCe(texturePtr);
        svEmissivePtr.scaleRadiance(1.0);

        EnvironmentLight environmentLightPtr = new EnvironmentLight();
        environmentLightPtr.setMaterial(svEmissivePtr);
        environmentLightPtr.setSampler(new MultiJittered(numSamples));
        environmentLightPtr.setShadows(true);
        w.addLight(environmentLightPtr);

        // large concave hemisphere for direct rendering of the skies
        ConcaveHemisphere unitHemispherePtr = new ConcaveHemisphere();
        Instance hemispherePtr = new Instance(unitHemispherePtr);
        hemispherePtr.setShadows(false);
        hemispherePtr.scale(1000000.0);
        hemispherePtr.rotateY(-30);
        hemispherePtr.setMaterial(svEmissivePtr);
        w.addObject(hemispherePtr);

	// the other objects
        // large sphere - reflective
        Reflective reflectivePtr1 = new Reflective();
        reflectivePtr1.setKr(0.9);
        reflectivePtr1.setCr(1.0, 0.75, 0.5);       // pink

        Sphere spherePtr1 = new Sphere(new Point3D(38, 20, -24), 20);
        spherePtr1.setMaterial(reflectivePtr1);
        w.addObject(spherePtr1);

        // small sphere - non reflective
        Matte mattePtr1 = new Matte();
        mattePtr1.setKa(0.2);
        mattePtr1.setKd(0.5);
        mattePtr1.setCd(0.85);

        Sphere spherePtr2 = new Sphere(new Point3D(34, 12, 13), 12);
        spherePtr2.setMaterial(mattePtr1);
        w.addObject(spherePtr2);

        // medium sphere - reflective
        Sphere spherePtr3 = new Sphere(new Point3D(-7, 15, 42), 16);
        spherePtr3.setMaterial(reflectivePtr1);
        w.addObject(spherePtr3);

        // cylinder - reflective
        Reflective reflectivePtr2 = new Reflective();
        reflectivePtr2.setKr(0.9);
        reflectivePtr2.setCr(1.0, 1.0, 0.5);   // lemon

        double bottom = 0.0;
        double top = 85.0;
        double radius = 22.0;
        SolidCylinder cylinderPtr = new SolidCylinder(bottom, top, radius);
        cylinderPtr.setMaterial(reflectivePtr2);
        w.addObject(cylinderPtr);

        // box - non reflective
        Matte mattePtr2 = new Matte();
        mattePtr2.setKa(0.2);
        mattePtr2.setKd(0.5);
        mattePtr2.setCd(0.95);

        Box boxPtr
                = new Box(new Point3D(-35, 0, -110), new Point3D(-25, 60, 65));
        boxPtr.setMaterial(mattePtr2);
        w.addObject(boxPtr);

        // ground plane:
        Matte mattePtr3 = new Matte();
        mattePtr3.setKa(0.15);
        mattePtr3.setKd(0.5);
        mattePtr3.setCd(0.7);

        Plane planePtr = new Plane(new Point3D(0, 0.01, 0), new Normal(0, 1, 0));
        planePtr.setMaterial(mattePtr3);
        w.addObject(planePtr);
    }

}
