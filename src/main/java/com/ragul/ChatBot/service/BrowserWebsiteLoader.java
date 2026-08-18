package com.ragul.ChatBot.service;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.WaitUntilState;
import org.springframework.stereotype.Service;

@Service
public class BrowserWebsiteLoader {

    public String loadRenderedText(String url) {

        try (Playwright playwright = Playwright.create()) {

            Browser browser = playwright
                    .chromium()
                    .launch(
                            new BrowserType.LaunchOptions()
                                    .setHeadless(true)
                    );

            Page page = browser.newPage();

            page.setDefaultTimeout(15_000);

            page.navigate(
                    url,
                    new Page.NavigateOptions()
                            .setWaitUntil(
                                    WaitUntilState.DOMCONTENTLOADED
                            )
                            .setTimeout(15_000)
            );

            page.waitForTimeout(2000);

            String text = page.locator("body").innerText();

            browser.close();

            return text;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to render website: " + url,
                    e
            );
        }
    }

}
