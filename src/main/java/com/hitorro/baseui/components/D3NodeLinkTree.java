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
package com.hitorro.baseui.components;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;


//@Import(library={"3rdpartyjavascript/d3_2.6.0.js", "3rdpartyjavascript/d3.layout_2.6.0.js", "D3NodeLinkTree.js"}, stylesheet = {"3rdpartyjavascript/d3_syntax.css","3rdpartyjavascript/d3_style.css", "3rdpartyjavascript/tree.css"})
@Import(library = {"d3/d3_2.6.0.js", "d3/d3.layout_2.6.0.js", "D3NodeLinkTree.js"}, stylesheet = {"d3/d3_syntax.css", "d3/d3_style.css", "d3/tree.css"})

public class D3NodeLinkTree {
    @Parameter
    private String data;

    @Parameter(required = true)
    private String width;

    @Parameter(required = true)
    private String height;

    @Parameter(required = true)
    private String graphType;

    private String clientId;

    @org.apache.tapestry5.ioc.annotations.Inject
    private ComponentResources resources;

    @Environmental
    private JavaScriptSupport jsSupport;

    @InjectContainer
    private ClientElement clientElement;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public final String getClientId() {
        if (clientId == null) {
            clientId = jsSupport.allocateClientId(resources);
        }

        return clientId;
    }


    public void afterRender() {
        JSONObject spec = new JSONObject();
        spec.put("chris", "fred");
        spec.put("type", graphType);
        spec.put("width", width);
        spec.put("height", height);
        String id = getClientId();
        spec.put("id", id);
        spec.put("jsonauth", data);
        jsSupport.addInitializerCall("d3Node", spec);
    }
}
