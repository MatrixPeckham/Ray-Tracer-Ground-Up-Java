/*
 * Copyright (C) 2015 William Matrix Peckham
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
package com.matrixpeckham.raytracer.build.figures.ch08;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildPageOneImage implements BuildWorldFunction {

    @Override
    public void build(World w) {
        int numSamples = 1;

        w.vp.setHres(500);
        w.vp.setVres(500);
        w.vp.setSamples(numSamples);
        w.vp.setPixelSize(1.0);

        w.backgroundColor = Utility.BLACK;
        w.tracer = new RayCast(w);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(0, 2, 0);
        pinholePtr.setLookat(new Point3D(0));
        pinholePtr.setViewDistance(73.0);
        pinholePtr.computeUVW();
        w.setCamera(pinholePtr);

        Directional lightPtr = new Directional();
        lightPtr.setDirection(0, 1, 1);
        lightPtr.scaleRadiance(3.0);
        w.addLight(lightPtr);

        // construct the spheres
        double d = 2.0; 		// sphere center spacing
        double r = 0.75; 	// sphere radius
        double xc, yc; 		// sphere center coordinates
        int numRows = 5;
        int numColumns = 5;

        Checker3D checkerPtr = new Checker3D();
        checkerPtr.setSize(0.5);
        checkerPtr.setColor1(0, 0.4, 0.8);
        checkerPtr.setColor2(1, 1, 1);

        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(0.2);
        svMattePtr.setKd(0.8);
        svMattePtr.setCd(checkerPtr);

        for (int k = 0; k < numColumns; k++) { 		// up
            for (int j = 0; j < numRows; j++) {	// across
                Sphere spherePtr = new Sphere();
                xc = d * (j - (numColumns - 1) / 2.0);
                yc = d * (k - (numRows - 1) / 2.0);
                spherePtr.setCenter(new Point3D(xc, 0, yc));
                spherePtr.setRadius(r);
                spherePtr.setMaterial(svMattePtr);
                w.addObject(spherePtr);
            }
        }

    }

}
