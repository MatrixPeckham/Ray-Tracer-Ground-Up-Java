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

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.compound.TriangleMesh;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Rectangle;
import com.matrixpeckham.raytracer.lights.AmbientOccluder;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
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
public class BuildFigure12 implements BuildWorldFunction{

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 17.12
// Triangle meshes are discussed in Chapter 23
// The bunny PLY files are in the Chapter 23 archive

	int numSamples = 256; 
	
	w.vp.setHres(400);
	w.vp.setVres(400);
	w.vp.setSamples(numSamples);
	
	w.tracer = new RayCast(w);
	
	MultiJittered samplerPtr = new MultiJittered(numSamples);
	
	AmbientOccluder aclPtr = new AmbientOccluder();
//	aclPtr.setMinAmount(1.0);    	// for Figure 17.12(a)
//	aclPtr.setMinAmount(0.25);		// for Figure 17.12(b)
	aclPtr.setMinAmount(0.0);		// for Figure 17.12(c)
	aclPtr.setSampler(samplerPtr);
	w.setAmbient(aclPtr);
	
	Pinhole pinholePtr = new Pinhole();
	
	// for regular view	
	
	pinholePtr.setEye(20, 10, 40);
	pinholePtr.setLookat(-0.025, 0.11, 0.0);  
	pinholePtr.setViewDistance(70000);	
	pinholePtr.computeUVW();
	w.setCamera(pinholePtr);
	
	
	Directional lightPtr1 = new Directional();
	lightPtr1.setDirection(20, 40, 20);
	lightPtr1.scaleRadiance(1.5); 
	lightPtr1.setShadows(true);
	w.addLight(lightPtr1);

	
	double ka = 0.5;		// used for all materials

	Matte mattePtr1 = new Matte();			
	mattePtr1.setKa(ka);		
	mattePtr1.setKd(0.5);
	mattePtr1.setCd(0.5, 0.75, 1);   // pale blue for bunny
	String path = "C:\\Users\\Owner\\Documents\\Ground Up raytracer\\Models\\Stanford Bunny\\";
//	String fileName = "Bunny4K.ply"; 		// 4000 triangles
	String fileName = "Bunny10K.ply"; 	// 10000 triangles - needs the normals reversed
//	String fileName = "Bunny16K.ply"; 	// 16000 triangles
//	String fileName = "Bunny69K.ply"; 	// 69000 triangles
	
	TriangleMesh bunnyPtr = new TriangleMesh();
	bunnyPtr.reverseNormal();// only required for the Bunny10K.ply file
        try {
            //	bunnyPtr.readFlatTriangles(fileName);		// read PLY file
            bunnyPtr.readSmoothTriangles(new File(path+fileName));	// read PLY file
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure12.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
	bunnyPtr.setMaterial(mattePtr1);
	bunnyPtr.setupCells();
	
	Instance rotatedBunnyPtr = new Instance(bunnyPtr);
	rotatedBunnyPtr.setMaterial(mattePtr1);
	rotatedBunnyPtr.rotateY(40);
	w.addObject(rotatedBunnyPtr);
	
	
	// rectangle parameters
	
	Point3D p0=new Point3D(-0.13, 0.033, -0.1);  	// common corner
	double height = 0.25;  				// y direction
	double width = 0.45;  				// x direction	
	double depth = 0.45;   				// z direction	

	// horizontal rectangle 
	
	Matte mattePtr2 = new Matte();			
	mattePtr2.setKa(ka);		
	mattePtr2.setKd(0.5);
	mattePtr2.setCd(Utility.WHITE);
	
	Rectangle rectanglePtr1 = new Rectangle(p0, new Vector3D(0, 0,depth), new Vector3D(width, 0, 0));
	rectanglePtr1.setMaterial(mattePtr2);
	w.addObject(rectanglePtr1);	
	
	// rectangle perpendicular to x axis
	
	Matte mattePtr3 = new Matte();			
	mattePtr3.setKa(ka);		
	mattePtr3.setKd(0.75);
	mattePtr3.setCd(0.5, 1, 0.75);
	
	Rectangle rectanglePtr2 = new Rectangle(p0, new Vector3D(0, height, 0), new Vector3D(0, 0, depth));
	rectanglePtr2.setMaterial(mattePtr3);
	w.addObject(rectanglePtr2);	
	
	// rectangle perpendicular to w axis
	
	Matte mattePtr4 = new Matte();			
	mattePtr4.setKa(ka);		
	mattePtr4.setKd(0.5);
	mattePtr4.setCd(1, 1, 0.5);
	
	Rectangle rectanglePtr3 = new Rectangle(p0, new Vector3D(width, 0, 0), new Vector3D(0, height, 0));
	rectanglePtr3.setMaterial(mattePtr4);
	w.addObject(rectanglePtr3);


    }
    
}
