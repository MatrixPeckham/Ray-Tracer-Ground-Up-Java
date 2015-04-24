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
package com.matrixpeckham.raytracer.build.figures.ch09;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Sphere;
import com.matrixpeckham.raytracer.geometricobjects.triangles.Triangle;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.Phong;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure09 implements BuildWorldFunction{

    @Override
    public void build(World w) {
	int numSamples = 1;
	
	w.vp.setHres(300);			
	w.vp.setVres(300);
	w.vp.setSamples(numSamples);
	
	w.tracer = new RayCast(w);
	
	Pinhole camera = new Pinhole();
	
	// for Figure 9.9(a)	
/*
	camera.setEye(0, 0, 500);
	camera.setLookat(new Point3D(0));    
	camera.setViewDistance(500); 
*/
/*	
	// for Figure 9.9(b)
	
	camera.setEye(300, 400, 500);
	camera.setLookat(0, 0, -50);
	camera.setViewDistance(400);
*/

	
	// for Figure 9.9(c)
	
	camera.setEye(-1000, 2000, -500);
	camera.setLookat(0, -100, 0);
	camera.setViewDistance(250);
	
	camera.computeUVW();		  
	w.setCamera(camera);
	

	PointLight light1 = new PointLight();
	light1.setLocation(50, 150, 200); 
	light1.scaleRadiance(6.0);
	light1.setShadows(true);
	w.addLight(light1);
	
	
	// sphere
	
	Phong phong1 = new Phong();			
	phong1.setKa(0.5); 
	phong1.setKd(0.4);
	phong1.setCd(0.5, 0.6, 0);  	// green
	phong1.setKs(0.05); 
	phong1.setExp(25); 	
	
	Sphere	sphere1 = new Sphere(new Point3D(-45, 45, 40), 50); 
	sphere1.setMaterial(phong1);
	w.addObject(sphere1);
	
	
	// box
	
	Matte matte = new Matte();				
	matte.setKa(0.4); 
	matte.setKd(0.3);
	matte.setCd(0.8, 0.5, 0);  	// orange
	
	Box box1 = new Box(new Point3D(20, -101, -100), new Point3D(90, 100, 20));
	box1.setMaterial(matte);
	w.addObject(box1);

	
	// triangle
	
	Phong	phong2 = new Phong();			
	phong2.setKa(0.25); 
	phong2.setKd(0.5); 
	phong2.setCd(0, 0.5, 0.5);     // cyan
	phong2.setKs(0.05); 
	phong2.setExp(50); 

	Triangle triangle1 = new Triangle(new Point3D(-110, -85, 80), new Point3D(120, 10, 20), new Point3D(-40, 50, -30));
	triangle1.setMaterial(phong2);     
	w.addObject(triangle1);
	
	
	// ground plane with checker
	
	Checker3D checker3D = new Checker3D();
	checker3D.setSize(100); 
	checker3D.setColor1(0.7);  
	checker3D.setColor2(1.0);

	SV_Matte svMatte = new SV_Matte();
	svMatte.setKa(0.25);
	svMatte.setKd(0.35);
	svMatte.setCd(checker3D);
	
	Plane plane = new Plane(new Point3D(0, -101, 0),new Normal(0, 1, 0));
	plane.setMaterial(svMatte);
	w.addObject(plane);



    }
    
}
