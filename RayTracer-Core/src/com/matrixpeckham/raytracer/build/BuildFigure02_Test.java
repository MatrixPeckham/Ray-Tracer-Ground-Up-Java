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
package com.matrixpeckham.raytracer.build;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.BumpedObject;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.beveledobjects.BeveledBox;
import com.matrixpeckham.raytracer.geometricobjects.compound.Compound;
import com.matrixpeckham.raytracer.geometricobjects.compound.Grid;
import com.matrixpeckham.raytracer.geometricobjects.compound.TriangleMesh;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Disk;
import com.matrixpeckham.raytracer.geometricobjects.primitives.OpenCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Rectangle;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.geometricobjects.triangles.Triangle;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Dielectric;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.Phong;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.materials.SV_Phong;
import com.matrixpeckham.raytracer.materials.SV_Reflector;
import com.matrixpeckham.raytracer.textures.ConstantColor;
import com.matrixpeckham.raytracer.textures.TInstance;
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.textures.image.ImageTexture;
import com.matrixpeckham.raytracer.textures.image.mappings.CylindricalMap;
import com.matrixpeckham.raytracer.textures.image.mappings.SphericalMap;
import com.matrixpeckham.raytracer.textures.image.mappings.SquareMap;
import com.matrixpeckham.raytracer.textures.procedural.CubicNoise;
import com.matrixpeckham.raytracer.textures.procedural.DiskChecker;
import com.matrixpeckham.raytracer.textures.procedural.FBMBump;
import com.matrixpeckham.raytracer.textures.procedural.RampFBmTexture;
import com.matrixpeckham.raytracer.textures.procedural.TurbulenceTexture;
import com.matrixpeckham.raytracer.textures.procedural.Wood;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Mesh;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
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
public class BuildFigure02_Test implements BuildWorldFunction {

    @Override
    public void build(World world) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 29.1
// Although w build function doesn't render the textures, it defines the textured materials
// as well as the un-textured (plain) materials.
// The main differences between the build fuctions for Figures 29.1 and 29.2
// are the materials that are applied to the objects.
// The walls and the grout do not have textures.
// The bath water in Figure 29.2 is bump mapped, and w build function does not apply the bump map.
// The bath water is Ken Musgrave's beautifully simple water simulation implemented as a bump map.
// See Musgrave (2003b), where it's implemented as a RenderMan displacement map.
// Actually, it's only simple if you have implemented a vector fractional Brownian motion (fBm) noise function.
// This has a lot of code behind it. See Chapter 31.
// In my ray tracer, water is the only application of vector noise.
// Bump mapping didn't make it into the book, but I'll include the bump mapping classes in the Chapter 31 download.
// Figures 29.1 and 29.2 in the book were rendered before I was using the etas
// in the Dielectric material. The etas make the materials that are under the water darker, thereby
// making the water edge visible on the bath sides.
// I've compensated for w by making the filter color lighter.
// Noise details in the wood, sandstone, and water are slightly different from the orginal images.
// The sandstone is different because the original image used row 5 in the sandstone texture ramp image,
// but the current code uses row 0.
        int numSamples = 1;			// development
//s	int numSamples = 16;			// production

//	world.vp.setHres(475);				// development
//	world.vp.setVres(250);
        world.vp.setHres(950);				// production
        world.vp.setVres(500);
        world.vp.setSamples(numSamples);
        world.vp.setMaxDepth(2);

        Ambient ambientPtr = new Ambient();
        ambientPtr.scaleRadiance(0.5);
        world.setAmbient(ambientPtr);

        world.tracer = new Whitted(world);

        Pinhole pinholePtr = new Pinhole();
        pinholePtr.setEye(0, 15, 50);
        pinholePtr.setLookat(0.4, 3.0, 0.0);
//	pinholePtr.setViewDistance(600.0);		// for 475 x 250
        pinholePtr.setViewDistance(1200.0);   	// for 950 X 500
        pinholePtr.computeUVW();
        world.setCamera(pinholePtr);

        PointLight lightPtr = new PointLight();
        lightPtr.setLocation(14, 50, 50);
        lightPtr.scaleRadiance(3.0);
        lightPtr.setShadows(true);
        world.addLight(lightPtr);

        // ************************************************************************************************* walls
        // these are not textured
        // back wall
        Matte mattePtr1 = new Matte();
        mattePtr1.setKa(0.75);
        mattePtr1.setKd(0.5);
        mattePtr1.setCd(0.85);

