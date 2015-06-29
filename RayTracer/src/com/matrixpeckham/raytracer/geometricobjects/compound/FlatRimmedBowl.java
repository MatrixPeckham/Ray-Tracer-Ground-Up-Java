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

import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConcavePartSphere;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConvexPartSphere;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Ring;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;

/**
 *
 * @author William Matrix Peckham
 */
public class FlatRimmedBowl extends Compound {

    public FlatRimmedBowl(double innerRadius, double outerRadius) {
        ConvexPartSphere outer = new ConvexPartSphere(new Point3D(), outerRadius, 0,
                        360, 90, 180);
        ConcavePartSphere inner = new ConcavePartSphere(new Point3D(), innerRadius, 0,
                        360, 90, 180);
        Ring rim = new Ring(new Point3D(),new Normal(0,1,0),innerRadius,outerRadius);
        this.addObject(outer);
        this.addObject(inner);
        this.addObject(rim);
    }
    
}
