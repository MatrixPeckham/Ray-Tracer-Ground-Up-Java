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

import com.matrixpeckham.raytracer.geometricobjects.csg.CSGShadeRec;
import com.matrixpeckham.raytracer.materials.Material;
import com.matrixpeckham.raytracer.textures.Texture;
import com.matrixpeckham.raytracer.util.*;
import com.matrixpeckham.raytracer.world.World;
import java.util.ArrayList;

/**
 *
 * @author William Matrix Peckham
 */
public class ClippedObject extends GeometricObject {

    /**
     * object to bump map
     */
    GeometricObject obj = null;

    /**
     * texture for bump mapping (texture colors are interpreted as vector
     * displacements to normals)
     */
    Texture clipMap = null;

    /**
     * default constructor
     */
    public ClippedObject() {
    }

    public ClippedObject(GeometricObject o, Texture t) {
        obj = o;
        clipMap = t;
    }

    /**
     * copy constructor
     *
     * @param aThis
     */
    private ClippedObject(ClippedObject aThis) {
        super(aThis);
        obj = aThis.obj.cloneGeometry();
        clipMap = aThis.clipMap.cloneTexture();
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
    public void setClipMap(Texture fBmBumpPtr) {
        this.clipMap = fBmBumpPtr.cloneTexture();
    }

    /**
     * clone method
     *
     * @return
     */
    @Override
    public GeometricObject cloneGeometry() {
        return new ClippedObject(this);
    }

    /**
     * Hit function this is where we augment the normal before returning.
     *
     * @param ray
     * @param s
     *
     * @return
     */
    @Override
    public boolean hit(Ray ray, ShadeRec s) {
        //differ to sub object
        boolean hit = obj.hit(ray, s);
        //if we have a hit we need to augment the normal
        if (hit) {
            RGBColor c = clipMap.getColor(s);
            if (c.average() > 0.5) {
                return false;
            }
        }
        return hit;
    }

    /**
     * Hit function this is where we augment the normal before returning.
     *
     * @param ray
     * @param s
     *
     * @return
     */
    @Override
    public boolean hit(Ray ray, ArrayList<CSGShadeRec> hits, ShadeRec sr) {
        //differ to sub object
        ArrayList<CSGShadeRec> nhit = new ArrayList<>();
        boolean hit = obj.hit(ray, nhit, sr);
        //if we have a hit we need to augment the normal
        if (hit) {
            for (CSGShadeRec s : nhit) {
                RGBColor c = clipMap.getColor(s);
                if (!(c.average() > 0.5)) {
                    hits.add(s);
                }
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
     *
     * @return
     */
    @Override
    public boolean shadowHit(Ray ray, DoubleRef t) {
        ShadeRec rec = new ShadeRec((World) null);
        boolean hit = hit(ray, rec);
        t.d = rec.lastT;
        return hit;
    }

    /**
     * Here we call the sub-object's method then augment the normal.
     *
     * @param p
     *
     * @return
     */
    @Override
    public Normal getNormal(Point3D p) {
        return obj.getNormal(p);
    }

    @Override
    public Material getMaterial() {
        return obj.getMaterial(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BBox getBoundingBox() {
        return obj.getBoundingBox(); //To change body of generated methods, choose Tools | Templates.
    }

}
