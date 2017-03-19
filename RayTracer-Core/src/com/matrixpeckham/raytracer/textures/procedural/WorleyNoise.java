/*
 * Copyright (C) 2016 William Matrix Peckham
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

import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Vector3D;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

/**
 *
 */
/**
 * @author Wiliam Peckham
 *
 */
public class WorleyNoise extends Noise {

    //seed for reproducability
    long seed = seed_value;

    //points per line segment/square/cube/hypercube
    int numPerBlock;

    //size of same
    int blockSize;

    //which distance do we use
    int nthDist;

    //distance calulation
    int dist;

    //Euclician
    public final static int EUCLID_DIST = 0;

    //Manhattan
    public final static int MANHAT_DIST = 1;

    //ChebyShev
    public final static int CHEBYC_DIST = 2;

    //Squared distance
    public final static int SQUARE_DIST = 3;

    //Quadratic distance
    public final static int QUADRA_DIST = 4;

    public WorleyNoise() {
        this(1, 10, 1, EUCLID_DIST);
    }

    public WorleyNoise(int blocksize, int numPerBlocks, int nthDist,
            int dist) {
        this.blockSize = blocksize;
        this.numPerBlock = numPerBlocks;
        this.nthDist = nthDist;
        this.dist = dist;
    }

    public WorleyNoise(WorleyNoise n) {
        super(n);
        this.blockSize = n.blockSize;
        this.numPerBlock = n.numPerBlock;
        this.nthDist = n.nthDist;
        this.dist = n.dist;
    }

    @Override
    public Noise cloneNoise() {
        return this;//new WorleyNoise(this);
    }

    //gets the nth distance from the set
    private double getNth(TreeSet<Double> set) {
        int i = 0;
        double ret = Double.NaN;
        Iterator<Double> it = set.iterator();
        while (it.hasNext() && i < nthDist) {
            i++;
            ret = it.next();
        }
        return ret;
    }

    //one dimensional noise
    public double noise(double x) {

        int ix = (int) Math.floor(x / blockSize);

        TreeSet<Double> set = new TreeSet<>();

        block1D(ix, x, set);
        block1D(ix + 1, x, set);
        block1D(ix - 1, x, set);

        return getNth(set);
    }

    //calculate segment ix and add distances to set
    void block1D(int ix, double x, TreeSet<Double> set) {
        Random r = new Random((seed + "" + ix).hashCode());
        for (int i = 0; i < numPerBlock; i++) {
            double np = r.nextDouble() * blockSize + ix * blockSize;
            set.add(abs(np - x));
        }
    }

    //two d noise
    public double noise(double x, double y) {
        int ix = (int) Math.floor(x / blockSize);
        int iy = (int) Math.floor(x / blockSize);

        TreeSet<Double> set = new TreeSet<>();

        //add all the surrounding grid cells points to the set if needed
        block2D(ix, iy, x, y, set);

        block2D(ix - 1, iy - 1, x, y, set);
        block2D(ix - 1, iy, x, y, set);
        block2D(ix - 1, iy + 1, x, y, set);
        block2D(ix + 1, iy - 1, x, y, set);
        block2D(ix + 1, iy, x, y, set);
        block2D(ix + 1, iy + 1, x, y, set);
        block2D(ix, iy + 1, x, y, set);
        block2D(ix, iy - 1, x, y, set);

        return getNth(set);
    }

    //gets the disnatances to x,y in box ix,iy and adds them to the set
    void block2D(int ix, int iy, double x, double y, TreeSet<Double> set) {
        Random r = new Random((seed + "" + ix + "" + iy).hashCode());
        for (int i = 0; i < numPerBlock; i++) {
            double nx = r.nextDouble() * blockSize + ix * blockSize;
            double ny = r.nextDouble() * blockSize + iy * blockSize;
            double dist2 = Double.NaN;
            switch (this.dist) {
                case EUCLID_DIST:
                    dist2 = Math.hypot(x - nx, y - ny);
                    break;
                case MANHAT_DIST:
                    dist2 = abs(nx - x) + abs(ny - y);
                    break;
                case CHEBYC_DIST:
                    dist2 = max(abs(nx - x), abs(ny - y));
                    break;
                case SQUARE_DIST:
                    dist2 = (nx - x) * (nx - x) + (ny - y) * (ny - y);
                    break;
                case QUADRA_DIST:
                    dist2 = (nx - x) * (nx - x) + (nx - x) * (ny - y) + (ny - y)
                            * (ny - y);
                    break;
                default:
                    dist2 = Math.hypot(x - nx, y - ny);
            }
            set.add(dist2);
        }
    }

