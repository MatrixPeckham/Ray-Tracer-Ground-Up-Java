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
 * Abstract base class for generating random samples in various patters.
 *
 * @author William Matrix Peckham
 */
public abstract class Sampler {

    /**
     * number of samples to generate per pattern
     */
    protected int numSamples;

    /**
     * number of patterns to generate. defaults to 83, a somewhat large prime
     * should be changed if image width will be a multiple of 83, as that will
     * cause column samples to be identical and may lead to some bad artifacts.
     */
    protected int numSets;

    /**
     * original 2d unit square samples, will have size numSamples*numSets, or
     * will be empty if a mapping function is called
     */
    protected ArrayList<Point2D> samples;

    /**
     * shuffled samples array indices
     */
    protected ArrayList<Integer> shuffledIndices;

    /**
     * sample points mapped to a unit disc, filled to size numSamples*numSets
     * when mapSamplesToUnitDisc is called.
     */
    protected ArrayList<Point2D> discSamples;

    /**
     * sample points mapped to unit hemisphere, filled to size
     * numSamples*numSets when mapSamplesToUnitHemisphere(exp) is called.
     */
    protected ArrayList<Point3D> hemisphereSamples;

    /**
     * sample points mapped to unit sphere, filled to size numSamples*numSets
     * when mapSamplesToUnitPhere is called.
     */
    protected ArrayList<Point3D> sphereSamples;

    /**
     * current number of samples taken, also considered current index in c/C++
     * this would be unsigned long to avoid possible overflow and to allow
     * extreme number of samples, but java doesn't allow long indices to
     * arrays/ArrayLists, so we check for negative number and reset it to 0 if
     * overflow happens, which it can on LARGE images.
     */
    protected int count;

    /**
     * random index jump, used to select random sample set at each new pixel
     */
    protected int jump;

    /**
     * default sampler constructor, one sample.
     */
    public Sampler() {
        this(1);
    }

    /**
     * Create a sampler with i samples and 83 sets
     *
     * @param i
     */
    public Sampler(int i) {
        this(i, 83);
    }

    /**
     * create a sampler with i samples and s sets.
     *
     * @param i
     * @param s
     */
    public Sampler(int i, int s) {
        numSamples = i;
        numSets = s;
        count = 0;
        jump = 0;
        samples = new ArrayList<>(numSamples * numSets);
        samples = new ArrayList<>();
        shuffledIndices = new ArrayList<>();
        discSamples = new ArrayList<>();
        hemisphereSamples = new ArrayList<>();
        sphereSamples = new ArrayList<>();
        setupShuffledIndices();
    }

    /**
     * copy constructor
     *
     * @param s
     */
    public Sampler(Sampler s) {
        numSamples = s.numSamples;
        numSets = s.numSets;
        samples = new ArrayList<>(s.samples);
        shuffledIndices = new ArrayList<>(s.shuffledIndices);
        discSamples = new ArrayList<>(s.discSamples);
        hemisphereSamples = new ArrayList<>(s.hemisphereSamples);
        sphereSamples = new ArrayList<>(s.sphereSamples);
        count = s.count;
        jump = s.jump;
    }

    /**
     * sets number of sample set
     *
     * @param s
     */
    public void setNumSets(int s) {
        numSets = s;
    }

    /**
     * sets number of samples
     *
     * @return
     */
    public int getNumSamples() {
        return numSamples;
    }

    /**
     * shuffles x coordinates of sample points (used in NRooks and
     * MultiJittered)
     */
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

    /**
     * shuffles y coordinates of sample points (used in NRooks and
     * MultiJittered)
     */
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

    /**
     * sets up the shuffled indices for each set
     */
    public final void setupShuffledIndices() {
        //make sure we have enough space, more efficient to allocate all at once
        shuffledIndices.ensureCapacity(numSamples * numSets);

        //temporary array for indices 0-(numSamples-1)
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < numSamples; i++) {
            indices.add(i);
        }

