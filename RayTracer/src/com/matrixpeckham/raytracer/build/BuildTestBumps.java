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
import com.matrixpeckham.raytracer.geometricobjects.primitives.Rectangle;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.Phong;
import com.matrixpeckham.raytracer.textures.TInstance;
import com.matrixpeckham.raytracer.textures.procedural.FBMBump;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Vector3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildTestBumps implements BuildWorldFunction{

    @Override
    public void build(World world) {
	int numSamples = 1;			// development
//s	int numSamples = 16;			// production
	
	world.vp.setHres(600);				// production
	world.vp.setVres(600);
	world.vp.setSamples(numSamples);	
	world.vp.setMaxDepth(2);
		
	Ambient ambientPtr = new Ambient();
	ambientPtr.scaleRadiance(0.5);
	world.setAmbient(ambientPtr);
	
	world.tracer = new Whitted(world);
	
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(0, 5, 5);
	pinholePtr.setLookat(0, 0, 0.0); 
//	pinholePtr.setViewDistance(600.0);		// for 475 x 250
	pinholePtr.setViewDistance(1200.0);   	// for 950 X 500
	pinholePtr.computeUVW();
	world.setCamera(pinholePtr);
	
	PointLight lightPtr = new PointLight();
	lightPtr.setLocation(14, 50, 50);  
	lightPtr.scaleRadiance(3.0);  
	lightPtr.setShadows(true);
	world.addLight(lightPtr);
	
	
	
	// plain material	
	
	Phong phongPtr = new Phong();		
	phongPtr.setKa(0.5);  
	phongPtr.setKd(1.0);
	phongPtr.setKs(0.2);  
	phongPtr.setExp(20.0);
	phongPtr.setCd(0.5, 0.3, 0.1);
	double 	bathXmin 			= -1.0;
	double 	bathZmin 			= -1.0;  
	double 	bathXmax 			= 1.0;
	double 	bathZmax 			= 1.0;   
	double 	xSize 				= bathXmax - bathXmin;
	double 	zSize 				= bathZmax - bathZmin;
		
	double bathKa 				= 0.5;  	// common material property
	double bathKd 				= 0.85; 	// common material property
	
	
	// plain material 
	
	Matte mattePtr10 = new Matte();				
	mattePtr10.setKa(bathKa);
	mattePtr10.setKd(bathKd);
	mattePtr10.setCd(0.53, 0.51, 0.45);
	
	
	
	
	
	
	
	// ************************************************************************************************* bath water
	
	// the bath water
	// w is a bump mapped rectangle with a transparent material	
	
	
	Rectangle waterSurfacePtr = new Rectangle(	new Point3D(bathXmin, 0, bathZmin), 
													new Vector3D(0, 0, zSize), 
													new Vector3D(xSize, 0, 0),
													new Normal(0, 1, 0));

	waterSurfacePtr.setShadows(false);
	waterSurfacePtr.setMaterial(phongPtr);
	//world.addObject(waterSurfacePtr);						// no bump map - use w for Figure 29.1

	// the bump mapped object
	
	int		numOctaves 		= 4;
	double 	lacunarity 			= 2.0;
	double 	gain 				= 0.5;
	double 	perturbationAmount = 1.0;
	
	FBMBump fBmBumpPtr = new FBMBump(numOctaves, lacunarity, gain, perturbationAmount);    // Ken Musgrave's water
	TInstance tinst = new TInstance(fBmBumpPtr);
        tinst.scale(0.125);
	BumpedObject bumpedWaterPtr = new BumpedObject();
	bumpedWaterPtr.setMaterial(phongPtr);
	bumpedWaterPtr.setObject(waterSurfacePtr);
	bumpedWaterPtr.setBumpMap(tinst);//fBmBumpPtr);
	world.addObject(bumpedWaterPtr);						// use w for Figure 29.2

//TODO: NEED TO IMPLEMENT BUMPS


    }
    
}
