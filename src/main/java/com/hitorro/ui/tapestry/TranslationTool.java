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

import com.hitorro.util.core.Console;
import com.hitorro.util.core.Env;
import com.hitorro.util.core.Log;
import com.hitorro.util.core.params.HTProperties;
import com.hitorro.util.core.string.StringBuilderUtil;
import com.hitorro.util.core.string.StringUtil;
import com.hitorro.util.io.FileUtil;
import com.hitorro.util.io.filefilters.FilenameExtensionFilter;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Tools for helping with localization and internationalization of Tapestry pages.
 */
public class TranslationTool {
    private File _sourceDir;

    private TranslationTool() {
        _sourceDir = new File(Env.getBin(), "src");
    }

    public static void main(String[] args) {
        try {
            TranslationTool tool = new TranslationTool();
            tool.run();
        } catch (Exception exc) {
            Console.println("Exception in translation: %s", exc);
            exc.printStackTrace();
        }
    }

    private void run()
            throws IOException {
        File propertiesRoot = new File(_sourceDir, "resources");
        File templateRoot = new File(_sourceDir, "src/ht");

        // read the global application properties
        File appTranslationsF = new File(propertiesRoot, "app.properties");
        if (!appTranslationsF.exists()) {
            Console.println("no app.properties file in ", appTranslationsF.getCanonicalPath());
            return;
        }
        SortedMap<String, String> appTranslations = readTranslationFile(appTranslationsF);

        // process all the template files and their matching properties files
        // Todo - put in a check for properties files with no templates
        FilenameFilter templateFilter = new FilenameExtensionFilter("tml", true);
        List<File> templateFiles = FileUtil.findFilteredFiles(templateFilter, templateRoot, true);

        for (File tf : templateFiles) {
            SortedMap<String, String> tMessages = readTemplateFile(tf);
            File propertiesF = propertyFileFromTemplate(templateRoot, propertiesRoot, tf);
            SortedMap<String, String> tTranslations = readTranslationFile(propertiesF);
            resolveTranslations(tMessages, tTranslations, appTranslations, propertiesF);
        }

    }

    private File propertyFileFromTemplate(File templateRoot, File propertiesRoot, File template)
            throws IOException {
        // figure out the directory of the properties file
        File pParent;
        String templatePath = template.getCanonicalPath();
        if (templatePath.indexOf("WEB-INF") > 0) {
            // component templates live at the top of the tree
            pParent = propertiesRoot;
        } else {
            // other files are in a parallel tree
            File templateAtProperties = reRootFile(templateRoot, template, propertiesRoot);
            pParent = templateAtProperties.getParentFile();
        }
        String templateName = template.getName();
        String propertyFName = StringUtil.strcat(templateName.substring(0, templateName.length() - 5), ".properties");
        return new File(pParent, propertyFName);
    }

    private File reRootFile(File rootDir, File theFile, File newRootDir) {
        try {
            String rootDirPath = rootDir.getCanonicalPath();
            String filePath = theFile.getCanonicalPath();
            String subPath = filePath.substring(rootDirPath.length());
            return new File(newRootDir, subPath);
        } catch (IOException ioe) {
            return null;
        }
    }

    private void resolveTranslations(SortedMap<String, String> messages,
                                     SortedMap<String, String> tTranslations,
                                     SortedMap<String, String> aTranslations,
                                     File propertyFile)
            throws IOException {
        if (messages == null || messages.size() == 0) {
            // no messages for translation in this template - nothing to do
            return;
        }

        // for every message, make sure that there is a translation
        // if not, write a default translation to the property file
        PrintWriter pWriter = null;
        for (String msg : messages.keySet()) {
            // try to find the message
            String translation = tTranslations.get(msg);
            if (translation == null) {
                translation = aTranslations.get(msg);
                if (translation == null) {
                    // no translation
                    Console.println("Creating translation for %s  in %s", msg, messages.get(msg));
                    if (pWriter == null) {
                        // open the file
                        if (!propertyFile.exists()) {
                            // need to make sure the directory exists
                            File dirF = propertyFile.getParentFile();
                            dirF.mkdirs();
                        }
                        FileOutputStream fout = new FileOutputStream(propertyFile, true);
                        pWriter = new PrintWriter(fout);
                    }
                    pWriter.println(StringUtil.strcat(msg, " = ", defaultTranslation(msg)));
                }
            }

        }

        if (pWriter != null) {
            pWriter.close();
            propertyFile.setLastModified(System.currentTimeMillis());
        }


    }

