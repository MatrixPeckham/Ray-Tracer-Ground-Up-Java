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
package com.matrixpeckham.raytracer.geometricobjects.parametric;

import com.matrixpeckham.raytracer.geometricobjects.compound.TriangleMesh;
import com.matrixpeckham.raytracer.geometricobjects.triangles.SmoothMeshTriangle;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Vector3D;
import java.util.ArrayList;

/**
 * 
 * @author William Matrix Peckham
 */
public class ParametricObject extends TriangleMesh {
    public interface ParametricEquation {
        public enum NormalType {
            REGULAR,REVERSE,TWO_SIDE
        }
        public double getMinU();
        public double getMaxU();
        public double getMinV();
        public double getMaxV();
        public double getUStep();
        public double getVStep();
        public Point3D getPointAt(double u, double v);
        public Normal getNormalAt(double u, double v);
        public boolean isClosedU();
        public boolean isClosedV();
        public NormalType getNormalType();
    }
    ParametricEquation.NormalType normType;
    public ParametricObject(ParametricEquation p){
        super();
        double uMin = p.getMinU();
        double uMax = p.getMaxU();
        double vMin = p.getMinV();
        double vMax = p.getMaxV();
        double uStep = p.getUStep();
        double vStep = p.getVStep();
        boolean closedU = p.isClosedU();
        boolean closedV = p.isClosedV();
        normType = p.getNormalType();
        int numU = 0;
        int numV = 0;
        double u;
        for(u = uMin; u<=uMax; u+=uStep){
            numV=0;
            double v;
            for(v=vMin; v<=vMax; v+=vStep){
                mesh.vertices.add(p.getPointAt(u, v));
                mesh.normals.add(p.getNormalAt(u, v));
                numV++;
            }
            if(v!=vMax && v>vMax){
                mesh.vertices.add(p.getPointAt(u, vMax));
                mesh.normals.add(p.getNormalAt(u, vMax));
                numV++;
            }
            numU++;
        }
        if(u!=uMax && u>uMax){
            double v;
            for(v=vMin; v<=vMax; v+=vStep){
                mesh.vertices.add(p.getPointAt(uMax, v));
                mesh.normals.add(p.getNormalAt(uMax, v));
                //numV++;
            }
            if(v!=vMax && v>vMax){
                mesh.vertices.add(p.getPointAt(uMax, vMax));
                mesh.normals.add(p.getNormalAt(uMax, vMax));
//                numV++;
            }
            numU++;
        }
        for(int i =0; i<numU-1; i++){
            for(int j=0; j<numV-1; j++){
                int bl = i*numV+j;
                int br = i*numV+j+1;
                int tl = (i+1)*numV+j;
                int tr = (i+1)*numV+j+1;
                SmoothMeshTriangle b = new SmoothMeshTriangle(mesh, bl, br, tl);
                objects.add(b);
                SmoothMeshTriangle t = new SmoothMeshTriangle(mesh, tl, br, tr);
                objects.add(t);
            }
        }
        if(closedV){
            for(int i = 0; i<numU-1; i++){
                int bl = i*numV+numV-1;
                int br = i*numV;
                int tl = (i+1)*numV+numV-1;
                int tr = (i+1)*numV;
                SmoothMeshTriangle b = new SmoothMeshTriangle(mesh, bl, br, tl);
                objects.add(b);
                SmoothMeshTriangle t = new SmoothMeshTriangle(mesh, tl, br, tr);
                objects.add(t);
            }
        }
        if(closedU){
            for(int j = 0; j<numU-1; j++){
                int bl = (numU-1)*numV+j;
                int br = (numU-1)*numV+j+1;
                int tl = j;
                int tr = j+1;
                SmoothMeshTriangle b = new SmoothMeshTriangle(mesh, bl, br, tl);
                objects.add(b);
                SmoothMeshTriangle t = new SmoothMeshTriangle(mesh, tl, br, tr);
                objects.add(t);
            }
        }
        setupCells();
    }

    @Override
    public boolean hit(Ray ray, ShadeRec sr) {
        boolean hit = super.hit(ray, sr);
        if(hit){
            switch(normType){
                case REGULAR:
                    break;
                case REVERSE:
                    sr.normal.setTo(sr.normal.neg());
                    break;
                case TWO_SIDE:
                    if (ray.d.neg().dot(new Vector3D(sr.normal)) < 0.0) {
                        sr.normal .setTo( sr.normal.neg());
                    }
            }
        }
        return hit;
    }
    
    
    
}
