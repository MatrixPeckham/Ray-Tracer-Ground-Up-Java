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
package com.matrixpeckham.raytracer.geometricobjects.text;

import com.matrixpeckham.raytracer.geometricobjects.GeometricObject;
import com.matrixpeckham.raytracer.geometricobjects.csg.CSGShadeRec;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Rectangle;
import com.matrixpeckham.raytracer.materials.Material;
import com.matrixpeckham.raytracer.materials.SV_Matte;
import com.matrixpeckham.raytracer.textures.ConstantColor;
import com.matrixpeckham.raytracer.textures.Texture;
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.textures.image.ImageTexture;
import com.matrixpeckham.raytracer.util.Utility.Greek;
import com.matrixpeckham.raytracer.util.*;
import com.matrixpeckham.raytracer.world.World;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author William Matrix Peckham
 */
public class TextQuad extends GeometricObject {

    /**
     * object to bump map
     */
    GeometricObject obj = null;

    /**
     * texture for bump mapping (texture colors are interpreted as vector
     * displacements to normals)
     */
    Texture clipMap = null;

    /**
     * string to display
     */
    String text;

    public static final String DEFAULT_STRING = "Unintentionally Left Blank\n"
            + "THE QUICK BROWN FOX JUMPED OVER THE LAZY DOG\n"
            + "the quick brown fox jumped over the lazy dog\n"
            + Greek.Capital.ALPHA
            + Greek.Capital.BETA
            + Greek.Capital.CHI
            + Greek.Capital.DELTA
            + Greek.Capital.EPSILON
            + Greek.Capital.ETA
            + Greek.Capital.GAMMA
            + Greek.Capital.IOTA
            + Greek.Capital.KAPPA
            + Greek.Capital.LAMBDA
            + Greek.Capital.MU
            + Greek.Capital.NU
            + Greek.Capital.OMEGA
            + Greek.Capital.OMICRON
            + Greek.Capital.PHI
            + Greek.Capital.PI
            + Greek.Capital.PSI
            + Greek.Capital.RHO
            + Greek.Capital.SIGMA
            + Greek.Capital.TAU
            + Greek.Capital.THETA
            + Greek.Capital.UPSILON
            + Greek.Capital.XI
            + Greek.Capital.ZETA
            + "\n"
            + Greek.Small.ALPHA
            + Greek.Small.BETA
            + Greek.Small.CHI
            + Greek.Small.DELTA
            + Greek.Small.EPSILON
            + Greek.Small.ETA
            + Greek.Small.GAMMA
            + Greek.Small.IOTA
            + Greek.Small.KAPPA
            + Greek.Small.LAMBDA
            + Greek.Small.MU
            + Greek.Small.NU
            + Greek.Small.OMEGA
            + Greek.Small.OMICRON
            + Greek.Small.PHI
            + Greek.Small.PI
            + Greek.Small.PSI
            + Greek.Small.RHO
            + Greek.Small.SIGMA
            + Greek.Small.FINAL_SIGMA
            + Greek.Small.TAU
            + Greek.Small.THETA
            + Greek.Small.UPSILON
            + Greek.Small.XI
            + Greek.Small.ZETA;

    /**
     * default constructor
     */
    public TextQuad() {
        this(DEFAULT_STRING
        );
    }

    public TextQuad(String text) {
        this(text, 1);
    }

    public TextQuad(String text, double scale) {
        this(text, scale, Utility.BLACK);
    }

