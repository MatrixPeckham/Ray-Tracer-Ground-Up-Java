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
 * Texture represents a spatially varying color.
 *
 * @author William Matrix Peckham
 */
public interface Texture {

    /**
     * Clone the texture.
     *
     * @return
     */
    public Texture cloneTexture();

    /**
     * return the color at the location represented by the shaderec.
     * Implementation of this will vary wildly.
     *
     * @param sr
     * @return
     */
    public RGBColor getColor(ShadeRec sr);
}
