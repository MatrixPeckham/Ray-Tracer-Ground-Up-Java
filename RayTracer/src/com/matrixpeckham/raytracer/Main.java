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

import com.matrixpeckham.raytracer.build.BuildBBCoverPic;
import com.matrixpeckham.raytracer.build.BuildMultipleObjects;
import com.matrixpeckham.raytracer.build.BuildShadedObjects;
import com.matrixpeckham.raytracer.build.BuildSingleSphere;
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
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.Timer;

/**
 * functions:
 * start
 * pause 
 * resume
 * open
 * save
 * quit
 * @author William Matrix Peckham
 */
public class Main extends JFrame implements ActionListener{
    JMenuBar bar;
    JMenuItem startButton;
    JMenuItem openButton;
    JMenuItem saveButton;
    JMenuItem quitButton;
    JMenuItem z1Button;
    JMenuItem z2Button;
    JMenuItem z4Button;
    JMenuItem z8Button;
    JMenuItem z16Button;
    JLabel statusBar;
    BufferedImage image;
    ImageViewComponent imageComponent;
    private BlockingQueue<RenderPixel> queue = new LinkedBlockingQueue<RenderPixel>();
    
    public Timer updateTimer;
    
    public RayTraceThread thread = null;
    
    BuildWorldFunction builder = new BuildShadedObjects();
    
    int pixelsRendered = 0;
    int pixelsToRender = 0;
    private long startTime;
    
    public Main(){
        menuBar();
        statusBar=new JLabel();
        updateTimer=new Timer(40, this);
        updateTimer.setRepeats(true);
        imageComponent = new ImageViewComponent();
        
        this.setJMenuBar(bar);
        this.add(statusBar,BorderLayout.SOUTH);
        this.add(new JScrollPane(imageComponent));
        this.pack();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void menuBar() {
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
    
    public void start(){
        World w = new World();
        statusBar.setText("Building World...");
        builder.build(w);
        w.setQueue(queue);
        statusBar.setText("Rendering...");
        pixelsRendered=0;
        pixelsToRender=w.vp.hRes*w.vp.vRes;
        image = new BufferedImage(w.vp.hRes, w.vp.vRes, BufferedImage.TYPE_INT_ARGB);
        BufferedImage check = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)check.getGraphics();
        g.setColor(new Color(0x666666));
        g.fillRect(0, 0, 16, 16);
        g.setColor(new Color(0x999999));
        g.fillRect(8, 0, 8, 8);
        g.fillRect(0, 8, 8, 8);
        g=(Graphics2D)image.getGraphics();
        Rectangle r = new Rectangle(0,0,16,16);
        g.setPaint(new TexturePaint(check, r));
        g.fill(new Rectangle(0,0,w.vp.hRes,w.vp.vRes));
        imageComponent.setImage(image);
        
        startTime = System.currentTimeMillis();
        
        
        thread = new RayTraceThread(w);
        
        updateTimer.start();
        
        thread.start();
    }
        
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
    
    public void quit(){
        System.exit(0);
    }
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Main m = new Main();
        m.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        while(!queue.isEmpty()){
            try{
                RenderPixel pix = queue.take();
                image.setRGB(pix.x, pix.y, pix.color);
                pixelsRendered++;
            } catch(InterruptedException ex){}
        }
        double completed = (double)pixelsRendered/(double)pixelsToRender;
        if(completed==1){
            updateTimer.stop();
        }
        String str = "Rendering... "+((int)completed*100)+"%";
        long time = System.currentTimeMillis();
        long diff = time-startTime;
        double remaining = 1-completed;
        long msRemain = (long)((diff/(completed*100))*100*remaining);
        String timeremStr = msRemain/1000f + " seconds remain ";
        String timeStr = diff/1000f + " seconds done ";
        if(msRemain>=0){
            statusBar.setText(str+timeStr + timeremStr);
        } else {
            statusBar.setText(str+timeStr);
        }
        imageComponent.repaint();
    }
    
}
