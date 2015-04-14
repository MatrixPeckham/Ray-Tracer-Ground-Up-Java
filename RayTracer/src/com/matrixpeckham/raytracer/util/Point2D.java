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
public class Point2D {
    public double x;
    public double y;
    @Override
    public String toString(){
        return "("+x+","+y+")Vector2D";
    }
    public Point2D(){
        this(0);
    }
    public Point2D(double a){
        this(a,a);
    }
    public Point2D(double x, double y){
        this.x=x;
        this.y=y;
    }
    public Point2D(Point2D p){
        x=p.x;
        y=p.y;
    }
    public Point2D setTo(Point2D p){
        x=p.x;
        y=p.y;
        return this;
    }
    public Point2D setTo(double x, double y){
        this.x=x;
        this.y=y;
        return this;
    }
    public Point2D mul(double a){
        return new Point2D(a*x,a*y);
    }
}
