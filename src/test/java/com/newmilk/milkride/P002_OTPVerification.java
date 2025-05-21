package com.newmilk.milkride;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class P002_OTPVerification {

    WebDriver driver;
    WebDriverWait wait;
    String registeredMobileNumber; // To verify parts of it on the OTP screen

    // Constructor
    public P002_OTPVerification(WebDriver driver, WebDriverWait wait, String mobileNumber) {
        this.driver = driver;
        this.wait = wait;
        this.registeredMobileNumber = mobileNumber;
    }

    public void verifyOtpPageElements() {
        System.out.println("\n--- Verifying OTP Page Elements ---");

        // 1. Verify OTP Page URL (Optional, but good for confirmation)
        wait.until(ExpectedConditions.urlContains("/customer/otp"));
        System.out.println("Current URL is OTP page: " + driver.getCurrentUrl() + " - Verified");

        // 2. Verify Back Button (Assuming it's an image or button with specific class/alt)
        //    This XPath is a guess. INSPECT the element for the correct locator.
        WebElement backButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                "//button[contains(@aria-label,'back')] | //img[contains(@alt,'back')] | //i[contains(@class,'back-arrow')]"
        )));
        if (!backButton.isDisplayed()) {
            throw new AssertionError("Back button on OTP page is not displayed.");
        }
        System.out.println("Back button is displayed - Verified");

        // 3. Verify "OTP Verification" Header and partial phone number
        //    The "4770" seems to be the last 4 digits of your example number. Let's try to verify that.
        String lastFourDigits = "";
        if (registeredMobileNumber != null && registeredMobileNumber.length() >= 4) {
            lastFourDigits = registeredMobileNumber.substring(registeredMobileNumber.length() - 4);
        }

        // This XPath looks for a heading containing "OTP Verification"
        // You might need to adjust the tag (h2, h3, h4, etc.) based on inspection
        WebElement otpHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                "//h2[contains(normalize-space(),'OTP Verification')] | //h3[contains(normalize-space(),'OTP Verification')] | //h4[contains(normalize-space(),'OTP Verification')]"
        )));
        String headerText = otpHeader.getText().trim();
        if (!headerText.startsWith("OTP Verification")) {
            throw new AssertionError("OTP page header text does not start with 'OTP Verification'. Found: " + headerText);
        }
        if (!lastFourDigits.isEmpty() && !headerText.contains(lastFourDigits)) {
            System.out.println("Warning: OTP page header does not contain the expected last four digits '" + lastFourDigits + "'. Found: " + headerText + ". This might be acceptable.");
            // Depending on strictness, you might throw an AssertionError here or just log a warning.
        }
        System.out.println("OTP Header text: '" + headerText + "' - Verified (Partial number check: " + (!lastFourDigits.isEmpty() && headerText.contains(lastFourDigits)) + ")");


        // 4. Verify "We have sent a verification code to +91xxxxxxxxxx" text
        WebElement verificationSentText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                "//p[contains(normalize-space(),'We have sent a verification code to')]"
        )));
        String expectedSentTextPart = "We have sent a verification code to";
        String actualSentText = verificationSentText.getText().trim();

        if (!actualSentText.contains(expectedSentTextPart)) {
            throw new AssertionError("Verification code sent text is incorrect. Expected to contain: '" + expectedSentTextPart + "'. Found: '" + actualSentText + "'");
        }
        if (!actualSentText.contains("+" + registeredMobileNumber) && !actualSentText.contains(registeredMobileNumber)) { // Check with or without +91 prefix part of the number string
             throw new AssertionError("Verification code sent text does not contain the correct mobile number. Expected: '" + registeredMobileNumber + "'. Found: '" + actualSentText + "'");
        }
        System.out.println("Verification code sent text: '" + actualSentText + "' - Verified");

        // 5. Verify OTP Input Fields (Assuming 4 input boxes)
        //    This XPath looks for input fields that are typically used for OTPs.
        //    Inspect them for common class names or parent container for a more robust locator.
        List<WebElement> otpInputFields = wait.until(ExpectedConditions.numberOfElementsToBe(
                By.xpath("//div[contains(@class,'otp-input-fields')]/input | //div[contains(@class,'otp-container')]//input | //input[@type='text' and string-length(@id) > 0 and starts-with(@id, 'otp')] | //input[contains(@aria-label, 'otp') or contains(@data-testid, 'otp-input')]"),
                4 // Expected number of OTP fields
        ));
        if (otpInputFields.size() != 4) {
            throw new AssertionError("Expected 4 OTP input fields, but found " + otpInputFields.size());
        }
        for (WebElement otpField : otpInputFields) {
            if (!otpField.isDisplayed()) {
                throw new AssertionError("An OTP input field is not displayed.");
            }
        }
        System.out.println(otpInputFields.size() + " OTP input fields are present and displayed - Verified");

        // 6. Verify "Resend OTP in 00:XXs" text
        //    This text is dynamic due to the timer. We'll check for the static part.
        WebElement resendOtpText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                "//p[contains(normalize-space(),'Resend OTP in')] | //span[contains(normalize-space(),'Resend OTP in')]"
        )));
        if (!resendOtpText.getText().trim().startsWith("Resend OTP in")) {
            throw new AssertionError("'Resend OTP in...' text is not correct or not found. Found: " + resendOtpText.getText().trim());
        }
        System.out.println("Resend OTP text: '" + resendOtpText.getText().trim() + "' (Timer is dynamic) - Verified");

        // 7. Verify "Submit" button
        WebElement submitButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                "//button[normalize-space()='Submit']"
        )));
        if (!submitButton.getText().trim().equalsIgnoreCase("Submit")) {
            throw new AssertionError("'Submit' button text is incorrect. Found: " + submitButton.getText().trim());
        }
        if (!submitButton.isDisplayed()) {
            throw new AssertionError("'Submit' button is not displayed.");
        }
        // Check if it's enabled or disabled (initially might be disabled until OTP is entered)
        // boolean isSubmitEnabled = submitButton.isEnabled();
        // System.out.println("'Submit' button is enabled: " + isSubmitEnabled); // This might be false initially
        System.out.println("Submit button text: '" + submitButton.getText().trim() + "' - Verified");

        System.out.println("All OTP page elements verified.");
    }

    // You can add methods here later to interact with OTP fields, click submit, etc.
    // public void enterOtp(String otpValue) { ... }
    // public void clickSubmit() { ... }
}