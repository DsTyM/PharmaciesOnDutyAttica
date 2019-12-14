import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);


//        var url = "https://www.foititikanea.gr/";
        var url = "http://www.fsa.gr/duties.asp";
//        var url = "http://www.fsa.gr/%CE%95%CF%86%CE%B7%CE%BC%CE%B5%CF%81%CE%AF%CE%B5%CF%82%CE%A6%CE%B1%CF%81%CE%BC%CE%B1%CE%BA%CE%B5%CE%AF%CF%89%CE%BD/tabid/95/Default.aspx";

        WebClient webClient;
        HtmlPage page;
        HtmlInput input;
        List<HtmlAnchor> anchors;
        List<HtmlPage> pages = new ArrayList<>();

        Document jsoupdoc;
        int numOfPages;

        try {
            webClient = new WebClient(BrowserVersion.CHROME);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
//            webClient.getOptions().setPrintContentOnFailingStatusCode(false);
//            webClient.getOptions().setThrowExceptionOnScriptError(false);
//            webClient.getCookieManager().setCookiesEnabled(true);
//            webClient.setRefreshHandler(new ThreadedRefreshHandler());

            // Load the page

            page = webClient.getPage(url);

            // Select Date

            HtmlSelect select = page.getForms().get(0).getSelectByName("dateduty");
            HtmlOption option = select.getOptionByValue("15/12/2019");
            select.setSelectedAttribute(option, true);

            // if date is today, get(2)
            // else get(1)
            input = page.getForms().get(0).getInputsByValue("").get(1);

            // Click Search

            page = input.click();

            // jsoup Code

            // Convert XPath to jsoup path:
            // https://stackoverflow.com/q/16335820/6151784

            jsoupdoc = Jsoup.parse(page.asXml());
            var numOfPagesAsText = jsoupdoc.select("html body table tbody tr td:eq(1) table tbody tr:eq(4) td table tbody tr td nobr").text().trim();
            // this equals this XPath: /html/body/table/tbody/tr/td[2]/table/tbody/tr[5]/td/table/tbody/tr[1]/td/nobr

            numOfPages = Integer.parseInt(numOfPagesAsText.substring(numOfPagesAsText.lastIndexOf(" ") + 1));
//            System.out.println(numOfPages);


            pages.add(page);
//            System.out.println(page.asXml());

            // Click next until the last page.

            anchors = page.getAnchors();
//            System.out.println(anchors.toArray()[10]);
            for (var i = 0; i < numOfPages - 1; i++) {
                page = anchors.get(10).click();
                pages.add(page);
            }

            for (var singlePage : pages) {
                System.out.println(singlePage.asText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