        Plane backWallPtr = new Plane(new Point3D(0, 0, 0), new Normal(0, 0, 1));
        backWallPtr.setMaterial(mattePtr1);
        world.addObject(backWallPtr);

        // front wall
        Matte mattePtr2 = new Matte();
        mattePtr2.setKa(1.0);
        mattePtr2.setKd(0.5);
        mattePtr2.setCd(Utility.WHITE);

        Plane frontWallPtr = new Plane(new Point3D(0, 0, 51), new Normal(0, 0,
                -1));
        frontWallPtr.setMaterial(mattePtr2);
        world.addObject(frontWallPtr);

        // left wall
        Matte mattePtr3 = new Matte();
        mattePtr3.setKa(0.25);
        mattePtr3.setKd(0.75);
        mattePtr3.setCd(0.75, 1.0, 1.0);

        Plane leftWallPtr = new Plane(new Point3D(-15, 0, 0),
                new Normal(1, 0, 0));
        leftWallPtr.setMaterial(mattePtr3);
        world.addObject(leftWallPtr);

        // right wall
        Matte mattePtr4 = new Matte();
        mattePtr4.setKa(0.5);
        mattePtr4.setKd(0.5);
        mattePtr4.setCd(0.75, 1.0, 1.0);

        Plane rightWallPtr = new Plane(new Point3D(15, 0, 0), new Normal(-1, 0,
                0));
        rightWallPtr.setMaterial(mattePtr4);
        world.addObject(rightWallPtr);

        // there is no ceiling
        // ************************************************************************************************* floor planks
        // plain material
        Phong phongPtr = new Phong();
        phongPtr.setKa(0.5);
        phongPtr.setKd(1.0);
        phongPtr.setKs(0.2);
        phongPtr.setExp(20.0);
        phongPtr.setCd(0.5, 0.3, 0.1);

        // the floor is a simulation of wood planks using beveled boxes with random lengths in the x direction
        double x0 = -15.0;				// planks start at x = x0
        double z0 = 0.0;					// planks start at z = z0
        double minLength = 4.0;   				// minumum plank length in x direction
        double maxLength = 10.0;	  				// maximum plank length in x direction
        double plankThickness = 1.0;					// common plank thickness - in y direction
        double y0 = -plankThickness; 	// places top of planks at y = 0;
        double plankWidth = 1.0;  				// common plank width
        double plankBevel = 0.25;    				// the bevel radius
        int numXPlanks = 6;					// number of planks in the x direction (a single column)
        int numZColumns = 50;  					// number of plank columns in the z direction

        Grid planksPtr = new Grid();

        for (int iz = 0; iz < numZColumns; iz++) {     // to the front
            double p0x = x0;

            for (int ix = 0; ix < numXPlanks; ix++) {  // to the right
                TInstance woodPtr = new TInstance(new Wood(
                        new RGBColor(0.5, 0.3, 0.1), Utility.BLACK));
                woodPtr.scale(5.0);
                woodPtr.rotateY(90);
                Utility.setRandSeed(ix * 1000 + iz * 10000);  // w must go right here
                woodPtr.translate(10.0 * (2.0 * Utility.randDouble() - 1.0),
                        20.0 * (2.0 * Utility.randDouble() - 1.0), 30.0 * (2.0
                        * Utility.randDouble() - 1.0));

                SV_Phong svPhongPtr = new SV_Phong();
                svPhongPtr.setKa(0.5);
                svPhongPtr.setKd(1);
                svPhongPtr.setKs(0.2);
                svPhongPtr.setExp(20.0);
                svPhongPtr.setCd(woodPtr);
                svPhongPtr.setCs(new ConstantColor());   // Utility.WHITE, by default

                Utility.setRandSeed(ix * 10000 + iz * 100);
                double plankLength = minLength + Utility.randDouble()
                        * (maxLength - minLength);

                Point3D p0 = new Point3D(p0x, y0, iz * plankWidth);
                Point3D p1 = new Point3D(p0x + plankLength, y0 + plankThickness,
                        (iz + 1) * plankWidth);

                BeveledBox plankPtr = new BeveledBox(p0, p1, plankBevel);
//			plankPtr.setMaterial(phongPtr);			// plain
                plankPtr.setMaterial(svPhongPtr);		// textured
                planksPtr.addObject(plankPtr);

                p0x += plankLength;
            }
        }

        planksPtr.setupCells();
        world.addObject(planksPtr);

