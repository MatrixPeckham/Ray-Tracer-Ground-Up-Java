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
import com.matrixpeckham.raytracer.tracers.MultipleObjects;
import com.matrixpeckham.raytracer.tracers.SingleSphere;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure18 implements BuildWorldFunction {

    @Override
    public void build(World w) {

	w.vp.setHres(400);
	w.vp.setVres(400);
	w.vp.setPixelSize(0.5);
        w.vp.setSamples(1);
	
	w.backgroundColor = Utility.BLACK;
	w.tracer = new SingleSphere(w);  
        Camera cam = new Orthographic();
        cam.setEye(0, 0, 100);
        cam.setLookat(0, 0, 0);
	w.setCamera(cam);
        
        w.sphere.setCenter(new Point3D());
        w.sphere.setRadius(85);
    }
    
}
