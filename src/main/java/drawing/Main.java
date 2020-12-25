package drawing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends JFrame
{

    public Main()
    {
        
        initComponents();
        
    }
    
    public void initComponents()
    {
        
        this.setTitle("image animation");
        this.setBounds(250, 300, 300, 250);
        animationPanel.setBackground(Color.BLUE);
        JButton buttonStart = (JButton)buttonsPanel.add(new JButton("generuj nowego Naymana"));
        
        buttonStart.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                
                startAnimation();
                
            }
        });
        
        JButton buttonStop = (JButton)buttonsPanel.add(new JButton("Wyczyœæ armiê Naymanów"));
        
        buttonStop.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                
                stopAnimation();
                
            }
        });
        
        this.getContentPane().add(animationPanel);
        this.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
    
    public void startAnimation()
    {
        
        animationPanel.addImage();
        
    }
    
    public void stopAnimation()
    {
        
        animationPanel.stop();
        
    }
    
    private JPanel buttonsPanel = new JPanel();
    private AnimationPanel animationPanel = new AnimationPanel();
    
    public static void main(String[] args) 
    {
        
        new Main().setVisible(true);
        
    }       
    
    class AnimationPanel extends JPanel
    {
        
        public void addImage() 
        {
            
            listOfImages.add(new Imager());
            thread = new Thread(threadGroup, new ImageRunnable((Imager)listOfImages.get(listOfImages.size()-1)));
            thread.start();
                        
            threadGroup.list();
            
        }
        
        public void stop()
        {
            
            threadGroup.interrupt();
            
        }
        
        @Override
        public void paintComponent(Graphics g)
        {
            
            super.paintComponent(g);
            
            for(int i = 0; i < listOfImages.size(); i++)
            {
                
                g.drawImage(Imager.getImg(), ((Imager)listOfImages.get(i)).x, ((Imager)listOfImages.get(i)).y, null);
                
            }
            
        }
        
        ArrayList listOfImages = new ArrayList();
        JPanel thisPanel = this;
        Thread thread;
        ThreadGroup threadGroup = new ThreadGroup("Group of images");
        
        public class ImageRunnable implements Runnable
        {

            public ImageRunnable(Imager image)
            {
                
                this.image = image;
                
            }
            
            @Override
            public void run() 
            {
                
                try
                {
                
                    while(!Thread.currentThread().isInterrupted())
                    {

                        this.image.moveImage(thisPanel);
                        repaint();


                        Thread.sleep(5);

                    }

                }    
                        
                catch(InterruptedException ex)
                {

                    System.out.println(ex.getMessage());
                    listOfImages.clear();
                    repaint();

                }
                   
            }
            
            Imager image;
            
        }
            
    }
}


class Imager
{
    
    public static Image getImg()
    {
        
        return Imager.image;
        
    }
    
    public void moveImage(JPanel container)
    {
        
        Rectangle bordersOfPanel = container.getBounds();
        
        x += dx;
        y += dy;
        
        if(y + y_img >= bordersOfPanel.getMaxY())
        {
            
            y = (int)(bordersOfPanel.getMaxY()-y_img);
            dy = -dy;
            
        }
        
        if(x + x_img >= bordersOfPanel.getMaxX())
        {
            
            x = (int)(bordersOfPanel.getMaxX()-x_img);
            dx = -dx;
            
        }
        
        if(y < bordersOfPanel.getMinY())
        {
            
            y = (int)bordersOfPanel.getMinY();
            dy = -dy;
            
        }
        
        if(x < bordersOfPanel.getMinX())
        {
            
            x = (int)bordersOfPanel.getMinX();
            dx = -dx;
            
        }
        
    }
    
    public static Image image = new ImageIcon("nejmen.png").getImage().getScaledInstance(100, 200, Image.SCALE_SMOOTH);
    
    int x = 0;
    int y = 0;
    int dx = 1;
    int dy = 1;
    int x_img = image.getWidth(null);
    int y_img = image.getHeight(null);
    
}