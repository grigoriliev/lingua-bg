/*
 *   Copyright (C) 2024-2026 Grigor Iliev <grigor@grigoriliev.com>
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package eu.ideya.lingua.bg.core;

import java.util.Locale;

/**
 *
 */
public class I18n extends eu.ideya.simplicity.I18n {
	/** Provides the locale-specific data of this library. */
	private static final I18n INSTANCE = new I18n();

	private static final Locale[] locales = {  new Locale("en", "US") };

	private I18n() {
		//setButtonsBundle("eu.ideya.lingua.bg.core.langprops.ButtonsLabelsBundle");
		setErrorsBundle("eu.ideya.lingua.bg.core.langprops.ErrorsBundle");
		setLabelsBundle("eu.ideya.lingua.bg.core.langprops.LabelsBundle");
		//setLogsBundle("eu.ideya.lingua.bg.core.langprops.LogsBundle");
		//setMenusBundle("eu.ideya.lingua.bg.core.langprops.MenuLabelsBundle");
		setMessagesBundle("eu.ideya.lingua.bg.core.langprops.MessagesBundle");
	}

	public static I18n getInstance() { return INSTANCE; }

	/**
	 * Gets all available locales.
	 * @return All available locales.
	 */
	public static Locale[] getAvailableLocales() { return locales; }
}
