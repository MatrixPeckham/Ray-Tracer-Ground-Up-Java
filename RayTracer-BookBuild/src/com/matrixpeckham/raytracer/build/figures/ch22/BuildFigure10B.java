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

import com.matrixpeckham.raytracer.cameras.Spherical;
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
public class BuildFigure10B implements BuildWorldFunction {

    @Override
    public void build(World w) {
int numSamples = 16;
	
	w.vp.setHres(800);
	w.vp.setVres(400);
	w.vp.setSamples(numSamples);
	
	w.tracer = new RayCast(w);
	
	Spherical sphericalPtr = new Spherical();
	sphericalPtr.setEye(0, 0, 0);       
	sphericalPtr.setLookat(-100, 0, 0);
	sphericalPtr.setHorizontalFov(360);     
	sphericalPtr.setVerticalFov(180);
	sphericalPtr.computeUVW();
	w.setCamera(sphericalPtr);

	
	Directional lightPtr1 = new Directional();
	lightPtr1.setDirection(1, 0, 0);    				// from the +ve x direction     
	lightPtr1.scaleRadiance(1);   
	lightPtr1.setShadows(true);
	w.addLight(lightPtr1);
	
	Directional lightPtr2 = new Directional();
	lightPtr2.setDirection(0, 1, 0);    				// from the +ve y direction     
	lightPtr2.scaleRadiance(2);   
	lightPtr2.setShadows(true);
	w.addLight(lightPtr2);	
	
	Directional lightPtr3 = new Directional();
	lightPtr3.setDirection(0, 0, 1);    				// from the +ve z direction      
	lightPtr3.scaleRadiance(1.5);   
	lightPtr3.setShadows(true);
	w.addLight(lightPtr3);
	
	
	Directional lightPtr4 = new Directional();
	lightPtr4.setDirection(-1, 0, 0);    			// from the -ve x direction     
	lightPtr4.scaleRadiance(1);   
	lightPtr4.setShadows(true);
	w.addLight(lightPtr4);
	
	Directional lightPtr5 = new Directional();
	lightPtr5.setDirection(0, -1, 0);    			// from the -ve y direction     
	lightPtr5.scaleRadiance(2);   
	lightPtr5.setShadows(true);
	w.addLight(lightPtr5);	
	
	Directional lightPtr6 = new Directional();
	lightPtr6.setDirection(0, 0, -1);    			// from the -ve z direction      
	lightPtr6.scaleRadiance(1.5);   
	lightPtr6.setShadows(true);
	w.addLight(lightPtr6);
	
	
	Grid gridPtr = new Grid();

	// eight boxes
	
	// bottom four
	
	double bottomSize = 0.5;
	Vector3D diagonal=new Vector3D(bottomSize, bottomSize, bottomSize);
	Point3D p0;
	
	Matte mattePtr1 = new Matte();
	mattePtr1.setKa(0.25);
	mattePtr1.setKd(0.75);
	mattePtr1.setCd(0.5);   // gray
		
	p0 = new Point3D(-1, -1, -1);
	Box boxPtr1 = new Box(p0, p0.add(diagonal)); 
	boxPtr1.setMaterial(mattePtr1);
	gridPtr.addObject(boxPtr1);
	
		
	Matte mattePtr2 = new Matte();
	mattePtr2.setKa(0.25);
	mattePtr2.setKd(0.75);
	mattePtr2.setCd(1, 0, 0);  // red
	
	p0 = new Point3D(-1, -1, 1 - bottomSize);
	Box boxPtr2 = new Box(p0, p0.add(diagonal)); 
	boxPtr2.setMaterial(mattePtr2);
	gridPtr.addObject(boxPtr2);
	
	
	Matte mattePtr3 = new Matte();
	mattePtr3.setKa(0.25);
	mattePtr3.setKd(0.75);
	mattePtr3.setCd(1, 1, 0);  // yellow
	
	p0 = new Point3D(1 - bottomSize, -1, 1 - bottomSize);
	Box boxPtr3 = new Box(p0, p0.add(diagonal)); 
	boxPtr3.setMaterial(mattePtr3);
	gridPtr.addObject(boxPtr3);
	
	
	Matte mattePtr4 = new Matte();
	mattePtr4.setKa(0.25);
	mattePtr4.setKd(0.75);
	mattePtr4.setCd(0.2, 0.6, 0.4);  // green
	
	p0 = new Point3D(1 - bottomSize, -1, -1);
	Box boxPtr4 = new Box(p0, p0.add(diagonal)); 
	boxPtr4.setMaterial(mattePtr4);
	gridPtr.addObject(boxPtr4);
	
	
	// top four
	
	double topSize = 0.35;
	diagonal =new Vector3D(topSize, topSize, topSize);
	
	Matte mattePtr5 = new Matte();
	mattePtr5.setKa(0.25);
	mattePtr5.setKd(0.75);
	mattePtr5.setCd(0.27, 0.36, 1.0);  // blue
	
	p0 = new Point3D(-1, 1 - topSize, -1);
	Box boxPtr5 = new Box(p0, p0.add(diagonal)); 
	boxPtr5.setMaterial(mattePtr5);
	gridPtr.addObject(boxPtr5);

	
	Matte mattePtr6 = new Matte();
	mattePtr6.setKa(0.25);
	mattePtr6.setKd(0.75);
	mattePtr6.setCd(0.75, 0, 0.75);  // majenta
	
	p0 = new Point3D(-1, 1 - topSize, 1 - topSize);
	Box boxPtr6 = new Box(p0, p0 .add( new Vector3D(0.25, topSize, topSize))); 
	boxPtr6.setMaterial(mattePtr6);
	gridPtr.addObject(boxPtr6);
	
	
	Matte mattePtr7 = new Matte();
	mattePtr7.setKa(0.25);
	mattePtr7.setKd(0.75);
	mattePtr7.setCd(1, 1, 1);  // white
	
	p0 = new Point3D(1 - 0.25, 1 - 0.25, 1 - 0.25);
	Box boxPtr7 = new Box(p0, p0 .add( new Vector3D(0.25))); 
	boxPtr7.setMaterial(mattePtr7);
	gridPtr.addObject(boxPtr7);
	
	
	Matte mattePtr8 = new Matte();
	mattePtr8.setKa(0.25);
	mattePtr8.setKd(0.75);
	mattePtr8.setCd(0, 0.75, 0.75);  // cyan
	
	p0 = new Point3D(1 - topSize, 1 - topSize, -1);
	Box boxPtr8 = new Box(p0, p0 .add(new Vector3D(topSize, topSize, 0.25))); 
	boxPtr8.setMaterial(mattePtr8);
	gridPtr.addObject(boxPtr8);
	
	gridPtr.setupCells();
	w.addObject(gridPtr);	    }
    
}
