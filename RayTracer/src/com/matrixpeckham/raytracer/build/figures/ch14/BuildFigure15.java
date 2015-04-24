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
package com.matrixpeckham.raytracer.build.figures.ch14;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.beveledobjects.BeveledCylinder;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure15 implements BuildWorldFunction{

    @Override
    public void build(World w) {

	int numSamples = 16;
	
	w.vp.setHres(650);	  		
	w.vp.setVres(300);
	w.vp.setSamples(numSamples);
	
	w.tracer = new RayCast(w);
	
	Pinhole pinhole = new Pinhole();
	pinhole.setEye(0, 0, 100);
	pinhole.setLookat(0, 0, 0);
	pinhole.setViewDistance(6000); 
	pinhole.computeUVW();
	w.setCamera(pinhole);
	
	Directional light2 = new Directional();
	light2.setDirection(20, 0, 20);
	light2.scaleRadiance(3.0);
	w.addLight(light2);
	
	// beveled cylinder
	
	double bottom 		= -2.0;
	double top 			= 2.0;
	double radius 		= 1.0;
	double bevelRadius 	= 0.2;
	
	BeveledCylinder cylinder1 = new BeveledCylinder(bottom, top, radius, bevelRadius);
	
	for (int j = 0; j < 4; j++) {
	
		Matte matte = new Matte();
		matte.setKa(0.25); 					
		matte.setKd(0.1 + 0.3 * j);
		matte.setCd(0.5);
	
		Instance cylinder2 = new Instance(cylinder1);
		cylinder2.translate(-3.75 + 2.5 * j, 0, 0);
		cylinder2.setMaterial(matte);
                cylinder2.setShadows(false);
		w.addObject(cylinder2);
	}

    }
    
}
