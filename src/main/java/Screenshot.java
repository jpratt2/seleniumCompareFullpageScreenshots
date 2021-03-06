import org.openqa.selenium.OutputType;
import org.openqa.selenium.firefox.FirefoxDriver;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Screenshot {

    /*
        fullpage
        use Selenium 4 and Firefox to take a full page screenshot
     */
    public static void fullpage(FirefoxDriver driver, String folder) {
        String URL = driver.getCurrentUrl();
        byte[] imageBytes = ((FirefoxDriver)driver).getFullPageScreenshotAs(OutputType.BYTES); //source: https://stackoverflow.com/a/66859108/1691651
        String filename = Utilities.convertUrlToFileName(URL);
        String path = System.getProperty("user.dir");
        String separator = File.separator;
        try {
            Files.write(Paths.get(path + separator + folder + separator + filename + ".png"), imageBytes);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void fullpage(FirefoxDriver driver){
        fullpage(driver, "expected");
    }


    /*
        compareImage
        use AShot to compare the current web page image against the baseline
        https://github.com/pazone/ashot

     */
    public static void compareImage(FirefoxDriver driver, int pixelThreshold){
        String pathExpected = "expected";
        String pathObserved = "observed";
        String pathDiff = "diffs";

        String URL = driver.getCurrentUrl();
        String fileName = Utilities.convertUrlToFileName(URL);

        //take a fresh full-page image and put it in the "observed" folder
        Screenshot.fullpage(driver, "observed");

        //read data for baseline image (from the "expected" folder, typically)
        String pathToExpected = System.getProperty("user.dir") + File.separator + pathExpected + File.separator + fileName + ".png";
        BufferedImage imgExpected = null;
        try {
            imgExpected = ImageIO.read(new File(pathToExpected));
        } catch (IOException e) {
            //read image resource https://docs.oracle.com/javase/tutorial/2d/images/loadimage.html
        }

        //read data for the observed image ("observed" folder)
        String pathToObserved = System.getProperty("user.dir") + File.separator + pathObserved + File.separator + fileName + ".png";
        BufferedImage imgObserved = null;
        try {
            imgObserved = ImageIO.read(new File(pathToObserved));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //generate a diff of these 2 images if any difference exists
        ImageDiff diff = new ImageDiffer().makeDiff(imgExpected, imgObserved).withDiffSizeTrigger(pixelThreshold);
        if (diff.hasDiff()) {
            BufferedImage diffImage = diff.getMarkedImage();
            //save the diff image in the diffs folder
            String diffPath = System.getProperty("user.dir") + File.separator + pathDiff + File.separator + fileName + ".png";
            try {
                File outputfile = new File(diffPath);
                ImageIO.write(diffImage, "png", outputfile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void compareImage(FirefoxDriver driver){
        compareImage(driver, 0);
    }

}

