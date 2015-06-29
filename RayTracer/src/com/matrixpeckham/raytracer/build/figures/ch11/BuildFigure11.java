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
package com.matrixpeckham.raytracer.build.figures.ch11;

import com.matrixpeckham.raytracer.cameras.Spherical;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.compound.Grid;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.PointLight;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.textures.image.ImageTexture;
import com.matrixpeckham.raytracer.textures.image.mappings.SphericalMap;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
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
import static java.lang.Math.abs;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure11 implements BuildWorldFunction{

    @Override
    public void build(World w) {

	int numSamples = 16;
	
	w.vp.setHres(600);			
	w.vp.setVres(400);
	w.vp.setSamples(numSamples);
	w.vp.setPixelSize(1.0);
	w.vp.setMaxDepth(0);
	
	w.tracer = new RayCast(w);	
	
	Spherical spherical = new Spherical();
	
	// for Figure 11.11(a)
		
	spherical.setEye(100, 100, 50);  
	spherical.setLookat(0, -10, 0);
	spherical.setHorizontalFov(84);  
	spherical.setVerticalFov(56);
	
/*
	
	// for Figure 11.11(b)
	
	spherical.setEye(150, 50, 75);  
	spherical.setLookat(0, 10, 0);
	spherical.setHorizontalFov(60);  
	spherical.setVerticalFov(40);

*/
	
	spherical.computeUVW(); 
	w.setCamera(spherical);
	   	
	
	PointLight light2 = new PointLight();
	light2.setLocation(150, 500, 300);  
	light2.scaleRadiance(3.75);  
	light2.setShadows(true);
	w.addLight(light2);
	
	
	// city parameters
	
	double 	a					= 10;   // city block width:  xw extent
	double 	b   				= 12;	// city block length:  yw extent
	int 	numRows			= 10;  	// number of blocks in the xw direction
	int 	numColumns			= 12; 	// number of blocks in the zw direction
	double	width				= 7;	// building width: xw extent in range [min, a - offset]
	double 	length				= 7;	// building length: zw extent in range [min, b - offset]
	double 	minSize			= 6;	// mininum building extent in xw and yw directions
	double 	offset				= 1.0;	// half the minimum distance between buildings
	double 	minHeight			= 0.0; 	// minimum building height
	double 	maxHeight			= 30; 	// maximum bulding height
	double 	height;						// the building height in range [minHeight, maxHeight]
	int		numParkRows		= 4;  	// number of blocks of park in xw direction
	int		numParkColumns	= 6;  	// number of blocks of park in xw direction
	int 	rowTest;					// there are no buildings in the park
	int 	columnTest;				// there are no buildings in the park
	double 	minColor			= 0.1;  // prevents black buildings
	double 	maxColor			= 0.9;	// prevents white buildings
		
	Utility.setRandSeed(15);  				// as the buildings' dimensions and colors are random, it's necessary to 
										// seed rand to keep these quantities the same at each run
										// if you leave this out, and change the number of samples per pixel,
										// these will change
	
	// the buildings are stored in a grid
	
	Grid grid = new Grid();
	
	for (int r = 0; r < numRows; r++)  			// xw direction
		for (int c = 0; c < numColumns; c++) {		// zw direction
			// determine if the block is in the park
		
			if ((r - numRows / 2) >= 0)
				rowTest = r -  numRows / 2;
			else
				rowTest = r -  numRows / 2 + 1;
				
			if ((c - numColumns / 2) >= 0)
				columnTest = c - numColumns / 2;
			else
				columnTest = c - numColumns / 2 + 1;
			
			if (abs(rowTest) >= (numParkRows / 2) || abs(columnTest) >= (numParkColumns / 2)) {
		
				Matte matte = new Matte();
				matte.setKa(0.4); 
				matte.setKd(0.6); 			
				matte.setCd(	minColor + Utility.randDouble() * (maxColor - minColor), 
									minColor + Utility.randDouble() * (maxColor - minColor), 
									minColor + Utility.randDouble() * (maxColor - minColor));    
				
				// block center coordinates
				
				double xc = a * (r - numRows / 2.0 + 0.5);
				double zc = b * (c - numColumns / 2.0 + 0.5);
				
				width = minSize + Utility.randDouble() * (a - 2 * offset - minSize);
				length = minSize + Utility.randDouble() * (b - 2 * offset - minSize);	
				
				// minimum building coordinates
				
				double xmin = xc - width / 2.0;
				double ymin = 0.0;
				double zmin = zc - length / 2.0;
				
				// maximum building coordinates
				
				height = minHeight + Utility.randDouble() * (maxHeight - minHeight);
				
				// The following is a hack to make the middle row and column of buildings higher
				// on average than the other buildings. 
				// This only works properly when there are three rows and columns of buildings
				
				if (r == 1 || r == numRows - 2 || c == 1 || c == numColumns - 2)
					height *= 1.5;
				
				double xmax = xc + width / 2.0;
				double ymax = height;
				double zmax = zc + length / 2.0;
				
				Box building = new  Box(new Point3D(xmin, ymin, zmin), new Point3D(xmax, ymax, zmax));
				building.setMaterial(matte);
				grid.addObject(building);
			}
		}
		
	grid.setupCells();
	w.addObject(grid);
	
	
	// render the park with small green checkers
	
	Checker3D checker3D1 = new Checker3D();
	checker3D1.setSize(5); 
	checker3D1.setColor1(0.35, 0.75, 0.35);  
	checker3D1.setColor2(0.3, 0.5, 0.3);
	
	SV_Matte svMatte1 = new SV_Matte();		
	svMatte1.setKa(0.3);
	svMatte1.setKd(0.50);  
	svMatte1.setCd(checker3D1);
											
	Box park = new Box( 	new Point3D(-a * numParkRows / 2, 0.0, -b * numParkColumns / 2), 
								new Point3D(a * numParkRows / 2, 0.1, b * numParkColumns / 2)  );										
	park.setMaterial(svMatte1);
	w.addObject(park);
											
	
	// ground plane with checker:
	
	Checker3D checker3D2 = new Checker3D();
	checker3D2.setSize(50); 
	checker3D2.setColor1(new RGBColor(0.7));  
	checker3D2.setColor2(new RGBColor(1));
	
	SV_Matte svMatte2 = new SV_Matte();		
	svMatte2.setKa(0.30);
	svMatte2.setKd(0.40);  
	svMatte2.setCd(checker3D2);
	
	Plane plane = new Plane(new Point3D(0, 0.01, 0), new Normal(0, 1, 0));
	plane.setMaterial(svMatte2);
	w.addObject(plane);

	
	Image image = new Image();
        try {	
            image.loadPPMFile(new File("C:\\Users\\Owner\\Documents\\Ground Up raytracer\\Textures\\ppm\\CloudsLowRes.ppm"));
        } catch (IOException ex) {
            Logger.getLogger(BuildFigure11.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
	
	SphericalMap sphericalMap = new SphericalMap(); 
	
	ImageTexture imageTexture = new ImageTexture(image); 
	imageTexture.setMapping(sphericalMap); 
	
	SV_Matte svMatte = new SV_Matte();		
	svMatte.setKa(1.0);
	svMatte.setKd(0.85); 
	svMatte.setCd(imageTexture);
	
	Sphere unitSphere = new Sphere();
	unitSphere.setShadows(false);	
	
	Instance sphere = new Instance(unitSphere); 
	sphere.scale(new Vector3D(1000000));
	sphere.setMaterial(svMatte);
	w.addObject(sphere);




    }
    
}
