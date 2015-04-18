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

/**
 *
 * @author William Matrix Peckham
 */
public class PlaneChecker implements Texture {
    private double size=1;
    private double outlineWidth=0;
    private RGBColor color1=new RGBColor();
    private RGBColor color2=new RGBColor(1);
    private RGBColor outlineColor = new RGBColor(0.1, 0.1, 0.5);

    public void setSize(double size) {
        this.size = size;
    }

    public void setOutlineWidth(double outlineWidth) {
        this.outlineWidth = outlineWidth;
    }

    public void setColor1(RGBColor color1) {
        this.color1 .setTo( color1);
    }

    public void setColor2(RGBColor color2) {
        this.color2 .setTo( color2);
    }

    public void setOutlineColor(RGBColor outlineColor) {
        this.outlineColor .setTo( outlineColor);
    }

    public PlaneChecker(double size, double outlineWidth, RGBColor color1,
            RGBColor color2, RGBColor outlineColor) {
        this.size = size;
        this.outlineWidth = outlineWidth;
        this.color1 .setTo( color1 );
        this.color2 .setTo( color2 );
        this.outlineColor .setTo( outlineColor );
    }
    public PlaneChecker(PlaneChecker c) {
        this.size = c.size;
        this.outlineWidth = c.outlineWidth;
        this.color1 .setTo( c.color1 );
        this.color2 .setTo( c.color2 );
        this.outlineColor .setTo( outlineColor );
    }

    public PlaneChecker() {
    }
    

    
    
    @Override
    public Texture clone() {
        return new PlaneChecker(this);
    }

    @Override
    public RGBColor getColor(ShadeRec sr) {
        double x = sr.localHitPosition.x;
        double z = sr.localHitPosition.z;
        int ix = (int)Math.floor(x/size);
        int iz = (int)Math.floor(z/size);
        double fx = x/size - ix;
        double fz = z/size - iz;
        double width = 0.5 * outlineWidth / size;
        boolean inOutline = (fx<width||fx>1.0-width)||(fz<width||fz>1.0-width);
        if((ix+iz)%2==0){
            if(!inOutline){
                return color1;
            }
        } else {
            if(!inOutline){
                return color2;
            }
        }
        return outlineColor;
    }
    
}
