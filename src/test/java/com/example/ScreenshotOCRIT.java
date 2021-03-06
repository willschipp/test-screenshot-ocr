package com.example;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sourceforge.tess4j.Tesseract;

public class ScreenshotOCRIT {

	private static final String FOLDER = "/tmp//";
	
	private static final String URL = "http://localhost:3000"; //TODO - create autostart URL

    private static WebDriver driver;
    
    private static ObjectMapper mapper;

    String language = "eng";
    
    @BeforeClass
    public static void setUp(){
        //check for URL availability
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(URL);
        try {
			client.execute(get);
		} catch (Exception e) {
			throw new RuntimeException("Failed on server test");
		}
        //now setup the selenium driver
    	System.setProperty("webdriver.gecko.driver","./bin/mac/geckodriver");
        driver = new FirefoxDriver();
        //json
        mapper = new ObjectMapper();
    }
    
    @AfterClass
    public static void cleanUp(){
        if (driver != null){
            driver.close();
            driver.quit();
        }
    }    
    
    @Test
    public void testTakeScreenShotEN() throws Exception {
        driver.get(URL + "/?lang=en");
        language = "eng";
        File shot = takeScreenShot();
        String result = ocrScreenshot(shot,language);
		String comparison = getElementValue("someInputField","placeholder");    
        assertEquals(comparison.trim(),result.trim());
    }
    
    
    @Ignore //Test fails due to OCR of Korean
    @Test
    public void testTakeScreenShotKR() throws Exception {
        driver.get(URL + "/?lang=kr");
        language = "kor";
        File shot = takeScreenShot();
        String result = ocrScreenshot(shot,language);
		String comparison = getElementValue("someInputField","placeholder");    
        assertEquals(comparison.trim(),result.trim());
    }    

    /*
    @Test
    public void testTakeScreenShotKR() throws Exception {
    		//get the comparison data
    		String comparison = getContent("./example/public/data/kr.json","placeholder");
        driver.get(URL + "/?lang=kr");
        language = "kor";
        File shot = takeScreenShot();
        String result = ocrScreenshot(shot,language);
        assertEquals(comparison,result);
    }
    */
    
    public String getElementValue(String elementName,String attribute) throws Exception {
    		return driver.findElement(By.name(elementName)).getAttribute(attribute);
    }

    public File takeScreenShot() throws IOException {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String fileName = UUID.randomUUID().toString();
        File targetFile = new File(FOLDER + fileName + ".png");
        FileUtils.copyFile(scrFile, targetFile);
        return targetFile;
    }

    
    //Supporting method to retrieve from the JSON raw itself
    public String getContent(String location,String field) throws Exception {
    		Map<String,Object> map = mapper.readValue(this.getClass().getClassLoader().getResourceAsStream(location), new TypeReference<Map<String,Object>>() {});
    		return map.get(field).toString();
    }
    
    
    public String ocrScreenshot(File file,String language) throws Exception {
    		Tesseract tesseract = new Tesseract();
    		tesseract.setDatapath("/usr/local/Cellar/tesseract/3.05.01/share/");
    		tesseract.setLanguage(language);
    		return tesseract.doOCR(file);
    }

	
	
}
