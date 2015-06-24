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
package com.matrixpeckham.raytracer.cameras;

import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.ViewPlane;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class StereoCamera extends Camera{

    public static enum ViewingType{
        PARALLEL,
        TRANSVERSE
    }
    ViewingType viewingType = ViewingType.PARALLEL;
    int pixelGap = 4;
    double beta = 30;
    Camera leftCamera;
    Camera rightCamera;
    ViewPlane eachVP;
    
    public void setupCameras(){
        double r = eye.distance(lookat);
        double x = r*Math.tan(0.5*beta*Utility.PI_ON_180);
        leftCamera.setEye(eye.sub(u.mul(x)));
        leftCamera.setLookat(lookat.sub(u.mul(x)));
        leftCamera.computeUVW();
        rightCamera.setEye(eye.add(u.mul(x)));
        rightCamera.setLookat(lookat.add(u.mul(x)));
        rightCamera.computeUVW();
        
    }
    
    @Override
    public void renderScene(World w) {
        ViewPlane vp = w.vp;
        
    }

    @Override
    public Camera clone() {
        return new StereoCamera(this);
    }
    
}
