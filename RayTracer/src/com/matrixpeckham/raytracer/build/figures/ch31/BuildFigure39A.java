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
package com.matrixpeckham.raytracer.build.figures.ch31;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.CubicNoise;
import com.matrixpeckham.raytracer.textures.procedural.WrappedTwoColors;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure39A implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2008.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 31.39(a)
        int numSamples = 16;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setSamples(numSamples);

        w.backgroundColor = new RGBColor(0.5);
        w.tracer = new RayCast(w);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(0, 0, 100);
        pinholePtr.setLookat(new Point3D(0.0));
        pinholePtr.setViewDistance(9600.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        PointLight lightPtr = new PointLight();
        lightPtr.setLocation(20, 20, 40);
        lightPtr.scaleRadiance(3.0);
        w.addLight(lightPtr);

        // noise:
        CubicNoise noisePtr = new CubicNoise();
        noisePtr.setNumOctaves(6);
        noisePtr.setGain(0.5);
        noisePtr.setLacunarity(4.0);

        WrappedTwoColors texturePtr = new WrappedTwoColors(noisePtr);
        texturePtr.setColor1(1.0, 0.8, 0.0);		// gold
        texturePtr.setColor2(0.5, 0.75, 1.0);  	// light blue
        texturePtr.setExpansionNumber(2.0);

        // material:
        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.25);
        svMattePtr.setKd(0.9);
        svMattePtr.setCd(texturePtr);

        Sphere spherePtr = new Sphere(new Point3D(0.0), 3.0);
        spherePtr.setMaterial(svMattePtr);
        w.addObject(spherePtr);
    }

}
