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

import com.hitorro.base.objects.Category;
import com.hitorro.basedms.session.DMSSessionState;
import com.hitorro.baseui.treeadapters.CategoryAdapter;
import com.hitorro.util.core.string.Fmt;
import com.hitorro.util.core.string.StringUtil;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Tree;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.util.Stack;
import org.apache.tapestry5.tree.*;

import java.util.List;


public class CategoryTreeTest {
    @SessionState
    private DMSSessionState ss;
    private String category = "testcat";

    private Category directory;

    @Persist
    private TreeSelectionModel selectionModel;
    @InjectComponent
    private Tree tree;
    @Property
    private Category selectedObject;

    void onActivate() {
    }

    @Log
    public TreeSelectionModel<Category> getSelectionModel() {
        if (selectionModel == null) {
            selectionModel = new DefaultTreeSelectionModel();
        }

        return selectionModel;
    }

    //////

    public TreeModel<Category> getCategoryModel() {
        ValueEncoder<Category> encoder = new ValueEncoder<Category>() {

            public String toClient(Category cat) {
                return Fmt.S("%s:%s", cat.getDomain(), cat.getValue());
            }

            public Category toValue(String name) {
                String parts[] = StringUtil.tokenizeEscapedString(name, ':', false);
                return CategoryAdapter.getCat(ss, parts[0], parts[1]);
            }
        };

        return new DefaultTreeModel<Category>(encoder, new CategoryAdapter(ss), getRootList());

    }

    public List<Category> getRootList() {
        return CategoryAdapter.getList(ss, category, null);
    }

    void onActionFromClearAll() {
        tree.getExpansionModel().clear();
        tree.getSelectionModel().clear();
    }

    public List<Category> getSelectedObjects() {

        List<Category> result = CollectionFactory.newList();
        Stack<TreeNode<Category>> queue = CollectionFactory.newStack();

        TreeModel<Category> model = getCategoryModel();
        TreeSelectionModel<Category> selectionModel = getSelectionModel();

        for (TreeNode<Category> root : model.getRootNodes()) {
            queue.push(root);
        }


        while (!queue.isEmpty()) {
            TreeNode<Category> current = queue.pop();

            if (selectionModel.isSelected(current)) {
                result.add(current.getValue());
            }

            for (TreeNode<Category> child : current.getChildren()) {
                queue.push(child);
            }
        }


        return result;
    }
}