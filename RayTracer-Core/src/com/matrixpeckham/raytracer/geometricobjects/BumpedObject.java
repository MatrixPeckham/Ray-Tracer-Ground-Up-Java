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

import com.matrixpeckham.raytracer.textures.Texture;
import com.matrixpeckham.raytracer.util.BBox;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.world.World;
import java.util.ArrayList;

/**
 * Special class for an object that will be bump mapped.
 *
 * @author William Matrix Peckham
 */
public class BumpedObject extends GeometricObject {

    /**
     * object to bump map
     */
    GeometricObject obj = null;

    /**
     * texture for bump mapping (texture colors are interpreted as vector
     * displacements to normals)
     */
    Texture bumpMap = null;

    /**
     * default constructor
     */
    public BumpedObject() {
    }

    /**
     * copy constructor
     *
     * @param aThis
     */
    private BumpedObject(BumpedObject aThis) {
        super(aThis);
        obj = aThis.obj.cloneGeometry();
        bumpMap = aThis.bumpMap.cloneTexture();
    }

    /**
     * sets the object to be bumped.
     *
     * @param obj
     */
    public void setObject(GeometricObject obj) {
        this.obj = obj.cloneGeometry();
    }

    /**
     * sets the texture to use as a bump map
     *
     * @param fBmBumpPtr
     */
    public void setBumpMap(Texture fBmBumpPtr) {
        this.bumpMap = fBmBumpPtr.cloneTexture();
    }

    /**
     * clone method
     *
     * @return
     */
    @Override
    public GeometricObject cloneGeometry() {
        return new BumpedObject(this);
    }

    /**
     * Hit function this is where we augment the normal before returning.
     *
     * @param ray
     * @param s
     * @return
     */
    @Override
    public boolean hit(Ray ray, ShadeRec s) {
        //differ to sub object
        boolean hit = obj.hit(ray, s);
        //if we have a hit we need to augment the normal
        if (hit) {
            //we get the color from the texture and the default normal
            Normal n = new Normal(s.normal);
            RGBColor c = bumpMap.getColor(s);
            //now we add the offset.
            n.addLocal(new Normal(c.r, c.g, c.b));
            //offset in range -1-1 so in theory we could double the normal so we average it then renormalize
            n.x /= 2;
            n.y /= 2;
            n.z /= 2;
            n.normalize();
            s.normal.setTo(n);
        }
        return hit;
    }

    /**
     * Hit function this is where we augment the normal before returning.
     *
     * @param ray
     * @param s
     * @return
     */
    @Override
    public boolean hit(Ray ray, ArrayList<ShadeRec> hits, ShadeRec sr) {
        //differ to sub object
        boolean hit = obj.hit(ray, hits, sr);
        //if we have a hit we need to augment the normal
        if (hit) {
            for (ShadeRec s : hits) {
                //we get the color from the texture and the default normal
                Normal n = new Normal(s.normal);
                RGBColor c = bumpMap.getColor(s);
                //now we add the offset.
                n.addLocal(new Normal(c.r, c.g, c.b));
                //offset in range -1-1 so in theory we could double the normal so we average it then renormalize
                n.x /= 2;
                n.y /= 2;
                n.z /= 2;
                n.normalize();
                s.normal.setTo(n);
            }
        }
        return hit;
    }

    /**
     * shadow hit only differs to sub object, because it doesn't need the normal
     * it doesn't need to alter anything
     *
     * @param ray
     * @param t
     * @return
     */
    @Override
    public boolean shadowHit(Ray ray, DoubleRef t) {
        return obj.shadowHit(ray, t);
    }

    /**
     * Here we call the sub-object's method then augment the normal.
     *
     * @param p
     * @return
     */
    @Override
    public Normal getNormal(Point3D p) {
        Normal n = new Normal(obj.getNormal(p));
        ShadeRec r = new ShadeRec((World) null);
        r.localHitPosition.setTo(p);
        RGBColor c = bumpMap.getColor(r);
        n.addLocal(new Normal(c.r, c.g, c.b));
        n.x /= 2;
        n.y /= 2;
        n.z /= 2;
        n.normalize();
        return n;
    }

    /*    @Override
     public Material getMaterial() {
     return obj.getMaterial(); //To change body of generated methods, choose Tools | Templates.
     }
     */
    @Override
    public BBox getBoundingBox() {
        return obj.getBoundingBox(); //To change body of generated methods, choose Tools | Templates.
    }

}
