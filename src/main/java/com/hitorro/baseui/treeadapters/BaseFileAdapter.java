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

import com.hitorro.util.basefile.fs.BaseFile;
import com.hitorro.util.core.Log;
import org.apache.tapestry5.tree.TreeModelAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: chris Date: 12/30/11 Time: 10:21 PM To change this template use File | Settings |
 * File Templates.
 */
public class BaseFileAdapter implements TreeModelAdapter<BaseFile> {

    public boolean isLeaf(BaseFile file) {
        return !file.isDir();
    }

    public boolean hasChildren(BaseFile file) {
        return file.isDir();
    }

    public List<BaseFile> getChildren(BaseFile file) {
        try {
            return Arrays.asList(file.listFiles());
        } catch (IOException e) {
            Log.filesystem.error("Unable to list directory %s %s %e", file, e, e);
            return new ArrayList();
        }
    }

    public String getLabel(BaseFile file) {
        return file.getName();
    }
}