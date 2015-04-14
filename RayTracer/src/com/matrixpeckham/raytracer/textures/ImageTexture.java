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
package com.matrixpeckham.raytracer.textures;

import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;

/**
 *
 * @author William Matrix Peckham
 */
public class ImageTexture implements Texture{
    private int hRes = 100;
    private int vRes = 100;
    private Image image = null;
    private Mapping mapping = null;
    
    public ImageTexture(){
    }
    
    public ImageTexture(Image i){
        hRes=image.getHres();
        vRes=image.getVres();
        image=i;
    }
    public ImageTexture(ImageTexture i){
        hRes=i.hRes;
        vRes=i.vRes;
        if(i.image!=null){
            image=i.image.clone();
        }
        if(i.mapping!=null){
            mapping=i.mapping.clone();
        }
    }
    
    public void setImage(Image i){
        image = i;
        hRes=image.getHres();
        vRes=image.getVres();
    }
    
    public void setMapping(Mapping map){
        mapping=map;
    }

    @Override
    public Texture clone() {
        return new ImageTexture(this);
    }

    @Override
    public RGBColor getColor(ShadeRec sr) {
        TexelCoord point = new TexelCoord();
        if(mapping!=null){
            point.setTo(mapping.getTexelCoordinate(sr.localHitPosition, hRes, vRes));
        } else {
            point.row=(int)(sr.v*(vRes-1));
            point.row=(int)(sr.u*(hRes-1));
        }
        return image.getColor(point.row, point.col);
    }
}
