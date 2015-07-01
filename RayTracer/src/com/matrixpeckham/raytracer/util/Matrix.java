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
package com.matrixpeckham.raytracer.util;

/**
 * class to represent a 4x4 matrix
 * @author William Matrix Peckham
 */
public class Matrix {
    public double[][] m = new double[4][4];
    public Matrix(){
        setIdentity();
    }
    
    public Matrix(Matrix m){
        this();
        setTo(m);
    }
    
    public Matrix setTo(Matrix o){
        if(this.equals(o))
            return this;
        for(int x=0;x<4;x++)
            for(int y=0;y<4;y++)
                m[x][y]=o.m[x][y];
        return this;
    }
    
    public Matrix mul(Matrix m){
        Matrix ret = new Matrix();
        
        for(int y=0; y<4; y++){
            for(int x = 0; x<4; x++){
                double sum = 0;
                for(int j=0; j<4; j++){
                    sum+=this.m[x][j] * m.m[j][y];
                }
                ret.m[x][y]=sum;
            }
        }
        
        return ret;
    }
    
    public Matrix div(double d){
        for(int x=0;x<4;x++){
            for(int y = 0; y<4; y++){
                m[x][y]=m[x][y]/d;
            }
        }
        return this;
    }
    
    public void setIdentity(){
        for(int x = 0; x<4; x++){
            for(int y=0; y<4;y++){
                if(x==y)
                    m[x][y]=1.0;
                else
                    m[x][y]=0.0;
            }
        }
    }
    
}
