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
import com.matrixpeckham.raytracer.geometricobjects.primitives.OpenCylinder;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.textures.image.ImageTexture;
import com.matrixpeckham.raytracer.textures.image.mappings.CylindricalMap;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
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
public class BuildFigure06 implements BuildWorldFunction {

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.


// This biuilds the scene for Figure 29.6

 												

	int numSamples = 16;
	
	w.vp.setHres(400);
	w.vp.setVres(400); 
	w.vp.setSamples(numSamples);
	w.vp.setMaxDepth(0);
	
	w.backgroundColor = new RGBColor(0.5);
	
	w.tracer = new RayCast(w);
	
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(2, 3.5, 5);
	pinholePtr.setLookat(new Point3D(0)); 
	pinholePtr.setViewDistance(800.0);
	pinholePtr.computeUVW();
	w.setCamera(pinholePtr);
	
	Directional lightPtr = new Directional();
	lightPtr.setDirection(14, 20, 25);  
	lightPtr.scaleRadiance(1.75);  
	lightPtr.setShadows(true);
	w.addLight(lightPtr);

	// image:

	Image imagePtr = new Image();			
        String path = "C:\\Users\\Owner\\Documents\\Ground Up raytracer\\Textures\\ppm\\";
        try {
            imagePtr.loadPPMFile(new File(path+"CountryScene.ppm"));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure06.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
	CylindricalMap mapPtr = new CylindricalMap();   
	ImageTexture texturePtr = new ImageTexture(imagePtr); 
	texturePtr.setMapping(mapPtr);

	SV_Matte svMattePtr = new SV_Matte();		
	svMattePtr.setKa(0.40);
	svMattePtr.setKd(0.95);
	svMattePtr.setCd(texturePtr);

	OpenCylinder cylinderPtr = new OpenCylinder();
	cylinderPtr.setMaterial(svMattePtr); 
	w.addObject(cylinderPtr);
	
    }
    
}
