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
import java.util.logging.Logger;

/**
 * Checker texture for planes.
 *
 * @author William Matrix Peckham
 */
public class PlaneChecker implements Texture {

    /**
     * size of checker
     */
    private double size = 1;

    /**
     * size of outline
     */
    private double outlineWidth = 0;

    /**
     * first color
     */
    private final RGBColor color1 = new RGBColor();

    /**
     * second color
     */
    private final RGBColor color2 = new RGBColor(1);

    /**
     * line color
     */
    private final RGBColor outlineColor = new RGBColor(0.1, 0.1, 0.5);

    /**
     * setter
     *
     * @param size
     */
    public void setSize(double size) {
        this.size = size;
    }

    /**
     * setter
     *
     * @param outlineWidth
     */
    public void setOutlineWidth(double outlineWidth) {
        this.outlineWidth = outlineWidth;
    }

    /**
     * setter
     *
     * @param color1
     */
    public void setColor1(RGBColor color1) {
        this.color1.setTo(color1);
    }

    /**
     * setter
     *
     * @param color2
     */
    public void setColor2(RGBColor color2) {
        this.color2.setTo(color2);
    }

    /**
     * setter
     *
     * @param outlineColor
     */
    public void setOutlineColor(RGBColor outlineColor) {
        this.outlineColor.setTo(outlineColor);
    }

    /**
     * Constructor for setting all the fields.
     *
     * @param size
     * @param outlineWidth
     * @param color1
     * @param color2
     * @param outlineColor
     */
    public PlaneChecker(double size, double outlineWidth, RGBColor color1,
            RGBColor color2, RGBColor outlineColor) {
        this.size = size;
        this.outlineWidth = outlineWidth;
        this.color1.setTo(color1);
        this.color2.setTo(color2);
        this.outlineColor.setTo(outlineColor);
    }

    /**
     * copy constructor
     *
     * @param c
     */
    public PlaneChecker(PlaneChecker c) {
        this.size = c.size;
        this.outlineWidth = c.outlineWidth;
        this.color1.setTo(c.color1);
        this.color2.setTo(c.color2);
        this.outlineColor.setTo(outlineColor);
    }

    /**
     * default constructor black and white checkers no outline
     */
    public PlaneChecker() {
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Texture cloneTexture() {
        return new PlaneChecker(this);
    }

    /**
     * sample texture
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor getColor(ShadeRec sr) {
        //hit point
        double x = sr.localHitPosition.x;
        double z = sr.localHitPosition.z;

        //calculate checkers
        int ix = (int) Math.floor(x / size);
        int iz = (int) Math.floor(z / size);

        //outline calculations
        double fx = x / size - ix;
        double fz = z / size - iz;
        double width = 0.5 * outlineWidth / size;

        //in outline
        boolean inOutline = (fx < width || fx > 1.0 - width) || (fz < width
                || fz > 1.0 - width);

        //return proper color
        if ((ix + iz) % 2 == 0) {
            if (!inOutline) {
                return color1;
            }
        } else {
            if (!inOutline) {
                return color2;
            }
        }
        return outlineColor;
    }

    /**
     * setter
     *
     * @param d
     * @param d0
     * @param d1
     */
    public void setColor1(double d, double d0, double d1) {
        color1.setTo(d, d0, d1);
    }

    private static final Logger LOG
            = Logger.getLogger(PlaneChecker.class.getName());

}
