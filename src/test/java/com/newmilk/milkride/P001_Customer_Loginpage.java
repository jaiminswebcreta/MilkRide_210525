package com.newmilk.milkride;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.time.Duration;
import java.util.Set;

public class P001_Customer_Loginpage {

    WebDriver driver;
    WebDriverWait wait;
    String loginPageUrl = "https://dev.milkride.com/milkmates/signin";
    String expectedTermsUrlPartial = "terms";
    String expectedPrivacyUrlPartial = "privacy";
    String mobileNumber = "8849776064"; // Your mobile number

    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.get(loginPageUrl);
        System.out.println("Browser setup complete and navigated to login page.");
    }

    public void verifyPageUrlAndTitle() {
        System.out.println("\n--- Verifying Page URL and Title ---");
        wait.until(ExpectedConditions.urlToBe(loginPageUrl));
        if (!driver.getCurrentUrl().equals(loginPageUrl)) {
            throw new AssertionError("Login page URL is incorrect. Expected: " + loginPageUrl + ", Actual: " + driver.getCurrentUrl());
        }
        System.out.println("Current URL: " + driver.getCurrentUrl() + " - Verified");
    }

    public void verifyUiElementsText() {
        System.out.println("\n--- Verifying UI Elements Text and Presence ---");

        // Verify Logo (presence) - Using your existing confirmed XPath
        WebElement logo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//img[@alt='Logo']")));
        if (!logo.isDisplayed()) {
            throw new AssertionError("Logo is not displayed.");
        }
        System.out.println("Logo is displayed - Verified");

        // Verify "Log in or Sign up" text - Using your existing confirmed XPath
        WebElement loginSignUpHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h4[normalize-space()='Log in or Sign up']")));
        if (!loginSignUpHeader.getText().trim().equals("Log in or Sign up")) {
            throw new AssertionError("'Log in or Sign up' text is incorrect or not found. Found: '" + loginSignUpHeader.getText().trim() + "'");
        }
        System.out.println("Header text: '" + loginSignUpHeader.getText().trim() + "' - Verified");

        // Verify "+91" country code - Using your existing confirmed XPath
        WebElement countryCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@id='basic-addon1']")));
        if (!countryCode.getText().trim().equals("+91")) {
            throw new AssertionError("Country code '+91' is incorrect or not found. Found: '" + countryCode.getText().trim() + "'");
        }
        System.out.println("Country code text: '" + countryCode.getText().trim() + "' - Verified");

        // Verify "Enter Mobile Number" placeholder
        WebElement mobileInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Enter Mobile Number']")));
        if (!mobileInput.getAttribute("placeholder").equals("Enter Mobile Number")) {
            throw new AssertionError("Mobile number input placeholder text is incorrect. Found: '" + mobileInput.getAttribute("placeholder") + "'");
        }
        System.out.println("Mobile input placeholder: '" + mobileInput.getAttribute("placeholder") + "' - Verified");

        // Verify "Continue" button text and state
        WebElement continueButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[normalize-space()='Continue']")));
        if (!continueButton.getText().trim().equals("Continue")) {
            throw new AssertionError("'Continue' button text is incorrect. Found: '" + continueButton.getText().trim() + "'");
        }
        if (!continueButton.isDisplayed()) {
            throw new AssertionError("'Continue' button is not displayed.");
        }
        System.out.println("Continue button text: '" + continueButton.getText().trim() + "' - Verified");

        // Verify "By continuing, you agree to our Terms of Service & Privacy Policy" text
        WebElement agreementText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(normalize-space(), 'By continuing, you agree to our')]")));
        String expectedAgreementText = "By continuing, you agree to our Terms of Service & Privacy Policy";
        if (!agreementText.getText().contains(expectedAgreementText)) {
            throw new AssertionError("Agreement text is incorrect or not found. Expected to contain: '" + expectedAgreementText + "' Found: '" + agreementText.getText().trim() + "'");
        }
        System.out.println("Agreement text: '" + agreementText.getText().trim() + "' - Verified");
    }

    // Method to perform login and navigate to OTP page
    public void performLoginToOtpPage() {
        System.out.println("\n--- Performing Login Action to Reach OTP Page ---");
        WebElement mobileInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Enter Mobile Number']")));
        // It's good practice to clear the field first, though usually not necessary for initial input
        // mobileInput.clear();
        mobileInput.sendKeys(mobileNumber);
        System.out.println("Entered mobile number: " + mobileNumber);

        WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Continue']")));
        continueButton.click();
        System.out.println("Clicked 'Continue' button.");

        // Wait for navigation to OTP page. Adjust expected URL part if necessary.
        // Example: "customer/otp" or a unique element on the OTP page.
        String expectedOtpPageUrlPart = "/customer/otp"; // Or "/milkmates/customer/otp" if that's more accurate
        wait.until(ExpectedConditions.urlContains(expectedOtpPageUrlPart));
        System.out.println("Successfully navigated to OTP page. Current URL: " + driver.getCurrentUrl());
    }


    // verifyLink method (keep as is)
    private void verifyLink(String linkText, String expectedUrlPartial, String linkType) {
        System.out.println("\n--- Verifying '" + linkType + "' Link ---");
         // Ensure we are on the login page before trying to find the link again
        if (!driver.getCurrentUrl().equals(loginPageUrl)) {
            System.out.println("Not on login page for link check. Navigating back to: " + loginPageUrl);
            driver.get(loginPageUrl);
            wait.until(ExpectedConditions.urlToBe(loginPageUrl));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Enter Mobile Number']"))); // Wait for a known element
        }

        WebElement linkElement = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(linkText)));

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

        wait.until(driver -> driver.getWindowHandles().size() > originalWindowHandles.size() || !driver.getCurrentUrl().equals(loginPageUrl));

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
        verifyLink("Terms of Service", expectedTermsUrlPartial, "Terms of Service");
    }

    public void verifyPrivacyPolicyLink() {
        verifyLink("Privacy Policy", expectedPrivacyUrlPartial, "Privacy Policy");
    }

    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("\nBrowser closed.");
        }
    }

    // --- UPDATED main method ---
    public static void main(String[] args) {
        P001_Customer_Loginpage loginPageVerifier = new P001_Customer_Loginpage();
        P002_OTPVerification otpPageVerifier = null; // Declare OTP verifier

        try {
            loginPageVerifier.setUp();
            loginPageVerifier.verifyPageUrlAndTitle();
            loginPageVerifier.verifyUiElementsText(); // Verifies elements on the login page

            // Optionally verify links (uncomment if needed for this run)
            // loginPageVerifier.verifyTermsOfServiceLink();
             if (!loginPageVerifier.driver.getCurrentUrl().equals(loginPageVerifier.loginPageUrl)) {
                  loginPageVerifier.driver.get(loginPageVerifier.loginPageUrl);
                  loginPageVerifier.wait.until(ExpectedConditions.urlToBe(loginPageVerifier.loginPageUrl));
                  System.out.println("Re-navigated to login page before Privacy Policy check.");
             }
             loginPageVerifier.verifyPrivacyPolicyLink();

            // Perform login to navigate to the OTP page
            loginPageVerifier.performLoginToOtpPage();

            // Instantiate and use the OTP verifier
            // Pass the existing driver, wait, and mobileNumber to P002
            otpPageVerifier = new P002_OTPVerification(loginPageVerifier.driver, loginPageVerifier.wait, loginPageVerifier.mobileNumber);
            otpPageVerifier.verifyOtpPageElements(); // Call method from P002

            System.out.println("\n\n*** All verifications (Login Page & OTP Page) completed successfully! ***");

        } catch (AssertionError e) {
            System.err.println("\n\nXXX VERIFICATION FAILED: " + e.getMessage() + " XXX");
            // e.printStackTrace(); // Uncomment for full stack trace
        } catch (Exception e) {
            System.err.println("\n\nXXX AN UNEXPECTED ERROR OCCURRED: " + e.getMessage() + " XXX");
            e.printStackTrace();
        } finally {
            // tearDown is in loginPageVerifier, it will close the browser
            if (loginPageVerifier != null) {
                loginPageVerifier.tearDown();
            }
        }
    }
}