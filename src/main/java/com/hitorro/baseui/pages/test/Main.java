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
import com.hitorro.util.basefile.fs.BaseFile;
import com.hitorro.util.basefile.fs.file.FileFileSystem;
import com.hitorro.util.core.Console;
import com.hitorro.util.core.ListUtil;
import com.hitorro.util.core.opers.AlwaysTrueOperator;
import com.hitorro.util.typesystem.constraint.IsViewForType;
import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.util.TextStreamResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * call this to start up
 * <p/>
 * http://localhost:8072/hitorro/chris
 * <p/>
 * If this shit doesnt work, verify the jar files that are loaded (at the point I write this beaneditor jfly and a few
 * other were causing havoc).
 */
//@IncludeJavaScriptLibrary("${tapestry.scriptaculous}/dragdrop.js")
//@DojoInitialization
public class Main {
    @Property
    static private BeanModel<Document> mymodel;
    @SessionState
    private DMSSessionState ss;
    @Persist
    private Document beacon;
    @Inject
    private ComponentResources _resources;
    @Inject
    private Request req;
    @Property
    private String guid;

    // The code
    @Persist
    private String t;
    @Persist
    private Date dob;

    /*public BeanEditForm getBeanEditForm ()
    {
        return beanEditForm;
    }

    public void setBeanEditForm (BeanEditForm bef)
    {
        beanEditForm = bef;
    } */
    // Zone stuff
    @Property
    @Persist
    private int clickCount;
    @InjectComponent
    private Zone counterZone;
    @javax.inject.Inject
    private BeanModelSource beanModelSource;
    @javax.inject.Inject
    private ComponentResources componentResources;
    @Inject
    private AlertManager alertManager;
    @InjectComponent
    private Zone zone;
    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    public Link getDatasource() {
        return _resources.createEventLink("datasource");
    }

    String onPassivate() {
        return guid;
    }

    void onActivate(String guid) {
        this.guid = guid;
    }

    public Document getBeacon() {
        beacon = (Document) ss.getSession().getHTSerializableFromGUID(guid);
        return beacon;
    }

    public void setBeacon(Document b) {
        beacon = b;
    }

    public Object onDatasource() {

        int start = getDatasourceStart();
        int count = getDatasourceCount();
        //String sorting = getReqParam("sort");

        int numRows = 10000;

        JSONObject myResults = new JSONObject();
        myResults.put("numRows", numRows);
        JSONArray items = new JSONArray();


        for (int i = start + 1; i <= (start + count); i++) {
            JSONObject item = new JSONObject();
            item.put("id", "" + i);
            item.put("name", "nam_" + i);
            item.put("type", "typ_" + i);
            items.put(item);
        }

        myResults.put("items", items);

        return new TextStreamResponse("application/json", myResults.toString());
    }

    private Integer getDatasourceStart() {
        return getIntReqParam("start", 0);
    }

    private Integer getDatasourceCount() {
        return getIntReqParam("count", 20);
    }

    private String getReqParam(String paramName) {
        String value = null;
        String parameter = req.getParameter(paramName);
        if (parameter != null) {
            value = parameter;
        }
        return value;
    }

    private Integer getIntReqParam(String paramName, Number defaultVal) {
        String valueString = getReqParam(paramName);

        Integer value = (Integer) defaultVal;
        if (!StringUtils.isEmpty(valueString) && StringUtils.isNumeric(valueString)) {
            value = Integer.parseInt(valueString);
        }
        return value;
    }

    public Date getCurrentTime() {
        return new Date();
    }

    public String getMyText() {
        return t;
    }

    //Chart2D chart1;
    public void setMyText(String t) {
        this.t = t;
    }

    public Date getDateOfBirth() {
        if (dob == null) {
            dob = new Date(System.currentTimeMillis());
        }
        return dob;
    }

    public void setDateOfBirth(Date dob) {
        this.dob = dob;
    }

    Object onActionFromClicker() {
        clickCount++;

        return counterZone.getBody();
    }

    List<String> onProvideCompletionsFromAccountName(String partial) {
        List<String> list = new ArrayList();
        list.add(partial + "aaa");
        list.add(partial + "bbb");
        return list;
    }

    Object onSuccess() {
        com.hitorro.util.typesystem.BaseSession sess = ss.getSession();
        BaseFile a = FileFileSystem.Root.getFile("/wiki.xml");
        if (!a.exists()) {
            Console.println();
        }

        Document doc = getBeacon();
        // doc.setContent("ContentString");
        //  ContentType ct = ContentTypeCache.getCache().getContentTypeByMimeType("application/msword");

        //doc.setContent("a.html", ct, a);
        sess.saveOrUpdate(doc);
        sess.commit();

        Console.println("Success");
        return Main.class;
    }

    public BeanModel<Document> getMyModel() {
        return mymodel;
    }

    void setupRender() {

        String arr[] = new String[0];
        Document doc = getBeacon();
        if (doc != null) {
            com.hitorro.util.typesystem.TypeIntf ti = com.hitorro.util.typesystem.TypeManager.getTypeManager().getTypeForBaseType(doc);
            IsViewForType c = new IsViewForType(ti, "edit");
            List<com.hitorro.util.typesystem.Type> list = com.hitorro.util.typesystem.TypeManager.getTypeManager().getTypesMatchingConstraint(c);
            if (!ListUtil.nullOrEmpty(list)) {
                com.hitorro.util.typesystem.Type view = list.get(0);
                List<com.hitorro.util.typesystem.TypeFieldIntf> viewFields = view.getTypeFieldsByConstraint(AlwaysTrueOperator.oper);
                arr = new String[viewFields.size() - 1];
                int counter = 0;
                for (int i = 0; i < viewFields.size(); i++) {
                    String name = viewFields.get(i).getName();
                    if (!name.equals("class")) {
                        arr[counter++] = name;
                    }
                }
            }
        }
        //if (mymodel == null)
        {
            Class c = doc.getClass();
            mymodel = beanModelSource.createDisplayModel(c, componentResources.getMessages());
            mymodel.add("action", null);
            mymodel.include(arr);
            mymodel.get("title").sortable(false);
            String s = mymodel.toString();
            Console.println();
        }


    }

    Object onActionFromIncrementAjax() {
        clickCount++;

        alertManager.alert(Duration.TRANSIENT, Severity.INFO, "Incrementing but transient");
        alertManager.info("Increment (via Ajax) clicked");


        ajaxResponseRenderer.addCallback(new JavaScriptCallback() {
            public void run(JavaScriptSupport javascriptSupport) {
                JSONObject spec = new JSONObject();
                spec.put("chris", "fred");
                spec.put("jsonauth", "http://mbostock.github.com/d3/data/flare.json");
                javascriptSupport.addInitializerCall("d3Node", spec);
            }
        });


        /**
         * Why does this work?  Well it seems that when you make an ajax request effectively the response goes through some kind of
         * generic ajax response.   When the alertManager is called it adds to the ajax response its own method to invoke:
         *
         It constructs an anonymouse class that is a javascript callback that is added to the ajax response renderer:

         private final AjaxResponseRenderer ajaxResponseRenderer;

         ...
         ajaxResponseRenderer.addCallback(new JavaScriptCallback()
         {
         public void run(JavaScriptSupport javascriptSupport)
         {
         javascriptSupport.addInitializerCall("addAlert", alert.toJSON());
         }
         });
         */

        return zone;
    }


}
