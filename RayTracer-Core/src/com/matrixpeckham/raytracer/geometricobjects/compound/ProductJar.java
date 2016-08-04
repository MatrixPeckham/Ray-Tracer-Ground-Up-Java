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
package com.matrixpeckham.raytracer.geometricobjects.compound;

import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConvexPartCylinder;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConvexPartTorus;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Disk;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;

/**
 * The product jar is centered on the y axis , The radius of the cap is
 * bodyRadius - topBevelRadius , The texture image used here completely covers
 * the vertical curved surface , of the body. This has vertical extent from
 * bottom + bottomBevelRadius , to body top - bottomBevelRadius. , If you use
 * the bodyMattePtr defined above for the body, it will be rendered red. , See
 * extra image in the Chapter 29 download.
 *
 * @author William Matrix Peckham
 */
public class ProductJar extends Compound {

    /**
     * initializes jar
     *
     * @param bottom
     * @param bodyTop
     * @param capTop
     * @param bodyRadius
     * @param bottomBevelRadius
     * @param topBevelRadius
     * @param capBevelRadius
     */
    public ProductJar(double bottom, double bodyTop, double capTop,
            double bodyRadius, double bottomBevelRadius, double topBevelRadius,
            double capBevelRadius) {
        //bottom disc body radius - bevel radius
        Disk bottomDisc = new Disk(new Point3D(0, bottom, 0), new Normal(0, -1,
                -0), bodyRadius - bottomBevelRadius);
        addObject(bottomDisc);

        //bottom bevel torus
        ConvexPartTorus bottomBev = new ConvexPartTorus(bodyRadius
                - bottomBevelRadius, bottomBevelRadius, 0,
                360, 270, 360);
        Instance botBev = new Instance(bottomBev);
        botBev.translate(0, bottom + bottomBevelRadius, 0);
        addObject(botBev);

        //top bevel
        ConvexPartTorus topBevel = new ConvexPartTorus(bodyRadius
                - topBevelRadius, topBevelRadius, 0,
                360, 0, 90);
        Instance topBev = new Instance(topBevel);
        topBev.translate(0, bodyTop - topBevelRadius, 0);
        addObject(topBev);

        //lid wall
        ConvexPartCylinder cap = new ConvexPartCylinder(bodyTop, capTop
                - capBevelRadius,
                bodyRadius - topBevelRadius);
        addObject(cap);

        //cap top bevel
        ConvexPartTorus capBevel = new ConvexPartTorus(bodyRadius
                - topBevelRadius - capBevelRadius, capBevelRadius, 0,
                360, 0, 90);
        Instance capInst = new Instance(capBevel);
        capInst.translate(0, capTop - capBevelRadius, 0);
        addObject(capInst);

        //lid top disc
        Disk top = new Disk(new Point3D(0, capTop, 0), new Normal(0, 1, 0),
                bodyRadius - topBevelRadius - capBevelRadius);
        addObject(top);

        //body cylinder
        ConvexPartCylinder body = new ConvexPartCylinder(-1, 1,
                bodyRadius);
        //addObject(body);
        Instance bodyInst = new Instance(body);
        bodyInst.scale(1, 0.5, 1);
        bodyInst.translate(0, .5, 0);
        bodyInst.scale(1, (bodyTop - topBevelRadius) - (bottom
                + bottomBevelRadius), 1);
        bodyInst.translate(0, bottom + bottomBevelRadius, 0);
        addObject(bodyInst);

    }

    /**
     * sets the material for the outside of the bottle
     *
     * @param svMattePtr
     */
    public void setBodyMaterial(SV_Matte svMattePtr) {
        objects.get(6).setMaterial(svMattePtr);
    }

}