    public double noise(double x, double y, double z) {
        int ix = (int) Math.floor(x / blockSize);
        int iy = (int) Math.floor(y / blockSize);
        int iz = (int) Math.floor(z / blockSize);

        TreeSet<Double> set = new TreeSet<>();

        block3D(ix, iy, iz, x, y, z, set);
        block3D(ix, iy, iz - 1, x, y, z, set);
        block3D(ix, iy, iz + 1, x, y, z, set);
        block3D(ix, iy - 1, iz - 1, x, y, z, set);
        block3D(ix, iy - 1, iz, x, y, z, set);
        block3D(ix, iy - 1, iz + 1, x, y, z, set);
        block3D(ix, iy + 1, iz - 1, x, y, z, set);
        block3D(ix, iy + 1, iz, x, y, z, set);
        block3D(ix, iy + 1, iz + 1, x, y, z, set);

        block3D(ix + 1, iy, iz, x, y, z, set);
        block3D(ix + 1, iy, iz + 1, x, y, z, set);
        block3D(ix + 1, iy, iz - 1, x, y, z, set);
        block3D(ix + 1, iy + 1, iz - 1, x, y, z, set);
        block3D(ix + 1, iy + 1, iz, x, y, z, set);
        block3D(ix + 1, iy + 1, iz + 1, x, y, z, set);
        block3D(ix + 1, iy - 1, iz - 1, x, y, z, set);
        block3D(ix + 1, iy - 1, iz, x, y, z, set);
        block3D(ix + 1, iy - 1, iz + 1, x, y, z, set);

        block3D(ix - 1, iy, iz, x, y, z, set);
        block3D(ix - 1, iy, iz + 1, x, y, z, set);
        block3D(ix - 1, iy, iz - 1, x, y, z, set);
        block3D(ix - 1, iy + 1, iz - 1, x, y, z, set);
        block3D(ix - 1, iy + 1, iz, x, y, z, set);
        block3D(ix - 1, iy + 1, iz + 1, x, y, z, set);
        block3D(ix - 1, iy - 1, iz - 1, x, y, z, set);
        block3D(ix - 1, iy - 1, iz, x, y, z, set);
        block3D(ix - 1, iy - 1, iz + 1, x, y, z, set);

        return getNth(set);
    }

    //gets the disnatances to x,y,x in box ix,iy,iz and adds them to the set
    void block3D(int ix, int iy, int iz, double x, double y, double z,
            TreeSet<Double> set) {
        Random r = new Random((seed + "" + ix + "" + iy + "" + iz).hashCode());
        for (int i = 0; i < numPerBlock; i++) {
            double nx = r.nextDouble() * blockSize + ix * blockSize;
            double ny = r.nextDouble() * blockSize + iy * blockSize;
            double nz = r.nextDouble() * blockSize + iz * blockSize;
            double dist2 = Double.NaN;
            double dx = nx - x;
            double dy = ny - y;
            double dz = nz - z;
            switch (this.dist) {
                case EUCLID_DIST:
                    dist2 = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    break;
                case MANHAT_DIST:
                    dist2 = abs(dx) + abs(dy) + abs(dz);
                    break;
                case CHEBYC_DIST:
                    dist2 = max(abs(dx), max(abs(dy), abs(dz)));
                    break;
                case SQUARE_DIST:
                    dist2 = dx * dx + dy * dy + dz * dz;
                    break;
                case QUADRA_DIST:
                    dist2 = dx * dx + 2 * dx * dy + 2 * dx * dz + 2 * dy * dz
                            + dy * dy + dz * dz;
                    break;
                default:
                    dist2 = Math.sqrt(dx * dx + dy * dy + dz * dz);
            }
            if (set.size() == nthDist) {
                if (set.last() > dist2) {
                    set.remove(set.last());
                    set.add(dist2);
                }
            } else {
                set.add(dist2);
            }
        }

    }

