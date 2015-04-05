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
package com.matrixpeckham.raytracer.util;

/**
 *
 * @author William Matrix Peckham
 */
public class Constants {
    public static final double PI = Math.PI;
    public static final double TWO_PI = 2*PI;
    public static final double PI_ON_180 = PI/180.0;
    public static final double INV_PI = 1.0/PI;
    public static final double INV_2_PI = 1.0/TWO_PI;
    public static final double EPSILON = 0.0001;
    public static final double HUGE_VALUE = 1.0e10;
    public static final RGBColor BLACK = new RGBColor(0);
    public static final RGBColor WHITE = new RGBColor(1);
    public static final RGBColor RED = new RGBColor(1,0,0);
    
}
