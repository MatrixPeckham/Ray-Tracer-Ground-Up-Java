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
package com.matrixpeckham.raytracer.util.ply;

import java.util.TreeMap;

/**
 *
 * @author William Matrix Peckham
 */
public class PLYElement {
    double data[][];
    ElementType t;
    TreeMap<String, Integer> props;
    public PLYElement(ElementType t){
        this.t=t;
        props=t.props;
        data=new double[props.size()][];
        for(int i = 0; i<props.size(); i++){
            data[i]=t.isList.get(i)?new double[0]:new double[]{0};
        }
    }
    
    public void setDouble(String name, double d){
        data[props.get(name)][0]=d;
    }
    public void setDoubleList(String name, double d[]){
        data[props.get(name)]=d.clone();
    }
    public void setDouble(int i, double d){
        data[i][0]=d;
    }
    public void setDoubleList(int i, double d[]){
        data[i]=d.clone();
    }
    public double getDouble(String name){
        return data[props.get(name)][0];
    }
    public double[] getDoubleList(String name){
        return data[props.get(name)].clone();
    }
    public void setInt(String name, int d){
        data[props.get(name)][0]=d;
    }
    public void setIntList(String name, int d[]){
        data[props.get(name)]=new double[d.length];
        for(int i =0; i<d.length; i++){
            data[props.get(name)][i]=d[i];
        }
    }
    public void setInt(int i, int d){
        data[i][0]=d;
    }
    public void setIntList(int ind, int d[]){
        data[ind]=new double[d.length];
        for(int i =0; i<d.length; i++){
            data[ind][i]=d[i];
        }
    }
    public int getInt(String name){
        return(int) data[props.get(name)][0];
    }
    public int[] getIntList(String name){
        int[] a=new int[data[props.get(name)].length];
        for(int i =0; i<a.length; i++){
            a[i]=(int)data[props.get(name)][i];
        }
        return a;
    }
    
}
