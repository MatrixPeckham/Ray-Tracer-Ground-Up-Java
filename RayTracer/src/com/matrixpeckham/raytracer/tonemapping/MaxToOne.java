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
package com.matrixpeckham.raytracer.tonemapping;

import com.matrixpeckham.raytracer.util.RGBColor;

/**
 * Normalizes color based on the largest component.
 *
 * @author William Matrix Peckham
 */
public class MaxToOne implements ToneMapper {

    @Override
    public RGBColor map(RGBColor c) {
        double maxVal = Math.max(c.r, Math.max(c.g, c.b));
        if (maxVal > 1) {
            return c.div(maxVal);
        }
        return c;
    }

}
