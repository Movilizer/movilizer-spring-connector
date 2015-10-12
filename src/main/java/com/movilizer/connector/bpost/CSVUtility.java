package com.movilizer.connector.bpost;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class CSVUtility {

    @Autowired
    private ResourceLoader resourceLoader;

    public Map<String, String[]> createMapFromCSV(String resourcePath) {
        Map<String, String[]> result = new HashMap<String, String[]>();
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {
        	Resource resource = resourceLoader.getResource(resourcePath);
        	if(resource.exists())
        	{
                br = new BufferedReader(new java.io.InputStreamReader(resource.getInputStream()));
                while ((line = br.readLine()) != null) {
                    // use comma as separator
                    String[] participant = line.split(cvsSplitBy);
                    result.put(participant[0], participant);
                }
        	}


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
