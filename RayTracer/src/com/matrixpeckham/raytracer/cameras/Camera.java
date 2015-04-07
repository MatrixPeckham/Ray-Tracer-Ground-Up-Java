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

import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Vector3D;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public abstract class Camera {
    protected Point3D eye;
    protected Point3D lookat;
    protected double ra;
    protected Vector3D u;
    protected Vector3D v;
    protected Vector3D w;
    protected Vector3D up;
    protected double exposureTime=1;
    
    public Camera(){
        eye = new Point3D(0,0,500);
        lookat = new Point3D(0);
        ra=0;
        up=new Vector3D(0,1,0);
        u=new Vector3D(1,0,0);
        v=new Vector3D(0,1,0);
        w=new Vector3D(0,0,1);
        exposureTime=1;
    }
    
    public Camera(Camera c){
        eye=new Point3D(c.eye);
        lookat=new Point3D(c.lookat);
        ra=c.ra;
        up=new Vector3D(c.up);
        u=new Vector3D(c.u);
        v=new Vector3D(c.v);
        w=new Vector3D(c.w);
        exposureTime=c.exposureTime;
    }
    
    public void computeUVW(){
        w.setTo(eye.sub(lookat));
        w.normalize();
        u=up.cross(w);
        u.normalize();
        v=w.cross(u);
        
        if(eye.x==lookat.x&&eye.z==lookat.z&&eye.y>lookat.y){
            u.setTo(new Vector3D(0,0,1));
            v.setTo(new Vector3D(1,0,0));
            w.setTo(new Vector3D(0,1,0));
        }
        if(eye.x==lookat.x&&eye.z==lookat.z&&eye.y<lookat.y){
            u.setTo(new Vector3D(1,0,0));
            v.setTo(new Vector3D(0,0,1));
            w.setTo(new Vector3D(0,-1,0));
        }
    }
    
    public void setEye(Point3D p){
        eye.setTo(p);
    }
    public void setEye(double x, double y, double z){
        eye.x=x;
        eye.y=y;
        eye.z=z;
    }
    
    public void setLookat(Point3D p){
        lookat.setTo(p);
    }
    public void setLookat(double x, double y, double z){
        lookat.x=x;
        lookat.y=y;
        lookat.z=z;
    }
    
    public void setUp(Vector3D p){
        up.setTo(p);
    }
    public void setUp(double x, double y, double z){
        up.x=x;
        up.y=y;
        up.z=z;
    }
    
    public void setRoll(double r){
        ra=r;
    }
    
    public void setExposureTime(double exp){
        exposureTime=exp;
    }
    
    public abstract void renderScene(World w);
    
    public abstract Camera clone();
    
    
    
}
