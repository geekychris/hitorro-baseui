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
package com.hitorro.baseui.treeadapters;

import com.hitorro.util.core.ArrayUtil;
import opennlp.tools.parser.Parse;
import opennlp.tools.util.Span;
import org.apache.tapestry5.tree.TreeModelAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents open nlp chunker as a tree of nodes.
 */
public class OpenNLPChunkerAdapter implements TreeModelAdapter<Parse> {

    public boolean isLeaf(Parse p) {
        return !hasChildren(p);
    }

    public boolean hasChildren(Parse p) {
        if (p == null) {
            return false;
        }
        return !ArrayUtil.nullOrEmpty(p.getChildren());
    }

    public List<Parse> getChildren(Parse p) {
        if (p == null) {
            return new ArrayList();
        }

        return Arrays.asList(p.getChildren());
    }

    public String getLabel(Parse p) {
        if (p == null) {
            return "";
        }
        Span s = p.getSpan();
        String type = p.getType();
        return p.getText().substring(s.getStart(), s.getEnd()) + "(" + type + ")";
    }
}
