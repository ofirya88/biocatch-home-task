import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;


/**
 * Created by ofiry on 7/1/2018.
 */

public class AutoPurchase {

    private static WebDriver driver = null;

    public static void main(String[] args) {
        configureDriver();
        clickOnElementByLinkText(EbayScriptsConstants.DAILY_DEALS);
        addItemsTillAmount(EbayScriptsConstants.TOTAL_CHECK_OUT);
        System.out.print("Total cart is bigger then " + EbayScriptsConstants.TOTAL_CHECK_OUT + " we are going to checkout !!");
        return;
    }

    /**
     * Add items into cart till cart total amount is bigger than max amount
     *
     * @param maxAmount Double value that describe the max amount in cart
     */
    private static void addItemsTillAmount(Double maxAmount) {
        double totalCheckOut = 0;
        int itemNumber = 2;
        while (totalCheckOut < maxAmount) {
            addItemByIdToCart("s3-c" + itemNumber);
            totalCheckOut = getTotalCartAmount();
            clickOnElementById(EbayScriptsConstants.CONTINUE_SHOPPING_BUTTON_ID);
            itemNumber++;
        }
    }

    /**
     * add item by id locator to cart
     *
     * @param locator id locator
     */
    private static void addItemByIdToCart(String locator) {
        clickOnElementById(locator);
        if (!checkIfFilled(EbayScriptsConstants.FIRST_ITEM_CATEGORY_ID))
            selectFirstOptionById(EbayScriptsConstants.FIRST_ITEM_CATEGORY_ID);
        if (!checkIfFilled(EbayScriptsConstants.SECOND_ITEM_CATEGORY_ID))
            selectFirstOptionById(EbayScriptsConstants.SECOND_ITEM_CATEGORY_ID);
        clickOnElementById(EbayScriptsConstants.CART_BUTTON_ID);
    }

    /**
     * return the total amount in cart
     *
     * @return Double - Total amount in cart
     */
    private static Double getTotalCartAmount() {
        String temporaryAmount;
        temporaryAmount = driver.findElement(By.id(EbayScriptsConstants.TOTAL_AMOUNT_ID)).getText();
        temporaryAmount = temporaryAmount.substring(4);
        temporaryAmount = temporaryAmount.replace(",", "");
        return Double.parseDouble(temporaryAmount);
    }

    /**
     * check if category of item was filled in default
     *
     * @param id locator
     * @return true if was filled , false if not .
     */
    private static boolean checkIfFilled(String id) {
        if (isElementPresent(id)) {
            Select select = new Select(driver.findElement(By.id(id)));
            String value = select.getFirstSelectedOption().getText();
            if (EbayScriptsConstants.DEFAULT_SELECT_STRING_VLAUE.equals(value))
                return false;
        }
        return true;
    }

    /**
     * check if element present in screen
     *
     * @param locator id locator
     * @return true if present . false if not
     */
    private static boolean isElementPresent(String locator) {
        return (driver.findElements(By.id(locator)).size() > 0);
    }

    /**
     * Click on element by ID locator
     *
     * @param locator ID locator
     */
    private static void clickOnElementById(String locator) {
        WebElement continueShippingButton = driver.findElement(By.id(locator));
        continueShippingButton.click();
    }

    /**
     * Click on element by Link text
     *
     * @param locator link text string
     */
    private static void clickOnElementByLinkText(String locator) {
        WebElement continueShippingButton = driver.findElement(By.linkText(locator));
        continueShippingButton.click();
    }

    /**
     * select the first option in select field by id
     *
     * @param locator locator id
     */
    private static void selectFirstOptionById(String locator) {
        Select select = new Select(driver.findElement(By.id(locator)));
        select.selectByVisibleText(select.getOptions().get(1).getText());
    }

    private static void configureDriver() {
        System.setProperty(EbayScriptsConstants.CHROME_DRIVER_KEY, EbayScriptsConstants.CHROME_DRIVER_DESTINATION);
        driver = new ChromeDriver();
        driver.get(EbayScriptsConstants.EBAY_SITE);
        driver.manage().window().maximize();
    }
}
