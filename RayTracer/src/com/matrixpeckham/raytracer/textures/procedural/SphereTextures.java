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

import com.matrixpeckham.raytracer.textures.ConstantColor;
import com.matrixpeckham.raytracer.textures.Texture;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import java.util.logging.Logger;

/**
 * This is the sphere checkers that has textures as its checkers.
 *
 * @author William Matrix Peckham
 */
public class SphereTextures implements Texture {

    /**
     * horizontal checkers per full sphere
     */
    private int numHorizontalCheckers = 20;

    /**
     * vertical checkers per full sphere
     */
    private int numVerticalCheckers = 10;

    /**
     * line width in radians
     */
    private double horizontalLineWidth = 0;

    /**
     * line width in radians
     */
    private double verticalLineWidth = 0;

    /**
     * first texture
     */
    private Texture color1;

    /**
     * second texture
     */
    private Texture color2;

    /**
     * third texture
     */
    private Texture lineColor;

    /**
     * default constructor
     */
    public SphereTextures() {
        color1 = new ConstantColor();
        color2 = new ConstantColor();
        lineColor = new ConstantColor();
    }

    /**
     * copy constructor
     *
     * @param c
     */
    public SphereTextures(SphereTextures c) {
        numHorizontalCheckers = c.numHorizontalCheckers;
        numVerticalCheckers = c.numVerticalCheckers;
        horizontalLineWidth = c.horizontalLineWidth;
        verticalLineWidth = c.verticalLineWidth;
        if (c.color1 != null) {
            color1 = c.color1.cloneTexture();
        }
        if (c.color2 != null) {
            color2 = c.color2.cloneTexture();
        }
        if (c.lineColor != null) {
            lineColor = c.lineColor.cloneTexture();
        }
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
    public void setTexture1(Texture color1) {
        this.color1 = color1.cloneTexture();
    }

    /**
     * setter
     *
     * @param color2
     */
    public void setTexture2(Texture color2) {
        this.color2 = color2.cloneTexture();
    }

    /**
     * setter
     *
     * @param lineColor
     */
    public void setLineColor(Texture lineColor) {
        this.lineColor = lineColor.cloneTexture();
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Texture cloneTexture() {
        return new SphereTextures(this);
    }

    /**
     * setter
     *
     * @param d
     */
    public void setLineWidth(double d) {
        setHorizontalLineWidth(d);
        setVerticalLineWidth(d);
    }

    /**
     * setter
     *
     * @param i
     */
    public void setNumlat(int i) {
        setNumHorizontalCheckers(i);
    }

    /**
     * setter
     *
     * @param i
     */
    public void setNumlong(int i) {
        setNumVerticalCheckers(i);
    }

    /**
     * setter
     *
     * @param i
     */
    public void setNumHorizontal(int i) {
        setNumHorizontalCheckers(i);
    }

    /**
     * setter
     *
     * @param i
     */
    public void setNumVertical(int i) {
        setNumVerticalCheckers(i);
    }

    /**
     * setter
     *
     * @param ref
     */
    public void setChecker1Texture(Texture ref) {
        setTexture1(ref);
    }

    /**
     * setter
     *
     * @param ref
     */
    public void setChecker2Texture(Texture ref) {
        setTexture1(ref);
    }

    /**
     * setter
     *
     * @param ref
     */
    public void setLineTexture(Texture ref) {
        setLineColor(ref);
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
        double y = sr.localHitPosition.y;
        double z = sr.localHitPosition.z;

        //double len = Math.sqrt(x*x+y*y+z*z);
        //x/=len;
        //y/=len;
        //z/=len;
        //calculate the angles of the hit point
        double theta = Math.acos(y);
        double phi = Math.atan2(x, z);
        if (phi < 0) {
            phi += Utility.TWO_PI;
        }

        //calculate the thresholds
        double phiSize = Utility.TWO_PI / numHorizontalCheckers;
        double thetaSize = Utility.PI / numVerticalCheckers;

        //do the checker calculations
        int iphi = (int) Math.floor(phi / phiSize);
        int itheta = (int) Math.floor(theta / thetaSize);

        double fphi = phi / phiSize - iphi;
        double ftheta = theta / thetaSize - itheta;

        double phiLineWidth = 0.5 * verticalLineWidth;
        double thetaLineWidth = 0.5 * horizontalLineWidth;

        //are we in the outline
        boolean inOutline = (fphi < phiLineWidth || fphi > 1.0 - phiLineWidth)
                || (ftheta < thetaLineWidth || ftheta > 1.0 - thetaLineWidth);

        //do checkers and delegate to textures
        if (!((iphi + itheta) % 2 == 0)) {
            if (!inOutline) {
                return color1.getColor(sr);
            }
        } else {
            if (!inOutline) {
                return color2.getColor(sr);
            }
        }
        return lineColor.getColor(sr);
    }

    private static final Logger LOG
            = Logger.getLogger(SphereTextures.class.getName());

}