    /**
     * Create a apply of resources referenced by a template file. This will create a apply of
     * resourceKey/defaultTranslations.  The default translations are a simple transformation of the resourcecache key.
     *
     * @param templateFile the template file to check
     * @return a apply of the referenced resourcecache keys
     * @throws IOException on io problem
     */
    private SortedMap<String, String> readTemplateFile(File templateFile)
            throws IOException {
        // read the whole file into a StringBuilder
        StringBuilder fileContent = new StringBuilder();
        StringBuilderUtil.readFileIntoBuilder(fileContent, templateFile);

        String templateSource = templateFile.getCanonicalPath();

        // create a listFiles of keys and populate with the referenced keys
        SortedMap<String, String> keys = new TreeMap<String, String>();
        // need to find:
        // span with key attribute
        // an ognl expression with message
        // an ognl expression with messages..format - pull out the format
        findKeySpan(fileContent, keys, templateSource);
        findMessage(fileContent, keys, templateSource);
        findMessagesFormat(fileContent, keys, templateSource);
        // findMessagesMessage  -  need this if we ever use ognl:messages.getMessage

        return keys;
    }

    private void findKeySpan(StringBuilder content, Map<String, String> keys, String source) {
        String startTarget = "span key=\"";
        String endTarget = "\"";
        findHelper(content, keys, startTarget, endTarget, source);
    }

    private void findMessage(StringBuilder content, Map<String, String> keys, String source) {
        String startTarget = "\"message:";
        String endTarget = "\"";
        findHelper(content, keys, startTarget, endTarget, source);
    }

    private void findMessagesFormat(StringBuilder content, Map<String, String> keys, String source) {
        String startTarget = "ognl:messages.format('";
        String endTarget = "'";
        findHelper(content, keys, startTarget, endTarget, source);
    }

    private void findHelper(StringBuilder content, Map<String, String> keys, String startTarget,
                            String endTarget, String source) {
        int targetLen = startTarget.length();
        int curIndex = 0;
        while (curIndex >= 0) {
            curIndex = content.indexOf(startTarget, curIndex);
            if (curIndex > 0) {
                int endIndex = content.indexOf(endTarget, curIndex + targetLen);
                if (endIndex > 0) {
                    String key = content.substring(curIndex + targetLen, endIndex);
                    if (null == keys.get(key)) {
                        // we don't already have this, put it in the source of the message
                        keys.put(key, source);
                    }
                    curIndex = endIndex + 1;
                } else {
                    // if we didn't find the end target, we're done
                    curIndex = -1;
                }
            }
        }
    }

    private String defaultTranslation(String key) {
        return StringUtil.strcat("?!", key, "!?");
    }

    /**
     * Create a apply of translations from a translation file. This will create a apply of resourcekey/value pairs.
     *
     * @param translations The file to read the translations from.
     * @return the apply of translations
     */
    private SortedMap<String, String> readTranslationFile(File translations) {
        // we expect to see translastions in a Properties format
        // use the HiTorro utility to read it
        TreeMap<String, String> result = new TreeMap<String, String>();
        if (!translations.exists()) {
            return result;
        }

        try {
            FileInputStream in = new FileInputStream(translations);
            HTProperties.load(in, result);
        } catch (IOException ioe) {
            Log.tapestry.error(ioe, "Error loading translation file");
            return null;
        }

        return result;
    }

}
