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
package com.matrixpeckham.raytracer.lights;

import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 *
 * @author William Matrix Peckham
 */
public class JitteredDirectional extends Directional {
    double r = 1;
    public JitteredDirectional() {
        super();
        dir.normalize();
    }

    public JitteredDirectional(JitteredDirectional dl) {
        super(dl);
        dir.normalize();
        this.r=dl.r;
    }

    @Override
    public void setDirection(Vector3D d) {
        super.setDirection(d); //To change body of generated methods, choose Tools | Templates.
        dir.normalize();
    }

    @Override
    public void setDirection(double dx, double dy, double dz) {
        super.setDirection(dx, dy, dz); //To change body of generated methods, choose Tools | Templates.
        dir.normalize();
    }

    @Override
    public Vector3D getDirection(ShadeRec sr) {
        Vector3D ndir = new Vector3D();
        ndir.x=dir.x+r*(2*Utility.randDouble()-1);
        ndir.y=dir.y+r*(2*Utility.randDouble()-1);
        ndir.z=dir.z+r*(2*Utility.randDouble()-1);
        ndir.normalize();
        return ndir;
    }

    public void setJitterAmount(double d) {
        r=d;
    }


    
}
