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
import com.matrixpeckham.raytracer.util.Utility;
import java.util.logging.Logger;

/**
 * Texture that does not actually vary spatially. Useful for debugging and for
 * use as an interior texture for nested textures with more than one inner
 * texture.
 *
 * @author William Matrix Peckham
 */
public class ConstantColor implements Texture {

    /**
     * color
     */
    private final RGBColor col = new RGBColor();

    /**
     * white default
     */
    public ConstantColor() {
        this(Utility.WHITE);
    }

    /**
     * initialize to color
     *
     * @param color
     */
    public ConstantColor(RGBColor color) {
        col.setTo(color);
    }

    /**
     * sets the color
     *
     * @param col
     */
    public void setColor(RGBColor col) {
        this.col.setTo(col);
    }

    /**
     * getter for color
     *
     * @return
     */
    public RGBColor getColor() {
        return col;
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Texture cloneTexture() {
        return new ConstantColor(col);
    }

    /**
     * sample texture, return color
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor getColor(ShadeRec sr) {
        return col;
    }

    private static final Logger LOG
            = Logger.getLogger(ConstantColor.class.getName());

}
