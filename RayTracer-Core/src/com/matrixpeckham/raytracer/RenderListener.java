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
package com.matrixpeckham.raytracer;

/**
 *
 * @author William Matrix Peckham
 */
public interface RenderListener {

    /**
     * Called when we start a render, passes the x resolution as width, and the
     * y resolution as height. Should create an image to render to.
     *
     * @param width
     * @param height
     */
    public void renderStarting(int width, int height);

    /**
     * Called each time a pixel is finished, must be thread safe, as it may be
     * called by several threads.
     *
     * @param pixel
     */
    public void newPixel(RenderPixel pixel);

    /**
     * called periodically, should be used to update user on progress.
     *
     * @param progress double from 0-1, percentage
     */
    public void progress(double progress);

    /**
     * called after the render is done, should be synchronized to avoid
     */
    public void renderFinished();

}
