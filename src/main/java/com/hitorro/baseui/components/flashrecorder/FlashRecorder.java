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
package com.hitorro.baseui.components.flashrecorder;

import com.hitorro.util.core.string.Fmt;
import com.hitorro.util.core.string.StringUtil;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import javax.inject.Inject;


@Import(library = {"jquery.webcam.js", "flashrecorder.js"})

public class FlashRecorder {
    @Inject
    private JavaScriptSupport javaScriptSupport;

    @org.apache.tapestry5.ioc.annotations.Inject
    private ComponentResources resources;

    @Environmental
    private JavaScriptSupport javascriptSupport;

    @InjectContainer
    private ClientElement clientElement;

    // The code

    private String id;
    private String idDecorated;
    @Parameter
    private String data;

    public String getClientElement() {
        return idDecorated;
    }

    public void setupRender() {
        String id = javascriptSupport.allocateClientId(resources);
        idDecorated = Fmt.S("%s_flashrecorder", id);
    }

    public void setData(String data) {
        this.data = data;
    }

    public void afterRender() {
        JSONObject spec = new JSONObject();
        spec.put("targetId", StringUtil.strcat(".", idDecorated));
        //spec.put("dragId", clientElement.getClientId());

        spec.put("recorder", data);


        javaScriptSupport.addInitializerCall("flashRecorder", spec);
    }
}
