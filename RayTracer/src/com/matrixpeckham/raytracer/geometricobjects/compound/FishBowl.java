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
package com.matrixpeckham.raytracer.geometricobjects.compound;

import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConcavePartSphere;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConcavePartTorus;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConvexPartSphere;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConvexPartTorus;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Disk;
import com.matrixpeckham.raytracer.materials.Material;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import static com.matrixpeckham.raytracer.util.Utility.PI;
import static com.matrixpeckham.raytracer.util.Utility.PI_ON_180;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 *
 * @author William Matrix Peckham
 */
public class FishBowl extends Compound {

    protected double inner_radius;		// radius of the inside glass surface
    protected double glass_thickness;
    protected double water_depth; 		// measured from the bottom of the water-glass boundary		
    protected double meniscus_radius;
    protected double opening_angle;		// specifies how wide the opening is at the top (alpha in Figure 28.40(a))

    /**
     * default constructor
     */
    public FishBowl() {
        super();
        inner_radius = (1.0);
        glass_thickness = (0.1);
        water_depth = (1.25);
        meniscus_radius = (0.05);
        opening_angle = (90);

        build_components();
    }

    /**
     * Initialization constructor
     *
     * @param _inner_radius radius of the inside of the fish bowl
     * @param _glass_thickness thickness of bowl
     * @param _water_depth depth of water from the inside bottom of bowl
     * @param Meniscus_radius radius of the meniscus
     * @param _opening_angle angle that the bowl opens at
     */
    public FishBowl(double _inner_radius,
            double _glass_thickness,
            double _water_depth,
            double Meniscus_radius,
            double _opening_angle) {
        super();
        inner_radius = (_inner_radius);
        glass_thickness = (_glass_thickness);
        water_depth = (_water_depth);
        meniscus_radius = (Meniscus_radius);
        opening_angle = (_opening_angle);
        build_components();
    }

    /**
     * copy constructor
     *
     * @param fb
     */
    public FishBowl(FishBowl fb) {
        super(fb);
        inner_radius = fb.inner_radius;
        glass_thickness = fb.glass_thickness;
        water_depth = fb.water_depth;
        meniscus_radius = fb.meniscus_radius;
        opening_angle = fb.opening_angle;
    }

    /**
     * clone
     *
     * @return
     */
    public FishBowl clone() {
        return new FishBowl(this);
    }

    /**
     * adds the components for the bowl to the compound
     */
    public void build_components() {
        double angle_radians = (opening_angle / 2.0) * PI_ON_180; // half the opening angle in radians

	// meniscus calculations - required here because they affect the inner surface of the glass-air boundary
        // torus tube center coordinates
        double h = water_depth - inner_radius;
        double yc = h + meniscus_radius;
        double xc = sqrt(inner_radius * (inner_radius - 2.0 * meniscus_radius)
                - h * (h + 2.0 * meniscus_radius));
        double beta = atan2(yc, xc) * 180.0 / PI;   // in degrees

        // outer glass-air boundary
        objects.add(new ConvexPartSphere(new Point3D(0.0),
                inner_radius + glass_thickness,
                0, 360, // azimuth angle range - full circle
                opening_angle / 2.0, // minimum polar angle measured from top
                180));                 	// maximum polar angle measured from top

        // inner glass-air boundary
        // the inner surface of the glass only goes down to the top of the meniscus
        objects.add(new ConcavePartSphere(new Point3D(0.0),
                inner_radius,
                0, 360, // azimuth angle - full circle
                opening_angle / 2.0, // mimimum polar angle measured from top
                90 - beta));   			// maximum polar angle measured from top																

        // round rim - need an instance for this as it's a half torus
        double thetaMin = opening_angle / 2.0;  	// measured counter-clockwise from (x, z) plane
        double thetaMax = thetaMin + 180;			// measured counter-clockwise from (x, z) plane

        Instance rim_ptr = new Instance(new ConvexPartTorus(
                (inner_radius + glass_thickness / 2.0) * sin(angle_radians), // a
                glass_thickness / 2.0, // b
                0, 360,
                thetaMin,
                thetaMax));

        rim_ptr.translate(0, (inner_radius + glass_thickness / 2.0) * cos(
                angle_radians), 0);
        objects.add(rim_ptr);

        // meniscus - if water_depth > 1, we need two part tori
        Instance torus_ptr1 = new Instance(new ConcavePartTorus(xc,
                meniscus_radius,
                0, 360,
                270, 360));
        torus_ptr1.translate(0, yc, 0);
        objects.add(torus_ptr1);

        Instance torus_ptr2 = new Instance(new ConcavePartTorus(xc,
                meniscus_radius,
                0, 360,
                0, beta));
        torus_ptr2.translate(0, yc, 0);
        objects.add(torus_ptr2);

        // water-air boundary
        objects.add(new Disk(new Point3D(0, h, 0),
                new Normal(0, 1, 0),
                xc));				// the disk just touches the bottom of the meniscus

        // water-glass boundary
        objects.add(new ConvexPartSphere(new Point3D(0),
                inner_radius,
                0, 360,
                90 - beta, // mimimum polar angle measured from top
                180));			// maximum polar angle measured from top
    }

    /**
     * sets the material for the glass that touches air
     *
     * @param m_ptr
     */
    public void setGlassAirMaterial(Material m_ptr) {
        // [0]: outer glass-air boundary
        // [1]: inner glass-air boundary
        // [2]: rim
        for (int j = 0; j < 3; j++) {
            objects.get(j).setMaterial(m_ptr);
        }
    }

    /**
     * sets the material for the water that hits air
     *
     * @param m_ptr
     */
    public void setWaterAirMaterial(Material m_ptr) {
        // [3]: meniscus torus 1
        // [4]: meniscus torus 2
        // [5]: water-air boundary
        objects.get(3).setMaterial(m_ptr);
        objects.get(4).setMaterial(m_ptr);
        objects.get(5).setMaterial(m_ptr);
    }

    /**
     * sets the material for water that touches the glass
     *
     * @param m_ptr
     */
    public void setWaterGlassMaterial(Material m_ptr) {
        // [6]: water-glass boundary
        objects.get(6).setMaterial(m_ptr);
    }

}
