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
import com.matrixpeckham.raytracer.geometricobjects.primatives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Disk;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Rectangle;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Sphere;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Torus;
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
        int num_samples = 1;
        
        Sampler uniform_ptr = new MultiJittered(num_samples);

        w.vp.setHres(300);
        w.vp.setVres(300);
        w.vp.setPixelSize(0.05);
        w.vp.setSampler(uniform_ptr);

        w.backgroundColor = new RGBColor(1,1,0);
        w.tracer = new RayCast(w);

        Orthographic orthographic_ptr = new Orthographic();
        orthographic_ptr.setEye(0, 0, 5);
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
        matte_ptr.setCd(new Checker3D());				// yellow	

        Sphere sphere_ptr = new Sphere(new Point3D(0,5,0), 5.0);
        sphere_ptr.setMaterial(matte_ptr);
	//w.addObject(sphere_ptr);    
	
	Reflective reflectivePtr1 = new Reflective();			
	reflectivePtr1.setKa(0); 
	reflectivePtr1.setKd(0.5);
	reflectivePtr1.setCd(0.75, 0.75, 0);    	// yellow
	reflectivePtr1.setKs(0.15);
	reflectivePtr1.setExp(100.0);
	reflectivePtr1.setKr(0.75);
	reflectivePtr1.setCr(Utility.WHITE); 			// default color

        Torus torus = new Torus(1, 0.5);
        torus.setMaterial(matte_ptr);
        Instance inst = new Instance(torus);
        inst.translate(0, 2, 0);
        w.addObject(inst);
        
        Disk disk = new Disk(new Point3D(0, 2, 0), new Normal(0,0,1), 1);
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
        PlaneChecker pcheck = new PlaneChecker();
        SV_Matte pmat = new SV_Matte();
        pmat.setKa(0.2);
        pmat.setKd(0.8);
        pmat.setCd(pcheck);
        
        Plane plane = new Plane();
        plane.setMaterial(pmat);
        //w.addObject(plane);
        
        WireframeBox wireframe = new WireframeBox(new Point3D(-1,-1,-1), new Point3D(1,1,1), 0.1);
        wireframe.setMaterial(matte_ptr);
        //w.addObject(wireframe);
        
        BeveledCylinder cyl = new BeveledCylinder(-1, 1,
                1, 0.1);
        cyl.setMaterial(matte_ptr);
        cyl.setShadows(false);
        w.addObject(cyl);
        
	// skydome with clouds
        Image image = new Image();
        try {
            image.loadPPMFile(new File(
                    "C:\\Users\\Owner\\Documents\\Ground Up raytracer\\Textures\\ppm\\CloudsLowRes.ppm"));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure07.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

        SphericalMap sphericalMap = new SphericalMap();

        ImageTexture imageTexture = new ImageTexture(image);
        imageTexture.setMapping(sphericalMap);

        SV_Matte svMatte2 = new SV_Matte();
        svMatte2.setKa(1);
        svMatte2.setKd(0.85);
        svMatte2.setCd(imageTexture);
        Sphere s = new Sphere(new Point3D(0), 1);
        s.setMaterial(svMatte2);
//        w.addObject(s);
    }
    
}
