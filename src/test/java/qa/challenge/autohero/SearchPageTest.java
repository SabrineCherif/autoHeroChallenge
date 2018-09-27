package qa.challenge.autohero;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import qa.challenge.autohero.page.SearchPage;
import qa.challenge.autohero.utils.DateUtils;

import static java.util.Comparator.reverseOrder;
import static org.assertj.core.api.Assertions.assertThat;

public class SearchPageTest {

    private WebDriver driver;


    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:/TestautoApp/chromedriver.exe");
        driver = new ChromeDriver();
    }

    @Test
    public void should_all_cars_sorted_by_price_descending() throws InterruptedException {
        SearchPage page = new SearchPage(driver);
        page.goTo();
        page.selectFirstRegistration(2015);
        page.sortByPriceDesc();

        assertThat(page.findPrices()).isSortedAccordingTo(reverseOrder());
    }


    @Test
    public void should_all_cars_filtered_by_first_registration() throws InterruptedException {
        SearchPage page = new SearchPage(driver);
        page.goTo();
        page.selectFirstRegistration(2015);
        page.sortByPriceDesc();

        assertThat(page.findFirstRegistrations().stream()
                .allMatch(registrationYear -> DateUtils.isYearAfterOrEqual(registrationYear, 2015)))
                .isTrue();
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }
}
