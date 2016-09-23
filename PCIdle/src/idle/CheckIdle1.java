package idle;


import java.awt.AWTException;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Date;

public class CheckIdle1 extends Thread {
    private Robot robot;
    private double threshHold = 0.05;
    private int activeTime;
    private int idleTime;
    private boolean idle;
    private Rectangle screenDimenstions;

    public CheckIdle1(int activeTime, int idleTime) {
        this.activeTime = activeTime;
        this.idleTime = idleTime;

        // Get the screen dimensions
        // MultiMonitor support.
        int screenWidth = 0;
        int screenHeight = 0;

        GraphicsEnvironment graphicsEnv = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        
        System.out.println("--------------------1------------------------------");
        if (GraphicsEnvironment.isHeadless()) {
            System.err.println("headless graphics environment detected");
            return;
        }
        PointerInfo info = MouseInfo.getPointerInfo();
        Point point = info.getLocation();       
        System.out.println("point  = "+point);
        System.out.println("--------------------1------------------------------");
        
        GraphicsDevice[] graphicsDevices = graphicsEnv.getScreenDevices();

        for (GraphicsDevice screens : graphicsDevices) {
            DisplayMode mode = screens.getDisplayMode();
            screenWidth += mode.getWidth();

            if (mode.getHeight() > screenHeight) {
                screenHeight = mode.getHeight();
            }
        }

        screenDimenstions = new Rectangle(0, 0, screenWidth, screenHeight);

        // setup the robot.
        robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e1) {
            e1.printStackTrace();
        }

        idle = false;
    }

    public void run() {
        while (true) {
            BufferedImage screenShot = robot
                    .createScreenCapture(screenDimenstions);

            try {
                Thread.sleep(idle ? idleTime : activeTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            BufferedImage screenShot2 = robot
                    .createScreenCapture(screenDimenstions);

            if (compareScreens(screenShot, screenShot2) < threshHold) {
                idle = true;
                System.out.println("idle "+new Date());
                System.out.println("width "+screenShot.getWidth()+" height = "+screenShot2.getHeight());                
        
            } else {
                idle = false;
                System.out.println("active "+new Date());
                System.out.println("width "+screenShot.getWidth()+" height = "+screenShot2.getHeight());                
            }
        }
    }

    private double compareScreens(BufferedImage screen1, BufferedImage screen2) {
        int counter = 0;
        boolean changed = false;

        // Count the amount of change.
        for (int i = 0; i < screen1.getWidth() && !changed; i++) {
            for (int j = 0; j < screen1.getHeight(); j++) {
                if (screen1.getRGB(i, j) != screen2.getRGB(i, j)) {
                    counter++;
                }
    //            System.out.println("width = "+screen1.getWidth()+" height = "+screen1.getHeight()+new Date());
            }
        }

        return (double) counter
                / (double) (screen1.getHeight() * screen1.getWidth()) * 100;
    }

    public static void main(String[] args) {
        CheckIdle1 idleChecker = new CheckIdle1(20000, 1000);
        idleChecker.run();
    }
}