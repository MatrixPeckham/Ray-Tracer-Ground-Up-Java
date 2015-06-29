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
package com.matrixpeckham.raytracer.build.figures.ch11;

import com.matrixpeckham.raytracer.cameras.FishEye;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.textures.image.ImageTexture;
import com.matrixpeckham.raytracer.textures.image.mappings.LightProbe;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
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
public class BuildFigure08 implements BuildWorldFunction{

    @Override
    public void build(World w) {
int numSamples = 16;
	
	w.vp.setHres(600);			
	w.vp.setVres(600); 
	w.vp.setSamples(numSamples);
	w.vp.setPixelSize(1.0);
	
	w.tracer = new RayCast(w);	
	
		
	FishEye fisheye = new FishEye();
	
	fisheye.setEye(new Point3D(0)); 
	fisheye.setLookat(0, 0, -100);
	
	fisheye.setFov(180);			// For Figure 11.8(a)
//	fisheye.setFov(360);	 		// For Figure 11.8(b)
	
//	fisheye.setLookat(0, 100, 0);	// For Figure 11.8(c) - camera looks vertically up
//	fisheye.setFov(200);		
	
	fisheye.computeUVW(); 
	w.setCamera(fisheye);

	
	Image image = new Image();
        try {
            image.loadPPMFile(new File("C:\\Users\\Owner\\Documents\\Ground Up raytracer\\Textures\\ppm\\uffizi_probe_small.ppm"));   // for development
//	image.loadPPMFile(new File("C:\\Users\Owner\\Documents\\Ground Up raytracer\\Textures\ppm\\uffizi_probe_large.ppm"));   // for production
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure08.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

	LightProbe lightProbe = new LightProbe();     	
	lightProbe.makePanoramic();
	
	ImageTexture imageTexture = new ImageTexture(image); 
	imageTexture.setMapping(lightProbe);
	
	SV_Matte svMatte = new SV_Matte();	// ka + kd > 1
	svMatte.setKa(1.0);
	svMatte.setKd(0.85); 	
	svMatte.setCd(imageTexture);
	
	Sphere unitSphere = new Sphere();
	unitSphere.setShadows(false);	
	
	Instance sphere = new Instance(unitSphere); 
	sphere.scale(new Vector3D(1000000));
	sphere.setMaterial(svMatte);
	w.addObject(sphere);    }
    
}
