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
package com.matrixpeckham.raytracer.geometricobjects.beveledobjects;

import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.compound.Compound;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Disk;
import com.matrixpeckham.raytracer.geometricobjects.primitives.OpenCylinder;
import com.matrixpeckham.raytracer.geometricobjects.parametric.Torus;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;

/**
 *
 * @author William Matrix Peckham
 */
public class BeveledCylinder extends Compound{
    double bottom,top,radius,bevelRadius;
    public BeveledCylinder(double bottom, double top, double radius, double bevelRadius){
        super();
        this.bottom=bottom;
        this.top=top;
        this.radius=radius;
        this.bevelRadius=bevelRadius;
        addObject(new Disk(new Point3D(0,bottom,0), new Normal(0,-1,0), radius-bevelRadius));
        addObject(new Disk(new Point3D(0,top,0), new Normal(0,1,0), radius-bevelRadius));
        addObject(new OpenCylinder(bottom+bevelRadius, top-bevelRadius, radius));
        
        Instance botTorus = new Instance(new Torus(radius-bevelRadius, bevelRadius));
        botTorus.translate(0,bottom+bevelRadius,0);
        botTorus.setTransformTexture(false);
        addObject(botTorus);

        Instance topTorus = new Instance(new Torus(radius-bevelRadius, bevelRadius));
        topTorus.translate(0,top-bevelRadius,0);
        topTorus.setTransformTexture(false);
        addObject(topTorus);
    }
}
