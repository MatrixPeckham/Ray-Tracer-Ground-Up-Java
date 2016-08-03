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
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.compound.Grid;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.SphereChecker;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;
import static java.lang.Math.pow;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure14 implements BuildWorldFunction {

    @Override
    public void build(World w) {
int numSamples = 1;
	
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
	
	int numSpheres	= 25;
	double volume = 0.1 / numSpheres;
	double radius = 2.5 * pow(0.75 * volume / Math.PI, 0.333333);
	
	Utility.setRandSeed(14);  
	
	for (int j = 0; j < numSpheres; j++) {
		SphereChecker checkerPtr = new SphereChecker();
		checkerPtr.setNumlong(20);
		checkerPtr.setNumlat(10);
		checkerPtr.setLineWidth(0.05);
		
		RGBColor color=new RGBColor(Utility.randDouble(), Utility.randDouble(), Utility.randDouble());
		checkerPtr.setColor1(color);
		checkerPtr.setColor2(color);
		checkerPtr.setLineColor(Utility.BLACK);
		
		SV_Matte svMattePtr1 = new SV_Matte();		
		svMattePtr1.setKa(0.25);
		svMattePtr1.setKd(0.85);
		svMattePtr1.setCd(checkerPtr);
		
		Sphere spherePtr1 = new Sphere();
		spherePtr1.setMaterial(svMattePtr1);
		
		Instance spherePtr2 = new Instance(spherePtr1);
		spherePtr2.scale(radius,radius,radius);
		spherePtr2.translate(1.0 - 2.0 * Utility.randDouble(), 1.0 - 2.0 * Utility.randDouble(), 1.0 - 2.0 * Utility.randDouble());
		spherePtr2.computeBoundingBox();
		
		gridPtr.addObject(spherePtr2);
	}	
	
	gridPtr.setupCells();
	w.addObject(gridPtr);	
    }
    
}
