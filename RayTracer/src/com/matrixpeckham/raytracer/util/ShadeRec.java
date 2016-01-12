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
    public final Point3D hitPoint = new Point3D();
    public final Point3D localHitPosition=new Point3D();
    public final Normal normal=new Normal();
    public final RGBColor color=new RGBColor(Utility.BLACK);
    public final World w;
    public double lastT = Double.POSITIVE_INFINITY;
    //public double t=0;
    public double u = 0;
    public double v = 0;
    public int depth=0;
    public final Ray ray = new Ray();
    public Material material = null;
    public ShadeRec(World w){
        this.w=w;
    }
    public ShadeRec(ShadeRec r){
        hitAnObject=r.hitAnObject;
        localHitPosition.setTo(r.localHitPosition);
        hitPoint.setTo(r.hitPoint);
        color.setTo(r.color);
        w=r.w;
        ray.setTo(r.ray);
        //t=r.t;
        lastT=r.lastT;
        depth=r.depth;
        if(r.material!=null)
            material=r.material;
        normal.setTo(r.normal);
    }
    
}
