/*
 * Copyright (C) 2016 William Matrix Peckham
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
package com.matrixpeckham.raytracer.build.figures.ch03;

import com.matrixpeckham.raytracer.cameras.Camera;
import com.matrixpeckham.raytracer.cameras.Orthographic;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.tracers.MultipleObjects;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure20A implements BuildWorldFunction {

    @Override
    public void build(World w) {

	w.vp.setHres(400);
	w.vp.setVres(400);
	w.vp.setPixelSize(0.5);
        w.vp.setSamples(1);
	
	w.backgroundColor = Utility.BLACK;
	w.tracer = new MultipleObjects(w);  
        Camera cam = new Orthographic();
        cam.setEye(0, 0, 100);
        cam.setLookat(0, 0, 0);
	w.setCamera(cam);
        
        // use access functions to set center and radius for this sphere
	
	Sphere sphere = new Sphere();
	sphere.setCenter(new Point3D(0, -25, 0));
	sphere.setRadius(80.0);
	sphere.setColor(1, 0, 0);  // red
	w.addObject(sphere);

	// use a constructor to set center and radius for this sphere
	
	sphere = new Sphere(new Point3D(0, 30, 0), 60);
	sphere.setColor(1, 1, 0);	// yellow
	w.addObject(sphere);
	
	
	Plane plane = new Plane(new Point3D(0.0), new Normal(0, 1, 1));
	plane.setColor(0.0, 0.25, 0.0);	// dark green 
	w.addObject(plane);    }
    
}
