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

import com.hitorro.analysis.cky.ling.Constituent;
import com.hitorro.analysis.cky.ling.PennTreeRenderer;
import com.hitorro.analysis.cky.ling.Tree;
import com.hitorro.analysis.cky.model.Grammar;
import com.hitorro.analysis.cky.model.GrammarSingletonMapper;
import com.hitorro.analysis.cky.model.OpenNLPCKYLexicon;
import com.hitorro.analysis.cky.model.TreeAnnotations;
import com.hitorro.analysis.cky.parser.CKYParser;
import com.hitorro.analysis.mstparser.DependencyResult;
import com.hitorro.analysis.mstparser.MSTParser;
import com.hitorro.analysis.mstparser.MSTParserSingleton;
import com.hitorro.util.core.Console;
import com.hitorro.util.core.events.cache.PoolContainer;
import com.hitorro.util.core.string.StringUtil;
import opennlp.tools.parser.Parse;
import opennlp.tools.util.Span;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import java.util.ArrayList;
import java.util.List;

public class POSRow {
    @Parameter
    private String language;

    @Parameter
    private String posText;
    @Property
    private String posPart;

    private com.hitorro.language.PennAndTreebankBase patb;
    @Property
    private String textPart;
    @Persist
    private String[] textParts;
    @Persist
    private String[] posParts;
    @Persist
    private String names[];
    @Property
    private String name;
    @Persist
    private String deps[];
    @Property
    private String dep;
    @Persist
    private String heads[];
    @Property
    private String head;
    private String chunk;
    private boolean doNER = true;

    public String[] getPosParts() {

        return posParts;

    }

    public String posPartDetails() {
        if (patb == null) {
            return "";
        }
        com.hitorro.language.PATElem elem = patb.getValue(posPart);
        if (elem == null) {
            return null;
        }
        return elem.getDescription();
    }

    public String chunkerDetails() {
        return chunk;
    }

    public String[] getTextParts() {
        foo();
        return textParts;
    }

    public String getPosText() {
        return posText;

    }

    public void setPosText(String posText) {
        this.posText = posText;
    }

    public String[] getNames() {
        return names;
    }

    public String[] getDeps() {
        return deps;
    }

    public String[] getHeads() {
        return heads;
    }

