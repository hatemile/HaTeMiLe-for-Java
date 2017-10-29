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
package org.hatemile.implementation;

import org.hatemile.AccessibleNavigation;
import org.hatemile.util.CommonFunctions;
import org.hatemile.util.Configure;
import org.hatemile.util.html.HTMLDOMElement;
import org.hatemile.util.html.HTMLDOMParser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * The AccessibleNavigationImplementation class is official implementation of
 * AccessibleNavigation interface.
 */
public class AccessibleNavigationImplementation
        implements AccessibleNavigation {

    /**
     * The id of list element that contains the skippers.
     */
    protected static final String ID_CONTAINER_SKIPPERS = "container-skippers";

    /**
     * The id of list element that contains the links for the headings.
     */
    protected static final String ID_CONTAINER_HEADING = "container-heading";

    /**
     * The id of text of description of container of heading links.
     */
    protected static final String ID_TEXT_HEADING = "text-heading";

    /**
     * The HTML class of anchor of skipper.
     */
    protected static final String CLASS_SKIPPER_ANCHOR = "skipper-anchor";

    /**
     * The HTML class of anchor of heading link.
     */
    protected static final String CLASS_HEADING_ANCHOR = "heading-anchor";

    /**
     * The HTML class of element for show the long description of image.
     */
    protected static final String CLASS_LONG_DESCRIPTION_LINK =
            "longdescription-link";

    /**
     * The name of attribute that links the anchor of skipper with the element.
     */
    protected static final String DATA_ANCHOR_FOR = "data-anchorfor";

    /**
     * The name of attribute that indicates the level of heading of link.
     */
    protected static final String DATA_HEADING_LEVEL = "data-headinglevel";

    /**
     * The name of attribute that links the anchor of heading link with heading.
     */
    protected static final String DATA_HEADING_ANCHOR_FOR =
            "data-headinganchorfor";

    /**
     * The name of attribute that link the anchor of long description with the
     * image.
     */
    protected static final String DATA_LONG_DESCRIPTION_FOR_IMAGE =
            "data-longdescriptionfor";

    /**
     * Level value of h1.
     */
    protected static final int HEADING_LEVEL_1 = 1;

    /**
     * Level value of h2.
     */
    protected static final int HEADING_LEVEL_2 = 2;

    /**
     * Level value of h3.
     */
    protected static final int HEADING_LEVEL_3 = 3;

    /**
     * Level value of h4.
     */
    protected static final int HEADING_LEVEL_4 = 4;

    /**
     * Level value of h5.
     */
    protected static final int HEADING_LEVEL_5 = 5;

    /**
     * Level value of h6.
     */
    protected static final int HEADING_LEVEL_6 = 6;

    /**
     * Level value of invalid heading.
     */
    protected static final int HEADING_LEVEL_INVALID = -1;

    /**
     * The HTML parser.
     */
    protected final HTMLDOMParser parser;

    /**
     * The text of description of container of heading links.
     */
    protected final String textHeading;

    /**
     * The prefix of content of long description, before the image.
     */
    protected final String attributeLongDescriptionPrefixBefore;

    /**
     * The suffix of content of long description, before the image.
     */
    protected final String attributeLongDescriptionSuffixBefore;

    /**
     * The prefix of content of long description, after the image.
     */
    protected final String attributeLongDescriptionPrefixAfter;

    /**
     * The suffix of content of long description, after the image.
     */
    protected final String attributeLongDescriptionSuffixAfter;

    /**
     * The prefix of generated ids.
     */
    protected final String prefixId;

    /**
     * The skippers configured.
     */
    protected final Collection<Map<String, String>> skippers;

    /**
     * The state that indicates if the container of skippers has added.
     */
    protected boolean listSkippersAdded;

    /**
     * The list element of skippers.
     */
    protected HTMLDOMElement listSkippers;

    /**
     * The state that indicates if the sintatic heading of parser be validated.
     */
    protected boolean validateHeading;

    /**
     * The state that indicates if the sintatic heading of parser is correct.
     */
    protected boolean validHeading;

    /**
     * Initializes a new object that manipulate the accessibility of the
     * navigation of parser.
     * @param htmlParser The HTML parser.
     * @param configure The configuration of HaTeMiLe.
     */
    public AccessibleNavigationImplementation(final HTMLDOMParser htmlParser,
            final Configure configure) {
        this(htmlParser, configure, null);
    }

    /**
     * Initializes a new object that manipulate the accessibility of the
     * navigation of parser.
     * @param htmlParser The HTML parser.
     * @param configure The configuration of HaTeMiLe.
     * @param skipperFileName The file path of skippers configuration.
     */
    public AccessibleNavigationImplementation(final HTMLDOMParser htmlParser,
            final Configure configure, final String skipperFileName) {
        this.parser = Objects.requireNonNull(htmlParser);
        prefixId = configure.getParameter("prefix-generated-ids");
        textHeading = configure.getParameter("elements-heading-before");
        attributeLongDescriptionPrefixBefore = configure
                .getParameter("attribute-longdescription-prefix-after");
        attributeLongDescriptionSuffixBefore = configure
                .getParameter("attribute-longdescription-suffix-after");
        attributeLongDescriptionPrefixAfter = configure
                .getParameter("attribute-longdescription-prefix-after");
        attributeLongDescriptionSuffixAfter = configure
                .getParameter("attribute-longdescription-suffix-after");
        skippers = getSkippers(skipperFileName);
        listSkippersAdded = false;
        validateHeading = false;
        validHeading = false;
        listSkippers = null;
    }

    /**
     * Returns the skippers of configuration.
     * @param fileName The file path of skippers configuration.
     * @return The skippers of configuration.
     */
    protected static Collection<Map<String, String>> getSkippers(
            final String fileName) {
        Collection<Map<String, String>> skippers =
                new ArrayList<Map<String, String>>();

        InputStream inputStream = File.class.getResourceAsStream(fileName);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            Element rootElement = document.getDocumentElement();

            if (rootElement.getTagName().equalsIgnoreCase("SKIPPERS")) {
                NodeList nodeListSkippers = rootElement
                        .getElementsByTagName("skipper");

                for (int i = 0, length = nodeListSkippers.getLength();
                        i < length; i++) {
                    Element skipperElement = (Element) nodeListSkippers.item(i);

                    if ((skipperElement.hasAttribute("selector"))
                            && (skipperElement.hasAttribute("description"))
                            && (skipperElement.hasAttribute("shortcut"))) {
                        Map<String, String> skipper =
                                new HashMap<String, String>();
                        skipper.put("selector",
                                skipperElement.getAttribute("selector"));
                        skipper.put("description",
                                skipperElement.getAttribute("description"));
                        skipper.put("shortcut",
                                skipperElement.getAttribute("shortcut"));
                        skippers.add(skipper);
                    }
                }
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(AccessibleNavigationImplementation.class
                        .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return skippers;
    }

    /**
     * Generate the list of skippers of page.
     * @return The list of skippers of page.
     */
    protected HTMLDOMElement generateListSkippers() {
        HTMLDOMElement container = parser.find("#" + ID_CONTAINER_SKIPPERS)
                .firstResult();
        HTMLDOMElement htmlList = null;
        if (container == null) {
            HTMLDOMElement local = parser.find("body").firstResult();
            if (local != null) {
                container = parser.createElement("div");
                container.setAttribute("id", ID_CONTAINER_SKIPPERS);
                local.getFirstElementChild().insertBefore(container);
            }
        }
        if (container != null) {
            htmlList = parser.find(container).findChildren("ul").firstResult();
            if (htmlList == null) {
                htmlList = parser.createElement("ul");
                container.appendElement(htmlList);
            }
        }
        listSkippersAdded = true;

        return htmlList;
    }

    /**
     * Generate the list of heading links of page.
     * @return The list of heading links of page.
     */
    protected HTMLDOMElement generateListHeading() {
        HTMLDOMElement container = parser.find("#" + ID_CONTAINER_HEADING)
                .firstResult();
        HTMLDOMElement htmlList = null;
        if (container == null) {
            HTMLDOMElement local = parser.find("body").firstResult();
            if (local != null) {
                container = parser.createElement("div");
                container.setAttribute("id", ID_CONTAINER_HEADING);

                HTMLDOMElement textContainer = parser.createElement("span");
                textContainer.setAttribute("id", ID_TEXT_HEADING);
                textContainer.appendText(textHeading);

                container.appendElement(textContainer);
                local.appendElement(container);
            }
        }
        if (container != null) {
            htmlList = parser.find(container).findChildren("ol").firstResult();
            if (htmlList == null) {
                htmlList = parser.createElement("ol");
                container.appendElement(htmlList);
            }
        }
        return htmlList;
    }

    /**
     * Returns the level of heading.
     * @param element The heading.
     * @return The level of heading.
     */
    protected int getHeadingLevel(final HTMLDOMElement element) {
        String tag = element.getTagName();
        if (tag.equals("H1")) {
            return HEADING_LEVEL_1;
        } else if (tag.equals("H2")) {
            return HEADING_LEVEL_2;
        } else if (tag.equals("H3")) {
            return HEADING_LEVEL_3;
        } else if (tag.equals("H4")) {
            return HEADING_LEVEL_4;
        } else if (tag.equals("H5")) {
            return HEADING_LEVEL_5;
        } else if (tag.equals("H6")) {
            return HEADING_LEVEL_6;
        } else {
            return HEADING_LEVEL_INVALID;
        }
    }

    /**
     * Inform if the headings of page are sintatic correct.
     * @return True if the headings of page are sintatic correct or false if
     * not.
     */
    protected boolean isValidHeading() {
        Collection<HTMLDOMElement> elements = parser.find("h1,h2,h3,h4,h5,h6")
                .listResults();
        int lastLevel = 0;
        int countMainHeading = 0;
        int level;
        validateHeading = true;
        for (HTMLDOMElement element : elements) {
            level = getHeadingLevel(element);
            if (level == 1) {
                if (countMainHeading == 1) {
                    return false;
                } else {
                    countMainHeading = 1;
                }
            }
            if ((level - lastLevel) > 1) {
                return false;
            }
            lastLevel = level;
        }
        return true;
    }

    /**
     * Generate an anchor for the element.
     * @param element The element.
     * @param dataAttribute The name of attribute that links the element with
     * the anchor.
     * @param anchorClass The HTML class of anchor.
     * @return The anchor.
     */
    protected HTMLDOMElement generateAnchorFor(final HTMLDOMElement element,
            final String dataAttribute, final String anchorClass) {
        CommonFunctions.generateId(element, prefixId);
        HTMLDOMElement anchor = null;
        if (parser.find("[" + dataAttribute + "=\"" + element
                .getAttribute("id") + "\"]").firstResult() == null) {
            if (element.getTagName().equals("A")) {
                anchor = element;
            } else {
                anchor = parser.createElement("a");
                CommonFunctions.generateId(anchor, prefixId);
                anchor.setAttribute("class", anchorClass);
                element.insertBefore(anchor);
            }
            if (!anchor.hasAttribute("name")) {
                anchor.setAttribute("name", anchor.getAttribute("id"));
            }
            anchor.setAttribute(dataAttribute, element.getAttribute("id"));
        }
        return anchor;
    }

    /**
     * Replace the shortcut of elements, that has the shortcut passed.
     * @param shortcut The shortcut.
     */
    protected void freeShortcut(final String shortcut) {
        String shortcuts;
        String key;
        boolean found = false;
        String alphaNumbers = "1234567890abcdefghijklmnopqrstuvwxyz";
        Collection<HTMLDOMElement> elements = parser.find("[accesskey]")
                .listResults();
        for (HTMLDOMElement element : elements) {
            shortcuts = element.getAttribute("accesskey").toLowerCase();
            if (CommonFunctions.inList(shortcuts, shortcut)) {
                for (int i = 0, length = alphaNumbers.length(); i < length;
                        i++) {
                    key = Character.toString(alphaNumbers.charAt(i));
                    found = true;
                    for (HTMLDOMElement elementWithShortcuts : elements) {
                        shortcuts = elementWithShortcuts
                                .getAttribute("accesskey").toLowerCase();
                        if (CommonFunctions.inList(shortcuts, key)) {
                            found = false;
                            break;
                        }
                    }
                    if (found) {
                        element.setAttribute("accesskey", key);
                        break;
                    }
                }
                if (found) {
                    break;
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void provideNavigationBySkipper(final HTMLDOMElement element) {
        Map<String, String> skipper = null;
        Collection<HTMLDOMElement> auxiliarElements;
        for (Map<String, String> auxiliarSkipper : skippers) {
            auxiliarElements = parser.find(auxiliarSkipper.get("selector"))
                    .listResults();
            for (HTMLDOMElement auxiliarElement : auxiliarElements) {
                if (auxiliarElement.equals(element)) {
                    skipper = auxiliarSkipper;
                    break;
                }
            }
            if (skipper != null) {
                break;
            }
        }

        if (skipper != null) {
            if (!listSkippersAdded) {
                listSkippers = generateListSkippers();
            }
            if (listSkippers != null) {
                HTMLDOMElement anchor = generateAnchorFor(element,
                        DATA_ANCHOR_FOR, CLASS_SKIPPER_ANCHOR);
                if (anchor != null) {
                    HTMLDOMElement itemLink = parser.createElement("li");
                    HTMLDOMElement link = parser.createElement("a");
                    link.setAttribute("href",
                            "#" + anchor.getAttribute("name"));
                    link.appendText(skipper.get("description"));

                    List<String> shortcuts = Arrays
                            .asList(skipper.get("shortcut").split(" "));
                    if (!shortcuts.isEmpty()) {
                        String shortcut = shortcuts.get(0);
                        if (!shortcut.isEmpty()) {
                            freeShortcut(shortcut);
                            link.setAttribute("accesskey", shortcut);
                        }
                    }
                    CommonFunctions.generateId(link, prefixId);

                    itemLink.appendElement(link);
                    listSkippers.appendElement(itemLink);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void provideNavigationByAllSkippers() {
        Collection<HTMLDOMElement> elements;
        for (Map<String, String> skipper : skippers) {
            elements = parser.find(skipper.get("selector")).listResults();
            for (HTMLDOMElement element : elements) {
                if (CommonFunctions.isValidElement(element)) {
                    provideNavigationBySkipper(element);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void provideNavigationByHeading(final HTMLDOMElement element) {
        if (!validateHeading) {
            validHeading = isValidHeading();
        }
        if (validHeading) {
            HTMLDOMElement anchor = generateAnchorFor(element,
                    DATA_HEADING_ANCHOR_FOR, CLASS_HEADING_ANCHOR);
            if (anchor != null) {
                HTMLDOMElement list = null;
                int level = getHeadingLevel(element);
                if (level == 1) {
                    list = generateListHeading();
                } else {
                    HTMLDOMElement superItem = parser
                            .find("#" + ID_CONTAINER_HEADING)
                            .findDescendants("[" + DATA_HEADING_LEVEL + "=\""
                                + Integer.toString(level - 1) + "\"]")
                            .lastResult();
                    if (superItem != null) {
                        list = parser.find(superItem).findChildren("ol")
                                .firstResult();
                        if (list == null) {
                            list = parser.createElement("ol");
                            superItem.appendElement(list);
                        }
                    }
                }
                if (list != null) {
                    HTMLDOMElement item = parser.createElement("li");
                    item.setAttribute(DATA_HEADING_LEVEL,
                            Integer.toString(level));

                    HTMLDOMElement link = parser.createElement("a");
                    link.setAttribute("href",
                            "#" + anchor.getAttribute("name"));
                    link.appendText(element.getTextContent());

                    item.appendElement(link);
                    list.appendElement(item);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void provideNavigationByAllHeadings() {
        Collection<HTMLDOMElement> elements = parser.find("h1,h2,h3,h4,h5,h6")
                .listResults();
        for (HTMLDOMElement element : elements) {
            if (CommonFunctions.isValidElement(element)) {
                provideNavigationByHeading(element);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void provideNavigationToLongDescription(final HTMLDOMElement image) {
        if (image.hasAttribute("longdesc")) {
            CommonFunctions.generateId(image, prefixId);
            String id = image.getAttribute("id");
            if (parser.find("[" + DATA_LONG_DESCRIPTION_FOR_IMAGE + "=\""
                    + id + "\"]").firstResult() == null) {
                if (image.hasAttribute("alt")) {
                    if (!(attributeLongDescriptionPrefixBefore.isEmpty()
                            || attributeLongDescriptionSuffixBefore
                                .isEmpty())) {
                        String beforeText = attributeLongDescriptionPrefixBefore
                                + " " + image.getAttribute("alt") + " "
                                + attributeLongDescriptionSuffixBefore;
                        HTMLDOMElement beforeAnchor = parser.createElement("a");
                        beforeAnchor.setAttribute("href",
                                image.getAttribute("longdesc"));
                        beforeAnchor.setAttribute("target", "_blank");
                        beforeAnchor
                                .setAttribute(DATA_LONG_DESCRIPTION_FOR_IMAGE,
                                    id);
                        beforeAnchor.setAttribute("class",
                                CLASS_LONG_DESCRIPTION_LINK);
                        beforeAnchor.appendText(beforeText.trim());
                        image.insertBefore(beforeAnchor);
                    }
                    if (!(attributeLongDescriptionPrefixAfter.isEmpty()
                            || attributeLongDescriptionSuffixAfter.isEmpty())) {
                        String afterText = attributeLongDescriptionPrefixAfter
                                + " " + image.getAttribute("alt") + " "
                                + attributeLongDescriptionSuffixAfter;
                        HTMLDOMElement afterAnchor = parser.createElement("a");
                        afterAnchor.setAttribute("href",
                                image.getAttribute("longdesc"));
                        afterAnchor.setAttribute("target", "_blank");
                        afterAnchor
                                .setAttribute(DATA_LONG_DESCRIPTION_FOR_IMAGE,
                                    id);
                        afterAnchor.setAttribute("class",
                                CLASS_LONG_DESCRIPTION_LINK);
                        afterAnchor.appendText(afterText.trim());
                        image.insertAfter(afterAnchor);
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void provideNavigationToAllLongDescriptions() {
        Collection<HTMLDOMElement> images = parser.find("[longdesc]")
                .listResults();
        for (HTMLDOMElement image : images) {
            if (CommonFunctions.isValidElement(image)) {
                provideNavigationToLongDescription(image);
            }
        }
    }
}
