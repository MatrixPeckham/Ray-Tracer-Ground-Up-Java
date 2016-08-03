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
package com.matrixpeckham.raytracer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

/**
 * Simple component to display a BufferedImage at varying zooms.
 *
 * @author William Matrix Peckham
 */
class ImageViewComponent extends JComponent {

    /**
     * image to display.
     */
    transient BufferedImage image = null;

    /**
     * zoom level
     */
    private int zoom = 1;

    /**
     * default constructor, sets a default size for the component.
     */
    public ImageViewComponent() {
        //set the size and preferred size to play nice with scrollpane.
        this.setPreferredSize(new Dimension(800, 600));
        this.setSize(800, 600);
    }

    /**
     * Sets a new zoom level for this component.
     *
     * @param i
     */
    public void setZoomLevel(int i) {
        //sets the zoom level
        zoom = i;

        //sets width and height
        int w = 800;
        int h = 600;
        if (image != null) {
            w = image.getWidth();
            h = image.getHeight();
        }
        //our component and display size is the zoom and image size multiplied.
        this.setPreferredSize(new Dimension(w * zoom, h * zoom));
        this.setSize(w * zoom, h * zoom);
        //fire repaint
        repaint();
    }

    /**
     * Sets the image to display.
     *
     * @param image
     */
    public void setImage(BufferedImage image) {
        this.image = image;
        //sets the size according to the zoom and image size
        this.setPreferredSize(new Dimension(image.getWidth() * zoom, image.
                getHeight() * zoom));
        this.setSize(image.getWidth() * zoom, image.getHeight() * zoom);
    }

    /**
     * paint override
     *
     * @param g3
     */
    @Override
    public void paint(Graphics g3) {
        if (g3 instanceof Graphics2D) {
            Graphics2D g2 = (Graphics2D) g3;
            super.paint(g2);
            //if we have an image simply draw it at the size it would be zoomed.
            //otherwise we fill the viewport with checker pattern see Main for more
            //comments on the checker pattern.
            if (image != null) {
                g2.drawImage(image, 0, 0, image.getWidth() * zoom, image.
                        getHeight() * zoom, 0, 0, image.getWidth(), image.
                        getHeight(), null);
            } else {
                BufferedImage check = new BufferedImage(16, 16,
                        BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = (Graphics2D) check.getGraphics();
                g.setColor(new Color(0x666666));
                g.fillRect(0, 0, 16, 16);
                g.setColor(new Color(0x999999));
                g.fillRect(8, 0, 8, 8);
                g.fillRect(0, 8, 8, 8);
                Rectangle r = new Rectangle(0, 0, 16, 16);
                g2.setPaint(new TexturePaint(check, r));
                g2.fill(new Rectangle(0, 0, getWidth(), getHeight()));

            }
        }
    }

}
