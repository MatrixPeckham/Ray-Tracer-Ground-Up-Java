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
package com.matrixpeckham.raytracer.build.figures.ch24;

import com.matrixpeckham.raytracer.cameras.Spherical;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.compound.Grid;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Sphere;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.textures.image.ImageTexture;
import com.matrixpeckham.raytracer.textures.image.mappings.SphericalMap;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.abs;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure11 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 24.11
// This is the city scene used in the Chapter 6 page one figure, and Figures 11.11 and 11.12.
// Here, the buildings have Utility.random reflective materials
// Utility.randomFloat is called three times for each material, the same as for the matte materials
// in Figures 11.11 and 11.12. This keeps the buildings the same.
// In w figure, minColor = 0.5
        int numSamples = 16;

        w.vp.setHres(900);
        w.vp.setVres(600);
        w.vp.setSamples(numSamples);
        w.vp.setPixelSize(1.0);
        w.vp.setMaxDepth(20);

        w.tracer = new Whitted(w);

        Spherical sphericalPtr = new Spherical();
        sphericalPtr.setEye(10, 15, 13);
        sphericalPtr.setLookat(34, 15, 0);
        sphericalPtr.setHorizontalFov(180);
        sphericalPtr.setVerticalFov(120);
        sphericalPtr.computeUVW();
        w.setCamera(sphericalPtr);

        PointLight lightPtr2 = new PointLight();
        lightPtr2.setLocation(150, 500, 300);
        lightPtr2.scaleRadiance(3.75);
        lightPtr2.setShadows(true);
        w.addLight(lightPtr2);

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
        double minColor = 0.5;  // prevents black buildings
        double maxColor = 0.9;	// prevents white buildings

        Utility.setRandSeed(15);  				// as the buildings' dimensions and colors are Utility.random, it's necessary to 
        // seed Utility.rand to keep these quantities the same at each run
        // if you leave w out, and change the number of samples per pixel,
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

                    Reflective reflectivePtr = new Reflective();
                    reflectivePtr.setKa(0.0);
                    reflectivePtr.setKd(0.0);
                    reflectivePtr.setKs(0.0);
                    reflectivePtr.setCd(new RGBColor(0));
                    reflectivePtr.setKr(1.0);
                    reflectivePtr.setCr(new RGBColor(minColor + Utility.
                            randDouble() * (maxColor - minColor),
                            minColor + Utility.randDouble() * (maxColor
                            - minColor),
                            minColor + Utility.randDouble() * (maxColor
                            - minColor)));

				// block center coordinates
                    double xc = a * (r - numRows / 2.0 + 0.5);
                    double zc = b * (c - numColumns / 2.0 + 0.5);

                    width = minSize + Utility.randDouble() * (a - 2 * offset
                            - minSize);
                    length = minSize + Utility.randDouble() * (b - 2 * offset
                            - minSize);

				// minimum building coordinates
                    double xmin = xc - width / 2.0;
                    double ymin = 0.0;
                    double zmin = zc - length / 2.0;

				// maximum building coordinates
                    height = minHeight + Utility.randDouble() * (maxHeight
                            - minHeight);

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
                    //			buildingPtr.setMaterial(mattePtr);
                    buildingPtr.setMaterial(reflectivePtr);
                    gridPtr.addObject(buildingPtr);

                }
            }
        }

        gridPtr.setupCells();
        w.addObject(gridPtr);

	// render the park with small green checkers
        Checker3D checker3DPtr1 = new Checker3D();
        checker3DPtr1.setSize(5);
        checker3DPtr1.setColor1(0.35, 0.75, 0.35);
        checker3DPtr1.setColor2(0.3, 0.5, 0.3);

        SV_Matte svMattePtr1 = new SV_Matte();
        svMattePtr1.setKa(0.3);
        svMattePtr1.setKd(0.50);
        svMattePtr1.setCd(checker3DPtr1);

        Box parkPtr = new Box(new Point3D(-a * numParkRows / 2, 0.0, -b
                * numParkColumns / 2),
                new Point3D(a * numParkRows / 2, 0.1, b * numParkColumns / 2));
        parkPtr.setMaterial(svMattePtr1);
        w.addObject(parkPtr);

	// ground plane with checkers:
        Checker3D checker3DPtr2 = new Checker3D();
        checker3DPtr2.setSize(50);
        checker3DPtr2.setColor1(new RGBColor(0.7));
        checker3DPtr2.setColor2(new RGBColor(1));

        SV_Matte svMattePtr2 = new SV_Matte();
        svMattePtr2.setKa(0.30);
        svMattePtr2.setKd(0.40);
        svMattePtr2.setCd(checker3DPtr2);

        Plane planePtr = new Plane(new Point3D(0, 0.01, 0), new Normal(0, 1, 0));
        planePtr.setMaterial(svMattePtr2);
        w.addObject(planePtr);

        Image imagePtr = new Image();
        String path = "C:\\Users\\Owner\\Documents\\Ground Up raytracer\\Textures\\ppm\\";
        try {
            imagePtr.loadPPMFile(new File(path+"CloudsLowRes.ppm"));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure11.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

        SphericalMap sphericalMapPtr = new SphericalMap();

        ImageTexture imageTexturePtr = new ImageTexture(imagePtr);
        imageTexturePtr.setMapping(sphericalMapPtr);

        SV_Matte svMattePtr = new SV_Matte();
        svMattePtr.setKa(1.0);
        svMattePtr.setKd(0.85);
        svMattePtr.setCd(imageTexturePtr);

        Sphere unitSpherePtr = new Sphere();
        unitSpherePtr.setShadows(false);

        Instance spherePtr = new Instance(unitSpherePtr);
        spherePtr.scale(new Vector3D(1000000));
        spherePtr.setMaterial(svMattePtr);
        w.addObject(spherePtr);

    }

}
