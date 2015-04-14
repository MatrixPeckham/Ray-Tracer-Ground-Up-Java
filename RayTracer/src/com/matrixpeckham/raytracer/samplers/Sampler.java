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
package com.matrixpeckham.raytracer.samplers;

import com.matrixpeckham.raytracer.util.Point2D;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author William Matrix Peckham
 */
public abstract class Sampler {

    protected int numSamples;
    protected int numSets;
    protected ArrayList<Point2D> samples;
    protected ArrayList<Integer> shuffledIndices;
    protected ArrayList<Point2D> diskSamples;
    protected ArrayList<Point3D> hemisphereSamples;
    protected ArrayList<Point3D> sphereSamples;
    protected int count;
    protected int jump;

    public Sampler() {
        this(1);
    }

    public Sampler(int i) {
        this(i, 83);
    }

    public Sampler(int i, int s) {
        numSamples = i;
        numSets = s;
        count = 0;
        jump = 0;
        samples = new ArrayList<>(numSamples * numSets);
        samples = new ArrayList<>();
        shuffledIndices = new ArrayList<>();
        diskSamples = new ArrayList<>();
        hemisphereSamples = new ArrayList<>();
        sphereSamples = new ArrayList<>();
        setupShuffledIndices();
    }

    public Sampler(Sampler s) {
        numSamples = s.numSamples;
        numSets = s.numSets;
        samples = new ArrayList<>(s.samples);
        shuffledIndices = new ArrayList<>(s.shuffledIndices);
        diskSamples = new ArrayList<>(s.diskSamples);
        hemisphereSamples = new ArrayList<>(s.hemisphereSamples);
        sphereSamples = new ArrayList<>(s.sphereSamples);
        count = s.count;
        jump = s.jump;
    }

    public void setNumSets(int s) {
        numSets = s;
    }

    public int getNumsamples() {
        return numSamples;
    }

    public void shuffleXCoordinates() {
        for (int p = 0; p < numSets; p++) {
            for (int i = 0; i < numSamples - 1; i++) {
                int target = Utility.randInt() % numSamples + p * numSamples;
                double temp = samples.get(i + p * numSamples + 1).x;
                samples.get(i + p * numSamples + 1).x = samples.get(target).x;
                samples.get(target).x = temp;
            }
        }
    }

    public void shuffleYCoordinates() {
        for (int p = 0; p < numSets; p++) {
            for (int i = 0; i < numSamples - 1; i++) {
                int target = Utility.randInt() % numSamples + p * numSamples;
                double temp = samples.get(i + p * numSamples + 1).y;
                samples.get(i + p * numSamples + 1).y = samples.get(target).y;
                samples.get(target).y = temp;
            }
        }
    }

    public void setupShuffledIndices() {
        shuffledIndices.ensureCapacity(numSamples * numSets);
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < numSamples; i++) {
            indices.add(i);
        }
        for (int i = 0; i < numSets; i++) {
            Collections.shuffle(indices, Utility.rand);
            for (int j = 0; j < numSamples; j++) {
                shuffledIndices.add(indices.get(j));
            }
        }
    }
    
    public void mapSamplesToUnitDisk(){
        int size = samples.size();
        double r;
        double phi;
        Point2D sp=new Point2D();
        diskSamples = new ArrayList<>(size);
        for(int j=0; j<size;j++){
            sp.x=2.0*samples.get(j).x-1.0;
            sp.y=2.0*samples.get(j).y-1.0;
            if(sp.x>-sp.y){
                if(sp.x>sp.y){
                    r=sp.x;
                    if(sp.x!=0){
                        phi=sp.y/sp.x;
                    } else {
                        phi=0;
                    }
                } else {
                    r=sp.y;
                    if(sp.y!=0){
                        phi=2.0-sp.x/sp.y;
                    } else {
                        phi=0;
                    }
                }
            } else {
                if(sp.x<sp.y){
                    r=-sp.x;
                    if(sp.x!=0){
                        phi=4+sp.y/sp.x;
                    } else {
                        phi=0;
                    }
                } else {
                    r=-sp.y;
                    if(sp.y!=0){
                        phi=6-sp.x/sp.y;
                    } else {
                        phi=0;
                    }
                }
            }
            phi*=Utility.PI/4.0;
            diskSamples.add(j, new Point2D(r*Math.cos(phi),r*Math.sin(phi)));
        }
        samples.clear();
    }
    
    public void mapSamplesToHemisphere(double exp){
	int size = samples.size();
	hemisphereSamples=new ArrayList<>(numSamples * numSets);
		
	for (int j = 0; j < size; j++) {
		double cos_phi = Math.cos(2.0 * Utility.PI * samples.get(j).x);
		double sin_phi = Math.sin(2.0 * Utility.PI * samples.get(j).x);	
		double cos_theta = Math.pow((1.0 - samples.get(j).y), 1.0 / (exp + 1.0));
		double sin_theta = Math.sqrt (1.0 - cos_theta * cos_theta);
		double pu = sin_theta * cos_phi;
		double pv = sin_theta * sin_phi;
		double pw = cos_theta;
		hemisphereSamples.add(new Point3D(pu, pv, pw)); 
	}
    }
    
    public void mapSamplesToSphere(){
        sphereSamples.ensureCapacity(numSamples*numSets);
        for(int j=0; j<numSamples*numSets; j++){
            double r1=samples.get(j).x;
            double r2=samples.get(j).y;
            double z=1.0-2.0*r1;
            double r=Math.sqrt(1.0-z*z);
            double phi=Utility.TWO_PI*r2;
            double x=r*Math.cos(phi);
            double y=r*Math.sin(phi);
            sphereSamples.add(new Point3D(x,y,z));
        }
    }
    
    public Point2D sampleUnitSquare(){
        if(count%numSamples==0)
            jump=(Utility.randInt()%numSets)*numSamples;
        return (samples.get(jump+shuffledIndices.get(jump + count++ %numSamples)));
    }
    
    public Point2D sampleUnitDisk(){
        if(count%numSamples==0)
            jump=(Utility.randInt()%numSets)*numSamples;
        return (diskSamples.get(jump+shuffledIndices.get(jump + count++ %numSamples)));
    }
    
    public Point3D sampleHemisphere(){
        if(count%numSamples==0)
            jump=(Utility.randInt()%numSets)*numSamples;
        return (hemisphereSamples.get(jump+shuffledIndices.get(jump + count++ %numSamples)));
    }
    
    public Point3D sampleSphere(){
        if(count%numSamples==0)
            jump=(Utility.randInt()%numSets)*numSamples;
        return (sphereSamples.get(jump+shuffledIndices.get(jump + count++ %numSamples)));
    }
    public abstract Sampler clone();
    public abstract void generateSamples();
    Point2D sampleOneSet(){
        return samples.get(count++%numSamples);
    }
}
