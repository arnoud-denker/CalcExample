package main;


import helpers.CommonUtilities;
import helpers.GuiUtilities;
import data.Datamanager;
import data.TimeValue;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class MonitorMainPanelGraph extends JPanel
    {
    public int height_OfPanel = 400;    
    public int width_OfPanel = 600;

    public static final int width_ofYaxis = 40;
    public static final int height_ofXaxis = 22;
    public static final int nrOffDates_eod = 28;
    public static final int bordersize = 10;
    

    private final Datamanager datamanger;
    
    public final static Font axisfont = new Font("verdana", Font.PLAIN, 8);
    public static final Color backgroundGraphColor = new Color(0xfeeec0);
    
//	  ------------------------------------------------------------------
//	  ------------------------------------------------------------------
//	  ------------------------------------------------------------------
    
    @SuppressWarnings({"OverridableMethodCallInConstructor", "LeakingThisInConstructor"})
    public MonitorMainPanelGraph(Datamanager _datamanger)
        {
        datamanger = _datamanger;
        
        Dimension dim = new Dimension(width_OfPanel, height_OfPanel);
        setMinimumSize(dim);
        setMaximumSize(dim);
        setPreferredSize(dim);
        setSize(dim);
        }   

//	  ------------------------------------------------------------------
//	  ------------------------------------------------------------------
//	  ------------------------------------------------------------------

    private double stopDate = 0;
    private double startDate = 0;
    
    private int stopIndex = -1;
    private int startIndex = -1;
    
    private double highestTick = Double.MIN_VALUE;
    private double lowestTick = Double.MAX_VALUE;


    

    
    @Override
    public void paintComponent(Graphics _g)
	{
            
     	setBackground(backgroundGraphColor);
     	super.paintComponent(_g);
        
        Graphics2D graphics = (Graphics2D) _g;
                
        stopIndex = -1;
        startIndex = -1;    
        highestTick = Double.MIN_VALUE;
        lowestTick = Double.MAX_VALUE;    
              
        ArrayList <TimeValue> beforeEventData = datamanger.get_before_array();
        ArrayList <TimeValue> afterEventData = datamanger.get_after_array();
        
   
            startDate = beforeEventData.get(0).date;
            stopDate = afterEventData.get(afterEventData.size() -1).date;

            highestTick = Double.MIN_VALUE;
            lowestTick = Double.MAX_VALUE;

            for (TimeValue _beforeEventData : beforeEventData)
                {
                double lastTick = _beforeEventData.value;

                if (highestTick < lastTick)
                    {
                    highestTick = lastTick;
                    }

                if (lowestTick > lastTick && lastTick > 0)
                    {
                    lowestTick = lastTick;
                    }
                }

            for (TimeValue _afterEventData : afterEventData)
                {
                double lastTick = _afterEventData.value;

                if (highestTick < lastTick)
                    {
                    highestTick = lastTick;
                    }

                if (lowestTick > lastTick && lastTick > 0)
                    {
                    lowestTick = lastTick;
                    }
                }
    //------------------------

            graphics.setColor(Color.BLACK);

            GuiUtilities.drawLine(graphics, 
                                            (width_OfPanel - width_ofYaxis), 
                                            10, 
                                            (width_OfPanel - width_ofYaxis) , 
                                            height_OfPanel - height_ofXaxis , 
                                            1);

            graphics.setFont(axisfont);

            double valueMin = lowestTick;
            double ypixelMin = calcYpixelFromYValue(valueMin);
            graphics.drawString((CommonUtilities.conCatStringForDatapurpose(valueMin, 1)), (int)(width_OfPanel - width_ofYaxis) + 4, (int) ypixelMin);

            double valueMax = highestTick;
            double ypixelMax = calcYpixelFromYValue(valueMax);
            graphics.drawString((CommonUtilities.conCatStringForDatapurpose(valueMax, 1)), (int)(width_OfPanel - width_ofYaxis) + 4, (int) ypixelMax);

            graphics.setColor(Color.BLACK);
            for (TimeValue data : beforeEventData)
                {
                double date = data.date;
                double close = data.value;

                normalDrawLine(graphics, date, close);
                }
            
            graphics.setColor(Color.BLACK);
            for (TimeValue data : afterEventData)
                {
                double date = data.date;
                double close = data.value;
                
                normalDrawLine(graphics, date, close);
                }        

// -------------------------------------------------            
            
            graphics.setColor(Color.red);
            
            if (datamanger.is_calced_parabol_list_present() == true)
                {
                for (TimeValue timeValue : datamanger.get_calced_parabol_list() )
                    {
                    double date = timeValue.date;
                    double value = timeValue.value;    
                                          
                    normalDrawLine(graphics, date, value);
                    }
                }
            
            graphics.setColor(Color.blue);
            if (datamanger.is_calced_exponential_list_present() == true)
                {
                for (TimeValue timevalue : datamanger.get_calced_exponential_list() )
                    {
                    double date = timevalue.date;
                    double value = timevalue.value;    
                    
                    normalDrawLine(graphics, date, value);
                    }
                }
            
            graphics.setColor(Color.green);
            if (datamanger.is_calced_naturalLogarithm_list_present() == true)
                {
                for (TimeValue timevalue : datamanger.get_calced_naturalLogarithm_list() )
                    {
                    double date = timevalue.date;
                    double value = timevalue.value;    
                    
                    normalDrawLine(graphics, date, value);
                    }
                }  
        }
        
//	  ------------------------------------------------------------------
//	  ------------------------------------------------------------------
//	  ------------------------------------------------------------------
        
    private double previousx = -1;
    private double previousy = -1;
    
    private void normalDrawLine(Graphics2D grahics, double date_current, double laasteDay)
        {
        double x = calcXPixelFromXTimeRel(date_current);
        double yClose = calcYpixelFromYValue(laasteDay);	
              
        if (previousx == -1)
            {
            previousx = x;
            previousy = yClose; 
            return;
            }
                                       
        if (previousx < x && yClose > 0 && previousy > 0)
            {
            GuiUtilities.drawLine(grahics, previousx, previousy, x, yClose, 1);
            }
        
        previousx = x;
        previousy = yClose; 
        }	
        
//	  ------------------------------------------------------------------
//	  ------------------------------------------------------------------
//	  ------------------------------------------------------------------

    private double calcXPixelFromXTimeRel(double searchedTime) 
        {
        if (searchedTime == -1)    
            return -1;
            
        double difference = searchedTime - startDate;
                
        double xSizeInBetween_WholeScreen =  (width_OfPanel - width_ofYaxis - 10) / (stopDate - startDate);
        double xpixel = (difference * xSizeInBetween_WholeScreen) + 10;	

        return xpixel;
        }
    
//	  ------------------------------------------------------------------
//	  ------------------------------------------------------------------
//	  ------------------------------------------------------------------
  
    private double calcYpixelFromYValue(double value)
	{
        if (value == -1)    
            return -1;
        
        double difference = highestTick - value;    
            
        double ySizeInBetween_WholeScreen =  (height_OfPanel - 20 - height_ofXaxis - bordersize) / (highestTick - lowestTick);   
          
        double ypixel = difference * ySizeInBetween_WholeScreen; 
 
	return ypixel + 20;
	}
    
//	  ------------------------------------------------------------------
//	  ------------------------------------------------------------------
//	  ------------------------------------------------------------------

    }