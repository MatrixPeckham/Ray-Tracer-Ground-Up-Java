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
package com.matrixpeckham.raytracer.build.figures.ch21;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.beveledobjects.BeveledBox;
import com.matrixpeckham.raytracer.geometricobjects.beveledobjects.BeveledCylinder;
import com.matrixpeckham.raytracer.geometricobjects.beveledobjects.BeveledRing;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.Phong;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure07 implements BuildWorldFunction{

    @Override
    public void build(World w) {
       int numSamples = 16;
	
	w.vp.setHres(600);	  		
	w.vp.setVres(280);
	w.vp.setSamples(numSamples);
	
	w.tracer = new RayCast(w);
		
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(10, 15, 50);
	pinholePtr.setLookat(0, 0.75, 0);
	pinholePtr.setViewDistance(4000); 
	pinholePtr.computeUVW();
	w.setCamera(pinholePtr);
		
	PointLight lightPtr1 = new PointLight();
	lightPtr1.setLocation(10, 15, 20);
	lightPtr1.scaleRadiance(3.0);
	lightPtr1.setShadows(true);
	w.addLight(lightPtr1);
		
	Phong phongPtr = new Phong();			
	phongPtr.setKa(0.3);    
	phongPtr.setKd(0.75);
	phongPtr.setCd(0.7, 0.5, 0);		// orange
	phongPtr.setKs(0.2);
	phongPtr.setExp(3);
	
	double bevelRadius = 0.05;  // for all objects
		
	// cylinder
	
	double y0  = -0.75; 
	double y1  = 1.25; 
	double radius = 1.0;
	
	Instance cylinderPtr = new Instance(new BeveledCylinder(y0, y1, radius, bevelRadius));
	cylinderPtr.translate(-2.75, 0, 0);
	cylinderPtr.setMaterial(phongPtr);
	w.addObject(cylinderPtr);

	// thick ring
	
	y0 = -0.125;
	y1 = 0.125;
	double innerRadius = 0.75;
	double outerRadius = 1.6;
	
	Instance ringPtr = new Instance(new BeveledRing(y0, y1, innerRadius, outerRadius, bevelRadius));
	ringPtr.rotateX(90);
	ringPtr.rotateY(-30);
	ringPtr.translate(0.0, 0.85, 0.5);
	ringPtr.setMaterial(phongPtr);
	w.addObject(ringPtr);
		
	// box
	// the untransformed box is centered on the origin
	
	Point3D p0=new Point3D(-0.75, -1.125, -0.75);
	Point3D p1=new Point3D(0.75, 1.125, 0.75);
	
	Instance boxPtr = new Instance(new BeveledBox(p0, p1, bevelRadius));
	boxPtr.rotateY(-10);
	boxPtr.translate(2.5, 0.38, -1);
	boxPtr.setMaterial(phongPtr);
	w.addObject(boxPtr);
	
	// ground plane
	
	Matte mattePtr = new Matte();
	mattePtr.setKa(0.5);
	mattePtr.setKd(0.85);
	mattePtr.setCd(0.25);
	
	Plane planePtr = new Plane(new Point3D(0, -0.75, 0), new Normal(0, 1, 0));
	planePtr.setMaterial(mattePtr);
	w.addObject(planePtr);    }
}
