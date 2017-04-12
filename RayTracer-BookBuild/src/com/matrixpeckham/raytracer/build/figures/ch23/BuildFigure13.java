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
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Phong;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Mesh;
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
public class BuildFigure13 implements BuildWorldFunction {

    @Override
    public void build(World w) {
        try {
            int numSamples = 1;
            
            w.vp.setHres(400);
            w.vp.setVres(400);
            w.vp.setSamples(1);
            
            w.tracer = new RayCast(w);
            
            w.backgroundColor = Utility.BLACK;
            
            Pinhole pinholePtr = new Pinhole();
            pinholePtr.setEye(75, 20, 80);
            pinholePtr.setLookat(-0.05, -0.5, 0);
            pinholePtr.setViewDistance(1600);
            pinholePtr.computeUVW();
            w.setCamera(pinholePtr);
            
            Directional directionalPtr = new Directional();
            directionalPtr.setDirection(0.75, 0.5, -0.15);
            directionalPtr.scaleRadiance(2.0);
            directionalPtr.setShadows(true);
            w.addLight(directionalPtr);
            
            Phong phongPtr1 = new Phong();
            phongPtr1.setKa(0.4);
            phongPtr1.setKd(0.8);
            phongPtr1.setCd(1.0, 0.2, 0.0);
            phongPtr1.setKs(0.5);
            phongPtr1.setCs(1.0, 1.0, 0.0);
            phongPtr1.setExp(50.0);
            String path = "resources/Models/";
            String fileName = "goldfish_low_res.ply";
//	String fileName = "goldfishHighRes.ply";
            TriangleMesh gridPtr = new TriangleMesh(new Mesh());
            gridPtr.readFlatTriangles(Thread.currentThread().getContextClassLoader().getResourceAsStream(path+fileName));
//	gridPtr.readSmoothTriangles(fileName);
            gridPtr.setMaterial(phongPtr1);
            gridPtr.setupCells();
            w.addObject(gridPtr);
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure13.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
 }
    
}
