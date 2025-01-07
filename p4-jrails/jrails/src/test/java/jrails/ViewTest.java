package jrails;

import org.junit.Test;

import static org.junit.Assert.*;

public class ViewTest {
    @Test
    public void testEmpty() {
        Html html = View.empty();
        assertEquals("", html.toString());
    }

    @Test
    public void testParagraph() {
        Html html = View.p(View.t("Hello World"));
        assertEquals("<p>Hello World</p>", html.toString());
    }

    @Test
    public void testLinkTo() {
        Html html = View.link_to("Google", "https://google.com");
        assertEquals("<a href=\"https://google.com\">Google</a>", html.toString());
    }
}
