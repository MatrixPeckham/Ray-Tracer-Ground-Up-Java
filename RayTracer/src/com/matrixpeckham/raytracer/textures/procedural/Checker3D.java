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
package com.matrixpeckham.raytracer.textures.procedural;

import com.matrixpeckham.raytracer.textures.Texture;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;

/**
 *
 * @author William Matrix Peckham
 */
public class Checker3D implements Texture{

    private double size=1;
    private RGBColor color1 = new RGBColor();
    private RGBColor color2 = new RGBColor(1);
    
    public Checker3D(){};
    public Checker3D(Checker3D c){
        color1.setTo(c.color1);
        color2.setTo(c.color2);
        size=c.size;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public RGBColor getColor1() {
        return color1;
    }

    public void setColor1(RGBColor color1) {
        this.color1 .setTo( color1 );
    }

    public RGBColor getColor2() {
        return color2;
    }

    public void setColor2(RGBColor color2) {
        this.color2 .setTo( color2 );
    }
    
    
    
    @Override
    public Texture clone() {
        return new Checker3D(this);
    }

    @Override
    public RGBColor getColor(ShadeRec sr) {
        double eps = -0.000187453738;
        double x = sr.localHitPosition.x+eps;
        double y = sr.localHitPosition.y+eps;
        double z = sr.localHitPosition.z+eps;
        if(((int)Math.floor(x/size)+(int)Math.floor(y/size)+(int)Math.floor(z/size))%2==0){
            return color1;
        }
        return color2;
    }

    public void setColor1(double d, double d0, double d1) {
        color1.setTo(d, d0, d1);
    }

    public void setColor2(double d, double d0, double d1) {
        color2.setTo(d, d0, d1);
    }

    public void setColor1(double d) {
        color1.setTo(d);
    }

    public void setColor2(double d) {
        color2.setTo(d);
    }
    
}
