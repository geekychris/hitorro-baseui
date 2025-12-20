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

//import org.apache.tapestry.IPage;

/**
 * Utility methods for working with the tapestry layer.
 *
 * @author chris
 */
public class TapestryUtil {
    /**
     * Get a page, based on its class.
     *
     * @param currentPage The page we're currently on
     * @param pc          The requested page class.
     *
     * @return An instance of the page, or null if it can't be found
     */
    /* public static IPage getPage (IPage currentPage, Class pc)
    {
        return getPage(currentPage, pageName(pc));
    }*/

    /**
     * Get a page, based on its page name.
     *
     * @param currentPage The page we're currently on
     * @param pageName    The name of the page to fetch
     * @return An instance of the page, or null if it can't be found
     */
    /* public static IPage getPage (IPage currentPage, String pageName)
  {
      return currentPage.getRequestCycle().getPage(pageName);
  }  */

    private static final String PagePackageRoot = "ht.";

    /**
     * Get the page's name, based on the page's class.
     *
     * @param pc The page class.
     * @return The name of the page, suitable for handing to getPage
     */
    public static String pageName(Class pc) {
        String className = pc.getCanonicalName();
        if (className.startsWith(PagePackageRoot)) {
            // usual class name.  Since we're basing our page names under "ht" (see app.application)
            // we don't want that part of the class name
            className = className.substring(PagePackageRoot.length());
        }
        return className.replace('.', '/');
    }

    public static StringBuilder getRelativeUrlToRoot(Class pageClass) {
        // if the page class is something like ht.aa.bb.MyPage
        // then the relative url is ../../

        String baseName = getHiTorroClass(pageClass).getCanonicalName();
        if (baseName == null) {
            // couldn't get a good HiTorro class
            return new StringBuilder(".");
        }

        // we're going to expect everything to start with "ht."
        // then we'll count subpackages past that on base to create ..s
        // and then construct the path out of the rest of target
        int basePackageLen = PagePackageRoot.length();

        // count periods in baseName past ht
        int indx = basePackageLen + 1;
        int count = 0;
        while (indx > 0) {
            indx = baseName.indexOf('.', indx);
            if (indx > 0) {
                count++;
                indx++;  // move past the found period
            }
        }

        StringBuilder result = new StringBuilder();
        for (int ii = 0; ii < count; ii++) {
            result.append("../");
        }

        return result;
    }

    /**
     * Get the url for target page relative to a base page.
     *
     * @param baseClass   the class of the base page
     * @param targetClass the class of the target page
     * @return the relative url of the target page
     */
    public static String getRelativeUrl(Class baseClass, Class targetClass) {
        String targetName = getHiTorroClass(targetClass).getCanonicalName();
        int basePackageLen = PagePackageRoot.length();

        // baseName is something like: ht.sampleapp.FrontPage
        // targetName is something like: ht.ui.tapestry.Zebras
        // in that case we want to return ../ui/tapestry/Zebras

        StringBuilder scratch = getRelativeUrlToRoot(baseClass);
        int pindx = scratch.length();
        scratch.append(targetName.substring(basePackageLen));

        // convertToPdf . to / in the package name
        while (pindx > 0) {
            pindx = scratch.indexOf(".", pindx);
            if (pindx > 0) {
                scratch.setCharAt(pindx, '/');
            }
        }

        return scratch.toString();
    }

    public static Class getHiTorroClass(Class cl) {
        while (cl != null) {
            String name = cl.getCanonicalName();
            if (name.startsWith(PagePackageRoot)) {
                return cl;
            }
            cl = cl.getSuperclass();
        }

        // we popped all the way up to Object - no HiTorroness anywhere
        return null;
    }
}
