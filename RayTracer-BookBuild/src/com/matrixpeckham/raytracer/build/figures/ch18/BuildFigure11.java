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
package com.matrixpeckham.raytracer.build.figures.ch18;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.compound.SolidCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primitives.ConcaveSphere;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.AmbientOccluder;
import com.matrixpeckham.raytracer.lights.EnvironmentLight;
import com.matrixpeckham.raytracer.materials.Emissive;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.Phong;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.tracers.Whitted;
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
public class BuildFigure11 implements BuildWorldFunction{

    @Override
    public void build(World w) {
int numSamples = 100;
	
	w.vp.setHres(600);
	w.vp.setVres(400);
	w.vp.setSamples(numSamples);
		
	w.tracer = new Whitted(w);	
		
	AmbientOccluder ambientOccluderPtr = new AmbientOccluder();
	ambientOccluderPtr.setSampler(new MultiJittered(numSamples));
	ambientOccluderPtr.setMinAmount(0.5);
	w.setAmbient(ambientOccluderPtr);

			
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(100, 45, 100);  
	pinholePtr.setLookat(-10, 40, 0);  
	pinholePtr.setViewDistance(400);  
	pinholePtr.computeUVW(); 
	w.setCamera(pinholePtr);

	
	Emissive emissivePtr = new Emissive();
	emissivePtr.scaleRadiance(0.90);
	emissivePtr.setCe(Utility.WHITE);   	
		
	ConcaveSphere spherePtr = new ConcaveSphere();
	spherePtr.setRadius(1000000.0);
	spherePtr.setMaterial(emissivePtr);
	spherePtr.setShadows(false);
	w.addObject(spherePtr);
	
	EnvironmentLight environmentLightPtr = new EnvironmentLight();
	environmentLightPtr.setMaterial(emissivePtr);
	environmentLightPtr.setSampler(new MultiJittered(numSamples));
	environmentLightPtr.setShadows(true);
	w.addLight(environmentLightPtr);
	
	
	double ka = 0.2;  		// commom ambient reflection coefficient
	double ks = 1.0;  		// commom specular reflection coefficient
	double exp = 10.0;   	// for Figure 18.11(a)
//	double exp = 50.0;   	// for Figure 18.11(b)
//	double exp = 200.0;   	// for Figure 18.11(c)
	RGBColor cs=new RGBColor(1, 0, 0); 	// common specular color
	
	// large sphere
	
	Phong phongPtr1 = new Phong();			
	phongPtr1.setKa(ka); 
	phongPtr1.setKd(0.6);
	phongPtr1.setCd(new RGBColor(0.75));
	phongPtr1.setKs(ks);
	phongPtr1.setExp(exp);
	phongPtr1.setCs(cs);
	
	Sphere spherePtr1 = new Sphere(new Point3D(38, 20, -24), 20);
	spherePtr1.setMaterial(phongPtr1);
	w.addObject(spherePtr1);
	
	
	// small sphere
		
	Phong phongPtr2 = new Phong();			
	phongPtr2.setKa(ka); 
	phongPtr2.setKd(0.5);
	phongPtr2.setCd(new RGBColor(0.95));
	phongPtr2.setKs(ks);
	phongPtr2.setExp(exp);
	phongPtr2.setCs(cs);
	
	Sphere spherePtr2 = new Sphere(new Point3D(34, 12, 13), 12);
	spherePtr2.setMaterial(phongPtr2);
	w.addObject(spherePtr2);
	
	
	// medium sphere
	
	Phong phongPtr3 = new Phong();			
	phongPtr3.setKa(ka); 
	phongPtr3.setKd(0.5);
	phongPtr3.setCd(new RGBColor(0.75));
	phongPtr3.setKs(ks);
	phongPtr3.setExp(exp);
	phongPtr3.setCs(cs);
	
	Sphere spherePtr3 = new Sphere(new Point3D(-7, 15, 42), 16);
	spherePtr3.setMaterial(phongPtr3);
	w.addObject(spherePtr3);
	
	
	// cylinder
	
	Phong phongPtr4 = new Phong();			
	phongPtr4.setKa(ka); 
	phongPtr4.setKd(0.5);
	phongPtr4.setCd(new RGBColor(0.60));
	phongPtr4.setKs(ks);
	phongPtr4.setExp(exp);
	phongPtr4.setCs(cs);
	
	double bottom 	= 0.0;
	double top 		= 85; 
	double radius	= 22;
	SolidCylinder cylinderPtr = new SolidCylinder(bottom, top, radius);
	cylinderPtr.setMaterial(phongPtr4);
	w.addObject(cylinderPtr);

	
	// box
	
	Phong phongPtr5 = new Phong();			
	phongPtr5.setKa(ka); 
	phongPtr5.setKd(0.5);
	phongPtr5.setCd(new RGBColor(0.95));
	phongPtr5.setKs(ks);
	phongPtr5.setExp(exp);
	phongPtr5.setCs(cs);
	
	Box boxPtr = new Box(new Point3D(-35, 0, -110), new Point3D(-25, 60, 65));
	boxPtr.setMaterial(phongPtr5);
	w.addObject(boxPtr);
	
				
	// ground plane
		
	Matte mattePtr6 = new Matte();			
	mattePtr6.setKa(0.15); 
	mattePtr6.setKd(0.5);	
	mattePtr6.setCd(0.7);    
	
	Plane planePtr = new Plane(new Point3D(0, 0.01, 0), new Normal(0, 1, 0));
	planePtr.setMaterial(mattePtr6);
	w.addObject(planePtr);    }
    
}
