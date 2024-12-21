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

import java.io.Serializable;

/**
 *
 */
public class WordEntry implements Comparable<WordEntry>, Serializable {
	private static int counter = 0;

	public final String word;
	public final int    id;
	public final int    lemmaId;
	public final int    grammLabelUid;

	/**
	 *
	 * @param word
	 * @param lemmaId The ID of the lemma, which represents this
	 * word form. Set to <code>-1</code> if the current word is a lemma.
	 * @param grammLabelUid
	 */
	public WordEntry(String word, int lemmaId, int grammLabelUid) {
		this.word = word;
		this.id = ++counter;
		this.lemmaId = lemmaId;
		this.grammLabelUid = grammLabelUid;
	}

	public boolean isLemma() { return lemmaId == -1; }

	/**
	 * Checks whether this word is the same as the specified word and
	 * with the same grammatical properties.
	 */
	public boolean sameAs(WordEntry we) {
		if(!word.equals(we.word)) return false;
		if(grammLabelUid != we.grammLabelUid) return false;

		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null && !(obj instanceof WordEntry)) return false;
		return id == ((WordEntry)obj).id;
	}

	@Override
	public int compareTo(WordEntry e) {
		return id - e.id;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(word);
		addPadding(word, sb);
		sb.append(' ');

		if(isLemma()) {
			sb.append("(").append(
				I18n.getInstance().getLabel("WordEntry.lemma")
			).append(")");
		}

		GrammaticalLabel.LexicalClass l = GrammaticalLabel.getLexicalClass(grammLabelUid);

		sb.append(" ").append(l.toString());

		GrammaticalLabel.Number  n = GrammaticalLabel.getNumber(grammLabelUid);
		GrammaticalLabel.Gender  g = GrammaticalLabel.getGender(grammLabelUid);
		GrammaticalLabel.Article a = GrammaticalLabel.getArticle(grammLabelUid);

		switch(l) {
			case NOUN:

				GrammaticalLabel.NounCase nc =
					GrammaticalLabel.getNounCase(grammLabelUid);

				if(nc != GrammaticalLabel.NounCase.NONE) {
					sb.append(" (").append(nc).append(")");
				}
				GrammaticalLabel.NounType nt =
					GrammaticalLabel.getNounType(grammLabelUid);

				sb.append(", ").append(nt);

				sb.append(", ").append(g);

				if(a != GrammaticalLabel.Article.NONE) {
					sb.append(", ").append(a);
				}

				sb.append(", ").append(n);
				break;
			case ADJECTIVE:
				GrammaticalLabel.AdjectiveCase ac =
					GrammaticalLabel.getAdjectiveCase(grammLabelUid);
				if(ac != GrammaticalLabel.AdjectiveCase.NONE) {
					sb.append(" (").append(ac).append(")");
				}

				if(n == GrammaticalLabel.Number.SINGULAR) {
					sb.append(", ").append(g);
				}

				sb.append(", ").append(n);
				sb.append(", ").append(a);
				break;
			case PRONOUN:
				sb.append(getPronounInfo());
				break;
			case VERB:
				sb.append(getVerbInfo());
				break;
			case NUMERAL:
				sb.append(getNumeralInfo());
				break;
		}


		return sb.toString();
	}

	private String getPronounInfo() {
		StringBuffer sb = new StringBuffer();

		GrammaticalLabel.PronounType pt = GrammaticalLabel.getPronounType(grammLabelUid);
		sb.append(", ").append(pt);


		GrammaticalLabel.PronounForm pf = GrammaticalLabel.getPronounForm(grammLabelUid);

		if(pf != GrammaticalLabel.PronounForm.NONE) {
			sb.append(", ").append(pf);
		}

		GrammaticalLabel.PronounCase pc = GrammaticalLabel.getPronounCase(grammLabelUid);

		if(pc != GrammaticalLabel.PronounCase.NONE) {
			sb.append(", ").append(pc);
		}

		GrammaticalLabel.Number n = GrammaticalLabel.getNumber(grammLabelUid);
		if(n != GrammaticalLabel.Number.NONE) sb.append(", ").append(n);

		GrammaticalLabel.Person p = GrammaticalLabel.getPerson(grammLabelUid);
		if(p != GrammaticalLabel.Person.NONE) sb.append(", ").append(p);

		if(n == GrammaticalLabel.Number.SINGULAR) {
			GrammaticalLabel.Gender g = GrammaticalLabel.getGender(grammLabelUid);

			if(g != GrammaticalLabel.Gender.NONE) {
				sb.append(", ").append(g);
			}
		}

		GrammaticalLabel.Article a = GrammaticalLabel.getArticle(grammLabelUid);
		if(a != GrammaticalLabel.Article.NONE) sb.append(", ").append(a);

		return sb.toString();
	}

	private String getVerbInfo() {
		StringBuffer sb = new StringBuffer();

		GrammaticalLabel.VerbType vt = GrammaticalLabel.getVerbType(grammLabelUid);
		sb.append(", ").append(vt);

		GrammaticalLabel.Aspect a = GrammaticalLabel.getAspect(grammLabelUid);
		sb.append(", ").append(a);

		GrammaticalLabel.Transitivity t = GrammaticalLabel.getTransitivity(grammLabelUid);
		sb.append(", ").append(t);

		GrammaticalLabel.VerbForm vf = GrammaticalLabel.getVerbForm(grammLabelUid);
		sb.append(", ").append(vf);

		GrammaticalLabel.Voice v = GrammaticalLabel.getVoice(grammLabelUid);
		if(v != GrammaticalLabel.Voice.NONE) sb.append(", ").append(v);

		GrammaticalLabel.Tense te = GrammaticalLabel.getTense(grammLabelUid);
		if(te != GrammaticalLabel.Tense.NONE) sb.append(", ").append(te);

		GrammaticalLabel.Person p = GrammaticalLabel.getPerson(grammLabelUid);
		if(p != GrammaticalLabel.Person.NONE) sb.append(", ").append(p);

		GrammaticalLabel.Number n = GrammaticalLabel.getNumber(grammLabelUid);
		if(n != GrammaticalLabel.Number.NONE) sb.append(", ").append(n);

		GrammaticalLabel.Gender g = GrammaticalLabel.getGender(grammLabelUid);
		if(g != GrammaticalLabel.Gender.NONE) sb.append(", ").append(g);

		GrammaticalLabel.Article ar = GrammaticalLabel.getArticle(grammLabelUid);
		if(ar != GrammaticalLabel.Article.NONE) sb.append(", ").append(ar);

		return sb.toString();
	}

	private String getNumeralInfo() {
		StringBuffer sb = new StringBuffer();

		GrammaticalLabel.NumeralType nt = GrammaticalLabel.getNumeralType(grammLabelUid);
		if(nt != GrammaticalLabel.NumeralType.NONE) {
			sb.append(", ").append(nt);
		}

		GrammaticalLabel.Gender g = GrammaticalLabel.getGender(grammLabelUid);

		if(g != GrammaticalLabel.Gender.NONE) {
			sb.append(", ").append(g);
		}

		GrammaticalLabel.Number n = GrammaticalLabel.getNumber(grammLabelUid);
		if(n != GrammaticalLabel.Number.NONE) sb.append(", ").append(n);

		GrammaticalLabel.Article a = GrammaticalLabel.getArticle(grammLabelUid);
		if(a != GrammaticalLabel.Article.NONE) sb.append(", ").append(a);

		return sb.toString();
	}

	private void addPadding(String word, StringBuffer buf) {
		int i = 25 - word.length() - (isLemma() ? 7 : 0);
		for(; i > 0; i--) buf.append(' ');
	}
}
