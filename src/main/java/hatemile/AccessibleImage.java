/*
Copyright 2014 Carlson Santana Cruz

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
package hatemile;

import hatemile.util.HTMLDOMElement;

/**
 * The AccessibleImage interface fix the problems of accessibility associated
 * with images.
 */
public interface AccessibleImage {
	
	/**
	 * Provide a alternative way for access the longs descriptions of element.
	 * @param element The element with long description.
	 */
	public void fixLongDescription(HTMLDOMElement element);
	
	/**
	 * Provide a alternative way for access the longs descriptions of elements.
	 */
	public void fixLongDescriptions();
}