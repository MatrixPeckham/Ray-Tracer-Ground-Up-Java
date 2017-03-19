package com.matrixpeckham.raytracer.geometricobjects.compound;

import com.matrixpeckham.raytracer.geometricobjects.GeometricObject;
import com.matrixpeckham.raytracer.materials.Material;
import com.matrixpeckham.raytracer.util.BBox;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import java.util.ArrayList;

/**
 * Compound class, geometric object constructed of multiple other geometric
 * objects
 *
 * @author William Matrix Peckham
 */
public class Compound extends GeometricObject {

    /**
     * list of sub objects
     */
    protected ArrayList<GeometricObject> objects = new ArrayList<>();

    /**
     * default constructor
     */
    public Compound() {
        super();
    }

    /**
     * copy constructor
     *
     * @param c
     */
    public Compound(Compound c) {
        copyObjects(c.objects);
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public GeometricObject cloneGeometry() {
        return new Compound(this);
    }

    /**
     * Add an object to the collection of sub objects
     *
     * @param obj
     */
    public void addObject(GeometricObject obj) {
        objects.add(obj);
    }

    /**
     * sets the material for every object
     *
     * @param mat
     */
    @Override
    public void setMaterial(Material mat) {
        objects.stream().
                forEach((obj) -> {
                    obj.setMaterial(mat);
                });
    }

    /**
     * sets the material for an object at index i
     *
     * @param mat
     * @param i
     */
    public void setMaterial(Material mat, int i) {
        objects.get(i).setMaterial(mat);
    }

    /**
     * hit function, works the same way as World.hitObjects does.
     *
     * @param ray
     * @param s
     * @return
     */
    @Override
    public boolean hit(Ray ray, ShadeRec s) {

        //temporary storage for keeping lowest distance hit.
        Normal n = new Normal();
        Point3D localHitPoint = new Point3D();
        boolean hit = false;
        double tmin = Utility.HUGE_VALUE;
        int numObjects = objects.size();

        for (int j = 0; j < numObjects; j++) {
            if (objects.get(j).hit(ray, s) && s.lastT < tmin) {
                hit = true;
                tmin = s.lastT;
                material = objects.get(j).getMaterial();
                n.setTo(s.normal);
                localHitPoint.setTo(s.localHitPosition);
            }
        }

        if (hit) {
            //s.t=tmin;
            s.lastT = tmin;
            s.normal.setTo(n);
            s.localHitPosition.setTo(localHitPoint);
        }

        return hit;

    }

    /**
     * hit function, works the same way as World.hitObjects does.
     *
     * @param ray
     * @param s
     * @return
     */
    @Override
    public boolean hit(Ray ray, ArrayList<ShadeRec> hits, ShadeRec sr) {

        boolean hit = false;
        int numObjects = objects.size();

        for (int j = 0; j < numObjects; j++) {
            if (objects.get(j).hit(ray, hits, sr)) {
                hit = true;
            }
        }

        return hit;

    }

    /**
     * shadow hit function works same way as hit function
     *
     * @param ray
     * @param t
     * @return
     */
    @Override
    public boolean shadowHit(Ray ray, DoubleRef t) {
        //early out if shadows false all implementations do this
        if (!shadows) {
            return false;
        }
        boolean hit = false;
        double tmin = Utility.HUGE_VALUE;
        int numObjects = objects.size();

        for (int j = 0; j < numObjects; j++) {
            if (objects.get(j).shadowHit(ray, t) && t.d < tmin) {
                hit = true;
                tmin = t.d;
            }
        }

        if (hit) {
            t.d = tmin;
        }

        return hit;
    }

    /**
     * clears sub objects
     */
    private void deleteObjects() {
        objects.clear();
    }

    /**
     * clones objects in rhs into this compound object
     *
     * @param rhs
     */
    private void copyObjects(ArrayList<GeometricObject> rhs) {
        deleteObjects();
        rhs.stream().
                forEach((obj) -> {
                    objects.add(obj.cloneGeometry());
                });
    }

    /**
     * returns the number of objects in this compound
     *
     * @return
     */
    public int getNumObjects() {
        return objects.size();
    }

    /**
     * expands an empty bounding box to hold each sub object
     *
     * @return
     */
    @Override
    public BBox getBoundingBox() {
        BBox box = new BBox(0, 0, 0, 0, 0, 0);
        objects.stream().
                forEach((obj) -> {
                    box.expandToFit(obj.getBoundingBox());
                });
        return box;
    }

}
