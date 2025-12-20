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


import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides handling for servlet parameters, both for GET and POST
 */
public class ServletParameters {
    private Map<String, String[]> m_values;

    /**
     * Construct the parameter data, from the request
     *
     * @param req The HttpServletRequest being handled
     */
    public ServletParameters(HttpServletRequest req) {
        m_values = new HashMap<String, String[]>();
        Enumeration names = req.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            // normal parameter
            String[] values = req.getParameterValues(name);
            //System.out.println("parameter " + name + " has value " + values[0]);
            m_values.put(name, values);
        }
    }

    /**
     * Get the number of values we have for a parameter. Non-existent parameters have 0 values.  Some parameters will
     * have repeated values, in which case the result is greater than 1.
     *
     * @param name The name of the parameter we're counting
     * @return the number of values available
     */
    public int getValueCount(String name) {
        String[] argarray = m_values.get(name);
        return (argarray == null) ? 0 : argarray.length;
    }

    /**
     * Get a value, as a string. This version of the routine will return the first value for the parameter name, if
     * there are multiple values.  If the value is missing, it will return an empty string.
     *
     * @param name The name of the parameter we're fetching
     * @return The parameter value, or an empty string
     */
    public String getValueString(String name) {
        return getValueString(name, 0, "");
    }

    /**
     * Get a value, as a string. This version of the routine will return the first value for the parameter name, if
     * there are multiple values.
     *
     * @param name         The name of the parameter we're fetching
     * @param defaultValue The value that is returned if the parameter for this index doesn't exist
     * @return The parameter value, or defaultValue if it doesn't exist
     */
    public String getValueString(String name, String defaultValue) {
        return getValueString(name, 0, defaultValue);
    }

    /**
     * Get a value, as a string.
     *
     * @param name         The name of the parameter we're fetching
     * @param index        The index of the parameter value we want
     * @param defaultValue The value that is returned if the parameter for this index doesn't exist
     * @return The parameter value, or defaultValue if it doesn't exist
     */
    public String getValueString(String name, int index, String defaultValue) {
        String[] argarray = m_values.get(name);
        if (argarray == null || index < 0 || index >= argarray.length) {
            return defaultValue;
        }

        return argarray[index];
    }

    /**
     * Get a value, as an integer.
     *
     * @param name         The name of the parameter we're fetching
     * @param index        The index of the parameter value we want
     * @param defaultValue The value that is returned if the parameter for this index doesn't exist
     * @return The parameter value, or defaultValue if it doesn't exist or can't be parsed
     */
    public int getValueInt(String name, int index, int defaultValue) {
        int retval;
        try {
            String sval = getValueString(name, index, null);
            if (sval == null) {
                return defaultValue;
            }
            retval = Integer.parseInt(sval);
        } catch (Exception exc) {
            retval = defaultValue;
        }

        return retval;
    }

}


