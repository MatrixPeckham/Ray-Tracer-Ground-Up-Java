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
package com.matrixpeckham.raytracer.geometricobjects.compound;

import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConcavePartCylinder;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConvexPartCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Disk;
import com.matrixpeckham.raytracer.geometricobjects.primitives.OpenCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Ring;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;

/**
 * Thick Ring, this class implements a ring that has a thickness
 *
 * as with most compound subclasses nothing is overridden, constructor adds
 * child objects and does nothing else.
 *
 * @author William Matrix Peckham
 */
public class ThickRing extends Compound {

    /**
     * default constructor
     */
    public ThickRing() {
        this(-1, 1, 0.5, 1);
    }

    /**
     * Initializing constructor
     *
     * @param bottom
     * @param top
     * @param innerRadius
     * @param outerRadius
     */
    public ThickRing(double bottom, double top, double innerRadius,
            double outerRadius) {

        //top ring
        Ring topd = new Ring(new Point3D(0, top, 0), new Normal(0, 1,
                0), innerRadius, outerRadius);
        //bottom ring
        Ring bottomd = new Ring(new Point3D(0, bottom, 0), new Normal(0, -1,
                0), innerRadius, outerRadius);
        //for the outer edges, we use PartCylinders. this lets us use the 
        //convex and concave ones for oute and inner, lets this class play nice 
        //with transparent materials
        ConcavePartCylinder innercyl = new ConcavePartCylinder(bottom, top,
                innerRadius, 0, 360);
        ConvexPartCylinder outercyl = new ConvexPartCylinder(bottom, top,
                outerRadius, 0, 360);
        addObject(topd);
        addObject(bottomd);
        addObject(innercyl);
        addObject(outercyl);
    }

    /**
     * sets material of the bottom ring
     *
     * @param mattePtr1
     */
    public void setBottomMaterial(Matte mattePtr1) {
        objects.get(1).setMaterial(mattePtr1);
    }

    /**
     * sets material of the top ring
     *
     * @param mattePtr1
     */
    public void setTopMaterial(Matte mattePtr1) {
        objects.get(0).setMaterial(mattePtr1);
    }

    /**
     * sets material for both inner and outer walls
     *
     * @param mattePtr2
     */
    public void setWallMaterial(Matte mattePtr2) {
        objects.get(2).setMaterial(mattePtr2);
        objects.get(3).setMaterial(mattePtr2);
    }

    /**
     * sets material for inner wall
     *
     * @param mattePtr2
     */
    public void setInnerWallMaterial(Matte mattePtr2) {
        objects.get(2).setMaterial(mattePtr2);
    }

    /**
     * sets material for outer wall
     *
     * @param mattePtr3
     */
    public void setOuterWallMaterial(Matte mattePtr3) {
        objects.get(3).setMaterial(mattePtr3);
    }

}
