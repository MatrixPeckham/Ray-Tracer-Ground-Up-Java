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
package com.matrixpeckham.raytracer.geometricobjects.math;

import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.compound.Compound;
import com.matrixpeckham.raytracer.geometricobjects.primitives.OpenCone;
import com.matrixpeckham.raytracer.geometricobjects.primitives.OpenCylinder;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.procedural.Checker3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 *
 * @author William Matrix Peckham
 */
public class RayPrimitive extends Compound {

    public static final double radius = 0.01;

    public RayPrimitive(Vector3D vec) {
        this(vec, new Vector3D(0, 0, 0));
    }

    public RayPrimitive(Vector3D vec, Vector3D origin) {
        this(vec, origin, new RGBColor(0.5, 0.5, 0.5));
    }

    public RayPrimitive(Vector3D vec, Vector3D origin, RGBColor color1) {
        this(vec, origin, color1, new RGBColor(0, 0, 0));
    }

    public RayPrimitive(Vector3D vec, Vector3D origin, RGBColor color1,
            RGBColor color2) {
        double length = vec.length();
        vec.normalize();
        Checker3D check = new Checker3D();
        check.setColor1(color1);
        check.setColor2(color2);
        check.setSize(1);
        SV_Matte matte = new SV_Matte();
        matte.setCd(check);
        matte.setKa(1);

        OpenCylinder shaft = new OpenCylinder(0, 0.9, radius);
        shaft.setMaterial(matte);
        OpenCone cone = new OpenCone();
        cone.setR(radius * 2);
        cone.setH(0.1);
        cone.setMaterial(matte);

        Instance coneInst = new Instance();
        coneInst.setObject(cone);
        coneInst.translate(0, 0.9, 0);
        coneInst.setTransformTexture(false);

        Compound arrow = new Compound();
        arrow.addObject(shaft);
        arrow.addObject(coneInst);
        double x = vec.x;
        double y = vec.y;
        double z = vec.z;
        double thetaXZ = Math.atan2(z, x) / Utility.PI_ON_180;
        double phiY = Math.atan2(Math.sqrt(x * x + z * z), y)
                / Utility.PI_ON_180;

        Instance inst = new Instance();
        inst.setObject(arrow);
        inst.scale(1, length, 1);
        inst.rotateZ(-phiY);
        inst.rotateY(-thetaXZ);
        inst.translate(origin);
        inst.setTransformTexture(false);
        this.addObject(inst);

    }
}
