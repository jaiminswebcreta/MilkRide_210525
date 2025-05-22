package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;

public class OtpVerificationPage {

    private WebDriver driver;
    private WebDriverWait wait;
    // registeredMobileNumber is passed in constructor, not needed as a field if only used in constructor for other classes

    // Locators
    private By backButtonLocator = By.xpath("//i[@class='bi-arrow-left']");
    private By otpHeaderLocator = By.xpath("//h4[contains(text(),'OTP Verification')]");
    private By otpInputFieldsLocator = By.xpath("//input[@name='otp[]']");
    private By resendOtpTextLocator = By.xpath("//p[contains(normalize-space(),'Resend OTP in')] | //span[contains(normalize-space(),'Resend OTP in')]");
    private By submitButtonLocator = By.xpath("//button[normalize-space()='Submit']");


    public OtpVerificationPage(WebDriver driver, WebDriverWait wait, String mobileNumber) {
        this.driver = driver;
        this.wait = wait;
        // this.registeredMobileNumber = mobileNumber; // Only store if needed for methods within THIS class
    }

    public void verifyElements() { // Renamed for clarity
        System.out.println("\n--- Verifying OTP Page Elements ---");

        wait.until(ExpectedConditions.urlContains("/customer/otp"));
        System.out.println("Current URL is OTP page: " + driver.getCurrentUrl() + " - Verified");

        WebElement backButton = wait.until(ExpectedConditions.visibilityOfElementLocated(backButtonLocator));
        if (!backButton.isDisplayed()) throw new AssertionError("Back button on OTP page is not displayed.");
        System.out.println("Back button is displayed - Verified");

        WebElement otpHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(otpHeaderLocator));
        String headerText = otpHeader.getText().trim();
        if (!headerText.startsWith("OTP Verification"))
            throw new AssertionError("OTP page header text does not start with 'OTP Verification'. Found: " + headerText);
        System.out.println("OTP Header text: '" + headerText + "' - Verified (Dynamic part ignored as expected)");

        System.out.println("Skipping verification of 'We have sent a verification code to...' message (DEMO MODE).");

        List<WebElement> otpInputFields = wait.until(ExpectedConditions.numberOfElementsToBe(otpInputFieldsLocator, 4));
        if (otpInputFields.size() != 4)
            throw new AssertionError("Expected 4 OTP input fields, but found " + otpInputFields.size());
        for (WebElement otpField : otpInputFields) {
            if (!otpField.isDisplayed()) throw new AssertionError("An OTP input field (name='otp[]') is not displayed.");
        }
        System.out.println(otpInputFields.size() + " OTP input fields are present and displayed - Verified");

        WebElement resendOtpText = wait.until(ExpectedConditions.visibilityOfElementLocated(resendOtpTextLocator));
        if (!resendOtpText.getText().trim().startsWith("Resend OTP in"))
            throw new AssertionError("'Resend OTP in...' text incorrect. Found: " + resendOtpText.getText().trim());
        System.out.println("Resend OTP text: '" + resendOtpText.getText().trim() + "' (Timer is dynamic) - Verified");

        WebElement submitButton = wait.until(ExpectedConditions.visibilityOfElementLocated(submitButtonLocator));
        if (!submitButton.getText().trim().equalsIgnoreCase("Submit"))
            throw new AssertionError("'Submit' button text incorrect. Found: '" + submitButton.getText().trim());
        if (!submitButton.isDisplayed()) throw new AssertionError("'Submit' button is not displayed.");
        System.out.println("Submit button text: '" + submitButton.getText().trim() + "' - Verified");

        System.out.println("All OTP page elements verified (except 'sent code' message).");
    }
}