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
package com.matrixpeckham.raytracer.build.figures.ch09;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.compound.Grid;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Plane;
import com.matrixpeckham.raytracer.lights.Directional;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Normal;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure10 implements BuildWorldFunction {

    @Override
    public void build(World w) {

        w.vp.setHres(400);
        w.vp.setVres(400);
        w.vp.setSamples(25);

        w.tracer = new RayCast(w);

        Pinhole camera = new Pinhole();

	// Figure9.10(a)
/*	
         camera.setEye(150, 195, 125);
         camera.setLookat(0, 195, -40);
         camera.setViewDistance(167);
         */
        /*		
         // Figure9.10(b)
		
         camera.setEye(150, 300, 125);   
         camera.setLookat(0, 265, -40);  
         camera.setViewDistance(167);
         */
	// Figure9.10(c)
        camera.setEye(-250, 350, 500);
        camera.setLookat(-250, 350, 0);
        camera.setViewDistance(280);

        camera.computeUVW();
        w.setCamera(camera);

        Directional light1 = new Directional();
        light1.setDirection(150, 200, 65);
        light1.scaleRadiance(5.0);
        light1.setShadows(true);
        w.addLight(light1);

        Matte matte1 = new Matte();
        matte1.setCd(0, 0.5, 0.5);     // cyan
        matte1.setKa(0.4);
        matte1.setKd(0.5);

        Matte matte2 = new Matte();
        matte2.setCd(0.8, 0.5, 0);     // orange
        matte2.setKa(0.4);
        matte2.setKd(0.5);

        Matte matte3 = new Matte();
        matte3.setCd(0.5, 0.6, 0);     // green
        matte3.setKa(0.4);
        matte3.setKd(0.5);

	// construct rows of boxes parallel to the zw axis
        Grid grid = new Grid();

	// first row
        int numBoxes = 40;
        double wx = 50;
        double wz = 50;
        double h = 150;
        double s = 100;

        for (int j = 0; j < numBoxes; j++) {
            Box box = new Box(new Point3D(-wx, 0, -(j + 1) * wz - j * s),
                    new Point3D(0, h, -j * wz - j * s));
            box.setMaterial(matte2);
            //	addObject(box);
            grid.addObject(box);
        }

	// second row
        h = 300;

        for (int j = 0; j < numBoxes; j++) {
            Box box = new Box(
                    new Point3D(-wx - wx - s, 0, -(j + 1) * wz - j * s),
                    new Point3D(-wx - s, h, -j * wz - j * s));
            box.setMaterial(matte1);
//		addObject(box);
            grid.addObject(box);
        }

	// third row
        h = 600;

        for (int j = 0; j < numBoxes; j++) {
            Box box = new Box(new Point3D(-wx - 2 * wx - 2 * s, 0, -(j + 1) * wz
                    - j * s),
                    new Point3D(-2 * wx - 2 * s, h, -j * wz - j * s));
            box.setMaterial(matte3);
//		addObject(box);
            grid.addObject(box);
        }

	// a column
        h = 150;

        for (int j = 0; j < numBoxes; j++) {
            Box box = new Box(new Point3D(-3 * (wx + s) - (j + 1) * wz - j * s,
                    0, -wx),
                    new Point3D(-3 * (wx + s) - j * wz - j * s, h, 0));
            box.setMaterial(matte2);
//		addObject(box);
            grid.addObject(box);
        }

        grid.setupCells();
        w.addObject(grid);

	// ground plane with checker:
        Checker3D checker3D = new Checker3D();
        checker3D.setSize(wx);
        checker3D.setColor1(0.7);
        checker3D.setColor2(1.0);

        SV_Matte matte = new SV_Matte();
        matte.setKa(0.25);
        matte.setKd(0.35);
        matte.setCd(checker3D);

        Plane plane = new Plane(new Point3D(0, 1, 0), new Normal(0, 1, 0));
        plane.setMaterial(matte);
        w.addObject(plane);

    }

}
