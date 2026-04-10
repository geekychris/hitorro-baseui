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
package com.hitorro.baseui.pages.test;

import com.hitorro.baseui.treeadapters.OpenNLPChunkerAdapter;
import com.hitorro.language.Iso639Table;
import com.hitorro.language.IsoLanguage;
import com.hitorro.language.PartOfSpeech;
import com.hitorro.language.PartOfSpeechSingletonMapper;
import com.hitorro.util.core.events.cache.PoolContainer;
import com.hitorro.util.core.string.StringUtil;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.tree.DefaultTreeModel;
import org.apache.tapestry5.tree.TreeModel;


public class POS {
    @Persist
    private String posText;
    @Persist
    private String language;
    @Inject
    private JavaScriptSupport javaScriptSupport;
    private Parse parse;

    void onActionFromFail() {
        throw new RuntimeException("Failure inside action event handler.");
    }

    public String getPosText() {
        return posText;
    }

    public void setPosText(String text) {
        this.posText = text;
    }

    @Validate("required")
    public String getLanguage() {
        return language;
    }

    // The code

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setupRender() {

        // Add some JavaScript to the page to "grow" the element called "content_header". It will run when the DOM has been
        // fully loaded.

        javaScriptSupport.addScript(InitializationPriority.LATE, String.format("Effect.Grow('%s');", "content_header"));
    }

    void onActivate() {
        PoolContainer<IsoLanguage, PartOfSpeech> posPool = PartOfSpeechSingletonMapper.singleton.get(Iso639Table.english);
        PartOfSpeech pos = posPool.get();
        try {
            if (StringUtil.nullOrEmptyString(posText)) {
                return;
            }
            com.hitorro.language.POS p = pos.getPOS(posText);
            Parser parser = pos.getParser();
            Parse t[] = ParserTool.parseLine(posText, parser, 1);
            parse = t[0];
        } finally {
            posPool.returnIt(pos);
        }
    }

    public TreeModel<Parse> getFileModel() {
        ValueEncoder<Parse> encoder = new ValueEncoder<Parse>() {

            public String toClient(Parse p) {
                if (p == null) {
                    return null;
                }
                return p.toString();
            }

            public Parse toValue(String p) {
                Parse pa = Parse.parseParse(p);
                return pa;
            }
        };

        return new DefaultTreeModel<Parse>(encoder, new OpenNLPChunkerAdapter(), parse);

    }

    public Parse getRootDirectory() {
        return parse;
    }
}
