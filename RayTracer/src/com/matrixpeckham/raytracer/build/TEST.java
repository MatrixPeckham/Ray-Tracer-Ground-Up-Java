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

import com.matrixpeckham.raytracer.build.figures.ch11.BuildFigure07;
import com.matrixpeckham.raytracer.cameras.Orthographic;
import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.cameras.ThinLens;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.beveledobjects.BeveledBox;
import com.matrixpeckham.raytracer.geometricobjects.beveledobjects.BeveledCylinder;
import com.matrixpeckham.raytracer.geometricobjects.compound.WireframeBox;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConvexPartSphere;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.OpenPartCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Disk;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Rectangle;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Torus;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.AmbientOccluder;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.lights.Light;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.Reflective;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.samplers.Sampler;
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.textures.image.ImageTexture;
import com.matrixpeckham.raytracer.textures.image.mappings.SphericalMap;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
import com.matrixpeckham.raytracer.textures.procedural.PlaneChecker;
import com.matrixpeckham.raytracer.textures.procedural.SphereChecker;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
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
public class TEST implements BuildWorldFunction{

    @Override
    public void build(World w) {
        int num_samples = 16;
        
        Sampler uniform_ptr = new MultiJittered(num_samples);

        w.vp.setHres(300);
        w.vp.setVres(300);
        w.vp.setPixelSize(0.01);
        w.vp.setSampler(uniform_ptr);

        w.backgroundColor = new RGBColor(1,1,0);
        w.tracer = new RayCast(w);

        Orthographic orthographic_ptr = new Orthographic();
        orthographic_ptr.setEye(5, 5, 5);
        orthographic_ptr.setLookat(new Point3D(0));
        w.setCamera(orthographic_ptr);

        Pinhole pinhole = new Pinhole();
        pinhole.setEye(2.5, 2.5, 5);
        pinhole.setZoom(1);
        pinhole.setViewDistance(1);
        pinhole.setLookat(new Point3D(0));
        //w.setCamera(pinhole);

// thin lens camera	
        ThinLens thin_lens_ptr = new ThinLens();
        thin_lens_ptr.setEye(50, 50, 100);
        thin_lens_ptr.setLookat(0, 0, 0);
        thin_lens_ptr.setViewDistance(200.0);
        thin_lens_ptr.setFocalDistance(120.0);
        thin_lens_ptr.setLensRadius(1.0);
        thin_lens_ptr.setSampler(new MultiJittered(num_samples));
        thin_lens_ptr.computeUVW();
        //w.setCamera(thin_lens_ptr);        

        AmbientOccluder occ = new AmbientOccluder();
        occ.setSampler(new MultiJittered(num_samples));
        occ.setMinAmount(0);
        occ.setLs(2);
        
        w.ambient = new Ambient();
        //w.ambient = occ;
        
        
        Directional light_ptr = new Directional();
        //light_ptr.setLocation(100, 100, 200);
        light_ptr.setDirection(1,0.5,0);
        light_ptr.scaleRadiance(2.0);
        //light_ptr.setExp(2);
        w.addLight(light_ptr);

        SV_Matte matte_ptr = new SV_Matte();
        matte_ptr.setKa(0.2);
        matte_ptr.setKd(0.8);
        matte_ptr.setCd(new SphereChecker());				// yellow	

	

        Torus torus = new Torus(1, 0.5);
        torus.setMaterial(matte_ptr);
//        w.addObject(torus);
        
/*        ConvexPartSphere cps = new ConvexPartSphere(new Point3D(), 1,
                90, 180, 90, 180);
        cps.setMaterial(matte_ptr);
        w.addObject(cps);*/
        OpenPartCylinder pc = new OpenPartCylinder(-1, 1, 1,
                        90, 180);
        pc.setMaterial(matte_ptr);
        w.addObject(pc);
        
    }
    
}
