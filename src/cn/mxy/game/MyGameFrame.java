package cn.mxy.game;


import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;


public class MyGameFrame extends Frame {

    Image planeImg = GameUtil.getImage("images/plane.png");
    Image bg = GameUtil.getImage("images/bg.jpg");
    Explode bao;
    int period;
    Shell[] shells = new Shell[50];
    Plane plane = new Plane(planeImg,250,250);
    Date startTime = new Date();
    Date endTime;
    @Override
    public void paint(Graphics g){
        super.paint(g);
        g.drawImage(bg,0,0,null);
        plane.drawSelf(g);
        for(int i = 0; i < shells.length; i++){
            shells[i].draw(g);
            boolean peng = shells[i].getRect().intersects(plane.getRect());
            if(peng){
                plane.live = false;
                if(bao == null) {
                    bao = new Explode(plane.x, plane.y);
                    endTime = new Date();
                    period = (int)((endTime.getTime() - startTime.getTime())/1000);
                }

                bao.draw(g);

            }
        }

    }

    private Image offScreenImage = null;
    class KeyMonitor extends KeyAdapter{

        @Override
        public void keyPressed(KeyEvent e) {
            plane.addDirection(e);

        }

        @Override
        public void keyReleased(KeyEvent e) {
            plane.minusDirection(e);
        }
    }
    public void update(Graphics g) {
        if(offScreenImage == null)
            offScreenImage = this.createImage(Constant.GAME_WIDTH,Constant.GAME_HEIGHT);//这是游戏窗口的宽度和高度

        Graphics gOff = offScreenImage.getGraphics();
        paint(gOff);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    class PaintThread extends Thread{
        @Override
        public void run() {
            super.run();
            while(true){
                repaint(); //重画


                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    /**
     * 初始化窗口
     */
    public void launchFrame(){
        this.setTitle("马骁勇作品");
        this.setVisible(true);
        this.setSize(Constant.GAME_WIDTH,Constant.GAME_HEIGHT);
        this.setLocation(300,300);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        addKeyListener(new KeyMonitor());
        new PaintThread().start();

        for(int i = 0; i < shells.length; i++){
            shells[i] = new Shell();
        }

    }

    public static void main(String[] args){
        MyGameFrame f = new MyGameFrame();
        f.launchFrame();
    }


}
