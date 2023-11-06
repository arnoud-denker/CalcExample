package helpers;

import java.awt.Graphics;

/**
 *
 * @author arnoud
 */
public class GuiUtilities 
    {
   
//	  ------------------------------------------------------------------
//	  ------------------------------------------------------------------
//	  ------------------------------------------------------------------        
   
    public static void drawLine(Graphics g, PointD_coort point1, PointD_coort point2, int lineWidth) 
    	{
    	drawLine(g, (int) point1.x, (int) point1.y, (int) point2.x, (int) point2.y, lineWidth);
    	}
    
    
    public static void drawLine(Graphics g, double x1, double y1, double x2, double y2, int lineWidth) 
    	{
    	drawLine(g, (int) x1, (int) y1, (int) x2, (int) y2, lineWidth);
    	}
    
//	  ------------------------------------------------------------------
//	  ------------------------------------------------------------------
//	  ------------------------------------------------------------------        
    public static void drawLine(Graphics g, int x1, int y1, int x2, int y2, int lineWidth) 
    	{
    	if (lineWidth == 1)
            {
            g.drawLine(x1, y1, x2, y2);
            }
    	else if (x1 == x2)
            {
            if (y2 > y1)
                {
    		g.fillRect(x1 - (lineWidth/2), y1, lineWidth, (y2-y1));
    		}
            else
    		{
    		g.fillRect(x1 - (lineWidth/2), y2, lineWidth, (y1-y2));
    		}
            }
    	else 
            {
            double angle;
            double halfWidth = ((double)lineWidth)/2.0;
            double deltaX = (double)(x2 - x1);
            double deltaY = (double)(y2 - y1);
            if (x1 == x2)
    		{
    		angle=Math.PI;
    		}
            else
    		{
    		angle=Math.atan(deltaY/deltaX)+Math.PI/2;
    		}
            int xOffset = (int)(halfWidth*Math.cos(angle));
            int yOffset = (int)(halfWidth*Math.sin(angle));
            int[] xCorners = { x1-xOffset, x2-xOffset+1, x2+xOffset+1, x1+xOffset };
            int[] yCorners = { y1-yOffset, y2-yOffset, y2+yOffset+1, y1+yOffset+1 };
            g.fillPolygon(xCorners, yCorners, 4);
            }
    	}
//	  ------------------------------------------------------------------
//	  ------------------------------------------------------------------
//	  ------------------------------------------------------------------    
}
