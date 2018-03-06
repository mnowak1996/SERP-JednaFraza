import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

/**
 * @author Created  by Michal Nowak
 **/

public class Serp {
    @FXML
    private TextField textFraza;
    @FXML
    private TextField textDomena;
    @FXML
    private Button buttonSprawdz;
    @FXML
    private Label labelPozycja;

    @FXML
    public void Sprawdz() {
        SprawdzPozycje(textFraza.getText(), textDomena.getText());
    }

    public void SprawdzPozycje(String fraza, String domena) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    // buttonSprawdz.setDisable(true);
                    System.setProperty("webdriver.chrome.driver", "C:\\Users\\nowak\\Selenium\\chromedriver.exe");
                    WebDriver driver = new ChromeDriver();
                    driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
                    //driver.manage().window().setPosition(new Point(-2000, 0));
                    driver.get("https://google.pl/");
                    Thread.sleep(2000);
                    String source = driver.getPageSource();
                    String pozycja = "Brak poączenia z internetem";
                    if (source.contains("ERR_INTERNET_DISCONNECTED")) {
                        labelPozycja.setText("Brak polaczenia z internetem");
                        driver.close();
                        driver.quit();

                    } else {
                        driver.get("https://cmonitor.pl/sprawdz-pozycje");
                        //driver.navigate().refresh();
                        driver.findElement(By.xpath("//*[@id='phrases']")).sendKeys(textFraza.getText());
                        driver.findElement(By.xpath("//*[@id='domain']")).sendKeys(textDomena.getText());
                        driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/div[2]/div[1]/div/form/div[2]/a")).click();
                        Thread.sleep(6000);
                        pozycja = driver.findElement(By.xpath("//*[@id='result_0']")).getText();
                        driver.close();
                        driver.quit();
                    }

                    String finalPozycja = pozycja;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            labelPozycja.setText("Pozycja: " + finalPozycja);
                            System.out.print(finalPozycja);

                        }
                    });
                } catch (Exception e) {

                }
            }
        };
        try {
            new Thread(task).start();
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
        }


    }
}
