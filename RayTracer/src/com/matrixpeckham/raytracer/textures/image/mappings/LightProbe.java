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

/**
 *
 * @author William Matrix Peckham
 */
public class LightProbe implements Mapping {
    boolean lightProbe=true;
    public LightProbe(){
        
    }
    public LightProbe(boolean lightProbe){
        this.lightProbe=lightProbe;
    }
    public void makePanoramic(){
        lightProbe=false;
    }
    public void setLightprobe(boolean b){
        lightProbe=b;
    }
    @Override
    public Mapping clone() {
        return new LightProbe(lightProbe);
    }

    @Override
    public TexelCoord getTexelCoordinate(Point3D localHitPoint, int xRes, int yRes) {
        double x = localHitPoint.x;
        double y = localHitPoint.y;
        double z = localHitPoint.z;

        double d = Math.sqrt(x * x + y * y);
        double sinBeta = y / d;
        double cosBeta = x / d;
        double alpha;

        if (lightProbe) // the default
        {
            alpha = Math.acos(z);
        } else {
            alpha = Math.acos(-z);
        }

        double r = alpha * Utility.INV_PI;
        double u = (1.0 + r * cosBeta) * 0.5;
        double v = (1.0 + r * sinBeta) * 0.5;
        int column = (int) ((xRes - 1) * u);
        int row = (int) ((yRes - 1) * v);
        return new TexelCoord(row, column);
    }

}
