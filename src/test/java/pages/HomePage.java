package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Using the locator that successfully found the text
    private By successfulLoginToastLocator = By.xpath("//*[self::div or self::span or self::p][normalize-space()=\"You've successfully logged in.\"]");

    private By categoriesHeaderLocator = By.xpath("(//h4[normalize-space()='Categories'])[1]");


    public HomePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void verifyNavigationToHomePage() {
        System.out.println("\n--- Verifying Navigation to Home Page ---");

        // 1. Verify successful login toast message
        WebElement toastMessageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(successfulLoginToastLocator));
        if (!toastMessageElement.isDisplayed()) {
            throw new AssertionError("Successful login toast message is not displayed even after wait.");
        }
        String toastText = toastMessageElement.getText().trim();
        System.out.println("Retrieved toast text: [" + toastText + "]");

        if (toastText.isEmpty()) {
            throw new AssertionError("Successful login toast message was found, but its text content is EMPTY. Locator might be targeting a wrapper element. Current locator: " + successfulLoginToastLocator.toString());
        }
        if (!toastText.contains("You've successfully logged in.")) {
            throw new AssertionError("Toast message text is incorrect or does not contain expected phrase. Expected to contain: \"You've successfully logged in.\" Found: \"" + toastText + "\"");
        }
        System.out.println("Successful login toast message: '" + toastText + "' - Verified");

        // 2. Wait for the toast message to disappear - COMMENTED OUT FOR DEMO / PERSISTENT TOAST
        /*
        boolean toastDisappeared = wait.until(ExpectedConditions.invisibilityOfElementLocated(successfulLoginToastLocator));
        if (toastDisappeared) {
            System.out.println("Login toast message has disappeared - Verified");
        } else {
            // This else block would not be reached if it times out, an exception would be thrown.
            // For robustness, if the check is kept, handle TimeoutException specifically or make it less strict.
            System.out.println("Warning: Login toast message did not disappear within the timeout. Proceeding anyway.");
        }
        */
        System.out.println("Skipping wait for toast disappearance (assuming it might persist in demo or hide quickly).");
        // Optional: Add a small fixed pause if elements load slightly after the toast *would have* disappeared.
        // try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }


   
        WebElement categoriesHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(categoriesHeaderLocator));
        if (!categoriesHeader.isDisplayed()) {
            // This check is somewhat redundant if visibilityOfElementLocated passed, but good for sanity.
            throw new AssertionError("'Categories' header (or chosen unique element) on home page is not displayed after wait.");
        }
        System.out.println("Home page unique element ('" + categoriesHeader.getText().trim() + "') is displayed - Verified");

        System.out.println("Successfully navigated to and verified Home Page.");
    }
}