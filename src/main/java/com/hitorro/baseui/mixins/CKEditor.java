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
package com.hitorro.baseui.mixins;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import java.util.HashMap;
import java.util.Map;

@Import(library = "ckeditor/ckeditor.js")
public class CKEditor {
    @Parameter
    private Map<String, ?> parameters;

    @InjectContainer
    private TextArea textArea;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    Map<String, ?> defaultParameters() {
        return new HashMap<String, String>() {
            {
                put("toolbar", "Full");
            }
        };
    }

    void afterRender(MarkupWriter writer) {
        String name = textArea.getControlName();
        String id = textArea.getClientId();

        JSONObject json = new JSONObject();
        if (parameters != null)
            for (String paramName : parameters.keySet())
                json.put(paramName, parameters.get(paramName));


        javaScriptSupport.addScript("CKEDITOR.replace('%s', %s);", name, json.toCompactString());
        javaScriptSupport.addScript("document.observe(Tapestry.FORM_PREPARE_FOR_SUBMIT_EVENT, function(){"
                + "%s.value = $$('#%s iframe')[0].contentDocument.body.innerHTML})", id, "cke_" + id);
    }
}
