package com.newgen.common;

import java.io.*;
import java.util.*;
import java.util.Date;
import java.text.*;
/**
 * 
 * @author Administrator
 */
public class EntrySettingLog 
{
    private PrintWriter log;
    static private EntrySettingLog instance;
    String path = "";
    Calendar calendar = Calendar.getInstance();

    /**
     * 
     * @return
     */
    static synchronized public EntrySettingLog getInstance() {
        if (instance == null) {
            instance = new EntrySettingLog();
        }
        return instance;
    }
    
    /**
     * 
     */
    public EntrySettingLog() {
        init();
    }

    /**
     * 
     */
    public void init()
     {
             Properties dbProps = new Properties();
             String projectRoot = System.getProperty("user.dir");
             System.out.println("projectRoot = " + projectRoot);
             String result = projectRoot + "\\CustomisationLog";
       boolean exists=(new File(result)).exists();
       if(exists)
          {
           
           path =projectRoot +"\\CustomisationLog\\DocFormat"+ "_" +calendar.get(5)+ "_" + (calendar.get(2) + 1) + "_" + calendar.get(1)+"[1]"+".log";
         
          }
       else
          {
           
            boolean created=new File(result).mkdir();
            path =projectRoot +"\\CustomisationLog\\DocFormat"+ "_" +calendar.get(5)+ "_" + (calendar.get(2) + 1) + "_" + calendar.get(1)+"[1]"+".log";
            
          }

          File file = new File(path);
          try
          {
              boolean success = file.createNewFile();
              if (success) 
              {
                //System.out.println(" New File was created");
              }
              else 
              {
                //System.out.println(" New File was not created");
              }
              }
          catch (Exception e)
          {
               System.out.println("Error While Creating New File:"+e);
          }
          /////
          String logFile = dbProps.getProperty("logfile", path);
          try 
          {
                log = new PrintWriter(new FileWriter(logFile, true), true);
          }
          catch (IOException e)
          {
                System.err.println("Can't Open the log file: " +logFile);
                log = new PrintWriter(System.err);
          }
  
          /////
             long len1=0;
          long len=file.length();
          len1=len;
          File dir = new File(projectRoot+"/"+"EJBLog");
         
          String[] children  = dir.list();
          int j              = 0;
          String day         = "";
          String mon         = "";
          String year        = "";
          String binlogpath1 = "";
          String name        = "";
          String Numvalue    = "";
          String NumVal         = "";
          if (children == null)
          {
            //System.out.println("No file exits in the directory");
          }
          else 
          {
                for (int i=0; i<children.length; i++) 
                {
                    int index1 = children[i].indexOf("EJB_Log");
                    
                   
                    if(index1!=-1)
                    { 
                        StringTokenizer stn   = new StringTokenizer(children[i],"_");
                        
                        while(stn.hasMoreTokens())
                        {
                            name= stn.nextToken();
                            
                            if(stn.hasMoreTokens())
                            {
                            String name1=stn.nextToken();
                            
                            }
                            if(stn.hasMoreTokens())
                            {
                            day= stn.nextToken();
                            
                            }
                            if(stn.hasMoreTokens())
                            {
                            mon= stn.nextToken();
                            
                            }
                            if(stn.hasMoreTokens())
                            {
                                year= stn.nextToken();
                                StringTokenizer stn1   = new StringTokenizer(year,"[");
                                if(stn1.hasMoreTokens())
                                {
                                year     = stn1.nextToken();
                                Numvalue = stn1.nextToken();
                                
                                }
                                StringTokenizer stnVal   = new StringTokenizer(Numvalue,"]");
                                if(stnVal.hasMoreTokens())
                                {
                                NumVal=stnVal.nextToken();
                                
                                }
                            }
                       }
                String xx=(calendar.get(2) + 1)+"/"+calendar.get(5)+"/"+calendar.get(1); //get the current date string

                
                String yy=mon+"/"+day+"/"+year;
                
                long tt1=GetDateDiff(xx,yy);
                
                path = projectRoot + "/" + "BMLLog" + "/" +children[i];    
                
                file       = new File(path); 
                if(tt1 >= 345600000 ) // For 4 days log file will be there
                {
                file.delete();
                }
                else
                {

                if(tt1 == 0)
                {
                if(file.length() > (5*1024*1024)) // Size of the each file will be 5 MB
                {
                binlogpath1 = projectRoot +"/"+ "BML_Log"+"_"+calendar.get(5)+ "_" + (calendar.get(2) + 1) + "_" + calendar.get(1)+"["+(Integer.parseInt(NumVal)+1)+"]"+".log";
                
                }
                else
                {
                    binlogpath1=path;
                    
                }
               }
            }
          }
        }
                path=binlogpath1;

    }
}////End
    


     /**
      * 
      * @param x
      * @param y
      * @return
      */
     public long  GetDateDiff(String x,String y)
{ 
    Date dateN=null;
    Date dateM=null;
    try 
    {
        SimpleDateFormat df=new SimpleDateFormat("MM/dd/yyyy");
        dateN=df.parse(x);
        dateM=df.parse(y);
        
    }
    catch (Exception err)
    {
      
    }
        long differ= (dateN.getTime() - dateM.getTime());
        
        return differ;
}


     /**
      * 
      * @param msg
      */
     public void log(String msg)
{
   EntrySettingLog oEntrySettingLog=null;
   try
   {
       oEntrySettingLog=new EntrySettingLog();
       String packageName = oEntrySettingLog.getClass().getPackage().getName();
       log.println(packageName+"::"+new Date()+": " + msg);           
   }
   catch (Exception e)
   {
      System.out.println("In  File:EntrySettingLog And Function:log:: Exception......"+e.getMessage()); 
   }
   finally
   {
        if(oEntrySettingLog!=null)
        {
            oEntrySettingLog=null;
            
        }

    }
}
     
private void log(Throwable e, String msg) 
{
      log.println(new Date()+": " + msg);
      e.printStackTrace(log);
}
 }