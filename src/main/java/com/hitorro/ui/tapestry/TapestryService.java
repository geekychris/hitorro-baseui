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
package com.hitorro.ui.tapestry;

import com.hitorro.network.servlet.ServletService;
import com.hitorro.ui.RawFilePostWriterServlet;
import com.hitorro.util.core.Env;
import com.hitorro.util.core.Log;
import com.hitorro.util.core.string.StringUtil;
import com.hitorro.util.json.keys.StringProperty;
import com.hitorro.util.startupframework.ServiceContext;
import com.hitorro.util.startupframework.phases.ServiceDefinition;

import java.io.File;
import java.io.IOException;

/**
 * Service to support the Tapestry UI framework. Note from Adobe about using .tml extensions with DreamWeaver:
 * <p/>
 * http://kb2.adobe.com/cps/164/tn_16410.html
 * <p>
 * Here is an example URI for test UI:   http://localhost:8072/hitorro/test/filetree
 *
 * @author chris
 */
@ServiceDefinition(dependentService = {ServletService.class},
        shortName = "tapestry",
        description = "Tapestry service",
        debugCommands = {},
        typeManagedClasses = {},
        uiDirectories = {"public", "ui"},
        dependentServiceInterfaces = {})
public class TapestryService {
    // this constant needs to be coordinated with the servlet mapping in web.xml and hivemodule.xml
    public static final String DotPageExtension = ".htm";

    /**
     * the application compContext
     */
    public static StringProperty ContextPath =
            new StringProperty("ui.contextpath", "compContext path for app", "/com/hitorro");

    public TapestryService() {
    }

    public static TapestryService getService() {
        return (TapestryService) ServiceContext.getSC().getInitializedModule(TapestryService.class);
    }

    public String init(boolean dbInit, final boolean upgrading, final long currentVersion, final long targetVersion) {
        // install our war application into the servlet engine
        ServletService ssrv = ServletService.getService();
        if (ssrv == null) {
            String msg = "Could not initialize Tapestry service because ServletService is missing";
            Log.tapestry.error(msg);
            return msg;
        }

        ssrv.addServlet(new RawFilePostWriterServlet(), "rawupload", "/upload.php");

        String warPath;
        try {
            File pathF = new File(Env.getUIResourceDir());
            Log.tapestry.info("UI resource dir: %s", pathF);

            warPath = pathF.getCanonicalPath();
            // XXX Wherever this is you need a WEB-INF directory and a web.xml file
            warPath = Env.getConfigDir();
            Log.tapestry.info("WAR path: %s", warPath);

        } catch (IOException ioe) {
            String msg = StringUtil.strcat("Error getting war path for Tapestry service ", ioe);
            Log.tapestry.error(msg);
            return msg;
        }

        String ct = ContextPath.apply();
        // XXX Change this ct to a place where the tml files re
        Log.tapestry.info("Context path: %s", ct);
        // XXX FIX TAPESTRY
        //WebAppDeployer

        //String errmsg = ssrv.addWebApplication(warPath, ct);
        String errmsg = null;
        if (errmsg == null) {
            Log.tapestry.info("TapestryService initialized.");
        }

        return errmsg;
    }

    public String start(boolean dbInit) {
        return null;
    }

    public String deInit() {
        return null;
    }
}
