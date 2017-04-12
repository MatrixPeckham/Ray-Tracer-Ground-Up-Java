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
package com.matrixpeckham.raytracer.build.figures.ch23;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.compound.Grid;
import com.matrixpeckham.raytracer.geometricobjects.compound.TriangleMesh;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Phong;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Mesh;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import static com.matrixpeckham.raytracer.util.Utility.randDouble;
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
public class BuildFigure12 implements BuildWorldFunction {

    @Override
    public void build(World w) {

	int numSamples = 1;
	
	w.vp.setHres(500);
	w.vp.setVres(400);
	w.vp.setSamples(numSamples);
	
	w.backgroundColor = Utility.BLACK;
	w.tracer = new RayCast(w);
	
	// the camera is defined at the bottom
		
	Directional lightPtr1 = new Directional();
	lightPtr1.setDirection(20, 40, 20);
	lightPtr1.scaleRadiance(3.0); 
	w.addLight(lightPtr1);
	
	
	Phong phongPtr1 = new Phong();
	phongPtr1.setKa(0.2);
	phongPtr1.setKd(0.5);
	phongPtr1.setCd(new RGBColor(1.0));
	phongPtr1.setKs(0.4);
	phongPtr1.setExp(20);
	String path="resources/Models/Stanford Bunny/";
//	String fileName = "Bunny4K.ply";			// low res Stanford bunny
	String fileName = "Bunny16K.ply";			// medium res Stanford bunny
//	String fileName = "Bunny69K.ply";			// high res Stanford bunny

	TriangleMesh bunnyPtr = new TriangleMesh(new Mesh());
        try {
            bunnyPtr.readFlatTriangles(Thread.currentThread().getContextClassLoader().getResourceAsStream(path+fileName));		// read PLY file
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure12.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
//	bunnyPtr.readSmoothTriangles(path+fileName);		// read PLY file
//	bunnyPtr.setMaterial(phongPtr);				// only use this if you want white bunnies
	bunnyPtr.setupCells();
		
	int 	numLevels 			= 8;		// number of levels
	int 	instancesGridRes 	= 10; 		// each level consists of instancesGridRes x instancesGridRes instances
	double 	delta 				= 0.1; 		// gap between instances
	double 	gap 				= 0.08; 	// gap between bunnies in the level 0 grid
	double 	size 				= 0.1;    	// bunny size
	double 	mcx 				= 0.5; 		// camera coordinates as multiple mcx of grid size - allows us to keep the whole grid in view
	
	Utility.setRandSeed(1000);
	
	Grid currentGridPtr = bunnyPtr;							// just the bunny
	
	for (int level = 0; level < numLevels; level++) {	
		Grid instanceGridPtr = new Grid();						// temporary grid
		
		for (int i = 0; i < instancesGridRes; i++) {   		// xw direction
			for (int k = 0; k < instancesGridRes; k++) {   	// zw direction
			
				Phong phongPtr = new Phong();
				phongPtr.setKa(0.2);
				phongPtr.setKd(0.5);
				phongPtr.setCd(randDouble(), randDouble(), randDouble());
				phongPtr.setKs(0.4);
				phongPtr.setExp(20);
			
				Instance instancePtr = new Instance();
				instancePtr.setObject(currentGridPtr); 	// w.add whole grid up to this level
				instancePtr.setMaterial(phongPtr);
				instancePtr.translate(i * (size + gap), 0.0, k * (size + gap)); 
				instancePtr.computeBoundingBox();
				instanceGridPtr.addObject(instancePtr);
			}
		}
		
		size = instancesGridRes * size + (instancesGridRes - 1) * gap;  
		gap = delta * size;
		instanceGridPtr.setupCells();
		currentGridPtr = instanceGridPtr;					// now the whole grid up to this level	
	}
	
	w.addObject(currentGridPtr); 								// the whole n-level grid
	
	
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(7 * mcx * size, 4 * mcx * size, 5 * mcx * size); 
	pinholePtr.setLookat(mcx * size, 0.0, mcx * size);     				// use this for the whole grid 
//	pinholePtr.setLookat(mcx * size, 0.4 * mcx * size, mcx * size);   	// use this for the zooms
	pinholePtr.setViewDistance(1400);   		// Figure 23.12(a) the whole grid
//	pinholePtr.setViewDistance(140000);  		// Figure 23.12(b)
//	pinholePtr.setViewDistance(14000000);  		// Figure 23.12(c)
//	pinholePtr.setViewDistance(300000000);   	// Figure 23.12(d)
//	pinholePtr.setViewDistance(100000000);   	// extra image
	pinholePtr.computeUVW(); 
	w.setCamera(pinholePtr);     }
    
}
