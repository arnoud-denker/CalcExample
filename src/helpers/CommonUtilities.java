package helpers;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author arnoud
 */
public class CommonUtilities 
    {
    
//---------------------------------------------------------------------
//---------------------------------------------------------------------
//---------------------------------------------------------------------
    public static String conCatStringForDatapurpose(double doubleValue, int nrOffDecimalsAfter)
      	{
        if (Double.isNaN(doubleValue) == true ||  Double.isInfinite(doubleValue) == true)
            {
            return "";
            }
      	
    	boolean sign = false;
    	if (doubleValue < 0)
            {
            sign = true;
            doubleValue = doubleValue * -1;
            }
    	   	
    	BigDecimal bd = new BigDecimal(doubleValue);
        String outputstring =  bd.setScale(nrOffDecimalsAfter,BigDecimal.ROUND_HALF_UP).toString();
   	   	
      	if (outputstring.charAt(outputstring.length()-1) == ',')
      	    {
      	    outputstring = outputstring.substring(0, outputstring.length()-1);
      	    outputstring = outputstring + "0";
      	    }
      	
      	outputstring = outputstring.replace(".", ",");
      	
      	if (sign == true)
            {	
            return "-" + outputstring;
            }
      	else
            {	
            return outputstring;
            }
      	}
    
//---------------------------------------------------------------------
//---------------------------------------------------------------------
//---------------------------------------------------------------------

    public static double calculateMean(List<Double> values) 
        {
        if (values == null || values.isEmpty())
            return 0;
            
        double sum = 0.0;
        for (double value : values) 
            {
            sum += value;
            }
        return sum / values.size();
        }
    
//---------------------------------------------------------------------
//---------------------------------------------------------------------
//---------------------------------------------------------------------
    
    // Helper method to calculate the total sum of squares (SST)
    public static double calculateSST(List<Double> values, double mean) 
        {
        if (values == null || values.isEmpty())
            return 0;
                
        double sum = 0.0;
        for (double value : values) 
            {
            sum += Math.pow(value - mean, 2);
            }
        return sum;
        }

//---------------------------------------------------------------------
//---------------------------------------------------------------------
//---------------------------------------------------------------------
    
    // Helper method to calculate the residual sum of squares (SSE)
    public static double calculateSSE(List<Double> observed, List<Double> predicted) 
        {
        if (observed == null || observed.isEmpty() || predicted == null || predicted.isEmpty())
            return 0;
            
        double sum = 0.0;
        for (int i = 0; i < observed.size(); i++) 
            {
            sum += Math.pow(observed.get(i) - predicted.get(i), 2);
            }
        return sum;
        }
    
//---------------------------------------------------------------------
//---------------------------------------------------------------------
//---------------------------------------------------------------------
}
