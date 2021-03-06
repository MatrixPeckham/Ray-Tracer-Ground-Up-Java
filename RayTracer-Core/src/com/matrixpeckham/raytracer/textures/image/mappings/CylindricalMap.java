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
package com.matrixpeckham.raytracer.textures.image.mappings;

import com.matrixpeckham.raytracer.textures.image.Mapping;
import com.matrixpeckham.raytracer.textures.image.TexelCoord;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import java.util.logging.Logger;

/**
 * Mapping for generic cylinder
 *
 * @author William Matrix Peckham
 */
public class CylindricalMap implements Mapping {

    /**
     * clone
     *
     * @return
     */
    @Override
    public Mapping cloneMapping() {
        return new CylindricalMap();
    }

    /**
     * get texel
     *
     * @param hitPoint
     * @param xRes
     * @param yRes
     * @return
     */
    @Override
    public TexelCoord getTexelCoordinate(Point3D hitPoint, int xRes, int yRes) {
        TexelCoord p = new TexelCoord();

        //compute angle around cylinder
        double phi = Math.atan2(hitPoint.x, hitPoint.z);
        if (phi < 0) {
            phi += Utility.TWO_PI;
        }

        //normalize angle
        double u = phi * Utility.INV_2_PI;

        //generic cylinder goes from -1-1 normalize y coordinte
        double v = (hitPoint.y + 1) / 2;//1.0 - theta*Utility.INV_PI;

        //get texel
        p.col = (int) ((xRes - 1) * u);
        p.row = (int) ((yRes - 1) * v);
        return p;
    }
    private static final Logger LOG
            = Logger.getLogger(CylindricalMap.class.getName());

}
