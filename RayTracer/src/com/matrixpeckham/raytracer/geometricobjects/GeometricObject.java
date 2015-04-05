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
package com.matrixpeckham.raytracer.geometricobjects;

import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;

/**
 *
 * @author William Matrix Peckham
 */
public abstract class GeometricObject {
    protected RGBColor color;
    
		// default ructor
		public GeometricObject(){}
                
		// copy ructor
                public GeometricObject( GeometricObject object){}
                
		        // virtual copy ructor
		public abstract GeometricObject clone();

			
		 public abstract boolean hit( Ray ray, ShadeRec s);
		

		// the following three functions are only required for Chapter 3
		
		public void setColor( RGBColor c){
                    color.setTo(c);
                }
                public void setColor(float r, float g, float b){
                    color.setTo(r, g, b);
                }

                public RGBColor getColor(){
                    return color;
                }
	}
