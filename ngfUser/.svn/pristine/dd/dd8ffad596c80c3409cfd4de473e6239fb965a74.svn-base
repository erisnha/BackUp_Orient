package com.newgen.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ReadProperty 
{
	Properties prop = new Properties();
	InputStream input = null;
	public String getValue(String parameter)
	{
		try {
			input = new FileInputStream("config.properties");
			prop.load(input);
			
	 
		}catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop.getProperty(parameter);
	}
}
