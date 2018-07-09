/*
BSD 3-Clause License

Copyright (c) 2018, AHFriedman
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of the copyright holder nor the names of its
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
import java.io.*;
import java.util.*;
import java.lang.*;
import javax.swing.*;
import java.awt.*;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GameOfLife extends JPanel implements MouseListener
{
//https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life
   public static byte[][] blocks;
   public static byte[][] nextGen;
   public static final byte ALIVE = 1;
   public static final byte DEAD = 0;
   public static int size;
   public static int generations = 0;
   public static int population = 0;
   public static int[] populations = new int[generations];
   
   public static int born = 3;
   public static int survives = 2;
   
   public static boolean willColorCode = true;
   public static byte[][] neighbors; 
   
   public static String option = "";
   
   public GameOfLife()
   {
      addMouseListener( this );
   }
   public static void main(String[] args)
   {
      
      Scanner input = new Scanner(System.in);
      System.out.println("Enter world size (int): ");
      size = input.nextInt();
      
      System.out.println("Enter the number of living neighbors a cell needs to become ALIVE: ");
      born = input.nextInt();
      System.out.println("Enter the number of living neighbors a cell needs to survive: ");
      survives = input.nextInt();
      /*
      System.out.println("Would you like the units to be color coded by how many living neighbors it has? (y/n)");
      if(input.nextLine().equals("y"))
      {
         willColorCode = true;
      }
      */
      neighbors = new byte[size][size];
      blocks = new byte[size][size];
      nextGen = new byte[size][size];
      reset();
      //createRightGlider(10,10);
      populate(0.1);
      //create5x5Gen(10,10);
      //createGlider(5,5);
      //displayCheck(10,9);
      GameOfLifeDriver driver = new GameOfLifeDriver();
      driver.main(args);
   }
   
   public static void reset()
   {
      generations = 0;
      for(int x = 0; x < blocks.length; x++)
      {
         for(int y = 0; y < blocks.length; y++)
         {
            blocks[x][y] = DEAD;
         }
      }
      nextGen = blocks.clone();
   }
   
   public static void populate(double percent)
   {
      for(int x = 0; x < blocks.length; x++)
      {
         for(int y = 0; y < blocks.length; y++)
         {
            if(Math.random() <= percent)
            {
               blocks[x][y] = ALIVE;
            } 
         }
      }
   }
   public static void displayWorld()
   {
      for(int x = 0; x < blocks.length; x++)
      {
         for(int y = 0; y < blocks.length; y++)
         {
            System.out.print(blocks[x][y]);
         }
         System.out.println();
      }
   }
   public static void updateWorld()
   {
      population = 0;
      generations++;
      nextGen = new byte[size][size];
      neighbors = new byte[size][size];
      for(int x = 0; x < blocks.length; x++)
      {
         for(int y = 0; y < blocks.length; y++)
         {
            if(isAliveNextGen(x,y))
            {
               nextGen[x][y] = ALIVE;
               population++;
            }
            else
            {
               nextGen[x][y] = DEAD;
            }
         }
      }
      blocks = nextGen.clone();
   }
   public static boolean isAliveNextGen(int x, int y)
   {
      int count = 0;
      int x2;
      int y2;
      for(int x1 = x-1; x1 <= x+1; x1++)
      {
         for(int y1 = y-1; y1 <= y+1; y1++)
         {
            if(y1 == y && x1 == x)
            {
               continue;
            }
            x2 = x1;
            y2 = y1;             
            if(x1 < 0)
            {
               x2 = size-1;
            }
            if(x1 > blocks.length-1)
            {
              // x2 = 0;
               x2 = x1 % (blocks.length);
            }
            if(y1 < 0)
            {
               y2 = size-1;
            }
            if(y1 > blocks.length-1)
            {
               //y2 = 0;
               y2 = y1 % (blocks.length);
            }
            if(blocks[x2][y2] == ALIVE)
            {
               count++;
            }
         }
      }
      neighbors[x][y] = (byte)count;   
      if(count < survives)
      {
         return false;
      }
      if((count == survives && blocks[x][y] == ALIVE) || count == born)
      {
         return true;
      }
      else
      {
         return false;
      }
   }
   public void paintComponent(Graphics g)
   {
      g.setColor(new Color(220,220,220));
      g.fillRect(0,0,getWidth(),getHeight());
      for(int x = 0; x < blocks.length; x++)
      {
         for(int y = 0; y < blocks.length; y++)
         {
            int x1 = x * 10;
            int y1 = y * 10;
            if(!willColorCode)
            {
               if(blocks[x][y] == ALIVE)
               {
                  g.setColor(new Color(0,0,0));
                  g.fillRect(x1,y1,10,10);
                  
               }
               else
               {
                  g.setColor(new Color(255,255,255));
                  g.fillRect(x1,y1,10,10);
               }
            }
            else
            {
               if(blocks[x][y] == ALIVE)
               {
                  if(neighbors[x][y] == 8)
                  {
                     g.setColor(new Color(0,0,0));
                  }
                  if(neighbors[x][y] == 7)
                  {
                     g.setColor(new Color(220,220,220));
                  }
                  if(neighbors[x][y] == 6)
                  {
                     g.setColor(new Color(127,127,127));
                  }
                  if(neighbors[x][y] == 5)
                  {
                     g.setColor(new Color(237,28,36));
                  }
                  if(neighbors[x][y] == 4)
                  {
                     g.setColor(new Color(255,201,14));
                  }
                  if(neighbors[x][y] == 3)
                  {
                     g.setColor(new Color(0,162,232));
                  }
                  if(neighbors[x][y] == 2)
                  {
                     g.setColor(new Color(112,146,190));
                  }
                  if(neighbors[x][y] == 1)
                  {
                     g.setColor(new Color(63,72,204));
                  }
                  if(neighbors[x][y] == 0)
                  {
                     g.setColor(new Color(225,225,225));
                  }
               }
               else
               {
                  g.setColor(new Color(255,255,255));
               }
               g.fillRect(x1,y1,10,10);
            }
            g.setColor(new Color(220,220,220));
            g.drawLine(x1, 0, x1, (blocks.length) * 10);
            g.drawLine(0, y1, blocks.length * 10, y1);
         }
         g.setColor(new Color(0,0,0));
         Font font = new Font("Verdana", Font.BOLD, 24);
         g.setFont(font);
         g.drawString("Generations: " + generations, blocks.length * 10 + 20, 50);
         g.drawString("Population: " + population, blocks.length * 10 + 20, 100);
      }
         //updateWorld();
         //this.repaint();
   }
   
   public static void displayCheck(int x, int y)
   {
      int count = 0;
      int x2;
      int y2;
      for(int x1 = x-1; x1 <= x+1; x1++)
      {
         for(int y1 = y-1; y1 <= y+1; y1++)
         {
            x2 = x1;
            y2 = y1;
            if(x1 < 0)
            {
               x2 = size-1;
               //x2 = (size-1) % x1;
            }
            if(x1 > size-1)
            {
               x2 = x1 % (size);
            }
            if(y1 < 0)
            {
               y2 = size-1;
               //y2 = (size-1) % y1;
            }
            if(y1 > size-1)
            {
               y2 = y1% (size);
            }
            if(y1 == y && x1 == x)
            {
               continue;
            }
            blocks[x2][y2] = ALIVE;
         }
      }
   }
   public static void createGlider(int x, int y)
   {
      blocks[x][y] = DEAD;
      blocks[x-1][y+1] = ALIVE;
      blocks[x][y+1] = ALIVE;
      blocks[x+1][y+1] = ALIVE;
      blocks[x+1][y] = ALIVE;
      blocks[x][y-1] = ALIVE;
   }
   public static void createOscillator(int x, int y)
   {
      blocks[x][y] = ALIVE;
      blocks[x+1][y] = ALIVE;
      blocks[x-1][y] = ALIVE;
   }
   public static void createRightGlider(int x, int y)
   {
      blocks[x][y] = DEAD;
      blocks[x+1][y-1] = ALIVE;
      blocks[x+1][y] = ALIVE;
      blocks[x+1][y+1] = ALIVE;
      blocks[x][y+1] = ALIVE;
      blocks[x-1][y] = ALIVE;
   }
   public static void create5x5Gen(int x, int y)
   {
      blocks[x-2][y+2] = ALIVE;
      blocks[x-1][y+2] = ALIVE;
      blocks[x][y+2] = ALIVE;
      blocks[x+1][y+2] = DEAD;
      blocks[x+2][y+2] = ALIVE;
   
      blocks[x-2][y+1] = ALIVE;
      blocks[x-1][y+1] = DEAD;
      blocks[x][y+1] = DEAD;
      blocks[x+1][y+1] = DEAD;
      blocks[x+2][y+1] = DEAD;
   
      blocks[x+1][y] = ALIVE;
      blocks[x+2][y] = ALIVE;
   
      blocks[x-1][y-1] = ALIVE;
      blocks[x][y-1] = ALIVE;
      blocks[x+2][y-1] = ALIVE;
   
      blocks[x-2][y-2] = ALIVE;
      blocks[x][y-2] = ALIVE;
      blocks[x+2][y-2] = ALIVE;
   
   
   }
   public static void lengthenArray(int[] i, int len)
   {
      int[] dataholder = i.clone();
      i = new int[len];
      for(int index = 0; index < dataholder.length; index++)
      {
         i[index] = dataholder[index];
      }
   }
   public void mouseClicked( MouseEvent e )
   {
      int mouseX = e.getX();
      int mouseY = e.getY();
      if(mouseX <= (blocks.length *10))
      {
         if(mouseY <= (blocks.length * 10))
         {
            blocks[mouseX/10][mouseY/10] = ALIVE;
            repaint();
         
         }
      }
   }
   public void mousePressed( MouseEvent e )
   {}

   public void mouseReleased( MouseEvent e )
   {}

   public void mouseEntered( MouseEvent e )
   {}

   public void mouseMoved( MouseEvent e)
   {}
   public void mouseDragged( MouseEvent e)
   {}

   public void mouseExited( MouseEvent e )
   {}

}
