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

import com.matrixpeckham.raytracer.build.ReflectiveWithArea;
import com.matrixpeckham.raytracer.build.figures.ch18.BuildFigure04;
import com.matrixpeckham.raytracer.build.figures.ch24.BuildFigure07;
import com.matrixpeckham.raytracer.build.figures.ch24.BuildFigure10;
import com.matrixpeckham.raytracer.build.figures.ch24.BuildFigure11;
import com.matrixpeckham.raytracer.util.BruteForceSolver;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.Timer;

/**
 * Main class for the ray tracer program, contains the swing code for the GUI.
 * @author William Matrix Peckham
 */
public class Main extends JFrame implements ActionListener{
    /**
     * Menu bar.
     */
    JMenuBar bar;
    /**
     * Start button for starting render.
     */
    JMenuItem startButton;
    /**
     * Open button, for opening an image file.
     */
    JMenuItem openButton;
    /**
     * Save button for saving a rendered image to PNG file.
     */
    JMenuItem saveButton;
    /**
     * Exit Button.
     */
    JMenuItem quitButton;
    /**
     * Zoom 1x button.
     */
    JMenuItem z1Button;
    /**
     * Zoom 2x button.
     */
    JMenuItem z2Button;
    /**
     * Zoom 4x button.
     */
    JMenuItem z4Button;
    /**
     * Zoom 8x button.
     */
    JMenuItem z8Button;
    /**
     * Zoom 16x button.
     */
    JMenuItem z16Button;
    /**
     * Status bar for the bottom of the pane.
     */
    JLabel statusBar;
    /**
     * Image to show.
     */
    BufferedImage image;
    /**
     * Component to show.
     */
    ImageViewComponent imageComponent;
    /**
     * Thread synch queue for rendering. Holds the pixels.
     */
    private BlockingQueue<RenderPixel> queue = new LinkedBlockingQueue<RenderPixel>();
    /**
     * Swing timer for updating the image often. 
     */
    public Timer updateTimer;
    /**
     * Thread for rendering. 
     */
    public RayTraceThread thread = null;
    /**
     * The builder that will create the scene we render.
     */
//    BuildWorldFunction builder = new com.matrixpeckham.raytracer.build.figures.ch14.BuildFigure15();
    BuildWorldFunction builder = new BuildFigure11();
    /**
     * Number of pixels that have been rendered.
     */
    int pixelsRendered = 0;
    /**
     * Number of pixels that we need to render in total.
     */
    int pixelsToRender = 0;
    /**
     * Time we started rendering. 
     */
    private long startTime;
    
