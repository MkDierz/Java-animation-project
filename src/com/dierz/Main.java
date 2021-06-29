package com.dierz;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Main extends JApplet implements Runnable {
    Thread animator; // Init var for Threading
    BufferedImage img; // init var for buffer reduce flicker
    Graphics g2; // init var for Graphic

    int pan_cam, building_pres, dark_val, sun_position; // init val animation related
    float dash_inc; // init var for road strip change
    Color window_color; // init val color for window

    int skyR, skyG, skyB; // init for rgb sky color
    Color skyColor; // inirt var for sky color

    boolean starStatus; // init var fot star appreance status
    int starOpacity; // init var for star opacity value
    Color starColor; // init val for star color change

    int[][] star; // init var for storiing random coordinate valure generated for star

    Random rand = new Random(); // creating new random object for generating random star coordinates

    int cloudX = 800,cloudY = 100; // init var and value for cloud position

    public void init() {
        JRootPane root = this.getRootPane(); // using JrootPane
        root.putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);
    }

    public void start() {
        img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB); //creating new bufeer objcet uner name img
        g2 = img.createGraphics(); // embeding buffered image to grapic component
        animator = new Thread(this); // init thread new object under name animator
        pan_cam  = 0;// init val for pancam variable
        dash_inc = 10;// init value for road line movement
        building_pres = 0;// init value for building movement
        dark_val = 200;// init value for dark light shade
        window_color = Color.yellow;//init color for window
        sun_position = 220;//init sun position
        animator.start();//starting a thread

        skyR = 0;// init value for sky red value
        skyG = 0;// init value for sky green value
        skyB = 20;// init value for sky blue value
        skyColor = new Color(skyR, skyG, skyB); //create new object for sky color

        star = new int[1][];//creeating array var for star random generated location
        starStatus = true;//set status for activating star
        starOpacity = 255;//set opacitiy value for star
        starColor = new Color(255,255,255,starOpacity);//create new object for star color including opacity value
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Animasi UTS");// create new object jframe under name of animasi uts
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JApplet applet = new Main();//creating japplet new objecty under name of main
        f.getContentPane().add(applet);//adding applet to pane
        applet.init();//init applet
        f.pack();//packing applet
        f.setSize(new Dimension(800, 900));//set size for applet window
        f.setVisible(true);//set visible for window
        applet.start();//starting an applet
    }


    @Override
    public void stop() {
        animator = null;
    }//function for diactivating animation

    public void paint(Graphics graphics) {//paint function

        g2.setColor(skyColor);//set color by sky color
        g2.fillRect(0, 0, getWidth(), getHeight());//creating rectangle as a sky

        if (starStatus){// contition for star display
            stars(g2,60,getWidth(),200,starColor);// call function star for displaying star
        }

        g2.setColor(Color.yellow);//set color for sun
        g2.fillOval(100,sun_position,150,150);//cerating oval for sun

        drawCloud(g2,cloudX,cloudY);// function for creatiing cloud

        g2.setColor(new Color(46, 199, 158));//set color for mountain
        g2.fillOval(400,50-200+pan_cam,600,700);//creating oval for mountain

        g2.setColor(new Color(0,0,0,dark_val));//set color including opacity for night shade
        g2.fillOval(400,50-200+pan_cam,600,700);//create new mountauin over for night shade

        g2.setColor(new Color(52, 224, 178));//base color foe mountain
        g2.fillOval(0,160-200+pan_cam,500,300);//creating new mountain using oval

        g2.setColor(new Color(0, 0, 0,dark_val));//set color including opacity for night shade
        g2.fillOval(0,160-200+pan_cam,500,300);//create new mountauin over for night shade

        g2.setColor(new Color(52, 207, 116));//set color for grass
        g2.fillRect(0, 100+pan_cam, getWidth(), getHeight() );//create rectangle as grass

        g2.setColor(Color.black);//set color for road
        trapezoidal(90,200,900, 700/2,100+pan_cam);//create road using function trapezodial

        g2.setColor(Color.white);//set color for road dash line
        drawDashedLine(g2, 430,900,430,100+pan_cam);//create road dash line using fucntion draw dashline

        g2.setColor(new Color(209, 78, 42));//set color for building
        g2.fillRect(200,110+building_pres,100,210-building_pres);//drawing building using rect

        g2.setColor(new Color(240, 91, 50));// set color for buliding
        g2.fillRect(200,320,100,150-200+building_pres);//create building by using rect

        g2.setColor(new Color(0, 0, 0,dark_val));//set color including opacity for night shade
        g2.fillRect(0, 100+pan_cam, getWidth(), getHeight() );//set rect for nightshade

        g2.setColor(window_color);// set window color
        for (int i = 210; i <= 270; i+=30) {// creating window by using rect and a loop for every floor
            for (int j = 330; j <= 330+(30*2); j+=30) {
                g2.fillRect(i,j,20,20-200+building_pres);
            }
        }
        graphics.drawImage(img, 0, 0, this);//executing all paint componet to a thread
    }

    @Override
    public void run() {// this always executed bedause runnable 
        while (Thread.currentThread() == animator) {//check conniti
            if (pan_cam < 200) {
                pan_cam += 1;
                dash_inc += 0.05;
                building_pres += 1;
            }else if (dark_val > 0) {
                dark_val-=1;
                sun_position-=1;
                if (skyR > 190) {
                    skyR++;
                }
                if (skyG < 220) {
                    skyG++;
                }
                if (skyB < 240) {
                    skyB++;
                }
                if (starOpacity > 20) {
                    starOpacity-=20;
                } else {
                    starStatus = false;
                }
            }else if(sun_position <= 20){
                window_color = Color.gray;
                cloudX-=3;
            } else {
                stop();
            }
            starColor = new Color(255,255,255,starOpacity);
            skyColor = new Color(skyR, skyG, skyB);
            repaint();
            try {
                Thread.sleep(30); //time in milliseconds
            } catch (Exception e) {
                break;
            }
        }
    }

    public void trapezoidal(int top, int bottom,int height,int x,int y) {
        int y2 = height+y;
        int y3 = height+y;

        int x1 = bottom - ((bottom-top)/2)-15+x;
        int x2 = bottom-15+ x;
        int x3 = -15 + x;
        int x4 = (bottom-top)/2-15+x;
        int[] x_points = new int [] {x1,x2,x3,x4};
        int[] y_points = new int [] {y,y2,y3, y};
        g2.fillPolygon(x_points,y_points, 4);
    }

    public void drawDashedLine(Graphics g, int x1, int y1, int x2, int y2){
        // Create a copy of the Graphics instance
        Graphics2D g2d = (Graphics2D) g.create();
        // Set the stroke of the copy, not the original
        Stroke dashed = new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[]{dash_inc}, 0);
        g2d.setStroke(dashed);
        // Draw to the copy
        g2d.drawLine(x1, y1, x2, y2);
        // Get rid of the copy
        g2d.dispose();
    }

    public void stars(Graphics g,int count,int x,int y,Color color) {

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(color);
        if (star[0] == null) {
            star = new int[2][count];
            star[0] = randGen(count,x);
            star[1] = randGen(count,y);
        }
        for (int i = 0; i < count; i++) {
            drawStars(g,color, star[0][i], star[1][i]);
        }
    }

    public void drawStars(Graphics g, Color Color,int x,int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color);

        int x1 = x-5;
        int x2 = x-1;
        int x3 = x;
        int x4 = x+1;
        int x5 = x+5;
        int x6 = x-1;
        int x7 = x;
        int x8 = x+1;

        int y1 = y-5;
        int y2 = y-4;
        int y3 = y;
        int y4 = y-4;
        int y5 = y-5;
        int y6 = y-6;
        int y7 = y-10;
        int y8 = y-6;

        int[] x_points = new int [] {x1,x2,x3,x4,x5,x6,x7,x8};
        int[] y_points = new int [] {y1,y2,y3,y4,y5,y6,y7,y8};

        g2d.fillPolygon(x_points,y_points, 8);
    }

    public void drawCloud (Graphics g2, int cloudX, int cloudY) {
        g2.setColor(Color.white);
        g2.fillOval(cloudX, cloudY, 50, 60);
        g2.fillOval(cloudX + 15, cloudY - 25, 70, 80);
        g2.fillOval(cloudX + 30, cloudY + 30, 70, 50);
        g2.fillOval(cloudX + 60, cloudY, 80, 60);
        g2.fillOval(cloudX + 50, cloudY - 30, 60, 40);
        g2.fillOval(cloudX + 80, cloudY - 20, 70, 60);
        g2.fillOval(cloudX + 80, cloudY + 20, 70, 60);
        g2.fillOval(cloudX + 100, cloudY, 70, 60);
    }

    /**
     * @param length input for length of the array
     * @param limit the limit for random digit
     * @return random array of specified length
     */
    public int[] randGen(int length,int limit) {
        int[] arr = new int[length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rand.nextInt(limit); // storing random integers in an array
        }
        System.out.println("rand generated");
        return arr;
    }
}
