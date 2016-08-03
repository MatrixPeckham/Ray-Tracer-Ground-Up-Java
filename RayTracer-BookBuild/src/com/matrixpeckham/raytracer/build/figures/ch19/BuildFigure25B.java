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
package com.matrixpeckham.raytracer.build.figures.ch19;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.primitives.OpenCone;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.ConeChecker;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure25B implements BuildWorldFunction{

    @Override
    public void build(World w) {
int numSamples = 16;
	
	w.vp.setHres(400);      
	w.vp.setVres(400);    
	w.vp.setSamples(numSamples);
	
	w.tracer = new RayCast(w);
	
	w.backgroundColor = Utility.WHITE;
	
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(0, 0, 100); 
	pinholePtr.setEye(0, 50, 100); 
	pinholePtr.setLookat(0.0, 0.75, 0.0); 
	pinholePtr.setViewDistance(16000);           
	pinholePtr.computeUVW();     
	w.setCamera(pinholePtr); 
		
	PointLight lightPtr = new PointLight();
	lightPtr.setLocation(10, 5, 20);		
	lightPtr.scaleRadiance(3.0);
	lightPtr.setShadows(false);
	w.addLight(lightPtr);
	
	
	ConeChecker checkerPtr = new ConeChecker();
	checkerPtr.setColor1(new RGBColor(0.85));
	checkerPtr.setColor2(new RGBColor(0.85));
	checkerPtr.setLineColor(Utility.BLACK);
	checkerPtr.setVerticalLineWidth(0.075);
	checkerPtr.setHorizontalLineWidth(0.075);
	checkerPtr.setNumVerticalCheckers(10);
	checkerPtr.setNumHorizontalCheckers(14);

        SV_Matte svMattePtr = new SV_Matte();		
	svMattePtr.setKa(0.25);
	svMattePtr.setKd(0.85); 
	svMattePtr.setCd(checkerPtr);	
                /*
        Matte svMattePtr = new Matte();
        svMattePtr.setCd(1, 1, 0);
        svMattePtr.setKa(0.25);
        svMattePtr.setKd(0.85);
	*/
	OpenCone conePtr = new OpenCone();
	conePtr.setMaterial(svMattePtr);
	w.addObject(conePtr);    }
    
}
