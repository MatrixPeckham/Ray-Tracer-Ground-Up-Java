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
package com.matrixpeckham.raytracer.build.figures.ch23;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.compound.TriangleMesh;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Mesh;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure07 implements BuildWorldFunction{

    @Override
    public void build(World w) {
	int numSamples = 16;
	
	w.vp.setHres(400);      
	w.vp.setVres(400);    
	w.vp.setSamples(numSamples);	
	
	w.tracer = new RayCast(w);
	
	w.backgroundColor = Utility.BLACK;

	Pinhole pinhole = new Pinhole();
	pinhole.setEye(100, 50, 90);
	pinhole.setLookat(0, -0.5, 0);
	pinhole.setViewDistance(16000);  	
	pinhole.computeUVW();     
	w.setCamera(pinhole); 
		
	Directional directional = new Directional();
	directional.setDirection(0.75, 1, -0.15);     
	directional.scaleRadiance(4.5);  
	directional.setShadows(true);
	w.addLight(directional);
	
	Matte matte1 = new Matte();			
	matte1.setKa(0.1); 
	matte1.setKd(0.75);   
	matte1.setCd(0.1, 0.5, 1.0);
	
	String fileName = "C:\\Users\\Owner\\Documents\\Ground Up raytracer\\Models\\TwoTriangles.ply";
        TriangleMesh grid = new TriangleMesh(new Mesh());
        try {
//            grid.readFlatTriangles(new File(fileName));		// for Figure 23.7(a)
          grid.readSmoothTriangles(new File(fileName));		// for Figure 23.7(b)
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure07.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
	grid.setMaterial(matte1);   
	grid.setupCells();
	w.addObject(grid);
	
	Matte matte2 = new Matte();			
	matte2.setCd(1, 0.9, 0.6);
	matte2.setKa(0.25); 
	matte2.setKd(0.4);
		
	Plane plane1 = new Plane(new Point3D(0, -2.0, 0),new Normal(0, 1, 0));  
	plane1.setMaterial(matte2);
	w.addObject(plane1);	



    }
    
}
