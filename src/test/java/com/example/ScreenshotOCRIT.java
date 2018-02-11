package com.example;

import static org.junit.Assert.assertEquals;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sourceforge.tess4j.Tesseract;

public class ScreenshotOCRIT {

	private static final String FOLDER = "/tmp//";
	
	private static final String URL = "http://localhost:3000";

    private static WebDriver driver;
    
    private static ObjectMapper mapper;

    String language = "eng";
    
    @BeforeClass
    public static void setUp(){
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
    		//get the comparison data
    		String comparison = getContent("./example/public/data/kr.json","placeholder");
        driver.get(URL + "/?lang=kr");
        language = "kor";
        File shot = takeScreenShot();
        String result = ocrScreenshot(shot,language);
        assertEquals(comparison,result);
    }
    

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

    public File takeScreenShot() throws IOException {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String fileName = UUID.randomUUID().toString();
        File targetFile = new File(FOLDER + fileName + ".png");
        FileUtils.copyFile(scrFile, targetFile);
        return targetFile;
    }

    public void takeScreenShot(WebElement element) throws IOException {
        File screen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Point p = element.getLocation();
        Rectangle rect = new Rectangle(element.getSize().getWidth(), element.getSize().getHeight());
        BufferedImage img = ImageIO.read(screen);
        BufferedImage dest = img.getSubimage(p.getX(), p.getY(), rect.width, rect.height);
        ImageIO.write(dest, "png", screen);
        FileUtils.copyFile(screen, new File(FOLDER + System.nanoTime() + ".png"));
    }
    
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
