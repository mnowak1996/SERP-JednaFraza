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
        Runnable task = new Runnable() { // tworzenie watku
            @Override
            public void run() {
                try {

                    System.setProperty("webdriver.chrome.driver", "C:\\Users\\nowak\\Selenium\\chromedriver.exe"); // ustawienie miejsca dodaku do przegladarki chrome
                    WebDriver driver = new ChromeDriver(); // tworzenie obiektu przegldarki
                    driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);// czeka 15 sekund, jesli storna sie nie zaladuje to odswieza
                    driver.manage().window().setPosition(new Point(-2000, 0)); // przeglądarka bdzie schowana
                    driver.get("https://google.pl/");  // wejscie na strone google.pl
                    Thread.sleep(4000); // 4 sekundy oczekiwania
                    String source = driver.getPageSource(); // pobranie kodu zrodlowego strony
                    String pozycja = "Brak poączenia z internetem";
                    if (source.contains("ERR_INTERNET_DISCONNECTED")) { // sprawdzenie czy kod zrodlowy strony zawiera informacje o braku polaczneia z internetem
                        labelPozycja.setText("Brak polaczenia z internetem");
                        driver.close(); // zakmniecie przeglądarki
                        driver.quit();  // wylaczenie przegladarki

                    } else {
                        driver.get("https://cmonitor.pl/sprawdz-pozycje");// wejscie na strone cmonitor.pl
                        //driver.navigate().refresh();
                        driver.findElement(By.xpath("//*[@id='phrases']")).sendKeys(textFraza.getText()); // wpisanie frazy
                        driver.findElement(By.xpath("//*[@id='domain']")).sendKeys(textDomena.getText()); // wpisanie nazwy domeny
                        driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/div[2]/div[1]/div/form/div[2]/a")).click(); // klikniecie przyciksku sprawdz pozycje
                        Thread.sleep(6000); // 6 sekund oczekiwania
                        pozycja = driver.findElement(By.xpath("//*[@id='result_0']")).getText(); // pobranie pozycji
                        driver.close(); // zakmniecie przeglądarki
                        driver.quit();   // wylaczenie przegladarki
                    }

                    String finalPozycja = pozycja;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {  // watek odpowiedzialny za komunikacje z GUI
                            labelPozycja.setText("Pozycja: " + finalPozycja);  // wypisanie pozycji
                           // System.out.print(finalPozycja);

                        }
                    });
                } catch (Exception e) {

                }
            }
        };
        try {
            new Thread(task).start(); // rozpoczecie dzialania watku
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
        }


    }
}
