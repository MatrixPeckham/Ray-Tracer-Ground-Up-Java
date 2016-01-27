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
package com.matrixpeckham.raytracer.textures.image;

import com.matrixpeckham.raytracer.textures.Texture;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;

/**
 * Image based texture.
 *
 * @author William Matrix Peckham
 */
public class ImageTexture implements Texture {

    /**
     * horizontal size of image (default 100)
     */
    private int hRes = 100;

    /**
     * vertical size of image (default 100)
     */
    private int vRes = 100;

    /**
     * image to use
     */
    private Image image = null;

    /**
     * mapping to use, this can be null.
     */
    private Mapping mapping = null;

    /**
     * default constructor.
     */
    public ImageTexture() {
    }

    /**
     * initializes this texture with the image provided.
     *
     * @param i
     */
    public ImageTexture(Image i) {
        image = i;
        hRes = image.getHres();
        vRes = image.getVres();
    }

    /**
     * copy constructor
     *
     * @param i
     */
    public ImageTexture(ImageTexture i) {
        //copy image resolution
        hRes = i.hRes;
        vRes = i.vRes;

        //clone image and mapping if they aren't null
        if (i.image != null) {
            image = i.image;
        }

        if (i.mapping != null) {
            mapping = i.mapping.clone();
        }
    }

    /**
     * sets the image and resolution
     *
     * @param i
     */
    public void setImage(Image i) {
        image = i;
        hRes = image.getHres();
        vRes = image.getVres();
    }

    /**
     * sets the mapping for this texture
     *
     * @param map
     */
    public void setMapping(Mapping map) {
        mapping = map;
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Texture clone() {
        return new ImageTexture(this);
    }

    /**
     * sample texture
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor getColor(ShadeRec sr) {
        TexelCoord point = new TexelCoord();

        //if a mapping exists we get the texel coordinate from the mapping
        if (mapping != null) {
            point.setTo(mapping.getTexelCoordinate(sr.localHitPosition, hRes,
                    vRes));
        } else {
            //otherwise we use the u, v coordinates in the shade rec.
            point.row = (int) (sr.v * (vRes - 1));
            point.col = (int) (sr.u * (hRes - 1));
        }
        return image.getColor(point.row, point.col);
    }
}
