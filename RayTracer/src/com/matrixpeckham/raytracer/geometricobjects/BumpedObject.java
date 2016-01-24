/*
 * Copyright (C) 2016 William Matrix Peckham
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

import com.matrixpeckham.raytracer.geometricobjects.primitives.Rectangle;
import com.matrixpeckham.raytracer.textures.Texture;
import com.matrixpeckham.raytracer.textures.procedural.FBMBump;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;

/**
 *
 * @author William Matrix Peckham
 */
public class BumpedObject extends GeometricObject {
    GeometricObject obj=null;
    Texture bumpMap = null;
    public BumpedObject(){}
    private BumpedObject(BumpedObject aThis) {
        super(aThis);
        obj=aThis.obj.clone();
        bumpMap=aThis.bumpMap.clone();
    }
    

    public void setObject(GeometricObject obj) {
        this.obj=obj.clone();
    }

    public void setBumpMap(Texture fBmBumpPtr) {
        this.bumpMap=fBmBumpPtr.clone();
    }

    @Override
    public GeometricObject clone() {
        return new BumpedObject(this);
    }

    @Override
    public boolean hit(Ray ray, ShadeRec s) {
        boolean hit = obj.hit(ray, s);
        if(hit){
            Normal n = new Normal(s.normal);
            RGBColor c = bumpMap.getColor(s);
            n.addLocal(new Normal(c.r,c.g,c.b));
            //n.x/=2;
            //n.y/=2;
            //n.z/=2;
            n.normalize();
            s.normal.setTo(n);
        }
        return hit;
    }

    @Override
    public boolean shadowHit(Ray ray, DoubleRef t) {
        return obj.shadowHit(ray, t);
    }
    
}
