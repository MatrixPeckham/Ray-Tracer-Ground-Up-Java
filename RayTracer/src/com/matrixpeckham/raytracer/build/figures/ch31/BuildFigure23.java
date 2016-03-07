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
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.CubicNoise;
import com.matrixpeckham.raytracer.textures.procedural.TurbulenceTexture;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure23 implements BuildWorldFunction {

    @Override
    public void build(World w) {
        // 	Copyright (C) Kevin Suffern 2000-2008.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 31.23
// This scene illustrates turbulence.
// The noise details are different from the images in the book.
// The noise is also lighter because the original noise functions didn't
// scale the values to lie in the interval [0,1].
// This build function renders them at 600 x 600 pixels, instead of the
// original 150 x 150 pixels.
// There is no antialiasing.
        int numSamples = 1;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setSamples(numSamples);
        w.vp.setGamutDisplay(true);

        w.backgroundColor = Utility.BLACK;
        w.tracer = new RayCast(w);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(0, 0, 100);
        pinholePtr.setLookat(new Point3D(0));
        pinholePtr.setViewDistance(6000.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        Directional lightPtr = new Directional();
        lightPtr.setDirection(0, 0, 1);
        lightPtr.scaleRadiance(2.5);
        w.addLight(lightPtr);

        // noise:
        CubicNoise noisePtr = new CubicNoise();
//	noisePtr.setNumOctaves(1);				// for Figure 31.23(a)
//	noisePtr.setNumOctaves(2);				// for Figure 31.23(b)
//	noisePtr.setNumOctaves(3);				// for Figure 31.23(c)
        noisePtr.setNumOctaves(8);				// for Figure 31.23(c)
        noisePtr.setGain(0.5);
        noisePtr.setLacunarity(2.0);

        // texture:
        TurbulenceTexture texturePtr = new TurbulenceTexture(noisePtr);
        texturePtr.setColor(Utility.WHITE);
        texturePtr.setMinValue(0.0);
        texturePtr.setMaxValue(1.0);

        // material:
        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.25);
        svMattePtr.setKd(0.85);
        svMattePtr.setCd(texturePtr);

        Plane planePtr1 = new Plane(new Point3D(0.0), new Normal(0, 0, 1));
        planePtr1.setMaterial(svMattePtr);
        w.addObject(planePtr1);
    }

}
