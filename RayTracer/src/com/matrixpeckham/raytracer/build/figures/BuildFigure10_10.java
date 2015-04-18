/*
  Copyright (C) 2015 William Matrix Peckham
 
  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.
 
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
 
  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.matrixpeckham.raytracer.build.figures;

import com.matrixpeckham.raytracer.cameras.ThinLens;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Plane;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 
  @author William Matrix Peckham
 */
public class BuildFigure10_10 implements BuildWorldFunction{

    @Override
    public void build(World w) {
	int numSamples = 100;
	
	w.vp.setHres(400);                                                               	  		
	w.vp.setVres(300);
	w.vp.setPixelSize(0.05);
	w.vp.setSampler(new MultiJittered(numSamples)); 
	w.vp.setMaxDepth(0);
	
	w.tracer = new RayCast(w);
	w.backgroundColor = Utility.WHITE;
	
	Ambient ambientPtr = new Ambient();
	ambientPtr.scaleRadiance(0.5);
	w.setAmbient(ambientPtr);
	
	ThinLens thinLensPtr = new ThinLens();
	thinLensPtr.setSampler(new MultiJittered(numSamples));
	thinLensPtr.setEye(0, 6, 50);  
	thinLensPtr.setLookat(0, 6, 0);
	thinLensPtr.setViewDistance(40.0);
//	thinLensPtr.setFocalDistance(50.0); 
//	thinLensPtr.setFocalDistance(74.0); 
	thinLensPtr.setFocalDistance(98.0); 
	thinLensPtr.setLensRadius(1.0);	  // for Figure 10.9(b)
	thinLensPtr.computeUVW();
	w.setCamera(thinLensPtr);
	
	
	Directional lightPtr = new Directional();
	lightPtr.setDirection(1, 1, 1);     
	lightPtr.scaleRadiance(7.5); 
	lightPtr.setShadows(true);
	w.addLight(lightPtr);
	
	
	// box 1
	
	Checker3D checkerPtr1 = new Checker3D();
	checkerPtr1.setSize(2.0);
	checkerPtr1.setColor1(1.0, 1.0, 0.33);  		// lemon
	checkerPtr1.setColor2(Utility.BLACK);	 
	
	SV_Matte svMattePtr1 = new SV_Matte();		
	svMattePtr1.setKa(0.5);
	svMattePtr1.setKd(0.35);
	svMattePtr1.setCd(checkerPtr1);
	
	Box boxPtr1 = new Box( new Point3D(-9, 0, -1),new Point3D(-3, 12, 0));
	boxPtr1.setMaterial(svMattePtr1);
	w.addObject(boxPtr1);
		
	
	// box 2
	
	Checker3D checkerPtr2 = new Checker3D();
	checkerPtr2.setSize(2.0);
	checkerPtr2.setColor1(Utility.BLACK);  	
	checkerPtr2.setColor2(0.1, 1.0, 0.5);	  	// green
	
	SV_Matte svMattePtr2 = new SV_Matte();		
	svMattePtr2.setKa(0.5);
	svMattePtr2.setKd(0.35);
	svMattePtr2.setCd(checkerPtr2);	
	
	Box boxPtr2 = new Box(new Point3D(-3.25, 0, -25),new Point3D(4.75, 14, -24));
	boxPtr2.setMaterial(svMattePtr2);
	w.addObject(boxPtr2);
	
	
	// box 3
	
	Checker3D checkerPtr3 = new Checker3D();
	checkerPtr3.setSize(2.0);
	checkerPtr3.setColor1(Utility.BLACK);  	
	checkerPtr3.setColor2(1, 0.6, 0.15);	  	// orange
	
	SV_Matte svMattePtr3 = new SV_Matte();		
	svMattePtr3.setKa(0.5);
	svMattePtr3.setKd(0.35);
	svMattePtr3.setCd(checkerPtr3);
		
	Box boxPtr3 = new Box(new Point3D(8, 0, -49),new Point3D(18, 15, -48));
	boxPtr3.setMaterial(svMattePtr3);
	w.addObject(boxPtr3);
	

	// ground plane
		
	Checker3D checkerPtr = new Checker3D();
	checkerPtr.setSize(8.0);
	checkerPtr.setColor1(0.25);
        // gray
	checkerPtr.setColor2(Utility.WHITE);	 
	
	SV_Matte svMattePtr = new SV_Matte();		
	svMattePtr.setKa(0.5);
	svMattePtr.setKd(0.35);
	svMattePtr.setCd(checkerPtr);	

	Plane planePtr1 = new Plane(new Point3D(0.0),new Normal(0, 1, 0));
	planePtr1.setMaterial(svMattePtr);
	w.addObject(planePtr1);    }
    
}
