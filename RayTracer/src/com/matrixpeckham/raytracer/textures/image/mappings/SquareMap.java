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
package com.matrixpeckham.raytracer.textures.image.mappings;

import com.matrixpeckham.raytracer.textures.image.Mapping;
import com.matrixpeckham.raytracer.textures.image.TexelCoord;
import com.matrixpeckham.raytracer.util.Point3D;
import java.util.logging.Logger;

/**
 * Mapping for generic rectangle to full image
 *
 * @author William Matrix Peckham
 */
public class SquareMap implements Mapping {

    /**
     * clone
     *
     * @return
     */
    @Override
    public Mapping cloneMapping() {
        return new SquareMap();
    }

    /**
     * get texel coordinate
     *
     * @param hitPoint
     * @param xRes
     * @param yRes
     * @return
     */
    @Override
    public TexelCoord getTexelCoordinate(Point3D hitPoint, int xRes, int yRes) {
        //generic rectangle is from -1-1 normalize to 0-1
        TexelCoord p = new TexelCoord();
        double u = (hitPoint.z + 1) / 2;
        double v = (hitPoint.x + 1) / 2;

        //find texel based on normalized coordinates
        p.col = (int) ((xRes - 1) * u);
        p.row = (int) ((yRes - 1) * v);
        return p;
    }
    private static final Logger LOG
            = Logger.getLogger(SquareMap.class.getName());

}
