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
package com.matrixpeckham.raytracer.build.figures.ch17;

import com.matrixpeckham.raytracer.cameras.Orthographic;
import com.matrixpeckham.raytracer.geometricobjects.compound.Grid;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Plane;
import com.matrixpeckham.raytracer.lights.AmbientOccluder;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;
import static com.matrixpeckham.raytracer.util.Utility.randDouble;
import static java.lang.Math.abs;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure13 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 17.13 
// This is the city scene without the checker textures and rendered with
// an orthographoc camera
        int numSamples = 64;

        w.vp.setHres(600);
        w.vp.setVres(400);
        w.vp.setSamples(numSamples);

        w.tracer = new RayCast(w);

        AmbientOccluder ambientOccluderPtr = new AmbientOccluder();
        ambientOccluderPtr.setSampler(new MultiJittered(numSamples));
//        ambientOccluderPtr.setMinAmount(1.0);   	// for Figure 17.13(a)
	ambientOccluderPtr.setMinAmount(0.25);		// for Figure 17.13(b)
        w.setAmbient(ambientOccluderPtr);

        Orthographic orthographicPtr = new Orthographic();
        w.vp.setPixelSize(0.31);
        orthographicPtr.setEye(100, 100, 50);
        orthographicPtr.setLookat(0, 10, 0);
        orthographicPtr.computeUVW();
        w.setCamera(orthographicPtr);

        PointLight lightPtr = new PointLight();
        lightPtr.setLocation(150, 500, 300);
        lightPtr.scaleRadiance(3.75);
        lightPtr.setShadows(true);
        w.addLight(lightPtr);

	// city parameters
        double a = 10;   // city block width:  xw extent
        double b = 12;	// city block length:  yw extent
        int numRows = 10;  	// number of blocks in the xw direction
        int numColumns = 12; 	// number of blocks in the zw direction
        double width = 7;	// building width: xw extent in range [min, a - offset]
        double length = 7;	// building length: zw extent in range [min, b - offset]
        double minSize = 6;	// mininum building extent in xw and yw directions
        double offset = 1.0;	// half the minimum distance between buildings
        double minHeight = 0.0; 	// minimum building height
        double maxHeight = 30; 	// maximum bulding height
        double height;						// the building height in range [minHeight, maxHeight]
        int numParkRows = 4;  	// number of blocks of park in xw direction
        int numParkColumns = 6;  	// number of blocks of park in xw direction
        int rowTest;					// there are no buildings in the park
        int columnTest;				// there are no buildings in the park
        double minColor = 0.1;  // prevents black buildings
        double maxColor = 0.9;	// prevents white buildings

        Utility.setRandSeed(15);  				// as the buildings' dimensions and colors are random, it's necessary to 
        // seed rand to keep these quantities the same at each run
        // if you leave this out, and change the number of samples per pixel,
        // these will change

	// the buildings are stored in a grid
        Grid gridPtr = new Grid();

        for (int r = 0; r < numRows; r++) // xw direction
        {
            for (int c = 0; c < numColumns; c++) {		// zw direction
                // determine if the block is in the park

                if ((r - numRows / 2) >= 0) {
                    rowTest = r - numRows / 2;
                } else {
                    rowTest = r - numRows / 2 + 1;
                }

                if ((c - numColumns / 2) >= 0) {
                    columnTest = c - numColumns / 2;
                } else {
                    columnTest = c - numColumns / 2 + 1;
                }

                if (abs(rowTest) >= (numParkRows / 2) || abs(columnTest)
                        >= (numParkColumns / 2)) {

                    Matte mattePtr = new Matte();
                    mattePtr.setKa(0.4);
                    mattePtr.setKd(0.6);
                    mattePtr.setCd(minColor + randDouble() * (maxColor
                            - minColor),
                            minColor + randDouble() * (maxColor - minColor),
                            minColor + randDouble() * (maxColor - minColor));

				// block center coordinates
                    double xc = a * (r - numRows / 2.0 + 0.5);
                    double zc = b * (c - numColumns / 2.0 + 0.5);

                    width = minSize + randDouble() * (a - 2 * offset - minSize);
                    length = minSize + randDouble() * (b - 2 * offset - minSize);

				// minimum building coordinates
                    double xmin = xc - width / 2.0;
                    double ymin = 0.0;
                    double zmin = zc - length / 2.0;

				// maximum building coordinates
                    height = minHeight + randDouble() * (maxHeight - minHeight);

				// The following is a hack to make the middle row and column of buildings higher
                    // on average than the other buildings. 
                    // This only works properly when there are three rows and columns of buildings
                    if (r == 1 || r == numRows - 2 || c == 1 || c == numColumns
                            - 2) {
                        height *= 1.5;
                    }

                    double xmax = xc + width / 2.0;
                    double ymax = height;
                    double zmax = zc + length / 2.0;
                    Box buildingPtr = new Box(new Point3D(xmin, ymin, zmin),
                            new Point3D(xmax, ymax, zmax));
                    buildingPtr.setMaterial(mattePtr);
                    gridPtr.addObject(buildingPtr);
                }
            }
        }

        gridPtr.setupCells();
        w.addObject(gridPtr);

	// render the park green
        Matte mattePtr1 = new Matte();
        mattePtr1.setKa(0.4);
        mattePtr1.setKd(0.5);
        mattePtr1.setCd(0.3, 0.5, 0.3);     // green 

        Box parkPtr = new Box(new Point3D(-a * numParkRows / 2, 0.0, -b
                * numParkColumns / 2),
                new Point3D(a * numParkRows / 2, 0.1, b * numParkColumns / 2));
        parkPtr.setMaterial(mattePtr1);
        w.addObject(parkPtr);

	// ground plane 
        Matte mattePtr2 = new Matte();
        mattePtr2.setKa(0.3);
        mattePtr2.setKd(0.5);
        mattePtr2.setCd(0.85);

        Plane planePtr = new Plane(new Point3D(0, 0.01, 0), new Normal(0, 1, 0));
        planePtr.setMaterial(mattePtr2);
        w.addObject(planePtr);

    }

}
