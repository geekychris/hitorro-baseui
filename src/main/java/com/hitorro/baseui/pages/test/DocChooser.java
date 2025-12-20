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

import com.hitorro.base.objects.Document;
import com.hitorro.basedms.session.DMSSessionState;
import com.hitorro.util.core.Console;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.services.BeanModelSource;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
@Import(stylesheet = {"graphtest.css"})
public class DocChooser {
    @Property
    static private BeanModel<Document> mymodel;
    @SessionState
    private DMSSessionState ss;
    @Inject
    private BeanModelSource beanModelSource;
    private String posText = "this is my initial test";


    @org.apache.tapestry5.ioc.annotations.Inject
    private AlertManager alertManager;
    @Property
    private String dataUrl = "private String dataUrl = classpath:/ht/baseui/components/flare.json";
    @Inject
    private ComponentResources componentResources;
    @SuppressWarnings("unused")
    private List<Document> docs;
    private Document doc;

    public List<String> onDropEventFromDropMe(String v) {
        Console.println("%s", v);
        alertManager.alert(Duration.TRANSIENT, Severity.INFO, "Incrementing but transient");
        return new ArrayList();
    }

    public String getPosText() {
        return posText;
    }

    public void setPosText(String txt) {
        posText = txt;
    }

    public BeanModel<Document> getMyModel() {
        return mymodel;
    }

    public List<Document> getDocs() {
        return docs;
    }

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    void setupRender() {

        if (mymodel == null) {
            mymodel = beanModelSource.createDisplayModel(Document.class, componentResources.getMessages());
            mymodel.add("action", null);
            mymodel.include("title", "guid", "action");
            mymodel.get("title").sortable(false);
        }

        // Get all persons - ask business service to find them (from the database)
        Iterator iter = ss.getSession().getIteratorFromQuery("from Document");
        docs = new ArrayList();
        while (iter.hasNext()) {
            docs.add((Document) iter.next());
        }

    }


}