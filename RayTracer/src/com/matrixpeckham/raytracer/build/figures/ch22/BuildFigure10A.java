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

import com.matrixpeckham.raytracer.cameras.Orthographic;
import com.matrixpeckham.raytracer.geometricobjects.compound.Grid;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Box;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Vector3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure10A implements BuildWorldFunction{

    @Override
    public void build(World w) {
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 22.09

	int numSamples = 16;
	
	w.vp.setHres(400);
	w.vp.setVres(400);
	w.vp.setSamples(numSamples);
	
	w.tracer = new RayCast(w);

	Orthographic orthographic = new Orthographic();
	w.vp.setPixelSize(0.0085);
	orthographic.setEye(0, 0, 0); 
	orthographic.setLookat(0, -100, 0);
	orthographic.computeUVW();
	w.setCamera(orthographic);
	
	
	Directional light1 = new Directional();
	light1.setDirection(1, 0, 0);    				// from the +ve x direction     
	light1.scaleRadiance(1);   
	light1.setShadows(true);
	w.addLight(light1);
	
	Directional light2 = new Directional();
	light2.setDirection(0, 1, 0);    				// from the +ve y direction     
	light2.scaleRadiance(2);   
	light2.setShadows(true);
	w.addLight(light2);	
	
	Directional light3 = new Directional();
	light3.setDirection(0, 0, 1);    				// from the +ve z direction      
	light3.scaleRadiance(1.5);   
	light3.setShadows(true);
	w.addLight(light3);
	
	Directional light4 = new Directional();
	light4.setDirection(-1, 0, 0);    			// from the -ve x direction     
	light4.scaleRadiance(1);   
	light4.setShadows(true);
	w.addLight(light4);
	
	Directional light5 = new Directional();
	light5.setDirection(0, -1, 0);    			// from the -ve y direction     
	light5.scaleRadiance(2);   
	light5.setShadows(true);
	w.addLight(light5);	
	
	Directional light6 = new Directional();
	light6.setDirection(0, 0, -1);    			// from the -ve z direction      
	light6.scaleRadiance(1.5);   
	light6.setShadows(true);
	w.addLight(light6);
	
	
	Grid grid = new Grid();

	// eight boxes
	
	// bottom four
	
	double bottomSize = 0.5;
	Vector3D diagonal = new Vector3D(bottomSize, bottomSize, bottomSize);
	Point3D p0;
	
	Matte matte1 = new Matte();
	matte1.setKa(0.25);
	matte1.setKd(0.75);
	matte1.setCd(0.5);   // gray
		
	p0 = new Point3D(-1, -1, -1);
	Box box1 = new Box(p0, p0 .add( diagonal) ); 
	box1.setMaterial(matte1);
	grid.addObject(box1);
	
		
	Matte matte2 = new Matte();
	matte2.setKa(0.25);
	matte2.setKd(0.75);
	matte2.setCd(1, 0, 0);  // red
	
	p0 = new Point3D(-1, -1, 1 - bottomSize);
	Box box2 = new Box(p0, p0 .add(diagonal)); 
	box2.setMaterial(matte2);
	grid.addObject(box2);
	
	
	Matte matte3 = new Matte();
	matte3.setKa(0.25);
	matte3.setKd(0.75);
	matte3.setCd(1, 1, 0);  // yellow
	
	p0 = new Point3D(1 - bottomSize, -1, 1 - bottomSize);
	Box box3 = new Box(p0, p0 .add(diagonal)); 
	box3.setMaterial(matte3);
	grid.addObject(box3);
	
	
	Matte matte4 = new Matte();
	matte4.setKa(0.25);
	matte4.setKd(0.75);
	matte4.setCd(0.2, 0.6, 0.4);  // green
	
	p0 = new Point3D(1 - bottomSize, -1, -1);
	Box box4 = new Box(p0,p0.add(diagonal)); 
	box4.setMaterial(matte4);
	grid.addObject(box4);
	
	
	
	// top four
	
	double topSize = 0.35;
	diagonal = new Vector3D(topSize, topSize, topSize);
	
	Matte matte5 = new Matte();
	matte5.setKa(0.25);
	matte5.setKd(0.75);
	matte5.setCd(0.27, 0.36, 1.0);  // blue
	
	p0 = new Point3D(-1, 1 - topSize, -1);
	Box box5 = new Box(p0,p0.add(diagonal)); 
	box5.setMaterial(matte5);
	grid.addObject(box5);

	
	Matte matte6 = new Matte();
	matte6.setKa(0.25);
	matte6.setKd(0.75);
	matte6.setCd(0.75, 0, 0.75);  // majenta
	
	p0 = new Point3D(-1, 1 - topSize, 1 - topSize);
	Box box6 = new Box(p0, p0 .add(new Vector3D(0.25, topSize, topSize))); 
	box6.setMaterial(matte6);
	grid.addObject(box6);
	
	
	Matte matte7 = new Matte();
	matte7.setKa(0.25);
	matte7.setKd(0.75);
	matte7.setCd(1, 1, 1);  // white
	
	p0 = new Point3D(1 - 0.25, 1 - 0.25, 1 - 0.25);
	Box box7 = new Box(p0, p0 .add(new Vector3D(0.25))); 
	box7.setMaterial(matte7);
	grid.addObject(box7);
	
	
	Matte matte8 = new Matte();
	matte8.setKa(0.25);
	matte8.setKd(0.75);
	matte8.setCd(0, 0.75, 0.75);  // cyan
	
	p0 = new Point3D(1 - topSize, 1 - topSize, -1);
	Box box8 = new Box(p0, p0 .add(new Vector3D(topSize, topSize, 0.25))); 
	box8.setMaterial(matte8);
	grid.addObject(box8);
	
	grid.setupCells();
	w.addObject(grid);


    }
    
}
