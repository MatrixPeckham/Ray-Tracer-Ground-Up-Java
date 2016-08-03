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
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.SphereChecker;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;
import static java.lang.Math.pow;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure18B implements BuildWorldFunction {

    @Override
    public void build(World w) {
int numSamples = 25;
	
	w.vp.setHres(600);
	w.vp.setVres(600);
	w.vp.setSamples(numSamples);
	
	w.tracer = new RayCast(w);
	
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(7, 5, 4.5);
	pinholePtr.setLookat(0, -0.5, -0.25);
	pinholePtr.setViewDistance(1305);
	pinholePtr.computeUVW();
	w.setCamera(pinholePtr);

	
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
	
	Grid gridPtr = new Grid();
	
	int numSpheres = 25;
	double volume = 0.1 / numSpheres;
	double radius = 2.5 * pow(0.75 * volume / Math.PI, 0.333333);
	
	Utility.setRandSeed(15);  
	
	for (int j = 0; j < numSpheres; j++) {
		SphereChecker checkerPtr = new SphereChecker();
		checkerPtr.setNumlong(16);
		checkerPtr.setNumlat(8);
		checkerPtr.setLineWidth(0.075);
		
		RGBColor color=new RGBColor(Utility.randDouble(), Utility.randDouble(), Utility.randDouble());
		checkerPtr.setColor1(color);
		checkerPtr.setColor2(color);
		checkerPtr.setLineColor(Utility.BLACK);
		
		SV_Matte svMattePtr1 = new SV_Matte();		
		svMattePtr1.setKa(0.5);
		svMattePtr1.setKd(0.6);
		svMattePtr1.setCd(checkerPtr);
		svMattePtr1.setCd(checkerPtr);
		
		Sphere spherePtr1 = new Sphere();
		spherePtr1.setMaterial(svMattePtr1);
		
		Instance spherePtr2 = new Instance(spherePtr1);
		spherePtr2.scale(new Vector3D(radius));
		spherePtr2.scale(1, 0.25, 1);
		spherePtr2.translate(1.0 - 2.0 * Utility.randDouble(), 1.0 - 2.0 * Utility.randDouble(), 1.0 - 2.0 * Utility.randDouble());
		spherePtr2.computeBoundingBox();
		gridPtr.addObject(spherePtr2);
	}	
	
	gridPtr.setupCells();
	w.addObject(gridPtr);		
	
	// plane perpendicular to x axis
	
	Matte mattePtr1 = new Matte();		
	mattePtr1.setKa(0.15);
	mattePtr1.setKd(0.6);
	mattePtr1.setCd(0.5);
	
	Plane planePtr1 = new Plane(new Point3D(-2, 0, 0), new Normal(1, 0, 0));
	planePtr1.setMaterial(mattePtr1);
	w.addObject(planePtr1);
	
	// plane perpendicular to y axis
	
	Matte mattePtr2 = new Matte();		
	mattePtr2.setKa(0.15);
	mattePtr2.setKd(0.6);
	mattePtr2.setCd(0.5);
	
	Plane planePtr2 = new Plane(new Point3D(0, -2, 0), new Normal(0, 1, 0));
	planePtr2.setMaterial(mattePtr2);
	w.addObject(planePtr2);
	
	// plane perpendicular to z axis
	
	Matte mattePtr3 = new Matte();		
	mattePtr3.setKa(0.15);
	mattePtr3.setKd(0.6);
	mattePtr3.setCd(0.5);
	
	Plane planePtr3 = new Plane(new Point3D(0, 0, -2), new Normal(0, 0, 1));
	planePtr3.setMaterial(mattePtr3);
	w.addObject(planePtr3);    
    }
    
}
