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

import com.matrixpeckham.raytracer.materials.Material;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class ShadeRec {
    public boolean hitAnObject=false;
    public Point3D hitPoint = new Point3D();
    public Point3D localHitPosition=new Point3D();
    public Normal normal=new Normal();
    public RGBColor color=new RGBColor(Constants.BLACK);
    public World w;
    public double lastT = Double.POSITIVE_INFINITY;
    public double t=0;
    public int depth=0;
    public Ray ray = new Ray();
    public Material material = null;
    public ShadeRec(World w){
        this.w=w;
    }
    public ShadeRec(ShadeRec r){
        hitAnObject=r.hitAnObject;
        localHitPosition.setTo(r.localHitPosition);
        color.setTo(r.color);
        w=r.w;
        ray.setTo(r.ray);
        t=r.t;
        depth=r.depth;
        material=r.material.clone();
        normal.setTo(r.normal);
    }
    
}
