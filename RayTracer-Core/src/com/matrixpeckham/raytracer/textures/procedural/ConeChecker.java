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
import java.util.logging.Logger;

/**
 * Cone checker texture for texturing generic cones.
 *
 * @author William Matrix Peckham
 */
public class ConeChecker implements Texture {

    /**
     * horizontal checkers for whole round cone
     */
    private int numHorizontalCheckers = 20;

    /**
     * number vertical checkers
     */
    private int numVerticalCheckers = 10;

    /**
     * horizontal line width
     */
    private double horizontalLineWidth = 0;

    /**
     * vertical line width
     */
    private double verticalLineWidth = 0;

    /**
     * first color
     */
    private final RGBColor color1 = new RGBColor(1);

    /**
     * second color
     */
    private final RGBColor color2 = new RGBColor(0.5);

    /**
     * line color
     */
    private final RGBColor lineColor = new RGBColor(0);

    /**
     * default constructor, gray and white checkers with no lines
     */
    public ConeChecker() {
    }

    /**
     * copy constructor
     *
     * @param c
     */
    public ConeChecker(ConeChecker c) {
        numHorizontalCheckers = c.numHorizontalCheckers;
        numVerticalCheckers = c.numVerticalCheckers;
        horizontalLineWidth = c.horizontalLineWidth;
        verticalLineWidth = c.verticalLineWidth;
        color1.setTo(c.color1);
        color2.setTo(c.color2);
        lineColor.setTo(c.lineColor);
    }

    /**
     * setter
     *
     * @param numHorizontalCheckers
     */
    public void setNumHorizontalCheckers(int numHorizontalCheckers) {
        this.numHorizontalCheckers = numHorizontalCheckers;
    }

    /**
     * setter
     *
     * @param numVerticalCheckers
     */
    public void setNumVerticalCheckers(int numVerticalCheckers) {
        this.numVerticalCheckers = numVerticalCheckers;
    }

    /**
     * setter
     *
     * @param horizontalLineWidth
     */
    public void setHorizontalLineWidth(double horizontalLineWidth) {
        this.horizontalLineWidth = horizontalLineWidth;
    }

    /**
     * setter
     *
     * @param verticalLineWidth
     */
    public void setVerticalLineWidth(double verticalLineWidth) {
        this.verticalLineWidth = verticalLineWidth;
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
     * @param lineColor
     */
    public void setLineColor(RGBColor lineColor) {
        this.lineColor.setTo(lineColor);
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Texture cloneTexture() {
        return new ConeChecker(this);
    }

    /**
     * sample texture.
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor getColor(ShadeRec sr) {
        //hit point
        double x = sr.localHitPosition.x;
        double y = sr.localHitPosition.y;
        double z = sr.localHitPosition.z;

        //double len = Math.sqrt(x*x+y*y+z*z);
        //x/=len;
        //y/=len;
        //z/=len;
        //calculate angle
        double phi = Math.atan2(x, z);
        if (phi < 0) {
            phi += Utility.TWO_PI;
        }

        //calculate checkers
        double phiSize = Utility.TWO_PI / numHorizontalCheckers;

        double ySize = 2.0 / numVerticalCheckers;

        //calculate lines
        int iphi = (int) Math.floor(phi / phiSize);
        int iy = (int) Math.floor(y / ySize);

        double fphi = phi / phiSize - iphi;
        double fy = y / ySize - iy;

        double phiLineWidth = 0.5 * verticalLineWidth;
        double yLineWidth = 0.5 * horizontalLineWidth;

        //in lines
        boolean inOutline = (fphi < phiLineWidth || fphi > 1.0 - phiLineWidth)
                || (fy < yLineWidth || fy > 1.0 - yLineWidth);

        //return color
        if ((iphi + iy) % 2 == 0) {
            if (!inOutline) {
                return color1;
            }
        } else {
            if (!inOutline) {
                return color2;
            }
        }
        return lineColor;
    }

    private static final Logger LOG
            = Logger.getLogger(ConeChecker.class.getName());

}
