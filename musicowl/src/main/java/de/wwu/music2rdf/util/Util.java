package de.wwu.music2rdf.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import de.wwu.music2rdf.core.Instrument;

public class Util {
	
	public Util() {
		super();
	}

	public static String timeElapsed(Date startDate, Date endDate){
		
		long different = endDate.getTime() - startDate.getTime();
		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;
		long daysInMilli = hoursInMilli * 24;
		long elapsedDays = different / daysInMilli;
		different = different % daysInMilli;
		long elapsedHours = different / hoursInMilli;
		different = different % hoursInMilli;
		long elapsedMinutes = different / minutesInMilli;
		different = different % minutesInMilli;
		long elapsedSeconds = different / secondsInMilli;		
		long elapsedMilliseconds = different % secondsInMilli;
		String result = "";
				
		if (elapsedDays == 0 && elapsedHours != 0) {
			result = elapsedHours + " hours, " + elapsedMinutes + " minutes, " + elapsedSeconds + "." + elapsedMilliseconds+ " seconds";
		} else if (elapsedHours == 0 && elapsedMinutes != 0) {
			result = elapsedMinutes + " minutes, " + elapsedSeconds + "." + elapsedMilliseconds + " seconds";
		} else if (elapsedMinutes == 0 && elapsedSeconds != 0) {
			result = elapsedSeconds + "." + elapsedMilliseconds + " seconds";
		} else if (elapsedSeconds == 0) {			
			result = elapsedMilliseconds + " ms";
		}
		return result; 

	}
	
	public ArrayList<Instrument> getInstruments(){
		
		ArrayList<Instrument> result = new ArrayList<Instrument>();
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        
        try {

        	InputStream in = this.getClass().getResourceAsStream("/config/mediums_musicxml.csv"); 
        	br = new BufferedReader(new InputStreamReader(in));
        	
            while ((line = br.readLine()) != null) {

            	Instrument instrument = new Instrument();
                String[] medium = line.split(cvsSplitBy);

                instrument.setPerformanceMediumId(medium[0]);
                instrument.setPerformanceMediumTypeId(medium[1]);
                instrument.setPerformanceMediumDescription(medium[2]);
                
                result.add(instrument);
                
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

	public static File decompressMXL(String mxlFile, String outputFolder){

		byte[] buffer = new byte[1024];
		File newFile = new File("");
		
		try{

			File folder = new File(outputFolder);
			if(!folder.exists()){
				folder.mkdir();
			}

			ZipInputStream zis = new ZipInputStream(new FileInputStream(mxlFile));
			ZipEntry ze = zis.getNextEntry();

			while(ze!=null){

				String fileName = ze.getName();

				if(fileName.toLowerCase().equals("musicxml.xml")) { 

					newFile = new File(outputFolder + File.separator + new File (mxlFile).getName().replace(".mxl", ".xml"));					
					new File(newFile.getParent()).mkdirs();
					FileOutputStream fos = new FileOutputStream(newFile);             

					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}

					fos.close();   
					
				}
				
				ze = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();

		
		}catch(IOException ex){
			ex.printStackTrace(); 
		}
		return newFile;
	}    
	

}
