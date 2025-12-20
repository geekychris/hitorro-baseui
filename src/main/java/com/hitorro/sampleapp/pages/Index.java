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

import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.util.TextStreamResponse;

import java.util.Date;

/**
 * Start page of application dojotapestry5demo.
 */
/*@DojoInitialization*/
public class Index {

    @Inject
    @Property
    private Block _time2Block;

    @Inject
    private ComponentResources _resources;

    @Inject
    private Request req;

    public Link getDatasource() {
        return _resources.createEventLink("datasource");
    }


    Block onActionFromRefreshZone() {
        // Return the zone we want rendered. Without Ajax we'd typically return the page we want rendered.
        return _time2Block;
    }

    public Date getCurrentTime() {
        return new Date();
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

}
