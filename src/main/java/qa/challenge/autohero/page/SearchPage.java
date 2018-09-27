package qa.challenge.autohero.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import qa.challenge.autohero.utils.DateUtils;
import qa.challenge.autohero.utils.NumberUtils;

import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;
import static org.openqa.selenium.By.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static qa.challenge.autohero.PageProperties.getSearchPageUrl;

public class SearchPage {


    private WebDriver driver;
    private WebDriverWait wait;

    public SearchPage(WebDriver webDriver) {
        this.driver = webDriver;
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        this.wait = new WebDriverWait(driver, 5);
    }

    public void goTo() {
        driver.get(getSearchPageUrl());
    }


    public void selectFirstRegistration(int registrationYear) {
        openFirstRegistrationFilter();
        findChildElementByDataQaSelectorValue(findFirstRegistrationFilterSelect(), Integer.toString(registrationYear)).click();
    }

    private WebElement findFirstRegistrationFilterSelect() {
        return findChildElementByDataQaSelector(findFirstRegistrationFilter(), "select");
    }

    private void openFirstRegistrationFilter() {
        findFirstRegistrationFilter().click();
    }

    private WebElement findFirstRegistrationFilter() {
        return findElementByDataQaSelector("filter-year");
    }

    public void sortByPriceDesc() {
        WebElement priceDescOption = findChildElementByDataQaSelectorValue(findFirstRegistrationFilterSelect(), "offerPrice.amountMinorUnits.desc");
        priceDescOption.click();
    }

    private WebElement findPaginationNextButton() {
        return findPagination().findElement(xpath("//li/a[.//span[@aria-label='Next']]"));
    }

    private boolean isPaginationNextButtonEnabled() {
        return !findPaginationNextButton().getAttribute("class").contains("disabled");
    }

    public List<BigDecimal> findPrices() throws InterruptedException {
        List<BigDecimal> prices = new ArrayList<>(findCurrentPagePrices());
        do {
            goToNextPage();
            prices.addAll(findCurrentPagePrices());
        } while (isPaginationNextButtonEnabled());
        return prices;
    }

    public List<Year> findFirstRegistrations() throws InterruptedException {
        List<Year> firstRegistrations = new ArrayList<>(findCurrentPageFirstRegistration());
        do {
            goToNextPage();
            firstRegistrations.addAll(findCurrentPageFirstRegistration());
        } while (isPaginationNextButtonEnabled());
        return firstRegistrations;
    }

    private List<Year> findCurrentPageFirstRegistration() throws InterruptedException {
        Thread.sleep(2000);
        WebElement adItemsElement = findChildElementByDataQaSelector(findResultsFoundElement(), "ad-items");
        List<WebElement> ads = findChildElementsByDataQaSelector(adItemsElement, "ad");
        return ads.stream()
                .map(ad -> findChildElementByDataQaSelector(ad, "spec-list"))
                .map(specList -> findChildElementsByDataQaSelector(specList, "spec").get(0).getText()).
                        map(DateUtils::parseToYear).
                        collect(toList());
    }

    private void goToNextPage() {
        if (!isPaginationNextButtonEnabled()) {
            return;
        }
        wait.until(elementToBeClickable(findPaginationNextButton())).click();
    }

    private List<BigDecimal> findCurrentPagePrices() throws InterruptedException {
        Thread.sleep(2000);
        return findChildElementsByDataQaSelector(findResultsFoundElement(), "price")
                .stream()
                .map(WebElement::getText)
                .map(NumberUtils::parseToBigDecimal)
                .collect(toList());
    }

    private WebElement findPagination() {
        return findResultsFoundElement().findElement(cssSelector(".pagination"));
    }

    private WebElement findResultsFoundElement() {
        return wait.until(visibilityOf(findElementByDataQaSelector("results-found")));
    }

    private WebElement findElementByDataQaSelector(String value) {
        String xpathExpression = "//*[@data-qa-selector='" + value + "']";
        return driver.findElement(tagName("main")).findElement(xpath(xpathExpression));
    }

    private WebElement findChildElementByDataQaSelector(WebElement webElement, String value) {
        String xpathExpression = "//*[@data-qa-selector='" + value + "']";
        return webElement.findElement(xpath(xpathExpression));
    }

    private List<WebElement> findChildElementsByDataQaSelector(WebElement webElement, String value) {
        String xpathExpression = "//*[@data-qa-selector='" + value + "']";
        return webElement.findElements(xpath(xpathExpression));
    }

    private WebElement findChildElementByDataQaSelectorValue(WebElement webElement, String value) {
        String xpathExpression = "//*[@data-qa-selector-value='" + value + "']";
        return webElement.findElements(xpath(xpathExpression)).get(0);
    }


}