        // ************************************************************************************************* checkered cylinder
        // plain material
        Matte mattePtr5 = new Matte();
        mattePtr5.setKa(0.25);
        mattePtr5.setKd(0.95);
        mattePtr5.setCd(0.1, 0.4, 0.15);

        // textured material for the curved surface
        // w is a checker image
        Image imagePtr1 = new Image();
        String imagepath
                = "resources/Textures/ppm/";
        try {
            imagePtr1.loadPPMFile(Thread.currentThread().getContextClassLoader().getResourceAsStream(imagepath + "GreenAndYellow.ppm"));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure02_Test.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
        CylindricalMap cylindricalMapPtr = new CylindricalMap();
        ImageTexture imageTexturePtr1 = new ImageTexture(imagePtr1);
        imageTexturePtr1.setMapping(cylindricalMapPtr);

        SV_Matte svMattePtr1 = new SV_Matte();
        svMattePtr1.setKa(0.25);
        svMattePtr1.setKd(0.95);
        svMattePtr1.setCd(imageTexturePtr1);

        OpenCylinder cylinderPtr1 = new OpenCylinder(-1.0, 1.0, 1.0);  // default
//	cylinderPtr1.setMaterial(mattePtr5);		// plain
        cylinderPtr1.setMaterial(svMattePtr1); 	// textured

        Instance cylinderPtr2 = new Instance(cylinderPtr1);
        cylinderPtr2.translate(new Vector3D(0, 1, 0));
        cylinderPtr2.scale(2, 2.5, 2);
        cylinderPtr2.translate(new Vector3D(-11, 0, 12));
        world.addObject(cylinderPtr2);

        // a disk for the cylinder top
        // w is a 2D procedural checker texture designed to match the checkers
        // on the curved surface
        DiskChecker diskCheckerPtr = new DiskChecker();
        diskCheckerPtr.setNumAngularCheckers(20);
        diskCheckerPtr.setNumRadialCheckers(4);
        diskCheckerPtr.setAngularLineWidth(0.0);
        diskCheckerPtr.setRadialLineWidth(0.0);
        diskCheckerPtr.setColor1(0.08, 0.39, 0.14);  	// dark green
        diskCheckerPtr.setColor2(1.0, 1.0, 0.5);		// yellow

        SV_Matte svMattePtr2 = new SV_Matte();
        svMattePtr2.setKa(0.35);
        svMattePtr2.setKd(0.5);
        svMattePtr2.setCd(diskCheckerPtr);

        Disk diskPtr = new Disk();					// default - center (0, 0, 0), radius 1
//	diskPtr.setMaterial(mattePtr5);			// plain
        diskPtr.setMaterial(svMattePtr2);		// textured
        Instance cylinderTopPtr = new Instance(diskPtr);
        cylinderTopPtr.scale(2, 1, 2);
        cylinderTopPtr.translate(-11, 5, 12);
        world.addObject(cylinderTopPtr);

        // ************************************************************************************************* Earth sphere
        // plain material
        Matte mattePtr6 = new Matte();
        mattePtr6.setKa(0.5);
        mattePtr6.setKd(0.5);
        mattePtr6.setCd(0.2, 0.5, 1);

        // Earth image texture
        Image imagePtr2 = new Image();
        try {
//            imagePtr2.loadPPMFile(Thread.currentThread().getContextClassLoader().getResourceAsStream(imagepath+"EarthLowRes.ppm"));
            imagePtr2.loadPPMFile(Thread.currentThread().getContextClassLoader().getResourceAsStream(imagepath + "EarthHighRes.ppm"));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure02_Test.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
        SphericalMap sphericalMapPtr = new SphericalMap();
        ImageTexture imageTexturePtr2 = new ImageTexture(imagePtr2);
        imageTexturePtr2.setMapping(sphericalMapPtr);

        SV_Matte svMattePtr3 = new SV_Matte();
        svMattePtr3.setKa(0.5);
        svMattePtr3.setKd(0.95);
        svMattePtr3.setCd(imageTexturePtr2);

        Sphere spherePtr1 = new Sphere();
//	spherePtr1.setMaterial(mattePtr6);				// plain
        spherePtr1.setMaterial(svMattePtr3);			// textured with Earth image

        Instance spherePtr2 = new Instance(spherePtr1);
        spherePtr2.rotateY(75);
        spherePtr2.scale(3);
        spherePtr2.translate(-11, 8, 12);
        world.addObject(spherePtr2);

        // ************************************************************************************************* picture on back wall
        // w is initially constructed in the (x, z) plane, and then moved to the back wall
        // the picture and its frame are stored in a compound object:
        Compound framedPicturePtr = new Compound();

        // dimensions
        double a = 3.5;  	// +ve x coordinate of untransformed image
        double b = 5.05; 	// +ve z coordinate of untransformed image
        double w = 1.0;		// width of the frame

        // plain material picture
        Matte mattePtr7 = new Matte();
        mattePtr7.setKa(0.75);
        mattePtr7.setKd(0.5);
        mattePtr7.setCd(0.3, 0.65, 0.71);

        // the image is applied to a rectangle
        Image imagePtr3 = new Image();
        try {
            imagePtr3.loadPPMFile(Thread.currentThread().getContextClassLoader().getResourceAsStream(imagepath + "BlueGlass.ppm"));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure02_Test.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

        SquareMap squareMapPtr = new SquareMap();

        ImageTexture imageTexturePtr3 = new ImageTexture(imagePtr3);
        imageTexturePtr3.setMapping(squareMapPtr);

        SV_Matte svMattePtr4 = new SV_Matte();
        svMattePtr4.setKa(0.5);
        svMattePtr4.setKd(0.75);
        svMattePtr4.setCd(imageTexturePtr3);

        // construct the picture
        Rectangle rectanglePtr = new Rectangle();
//	rectanglePtr.setMaterial(mattePtr7);  	// plain
        rectanglePtr.setMaterial(svMattePtr4);  // textured with Blue Glass

        Instance picturePtr = new Instance(rectanglePtr);
        picturePtr.scale(a, 1, b);

        // construct the frame
        // plain material for the frame
        Matte mattePtr8 = new Matte();
        mattePtr8.setKa(0.75);
        mattePtr8.setKd(0.5);
        mattePtr8.setCd(0.5, 0.3, 0.14);

        // wood materials for the frame
        // for front and back sides
        TInstance woodPtr1 = new TInstance(new Wood(new RGBColor(0.55, 0.43,
                0.13), Utility.BLACK));
        woodPtr1.scale(new Vector3D(2));
        woodPtr1.rotateY(90);

        SV_Matte svMattePtr5 = new SV_Matte();
        svMattePtr5.setKa(1.0);
        svMattePtr5.setKd(1.0);
        svMattePtr5.setCd(woodPtr1);

        // for right and left sides
        TInstance woodPtr2 = new TInstance(new Wood(new RGBColor(0.55, 0.43,
                0.13), Utility.BLACK));
        woodPtr2.scale(2.0);

        SV_Matte svMattePtr6 = new SV_Matte();
        svMattePtr6.setKa(1.0);
        svMattePtr6.setKd(1.0);
        svMattePtr6.setCd(woodPtr2);

        // construct the frame: two triangles per side
        // front side: +ve z before any transformations
        Triangle trianglePtr1 = new Triangle(new Point3D(-a, 0, b), new Point3D(
                -a - w, 0, b + w), new Point3D(a + w, 0, b + w));
        trianglePtr1.setMaterial(svMattePtr5);
        framedPicturePtr.addObject(trianglePtr1);

        Triangle trianglePtr2 = new Triangle(new Point3D(-a, 0, b), new Point3D(
                a + w, 0, b + w), new Point3D(a, 0, b));
        trianglePtr2.setMaterial(svMattePtr5);
        framedPicturePtr.addObject(trianglePtr2);

        // back side: -ve z before any transformations
        Triangle trianglePtr3 = new Triangle(new Point3D(a, 0, -b), new Point3D(
                a + w, 0, -b - w), new Point3D(-a - w, 0, -b - w));
        trianglePtr3.setMaterial(svMattePtr5);
        framedPicturePtr.addObject(trianglePtr3);

        Triangle trianglePtr4 = new Triangle(new Point3D(a, 0, -b), new Point3D(
                -a - w, 0, -b - w), new Point3D(-a, 0, -b));
        trianglePtr4.setMaterial(svMattePtr5);
        framedPicturePtr.addObject(trianglePtr4);

        // right side: +ve x before any transformations
        Triangle trianglePtr5 = new Triangle(new Point3D(a, 0, b), new Point3D(a
                + w, 0, b + w), new Point3D(a + w, 0, -b - w));
        trianglePtr5.setMaterial(svMattePtr6);
        framedPicturePtr.addObject(trianglePtr5);

        Triangle trianglePtr6 = new Triangle(new Point3D(a, 0, b), new Point3D(a
                + w, 0, -b - w), new Point3D(a, 0, -b));
        trianglePtr6.setMaterial(svMattePtr6);
        framedPicturePtr.addObject(trianglePtr6);

        // left side: -ve x before any transformations
        Triangle trianglePtr7 = new Triangle(new Point3D(-a, 0, -b),
                new Point3D(-a - w, 0, -b - w), new Point3D(-a - w, 0, b + w));
        trianglePtr7.setMaterial(svMattePtr6);
        framedPicturePtr.addObject(trianglePtr7);

        Triangle trianglePtr8 = new Triangle(new Point3D(-a, 0, -b),
                new Point3D(-a - w, 0, b + w), new Point3D(-a, 0, b));
        trianglePtr8.setMaterial(svMattePtr6);
        framedPicturePtr.addObject(trianglePtr8);

        framedPicturePtr.setMaterial(mattePtr8);    // plain material for the frame - replaces all the wood textures - see Listing 19.13
        framedPicturePtr.addObject(picturePtr);

        Instance wallPicturePtr = new Instance(framedPicturePtr);
        wallPicturePtr.rotateY(90);
        wallPicturePtr.rotateX(90);
        wallPicturePtr.translate(-6, 8, 0.5);
        world.addObject(wallPicturePtr);

        // ************************************************************************************************* bunny
        // plain material for the bunny
        Matte mattePtr9 = new Matte();
        mattePtr9.setKa(0.25);
        mattePtr9.setKd(0.75);
        mattePtr9.setCd(0.8);

        // ramp based marble texture
        Image imagePtr4 = new Image();
        try {
            imagePtr4.loadPPMFile(Thread.currentThread().getContextClassLoader().getResourceAsStream(imagepath + "GrayMarbleRamp.ppm"));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure02_Test.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

        int numOctaves = 4;
        double fbmAmount = 3.0;
        TInstance marblePtr = new TInstance(new RampFBmTexture(imagePtr4,
                numOctaves, fbmAmount));
        marblePtr.scale(0.0075);  // the bunny is small
        marblePtr.translate(0.5, 0.0, 0.0);
        marblePtr.rotateX(100);
        marblePtr.rotateZ(30);
        marblePtr.rotateY(20);

        // marble material
        SV_Matte svMattePtr7 = new SV_Matte();
        svMattePtr7.setKa(0.25);
        svMattePtr7.setKd(0.75);
        svMattePtr7.setCd(marblePtr);

        Mesh meshPtr = new Mesh();
        String fileName = "Bunny4K.ply"; 		// 4000 triangles
        //	String fileName = "Bunny10K.ply"; 	// 10000 triangles
        //	String fileName = "Bunny16K.ply"; 	// 16000 triangles
        //String fileName = "Bunny69K.ply"; 	// 69000 triangles
        String meshpath
                = "resources/Models/Stanford Bunny/";

        TriangleMesh gridPtr = new TriangleMesh(new Mesh());
        try {
//	bunnyPtr.reverseMeshNormals();				// you must use w for the 10K model
//	bunnyPtr.readFlatTriangles(fileName);

            gridPtr.readSmoothTriangles(Thread.currentThread().getContextClassLoader().getResourceAsStream(meshpath + fileName));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure02_Test.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
//        gridPtr.setMaterial(mattePtr9);			// plain
        gridPtr.setMaterial(svMattePtr7); 		// textured
        gridPtr.setupCells();

        Instance bunnyPtr = new Instance(gridPtr);
        bunnyPtr.scale(47.0);
        bunnyPtr.translate(-3.25, -1.65, 10);
        world.addObject(bunnyPtr);

        // ************************************************************************************************* bath sides
        // the front and back sides of the bath run right across the bath in the x direction
        // the left and right sides fit between the front and back in the z direction
        // the sides are rows of beveled boxes with a random sandstone texture applied
        // these are stored in a single grid
        double bathXmin = 1.0;
        double bathZmin = 0.0;
        double bathXmax = 15.0;
        double bathZmax = 15.0;
        double xSize = bathXmax - bathXmin;
        double zSize = bathZmax - bathZmin;
        double thickness = 1.5;  	// common box thickness = side thickness
        double bathHeight = 2.5;		// common box height
        double bathBevelRadius = 0.25;
        int numXboxes = 4;		// number of boxes along the back and front sides
        int numZboxes = 3;		// number of boxes along the left and right sides

        double bathKa = 0.5;  	// common material property
        double bathKd = 0.85; 	// common material property

        // plain material
        Matte mattePtr10 = new Matte();
        mattePtr10.setKa(bathKa);
        mattePtr10.setKd(bathKd);
        mattePtr10.setCd(0.53, 0.51, 0.45);

        // sandstone texture
        Image imagePtr5 = new Image();
        try {
            imagePtr5.loadPPMFile(Thread.currentThread().getContextClassLoader().getResourceAsStream(imagepath + "sandstone_ramp1.ppm"));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure02_Test.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

        numOctaves = 4;
        fbmAmount = 0.1;

        Grid bathPtr = new Grid();

        // build back - runs in x direction
        for (int j = 0; j < numXboxes; j++) {

            // put a sandstone texture with a random world.set of intrinsic transformations on the beveled boxes
            TInstance sandstonePtr = new TInstance(new RampFBmTexture(imagePtr5,
                    numOctaves, fbmAmount));
            sandstonePtr.scale(2.0);
            Utility.setRandSeed(j * 10L);
            sandstonePtr.rotateX(20.0 * (2.0 * Utility.randDouble() - 1.0));
            sandstonePtr.rotateY(30.0 * (2.0 * Utility.randDouble() - 1.0));
            sandstonePtr.rotateZ(45.0 * (2.0 * Utility.randDouble() - 1.0));
            sandstonePtr.translate(10.0 * (2.0 * Utility.randDouble() - 1.0),
                    20.0 * (2.0 * Utility.randDouble() - 1.0), 30.0 * (2.0
                    * Utility.randDouble() - 1.0));

            SV_Matte svMattePtr = new SV_Matte();
            svMattePtr.setKa(bathKa);
            svMattePtr.setKd(bathKd);
            svMattePtr.setCd(sandstonePtr);

            BeveledBox boxPtr = new BeveledBox(new Point3D(bathXmin + j * (xSize
                    / numXboxes), 0, bathZmin),
                    new Point3D(bathXmin + (j + 1) * (xSize / numXboxes),
                            bathHeight, bathZmin + thickness),
                    bathBevelRadius);
            boxPtr.setMaterial(svMattePtr);
            bathPtr.addObject(boxPtr);

        }
        // build front - runs in xw direction

        for (int j = 0; j < numXboxes; j++) {

            TInstance sandstonePtr = new TInstance(new RampFBmTexture(imagePtr5,
                    numOctaves, fbmAmount));
            sandstonePtr.scale(2.0);
            Utility.setRandSeed(j * 1000000L);
            sandstonePtr.rotateX(20.0 * (2.0 * Utility.randDouble() - 1.0));
            sandstonePtr.rotateY(30.0 * (2.0 * Utility.randDouble() - 1.0));
            sandstonePtr.rotateZ(45.0 * (2.0 * Utility.randDouble() - 1.0));
            sandstonePtr.translate(10.0 * (2.0 * Utility.randDouble() - 1.0),
                    20.0 * (2.0 * Utility.randDouble() - 1.0), 30.0 * (2.0
                    * Utility.randDouble() - 1.0));

            SV_Matte svMattePtr = new SV_Matte();
            svMattePtr.setKa(bathKa);
            svMattePtr.setKd(bathKd);
            svMattePtr.setCd(sandstonePtr);

            BeveledBox boxPtr = new BeveledBox(new Point3D(bathXmin + j * (xSize
                    / numXboxes), 0, bathZmax - thickness),
                    new Point3D(bathXmin + (j + 1) * (xSize / numXboxes),
                            bathHeight, bathZmax),
                    bathBevelRadius);
            boxPtr.setMaterial(svMattePtr);
            bathPtr.addObject(boxPtr);

        }

        // build left side - runs in yw direction
        for (int j = 0; j < numZboxes; j++) {

            TInstance sandstonePtr = new TInstance(new RampFBmTexture(imagePtr5,
                    numOctaves, fbmAmount));
            sandstonePtr.scale(2.0);
            Utility.setRandSeed(j * 1000);
            sandstonePtr.rotateX(20.0 * (2.0 * Utility.randDouble() - 1.0));
            sandstonePtr.rotateY(30.0 * (2.0 * Utility.randDouble() - 1.0));
            sandstonePtr.rotateZ(45.0 * (2.0 * Utility.randDouble() - 1.0));
            sandstonePtr.translate(10.0 * (2.0 * Utility.randDouble() - 1.0),
                    20.0 * (2.0 * Utility.randDouble() - 1.0), 30.0 * (2.0
                    * Utility.randDouble() - 1.0));

            SV_Matte svMattePtr = new SV_Matte();
            svMattePtr.setKa(bathKa);
            svMattePtr.setKd(bathKd);
            svMattePtr.setCd(sandstonePtr);

            BeveledBox boxPtr = new BeveledBox(new Point3D(bathXmin, 0, bathZmin
                    + thickness + j * ((zSize - 2 * thickness) / numZboxes)),
                    new Point3D(bathXmin + thickness, bathHeight, bathZmin
                            + thickness + (j + 1) * ((zSize - 2 * thickness)
                            / numZboxes)),
                    bathBevelRadius);
            boxPtr.setMaterial(svMattePtr);
            bathPtr.addObject(boxPtr);

        }

        // build right side - runs in yw direction
        for (int j = 0; j < numZboxes; j++) {

            TInstance sandstonePtr = new TInstance(new RampFBmTexture(imagePtr5,
                    numOctaves, fbmAmount));
            sandstonePtr.scale(2.0);
            Utility.setRandSeed(j * 10000);
            sandstonePtr.rotateX(20.0 * (2.0 * Utility.randDouble() - 1.0));
            sandstonePtr.rotateY(30.0 * (2.0 * Utility.randDouble() - 1.0));
            sandstonePtr.rotateZ(45.0 * (2.0 * Utility.randDouble() - 1.0));
            sandstonePtr.translate(10.0 * (2.0 * Utility.randDouble() - 1.0),
                    20.0 * (2.0 * Utility.randDouble() - 1.0), 30.0 * (2.0
                    * Utility.randDouble() - 1.0));

            SV_Matte svMattePtr = new SV_Matte();
            svMattePtr.setKa(bathKa);
            svMattePtr.setKd(bathKd);
            svMattePtr.setCd(sandstonePtr);

            BeveledBox boxPtr = new BeveledBox(new Point3D(bathXmax - thickness,
                    0, bathZmin + thickness + j * ((zSize - 2 * thickness)
                    / numZboxes)),
                    new Point3D(bathXmax, bathHeight, bathZmin + thickness + (j
                            + 1) * ((zSize - 2 * thickness) / numZboxes)),
                    bathBevelRadius);
            boxPtr.setMaterial(svMattePtr);
            bathPtr.addObject(boxPtr);
        }

        //bathPtr.setMaterial(mattePtr10);  // world.sets plain material for all boxes
        bathPtr.setupCells();
        world.addObject(bathPtr);

        // ************************************************************************************************* bath water
        // the bath water
        // w is a bump mapped rectangle with a transparent material
        double c = 1.2;
        RGBColor waterColor = new RGBColor(0.50 * c, 0.8 * c, 0.8 * c);
        Dielectric waterPtr = new Dielectric();
        waterPtr.setIorIn(1.33);		// water
        waterPtr.setIorOut(1.0);		// air
        waterPtr.setCfIn(waterColor);
        waterPtr.setCfOut(Utility.WHITE);
        waterPtr.setShadows(false);

        double waterHeight = bathHeight - 0.45;
        Rectangle waterSurfacePtr = new Rectangle(new Point3D(bathXmin
                + thickness - bathBevelRadius, waterHeight, bathZmin + thickness
                - bathBevelRadius),
                new Vector3D(0, 0, zSize - 2 * thickness + 2 * bathBevelRadius),
                new Vector3D(xSize - 2 * thickness + 2 * bathBevelRadius, 0, 0),
                new Normal(0, 1, 0));

        waterSurfacePtr.setShadows(false);
        waterSurfacePtr.setMaterial(waterPtr);
	//world.addObject(waterSurfacePtr);						// no bump map - use w for Figure 29.1

        // the bump mapped object
        numOctaves = 2;
        double lacunarity = 2.0;
        double gain = 0.33333;
        double perturbationAmount = 1.0;

        FBMBump fBmBumpPtr = new FBMBump(numOctaves, lacunarity, gain,
                perturbationAmount);    // Ken Musgrave's water

        BumpedObject bumpedWaterPtr = new BumpedObject();
        bumpedWaterPtr.setMaterial(waterPtr);
//        bumpedWaterPtr.setMaterial(phongPtr);
        bumpedWaterPtr.setObject(waterSurfacePtr);
        bumpedWaterPtr.setBumpMap(fBmBumpPtr);
        world.addObject(bumpedWaterPtr);						// use w for Figure 29.2

        // ************************************************************************************************* tiles
        // build reflective tiles on the wall behind the bath
        // these are beveled boxes with a spatially varying reflective material
        // there is grout between the tiles which is a rectangle just in front of the back wall
        // because the reflective material has no ambient, there are Utility.BLACK areas in the image where the tiles are reflected in each other
        // w can be minimised by placing the back wall at z = 0.1 for a bevel radius of 0.25 and thickness of 0.6
        // w is where the grout rectangle should be.
        double tileSize = 3.25;	  						// tiles are square with dimensions tileSize in the x and y directions
        double tileThickness = 0.6;							// tile thickness in the z direction
        double groutWidth = 0.25;							// the grout width
        double tilesXmin = bathXmin;					// left boundary of the tiles is at left side of bath
        double tilesYmin = bathHeight + groutWidth; 	// tiles start at bath height + the grout width
        double tilesZmin = -tileThickness / 2.0; 		// back wall goes through the middle of the tiles
        double tilesZmax = tileThickness / 2.0;
        double tileBevelRadius = 0.25;    						// the bevel radius
        int numXTiles = 4;							// number of tiles in the x direction
        int numYTiles = 3;							// number of tiles in the y direction

        // plain material
        Reflective reflectivePtr = new Reflective();
        reflectivePtr.setCr(0.75, 1.0, 0.85);
        reflectivePtr.setKr(1.0);

        Grid tilesPtr = new Grid();

        for (int ix = 0; ix < numXTiles; ix++) {    	// across
            for (int iy = 0; iy < numYTiles; iy++) {  // up

                // the noise
                CubicNoise noisePtr = new CubicNoise();
                noisePtr.setNumOctaves(4);
                noisePtr.setGain(0.5);
                noisePtr.setLacunarity(2.0);

                // the texture:
                TurbulenceTexture texturePtr = new TurbulenceTexture(noisePtr);
                texturePtr.setColor(0.75, 1.0, 0.85);		// green
                texturePtr.setMinValue(0.25);
                //		texturePtr.setMaxValue(1.0);   // original - now renders darker
                texturePtr.setMaxValue(1.2);   // new value - lighter

                TInstance scaledTexturePtr = new TInstance(texturePtr);
                scaledTexturePtr.scale(0.5);

                // the material
                SV_Reflector reflectorPtr = new SV_Reflector();
                reflectorPtr.setKr(1.0);
                reflectorPtr.setCr(scaledTexturePtr);

                // the tiles
                Point3D p0 = new Point3D(tilesXmin + ix
                        * (tileSize + groutWidth), tilesYmin + iy * (tileSize
                        + groutWidth), tilesZmin);
                Point3D p1 = new Point3D(tilesXmin + (ix + 1) * tileSize + ix
                        * groutWidth, tilesYmin + (iy + 1) * tileSize + iy
                        * groutWidth, tilesZmax);
                BeveledBox tilePtr = new BeveledBox(p0, p1, tileBevelRadius);
//			tilePtr.setMaterial(reflectivePtr);  	// plain
                tilePtr.setMaterial(reflectorPtr);		// textured
                tilesPtr.addObject(tilePtr);
            }
        }

        tilesPtr.setupCells();

        // ************************************************************************************************* grout
        // w is not textured
        Matte mattePtr11 = new Matte();
        mattePtr11.setKa(0.5);
        mattePtr11.setKd(0.75);
        mattePtr11.setCd(0.92, 0.85, 0.6);

        Rectangle groutPtr = new Rectangle(new Point3D(tilesXmin, bathHeight,
                tileThickness / 2.0 - tileBevelRadius * 0.666),
                new Vector3D(numXTiles * (tileSize + groutWidth), 0.0, 0.0),
                new Vector3D(0.0, numYTiles * (tileSize + groutWidth), 0.0));
        groutPtr.setMaterial(mattePtr11);

        Compound tilesAndGroutPtr = new Compound();
        tilesAndGroutPtr.addObject(tilesPtr);
        tilesAndGroutPtr.addObject(groutPtr);

        Instance instancePtr = new Instance(tilesAndGroutPtr);
        instancePtr.translate(0, 0, -0.99 * (tileThickness / 2.0
                - tileBevelRadius * 0.666));
        world.addObject(instancePtr);

    }

    private static final Logger LOG
            = Logger.getLogger(BuildFigure02_Test.class.getName());

}
