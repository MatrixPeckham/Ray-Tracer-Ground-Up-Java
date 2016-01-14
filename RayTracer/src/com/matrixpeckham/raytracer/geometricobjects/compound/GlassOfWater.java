/*
 Copyright (C) 2016 William Matrix Peckham
 
 This program is free software{} you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation{} either version 2
 of the License, or (at your option) any later version.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY{} without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public License
 along with this program{} if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.matrixpeckham.raytracer.geometricobjects.compound;

import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConcavePartCylinder;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConcavePartTorus;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConvexPartCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Disk;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Ring;
import com.matrixpeckham.raytracer.materials.Material;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;

/**
 *
 * @author William Matrix Peckham
 */
public class GlassOfWater extends Compound {

    public GlassOfWater() {
        this(2, .9, .1, .3, 1.5, .1);
    }

    public GlassOfWater(double _height,
            double _inner_radius,
            double _wall_thickness,
            double _base_thickness,
            double _water_height,
            double _meniscus_radius) {
        super();
        height = _height;
        inner_radius = _inner_radius;
        wall_thickness = _wall_thickness;
        base_thickness = _base_thickness;
        water_height = _water_height;
        meniscus_radius = _meniscus_radius;
        buildComponents();
    }

    public GlassOfWater(GlassOfWater gw) {
        super(gw);
        height = gw.height;
        inner_radius = gw.inner_radius;
        wall_thickness = gw.wall_thickness;
        base_thickness = gw.base_thickness;
        water_height = gw.water_height;
        meniscus_radius = gw.meniscus_radius;
    }

    public GlassOfWater clone() {
        return new GlassOfWater(this);
    }

    void buildComponents() {
	// build the glass parts

        objects.add(new Ring(new Point3D(0, height, 0), // rim at top
                new Normal(0, 1, 0),
                inner_radius,
                inner_radius + wall_thickness));

        objects.add(new Disk(new Point3D(0), // bottom of glass
                new Normal(0, -1, 0),
                inner_radius + wall_thickness));

        objects.add(new ConcavePartCylinder(water_height + meniscus_radius, // inner curved surface of glass
                        height,
                        inner_radius));

        objects.add(new ConvexPartCylinder(0, // outer curved surface of glass
                height,
                inner_radius + wall_thickness));

	// build the water parts
        objects.add(new Disk(new Point3D(0, water_height, 0), // top of water
                new Normal(0, 1, 0),
                inner_radius - meniscus_radius));

        objects.add(new Disk(new Point3D(0, base_thickness, 0), // bottom of water
                new Normal(0, -1, 0),
                inner_radius));

        objects.add(new ConvexPartCylinder(base_thickness, // curved surface of water
                water_height + meniscus_radius,
                inner_radius));

	// build the meniscus: we need an instance for this
        Instance meniscus_ptr = new Instance(new ConcavePartTorus(inner_radius
                - meniscus_radius,
                meniscus_radius,
                0, 360, // azimuth angle - phi - range
                270, 360)); // polar angle - theta - range
        meniscus_ptr.translate(0, water_height + meniscus_radius, 0);
        objects.add(meniscus_ptr);
    }

    public void setGlassAirMaterial(Material m_ptr) {
 	for (int j = 0; j < 4; j++)
		objects.get(j).setMaterial(m_ptr);
   }

    public void setWaterAirMaterial(Material m_ptr) {
		objects.get(4).setMaterial(m_ptr);
		objects.get(7).setMaterial(m_ptr);
    }

    public void setWaterGlassMaterial(Material m_ptr) {
 	for (int j = 5; j < 7; j++)
		objects.get(j).setMaterial(m_ptr);
    }

    double height;// total height
    double inner_radius;// inner radius of glass, outer radius of water
    double wall_thickness;// thickness of the glass wall
    double base_thickness;// thickness of the glass base
    double water_height;// height of water from bottom of glass base on (x, z) plane
    double meniscus_radius;
}
