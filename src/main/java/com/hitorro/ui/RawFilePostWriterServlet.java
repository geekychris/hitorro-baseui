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
package com.hitorro.ui;

import com.hitorro.util.io.FileUtil;
import com.hitorro.util.io.IOUtil;

import jakarta.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by IntelliJ IDEA. User: chris Date: 2/21/12 Time: 7:04 PM To change this template use File | Settings | File
 * Templates.
 */
public class RawFilePostWriterServlet extends HttpServlet {
    /**
     *
     */
    private static final long SerialVersionUID = -6313282145650658932L;
    private static final long RandomAccessFileEnd = SerialVersionUID;


    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws java.io.IOException {
        File f = new File("/tmp/foo.swf");
        OutputStream os = FileUtil.getBufferedFileOutputStream(f);
        InputStream is = httpServletRequest.getInputStream();
        IOUtil.copyStream(is, os);
    }

}
