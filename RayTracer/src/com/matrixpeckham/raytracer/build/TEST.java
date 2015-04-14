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
package com.matrixpeckham.raytracer.build;

import com.matrixpeckham.raytracer.cameras.Orthographic;
import com.matrixpeckham.raytracer.cameras.ThinLens;
import com.matrixpeckham.raytracer.geometricobjects.beveledobjects.BeveledBox;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Disk;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Rectangle;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Sphere;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Torus;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.lights.Light;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.samplers.Sampler;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class TEST implements BuildWorldFunction{

    @Override
    public void build(World w) {
        int num_samples = 25;
        
        Sampler uniform_ptr = new MultiJittered(num_samples);

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setPixelSize(0.05);
        w.vp.setSampler(uniform_ptr);

        w.backgroundColor = Utility.BLACK;
        w.tracer = new RayCast(w);

        Orthographic orthographic_ptr = new Orthographic();
        orthographic_ptr.setEye(0, 40, 100);
        orthographic_ptr.setLookat(new Point3D(0));
        w.setCamera(orthographic_ptr);

// thin lens camera	
        ThinLens thin_lens_ptr = new ThinLens();
        thin_lens_ptr.setEye(50, 50, 100);
        thin_lens_ptr.setLookat(0, 0, 0);
        thin_lens_ptr.setViewDistance(200.0);
        thin_lens_ptr.setFocalDistance(100.0);
        thin_lens_ptr.setLensRadius(1.0);
        thin_lens_ptr.setSampler(new MultiJittered(num_samples));
        thin_lens_ptr.computeUVW();
        //w.setCamera(thin_lens_ptr);        

        w.ambient = new Ambient();

        Directional light_ptr = new Directional();
        //light_ptr.setLocation(100, 100, 200);
        light_ptr.setDirection(1,0.5,0);
        light_ptr.scaleRadiance(2.0);
        //light_ptr.setExp(2);
        w.addLight(light_ptr);

        Matte matte_ptr = new Matte();
        matte_ptr.setKa(0.2);
        matte_ptr.setKd(0.8);
        matte_ptr.setCd(1, 1, 0);				// yellow	

        Sphere sphere_ptr = new Sphere(new Point3D(0.0), 5.0);
        sphere_ptr.setMaterial(matte_ptr);
	w.addObject(sphere_ptr);    

        Torus torus = new Torus();
        torus.setMaterial(matte_ptr);
        //w.addObject(torus);
        
        Disk disk = new Disk();
        disk.setMaterial(matte_ptr);
        //w.addObject(disk);
        
        Rectangle rect = new Rectangle(new Point3D(-1,0,-1), new Vector3D(2,0,0), new Vector3D(0,2,0), new Normal(0,0,-1));
        rect.setMaterial(matte_ptr);
        //w.addObject(rect);
        
        Box box = new Box(-2.5,2.5, -2.5,2.5,-2.5,2.5);
        box.setMaterial(matte_ptr);
        //w.addObject(box);
        
        BeveledBox bevl = new BeveledBox(new Point3D(-2.5,-2.5,-2.5), new Point3D(2.5, 2.5, 2.5), 0.25);
        bevl.setMaterial(matte_ptr);
        //w.addObject(bevl);
    }
    
}
