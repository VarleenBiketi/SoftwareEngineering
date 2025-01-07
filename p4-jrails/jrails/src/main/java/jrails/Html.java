package jrails;

import java.util.ArrayList;
import java.util.List;

public class Html {
    private final StringBuilder content;

    public Html() {
        content = new StringBuilder();
    }

    public Html(String initialContent) {
        content = new StringBuilder(initialContent);
    }

    @Override
    public String toString() {
        return content.toString();
    }

    public Html seq(Html h) {
        content.append(h.toString());
        return this;
    }

    public Html t(Object o) {
        content.append(o == null ? "" : o.toString());
        return this;
    }

    public Html br() {
        content.append("<br/>");
        return this;
    }

    public Html p(Html child) {
        return wrapTag("p", child);
    }

    public Html div(Html child) {
        return wrapTag("div", child);
    }

    public Html strong(Html child) {
        return wrapTag("strong", child);
    }

    public Html h1(Html child) {
        return wrapTag("h1", child);
    }

    public Html tr(Html child) {
        return wrapTag("tr", child);
    }

    public Html th(Html child) {
        return wrapTag("th", child);
    }

    public Html td(Html child) {
        return wrapTag("td", child);
    }

    public Html table(Html child) {
        return wrapTag("table", child);
    }

    public Html thead(Html child) {
        return wrapTag("thead", child);
    }

    public Html tbody(Html child) {
        return wrapTag("tbody", child);
    }

    public Html textarea(String name, Html child) {
        content.append("<textarea name=\"").append(name).append("\">")
               .append(child.toString())
               .append("</textarea>");
        return this;
    }

    public Html link_to(String text, String url) {
        content.append("<a href=\"").append(url).append("\">")
               .append(text)
               .append("</a>");
        return this;
    }

    public Html form(String action, Html child) {
        content.append("<form action=\"").append(action)
               .append("\" accept-charset=\"UTF-8\" method=\"post\">")
               .append(child.toString())
               .append("</form>");
        return this;
    }

    public Html submit(String value) {
        content.append("<input type=\"submit\" value=\"").append(value).append("\"/>");
        return this;
    }

    private Html wrapTag(String tag, Html child) {
        content.append("<").append(tag).append(">")
               .append(child.toString())
               .append("</").append(tag).append(">");
        return this;
    }
}
