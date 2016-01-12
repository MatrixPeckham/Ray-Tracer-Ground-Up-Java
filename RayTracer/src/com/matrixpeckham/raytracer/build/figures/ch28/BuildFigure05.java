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
package com.matrixpeckham.raytracer.build.figures.ch28;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Rectangle;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Dielectric;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure05 implements BuildWorldFunction{

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.


// This builds the scene for Figure 28.5

	int numSamples = 16;
	
	w.vp.setHres(600);	  		
	w.vp.setVres(600);
	w.vp.setSamples(numSamples);		
	w.vp.setMaxDepth(5);		
	
	w.backgroundColor = new RGBColor(0.0, 0.3, 0.25);
	
	w.tracer = new Whitted(w);
	
	Ambient ambient = new Ambient();
	ambient.scaleRadiance(0.25);
	w.setAmbient(ambient);
		
	Pinhole pinhole = new Pinhole();
	pinhole.setEye(-8, 5.5, 40);   
	pinhole.setLookat(1, 4, 0);    
	pinhole.setViewDistance(2400.0);  
	pinhole.computeUVW();     
	w.setCamera(pinhole);
	
	
	// point light 
	
	PointLight light1 = new PointLight();
	light1.setLocation(40, 50, 0); 
	light1.scaleRadiance(4.5);
	light1.setShadows(true);
	w.addLight(light1);
	

	// point light 
	
	PointLight light2 = new PointLight();
	light2.setLocation(-10, 20, 10); 
	light2.scaleRadiance(4.5);
	light2.setShadows(true);
	w.addLight(light2);
	
	
	// directional light 
	
	Directional light3 = new Directional();
	light3.setDirection(-1, 0, 0); 
	light3.scaleRadiance(4.5);
	light3.setShadows(true);
	w.addLight(light3);
	
	
	// transparent sphere
	
	Dielectric dielectic = new Dielectric();
	dielectic.setKs(0.2);
	dielectic.setKt(1);
	dielectic.setExp(2000.0);
//	dielectic.setIorIn(1.5);		// for Figure 28.5(a)
	dielectic.setIorIn(0.75);	// for Figure 28.5(b)
	dielectic.setIorOut(1.0);
	dielectic.setCfIn(Utility.WHITE);
	dielectic.setCfOut(Utility.WHITE);
	
	Sphere sphere1 = new Sphere(new Point3D(0.0, 4.5, 0.0), 3.0);
	sphere1.setMaterial(dielectic);
	w.addObject(sphere1);
	
	
	// red sphere
		
	Reflective reflective = new Reflective();
	reflective.setKa(0.3);
	reflective.setKd(0.3); 
	reflective.setCd(Utility.RED); 
	reflective.setKs(0.2);
	reflective.setExp(2000.0);
	reflective.setKr(0.25);
	
	Sphere sphere2 = new Sphere(new Point3D(4, 4, -6), 3);
	sphere2.setMaterial(reflective);
	w.addObject(sphere2);

		
	Checker3D checker = new Checker3D();
	checker.setSize(4);
	checker.setColor1(0.75);  
	checker.setColor2(Utility.WHITE);	
	
	SV_Matte svMatte = new SV_Matte();		
	svMatte.setKa(0.5);
	svMatte.setKd(0.35);
	svMatte.setCd(checker);	
	
	// rectangle
	
	Point3D p0=new Point3D(-20, 0, -100);
	Vector3D a=new Vector3D(0, 0, 120);
	Vector3D b=new Vector3D(40, 0, 0);
	
	Rectangle rectangle = new Rectangle(p0, a, b); 
	rectangle.setMaterial(svMatte);
	w.addObject(rectangle);		
    }
    
}
