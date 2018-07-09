import javax.swing.JFrame;
import java.util.TimerTask;
import java.util.Timer;

public class GameOfLifeDriver extends TimerTask
{
   static GameOfLife panel = new GameOfLife();
   public void main(String[] args)
   {
      JFrame frame = new JFrame("Conway's Game of Life");
      frame.setSize(400,400);
      frame.setLocation(100,50);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setContentPane(panel);
      frame.setVisible(true);
      Timer timer = new Timer();
      timer.schedule(this, 0, 0250);
   }
   public void run()
   {  
      panel.repaint();
      panel.updateWorld();
   }
}