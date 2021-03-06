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

import java.util.ArrayList;
import java.util.Arrays;
import org.hatemile.AccessibleDisplay;
import org.hatemile.util.CommonFunctions;
import org.hatemile.util.Configure;
import org.hatemile.util.html.HTMLDOMElement;
import org.hatemile.util.html.HTMLDOMParser;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.hatemile.util.IDGenerator;

/**
 * The AccessibleDisplayScreenReaderImplementation class is official
 * implementation of {@link org.hatemile.AccessibleDisplay} for screen readers.
 */
public class AccessibleDisplayScreenReaderImplementation
        implements AccessibleDisplay {

    /**
     * The id of list element that contains the description of shortcuts, before
     * the whole content of page.
     */
    public static final String ID_CONTAINER_SHORTCUTS_BEFORE
            = "container-shortcuts-before";

    /**
     * The id of list element that contains the description of shortcuts, after
     * the whole content of page.
     */
    public static final String ID_CONTAINER_SHORTCUTS_AFTER
            = "container-shortcuts-after";

    /**
     * The HTML class of text of description of container shortcuts.
     */
    public static final String CLASS_TEXT_SHORTCUTS = "text-shortcuts";

    /**
     * The HTML class of content to force the screen reader show the current
     * state of element, before it.
     */
    public static final String CLASS_FORCE_READ_BEFORE = "force-read-before";

    /**
     * The HTML class of content to force the screen reader show the current
     * state of element, after it.
     */
    public static final String CLASS_FORCE_READ_AFTER = "force-read-after";

    /**
     * The name of attribute that links the description of shortcut of element.
     */
    public static final String DATA_ATTRIBUTE_ACCESSKEY_OF
            = "data-attributeaccesskeyof";

    /**
     * The name of attribute that links the content of download link.
     */
    public static final String DATA_ATTRIBUTE_DOWNLOAD_OF =
            "data-attributedownloadof";

    /**
     * The name of attribute that links the content of header cell with the
     * data cell.
     */
    public static final String DATA_ATTRIBUTE_HEADERS_OF = "data-headersof";

    /**
     * The name of attribute that links the description of language with the
     * element.
     */
    public static final String DATA_ATTRIBUTE_LANGUAGE_OF = "data-languageof";

    /**
     * The name of attribute that links the content of link that open a new
     * instance.
     */
    public static final String DATA_ATTRIBUTE_TARGET_OF =
            "data-attributetargetof";

    /**
     * The name of attribute that links the content of title of element.
     */
    public static final String DATA_ATTRIBUTE_TITLE_OF =
            "data-attributetitleof";

    /**
     * The name of attribute that links the content of autocomplete state of
     * field.
     */
    public static final String DATA_ARIA_AUTOCOMPLETE_OF =
            "data-ariaautocompleteof";

    /**
     * The name of attribute that links the content of busy state of element.
     */
    public static final String DATA_ARIA_BUSY_OF = "data-ariabusyof";

    /**
     * The name of attribute that links the content of checked state field.
     */
    public static final String DATA_ARIA_CHECKED_OF = "data-ariacheckedof";

    /**
     * The name of attribute that links the content of drop effect state of
     * element.
     */
    public static final String DATA_ARIA_DROPEFFECT_OF =
            "data-ariadropeffectof";

    /**
     * The name of attribute that links the content of expanded state of
     * element.
     */
    public static final String DATA_ARIA_EXPANDED_OF = "data-ariaexpandedof";

    /**
     * The name of attribute that links the content of grabbed state of element.
     */
    public static final String DATA_ARIA_GRABBED_OF = "data-ariagrabbedof";

    /**
     * The name of attribute that links the content that show if the field has
     * popup.
     */
    public static final String DATA_ARIA_HASPOPUP_OF = "data-ariahaspopupof";

    /**
     * The name of attribute that links the content of level state of element.
     */
    public static final String DATA_ARIA_LEVEL_OF = "data-arialevelof";

    /**
     * The name of attribute that links the content of orientation state of
     * element.
     */
    public static final String DATA_ARIA_ORIENTATION_OF =
            "data-ariaorientationof";

    /**
     * The name of attribute that links the content of pressed state of field.
     */
    public static final String DATA_ARIA_PRESSED_OF = "data-ariapressedof";

    /**
     * The name of attribute that links the content of minimum range state of
     * field.
     */
    public static final String DATA_ARIA_RANGE_MIN_OF =
            "data-attributevalueminof";

    /**
     * The name of attribute that links the content of maximum range state of
     * field.
     */
    public static final String DATA_ARIA_RANGE_MAX_OF =
            "data-attributevaluemaxof";

    /**
     * The name of attribute that links the content of required state of field.
     */
    public static final String DATA_ARIA_REQUIRED_OF =
            "data-attributerequiredof";

    /**
     * The name of attribute that links the content of selected state of field.
     */
    public static final String DATA_ARIA_SELECTED_OF = "data-ariaselectedof";

    /**
     * The name of attribute that links the content of sort state of element.
     */
    public static final String DATA_ARIA_SORT_OF = "data-ariasortof";

    /**
     * The name of attribute that links the content of role of element with the
     * element.
     */
    public static final String DATA_ROLE_OF = "data-roleof";

    /**
     * The browser shortcut prefix.
     */
    protected final String shortcutPrefix;

    /**
     * The description of shortcut list, before all elements.
     */
    protected final String attributeAccesskeyBefore;

    /**
     * The description of shortcut list, after all elements.
     */
    protected final String attributeAccesskeyAfter;

    /**
     * The prefix description of shortcut list, before all elements.
     */
    protected final String attributeAccesskeyPrefixBefore;

    /**
     * The suffix description of shortcut list, before all elements.
     */
    protected final String attributeAccesskeySuffixBefore;

    /**
     * The prefix description of shortcut list, after all elements.
     */
    protected final String attributeAccesskeyPrefixAfter;

    /**
     * The suffix description of shortcut list, after all elements.
     */
    protected final String attributeAccesskeySuffixAfter;

    /**
     * The text of link that download a file, before it.
     */
    protected final String attributeDownloadBefore;

    /**
     * The text of link that download a file, after it.
     */
    protected final String attributeDownloadAfter;

    /**
     * The prefix text of header cell, before it content.
     */
    protected final String attributeHeadersPrefixBefore;

    /**
     * The suffix text of header cell, before it content.
     */
    protected final String attributeHeadersSuffixBefore;

    /**
     * The prefix text of header cell, after it content.
     */
    protected final String attributeHeadersPrefixAfter;

    /**
     * The suffix text of header cell, after it content.
     */
    protected final String attributeHeadersSuffixAfter;

    /**
     * The prefix text of description of language element, before it.
     */
    protected final String attributeLanguagePrefixBefore;

    /**
     * The suffix text of description of language element, after it.
     */
    protected final String attributeLanguageSuffixBefore;

    /**
     * The prefix text of description of language element, before it.
     */
    protected final String attributeLanguagePrefixAfter;

    /**
     * The suffix text of description of language element, after it.
     */
    protected final String attributeLanguageSuffixAfter;

    /**
     * The prefix text of role of element, before it.
     */
    protected final String attributeRolePrefixBefore;

    /**
     * The suffix text of role of element, before it.
     */
    protected final String attributeRoleSuffixBefore;

    /**
     * The prefix text of role of element, after it.
     */
    protected final String attributeRolePrefixAfter;

    /**
     * The suffix text of role of element, after it.
     */
    protected final String attributeRoleSuffixAfter;

    /**
     * The text of link that open new instance, before it.
     */
    protected final String attributeTargetBlankBefore;

    /**
     * The text of link that open new instance, after it.
     */
    protected final String attributeTargetBlankAfter;

    /**
     * The prefix text of title of element, before it.
     */
    protected final String attributeTitlePrefixBefore;

    /**
     * The suffix text of title of element, before it.
     */
    protected final String attributeTitleSuffixBefore;

    /**
     * The prefix text of title of element, after it.
     */
    protected final String attributeTitlePrefixAfter;

    /**
     * The suffix text of title of element, after it.
     */
    protected final String attributeTitleSuffixAfter;

    /**
     * The content of autocomplete inline and list state of field, before it.
     */
    protected final String ariaAutoCompleteBothBefore;

    /**
     * The content of autocomplete inline and list state of field, after it.
     */
    protected final String ariaAutoCompleteBothAfter;

    /**
     * The content of autocomplete inline state of field, before it.
     */
    protected final String ariaAutoCompleteInlineBefore;

    /**
     * The content of autocomplete inline state of field, after it.
     */
    protected final String ariaAutoCompleteInlineAfter;

    /**
     * The content of autocomplete list state of field, before it.
     */
    protected final String ariaAutoCompleteListBefore;

    /**
     * The content of autocomplete list state of field, after it.
     */
    protected final String ariaAutoCompleteListAfter;

    /**
     * The content of busy state of element, before it.
     */
    protected final String ariaBusyTrueBefore;

    /**
     * The content of busy state of element, after it.
     */
    protected final String ariaBusyTrueAfter;

    /**
     * The content of unchecked state field, before it.
     */
    protected final String ariaCheckedFalseBefore;

    /**
     * The content of unchecked state field, after it.
     */
    protected final String ariaCheckedFalseAfter;

    /**
     * The content of mixed checked state field, before it.
     */
    protected final String ariaCheckedMixedBefore;

    /**
     * The content of mixed checked state field, after it.
     */
    protected final String ariaCheckedMixedAfter;

    /**
     * The content of checked state field, before it.
     */
    protected final String ariaCheckedTrueBefore;

    /**
     * The content of checked state field, after it.
     */
    protected final String ariaCheckedTrueAfter;

    /**
     * The content of drop with copy effect state of element, before it.
     */
    protected final String ariaDropeffectCopyBefore;

    /**
     * The content of drop with copy effect state of element, after it.
     */
    protected final String ariaDropeffectCopyAfter;

    /**
     * The content of drop with execute effect state of element, before it.
     */
    protected final String ariaDropeffectExecuteBefore;

    /**
     * The content of drop with execute effect state of element, after it.
     */
    protected final String ariaDropeffectExecuteAfter;

    /**
     * The content of drop with link effect state of element, before it.
     */
    protected final String ariaDropeffectLinkBefore;

    /**
     * The content of drop with link effect state of element, after it.
     */
    protected final String ariaDropeffectLinkAfter;

    /**
     * The content of drop with move effect state of element, before it.
     */
    protected final String ariaDropeffectMoveBefore;

    /**
     * The content of drop with move effect state of element, after it.
     */
    protected final String ariaDropeffectMoveAfter;

    /**
     * The content of drop with popup effect state of element, before it.
     */
    protected final String ariaDropeffectPopupBefore;

    /**
     * The content of drop with popup effect state of element, after it.
     */
    protected final String ariaDropeffectPopupAfter;

    /**
     * The content of collapsed state of element, before it.
     */
    protected final String ariaExpandedFalseBefore;

    /**
     * The content of collapsed state of element, after it.
     */
    protected final String ariaExpandedFalseAfter;

    /**
     * The content of expanded state of element, before it.
     */
    protected final String ariaExpandedTrueBefore;

    /**
     * The content of expanded state of element, after it.
     */
    protected final String ariaExpandedTrueAfter;

    /**
     * The content of ungrabbed state of element, before it.
     */
    protected final String ariaGrabbedFalseBefore;

    /**
     * The content of ungrabbed state of element, after it.
     */
    protected final String ariaGrabbedFalseAfter;

    /**
     * The content of grabbed state of element, before it.
     */
    protected final String ariaGrabbedTrueBefore;

    /**
     * The content of grabbed state of element, after it.
     */
    protected final String ariaGrabbedTrueAfter;

    /**
     * The content that show if the field has popup, before it.
     */
    protected final String ariaHaspopupTrueBefore;

    /**
     * The content that show if the field has popup, after it.
     */
    protected final String ariaHaspopupTrueAfter;

    /**
     * The prefix content of level state of element, before it.
     */
    protected final String ariaLevelPrefixBefore;

    /**
     * The suffix content of level state of element, before it.
     */
    protected final String ariaLevelSuffixBefore;

    /**
     * The prefix content of level state of element, after it.
     */
    protected final String ariaLevelPrefixAfter;

    /**
     * The suffix content of level state of element, after it.
     */
    protected final String ariaLevelSuffixAfter;

    /**
     * The prefix content of maximum range state of field, before it.
     */
    protected final String ariaValueMaximumPrefixBefore;

    /**
     * The suffix content of maximum range state of field, before it.
     */
    protected final String ariaValueMaximumSuffixBefore;

    /**
     * The prefix content of maximum range state of field, after it.
     */
    protected final String ariaValueMaximumPrefixAfter;

    /**
     * The suffix content of maximum range state of field, after it.
     */
    protected final String ariaValueMaximumSuffixAfter;

    /**
     * The prefix content of minimum range state of field, before it.
     */
    protected final String ariaValueMinimumPrefixBefore;

    /**
     * The suffix content of minimum range state of field, before it.
     */
    protected final String ariaValueMinimumSuffixBefore;

    /**
     * The prefix content of minimum range state of field, after it.
     */
    protected final String ariaValueMinimumPrefixAfter;

    /**
     * The suffix content of minimum range state of field, after it.
     */
    protected final String ariaValueMinimumSuffixAfter;

    /**
     * The content of horizontal orientation state of element, before it.
     */
    protected final String ariaOrientationHorizontalBefore;

    /**
     * The content of horizontal orientation state of element, after it.
     */
    protected final String ariaOrientationHorizontalAfter;

    /**
     * The content of vertical orientation state of element, before it.
     */
    protected final String ariaOrientationVerticalBefore;

    /**
     * The content of vertical orientation state of element, after it.
     */
    protected final String ariaOrientationVerticalAfter;

    /**
     * The content of unpressed state of field, before it.
     */
    protected final String ariaPressedFalseBefore;

    /**
     * The content of unpressed state of field, after it.
     */
    protected final String ariaPressedFalseAfter;

    /**
     * The content of mixed pressed state of field, before it.
     */
    protected final String ariaPressedMixedBefore;

    /**
     * The content of mixed pressed state of field, after it.
     */
    protected final String ariaPressedMixedAfter;

    /**
     * The content of pressed state of field, before it.
     */
    protected final String ariaPressedTrueBefore;

    /**
     * The content of pressed state of field, after it.
     */
    protected final String ariaPressedTrueAfter;

    /**
     * The content of required state of field, before it.
     */
    protected final String ariaRequiredTrueBefore;

    /**
     * The content of required state of field, after it.
     */
    protected final String ariaRequiredTrueAfter;

    /**
     * The content of unselected state of field, before it.
     */
    protected final String ariaSelectedFalseBefore;

    /**
     * The content of unselected state of field, after it.
     */
    protected final String ariaSelectedFalseAfter;

    /**
     * The content of selected state of field, before it.
     */
    protected final String ariaSelectedTrueBefore;

    /**
     * The content of selected state of field, after it.
     */
    protected final String ariaSelectedTrueAfter;

    /**
     * The content of ascending sort state of element, before it.
     */
    protected final String ariaSortAscendingBefore;

    /**
     * The content of ascending sort state of element, after it.
     */
    protected final String ariaSortAscendingAfter;

    /**
     * The content of descending sort state of element, before it.
     */
    protected final String ariaSortDescendingBefore;

    /**
     * The content of descending sort state of element, after it.
     */
    protected final String ariaSortDescendingAfter;

    /**
     * The content of sorted state of element, before it.
     */
    protected final String ariaSortOtherBefore;

    /**
     * The content of sorted state of element, after it.
     */
    protected final String ariaSortOtherAfter;

    /**
     * The HTML parser.
     */
    protected final HTMLDOMParser parser;

    /**
     * The id generator.
     */
    protected final IDGenerator idGenerator;

    /**
     * The configuration of HaTeMiLe.
     */
    protected final Configure configure;

    /**
     * The list element of shortcuts, before the whole content of page.
     */
    protected HTMLDOMElement listShortcutsBefore;

    /**
     * The list element of shortcuts, after the whole content of page.
     */
    protected HTMLDOMElement listShortcutsAfter;

    /**
     * The state that indicates if the list of shortcuts of page was added.
     */
    protected boolean listShortcutsAdded;

    /**
     * Initializes a new object that manipulate the display for screen readers
     * of parser.
     * @param htmlParser The HTML parser.
     * @param hatemileConfiguration The configuration of HaTeMiLe.
     */
    public AccessibleDisplayScreenReaderImplementation(
            final HTMLDOMParser htmlParser,
            final Configure hatemileConfiguration) {
        this(htmlParser, hatemileConfiguration, null);
    }

    /**
     * Initializes a new object that manipulate the display for screen readers
     * of parser.
     * @param htmlParser The HTML parser.
     * @param hatemileConfiguration The configuration of HaTeMiLe.
     * @param userAgent The user agent of browser.
     */
    public AccessibleDisplayScreenReaderImplementation(
            final HTMLDOMParser htmlParser,
            final Configure hatemileConfiguration,
            final String userAgent) {
        parser = Objects.requireNonNull(htmlParser);
        idGenerator = new IDGenerator("display");
        configure = hatemileConfiguration;
        shortcutPrefix = getShortcutPrefix(userAgent,
                configure.getParameter("attribute-accesskey-default"));

        attributeAccesskeyBefore = configure
                .getParameter("attribute-accesskey-before");
        attributeAccesskeyAfter = configure
                .getParameter("attribute-accesskey-after");
        attributeAccesskeyPrefixBefore = configure
                .getParameter("attribute-accesskey-prefix-before");
        attributeAccesskeySuffixBefore = configure
                .getParameter("attribute-accesskey-suffix-before");
        attributeAccesskeyPrefixAfter = configure
                .getParameter("attribute-accesskey-prefix-after");
        attributeAccesskeySuffixAfter = configure
                .getParameter("attribute-accesskey-suffix-after");
        attributeDownloadBefore = configure
                .getParameter("attribute-download-before");
        attributeDownloadAfter = configure
                .getParameter("attribute-download-after");
        attributeHeadersPrefixBefore = configure
                .getParameter("attribute-headers-prefix-before");
        attributeHeadersSuffixBefore = configure
                .getParameter("attribute-headers-suffix-before");
        attributeHeadersPrefixAfter = configure
                .getParameter("attribute-headers-prefix-after");
        attributeHeadersSuffixAfter = configure
                .getParameter("attribute-headers-suffix-after");
        attributeLanguagePrefixBefore = configure
                .getParameter("attribute-language-prefix-before");
        attributeLanguageSuffixBefore = configure
                .getParameter("attribute-language-suffix-before");
        attributeLanguagePrefixAfter = configure
                .getParameter("attribute-language-prefix-after");
        attributeLanguageSuffixAfter = configure
                .getParameter("attribute-language-suffix-after");
        attributeRolePrefixBefore = configure
                .getParameter("attribute-role-prefix-before");
        attributeRoleSuffixBefore = configure
                .getParameter("attribute-role-suffix-before");
        attributeRolePrefixAfter = configure
                .getParameter("attribute-role-prefix-after");
        attributeRoleSuffixAfter = configure
                .getParameter("attribute-role-suffix-after");
        attributeTargetBlankBefore = configure
                .getParameter("attribute-target-blank-before");
        attributeTargetBlankAfter = configure
                .getParameter("attribute-target-blank-after");
        attributeTitlePrefixBefore = configure
                .getParameter("attribute-title-prefix-before");
        attributeTitleSuffixBefore = configure
                .getParameter("attribute-title-suffix-before");
        attributeTitlePrefixAfter = configure
                .getParameter("attribute-title-prefix-after");
        attributeTitleSuffixAfter = configure
                .getParameter("attribute-title-suffix-after");

        ariaAutoCompleteBothBefore = configure
                .getParameter("aria-autocomplete-both-before");
        ariaAutoCompleteBothAfter = configure
                .getParameter("aria-autocomplete-both-after");
        ariaAutoCompleteInlineBefore = configure
                .getParameter("aria-autocomplete-inline-before");
        ariaAutoCompleteInlineAfter = configure
                .getParameter("aria-autocomplete-inline-after");
        ariaAutoCompleteListBefore = configure
                .getParameter("aria-autocomplete-list-before");
        ariaAutoCompleteListAfter = configure
                .getParameter("aria-autocomplete-list-after");
        ariaBusyTrueBefore = configure
                .getParameter("aria-busy-true-before");
        ariaBusyTrueAfter = configure
                .getParameter("aria-busy-true-after");
        ariaCheckedFalseBefore = configure
                .getParameter("aria-checked-false-before");
        ariaCheckedFalseAfter = configure
                .getParameter("aria-checked-false-after");
        ariaCheckedMixedBefore = configure
                .getParameter("aria-checked-mixed-before");
        ariaCheckedMixedAfter = configure
                .getParameter("aria-checked-mixed-after");
        ariaCheckedTrueBefore = configure
                .getParameter("aria-checked-true-before");
        ariaCheckedTrueAfter = configure
                .getParameter("aria-checked-true-after");
        ariaDropeffectCopyBefore = configure
                .getParameter("aria-dropeffect-copy-before");
        ariaDropeffectCopyAfter = configure
                .getParameter("aria-dropeffect-copy-after");
        ariaDropeffectExecuteBefore = configure
                .getParameter("aria-dropeffect-execute-before");
        ariaDropeffectExecuteAfter = configure
                .getParameter("aria-dropeffect-execute-after");
        ariaDropeffectLinkBefore = configure
                .getParameter("aria-dropeffect-link-before");
        ariaDropeffectLinkAfter = configure
                .getParameter("aria-dropeffect-link-after");
        ariaDropeffectMoveBefore = configure
                .getParameter("aria-dropeffect-move-before");
        ariaDropeffectMoveAfter = configure
                .getParameter("aria-dropeffect-move-after");
        ariaDropeffectPopupBefore = configure
                .getParameter("aria-dropeffect-popup-before");
        ariaDropeffectPopupAfter = configure
                .getParameter("aria-dropeffect-popup-after");
        ariaExpandedFalseBefore = configure
                .getParameter("aria-expanded-false-before");
        ariaExpandedFalseAfter = configure
                .getParameter("aria-expanded-false-after");
        ariaExpandedTrueBefore = configure
                .getParameter("aria-expanded-true-before");
        ariaExpandedTrueAfter = configure
                .getParameter("aria-expanded-true-after");
        ariaGrabbedFalseBefore = configure
                .getParameter("aria-grabbed-false-before");
        ariaGrabbedFalseAfter = configure
                .getParameter("aria-grabbed-false-after");
        ariaGrabbedTrueBefore = configure
                .getParameter("aria-grabbed-true-before");
        ariaGrabbedTrueAfter = configure
                .getParameter("aria-grabbed-true-after");
        ariaHaspopupTrueBefore = configure
                .getParameter("aria-haspopup-true-before");
        ariaHaspopupTrueAfter = configure
                .getParameter("aria-haspopup-true-after");
        ariaLevelPrefixBefore = configure
                .getParameter("aria-level-prefix-before");
        ariaLevelSuffixBefore = configure
                .getParameter("aria-level-suffix-before");
        ariaLevelPrefixAfter = configure
                .getParameter("aria-level-prefix-after");
        ariaLevelSuffixAfter = configure
                .getParameter("aria-level-suffix-after");
        ariaValueMaximumPrefixBefore = configure
                .getParameter("aria-value-maximum-prefix-before");
        ariaValueMaximumSuffixBefore = configure
                .getParameter("aria-value-maximum-suffix-before");
        ariaValueMaximumPrefixAfter = configure
                .getParameter("aria-value-maximum-prefix-after");
        ariaValueMaximumSuffixAfter = configure
                .getParameter("aria-value-maximum-suffix-after");
        ariaValueMinimumPrefixBefore = configure
                .getParameter("aria-value-minimum-prefix-before");
        ariaValueMinimumSuffixBefore = configure
                .getParameter("aria-value-minimum-suffix-before");
        ariaValueMinimumPrefixAfter = configure
                .getParameter("aria-value-minimum-prefix-after");
        ariaValueMinimumSuffixAfter = configure
                .getParameter("aria-value-minimum-suffix-after");
        ariaOrientationHorizontalBefore = configure
                .getParameter("aria-orientation-horizontal-before");
        ariaOrientationHorizontalAfter = configure
                .getParameter("aria-orientation-horizontal-after");
        ariaOrientationVerticalBefore = configure
                .getParameter("aria-orientation-vertical-before");
        ariaOrientationVerticalAfter = configure
                .getParameter("aria-orientation-vertical-after");
        ariaPressedFalseBefore = configure
                .getParameter("aria-pressed-false-before");
        ariaPressedFalseAfter = configure
                .getParameter("aria-pressed-false-after");
        ariaPressedMixedBefore = configure
                .getParameter("aria-pressed-mixed-before");
        ariaPressedMixedAfter = configure
                .getParameter("aria-pressed-mixed-after");
        ariaPressedTrueBefore = configure
                .getParameter("aria-pressed-true-before");
        ariaPressedTrueAfter = configure
                .getParameter("aria-pressed-true-after");
        ariaRequiredTrueBefore = configure
                .getParameter("aria-required-true-before");
        ariaRequiredTrueAfter = configure
                .getParameter("aria-required-true-after");
        ariaSelectedFalseBefore = configure
                .getParameter("aria-selected-false-before");
        ariaSelectedFalseAfter = configure
                .getParameter("aria-selected-false-after");
        ariaSelectedTrueBefore = configure
                .getParameter("aria-selected-true-before");
        ariaSelectedTrueAfter = configure
                .getParameter("aria-selected-true-after");
        ariaSortAscendingBefore = configure
                .getParameter("aria-sort-ascending-before");
        ariaSortAscendingAfter = configure
                .getParameter("aria-sort-ascending-after");
        ariaSortDescendingBefore = configure
                .getParameter("aria-sort-descending-before");
        ariaSortDescendingAfter = configure
                .getParameter("aria-sort-descending-after");
        ariaSortOtherBefore = configure.getParameter("aria-sort-other-before");
        ariaSortOtherAfter = configure.getParameter("aria-sort-other-after");

        listShortcutsAdded = false;
        listShortcutsBefore = null;
        listShortcutsAfter = null;
    }

    /**
     * Returns the shortcut prefix of browser.
     * @param userAgent The user agent of browser.
     * @param standartPrefix The default prefix.
     * @return The shortcut prefix of browser.
     */
    protected final String getShortcutPrefix(final String userAgent,
            final String standartPrefix) {
        if (userAgent != null) {
            String lowerUserAgent = userAgent.toLowerCase();
            boolean opera = lowerUserAgent.contains("opera");
            boolean mac = lowerUserAgent.contains("mac");
            boolean konqueror = lowerUserAgent.contains("konqueror");
            boolean spoofer = lowerUserAgent.contains("spoofer");
            boolean safari = lowerUserAgent.contains("applewebkit");
            boolean windows = lowerUserAgent.contains("windows");
            boolean chrome = lowerUserAgent.contains("chrome");
            boolean firefox = lowerUserAgent.contains("firefox")
                    || lowerUserAgent.contains("minefield");
            boolean ie = lowerUserAgent.contains("msie")
                    || lowerUserAgent.contains("trident");

            if (opera) {
                return "SHIFT + ESC";
            } else if (chrome && mac && !spoofer) {
                return "CTRL + OPTION";
            } else if (safari && !windows && !spoofer) {
                return "CTRL + ALT";
            } else if (!windows && (safari || mac || konqueror)) {
                return "CTRL";
            } else if (firefox) {
                return "ALT + SHIFT";
            } else if (chrome || ie) {
                return "ALT";
            } else {
                return standartPrefix;
            }
        } else {
            return standartPrefix;
        }
    }

    /**
     * Returns the description of role.
     * @param role The role.
     * @return The description of role.
     */
    protected String getRoleDescription(final String role) {
        String parameter = "role-" + role.toLowerCase();
        if (configure.hasParameter(parameter)) {
            return configure.getParameter(parameter);
        } else {
            return null;
        }
    }

    /**
     * Returns the description of language.
     * @param languageCode The BCP 47 code language.
     * @return The description of language.
     */
    protected String getLanguageDescription(final String languageCode) {
        String language = languageCode.toLowerCase();
        String parameter = "language-" + language;
        if (configure.hasParameter(parameter)) {
            return configure.getParameter(parameter);
        } else if (language.contains("-")) {
            parameter = "language-" + language.split("-")[0];
            if (configure.hasParameter(parameter)) {
                return configure.getParameter(parameter);
            }
        }
        return null;
    }

    /**
     * Returns the description of element.
     * @param element The element.
     * @return The description of element.
     */
    protected String getDescription(final HTMLDOMElement element) {
        String description = null;
        if (element.hasAttribute("title")) {
            description = element.getAttribute("title");
        } else if (element.hasAttribute("aria-label")) {
            description = element.getAttribute("aria-label");
        } else if (element.hasAttribute("alt")) {
            description = element.getAttribute("alt");
        } else if (element.hasAttribute("label")) {
            description = element.getAttribute("label");
        } else if ((element.hasAttribute("aria-labelledby"))
                || (element.hasAttribute("aria-describedby"))) {
            String[] descriptionIds;
            if (element.hasAttribute("aria-labelledby")) {
                descriptionIds = element.getAttribute("aria-labelledby")
                        .split("[ \n\t\r]+");
            } else {
                descriptionIds = element.getAttribute("aria-describedby")
                        .split("[ \n\t\r]+");
            }
            for (int i = 0, length = descriptionIds.length; i < length; i++) {
                HTMLDOMElement elementDescription = parser
                        .find("#" + descriptionIds[i]).firstResult();
                if (elementDescription != null) {
                    description = elementDescription.getTextContent();
                    break;
                }
            }
        } else if ((element.getTagName().equals("INPUT"))
                && (element.hasAttribute("type"))) {
            String type = element.getAttribute("type").toLowerCase();
            if (((type.equals("button")) || (type.equals("submit"))
                    || (type.equals("reset")))
                    && (element.hasAttribute("value"))) {
                description = element.getAttribute("value");
            }
        }
        if (description == null) {
            description = element.getTextContent();
        }
        return description.replaceAll("[ \n\t\r]+", " ").trim();
    }

    /**
     * Generate the list of shortcuts of page.
     */
    protected void generateListShortcuts() {
        HTMLDOMElement local = parser.find("body").firstResult();
        if (local != null) {
            HTMLDOMElement containerBefore = parser
                    .find("#" + ID_CONTAINER_SHORTCUTS_BEFORE).firstResult();
            if ((containerBefore == null)
                    && (!attributeAccesskeyBefore.isEmpty())) {
                containerBefore = parser.createElement("div");
                containerBefore.setAttribute("id",
                        ID_CONTAINER_SHORTCUTS_BEFORE);

                HTMLDOMElement textContainer = parser.createElement("span");
                textContainer.setAttribute("class", CLASS_TEXT_SHORTCUTS);
                textContainer.appendText(attributeAccesskeyBefore);

                containerBefore.appendElement(textContainer);
                local.prependElement(containerBefore);
            }

            if (containerBefore != null) {
                listShortcutsBefore = parser.find(containerBefore)
                        .findChildren("ul").firstResult();
                if (listShortcutsBefore == null) {
                    listShortcutsBefore = parser.createElement("ul");
                    containerBefore.appendElement(listShortcutsBefore);
                }
            }


            HTMLDOMElement containerAfter = parser
                    .find("#" + ID_CONTAINER_SHORTCUTS_AFTER).firstResult();
            if ((containerAfter == null)
                    && (!attributeAccesskeyAfter.isEmpty())) {
                containerAfter = parser.createElement("div");
                containerAfter.setAttribute("id", ID_CONTAINER_SHORTCUTS_AFTER);

                HTMLDOMElement textContainer = parser.createElement("span");
                textContainer.setAttribute("class", CLASS_TEXT_SHORTCUTS);
                textContainer.appendText(attributeAccesskeyAfter);

                containerAfter.appendElement(textContainer);
                local.appendElement(containerAfter);
            }

            if (containerAfter != null) {
                listShortcutsAfter = parser.find(containerAfter)
                        .findChildren("ul").firstResult();
                if (listShortcutsAfter == null) {
                    listShortcutsAfter = parser.createElement("ul");
                    containerAfter.appendElement(listShortcutsAfter);
                }
            }
        }
        listShortcutsAdded = true;
    }

    /**
     * Insert a element before or after other element.
     * @param element The reference element.
     * @param insertedElement The element that be inserted.
     * @param before To insert the element before the other element.
     */
    protected void insert(final HTMLDOMElement element,
            final HTMLDOMElement insertedElement, final boolean before) {
        String tagName = element.getTagName();
        Collection<String> appendTags = Arrays.asList("BODY", "A", "FIGCAPTION",
                "LI", "DT", "DD", "LABEL", "OPTION", "TD", "TH");
        Collection<String> controls = Arrays.asList("INPUT", "SELECT",
                "TEXTAREA");
        if (tagName.equals("HTML")) {
            HTMLDOMElement body = parser.find("body").firstResult();
            if (body != null) {
                insert(body, insertedElement, before);
            }
        } else if (appendTags.contains(tagName)) {
            if (before) {
                element.prependElement(insertedElement);
            } else {
                element.appendElement(insertedElement);
            }
        } else if (controls.contains(tagName)) {
            Collection<HTMLDOMElement> labels = new ArrayList<HTMLDOMElement>();
            if (element.hasAttribute("id")) {
                labels = parser.find("label[for=\"" + element.getAttribute("id")
                        + "\"]").listResults();
            }
            if (labels.isEmpty()) {
                labels = parser.find(element).findAncestors("label")
                        .listResults();
            }
            for (HTMLDOMElement label : labels) {
                insert(label, insertedElement, before);
            }
        } else if (before) {
            element.insertBefore(insertedElement);
        } else {
            element.insertAfter(insertedElement);
        }
    }

    /**
     * Force the screen reader display an information of element.
     * @param element The reference element.
     * @param textBefore The text content to show before the element.
     * @param textAfter The text content to show after the element.
     * @param dataOf The name of attribute that links the content with element.
     */
    protected void forceReadSimple(final HTMLDOMElement element,
            final String textBefore, final String textAfter,
            final String dataOf) {
        idGenerator.generateId(element);
        String identifier = element.getAttribute("id");
        String selector = "[" + dataOf + "=\"" + identifier + "\"]";

        HTMLDOMElement referenceBefore = parser.find("."
                + CLASS_FORCE_READ_BEFORE + selector).firstResult();
        HTMLDOMElement referenceAfter = parser.find("."
                + CLASS_FORCE_READ_AFTER + selector).firstResult();
        List<HTMLDOMElement> references = new ArrayList<HTMLDOMElement>(parser
                .find(selector).listResults());
        references.remove(referenceBefore);
        references.remove(referenceAfter);

        if (references.isEmpty()) {
            if (!textBefore.isEmpty()) {
                if (referenceBefore != null) {
                    referenceBefore.removeNode();
                }

                HTMLDOMElement span = parser.createElement("span");
                span.setAttribute("class", CLASS_FORCE_READ_BEFORE);
                span.setAttribute(dataOf, identifier);
                span.appendText(textBefore);
                insert(element, span, true);
            }
            if (!textAfter.isEmpty()) {
                if (referenceAfter != null) {
                    referenceAfter.removeNode();
                }

                HTMLDOMElement span = parser.createElement("span");
                span.setAttribute("class", CLASS_FORCE_READ_AFTER);
                span.setAttribute(dataOf, identifier);
                span.appendText(textAfter);
                insert(element, span, false);
            }
        }
    }

    /**
     * Force the screen reader display an information of element with prefixes
     * or suffixes.
     * @param element The reference element.
     * @param value The value to be show.
     * @param textPrefixBefore The prefix of value to show before the element.
     * @param textSuffixBefore The suffix of value to show before the element.
     * @param textPrefixAfter The prefix of value to show after the element.
     * @param textSuffixAfter The suffix of value to show after the element.
     * @param dataOf The name of attribute that links the content with element.
     */
    protected void forceRead(final HTMLDOMElement element, final String value,
            final String textPrefixBefore, final String textSuffixBefore,
            final String textPrefixAfter, final String textSuffixAfter,
            final String dataOf) {
        String textBefore = "";
        String textAfter = "";
        if ((!textPrefixBefore.isEmpty()) || (!textSuffixBefore.isEmpty())) {
            textBefore = textPrefixBefore + value + textSuffixBefore;
        }
        if ((!textPrefixAfter.isEmpty()) || (!textSuffixAfter.isEmpty())) {
            textAfter = textPrefixAfter + value + textSuffixAfter;
        }
        forceReadSimple(element, textBefore, textAfter, dataOf);
    }

    /**
     * {@inheritDoc}
     */
    public void displayShortcut(final HTMLDOMElement element) {
        if (element.hasAttribute("accesskey")) {
            String description = getDescription(element);
            if (!element.hasAttribute("title")) {
                idGenerator.generateId(element);
                element.setAttribute(DATA_ATTRIBUTE_TITLE_OF,
                        element.getAttribute("id"));
                element.setAttribute("title", description);
            }

            if (!listShortcutsAdded) {
                generateListShortcuts();
            }

            String[] keys = element.getAttribute("accesskey").toUpperCase()
                    .split("[ \n\t\r]+");
            for (String key : keys) {
                String selector = "[" + DATA_ATTRIBUTE_ACCESSKEY_OF + "=\""
                        + key + "\"]";

                String shortcut = shortcutPrefix + " + " + key;
                forceRead(element, shortcut, attributeAccesskeyPrefixBefore,
                        attributeAccesskeySuffixBefore,
                        attributeAccesskeyPrefixAfter,
                        attributeAccesskeySuffixAfter,
                        DATA_ATTRIBUTE_ACCESSKEY_OF);

                HTMLDOMElement item = parser.createElement("li");
                item.setAttribute(DATA_ATTRIBUTE_ACCESSKEY_OF, key);
                item.appendText(shortcut + ": " + description);
                if ((listShortcutsBefore != null) && (parser
                        .find(listShortcutsBefore).findChildren(selector)
                        .firstResult() == null)) {
                    listShortcutsBefore.appendElement(item.cloneElement());
                }
                if ((listShortcutsAfter != null) && (parser
                        .find(listShortcutsAfter).findChildren(selector)
                        .firstResult() == null)) {
                    listShortcutsAfter.appendElement(item.cloneElement());
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void displayAllShortcuts() {
        Collection<HTMLDOMElement> elements = parser.find("[accesskey]")
                .listResults();
        for (HTMLDOMElement element : elements) {
            if (CommonFunctions.isValidElement(element)) {
                displayShortcut(element);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void displayRole(final HTMLDOMElement element) {
        if (element.hasAttribute("role")) {
            String roleDescription =
                    getRoleDescription(element.getAttribute("role"));
            if (roleDescription != null) {
                forceRead(element, roleDescription, attributeRolePrefixBefore,
                        attributeRoleSuffixBefore, attributeRolePrefixAfter,
                        attributeRoleSuffixAfter, DATA_ROLE_OF);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void displayAllRoles() {
        Collection<HTMLDOMElement> elements = parser.find("[role]")
                .listResults();
        for (HTMLDOMElement element : elements) {
            if (CommonFunctions.isValidElement(element)) {
                displayRole(element);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void displayCellHeader(final HTMLDOMElement tableCell) {
        if (tableCell.hasAttribute("headers")) {
            String textHeader = "";
            List<String> idsHeaders = Arrays.asList(tableCell
                    .getAttribute("headers").split("[ \n\t\r]+"));
            for (String idHeader : idsHeaders) {
                HTMLDOMElement header = parser.find("#" + idHeader)
                        .firstResult();
                if (header != null) {
                    if (textHeader.equals("")) {
                        textHeader = header.getTextContent().trim();
                    } else {
                        textHeader = textHeader + " "
                                + header.getTextContent().trim();
                    }
                }
            }
            if (!textHeader.trim().isEmpty()) {
                forceRead(tableCell, textHeader, attributeHeadersPrefixBefore,
                        attributeHeadersSuffixBefore,
                        attributeHeadersPrefixAfter,
                        attributeHeadersSuffixAfter, DATA_ATTRIBUTE_HEADERS_OF);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void displayAllCellHeaders() {
        Collection<HTMLDOMElement> elements = parser
                .find("td[headers],th[headers]").listResults();
        for (HTMLDOMElement element : elements) {
            if (CommonFunctions.isValidElement(element)) {
                displayCellHeader(element);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void displayWAIARIAStates(final HTMLDOMElement element) {
        if ((element.hasAttribute("aria-busy"))
                && (element.getAttribute("aria-busy").equals("true"))) {
            forceReadSimple(element, ariaBusyTrueBefore, ariaBusyTrueAfter,
                    DATA_ARIA_BUSY_OF);
        }
        if (element.hasAttribute("aria-checked")) {
            String attributeValue = element.getAttribute("aria-checked");
            if (attributeValue.equals("true")) {
                forceReadSimple(element, ariaCheckedTrueBefore,
                        ariaCheckedTrueAfter, DATA_ARIA_CHECKED_OF);
            } else if (attributeValue.equals("false")) {
                forceReadSimple(element, ariaCheckedFalseBefore,
                        ariaCheckedFalseAfter, DATA_ARIA_CHECKED_OF);
            } else if (attributeValue.equals("mixed")) {
                forceReadSimple(element, ariaCheckedMixedBefore,
                        ariaCheckedMixedAfter, DATA_ARIA_CHECKED_OF);
            }
        }
        if (element.hasAttribute("aria-expanded")) {
            String attributeValue = element.getAttribute("aria-expanded");
            if (attributeValue.equals("true")) {
                forceReadSimple(element, ariaExpandedTrueBefore,
                        ariaExpandedTrueAfter, DATA_ARIA_EXPANDED_OF);
            } else if (attributeValue.equals("false")) {
                forceReadSimple(element, ariaExpandedFalseBefore,
                        ariaExpandedFalseAfter, DATA_ARIA_EXPANDED_OF);
            }
        }
        if ((element.hasAttribute("aria-haspopup"))
                && (element.getAttribute("aria-haspopup").equals("true"))) {
            forceReadSimple(element, ariaHaspopupTrueBefore,
                    ariaHaspopupTrueAfter, DATA_ARIA_HASPOPUP_OF);
        }
        if (element.hasAttribute("aria-level")) {
            forceRead(element, element.getAttribute("aria-level"),
                    ariaLevelPrefixBefore, ariaLevelSuffixBefore,
                    ariaLevelPrefixAfter, ariaLevelSuffixAfter,
                    DATA_ARIA_LEVEL_OF);
        }
        if (element.hasAttribute("aria-orientation")) {
            String attributeValue = element.getAttribute("aria-orientation");
            if (attributeValue.equals("vertical")) {
                forceReadSimple(element, ariaOrientationVerticalBefore,
                        ariaOrientationVerticalAfter, DATA_ARIA_ORIENTATION_OF);
            } else if (attributeValue.equals("horizontal")) {
                forceReadSimple(element, ariaOrientationHorizontalBefore,
                        ariaOrientationHorizontalAfter,
                        DATA_ARIA_ORIENTATION_OF);
            }
        }
        if (element.hasAttribute("aria-pressed")) {
            String attributeValue = element.getAttribute("aria-pressed");
            if (attributeValue.equals("true")) {
                forceReadSimple(element, ariaPressedTrueBefore,
                        ariaPressedTrueAfter, DATA_ARIA_PRESSED_OF);
            } else if (attributeValue.equals("false")) {
                forceReadSimple(element, ariaPressedFalseBefore,
                        ariaPressedFalseAfter, DATA_ARIA_PRESSED_OF);
            } else if (attributeValue.equals("mixed")) {
                forceReadSimple(element, ariaPressedMixedBefore,
                        ariaPressedMixedAfter, DATA_ARIA_PRESSED_OF);
            }
        }
        if (element.hasAttribute("aria-selected")) {
            String attributeValue = element.getAttribute("aria-selected");
            if (attributeValue.equals("true")) {
                forceReadSimple(element, ariaSelectedTrueBefore,
                        ariaSelectedTrueAfter, DATA_ARIA_SELECTED_OF);
            } else if (attributeValue.equals("false")) {
                forceReadSimple(element, ariaSelectedFalseBefore,
                        ariaSelectedFalseAfter, DATA_ARIA_SELECTED_OF);
            }
        }
        if (element.hasAttribute("aria-sort")) {
            String attributeValue = element.getAttribute("aria-sort");
            if (attributeValue.equals("ascending")) {
                forceReadSimple(element, ariaSortAscendingBefore,
                        ariaSortAscendingAfter, DATA_ARIA_SORT_OF);
            } else if (attributeValue.equals("descending")) {
                forceReadSimple(element, ariaSortDescendingBefore,
                        ariaSortDescendingAfter, DATA_ARIA_SORT_OF);
            } else if (attributeValue.equals("other")) {
                forceReadSimple(element, ariaSortOtherBefore,
                        ariaSortOtherAfter, DATA_ARIA_SORT_OF);
            }
        }
        if ((element.hasAttribute("aria-required"))
                && (element.getAttribute("aria-required").equals("true"))) {
            forceReadSimple(element, ariaRequiredTrueBefore,
                    ariaRequiredTrueAfter, DATA_ARIA_REQUIRED_OF);
        }
        if (element.hasAttribute("aria-valuemin")) {
            forceRead(element, element.getAttribute("aria-valuemin"),
                    ariaValueMinimumPrefixBefore, ariaValueMinimumSuffixBefore,
                    ariaValueMinimumPrefixAfter, ariaValueMinimumSuffixAfter,
                    DATA_ARIA_RANGE_MIN_OF);
        }
        if (element.hasAttribute("aria-valuemax")) {
            forceRead(element, element.getAttribute("aria-valuemax"),
                    ariaValueMaximumPrefixBefore, ariaValueMaximumSuffixBefore,
                    ariaValueMaximumPrefixAfter, ariaValueMaximumSuffixAfter,
                    DATA_ARIA_RANGE_MAX_OF);
        }
        if (element.hasAttribute("aria-autocomplete")) {
            String attributeValue = element.getAttribute("aria-autocomplete");
            if (attributeValue.equals("both")) {
                forceReadSimple(element, ariaAutoCompleteBothBefore,
                        ariaAutoCompleteBothAfter, DATA_ARIA_AUTOCOMPLETE_OF);
            } else if (attributeValue.equals("inline")) {
                forceReadSimple(element, ariaAutoCompleteListBefore,
                        ariaAutoCompleteListAfter, DATA_ARIA_AUTOCOMPLETE_OF);
            } else if (attributeValue.equals("list")) {
                forceReadSimple(element, ariaAutoCompleteInlineBefore,
                        ariaAutoCompleteInlineAfter, DATA_ARIA_AUTOCOMPLETE_OF);
            }
        }
        if (element.hasAttribute("aria-dropeffect")) {
            String attributeValue = element.getAttribute("aria-dropeffect");
            if (attributeValue.equals("copy")) {
                forceReadSimple(element, ariaDropeffectCopyBefore,
                        ariaDropeffectCopyAfter, DATA_ARIA_DROPEFFECT_OF);
            } else if (attributeValue.equals("move")) {
                forceReadSimple(element, ariaDropeffectMoveBefore,
                        ariaDropeffectMoveAfter, DATA_ARIA_DROPEFFECT_OF);
            } else if (attributeValue.equals("link")) {
                forceReadSimple(element, ariaDropeffectLinkBefore,
                        ariaDropeffectLinkAfter, DATA_ARIA_DROPEFFECT_OF);
            } else if (attributeValue.equals("execute")) {
                forceReadSimple(element, ariaDropeffectExecuteBefore,
                        ariaDropeffectExecuteAfter, DATA_ARIA_DROPEFFECT_OF);
            } else if (attributeValue.equals("popup")) {
                forceReadSimple(element, ariaDropeffectPopupBefore,
                        ariaDropeffectPopupAfter, DATA_ARIA_DROPEFFECT_OF);
            }
        }
        if (element.hasAttribute("aria-grabbed")) {
            String attributeValue = element.getAttribute("aria-grabbed");
            if (attributeValue.equals("true")) {
                forceReadSimple(element, ariaGrabbedTrueBefore,
                        ariaGrabbedTrueAfter, DATA_ARIA_GRABBED_OF);
            } else if (attributeValue.equals("false")) {
                forceReadSimple(element, ariaGrabbedFalseBefore,
                        ariaGrabbedFalseAfter, DATA_ARIA_GRABBED_OF);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void displayAllWAIARIAStates() {
        Collection<HTMLDOMElement> elements = parser
                .find("[aria-busy=true],[aria-checked],[aria-dropeffect],"
                    + "[aria-expanded],[aria-grabbed],[aria-haspopup],"
                    + "[aria-level],[aria-orientation],[aria-pressed],"
                    + "[aria-selected],[aria-sort],[aria-required=true],"
                    + "[aria-valuemin],[aria-valuemax],[aria-autocomplete]")
                .listResults();
        for (HTMLDOMElement element : elements) {
            if (CommonFunctions.isValidElement(element)) {
                displayWAIARIAStates(element);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void displayLinkAttributes(final HTMLDOMElement link) {
        if (link.hasAttribute("download")) {
            forceReadSimple(link, attributeDownloadBefore,
                    attributeDownloadAfter, DATA_ATTRIBUTE_DOWNLOAD_OF);
        }
        if ((link.hasAttribute("target"))
                && (link.getAttribute("target").equals("_blank"))) {
            forceReadSimple(link, attributeTargetBlankBefore,
                    attributeTargetBlankAfter, DATA_ATTRIBUTE_TARGET_OF);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void displayAllLinksAttributes() {
        Collection<HTMLDOMElement> elements = parser
                .find("a[download],a[target=\"_blank\"]").listResults();
        for (HTMLDOMElement element : elements) {
            if (CommonFunctions.isValidElement(element)) {
                displayLinkAttributes(element);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void displayTitle(final HTMLDOMElement element) {
        if (element.getTagName().equals("IMG")) {
            displayAlternativeTextImage(element);
        } else if ((element.hasAttribute("title"))
                && (!element.getAttribute("title").isEmpty())) {
            forceRead(element, element.getAttribute("title"),
                    attributeTitlePrefixBefore, attributeTitleSuffixBefore,
                    attributeTitlePrefixAfter, attributeTitleSuffixAfter,
                    DATA_ATTRIBUTE_TITLE_OF);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void displayAllTitles() {
        Collection<HTMLDOMElement> elements = parser.find("body [title]")
                .listResults();
        for (HTMLDOMElement element : elements) {
            if (CommonFunctions.isValidElement(element)) {
                displayTitle(element);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void displayLanguage(final HTMLDOMElement element) {
        String languageCode = null;
        if (element.hasAttribute("lang")) {
            languageCode = element.getAttribute("lang");
        } else if (element.hasAttribute("hreflang")) {
            languageCode = element.getAttribute("hreflang");
        }
        String language = getLanguageDescription(languageCode);
        if (language != null) {
            forceRead(element, language, attributeLanguagePrefixBefore,
                    attributeLanguageSuffixBefore, attributeLanguagePrefixAfter,
                    attributeLanguageSuffixAfter, DATA_ATTRIBUTE_LANGUAGE_OF);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void displayAllLanguages() {
        Collection<HTMLDOMElement> elements = parser
                .find("html[lang],body[lang],body [lang],body [hreflang]")
                .listResults();
        for (HTMLDOMElement element : elements) {
            if (CommonFunctions.isValidElement(element)) {
                displayLanguage(element);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void displayAlternativeTextImage(final HTMLDOMElement image) {
        if ((image.hasAttribute("alt")) || (image.hasAttribute("title"))) {
            if ((image.hasAttribute("alt")) && (!image.hasAttribute("title"))) {
                image.setAttribute("title", image.getAttribute("alt"));
            } else if ((image.hasAttribute("title"))
                    && (!image.hasAttribute("alt"))) {
                image.setAttribute("alt", image.getAttribute("title"));
            }
            idGenerator.generateId(image);
            image.setAttribute(DATA_ATTRIBUTE_TITLE_OF,
                    image.getAttribute("id"));
        } else {
            image.setAttribute("alt", "");
            image.setAttribute("role", "presentation");
            image.setAttribute("aria-hidden", "true");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void displayAllAlternativeTextImages() {
        Collection<HTMLDOMElement> images = parser.find("img").listResults();
        for (HTMLDOMElement image : images) {
            if (CommonFunctions.isValidElement(image)) {
                displayAlternativeTextImage(image);
            }
        }
    }
}