        //for each sample set
        for (int i = 0; i < numSets; i++) {
            //shuffle the indices
            Collections.shuffle(indices, Utility.rand);
            //add them all to the shuffled index array
            for (int j = 0; j < numSamples; j++) {
                shuffledIndices.add(indices.get(j));
            }
        }
    }

    /**
     * turns this sampler into a sampler for discs. Deletes original points
     * after mapping them to a disc, typically called by a primitive or camera
     * to a sampler that has been passed to it
     *
     */
    public void mapSamplesToUnitDisc() {
        //implements Shirley's Cocentric map
        int size = samples.size();
        double r;
        double phi;
        Point2D sp = new Point2D();
        discSamples = new ArrayList<>(size);
        for (int j = 0; j < size; j++) {
            sp.x = 2.0 * samples.get(j).x - 1.0;
            sp.y = 2.0 * samples.get(j).y - 1.0;
            if (sp.x > -sp.y) {
                if (sp.x > sp.y) {
                    r = sp.x;
                    if (sp.x != 0) {
                        phi = sp.y / sp.x;
                    } else {
                        phi = 0;
                    }
                } else {
                    r = sp.y;
                    if (sp.y != 0) {
                        phi = 2.0 - sp.x / sp.y;
                    } else {
                        phi = 0;
                    }
                }
            } else {
                if (sp.x < sp.y) {
                    r = -sp.x;
                    if (sp.x != 0) {
                        phi = 4 + sp.y / sp.x;
                    } else {
                        phi = 0;
                    }
                } else {
                    r = -sp.y;
                    if (sp.y != 0) {
                        phi = 6 - sp.x / sp.y;
                    } else {
                        phi = 0;
                    }
                }
            }
            phi *= Utility.PI / 4.0;
            discSamples.
                    add(j, new Point2D(r * Math.cos(phi), r * Math.sin(phi)));
        }
        samples.clear();
    }

    /**
     * turns this sampler into a sampler for hemisphere. Deletes original points
     * after mapping them to a hemisphere, typically called by a primitive or
     * camera to a sampler that has been passed to it generates a cosine
     * distribution with exp as the exponent
     *
     * @param exp
     */
    public void mapSamplesToHemisphere(double exp) {
        int size = samples.size();
        hemisphereSamples = new ArrayList<>(numSamples * numSets);

        for (int j = 0; j < size; j++) {
            double cos_phi = Math.cos(2.0 * Utility.PI * samples.get(j).x);
            double sin_phi = Math.sin(2.0 * Utility.PI * samples.get(j).x);
            double cos_theta = Math.pow((1.0 - samples.get(j).y), 1.0 / (exp
                    + 1.0));
            double sin_theta = Math.sqrt(1.0 - cos_theta * cos_theta);
            double pu = sin_theta * cos_phi;
            double pv = sin_theta * sin_phi;
            double pw = cos_theta;
            hemisphereSamples.add(new Point3D(pu, pv, pw));
        }
    }

    /**
     * turns this sampler into a sampler for sphere. Deletes original points
     * after mapping them to a sphere, typically called by a primitive or camera
     * to a sampler that has been passed to it
     */
    public void mapSamplesToSphere() {
        sphereSamples.ensureCapacity(numSamples * numSets);
        for (int j = 0; j < numSamples * numSets; j++) {
            double r1 = samples.get(j).x;
            double r2 = samples.get(j).y;
            double z = 1.0 - 2.0 * r1;
            double r = Math.sqrt(1.0 - z * z);
            double phi = Utility.TWO_PI * r2;
            double x = r * Math.cos(phi);
            double y = r * Math.sin(phi);
            sphereSamples.add(new Point3D(x, y, z));
        }
    }

    /**
     * gets the next sample from the unit sphere.
     *
     * @return
     */
    public Point2D sampleUnitSquare() {
        //if this is the first sample from this pixel, calculate next set of
        //samples and the jump point
        if (count % numSamples == 0) {
            jump = (Utility.randInt() % numSets) * numSamples;
        }
        if (count < 0) {
            count = 0;//overflow possible on very large images with large sample counts
        }
        return (samples.get(jump + shuffledIndices.get(jump + count++
                % numSamples)));
    }

    /**
     * get next sample from the unit disc
     *
     * @return
     */
    public Point2D sampleUnitDisc() {
        if (count % numSamples == 0) {
            jump = (Utility.randInt() % numSets) * numSamples;
        }
        if (count < 0) {
            count = 0;//overflow possible on very large images with large sample counts
        }
        return (discSamples.get(jump + shuffledIndices.get(jump + count++
                % numSamples)));
    }

    /**
     * get sample from unit hemisphere
     *
     * @return
     */
    public Point3D sampleHemisphere() {
        if (count % numSamples == 0) {
            jump = (Utility.randInt() % numSets) * numSamples;
        }
        if (count < 0) {
            count = 0;//overflow possible on very large images with large sample counts
        }
        return (hemisphereSamples.get(jump + shuffledIndices.get(jump + count++
                % numSamples)));
    }

    /**
     * get the next sample from the unit sphere.
     *
     * @return
     */
    public Point3D sampleSphere() {
        if (count % numSamples == 0) {
            jump = (Utility.randInt() % numSets) * numSamples;
        }
        if (count < 0) {
            count = 0;//overflow possible on very large images with large sample counts
        }
        return (sphereSamples.get(jump + shuffledIndices.get(jump + count++
                % numSamples)));
    }

    /**
     * clone this sampler
     *
     * @return
     */
    public abstract Sampler cloneSampler();

    /**
     * This method should generate numSamples*numSets samples
     */
    public abstract void generateSamples();

    /**
     * This method is only in the generate vector noise function of the lattice
     * noise class.
     *
     * @return
     */
    public Point2D sampleOneSet() {
        return samples.get(count++ % numSamples);
    }
}
