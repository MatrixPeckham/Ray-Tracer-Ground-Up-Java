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
package com.matrixpeckham.raytracer.materials;

import com.matrixpeckham.raytracer.brdfs.Lambertian;
import com.matrixpeckham.raytracer.brdfs.SV_Lambertian;
import com.matrixpeckham.raytracer.textures.Texture;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 *
 * @author William Matrix Peckham
 */
public class SV_Emissive extends Material {
    private Texture ce=null;
    private double ls=1;
    
    public SV_Emissive(){
        super();
    }
    
    public SV_Emissive(SV_Emissive m){
        super(m);
        if(m.ce!=null){
            ce=m.ce.clone();
        }
        ls=m.ls;
    }
    public void scaleRadiance(double ls){
        this.ls=ls;
    }
    @Override
    public RGBColor shade(ShadeRec sr) {
        if(sr.normal.neg().dot(sr.ray.d)>0){
            return ce.getColor(sr).mul(ls);
        } else {
            return Utility.BLACK;
        }
    }
    
    
    
    @Override
    public Material clone() {
        return new SV_Emissive(this);
    }
    
    public void setCe(Texture c){
        ce=c.clone();
    }

    @Override
    public RGBColor getLe(ShadeRec sr) {
        if(sr.normal.neg().dot(sr.ray.d)>0){
            return ce.getColor(sr).mul(ls);
        } else {
            return Utility.BLACK;
        }
    }
    
}
