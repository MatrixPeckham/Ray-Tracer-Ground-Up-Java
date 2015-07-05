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
package com.matrixpeckham.raytracer.geometricobjects.parametric;

import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;

/**
 * 
 * @author William Matrix Peckham
 */
public class ParametricObject {
    public interface ParametricEquation {
        public double getMinU();
        public double getMaxU();
        public double getMinV();
        public double getMaxV();
        public double getUStep();
        public double getVStep();
        public Point3D getPointAt(double u, double v);
        public Normal getNormalAt(double u, double v);
    }
    
    public ParametricObject(ParametricEquation p){
        
    }
    
}
