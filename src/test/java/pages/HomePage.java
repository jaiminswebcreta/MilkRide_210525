package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList; // For manual soft asserts
import java.util.List;      // For manual soft asserts

public class HomePage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators from previous steps
    private By successfulLoginToastLocator = By.xpath("//div[@class='toast-body']");
    // ***** ENSURE THIS IS THE CORRECT LOCATOR YOU FOUND *****
//    private By categoriesHeaderLocator = By.xpath("//h2[normalize-space()='Categories']"); // Example, update this

    // --- NEW LOCATORS FOR HEADER/NAVIGATION OPTIONS ---
    // ** YOU MUST INSPECT EACH OF THESE ELEMENTS AND PROVIDE ACCURATE LOCATORS **
    // These are general guesses based on common text.
    private By homeLinkLocator = By.xpath("(//a[normalize-space()='Home'])[1]"); // Example
    private By allProductsLinkLocator = By.xpath("(//a[normalize-space()='All Products'])[1]");
    private By myOrdersLinkLocator = By.xpath("(//a[normalize-space()='My Orders'])[1]");
    private By mySubscriptionsLinkLocator = By.xpath("(//a[normalize-space()='My Subscriptions'])[1]");
    private By searchIconLocator = By.xpath("(//i[@class='fa fa-search'])[1]"); // For search input or icon
    private By walletIconLocator = By.xpath("(//span[@class='wallet-amount fw-semibold'])[1]"); // For wallet icon/link
    private By profileIconLocator = By.xpath("(//i[@class='bi-person'])[1]");
    
		
	


    public HomePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void verifyNavigationToHomePage()  throws InterruptedException{
        System.out.println("\n--- Verifying Navigation to Home Page ---");

        
       

//        WebElement categoriesHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(categoriesHeaderLocator));
//        if (!categoriesHeader.isDisplayed()) {
//            throw new AssertionError("'Categories' header (or chosen unique element) on home page is not displayed.");
//        }
//        System.out.println("Home page unique element ('" + categoriesHeader.getText().trim() + "') is displayed - Verified");
//        System.out.println("Successfully navigated to and verified essential Home Page elements.");
    }

    
    // --- NEW METHOD FOR VERIFYING HEADER OPTIONS WITH MANUAL SOFT ASSERTS ---
    public List<String> verifyHeaderOptions() {
        System.out.println("\n--- Verifying Home Page Header Options ---");
        List<String> verificationFailures = new ArrayList<>();

        // Helper lambda for verifying visibility and clickability
        // Returns true if element is fine, false otherwise and adds to failures list
        java.util.function.BiFunction<By, String, Boolean> checkElement = (locator, elementName) -> {
            try {
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                if (!element.isDisplayed()) {
                    verificationFailures.add(elementName + " is not displayed.");
                    System.err.println(elementName + " is not displayed.");
                    return false;
                }
                // Check for clickability (elementToBeClickable also checks for visibility and enabled)
                wait.until(ExpectedConditions.elementToBeClickable(locator));
                System.out.println(elementName + " is visible and clickable - Verified.");
                return true;
            } catch (Exception e) {
                verificationFailures.add(elementName + " is not visible or not clickable. Error: " + e.getMessage().split("\n")[0]); // Get first line of error
                System.err.println(elementName + " is not visible or not clickable. Error: " + e.getMessage().split("\n")[0]);
                return false;
            }
        };

        // Verify each option
        checkElement.apply(homeLinkLocator, "Home option");
        checkElement.apply(allProductsLinkLocator, "All Products option");
        checkElement.apply(myOrdersLinkLocator, "My Orders option");
        checkElement.apply(mySubscriptionsLinkLocator, "My Subscriptions option");
        checkElement.apply(searchIconLocator, "Search option/icon");
        checkElement.apply(walletIconLocator, "Wallet option/icon");
        checkElement.apply(profileIconLocator, "Profile option/icon");
       
        return verificationFailures;
    }
}