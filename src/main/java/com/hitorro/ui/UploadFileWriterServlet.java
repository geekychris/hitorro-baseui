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

import com.hitorro.base.objects.Content;
import com.hitorro.base.objects.ContentType;
import com.hitorro.base.objects.VersionableObject;
import com.hitorro.basedms.cache.ContentTypeCache;
import com.hitorro.basedms.session.DMSSession;
import com.hitorro.util.basefile.fs.BaseFile;
import com.hitorro.util.basefile.fs.file.FileFile;
import com.hitorro.util.core.IntegerUtil;
import com.hitorro.util.core.Log;
import com.hitorro.util.core.string.StringUtil;
import com.hitorro.util.io.FileUtil;
import com.hitorro.util.io.IOUtil;
import com.hitorro.util.io.StoreException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Copyright (c) 2003 - present HiTorro All rights reserved. User: chris Date: Jul 23, 2005 Time: 10:13:07 AM
 * <p/>
 * Upload servlet for handling upload of content from the upload applet, uses http put.
 * <p/>
 * Note this is not a good example of using AppSession.
 */
public class UploadFileWriterServlet extends HttpServlet {
    /**
     *
     */
    private static final long SerialVersionUID = -6313282145650658932L;
    private static final long RandomAccessFileEnd = SerialVersionUID;


    protected void doPut(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws java.io.IOException {
        Cookie cookies[] = httpServletRequest.getCookies();
        String tapestryId = null;
        if (cookies != null && cookies.length > 0) {
            tapestryId = cookies[0].getValue();
        }

        try {

            //   get header fields
            String hdrAccount = httpServletRequest.getHeader("account");
            String hdrFilename = httpServletRequest.getHeader("filename");
            String hdrChunkAmount = httpServletRequest.getHeader("chunkamount");
            String hdrChunkId = httpServletRequest.getHeader("chunkid");


            if (StringUtil.nullOrEmptyOrBlankString(hdrFilename)) {
                return;
            }

            //   get the ptpost guid
            String parts[] = StringUtil.tokenizeFromSingleChar(hdrAccount, "=");
            if (parts == null || parts.length < 2) {
                return;
            }

            String guid = parts[1];

            //   get file chunk information
            int chunkCount = -1;
            int chunkIndex = -1;
            boolean hasFileChunks = false;
            if (!StringUtil.nullOrEmptyOrBlankString(hdrChunkAmount) && IntegerUtil.isNumber(hdrChunkAmount)) {
                chunkCount = Integer.parseInt(hdrChunkAmount);
                hasFileChunks = true;

                if (!StringUtil.nullOrEmptyOrBlankString(hdrChunkId) && IntegerUtil.isNumber(hdrChunkId)) {
                    chunkIndex = Integer.parseInt(hdrChunkId);
                }
            }

            //   get a session
            DMSSession session = null;// need to define session rendezvouz with UI
            if (session == null) {
                return;
            }

            try {
                VersionableObject so = (VersionableObject) session.getObjectFromGuid(guid);
                ContentTypeCache cache = ContentTypeCache.getCache();
                InputStream is = httpServletRequest.getInputStream();

                if (!hasFileChunks) {
                    ContentType ct = cache.getTypeFromFileWithDefault(hdrFilename);
                    so.setContent(hdrFilename, ct, is, null);
                } else {
                    String filename = FileUtil.getFileNameSansExtension(hdrFilename);
                    String filenameExtension = FileUtil.getFileExtension(hdrFilename);
                    int filenameIndex = Integer.parseInt(filenameExtension);
                    Content content = so.getContentByFileName(filename, true);
                    ContentType ct = cache.getTypeFromFileWithDefault(filename);
                    long seekPosition = 0;

                    if (content != null && chunkIndex > 1) {
                        seekPosition = RandomAccessFileEnd;
                    }

                    if (content == null || filenameIndex != chunkIndex) {
                        try {
                            content = so.createZeroLengthContent(filename, ct, null);
                        } catch (StoreException e) {
                            throw new IOException(e.getMessage());
                        }
                    }

                    BaseFile contentFile = content.getContentFile();
                    if (contentFile != null) {
                        if (!contentFile.isLocal()) {
                            Log.filesystem.error("Attempting todo file upload to a non local file system");
                            return;
                        }
                        File cf = ((FileFile) contentFile).getJavaFile();
                        RandomAccessFile raf = new RandomAccessFile(cf, "rw");
                        if (seekPosition == RandomAccessFileEnd) {
                            seekPosition = raf.length();
                        }

                        raf.seek(seekPosition);
                        FileOutputStream fos = new FileOutputStream(raf.getFD());
                        IOUtil.copyStream(is, fos);
                        is.close();
                        fos.close();
                    }
                }

                session.commit();
            } catch (StoreException se) {
                Log.rpc.error("Unable to create content %s %e", se, se);
            }
        } finally {
            //appSession.detach(step);
        }
    }

}