    void foo() {
        String sentence = getPosText();
        if (StringUtil.nullOrEmptyString(sentence)) {
            return;
        }
        com.hitorro.language.IsoLanguage lang = null;
        if (StringUtil.nullOrEmptyString(language)) {
            lang = com.hitorro.language.Iso639Table.english;
        } else {
            lang = com.hitorro.language.Iso639Table.getInstance().getRow(language);
        }
        patb = lang.getPennAndTreebank();
        PoolContainer<com.hitorro.language.IsoLanguage, com.hitorro.language.PartOfSpeech> posPool = com.hitorro.language.PartOfSpeechSingletonMapper.singleton.get(lang);
        com.hitorro.language.PartOfSpeech pos = posPool.get();
        try {

            if (StringUtil.nullOrEmptyString(sentence)) {
                posParts = new String[0];
                textParts = posParts;
            }
            pos.reset();
            com.hitorro.language.POS p = pos.getPOS(sentence);
            String f[] = p.getTokenizedText();
            textParts = f;
            List<String>[] tags = p.getTags();
            String pe0[] = p.getTagsEnglish(0);
            posParts = pe0;
            if (doNER) {
                com.hitorro.language.NameFinder person = p.getNameFinder(com.hitorro.language.IsoLanguage.NameFinderIntent.Person);
                com.hitorro.language.NameFinder location = p.getNameFinder(com.hitorro.language.IsoLanguage.NameFinderIntent.Location);
                com.hitorro.language.NameFinder org = p.getNameFinder(com.hitorro.language.IsoLanguage.NameFinderIntent.Organization);
                com.hitorro.language.NameFinder date = p.getNameFinder(com.hitorro.language.IsoLanguage.NameFinderIntent.Date);
                com.hitorro.language.NameFinder money = p.getNameFinder(com.hitorro.language.IsoLanguage.NameFinderIntent.Money);
                person.setToks(f);
                location.setToks(f);
                org.setToks(f);
                names = StringUtil.combineStringArrays(new String[][]{
                        StringUtil.preapendArray(person.getNames(), "person="),
                        StringUtil.preapendArray(location.getNames(), "location="),
                        StringUtil.preapendArray(org.getNames(), "org="),
                        StringUtil.preapendArray(date.getNames(), "date="),
                        StringUtil.preapendArray(money.getNames(), "money=")});
            }

            CKYParser parser = null;
            if (parser == null) {
                OpenNLPCKYLexicon lex = new OpenNLPCKYLexicon("en");
                Grammar g = GrammarSingletonMapper.singleton.get(null);
                parser = new CKYParser(g, lex);
            }
            String parts[] = StringUtil.tokenizeFromSingleChar(sentence, " ");
            parser.parse(parts);
            Tree<String> tree = parser.getBacktrace("ROOT");
            if (tree == null) {
                tree = new Tree<String>("empty");
            }
            tree = TreeAnnotations.unAnnotateTree(tree);
            Tree<String> de = TreeAnnotations.deMarkovization(tree);
            List<Constituent<String>> list = de.toConstituentList();
            StringBuilder sb = new StringBuilder();
            sb.append(PennTreeRenderer.renderHTML(de, patb));

            Console.bprint(sb, "<br/><br/>Constituency<br/><br/>");
            for (Constituent<String> c : list) {
                int start = c.getStart();
                int end = c.getEnd();
                String cont = StringUtil.mergeWithJoinToken(parts, " ", start, end - start);

                Console.bprint(sb, "%s start:%s end:%s [%s] <br>", c.getLabel(), c.getStart(), c.getEnd(), cont);
            }


            //getMSTParse(sb);
            chunk = sb.toString();


            // chunker
            //
            // CFGParserInterface parser = p.getParser();
            //chunk = printParseTree(ParserTool.parseLine(sentence, parser, 1)[0]);
            //chunk = "";


            //return tags[0];
        } finally {
            posPool.returnIt(pos);
        }
    }

    private void getMSTParse(final StringBuilder sb) {
        PoolContainer<com.hitorro.language.IsoLanguage, MSTParser> mstp = null;
        MSTParser mstParser = null;
        try {
            mstp = MSTParserSingleton.singleton.get(com.hitorro.language.Iso639Table.english);
            mstParser = mstp.get();
            DependencyResult di = mstParser.get(textParts, posParts, true);
            Tree<String> depTree = di.getTree();
            Console.bprint(sb, "Dependency Parse<br/><br/>");
            sb.append(PennTreeRenderer.renderHTML(depTree, patb));
            deps = di.labels;
            heads = new String[deps.length];
            for (int i = 0; i < deps.length; i++) {
                heads[i] = Integer.toString(di.heads[i]);
            }
        } finally {
            mstp.returnIt(mstParser);
        }
    }

    private String printParseTree(Parse p) {
        StringBuilder sb = new StringBuilder();
        ArrayList<ArrayList<String>> matrix = new ArrayList();
        printParseTreeAux(p, 0, 0, matrix);
        for (int i = 0; i < matrix.size(); i++) {

            ArrayList<String> row = matrix.get(i);
            for (int j = 0; j < row.size(); j++) {
                if (j != 0) {
                    Console.bprint(sb, "________");
                }
                Console.bprint(sb, row.get(j));

            }
            Console.bprintln(sb);
        }
        return sb.toString();

    }

    private void printParseTreeAux(Parse p, int level, int part, ArrayList<ArrayList<String>> matrix) {
        Span s = p.getSpan();
        if (matrix.size() < level + 1) {
            matrix.add(level, new ArrayList());
        }
        ArrayList<String> row = matrix.get(level);
        if (row == null) {
            row = new ArrayList();
            matrix.set(level, row);
        }
        row.add(p.getText().substring(s.getStart(), s.getEnd()));
        int index = 0;
        for (Parse pChild : p.getChildren()) {
            printParseTreeAux(pChild, level + 1, index++, matrix);
        }
    }

}
