/*
 *   Copyright (C) 2024 Grigor Iliev <grigor@grigoriliev.com>
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

package eu.ideya.lingua.bg;

/**
 *
 */
public class Lexeme {
	public final WordEntry lemma;
	public final WordEntry[] forms;

	Lexeme(WordEntry lemma, WordEntry[] forms) {
		this.lemma = lemma;
		this.forms = forms;
	}

	/**
	 * Checks whether the specified lexeme contains the same set of words
	 * with the same grammatical properties
	 * @param l
	 * @return
	 */
	public boolean sameAs(Lexeme l) {
		if(!lemma.sameAs(l.lemma)) return false;

		if(forms.length != l.forms.length) return false;

		// note that we expect that there are no duplicate word forms in both lexemes
		for(int i = 0; i < forms.length; i++) {
			boolean found = false;

			for(int j = 0; j < l.forms.length; j++) {
				if(forms[i].sameAs(l.forms[j])) {
					found = true;
					break;
				}
			}

			if(!found) return false;
		}

		return true;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		String nl = System.getProperty("line.separator");

		sb.append(lemma.toString()).append(nl);

		for(WordEntry we : forms) {
			sb.append(we.toString()).append(nl);
		}

		return sb.toString();
	}
}
