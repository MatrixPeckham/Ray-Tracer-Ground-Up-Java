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
package com.matrixpeckham.raytracer.build.figures.ch04;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.samplers.Jittered;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure08 implements BuildWorldFunction {

    @Override
    public void build(World w) {
       int numSamples = 64;  // use 1 for 4.8(a), 64 for 4.8(b)
	
	Jittered samplerPtr = new Jittered(numSamples);
	
	w.vp.setHres(300);	  		
	w.vp.setVres(200);
//	w.vp.setSamples(numSamples);		// for 4.8(a) (one regular sample per pixel)
	w.vp.setSampler(samplerPtr);		// for 4.8(b)	
	w.vp.setMaxDepth(0);		
	
	w.backgroundColor = new RGBColor(0.25);
	
	Ambient ambientPtr = new Ambient();
	ambientPtr.scaleRadiance(1.0);
	w.setAmbient(ambientPtr);
	
	w.tracer = new RayCast(w);
	
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(100, 500, 100);   
	pinholePtr.setLookat(0, 450, 0);    
	pinholePtr.setViewDistance(175);	
	pinholePtr.computeUVW();     
	w.setCamera(pinholePtr);
	
	
	// directional light 
	
	Directional lightPtr3 = new Directional();
	lightPtr3.setDirection(0, 1, 0); 
	lightPtr3.scaleRadiance(4.0);
	lightPtr3.setShadows(false);
	w.addLight(lightPtr3);
		
	
	// plane
		
	Checker3D checker3DPtr = new Checker3D();
	checker3DPtr.setSize(250.0);
	checker3DPtr.setColor1(Utility.BLACK);  	
	checker3DPtr.setColor2(Utility.WHITE); 
	
	SV_Matte svMattePtr = new SV_Matte();		
	svMattePtr.setKa(0.5);
	svMattePtr.setKd(0.35);
	svMattePtr.setCd(checker3DPtr);	
	
	Plane planePtr = new Plane(new Point3D(0.0),new Normal(0.0, 1.0, 0.0));
	planePtr.setMaterial(svMattePtr);
	w.addObject(planePtr);		
    }

}
