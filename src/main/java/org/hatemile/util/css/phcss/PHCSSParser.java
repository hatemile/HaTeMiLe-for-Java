/*
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package org.hatemile.util.css.phcss;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.parser.ParseException;
import com.helger.css.reader.CSSReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hatemile.util.css.StyleSheetParser;
import org.hatemile.util.css.StyleSheetRule;
import org.hatemile.util.html.HTMLDOMElement;
import org.hatemile.util.html.HTMLDOMParser;

/**
 * The PHCSSParser class is official implementation of
 * {@link org.hatemile.util.css.StyleSheetParser} for ph-css.
 */
public class PHCSSParser implements StyleSheetParser {

    /**
     * The ph-css stylesheet.
     */
    protected final CascadingStyleSheet styleSheet;

    /**
     * Initializes a new object that encapsulate the ph-css parser.
     * @param htmlParser The HTML parser.
     * @param currentURL The current URL of page.
     */
    public PHCSSParser(final HTMLDOMParser htmlParser,
            final String currentURL) {
        try {
            styleSheet = createParser(htmlParser, new URL(currentURL));
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Initializes a new object that encapsulate the ph-css parser.
     * @param htmlParser The HTML parser.
     * @param currentURL The current URL of page.
     */
    public PHCSSParser(final HTMLDOMParser htmlParser,
            final URL currentURL) {
        styleSheet = createParser(htmlParser, currentURL);
    }

    /**
     * Initializes a new object that encapsulate the ph-css parser.
     * @param cssCode The source code of CSS.
     */
    public PHCSSParser(final String cssCode) {
        styleSheet = createParser(cssCode);
    }

    /**
     * Returns the absolute path of a URL.
     * @param currentURL The current URL of document.
     * @param otherURL The other URL.
     * @return The absolute path of other URL.
     */
    protected URL getAbsolutePath(final URL currentURL, final String otherURL) {
        try {
            if (otherURL.startsWith("https://")
                    || otherURL.startsWith("http://")) {
                return new URL(otherURL);
            } else if (otherURL.startsWith("data:")) {
                return null;
            } else if (otherURL.startsWith("//")) {
                return new URL(currentURL.getProtocol() + ":" + otherURL);
            } else {
                if (otherURL.startsWith("/")) {
                    return new URL(currentURL.getProtocol() + "://"
                            + currentURL.getHost() + ":"
                            + Integer.toString(currentURL.getPort())
                            + otherURL);
                } else {
                    Stack<String> stackURL = new Stack<String>();
                    String currentPath = currentURL.getPath() + "a";
                    for (String pathPart : currentPath.split("/")) {
                        stackURL.push(pathPart);
                    }
                    stackURL.pop();
                    for (String relativePart : otherURL.split("/")) {
                        if (relativePart.equals("..")) {
                            stackURL.pop();
                        } else if (!relativePart.equals(".")) {
                            stackURL.push(relativePart);
                        }
                    }
                    String path = "";
                    for (Object object : stackURL.toArray()) {
                        String string = (String) object;
                        if (path.isEmpty()) {
                            path = string;
                        } else {
                            path += "/" + string;
                        }
                    }
                    return new URL(currentURL.getProtocol() + "://"
                            + currentURL.getHost() + ":"
                            + Integer.toString(currentURL.getPort()) + "/"
                            + path);
                }
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Returns the content of URL.
     * @param cssURL The URL.
     * @return The content of URL.
     */
    protected final String getContentFromURL(final URL cssURL) {
        try {
            StringBuilder response = new StringBuilder();
            URLConnection connection = cssURL.openConnection();
            connection.connect();
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine()).append("\n");
            }
            return response.toString();
        } catch (IOException ex) {
            Logger.getLogger(PHCSSParser.class.getName()).log(Level.SEVERE,
                    null, ex);
            return "";
        }
    }

    /**
     * Create the ph-css stylesheet.
     * @param htmlParser The HTML parser.
     * @param currentURL The current URL of page.
     * @return The ph-css stylesheet.
     */
    protected final CascadingStyleSheet createParser(
            final HTMLDOMParser htmlParser, final URL currentURL) {
        StringBuilder cssCode = new StringBuilder();

        List<HTMLDOMElement> elements = htmlParser.find("style,"
                + "link[rel=stylesheet]").listResults();
        for (HTMLDOMElement element : elements) {
            if (element.getTagName().equals("STYLE")) {
                cssCode.append(element.getTextContent());
            } else {
                cssCode.append(getContentFromURL(getAbsolutePath(currentURL,
                        element.getAttribute("href"))));
            }
        }

        return createParser(cssCode.toString());
    }

    /**
     * Create the ph-css stylesheet.
     * @param cssCode The source code of CSS.
     * @return The ph-css stylesheet.
     */
    protected final CascadingStyleSheet createParser(final String cssCode) {
        CascadingStyleSheet cascadingStyleSheet =
                CSSReader.readFromString(cssCode, ECSSVersion.CSS30);

        if (cascadingStyleSheet == null) {
            throw new RuntimeException(new ParseException("CSS parsing error"));
        }

        return cascadingStyleSheet;
    }

    /**
     * {@inheritDoc}
     */
    public List<StyleSheetRule> getRules(final Collection<String> properties) {
        List<StyleSheetRule> rules = new ArrayList<StyleSheetRule>();
        for (CSSStyleRule rule : styleSheet.getAllStyleRules()) {
            PHCSSRule atomicRule = new PHCSSRule(rule);
            for (String property : properties) {
                if (atomicRule.hasProperty(property)) {
                    rules.add(atomicRule);
                    break;
                }
            }
        }
        return rules;
    }
}