    /**
     * Default constructor.  
     */
    public Main(){
        //make the menu bar
        menuBar();
        
        statusBar=new JLabel();
        
        updateTimer=new Timer(40, this);
        
        updateTimer.setRepeats(true);
        
        imageComponent = new ImageViewComponent();
        
        //standard swing layout and frame stuff
        this.setJMenuBar(bar);
        this.add(statusBar,BorderLayout.SOUTH);
        //put the image viewing component in a scroll pane for scrolling.
        this.add(new JScrollPane(imageComponent));
        this.pack();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void menuBar() {
        //standard swing menu bar generation.
        bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu render = new JMenu("Render");
        JMenu zoom = new JMenu("Zoom");
        startButton=new JMenuItem("Start");
        openButton=new JMenuItem("Open");
        saveButton=new JMenuItem("Save");
        quitButton=new JMenuItem("Quit");
        z1Button=new JMenuItem("1x");
        z2Button=new JMenuItem("2x");
        z4Button=new JMenuItem("4x");
        z8Button=new JMenuItem("8x");
        z16Button=new JMenuItem("16x");
        
        //standard action listeners simply call appropriate methods
        startButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                start();
            }
        });
        openButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                open();
            }
        });
        saveButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        quitButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                quit();
            }
        });
        z1Button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                imageComponent.setZoomLevel(1);
            }
        });
        z2Button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                imageComponent.setZoomLevel(2);
            }
        });
        z4Button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                imageComponent.setZoomLevel(4);
            }
        });
        z8Button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                imageComponent.setZoomLevel(8);
            }
        });
        z16Button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                imageComponent.setZoomLevel(16);
            }
        });
        
        //standard menu bar creation.
        file.add(openButton);
        file.add(saveButton);
        file.add(quitButton);
        render.add(startButton);
        zoom.add(z1Button);
        zoom.add(z2Button);
        zoom.add(z4Button);
        zoom.add(z8Button);
        zoom.add(z16Button);
        bar.add(file);
        bar.add(render);
        bar.add(zoom);
    }
    
    /**
     * called from the start menu button.
     */
    public void start(){
        //create a new world
        World w = new World();
        statusBar.setText("Building World...");
        //generate the world
        builder.build(w);
        //world needs a reference to the render queue
        w.setQueue(queue);
        statusBar.setText("Rendering...");
        
        //sets up the pixel counts for this image
        pixelsRendered=0;
        
        //image size may be different than resolutions, but only stereo causes that
        
        int iwidth=w.vp.imageWidth!=null?w.vp.imageWidth:w.vp.hRes;
        int iheight=w.vp.imageHeight!=null?w.vp.imageHeight:w.vp.vRes;
        //this is after we build the world so hRes and vRes will be set already.
        pixelsToRender=iwidth*iheight;
        
        //creates the image and fills it with a grey checkerboard pattern.
        image = new BufferedImage(iwidth, iheight, BufferedImage.TYPE_INT_ARGB);
        //image for a 16x16 image of 2x2, 8x8px checkers drawn with standard Graphics
        BufferedImage check = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)check.getGraphics();
        g.setColor(new Color(0x666666));
        g.fillRect(0, 0, 16, 16);
        g.setColor(new Color(0x999999));
        g.fillRect(8, 0, 8, 8);
        g.fillRect(0, 8, 8, 8);
        g=(Graphics2D)image.getGraphics();
        //use the checker image as a repeated texture on the traced image.
        Rectangle r = new Rectangle(0,0,16,16);
        g.setPaint(new TexturePaint(check, r));
        g.fill(new Rectangle(0,0,w.vp.hRes,w.vp.vRes));
        
        //pass the image component the image.
        imageComponent.setImage(image);
        
        //save start time
        startTime = System.currentTimeMillis();
        
        //create and start thread and timer
        thread = new RayTraceThread(w);
        
        updateTimer.start();
        
        thread.start();
    }

    /**
     * open function, opens an image file and sets the component to render it.
     */
    public void open(){
        JFileChooser chooser = new JFileChooser();
        int b = chooser.showOpenDialog(this);
        if(b==JFileChooser.APPROVE_OPTION){
            try{
                File f = chooser.getSelectedFile();
                image = ImageIO.read(f);
                imageComponent.setImage(image);
            } catch(IOException ex){}
        }
    }

    /**
     * simply saves the image to a file. 
     */
    public void save(){
        JFileChooser chooser = new JFileChooser();
        int b = chooser.showSaveDialog(this);
        if(b==JFileChooser.APPROVE_OPTION){
            try{
                File f = chooser.getSelectedFile();
                ImageIO.write(image, "PNG", f);
            } catch(IOException ex){}
        }
    }
    /**
     * quit
     */
    public void quit(){
        System.exit(0);
    }
    
    
    
    /**
     * Main Method for the program, creates and displays the window.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Main m = new Main();
        m.setVisible(true);
        //BruteForceSolver solver = new BruteForceSolver(0.999999999, -24.591812430, 226.372338960,
        //        -924.44347730, 1412.89015972);
        BruteForceSolver solver = new BruteForceSolver(1, -399.98945581744084, 59996.5311948078,
                -3999622.5653444515, 9.998640035999998E7);
        double[] d = {0,0,0,0};
        int roots = solver.solveQuartic(d);
    }

    /**
     * Action Performed method for the timer. updates the image. 
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //if we have new pixels to render, set the proper pixels in the image.
        while(!queue.isEmpty()){
            try{
                RenderPixel pix = queue.take();
                image.setRGB(pix.x, pix.y, pix.color);
                pixelsRendered++;
            } catch(InterruptedException ex){}
        }
        //compute the completed ratio.
        double completed = (double)pixelsRendered/(double)pixelsToRender;
        if(completed==1){
            updateTimer.stop();
        }
        
        //update the status bar for the percent complete and timing. 
        String str = "Rendering... "+((int)(completed*100))+"%";
        
        long time = System.currentTimeMillis();
        //elapsed time
        long diff = time-startTime;
        //percent left
        double remaining = 1-completed;
        //approximate time left
        long msRemain = (long)((diff/(completed*100))*100*remaining);
        String timeremStr = msRemain/1000f + " seconds remain ";
        String timeStr = diff/1000f + " seconds done ";
        
        //set the status. 
        if(msRemain>=0){
            statusBar.setText(str+timeStr + timeremStr);
        } else {
            statusBar.setText(str+timeStr);
        }
        //fire repaint of the component.
        imageComponent.repaint();
    }
    
}