    public double noise(double x, double y, double z, double w) {
        int ix = (int) Math.floor(x / blockSize);
        int iy = (int) Math.floor(y / blockSize);
        int iz = (int) Math.floor(z / blockSize);
        int iw = (int) Math.floor(w / blockSize);

        TreeSet<Double> set = new TreeSet<>();

        block4D(ix, iy, iz, iw, x, y, z, w, set);

        block4D(ix + 1, iy, iz, iw, x, y, z, w, set);
        block4D(ix + 1, iy + 1, iz - 1, iw, x, y, z, w, set);
        block4D(ix + 1, iy + 1, iz, iw, x, y, z, w, set);
        block4D(ix + 1, iy + 1, iz + 1, iw, x, y, z, w, set);
        block4D(ix + 1, iy - 1, iz - 1, iw, x, y, z, w, set);
        block4D(ix + 1, iy - 1, iz, iw, x, y, z, w, set);
        block4D(ix + 1, iy - 1, iz + 1, iw, x, y, z, w, set);
        block4D(ix + 1, iy, iz + 1, iw, x, y, z, w, set);
        block4D(ix + 1, iy, iz - 1, iw, x, y, z, w, set);
        block4D(ix - 1, iy, iz, iw, x, y, z, w, set);
        block4D(ix - 1, iy + 1, iz - 1, iw, x, y, z, w, set);
        block4D(ix - 1, iy + 1, iz, iw, x, y, z, w, set);
        block4D(ix - 1, iy + 1, iz + 1, iw, x, y, z, w, set);
        block4D(ix - 1, iy - 1, iz - 1, iw, x, y, z, w, set);
        block4D(ix - 1, iy - 1, iz, iw, x, y, z, w, set);
        block4D(ix - 1, iy - 1, iz + 1, iw, x, y, z, w, set);
        block4D(ix - 1, iy, iz + 1, iw, x, y, z, w, set);
        block4D(ix - 1, iy, iz - 1, iw, x, y, z, w, set);
        block4D(ix, iy + 1, iz - 1, iw, x, y, z, w, set);
        block4D(ix, iy + 1, iz, iw, x, y, z, w, set);
        block4D(ix, iy + 1, iz + 1, iw, x, y, z, w, set);
        block4D(ix, iy - 1, iz - 1, iw, x, y, z, w, set);
        block4D(ix, iy - 1, iz, iw, x, y, z, w, set);
        block4D(ix, iy - 1, iz + 1, iw, x, y, z, w, set);
        block4D(ix, iy, iz + 1, iw, x, y, z, w, set);
        block4D(ix, iy, iz - 1, iw, x, y, z, w, set);

        block4D(ix, iy, iz, iw + 1, x, y, z, w, set);
        block4D(ix + 1, iy, iz, iw + 1, x, y, z, w, set);
        block4D(ix + 1, iy + 1, iz - 1, iw + 1, x, y, z, w, set);
        block4D(ix + 1, iy + 1, iz, iw + 1, x, y, z, w, set);
        block4D(ix + 1, iy + 1, iz + 1, iw + 1, x, y, z, w, set);
        block4D(ix + 1, iy - 1, iz - 1, iw + 1, x, y, z, w, set);
        block4D(ix + 1, iy - 1, iz, iw + 1, x, y, z, w, set);
        block4D(ix + 1, iy - 1, iz + 1, iw + 1, x, y, z, w, set);
        block4D(ix + 1, iy, iz + 1, iw + 1, x, y, z, w, set);
        block4D(ix + 1, iy, iz - 1, iw + 1, x, y, z, w, set);
        block4D(ix - 1, iy, iz, iw + 1, x, y, z, w, set);
        block4D(ix - 1, iy + 1, iz - 1, iw + 1, x, y, z, w, set);
        block4D(ix - 1, iy + 1, iz, iw + 1, x, y, z, w, set);
        block4D(ix - 1, iy + 1, iz + 1, iw + 1, x, y, z, w, set);
        block4D(ix - 1, iy - 1, iz - 1, iw + 1, x, y, z, w, set);
        block4D(ix - 1, iy - 1, iz, iw + 1, x, y, z, w, set);
        block4D(ix - 1, iy - 1, iz + 1, iw + 1, x, y, z, w, set);
        block4D(ix - 1, iy, iz + 1, iw + 1, x, y, z, w, set);
        block4D(ix - 1, iy, iz - 1, iw + 1, x, y, z, w, set);
        block4D(ix, iy + 1, iz - 1, iw + 1, x, y, z, w, set);
        block4D(ix, iy + 1, iz, iw + 1, x, y, z, w, set);
        block4D(ix, iy + 1, iz + 1, iw + 1, x, y, z, w, set);
        block4D(ix, iy - 1, iz - 1, iw + 1, x, y, z, w, set);
        block4D(ix, iy - 1, iz, iw + 1, x, y, z, w, set);
        block4D(ix, iy - 1, iz + 1, iw + 1, x, y, z, w, set);
        block4D(ix, iy, iz + 1, iw + 1, x, y, z, w, set);
        block4D(ix, iy, iz - 1, iw + 1, x, y, z, w, set);

        block4D(ix, iy, iz, iw - 1, x, y, z, w, set);
        block4D(ix + 1, iy, iz, iw - 1, x, y, z, w, set);
        block4D(ix + 1, iy + 1, iz - 1, iw - 1, x, y, z, w, set);
        block4D(ix + 1, iy + 1, iz, iw - 1, x, y, z, w, set);
        block4D(ix + 1, iy + 1, iz + 1, iw - 1, x, y, z, w, set);
        block4D(ix + 1, iy - 1, iz - 1, iw - 1, x, y, z, w, set);
        block4D(ix + 1, iy - 1, iz, iw - 1, x, y, z, w, set);
        block4D(ix + 1, iy - 1, iz + 1, iw - 1, x, y, z, w, set);
        block4D(ix + 1, iy, iz + 1, iw - 1, x, y, z, w, set);
        block4D(ix + 1, iy, iz - 1, iw - 1, x, y, z, w, set);
        block4D(ix - 1, iy, iz, iw - 1, x, y, z, w, set);
        block4D(ix - 1, iy + 1, iz - 1, iw - 1, x, y, z, w, set);
        block4D(ix - 1, iy + 1, iz, iw - 1, x, y, z, w, set);
        block4D(ix - 1, iy + 1, iz + 1, iw - 1, x, y, z, w, set);
        block4D(ix - 1, iy - 1, iz - 1, iw - 1, x, y, z, w, set);
        block4D(ix - 1, iy - 1, iz, iw - 1, x, y, z, w, set);
        block4D(ix - 1, iy - 1, iz + 1, iw - 1, x, y, z, w, set);
        block4D(ix - 1, iy, iz + 1, iw - 1, x, y, z, w, set);
        block4D(ix - 1, iy, iz - 1, iw - 1, x, y, z, w, set);
        block4D(ix, iy + 1, iz - 1, iw - 1, x, y, z, w, set);
        block4D(ix, iy + 1, iz, iw - 1, x, y, z, w, set);
        block4D(ix, iy + 1, iz + 1, iw - 1, x, y, z, w, set);
        block4D(ix, iy - 1, iz - 1, iw - 1, x, y, z, w, set);
        block4D(ix, iy - 1, iz, iw - 1, x, y, z, w, set);
        block4D(ix, iy - 1, iz + 1, iw - 1, x, y, z, w, set);
        block4D(ix, iy, iz + 1, iw - 1, x, y, z, w, set);
        block4D(ix, iy, iz - 1, iw - 1, x, y, z, w, set);

        return getNth(set);
    }

