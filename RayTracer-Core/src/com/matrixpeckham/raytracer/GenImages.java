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

import com.matrixpeckham.raytracer.build.BuildFigure16_multi_tracer;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

/**
 *
 * @author William Matrix Peckham
 */
public class GenImages {

    public static void main(String[] args) throws MalformedURLException,
            InstantiationException, IllegalAccessException {
        //use reflections api to get all buildworldfunction implementations
        File external = new File("./builders/");
        ArrayList<URL> jars = new ArrayList<>();
        jars.addAll(ClasspathHelper.forPackage(
                "com.matrixpeckham.raytracer.build"));
        jars.add(external.toURL());
        if (!(external.exists() && external.isDirectory())) {
            external.mkdir();
        } else {
            File[] files = external.listFiles((File f) -> {
                return f.getName().endsWith(".jar");
            });
            Arrays.stream(files).forEach((File f) -> {
                try {
                    jars.add(f.toURL());
                } catch (MalformedURLException ex) {
                    Logger.getLogger(Main.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
            });
        }
        ClassLoader currentThreadClassLoader
                = Thread.currentThread().getContextClassLoader();

        // Add the conf dir to the classpath
        // Chain the current thread classloader
        URLClassLoader urlClassLoader
                = new URLClassLoader(jars.toArray(new URL[jars.size()]),
                        currentThreadClassLoader);

        // Replace the thread classloader - assumes
        // you have permissions to do so
        Thread.currentThread().setContextClassLoader(urlClassLoader);
        Configuration config = ConfigurationBuilder.build(jars.toArray());
        Reflections refl = new Reflections(config);

        Set<Class<? extends BuildWorldFunction>> clss2 = refl.getSubTypesOf(
                BuildWorldFunction.class);
        System.out.println("Num Classes:" + clss2.size());
        Class<? extends BuildWorldFunction>[] clss
                = new Class[]{BuildFigure16_multi_tracer.class};
        String prefix
                = "C:/Users/Owner/Documents/GitHub/RayTracerWiki/Ray-Tracer-Ground-Up-Java.wiki/images/Samples/";
        int numDone = 0;
        for (Class<? extends BuildWorldFunction> cls : clss2) {
            World w = new World();
            BuildWorldFunction bwf = cls.newInstance();
            bwf.build(w);
            String name = bwf.getClass().getName();
            name = name.replaceAll("\\.", "/");
            String fname = prefix + name + ".png";
            int size = 200;
            double sized = size;
            double pH = sized / w.vp.hRes;
            double pV = sized / w.vp.vRes;
            double s = 1 / (pH < pV ? pH : pV);
            w.vp.hRes = size;
            w.vp.vRes = size;
            w.vp.s *= s;
            w.vp.maxDepth = 5;
            w.setRenderListener(new CreateFileRnderListener(fname));
            if (w.camera != null) {
                w.camera.renderScene(w);
            } else {
                System.err.println("SKIPPING: " + fname + " has no camera.");
            }
            numDone++;
            System.out.println(numDone + " out of " + clss2.size() + ": "
                    + ((double) numDone) / clss2.size() * 100 + "%");
        }
    }

    static class CreateFileRnderListener implements RenderListener {

        BufferedImage image;

        String filename;

        int counter = 0;

        public CreateFileRnderListener(String fname) {
            filename = fname;
        }

        @Override
        public void newPixel(RenderPixel pix) {
            synchronized (this) {
                image.setRGB(pix.x, pix.y, pix.color);
            }
        }

        @Override
        public void progress(double progress) {
            synchronized (this) {
                counter++;
                counter = counter % 100;
                if (counter % 100 == 25) {
                    System.out.println("File:" + filename + ": " + progress
                            * 100
                            + "%");

                }
            }
        }

        @Override
        public void renderFinished() {
            synchronized (this) {
                File file = new File(filename);
                File folder = file.getParentFile();
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                System.out.println("saving file:" + file.getAbsolutePath());
                try {
                    ImageIO.write(image, "PNG", file);
                } catch (IOException ex) {
                    Logger.getLogger(GenImages.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
            }
        }

        @Override
        public void renderStarting(int width, int height) {
            image
                    = new BufferedImage(width, height,
                            BufferedImage.TYPE_INT_ARGB);
        }

    }
}
