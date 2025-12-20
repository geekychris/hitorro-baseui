/*
 * Copyright (c) 2006-2025 Chris Collins
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.hitorro.baseui.components.partsofspeech;

import com.hitorro.util.core.events.cache.PoolContainer;
import com.hitorro.util.core.string.StringUtil;
import org.apache.tapestry5.annotations.Parameter;

public class POSChunk {
    @Parameter
    private String posText;


    @Parameter
    private String language;

    private String sentence;

    private String[] sentences;

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sent) {
        this.sentence = sent;
    }

    public String getLanguage() {
        return language;
    }

    public String[] getSentences() {
        if (StringUtil.nullOrEmptyString(posText)) {
            return new String[0];
        }
        PoolContainer<com.hitorro.language.IsoLanguage, com.hitorro.language.SentenceSegmenter> pool = null;
        com.hitorro.language.SentenceSegmenter ss = null;
        try {
            com.hitorro.language.IsoLanguage lang = null;
            if (StringUtil.nullOrEmptyString(language)) {
                lang = com.hitorro.language.Iso639Table.getInstance().getLanguageFromContent(posText);
            } else {
                lang = com.hitorro.language.Iso639Table.getInstance().getRow(language);
            }

            pool = com.hitorro.language.SentenceDetectorSingleton.singleton.get(lang);
            ss = pool.get();
            com.hitorro.language.Sentences s = ss.getSentenceOffsets(posText);
            return s.getSentencesDirect();
        } finally {
            if (pool != null && ss != null) {
                pool.returnIt(ss);
            }
        }
    }
}
