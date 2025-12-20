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
package com.hitorro.sampleapp.pages;

import com.hitorro.sampleapp.context.Beacon;
import com.hitorro.util.core.Console;
import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
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
public class chris {
    @Persist
    private Beacon beacon;

    //@InjectComponent
    //private BeanEditForm beanEditForm;

    @Inject
    private ComponentResources _resources;

    @Inject
    private Request req;
    @Persist
    private String t;

    /*public BeanEditForm getBeanEditForm ()
    {
        return beanEditForm;
    }

    public void setBeanEditForm (BeanEditForm bef)
    {
        beanEditForm = bef;
    } */
    @Persist
    private Date dob;
    private boolean done = false;
    // Zone stuff
    @Property
    @Persist
    private int clickCount;
    @InjectComponent
    private Zone counterZone;

    public Link getDatasource() {
        return _resources.createEventLink("datasource");
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon b) {
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

    void onSelectedFromDone() {
        done = true;
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
        Beacon b = getBeacon();
        Console.println("Success");
        return Index.class;
    }


}
