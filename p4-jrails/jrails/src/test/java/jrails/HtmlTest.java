package jrails;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HtmlTest {
    private Html html;

    @Before
    public void setUp() {
        html = new Html();
    }

    @Test
    public void testEmptyHtml() {
        assertEquals("", html.toString());
    }

    @Test
    public void testTextContent() {
        html.t("Hello, World!");
        assertEquals("Hello, World!", html.toString());
    }

    @Test
    public void testSequentialHtml() {
        Html first = new Html().t("First");
        Html second = new Html().t("Second");
        html.seq(first).seq(second);
        assertEquals("FirstSecond", html.toString());
    }

    @Test
    public void testParagraphTag() {
        html.p(new Html().t("This is a paragraph."));
        assertEquals("<p>This is a paragraph.</p>", html.toString());
    }

    @Test
    public void testNestedTags() {
        html.div(new Html().p(new Html().t("Nested content.")));
        assertEquals("<div><p>Nested content.</p></div>", html.toString());
    }

    @Test
    public void testSelfClosingTag() {
        html.br();
        assertEquals("<br/>", html.toString());
    }

    @Test
    public void testMultipleTags() {
        html.h1(new Html().t("Header")).p(new Html().t("Paragraph"));
        assertEquals("<h1>Header</h1><p>Paragraph</p>", html.toString());
    }

    @Test
    public void testLinkTo() {
        html.link_to("Visit", "/home");
        assertEquals("<a href=\"/home\">Visit</a>", html.toString());
    }

    @Test
    public void testForm() {
        html.form("/submit", new Html().textarea("input", new Html().t("Text content")));
        assertEquals("<form action=\"/submit\" accept-charset=\"UTF-8\" method=\"post\">" +
                     "<textarea name=\"input\">Text content</textarea></form>", html.toString());
    }

    @Test
    public void testSubmitButton() {
        html.submit("Save");
        assertEquals("<input type=\"submit\" value=\"Save\"/>", html.toString());
    }
}