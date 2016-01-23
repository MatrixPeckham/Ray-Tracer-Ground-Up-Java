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
package com.matrixpeckham.raytracer.build.figures.ch29;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.compound.TriangleMesh;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.textures.image.ImageTexture;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Mesh;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
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
public class BuildFigure22 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.


// This builds the scene for Figure 29.22

// The two triangles are read from a ply file.
// The triangles can be flat or smooth shaded depending on which of the 
// following two functions is called:

// Grid::gridPtr.readFlatUvTriangles(fileName);
// Grid::gridPtr.readSmoothUvRiangles(fileName);

// The version of the Grid class in the Chapter 29 download includes these functions, and the 
// function Grid::readUvPlyFile, which they call.


 												

	int numSamples = 16;
	
	w.vp.setHres(400);      
	w.vp.setVres(400);    
	w.vp.setSamples(numSamples);	
	
	w.tracer = new RayCast(w);
	
	w.backgroundColor = Utility.BLACK;

	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(11, 5, 9);  
	pinholePtr.setViewDistance(1600.0);
	pinholePtr.setLookat(0, -0.5, 0);
	pinholePtr.computeUVW();     
	w.setCamera(pinholePtr); 

	
	Directional directionalPtr = new Directional();
	directionalPtr.setDirection(0.75, 1, -0.15);     
	directionalPtr.scaleRadiance(4.5); 
	directionalPtr.setShadows(true);
	w.addLight(directionalPtr);
	
	// image:					
	Image imagePtr = new Image();			
        String imagePath = "C:\\Users\\Owner\\Documents\\Ground Up raytracer\\Textures\\ppm\\";
        try {
//            imagePtr.loadPPMFile(new File(imagePath+"EarthHighRes.ppm"));
            imagePtr.loadPPMFile(new File(imagePath+"BlueGlass.ppm"));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure22.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
	
	
	ImageTexture texturePtr = new ImageTexture(); 
	texturePtr.setImage(imagePtr);

	SV_Matte svMattePtr = new SV_Matte();		
	svMattePtr.setKa(0.1);
	svMattePtr.setKd(0.75);
	svMattePtr.setCd(texturePtr);
	String meshPath = "C:\\Users\\Owner\\Documents\\Ground Up raytracer\\chapters\\Chapter29\\";	
	String fileName = "TwoUVTriangles.ply";
	TriangleMesh gridPtr = new TriangleMesh(new Mesh());
        try{
  //        gridPtr.readFlatUVTriangles(new File(meshPath+fileName));		// for Figure 29.22(a)
            gridPtr.readSmoothUvTriangles(new File(meshPath+fileName));		// for Figure 29.22(b)
        }catch (IOException e){
            throw new RuntimeException(e);
        }
	gridPtr.setMaterial(svMattePtr);   
	gridPtr.setupCells();
	w.addObject(gridPtr);
	
	
	Matte mattePtr = new Matte();			
	mattePtr.setCd(1, 0.9, 0.6);
	mattePtr.setKa(0.25); 
	mattePtr.setKd(0.4);
		
	Plane planePtr1 = new Plane(new Point3D(0, -2.0, 0), new Normal(0, 1, 0));  
	planePtr1.setMaterial(mattePtr);
	w.addObject(planePtr1);	

	
    }
    
}
