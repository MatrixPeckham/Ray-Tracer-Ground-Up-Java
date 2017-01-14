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
import com.matrixpeckham.raytracer.geometricobjects.compound.Grid;
import com.matrixpeckham.raytracer.geometricobjects.compound.TriangleMesh;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.RGBColor;
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
public class BuildFigure10A implements BuildWorldFunction {

    @Override
    public void build(World w) 
// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.

// This builds the scene for Figure 23.10(a)
// The scene is Naomi Hatchman's penguin_ with flat shading
    {
        try// 	Copyright (C) Kevin Suffern 2000-2007.
//	This C++ code is for non-commercial purposes only.
//	This C++ code is licensed under the GNU General Public License Version 2.
//	See the file COPYING.txt for the full license.
// This builds the scene for Figure 23.10(a)
// The scene is Naomi Hatchman's penguin_ with flat shading
        {
            int numSamples = 16;
            
            w.vp.setHres(400);
            w.vp.setVres(400);
            w.vp.setMaxDepth(0);
            w.vp.setSamples(numSamples);
            w.vp.setPixelSize(1.0);
            
            w.backgroundColor = Utility.BLACK;
            
            w.tracer = new RayCast(w);
            
            Pinhole pinholePtr = new Pinhole();
            pinholePtr.setEye(50, 40, 110);
            pinholePtr.setLookat(1, 0, 0);
            pinholePtr.setViewDistance(1500);
            pinholePtr.computeUVW();
            w.setCamera(pinholePtr);
            
            Directional directionalPtr = new Directional();
            directionalPtr.setDirection(0.75, 1, 1);
            directionalPtr.scaleRadiance(2.5);
            directionalPtr.setShadows(true);
            w.addLight(directionalPtr);
            
            
            double ka = 0.25;
            double kd = 0.75;
            
            RGBColor bodyColor=new RGBColor(0.5, 0.5, 1.0);
            RGBColor wingColor=new RGBColor(0.5, 1.0, 0.4);
            RGBColor feetColor=new RGBColor(1.0, 0.8, 0.34);
            RGBColor eyeballColor=new RGBColor(1.0);
            RGBColor eyelidColor=new RGBColor(1, 0, 0);
            
            // body
            
            Matte mattePtr1 = new Matte();
            mattePtr1.setKa(ka);
            mattePtr1.setKd(0.75);
            mattePtr1.setCd(bodyColor);
            String path = "/Models/Penguin/";
            TriangleMesh bodyPtr = new TriangleMesh();
            bodyPtr.readFlatTriangles(getClass().getClassLoader().getResourceAsStream(path+"penguin_body.ply"));
            bodyPtr.setMaterial(mattePtr1);
            bodyPtr.setupCells();
            w.addObject(bodyPtr);
            
            // wings
            
            Matte mattePtr2 = new Matte();
            mattePtr2.setKa(ka);
            mattePtr2.setKd(0.5);
            mattePtr2.setCd(wingColor);
            
            TriangleMesh leftWingPtr = new TriangleMesh();
            leftWingPtr.readFlatTriangles(getClass().getClassLoader().getResourceAsStream(path+"penguin_left_wing.ply"));
            leftWingPtr.setMaterial(mattePtr2);
            leftWingPtr.setupCells();
            
            TriangleMesh rightWingPtr = new TriangleMesh();
            rightWingPtr.readFlatTriangles(getClass().getClassLoader().getResourceAsStream(path+"penguin_right_wing.ply"));
            rightWingPtr.setMaterial(mattePtr2);
            rightWingPtr.setupCells();
            
            
            // feet
            
            Matte mattePtr3 = new Matte();
            mattePtr3.setKa(ka);
            mattePtr3.setKd(0.5);
            mattePtr3.setCd(feetColor);
            
            TriangleMesh leftFootPtr = new TriangleMesh();
            leftFootPtr.readFlatTriangles(getClass().getClassLoader().getResourceAsStream(path+"penguin_left_foot.ply"));
            leftFootPtr.setMaterial(mattePtr3);
            leftFootPtr.setupCells();
            
            TriangleMesh rightFootPtr = new TriangleMesh();
            rightFootPtr.readFlatTriangles(getClass().getClassLoader().getResourceAsStream(path+"penguin_right_foot.ply"));
            rightFootPtr.setMaterial(mattePtr3);
            rightFootPtr.setupCells();
            
            
            // eyeballs
            
            Matte mattePtr4 = new Matte();
            mattePtr4.setKa(ka);
            mattePtr4.setKd(kd);
            mattePtr4.setCd(eyeballColor);
            
            TriangleMesh leftEyeballPtr = new TriangleMesh();
            leftEyeballPtr.readFlatTriangles(getClass().getClassLoader().getResourceAsStream(path+"penguin_left_eyeball.ply"));
            leftEyeballPtr.setMaterial(mattePtr4);
            leftEyeballPtr.setupCells();
            
            TriangleMesh rightEyeballPtr = new TriangleMesh();
            rightEyeballPtr.readFlatTriangles(getClass().getClassLoader().getResourceAsStream(path+"penguin_right_eyeball.ply"));
            rightEyeballPtr.setMaterial(mattePtr4);
            rightEyeballPtr.setupCells();
            
            
            // eyelids
            
            Matte mattePtr5 = new Matte();
            mattePtr5.setKa(ka);
            mattePtr5.setKd(kd);
            mattePtr5.setCd(eyelidColor);
            
            TriangleMesh leftLowerEyelidPtr = new TriangleMesh();
            leftLowerEyelidPtr.readFlatTriangles(getClass().getClassLoader().getResourceAsStream(path+"penguin_left_lower_eyelid.ply"));
            leftLowerEyelidPtr.setMaterial(mattePtr5);
            leftLowerEyelidPtr.setupCells();
            
            TriangleMesh rightLowerEyelidPtr = new TriangleMesh();
            rightLowerEyelidPtr.readFlatTriangles(getClass().getClassLoader().getResourceAsStream(path+"penguin_right_lower_eyelid.ply"));
            rightLowerEyelidPtr.setMaterial(mattePtr5);
            rightLowerEyelidPtr.setupCells();
            
            
            TriangleMesh leftUpperEyelidPtr = new TriangleMesh();
            leftUpperEyelidPtr.readFlatTriangles(getClass().getClassLoader().getResourceAsStream(path+"penguin_left_upper_eyelid.ply"));
            leftUpperEyelidPtr.setMaterial(mattePtr5);
            leftUpperEyelidPtr.setupCells();
            
            TriangleMesh rightUpperEyelidPtr = new TriangleMesh();
            rightUpperEyelidPtr.readFlatTriangles(getClass().getClassLoader().getResourceAsStream(path+"penguin_right_upper_eyelid.ply"));
            rightUpperEyelidPtr.setMaterial(mattePtr5);
            rightUpperEyelidPtr.setupCells();
            
            
            
            // complete penguin_
            
            Grid penguin_Ptr = new Grid();
            penguin_Ptr.addObject(bodyPtr);
            penguin_Ptr.addObject(leftWingPtr);
            penguin_Ptr.addObject(rightWingPtr);
            penguin_Ptr.addObject(leftFootPtr);
            penguin_Ptr.addObject(rightFootPtr);
            penguin_Ptr.addObject(leftEyeballPtr);
            penguin_Ptr.addObject(rightEyeballPtr);
            penguin_Ptr.addObject(leftLowerEyelidPtr);
            penguin_Ptr.addObject(rightLowerEyelidPtr);
            penguin_Ptr.addObject(leftUpperEyelidPtr);
            penguin_Ptr.addObject(rightUpperEyelidPtr);
            
            penguin_Ptr.setupCells();
            w.addObject(penguin_Ptr);
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure10A.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
}

 
}
