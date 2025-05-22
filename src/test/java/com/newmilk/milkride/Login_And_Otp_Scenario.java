package com.newmilk.milkride;

import pages.HomePage;
import pages.LoginPage;
import pages.OtpVerificationPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List; // Import List
import java.util.stream.Collectors; 

public class Login_And_Otp_Scenario { // Renamed for clarity

    WebDriver driver;
    WebDriverWait wait;
    String mobileNumber = "8849776064"; // Your mobile number

    public void setUpBrowser() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");
        // options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        System.out.println("Browser setup complete.");
    }

    public void tearDownBrowser() {
        if (driver != null) {
            driver.quit();
            System.out.println("\nBrowser closed.");
        }
    }

    public void executeTestScenario() throws InterruptedException {
        // 1. Initialize Page Objects
        LoginPage loginPage = new LoginPage(driver, wait);
        // OtpVerificationPage otpPage; // Declare here, instantiate after login

        // 2. Perform actions on Login Page
        loginPage.navigateTo();
        loginPage.verifyPageUrl();
        loginPage.verifyUiElements();

        // Optionally verify links (ensure your loginPage has these methods)
         loginPage.verifyTermsOfServiceLink();
         loginPage.verifyPrivacyPolicyLink();

        // 3. Perform login and get to OTP Page
        OtpVerificationPage otpPage = loginPage.clickContinueWithMobile(mobileNumber);

        // 4. Perform actions on OTP Page
        otpPage.verifyElements();
        HomePage homePage = otpPage.enterOtpFromHeaderAndSubmit(); // This now extracts, enters, and submits OTP

        homePage.verifyNavigationToHomePage(); // Verify elements on the home page
        List<String> headerFailures = homePage.verifyHeaderOptions();
        if (!headerFailures.isEmpty()) {
            String allFailures = headerFailures.stream().collect(Collectors.joining("\n - "));
            throw new AssertionError("Home Page Header Verification Failures:\n - " + allFailures);
        }
        System.out.println("All Home Page header options verified successfully.");


        System.out.println("\n\n*** Login, OTP, and Home Page full verification completed successfully! ***");
    }


    public static void main(String[] args) {
        Login_And_Otp_Scenario testScenario = new Login_And_Otp_Scenario();
        try {
            testScenario.setUpBrowser();
            testScenario.executeTestScenario();
        } catch (AssertionError e) {
            System.err.println("\n\nXXX VERIFICATION FAILED: " + e.getMessage() + " XXX");
            // e.printStackTrace();
        } catch (Exception e) {
            System.err.println("\n\nXXX AN UNEXPECTED ERROR OCCURRED: " + e.getMessage() + " XXX");
            e.printStackTrace();
        } finally {
            testScenario.tearDownBrowser();
        }
    }
}