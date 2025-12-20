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

import com.hitorro.base.objects.Category;
import com.hitorro.basedms.session.DMSSession;
import com.hitorro.basedms.session.DMSSessionState;
import org.apache.tapestry5.tree.TreeModelAdapter;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class CategoryAdapter implements TreeModelAdapter<Category> {

    private DMSSessionState ss;

    public CategoryAdapter(DMSSessionState ss) {
        this.ss = ss;
    }

    public static List<Category> getList(DMSSessionState session, String domain, Category parent) {
        Query q;
        if (parent != null) {
            q = ((DMSSession) session.getSession()).createQuery("from " + Category.class.getCanonicalName() + " where domain = :dom and parent = :par");
            q.setParameter("dom", domain);
            //XXX Is this right?  from setEntity to setParameter
            q.setParameter("par", parent);
        } else {
            q = ((DMSSession) session.getSession()).createQuery("from " + Category.class.getCanonicalName() + " where domain = :dom and parent is null");
            q.setParameter("dom", domain);
        }
        Iterator<Category> iter = q.stream().iterator();
        List<Category> list = new ArrayList<Category>();
        if (iter != null) {
            while (iter.hasNext()) {
                list.add(iter.next());
            }
        }
        return list;
    }

    public static Category getCat(DMSSessionState session, String domain, String value) {
        Iterator iter = session.getSession().getIteratorFromQueryArgs("from Category where domain=? and value=?", domain, value);
        if (iter != null) {
            if (iter.hasNext()) {
                return (Category) iter.next();
            }
        }
        return null;
    }

    public boolean isLeaf(Category cat) {
        return !hasChildren(cat);
    }

    public boolean hasChildren(Category cat) {
        return getChildren(cat).size() != 0;
    }

    public List<Category> getChildren(Category cat) {
        return getList(ss, cat.getDomain(), cat);
    }

    public String getLabel(Category file) {
        return file.getValue();
    }
}

