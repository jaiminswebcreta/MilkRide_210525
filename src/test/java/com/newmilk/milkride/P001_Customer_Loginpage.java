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

    // IMPORTANT: UPDATE THESE after manually checking where the links actually go
    String expectedTermsUrlPartial = "terms"; // e.g., "terms-of-service" or a unique part of the terms URL
    String expectedPrivacyUrlPartial = "privacy";
    String mobileNumber = "8849776064";// ;e.g., "privacy-policy" or a unique part of the privacy URL

    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless"); // Optional: run in headless mode
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        
        wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Increased wait time
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

        // You can add a title check if you know the expected title
        // String expectedTitle = "MilkRide Milkmates - Sign In";
        // wait.until(ExpectedConditions.titleIs(expectedTitle));
        // if (!driver.getTitle().equals(expectedTitle)) {
        //     throw new AssertionError("Login page title is incorrect. Expected: " + expectedTitle + ", Actual: " + driver.getTitle());
        // }
        // System.out.println("Page Title: " + driver.getTitle() + " - Verified");
    }

    public void verifyUiElementsText() {
        System.out.println("\n--- Verifying UI Elements Text and Presence ---");

        // Verify Logo (presence)
        WebElement logo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//img[@alt='Logo']")));
        if (!logo.isDisplayed()) {
            throw new AssertionError("Logo is not displayed.");
        }
        System.out.println("Logo is displayed - Verified");

        // Verify "Log in or Sign up" text
        WebElement loginSignUpHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h4[normalize-space()='Log in or Sign up']")));
        if (!loginSignUpHeader.getText().trim().equals("Log in or Sign up")) {
            throw new AssertionError("'Log in or Sign up' text is incorrect or not found. Found: '" + loginSignUpHeader.getText().trim() + "'");
        }
        System.out.println("Header text: '" + loginSignUpHeader.getText().trim() + "' - Verified");

        // Verify "+91" country code
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
        
//        WebElement numberElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//input[@id='mobileNumber'])[1]")));
//        numberElement.sendKeys(mobileNumber);
         

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
        if (!agreementText.getText().contains(expectedAgreementText)) { // Using contains as there might be extra spaces
            throw new AssertionError("Agreement text is incorrect or not found. Expected to contain: '"+expectedAgreementText+"' Found: '" + agreementText.getText().trim() + "'");
        }
        System.out.println("Agreement text: '" + agreementText.getText().trim() + "' - Verified");
    }

    private void verifyLink(String linkText, String expectedUrlPartial, String linkType) {
        System.out.println("\n--- Verifying '" + linkType + "' Link ---");
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

        // Wait for a new window or tab to open, or for URL to change in current tab
        wait.until(driver -> driver.getWindowHandles().size() > originalWindowHandles.size() || !driver.getCurrentUrl().equals(loginPageUrl));


        Set<String> allWindowHandles = driver.getWindowHandles();
        String newWindowHandle = originalWindowHandle; // Assume same window unless a new one is found

        if (allWindowHandles.size() > originalWindowHandles.size()) { // New tab/window opened
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


        wait.until(ExpectedConditions.urlContains(expectedUrlPartial));
        String currentUrl = driver.getCurrentUrl();
        if (!currentUrl.toLowerCase().contains(expectedUrlPartial.toLowerCase())) {
            String errorMsg = "URL after clicking '" + linkType + "' does not contain '" + expectedUrlPartial + "'. Actual URL: " + currentUrl;
            if (!newWindowHandle.equals(originalWindowHandle)) { // If new tab/window was opened
                driver.close();
                driver.switchTo().window(originalWindowHandle);
            } else {
                driver.navigate().back(); // Go back if in the same tab
                wait.until(ExpectedConditions.urlToBe(loginPageUrl)); // Ensure we are back
            }
            throw new AssertionError(errorMsg);
        }
        System.out.println("'" + linkType + "' link opened URL: " + currentUrl + " - Verified");

        if (!newWindowHandle.equals(originalWindowHandle)) { // If new tab/window was opened
            driver.close(); // Close the new tab/window
            driver.switchTo().window(originalWindowHandle); // Switch back to the original window
            System.out.println("Closed " + linkType + " tab and switched back to main window.");
        } else { // Link opened in the same tab
            driver.navigate().back(); // Navigate back to the login page
            wait.until(ExpectedConditions.urlToBe(loginPageUrl)); // Ensure we are back
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

    public static void main(String[] args) {
    	P001_Customer_Loginpage verifier = new P001_Customer_Loginpage();
        try {
            verifier.setUp();
            verifier.verifyPageUrlAndTitle();
            verifier.verifyUiElementsText();
            verifier.verifyTermsOfServiceLink();
            // Ensure the page is in the correct state before verifying the next link
            if (!verifier.driver.getCurrentUrl().equals(verifier.loginPageUrl)) {
                 verifier.driver.get(verifier.loginPageUrl); // Re-navigate if necessary
                 verifier.wait.until(ExpectedConditions.urlToBe(verifier.loginPageUrl));
                 System.out.println("Re-navigated to login page before Privacy Policy check.");
            }
            verifier.verifyPrivacyPolicyLink();
            System.out.println("\n\n*** All verifications completed successfully! ***");
        } catch (AssertionError e) {
            System.err.println("\n\nXXX VERIFICATION FAILED: " + e.getMessage() + " XXX");
            // e.printStackTrace(); // Uncomment for full stack trace
        } catch (Exception e) {
            System.err.println("\n\nXXX AN UNEXPECTED ERROR OCCURRED: " + e.getMessage() + " XXX");
            e.printStackTrace();
        } finally {
            verifier.tearDown();
        }
    }
}