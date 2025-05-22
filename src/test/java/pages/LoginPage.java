package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.Set;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private String loginPageUrl = "https://dev.milkride.com/milkmates/signin";
    private String expectedTermsUrlPartial = "terms";
    private String expectedPrivacyUrlPartial = "privacy";

    // Locators
    private By logoLocator = By.xpath("//img[@alt='Logo']");
    private By loginSignUpHeaderLocator = By.xpath("//h4[normalize-space()='Log in or Sign up']");
    private By countryCodeLocator = By.xpath("//span[@id='basic-addon1']");
    private By mobileInputLocator = By.xpath("//input[@placeholder='Enter Mobile Number']");
    private By continueButtonLocator = By.xpath("//button[normalize-space()='Continue']");
    private By agreementTextLocator = By.xpath("//p[contains(normalize-space(), 'By continuing, you agree to our')]");
    // Add locators for Terms and Privacy links if needed by verifyLink
    private By termsLinkLocator = By.linkText("Terms of Service");
    private By privacyLinkLocator = By.linkText("Privacy Policy");


    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void navigateTo() {
        driver.get(loginPageUrl);
        System.out.println("Navigated to login page: " + loginPageUrl);
    }

    public void verifyPageUrl() {
        System.out.println("\n--- Verifying Login Page URL ---");
        wait.until(ExpectedConditions.urlToBe(loginPageUrl));
        if (!driver.getCurrentUrl().equals(loginPageUrl)) {
            throw new AssertionError("Login page URL is incorrect. Expected: " + loginPageUrl + ", Actual: " + driver.getCurrentUrl());
        }
        System.out.println("Current URL: " + driver.getCurrentUrl() + " - Verified");
    }

    public void verifyUiElements() {
        System.out.println("\n--- Verifying Login Page UI Elements ---");
        WebElement logo = wait.until(ExpectedConditions.visibilityOfElementLocated(logoLocator));
        if (!logo.isDisplayed()) throw new AssertionError("Logo is not displayed.");
        System.out.println("Logo is displayed - Verified");

        WebElement loginSignUpHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(loginSignUpHeaderLocator));
        if (!loginSignUpHeader.getText().trim().equals("Log in or Sign up"))
            throw new AssertionError("'Log in or Sign up' text is incorrect. Found: '" + loginSignUpHeader.getText().trim() + "'");
        System.out.println("Header text: '" + loginSignUpHeader.getText().trim() + "' - Verified");

        WebElement countryCode = wait.until(ExpectedConditions.visibilityOfElementLocated(countryCodeLocator));
        if (!countryCode.getText().trim().equals("+91"))
            throw new AssertionError("Country code '+91' is incorrect. Found: '" + countryCode.getText().trim() + "'");
        System.out.println("Country code text: '" + countryCode.getText().trim() + "' - Verified");

        WebElement mobileInput = wait.until(ExpectedConditions.visibilityOfElementLocated(mobileInputLocator));
        if (!mobileInput.getAttribute("placeholder").equals("Enter Mobile Number"))
            throw new AssertionError("Mobile input placeholder incorrect. Found: '" + mobileInput.getAttribute("placeholder") + "'");
        System.out.println("Mobile input placeholder: '" + mobileInput.getAttribute("placeholder") + "' - Verified");

        WebElement continueButton = wait.until(ExpectedConditions.visibilityOfElementLocated(continueButtonLocator));
        if (!continueButton.getText().trim().equals("Continue"))
            throw new AssertionError("'Continue' button text incorrect. Found: '" + continueButton.getText().trim() + "'");
        if (!continueButton.isDisplayed()) throw new AssertionError("'Continue' button is not displayed.");
        System.out.println("Continue button text: '" + continueButton.getText().trim() + "' - Verified");

        WebElement agreementText = wait.until(ExpectedConditions.visibilityOfElementLocated(agreementTextLocator));
        String expectedAgreementText = "By continuing, you agree to our Terms of Service & Privacy Policy";
        if (!agreementText.getText().contains(expectedAgreementText))
            throw new AssertionError("Agreement text incorrect. Expected to contain: '"+expectedAgreementText+"' Found: '" + agreementText.getText().trim() + "'");
        System.out.println("Agreement text: '" + agreementText.getText().trim() + "' - Verified");
    }

    public OtpVerificationPage clickContinueWithMobile(String mobileNumber) {
        System.out.println("\n--- Performing Login Action to Reach OTP Page ---");
        WebElement mobileInputElement = wait.until(ExpectedConditions.visibilityOfElementLocated(mobileInputLocator));
        mobileInputElement.sendKeys(mobileNumber);
        System.out.println("Entered mobile number: " + mobileNumber);

        WebElement continueButtonElement = wait.until(ExpectedConditions.elementToBeClickable(continueButtonLocator));
        continueButtonElement.click();
        System.out.println("Clicked 'Continue' button.");

        String expectedOtpPageUrlPart = "/customer/otp";
        wait.until(ExpectedConditions.urlContains(expectedOtpPageUrlPart));
        System.out.println("Successfully navigated to OTP page. Current URL: " + driver.getCurrentUrl());
        return new OtpVerificationPage(driver, wait, mobileNumber);
    }

    private void verifyLinkNavigation(By linkLocator, String expectedUrlPartial, String linkType) {
         System.out.println("\n--- Verifying '" + linkType + "' Link ---");
         // Ensure we are on the login page before trying to find the link again
         if (!driver.getCurrentUrl().equals(loginPageUrl)) {
             System.out.println("Not on login page for link check. Navigating back to: " + loginPageUrl);
             driver.get(loginPageUrl);
             wait.until(ExpectedConditions.urlToBe(loginPageUrl));
             wait.until(ExpectedConditions.visibilityOfElementLocated(mobileInputLocator));
         }

         WebElement linkElement = wait.until(ExpectedConditions.elementToBeClickable(linkLocator));

         if (!linkElement.isDisplayed()) {
             throw new AssertionError("'" + linkType + "' link is not displayed.");
         }
         if (!linkElement.isEnabled()) {
             throw new AssertionError("'" + linkType + "' link is not clickable.");
         }
         System.out.println("'" + linkType + "' link is present and clickable - Verified");

         String originalWindowHandle = driver.getWindowHandle();
         Set<String> originalWindowHandles = driver.getWindowHandles();

         linkElement.click();

         wait.until(drv -> drv.getWindowHandles().size() > originalWindowHandles.size() || !drv.getCurrentUrl().equals(loginPageUrl));

         Set<String> allWindowHandles = driver.getWindowHandles();
         String newWindowHandle = originalWindowHandle;

         if (allWindowHandles.size() > originalWindowHandles.size()) {
             for (String handle : allWindowHandles) {
                 if (!originalWindowHandles.contains(handle)) {
                     newWindowHandle = handle;
                     break;
                 }
             }
             driver.switchTo().window(newWindowHandle);
             System.out.println("Switched to new tab/window for " + linkType);
         } else {
             System.out.println(linkType + " link likely opened in the same tab.");
         }

         wait.until(ExpectedConditions.urlContains(expectedUrlPartial.toLowerCase()));
         String currentUrl = driver.getCurrentUrl();
         if (!currentUrl.toLowerCase().contains(expectedUrlPartial.toLowerCase())) {
             String errorMsg = "URL after clicking '" + linkType + "' does not contain '" + expectedUrlPartial + "'. Actual URL: " + currentUrl;
             if (!newWindowHandle.equals(originalWindowHandle)) {
                 driver.close();
                 driver.switchTo().window(originalWindowHandle);
             } else {
                 driver.navigate().back();
                 wait.until(ExpectedConditions.urlToBe(loginPageUrl));
             }
             throw new AssertionError(errorMsg);
         }
         System.out.println("'" + linkType + "' link opened URL: " + currentUrl + " - Verified");

         if (!newWindowHandle.equals(originalWindowHandle)) {
             driver.close();
             driver.switchTo().window(originalWindowHandle);
             System.out.println("Closed " + linkType + " tab and switched back to main window.");
         } else {
             driver.navigate().back();
             wait.until(ExpectedConditions.urlToBe(loginPageUrl));
             System.out.println("Navigated back to login page after " + linkType + " check.");
         }
    }

    public void verifyTermsOfServiceLink() {
        verifyLinkNavigation(termsLinkLocator, expectedTermsUrlPartial, "Terms of Service");
    }

    public void verifyPrivacyPolicyLink() {
        verifyLinkNavigation(privacyLinkLocator, expectedPrivacyUrlPartial, "Privacy Policy");
    }
}