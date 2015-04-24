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
package com.matrixpeckham.raytracer.build.figures.ch10;

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
public class BuildFigure09 implements BuildWorldFunction{

    @Override
    public void build(World w) {
//	int numSamples = 25;  // for Figure 10.9(a)
	int numSamples = 1;  //  for Figure 10.9(b)
	
	w.vp.setHres(400);                                                               	  		
	w.vp.setVres(300);
	w.vp.setPixelSize(0.05);
	w.vp.setSampler(new MultiJittered(numSamples)); 
	w.vp.setMaxDepth(0);
	
	w.tracer = new RayCast(w);
	w.backgroundColor = Utility.WHITE;
	
	Ambient ambient = new Ambient();
	ambient.scaleRadiance(0.5);
	w.setAmbient(ambient);
	
	ThinLens thinLens = new ThinLens();
	thinLens.setSampler(new MultiJittered(numSamples));
	thinLens.setEye(0, 6, 50);  
	thinLens.setLookat(0, 6, 0);
	thinLens.setViewDistance(40.0);
	thinLens.setFocalDistance(74.0); 
//	thinLens.setLensRadius(0.0);      // for Figure 10.9(a)
	thinLens.setLensRadius(1.0);	  // for Figure 10.9(b)
	thinLens.computeUVW();
	w.setCamera(thinLens);
	
	
	Directional light = new Directional();
	light.setDirection(1, 1, 1);     
	light.scaleRadiance(7.5); 
	light.setShadows(true);
	w.addLight(light);
	
	
	// box 1
	
	Checker3D checker1 = new Checker3D();
	checker1.setSize(2.0);
	checker1.setColor1(1.0, 1.0, 0.33);  		// lemon
	checker1.setColor2(Utility.BLACK);	 
	
	SV_Matte svMatte1 = new SV_Matte();		
	svMatte1.setKa(0.5);
	svMatte1.setKd(0.35);
	svMatte1.setCd(checker1);
	
	Box box1 = new Box( new Point3D(-9, 0, -1),new Point3D(-3, 12, 0));
	box1.setMaterial(svMatte1);
	w.addObject(box1);
		
	
	// box 2
	
	Checker3D checker2 = new Checker3D();
	checker2.setSize(2.0);
	checker2.setColor1(Utility.BLACK);  	
	checker2.setColor2(0.1, 1.0, 0.5);	  	// green
	
	SV_Matte svMatte2 = new SV_Matte();		
	svMatte2.setKa(0.5);
	svMatte2.setKd(0.35);
	svMatte2.setCd(checker2);	
	
	Box box2 = new Box(new Point3D(-3.25, 0, -25),new Point3D(4.75, 14, -24));
	box2.setMaterial(svMatte2);
	w.addObject(box2);
	
	
	// box 3
	
	Checker3D checker3 = new Checker3D();
	checker3.setSize(2.0);
	checker3.setColor1(Utility.BLACK);  	
	checker3.setColor2(1, 0.6, 0.15);	  	// orange
	
	SV_Matte svMatte3 = new SV_Matte();		
	svMatte3.setKa(0.5);
	svMatte3.setKd(0.35);
	svMatte3.setCd(checker3);
		
	Box box3 = new Box(new Point3D(8, 0, -49),new Point3D(18, 15, -48));
	box3.setMaterial(svMatte3);
	w.addObject(box3);
	

	// ground plane
		
	Checker3D checker = new Checker3D();
	checker.setSize(8.0);
	checker.setColor1(0.25);
        // gray
	checker.setColor2(Utility.WHITE);	 
	
	SV_Matte svMatte = new SV_Matte();		
	svMatte.setKa(0.5);
	svMatte.setKd(0.35);
	svMatte.setCd(checker);	

	Plane plane1 = new Plane(new Point3D(0.0),new Normal(0, 1, 0));
	plane1.setMaterial(svMatte);
	w.addObject(plane1);    }
    
}
