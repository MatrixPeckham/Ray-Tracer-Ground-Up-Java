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
package com.matrixpeckham.raytracer.build.figures;

import com.matrixpeckham.raytracer.cameras.ThinLens;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Plane;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.tracers.RayCast;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildCh06PageOneImage implements BuildWorldFunction {

    @Override
    public void build(World w) {
        int num_samples = 9;

        w.vp.setHres(600);
        w.vp.setVres(400);
        w.vp.setSamples(num_samples);
        w.vp.setPixelSize(1.0);
        w.vp.setMaxDepth(0);

        w.backgroundColor = Utility.BLACK;
        w.tracer = new RayCast(w);

	// thin lens camera	
        ThinLens thin_lens_ptr = new ThinLens();
        thin_lens_ptr.setEye(100, 100, 50);
        thin_lens_ptr.setLookat(0, -10, 0);
        thin_lens_ptr.setViewDistance(390.0);
        thin_lens_ptr.setFocalDistance(135.0);
        thin_lens_ptr.setLensRadius(5.0);
        thin_lens_ptr.setSampler(new MultiJittered(num_samples));
        thin_lens_ptr.computeUVW();
        w.setCamera(thin_lens_ptr);

        PointLight light_ptr2 = new PointLight();
        light_ptr2.setLocation(150, 500, 300);
        light_ptr2.scaleRadiance(3.75);
        light_ptr2.setShadows(true);
        w.addLight(light_ptr2);

	// city parameters
        double a = 10;   // city block width:  xw extent
        double b = 12;	// city block length:  yw extent
        int num_rows = 10;  	// number of blocks in the xw direction
        int num_columns = 12; 	// number of blocks in the zw direction
        double width = 7;	// building width: xw extent in range [min, a - offset]
        double length = 7;	// building length: zw extent in range [min, b - offset]
        double min_size = 6;	// mininum building extent in xw and yw directions
        double offset = 1.0;	// half the minimum distance between buildings
        double min_height = 0.0; 	// minimum building height
        double max_height = 30; 	// maximum bulding height
        double height;						// the building height in range [min_height, max_height]
        int num_park_rows = 4;  	// number of blocks of park in xw direction
        int num_park_columns = 6;  	// number of blocks of park in xw direction
        int row_test;					// there are no buildings in the park
        int column_test;				// there are no buildings in the park
        double min_color = 0.1;  // prevents colors that are too dark
        double max_color = 0.9;	// prevents colors that are too saturated

        Utility.setRandSeed(15);  				// As the buildings' dimensions and colors are random, it's necessary to 
        // seed rand to keep these quantities the same at each run
        // If you leave this out, and change the number of samples per pixel,
        // these will change

	// The buildings are stored in a grid for efficiency, but you can render them without the grid
        // by storing them directly in the world.
//	Grid grid_ptr = new Grid();
        for (int r = 0; r < num_rows; r++) // xw direction
        {
            for (int c = 0; c < num_columns; c++) {		// zw direction
                // determine if the block is in the park

                if ((r - num_rows / 2) >= 0) {
                    row_test = r - num_rows / 2;
                } else {
                    row_test = r - num_rows / 2 + 1;
                }

                if ((c - num_columns / 2) >= 0) {
                    column_test = c - num_columns / 2;
                } else {
                    column_test = c - num_columns / 2 + 1;
                }

                if (Math.abs(row_test) >= (num_park_rows / 2) || Math.abs(
                        column_test) >= (num_park_columns / 2)) {

				// because both matte_ptr and reflective_ptr call randf, we have to keep one of
                    // them commented out to keep the boxes and colours the same for a given seed
                    Matte matte_ptr = new Matte();
                    matte_ptr.setCd(min_color + Utility.randDouble()
                            * (max_color - min_color),
                            min_color + Utility.randDouble() * (max_color
                            - min_color),
                            min_color + Utility.randDouble() * (max_color
                            - min_color));
                    matte_ptr.setKa(0.4);
                    matte_ptr.setKd(0.6);

				// block center coordinates
                    double xc = a * (r - num_rows / 2.0 + 0.5);
                    double zc = b * (c - num_columns / 2.0 + 0.5);

                    width = min_size + Utility.randDouble() * (a - 2 * offset
                            - min_size);
                    length = min_size + Utility.randDouble() * (b - 2 * offset
                            - min_size);

				// minimum building coordinates
                    double xmin = xc - width / 2.0;
                    double ymin = 0.0;
                    double zmin = zc - length / 2.0;

				// maximum building coordinates
                    height = min_height + Utility.randDouble() * (max_height
                            - min_height);

				// The following is a hack to make the middle row and column of buildings higher
                    // on average than the other buildings. 
                    // This only works when there are three rows and columns of buildings
                    if (r == 1 || r == num_rows - 2 || c == 1 || c
                            == num_columns - 2) {
                        height *= 1.5;
                    }

                    double xmax = xc + width / 2.0;
                    double ymax = height;
                    double zmax = zc + length / 2.0;

                    Box building_ptr = new Box(new Point3D(xmin, ymin, zmin),
                            new Point3D(xmax, ymax, zmax));
                    building_ptr.setMaterial(matte_ptr);
                    w.addObject(building_ptr);
                    //grid_ptr.add_object(building_ptr);
                }
            }
        }

	//grid_ptr.setup_cells();
        //add_object(grid_ptr);
	// render the park with small green checkers
        /*	Checker3D* checker3D_ptr1 = new Checker3D;
         checker3D_ptr1.set_size(5.0); 
         checker3D_ptr1.set_color1(0.35, 0.75, 0.35);  
         checker3D_ptr1.set_color2(0.3, 0.5, 0.3);
        
         SV_Matte sv_matte_ptr1 = new SV_Matte;		
         sv_matte_ptr1.set_ka(0.3);
         sv_matte_ptr1.set_kd(0.50);  
         sv_matte_ptr1.set_cd(checker3D_ptr1);
         */
        Matte sv_matte_ptr1=new Matte();
        sv_matte_ptr1.setCd(.5, .5, .5);
        sv_matte_ptr1.setKa(0.2);
        sv_matte_ptr1.setKd(0.8);
        Box park_ptr = new Box(new Point3D(-a * num_park_rows / 2, 0.0, -b
                * num_park_columns / 2),
                new Point3D(a * num_park_rows / 2, 0.1, b * num_park_columns / 2));
        park_ptr.setMaterial(sv_matte_ptr1);
        w.addObject(park_ptr);

/*	// ground plane with checker:
        Checker3D * checker3D_ptr2 = new Checker3D;
        checker3D_ptr2.set_size(50.0);
        checker3D_ptr2.set_color1(RGBColor(0.7));
        checker3D_ptr2.set_color2(RGBColor(1));

        SV_Matte * sv_matte_ptr2 = new SV_Matte;
        sv_matte_ptr2.set_ka(0.30);
        sv_matte_ptr2.set_kd(0.40);
        sv_matte_ptr2.set_cd(checker3D_ptr2);
*/
        Plane plane_ptr = new Plane(new Point3D(0, 0.01, 0), new Normal(0, 1, 0));
        plane_ptr.setMaterial(sv_matte_ptr1);
        w.addObject(plane_ptr);
    }

}