    public TextQuad(String text, double scale, RGBColor color) {
        BufferedImage temp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Font font = new Font("monospaced", Font.BOLD, 100);
        String[] lines = text.split("\n");
        Graphics2D tempG = temp.createGraphics();
        tempG.setFont(font);
        FontMetrics metrics = tempG.getFontMetrics();
        int ascent = metrics.getAscent();
        int lineHeight = metrics.getHeight();
        int height = lineHeight * lines.length;
        int width = Integer.MIN_VALUE;
        for (String line : lines) {
            int twid = (int) metrics.getStringBounds(line, tempG).getWidth();
            if (twid > width) {
                width = twid;
            }
        }
        temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        tempG = temp.createGraphics();
        tempG.setFont(font);
        tempG.setColor(Color.WHITE);
        tempG.fillRect(0, 0, width, height);
        tempG.setColor(Color.BLACK);
        int x = 0;
        int y = ascent;
        for (String line : lines) {
            tempG.drawString(line, x, y);
            y += lineHeight;
        }
        tempG.dispose();
        Image image = new Image();
        image.loadFromBufferedImage(temp);

        int tw = image.getHres();
        int th = image.getVres();

        //File file = new File("TEST_TEXT.png");
        //System.out.println(file.getAbsolutePath());
        //try {
        //ImageIO.write(temp, "PNG", file);
        //} catch (IOException ex) {
        //  Logger.getLogger(TextQuad.class.getName()).log(Level.SEVERE, null,
        //        ex);
        //}
        ImageTexture texture = new ImageTexture(image);
        double quadWidth = 1.0 * scale;
        double quadHeight = ((double) height / (double) width);
        quadHeight *= scale;
        Rectangle rect = new Rectangle(new Point3D(),
                new Vector3D(0, quadHeight,
                        0), new Vector3D(quadWidth, 0, 0));
        //Rectangle rect = new Rectangle(new Point3D(0, 0, 0), new Vector3D(1, 0,
        //     0), new Vector3D(0, 1, 0));
        SV_Matte matt = new SV_Matte();
        matt.setCd(new ConstantColor(color));
        matt.setKa(100);
        matt.setKd(1);
        rect.setMaterial(matt);
        this.material = matt;
        this.obj = rect;
        this.clipMap = texture;
    }

    /**
     * copy constructor
     *
     * @param aThis
     */
    private TextQuad(TextQuad aThis) {
        super(aThis);
    }

    /**
     * sets the object to be bumped.
     *
     * @param obj
     */
    public void setObject(GeometricObject obj) {
        this.obj = obj.cloneGeometry();
    }

    /**
     * sets the texture to use as a bump map
     *
     * @param fBmBumpPtr
     */
    public void setClipMap(Texture fBmBumpPtr) {
        this.clipMap = fBmBumpPtr.cloneTexture();
    }

    /**
     * clone method
     *
     * @return
     */
    @Override
    public GeometricObject cloneGeometry() {
        return new TextQuad(this);
    }

    /**
     * Hit function this is where we augment the normal before returning.
     *
     * @param ray
     * @param s
     *
     * @return
     */
    @Override
    public boolean hit(Ray ray, ShadeRec s) {
        //differ to sub object
        boolean hit = obj.hit(ray, s);
        //if we have a hit we need to augment the normal
        if (hit) {
            RGBColor c = clipMap.getColor(s);
            if (c.average() > 0.5) {
                return false;
            }
        }
        return hit;
    }

    /**
     * Hit function this is where we augment the normal before returning.
     *
     * @param ray
     * @param s
     *
     * @return
     */
    @Override
    public boolean hit(Ray ray, ArrayList<CSGShadeRec> hits, ShadeRec sr) {
        //differ to sub object
        ArrayList<CSGShadeRec> nhit = new ArrayList<>();
        boolean hit = obj.hit(ray, nhit, sr);
        //if we have a hit we need to augment the normal
        if (hit) {
            for (CSGShadeRec s : nhit) {
                RGBColor c = clipMap.getColor(s);
                if (!(c.average() > 0.5)) {
                    hits.add(s);
                }
            }
        }
        return hit;
    }

    /**
     * shadow hit only differs to sub object, because it doesn't need the normal
     * it doesn't need to alter anything
     *
     * @param ray
     * @param t
     *
     * @return
     */
    @Override
    public boolean shadowHit(Ray ray, DoubleRef t) {
        ShadeRec rec = new ShadeRec((World) null);
        boolean hit = hit(ray, rec);
        t.d = rec.lastT;
        return hit;
    }

    /**
     * Here we call the sub-object's method then augment the normal.
     *
     * @param p
     *
     * @return
     */
    @Override
    public Normal getNormal(Point3D p) {
        return obj.getNormal(p);
    }

    @Override
    public Material getMaterial() {
        return obj.getMaterial(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BBox getBoundingBox() {
        return obj.getBoundingBox(); //To change body of generated methods, choose Tools | Templates.
    }

}
