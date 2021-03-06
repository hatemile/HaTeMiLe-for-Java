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
package org.hatemile;

import org.hatemile.util.html.HTMLDOMElement;

/**
 * The AccessibleForm interface improve the accessibility of forms.
 */
public interface AccessibleForm {

    /**
     * Mark that the field is required.
     * @param requiredField The required field.
     */
    void markRequiredField(HTMLDOMElement requiredField);

    /**
     * Mark that the fields is required.
     */
    void markAllRequiredFields();

    /**
     * Mark that the field have range.
     * @param rangeField The range field.
     */
    void markRangeField(HTMLDOMElement rangeField);

    /**
     * Mark that the fields have range.
     */
    void markAllRangeFields();

    /**
     * Mark that the field have autocomplete.
     * @param autoCompleteField The field with autocomplete.
     */
    void markAutoCompleteField(HTMLDOMElement autoCompleteField);

    /**
     * Mark that the fields have autocomplete.
     */
    void markAllAutoCompleteFields();

    /**
     * Mark a solution to display that this field is invalid.
     * @param field The field.
     */
    void markInvalidField(HTMLDOMElement field);

    /**
     * Mark a solution to display that a fields are invalid.
     */
    void markAllInvalidFields();
}
