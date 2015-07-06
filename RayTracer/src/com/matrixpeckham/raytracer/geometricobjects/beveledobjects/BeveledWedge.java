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
import com.matrixpeckham.raytracer.geometricobjects.compound.*;
import com.matrixpeckham.raytracer.geometricobjects.parametric.Torus;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConcavePartCylinder;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConvexPartCylinder;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.PartRing;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.PartTorus;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Disk;
import com.matrixpeckham.raytracer.geometricobjects.primitives.OpenCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Rectangle;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Ring;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
import javafx.scene.shape.Cylinder;

/**
 *
 * @author William Matrix Peckham
 */
public class BeveledWedge extends Compound {

    public BeveledWedge() {
        this(-1, 1, 0.5, 1, 0.1, 0, 30);
    }

    public BeveledWedge(double bottom, double top, double innerRadius,
            double outerRadius, double bevelRadius) {
        this(bottom, top, innerRadius, outerRadius, bevelRadius, 0, 30);
    }

    public BeveledWedge(double bottom, double top, double innerRadius,
            double outerRadius, double bevelRadius, double phiMin, double phiMax) {
        OpenCylinder vertical = new OpenCylinder(bottom + bevelRadius, top
                - bevelRadius, bevelRadius);

        Instance inLowVert = new Instance(vertical);
        inLowVert.translate(0, 0, innerRadius + bevelRadius);
        inLowVert.rotateY(phiMin);
        addObject(inLowVert);
        Instance inHighVert = new Instance(vertical);
        inHighVert.translate(0, 0, innerRadius + bevelRadius);
        inHighVert.rotateY(phiMax);
        addObject(inHighVert);
        Instance outLowVert = new Instance(vertical);
        outLowVert.translate(0, 0, outerRadius - bevelRadius);
        outLowVert.rotateY(phiMin);
        addObject(outLowVert);
        Instance outHighVert = new Instance(vertical);
        outHighVert.translate(0, 0, outerRadius - bevelRadius);
        outHighVert.rotateY(phiMax);
        addObject(outHighVert);

        OpenCylinder hor = new OpenCylinder(innerRadius + bevelRadius,
                outerRadius - bevelRadius, bevelRadius);

        Instance downLowHor = new Instance(hor);
        downLowHor.rotateX(90);
        downLowHor.translate(0, bottom + bevelRadius, 0);
        downLowHor.rotateY(phiMin);
        addObject(downLowHor);
        Instance upLowHor = new Instance(hor);
        upLowHor.rotateX(90);
        upLowHor.translate(0, top - bevelRadius, 0);
        upLowHor.rotateY(phiMin);
        addObject(upLowHor);
        Instance downHighHor = new Instance(hor);
        downHighHor.rotateX(90);
        downHighHor.translate(0, bottom + bevelRadius, 0);
        downHighHor.rotateY(phiMax);
        addObject(downHighHor);
        Instance upHighHor = new Instance(hor);
        upHighHor.rotateX(90);
        upHighHor.translate(0, top - bevelRadius, 0);
        upHighHor.rotateY(phiMax);
        addObject(upHighHor);

        Sphere corner = new Sphere(new Point3D(0, 0, 0), bevelRadius);

        Instance lowInBot = new Instance(corner);
        lowInBot.translate(0, bottom + bevelRadius, innerRadius + bevelRadius);
        lowInBot.rotateY(phiMin);
        addObject(lowInBot);
        Instance lowOutBot = new Instance(corner);
        lowOutBot.translate(0, bottom + bevelRadius, outerRadius - bevelRadius);
        lowOutBot.rotateY(phiMin);
        addObject(lowOutBot);
        Instance lowInTop = new Instance(corner);
        lowInTop.translate(0, top - bevelRadius, innerRadius + bevelRadius);
        lowInTop.rotateY(phiMin);
        addObject(lowInTop);
        Instance lowOutTop = new Instance(corner);
        lowOutTop.translate(0, top - bevelRadius, outerRadius - bevelRadius);
        lowOutTop.rotateY(phiMin);
        addObject(lowOutTop);
        Instance highInBot = new Instance(corner);
        highInBot.translate(0, bottom + bevelRadius, innerRadius + bevelRadius);
        highInBot.rotateY(phiMax);
        addObject(highInBot);
        Instance highOutBot = new Instance(corner);
        highOutBot.translate(0, bottom + bevelRadius, outerRadius - bevelRadius);
        highOutBot.rotateY(phiMax);
        addObject(highOutBot);
        Instance highInTop = new Instance(corner);
        highInTop.translate(0, top - bevelRadius, innerRadius + bevelRadius);
        highInTop.rotateY(phiMax);
        addObject(highInTop);
        Instance highOutTop = new Instance(corner);
        highOutTop.translate(0, top - bevelRadius, outerRadius - bevelRadius);
        highOutTop.rotateY(phiMax);
        addObject(highOutTop);

        Rectangle rect1 = new Rectangle(new Point3D(0, bottom + bevelRadius,
                innerRadius + bevelRadius), new Vector3D(0, top - bottom - 2
                        * bevelRadius, 0), new Vector3D(0, 0, outerRadius
                        - innerRadius - 2 * bevelRadius), new Normal(-1, 0, 0));
        Rectangle rect2 = new Rectangle(new Point3D(0, bottom + bevelRadius,
                innerRadius + bevelRadius), new Vector3D(0, top - bottom - 2
                        * bevelRadius, 0), new Vector3D(0, 0, outerRadius
                        - innerRadius - 2 * bevelRadius));

        Instance lowSide = new Instance(rect1);
        lowSide.translate(-bevelRadius, 0, 0);
        lowSide.rotateY(phiMin);
        addObject(lowSide);
        Instance highSide = new Instance(rect2);
        highSide.translate(bevelRadius, 0, 0);
        highSide.rotateY(phiMax);
        addObject(highSide);

        PartRing topd = new PartRing(new Point3D(0, top, 0), innerRadius
                + bevelRadius, outerRadius - bevelRadius, phiMin, phiMax);
        PartRing bottomd = new PartRing(new Point3D(0, bottom, 0), innerRadius
                + bevelRadius, outerRadius - bevelRadius, phiMin, phiMax);
        ConcavePartCylinder innercyl = new ConcavePartCylinder(bottom
                + bevelRadius, top - bevelRadius, innerRadius, phiMin, phiMax);
        ConvexPartCylinder outercyl = new ConvexPartCylinder(bottom
                + bevelRadius, top - bevelRadius, outerRadius, phiMin, phiMax);
        PartTorus inTor = new PartTorus(innerRadius + bevelRadius, bevelRadius,
                phiMin,
                phiMax, 0, 360);
        PartTorus outTor = new PartTorus(outerRadius - bevelRadius, bevelRadius,
                phiMin,
                phiMax, 0, 360);
        addObject(topd);
        addObject(bottomd);
        addObject(innercyl);
        addObject(outercyl);
        Instance inUp = new Instance(inTor);
        inUp.translate(0, top - bevelRadius, 0);
        addObject(inUp);
        Instance inDown = new Instance(inTor);
        inDown.translate(0, bottom + bevelRadius, 0);
        addObject(inDown);
        Instance outUp = new Instance(outTor);
        outUp.translate(0, top - bevelRadius, 0);
        addObject(outUp);
        Instance outDown = new Instance(outTor);
        outDown.translate(0, bottom + bevelRadius, 0);
        addObject(outDown);
    }

    public void setBottomMaterial(Matte mattePtr1) {
        objects.get(1).setMaterial(mattePtr1);
    }

    public void setTopMaterial(Matte mattePtr1) {
        objects.get(0).setMaterial(mattePtr1);
    }

    public void setWallMaterial(Matte mattePtr2) {
        objects.get(2).setMaterial(mattePtr2);
        objects.get(3).setMaterial(mattePtr2);
    }

    public void setInnerWallMaterial(Matte mattePtr2) {
        objects.get(2).setMaterial(mattePtr2);
    }

    public void setOuterWallMaterial(Matte mattePtr3) {
        objects.get(3).setMaterial(mattePtr3);
    }

}