    //gets the disnatances to x,y,z,w in box ix,iy,iz,iw and adds them to the set
    void block4D(int ix, int iy, int iz, int iw, double x, double y, double z,
            double w, TreeSet<Double> set) {
        Random r = new Random((seed + "" + ix + "" + iy + "" + iz + "" + iw).
                hashCode());
        for (int i = 0; i < numPerBlock; i++) {
            double nx = r.nextDouble() * blockSize + ix * blockSize;
            double ny = r.nextDouble() * blockSize + iy * blockSize;
            double nz = r.nextDouble() * blockSize + iz * blockSize;
            double nw = r.nextDouble() * blockSize + iw * blockSize;
            double dist2 = Double.NaN;
            double dx = nx - x;
            double dy = ny - y;
            double dz = nz - z;
            double dw = nw - w;
            switch (this.dist) {
                case EUCLID_DIST:
                    dist2 = Math.sqrt(dx * dx + dy * dy + dz * dz + dw * dw);
                    break;
                case MANHAT_DIST:
                    dist2 = abs(dx) + abs(dy) + abs(dz) + abs(dw);
                    break;
                case CHEBYC_DIST:
                    dist2 = max(max(abs(dx), abs(dz)), max(abs(dy), abs(dz)));
                    break;
                case SQUARE_DIST:
                    dist2 = dx * dx + dy * dy + dz * dz + dw * dw;
                    break;
                case QUADRA_DIST:
                    dist2 = dx * dx + 2 * dx * dy + 2 * dx * dz + 2 * dx * dw
                            + dy * dy + 2 * dy * dz + 2 * dy * dw + dz * dz + 2
                            * dz * dw + dw * dw;
                    break;
                default:
                    dist2 = Math.sqrt(dx * dx + dy * dy + dz * dz + dw * dw);
            }
            set.add(dist2);
        }

    }

    private double abs(double d) {
        return d < 0 ? -d : d;
    }

    private double max(double a, double b) {
        return a < b ? b : a;
    }

    @Override
    public double valueNoise(Point3D p) {
        return noise(p.x, p.y, p.z) / blockSize;
    }

    @Override
    public Vector3D vectorNoise(Point3D p) {
        long classSeed = this.seed;

        long xSeed = classSeed * 317;
        long ySeed = classSeed * 887;
        long zSeed = classSeed * 997;

        this.seed = xSeed;
        double x = noise(p.x, p.y, p.z);
        this.seed = ySeed;
        double y = noise(p.x, p.y, p.z);
        this.seed = zSeed;
        double z = noise(p.x, p.y, p.z);

        this.seed = classSeed;

        return new Vector3D(x, y, z);
    }
}
