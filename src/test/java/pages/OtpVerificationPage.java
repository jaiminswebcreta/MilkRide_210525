package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OtpVerificationPage {

    private WebDriver driver;
    private WebDriverWait wait;
    // private String registeredMobileNumber; // Not strictly needed if only used for display

    // Locators
    private By backButtonLocator = By.xpath("//i[@class='bi-arrow-left']");
    private By otpHeaderLocator = By.xpath("//h4[contains(text(),'OTP Verification')]"); // To extract OTP
    private By otpInputFieldsLocator = By.xpath("//input[@name='otp[]']");
    private By resendOtpTextLocator = By.xpath("//p[contains(normalize-space(),'Resend OTP in')] | //span[contains(normalize-space(),'Resend OTP in')]");
    // private By submitButtonLocator = By.xpath("//button[normalize-space()='Submit']"); // Your previous XPath
    private By submitButtonLocator = By.xpath("//button[@id='submitButton']"); // Your NEW XPath from the image

    // Locators for Home Page verification
    private By successfulLoginToastLocator = By.xpath("(//div[@class='toast-body'])"); // Adjust if needed
    private By homePageUniqueElementLocator = By.xpath("(//h4[normalize-space()='Categories'])[1]"); // Example: "Categories" header on home page


    public OtpVerificationPage(WebDriver driver, WebDriverWait wait, String mobileNumber) {
        this.driver = driver;
        this.wait = wait;
        // this.registeredMobileNumber = mobileNumber;
    }

    private String extractOtpFromHeader() {
        WebElement otpHeaderElement = wait.until(ExpectedConditions.visibilityOfElementLocated(otpHeaderLocator));
        String headerText = otpHeaderElement.getText().trim(); // e.g., "OTP Verification 4856"

        // Use regex to extract the 4-digit OTP
        Pattern pattern = Pattern.compile("(\\d{4})$"); // Matches 4 digits at the end of the string
        Matcher matcher = pattern.matcher(headerText);
        if (matcher.find()) {
            return matcher.group(1); // group(1) is the captured 4 digits
        }
        System.err.println("Could not extract OTP from header text: " + headerText);
        throw new AssertionError("Could not extract OTP from header: " + headerText);
    }

    public void enterOtp(String otp) {
        List<WebElement> otpFields = wait.until(ExpectedConditions.numberOfElementsToBe(otpInputFieldsLocator, 4));
        if (otp.length() != 4) {
            throw new IllegalArgumentException("OTP must be 4 digits long. Received: " + otp);
        }
        for (int i = 0; i < otpFields.size(); i++) {
            otpFields.get(i).sendKeys(String.valueOf(otp.charAt(i)));
        }
        System.out.println("Entered OTP: " + otp);
    }

    public HomePage clickSubmit() { // Method now returns HomePage object
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(submitButtonLocator));
        submitButton.click();
        System.out.println("Clicked Submit button on OTP page.");
        return new HomePage(driver, wait); // Assuming you'll create a HomePage.java
    }

    public void verifyElements() {
        System.out.println("\n--- Verifying OTP Page Elements (Before OTP Entry) ---");

        wait.until(ExpectedConditions.urlContains("/customer/otp"));
        System.out.println("Current URL is OTP page: " + driver.getCurrentUrl() + " - Verified");

        WebElement backButton = wait.until(ExpectedConditions.visibilityOfElementLocated(backButtonLocator));
        if (!backButton.isDisplayed()) throw new AssertionError("Back button on OTP page is not displayed.");
        System.out.println("Back button is displayed - Verified");

        WebElement otpHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(otpHeaderLocator));
        String headerText = otpHeader.getText().trim();
        if (!headerText.startsWith("OTP Verification"))
            throw new AssertionError("OTP page header text does not start with 'OTP Verification'. Found: " + headerText);
        System.out.println("OTP Header text: '" + headerText + "' - Verified (Dynamic part ignored for now)");

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
        if (!submitButton.getText().trim().equalsIgnoreCase("Submit")) // Ensure submit button text is correct
            throw new AssertionError("'Submit' button text incorrect. Found: '" + submitButton.getText().trim() + "'. Using ID: submitButton");
        if (!submitButton.isDisplayed()) throw new AssertionError("'Submit' button is not displayed.");
        System.out.println("Submit button text: '" + submitButton.getText().trim() + "' - Verified");

        System.out.println("Initial OTP page elements verified.");
    }

    // New combined method for the OTP flow on this page
    public HomePage enterOtpFromHeaderAndSubmit() { // Ensure this method is public
        verifyElements(); // Verify initial state of the page
        String otp = extractOtpFromHeader();
        System.out.println("Extracted OTP from header: " + otp);
        enterOtp(otp);
        return clickSubmit(); // clickSubmit() should return HomePage
    }
}