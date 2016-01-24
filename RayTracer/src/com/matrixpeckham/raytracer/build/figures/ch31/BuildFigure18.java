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
import com.matrixpeckham.raytracer.geometricobjects.primitives.Box;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.CubicNoise;
import com.matrixpeckham.raytracer.textures.procedural.FBmTexture;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure18 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// This builds the scene for Figure 31.18

// 	Copyright (C) Kevin Suffern 2000-2008.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.
// The version of CubicNoise::valueNoise in Listing 31.6 was used for w.
// This does not clamp the returned value.
        int numSamples = 9;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setSamples(numSamples);

        w.backgroundColor = Utility.BLACK;
        w.tracer = new RayCast(w);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(0, 0, 100);
        pinholePtr.setLookat(new Point3D(0.0));
        pinholePtr.setViewDistance(3000.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        Directional lightPtr = new Directional();
        lightPtr.setDirection(0, 0, 1);
        lightPtr.scaleRadiance(2.5);
        w.addLight(lightPtr);

	// noise:
        CubicNoise noisePtr = new CubicNoise();
        noisePtr.setNumOctaves(1);
        noisePtr.setGain(0.5);			// not relevant for one octave
        noisePtr.setLacunarity(2.0);		// not relevant for one octave	

	// texture:
        FBmTexture texturePtr = new FBmTexture(noisePtr);
        texturePtr.setColor(Utility.WHITE);
        texturePtr.setMinValue(0.0);
        texturePtr.setMaxValue(1.0);

	// material:
        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.25);
        svMattePtr.setKd(0.85);
        svMattePtr.setCd(texturePtr);

        Box boxPtr1 = new Box(new Point3D(-10.0), new Point3D(10.0));
        boxPtr1.setMaterial(svMattePtr);
        w.addObject(boxPtr1);
    }

}
