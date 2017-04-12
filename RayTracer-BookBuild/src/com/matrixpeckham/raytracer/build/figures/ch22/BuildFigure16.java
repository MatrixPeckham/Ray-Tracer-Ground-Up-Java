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
package com.matrixpeckham.raytracer.build.figures.ch22;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.compound.Grid;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.textures.image.ImageTexture;
import com.matrixpeckham.raytracer.textures.image.mappings.LightProbe;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
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
public class BuildFigure16 implements BuildWorldFunction {

    @Override
    public void build(World w) {
int numSamples = 1;
	
	w.vp.setHres(600);			
	w.vp.setVres(600); 
	w.vp.setSamples(numSamples);
	
	w.tracer = new RayCast(w);	
	
	Pinhole pinholePtr = new Pinhole();			
	pinholePtr.setEye(0.05, 0.06, 0.07); 
	pinholePtr.setLookat(0.1, 0.1, -10.0);	
	pinholePtr.setViewDistance(80);
	pinholePtr.computeUVW(); 
	w.setCamera(pinholePtr);
	
	
	PointLight lightPtr = new PointLight();
	lightPtr.setLocation(new Point3D(0.0));   
	lightPtr.scaleRadiance(3.0);
	lightPtr.setShadows(false);
	w.addLight(lightPtr);

	
	// the box
			
	Matte mattePtr = new Matte();
	mattePtr.setKa(0.2); 
	mattePtr.setKd(0.5);
	mattePtr.setCd(1.0, 0.75, 0.25);  // brown
	
	Point3D p0=new Point3D(-10);
	Point3D p1=new Point3D(10);
	
	Box boxPtr = new Box(p0, p1);	
	boxPtr.setMaterial(mattePtr);

	Grid gridPtr = new Grid();
	gridPtr.addObject(boxPtr);
	gridPtr.setupCells();
	w.addObject(gridPtr);
		
	Image imagePtr = new Image();

        String path = "resources/Textures/ppm/";
        try {
            //	imagePtr.loadPPMFile(Thread.currentThread().getContextClassLoader().getResourceAsStream(path+"uffizi_probe_small.ppm"));  // for testing
            imagePtr.loadPPMFile(Thread.currentThread().getContextClassLoader().getResourceAsStream(path+"uffizi_probe_large.ppm"));  // for production

        } catch (IOException ex) {
            Logger.getLogger(BuildFigure16.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

	LightProbe lightProbePtr = new LightProbe();
	lightProbePtr.makePanoramic();     		
	
	ImageTexture imageTexturePtr = new ImageTexture(imagePtr); 
	imageTexturePtr.setMapping(lightProbePtr);
	
	SV_Matte svMattePtr = new SV_Matte();		
	svMattePtr.setKa(1);
	svMattePtr.setKd(0.85);  
	svMattePtr.setCd(imageTexturePtr);
	
	Sphere unitSpherePtr = new Sphere();
	unitSpherePtr.setShadows(false);	
	
	Instance largeSpherePtr = new Instance(unitSpherePtr); 
	largeSpherePtr.scale(1000000.0,1000000.0,1000000.0);
	largeSpherePtr.setMaterial(svMattePtr);
	w.addObject(largeSpherePtr);    }
    
}
