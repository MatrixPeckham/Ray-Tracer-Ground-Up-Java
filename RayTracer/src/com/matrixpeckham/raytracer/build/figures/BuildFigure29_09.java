/*
  Copyright (C) 2015 William Matrix Peckham
 *
  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.
 *
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
 *
  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.matrixpeckham.raytracer.build.figures;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Sphere;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.Image;
import com.matrixpeckham.raytracer.textures.ImageTexture;
import com.matrixpeckham.raytracer.textures.SphericalMap;
import com.matrixpeckham.raytracer.tracers.RayCast;
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
  @author William Matrix Peckham
 */
public class BuildFigure29_09 implements BuildWorldFunction{

    @Override
    public void build(World w) {
	int numSamples = 1;
	
	w.vp.setHres(700);      
	w.vp.setVres(700);    
	w.vp.setSamples(numSamples);
		
	w.backgroundColor = Utility.BLACK;
	
	w.tracer = new RayCast(w);
	
	Pinhole camera = new Pinhole();
	camera.setEye(40, 20, 40); 			// for Figure29.9(a)
//	camera.setEye(0, 65, 0); 				// for Figure29.9(b)			
	camera.setLookat(new Point3D()); 
	camera.setViewDistance(17000.0);
	camera.computeUVW();
	w.setCamera(camera); 
	
	
	// image:					

	Image image = new Image();				
        try {
            image.loadPPMFile(new File("C:\\Users\\Owner\\Documents\\Ground Up raytracer\\Textures\\ppm\\SphereGrid.ppm"));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure29_09.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
	
	
	// mapping:
	
	SphericalMap map = new SphericalMap(); 
	
	
	// image based texture:  
	
	ImageTexture texture = new ImageTexture(); 
	texture.setImage(image); 
	texture.setMapping(map);
	
	
	// textured material:

	SV_Matte svEmissive = new SV_Matte();	
	svEmissive.setKa(1.0);	
	svEmissive.setCd(texture);
	
	
	// generic sphere:
	
	Sphere	sphere = new Sphere(); 
	sphere.setMaterial(svEmissive);
	w.addObject(sphere);    
    }
    
}
