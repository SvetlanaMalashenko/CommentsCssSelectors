import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.xpath.SourceTree;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.NoSuchElementException;

import java.io.IOException;
//import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import static com.sun.activation.registries.LogSupport.log;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by svetlana on 27.12.15
 */
public class CreateComment {

    private WebDriver driver;
    private WebElement newButton;
    private WebElement commentText;
    private WebElement number;
    private WebElement category;
    private WebElement currentSelect;
    private WebElement saveButton;
    private WebElement sortNumber;
    private WebElement checkBox;

    @Before
    public void openBrowser() {
        driver = new FirefoxDriver();
        driver.get("http://comments.azurewebsites.net/");

    }

    @After
    public void closeBrowser() throws IOException {
        driver.quit();
    }

    @Test
    public void createComment() {
        log("Open page with comments");
        newButton = driver.findElement(By.cssSelector("input[value*= 'New']"));

        log("Click on newButton");
        newButton.click();

        log("Write text inside the comment Text");
        commentText = driver.findElement(By.cssSelector("input[id*= 'Text']"));
        commentText.sendKeys("BlaBla");

        log("Write date inside the number field");
        number = driver.findElement(By.cssSelector("input[id*='Number']"));
        number.sendKeys("432");

        log("Select Category");
        category = driver.findElement(By.cssSelector("input[value*='1']"));
        category.click();

        log("Click on the button CurSelect");
        currentSelect = driver.findElement(By.cssSelector("input[name*='CurSelect']"));
        currentSelect.click();

        log("Click save button");
        saveButton = driver.findElement(By.cssSelector("input[value*='Save']"));
        saveButton.click();

        log("Click button Return");
        driver.findElement(By.tagName("a")).click();

        log("Sort rows by id");
        sortNumber = driver.findElement(By.cssSelector("a[href*='NumberValue&Text=ASC']"));

        log("Get id");
        String idValue = driver.findElement(By.cssSelector("td[class*= 'numbercolumn']")).getText();


        log("Assert that line has correct Id");
        assertEquals(idValue,"432");
    }

    @Test
    public void duplicateComment() {
        log("Get id");
        checkBox = driver.findElement(By.cssSelector("td[class*= 'checkedcolumn']"));
        checkBox.click();

        log("Duplicate comment");
        driver.findElement(By.cssSelector("input[value*= 'Duplicate']")).click();

        log("Click save");
        driver.findElement(By.cssSelector("input[value*='Save']")).click();

        log("Get error");
        String errorMessage = driver.findElement(By.id("errorfield")).getText();

        assertEquals(errorMessage,"The Number field should contain value from 0 to 999 and should be unique");
    }

    @Test
    public void verifyChangingComment() {
        log("Get id");
        checkBox = driver.findElement(By.cssSelector("td[class*= 'checkedcolumn']"));
        checkBox.click();

        log("Duplicate comment");
        driver.findElement(By.cssSelector("input[value*= 'Duplicate']")).click();

        log("Write date inside the number field");
        number = driver.findElement(By.cssSelector("input[id*='Number']"));
        number.clear();
        number.sendKeys("506");

        log("Click save");
        driver.findElement(By.cssSelector("input[value*='Save']")).click();

        log("Click button Return");
        driver.findElement(By.tagName("a")).click();

        log("Navigate to the last page");
        driver.findElement(By.cssSelector("a[href*='/?page=4']")).click();

        String newNumber = driver.findElement(By.cssSelector("td[class*= 'numbercolumn']")).getText();

        log("Verify newNumber is 506");
        assertEquals(newNumber,"506");
    }

    @Test
    public void verifyCommentWasDeletedAndPageRefreshed() throws InterruptedException {
        log("Get id");
        checkBox = driver.findElement(By.cssSelector("td[class*= 'checkedcolumn']"));
        checkBox.click();

        String number = driver.findElement(By.cssSelector("td[class*= 'numbercolumn']")).getText();

        log("Click button delete the row");
        driver.findElement(By.cssSelector("input[value*='Delete']")).click();

        log("Click on modal po-up");
        WebElement modalPopup = driver.switchTo().activeElement();
        modalPopup.findElement(By.cssSelector("button span")).click();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        String newNumber = driver.findElement(By.cssSelector("td[class*= 'numbercolumn']")).getText();
        assertNotEquals(newNumber, number);

        log("Get infoField");
        String infoMessage = driver.findElement(By.id("infoField")).getText();

        log("Verify infoField is present after delete");
        assertEquals(infoMessage,"Selected comments deleted successfull");

        log("Click on refresh button");
        driver.findElement(By.cssSelector("a[href*='/']")).click();

        log("Verify infoField is not present after refresh");
        Thread.sleep(3000);
        String infoMessageAfterReload = driver.findElement(By.id("infoField")).getText();

        assertNotEquals(infoMessageAfterReload,infoMessage);
    }
//
//    protected boolean isElementPresent(String id){
//        try{
//            driver.findElement(By.id(id));
//            return true;
//        }
//        catch(NoSuchElementException e){
//            return false;
//        }
//    }
}
