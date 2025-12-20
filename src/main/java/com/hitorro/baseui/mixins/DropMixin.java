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

import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.RequestParameter;
import org.apache.tapestry5.internal.util.Holder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.MarkupWriterFactory;
import org.apache.tapestry5.services.ResponseRenderer;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.util.TextStreamResponse;

import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: Chris Date: 11/10/11 Time: 2:02 PM To change this template use File | Settings | File
 * Templates.
 */
@Import(library = {"${tapestry.scriptaculous}/dragdrop.js", "${tapestry.scriptaculous}/scriptaculous.js", "${tapestry.scriptaculous}/effects.js", "DropMixin.js"})

public class DropMixin {
    public static final String MIXIN_EVENT_NAME = "dropcall";
    public static final String EVENT_NAME = "dropEvent";
    private static final String PARAM_NAME = "t:input";


// Generally useful bits and pieces

    @Parameter(required = true)
    private String dropClass;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    @InjectContainer
    private ClientElement clientElement;

    @Inject
    private ComponentResources resources;

    @org.apache.tapestry5.ioc.annotations.Inject
    private TypeCoercer coercer;

    @org.apache.tapestry5.ioc.annotations.Inject
    private ResponseRenderer responseRenderer;

    @org.apache.tapestry5.ioc.annotations.Inject
    private MarkupWriterFactory factory;

    // The code

    public void afterRender(MarkupWriter writer) {
        JSONObject spec = new JSONObject();
        String id = clientElement.getClientId();

        String loaderId = id + ":loader";
        spec.put("indicator", loaderId);
        spec.put("elementId", id);
        spec.put("paramName", PARAM_NAME);
        spec.put("dropClass", dropClass);
        spec.put("dropId", id);
        Link link = resources.createEventLink(MIXIN_EVENT_NAME);
        spec.put("url", link.toURI());

        javaScriptSupport.addInitializerCall("dropMixin", spec);
    }

    Object onDropcall(@RequestParameter(PARAM_NAME)
                      String input) {
        final Holder<List> matchesHolder = Holder.create();

        // Default it to an empty list.

        matchesHolder.put(Collections.emptyList());

        ComponentEventCallback callback = new ComponentEventCallback() {
            public boolean handleResult(Object result) {
                List matches = coercer.coerce(result, List.class);

                matchesHolder.put(matches);

                return true;
            }
        };

        resources.triggerEvent(EVENT_NAME, new Object[]
                {input}, callback);

        ContentType contentType = responseRenderer.findContentType(this);

        MarkupWriter writer = factory.newPartialMarkupWriter(contentType);

        generateResponseMarkup(writer, matchesHolder.get());

        return new TextStreamResponse(contentType.toString(), writer.toString());
    }


    protected void generateResponseMarkup(MarkupWriter writer, List matches) {
        writer.element("ul");

        for (Object o : matches) {
            writer.element("li");
            writer.write(o.toString());
            writer.end();
        }

        writer.end(); // ul
    }
}
