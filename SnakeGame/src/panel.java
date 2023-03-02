import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Random;

import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_R;

public class panel extends JPanel implements ActionListener{

    static int width = 1200;
    static int hight = 600;
    static int unit = 50;
    int totunit = (width*hight)/unit;
    int score;
    int length = 3;
    int fx,fy;
    boolean flag = false;
    char dir = 'R';
    Random random;
    Timer timer;
    static int DELAY = 160;
    int[] xsnake = new int[totunit];
    int[] ysnake = new int[totunit];
    panel()
    {
        this.setPreferredSize(new Dimension(width,hight));
        this.setBackground(Color.BLACK);
        this.addKeyListener(new MyKey());
        this.setFocusable(true);
        random = new Random();

        gamestart();
    }



    public void gamestart()
    {
        flag = true;
        spawnfood();
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void spawnfood()
    {
        fx = random.nextInt((int) width/unit) * unit;
        fy = random.nextInt((int) hight/unit) * unit;
    }

    public void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);
        draw(graphics);
    }
    public void draw(Graphics graphics)
    {
        if(flag)
        {
            graphics.setColor(Color.ORANGE);
            graphics.fillOval(fx,fy,unit,unit);

            for(int i=0;i<length;i++)
            {
                if(i==0)
                {
                    graphics.setColor(Color.RED);
                    graphics.fillRect(xsnake[0],ysnake[0],unit,unit);
                }
                else {
                    graphics.setColor(Color.GREEN);
                    graphics.fillRect(xsnake[i],ysnake[i],unit,unit);
                }
            }
            //Score display on a Screen
            graphics.setColor(Color.GRAY);
            graphics.setFont(new Font("Comic Sans",Font.BOLD,40));
            FontMetrics fm = getFontMetrics(graphics.getFont());
            graphics.drawString("Score: "+score,(width - fm.stringWidth("Score: "+score))/2, graphics.getFont().getSize());

        }
        else{
            gameOver(graphics);
        }
    }

    public void gameOver(Graphics graphics)
    {
        //for display score after game is over
        graphics.setColor(Color.GRAY);
        graphics.setFont(new Font("Comic Sans",Font.BOLD,40));
        FontMetrics fm = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: "+score,(width - fm.stringWidth("Score: "+score))/2, graphics.getFont().getSize());

        //for display Game Over after game is over
        graphics.setColor(Color.RED);
        graphics.setFont(new Font("Comic Sans",Font.BOLD,80));
        FontMetrics fm1 = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over",(width - fm1.stringWidth("Game Over"))/2, hight/2);

        //for display press R to Replay after game is over
        graphics.setColor(Color.GREEN);
        graphics.setFont(new Font("Comic Sans",Font.BOLD,40));
        FontMetrics fm2 = getFontMetrics(graphics.getFont());
        graphics.drawString("Press R to Replay",(width - fm2.stringWidth("Press R to Replay"))/2, (hight/2)+150);
    }

    public void move()
    {
        // for body to move
        for(int i=length;i>0;i--)
        {
            xsnake[i] = xsnake[i-1];
            ysnake[i] = ysnake[i-1];
        }
        // this for only head
        switch (dir)
        {
            case 'U':
                ysnake[0] = ysnake[0] - unit;
                break;
            case 'D':
                ysnake[0] = ysnake[0] + unit;
                break;
            case 'L':
                xsnake[0] = xsnake[0] - unit;
                break;
            case 'R':
                xsnake[0] = xsnake[0] + unit;
                break;
        }
    }
    public void check()
    {
        // if snack can hit the body or not
        for(int i=length;i>0;i--)
        {
            if((xsnake[0] == xsnake[i]) && (ysnake[0] == ysnake[i]))
            {
                flag = false;
            }
        }

        // if snack head can tuch to wall or not
        if(xsnake[0] < 0)
        {
            flag = false;
        }
        else if(xsnake[0] > width)
        {
            flag = false;
        }
        else if(ysnake[0] < 0)
        {
            flag = false;
        }
        else if(ysnake[0] > hight)
        {
            flag = false;
        }

        if(flag == false)
        {
            timer.stop();
        }
    }

    public void foodeaten()
    {
        if((xsnake[0] == fx) && (ysnake[0] == fy))
        {
            length++;
            score++;
            spawnfood();
        }
    }
    public class MyKey extends KeyAdapter
    {
        public void keyPressed(KeyEvent k)
        {
            switch(k.getKeyCode())
            {
                case VK_UP:
                    if(dir != 'D')
                    {
                        dir = 'U';
                    }
                    break;
                case VK_DOWN:
                    if(dir != 'U')
                    {
                        dir = 'D';
                    }
                    break;
                case VK_LEFT:
                    if(dir != 'R')
                    {
                        dir = 'L';
                    }
                    break;
                case VK_RIGHT:
                    if(dir != 'L')
                    {
                        dir = 'R';
                    }
                    break;
                case VK_R:
                    if(flag == false)
                    {
                        score = 0;
                        length = 3;
                        dir = 'R';
                        Arrays.fill(xsnake,0);
                        Arrays.fill(ysnake,0);
                        gamestart();
                    }
                    break;
            }
        }
    }
    public void actionPerformed(ActionEvent e)
    {
        if(flag)
        {
            move();
            foodeaten();
            check();
        }
        //explicitly call the paintComponent function
        repaint();
    }
}
