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
package com.matrixpeckham.raytracer.build.figures.ch30;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Box;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.TInstance;
import com.matrixpeckham.raytracer.textures.procedural.CubicNoise;
import com.matrixpeckham.raytracer.textures.procedural.WrappedFBmTexture;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure06AB implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scenes for Figures 30.6(a) and 30.6(b)
        int numSamples = 16;

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setSamples(numSamples);

        w.tracer = new RayCast(w);
        w.backgroundColor = new RGBColor(Utility.BLACK);

        Ambient ambientPtr = new Ambient();
        ambientPtr.scaleRadiance(0.5);
        w.setAmbient(ambientPtr);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(0, 0, 100);
        pinholePtr.setLookat(new Point3D(0.0));
        pinholePtr.setViewDistance(30000.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        Directional lightPtr = new Directional();
        lightPtr.setDirection(0, 0, 1);
        lightPtr.scaleRadiance(3.0);
        lightPtr.setShadows(false);
        w.addLight(lightPtr);

	// wrapped noise texture
	// noise
        CubicNoise noisePtr = new CubicNoise();
        noisePtr.setNumOctaves(6);
        noisePtr.setGain(0.5);
        noisePtr.setLacunarity(2.0);

	// texture
        WrappedFBmTexture wrappedTexturePtr = new WrappedFBmTexture(noisePtr);
        wrappedTexturePtr.setColor(1.0, 0.85, 0.0);   // yellow
        wrappedTexturePtr.setExpansionNumber(10.0);

        double s = 0.5;      // uniform scaling factor
        double xs = 7.5;		// non-uniform scaling factor in x direction
        TInstance scaledTexturePtr = new TInstance(wrappedTexturePtr);
//        scaledTexturePtr.scale(s,s,s);				// for Figure 30.6(a)
	scaledTexturePtr.scale(s * xs, s, s);	// for Figure 30.6(b)

	// material:
        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.5);
        svMattePtr.setKd(0.85);
        svMattePtr.setCd(scaledTexturePtr);

	// box parameters
        Point3D p0 = new Point3D(-1.0);
        Point3D p1 = new Point3D(1.0);

        Box boxPtr = new Box(p0, p1);
        boxPtr.setMaterial(svMattePtr);
        w.addObject(boxPtr);
    }

}
