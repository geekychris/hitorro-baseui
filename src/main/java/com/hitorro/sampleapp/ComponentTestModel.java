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
package com.hitorro.sampleapp;

import com.hitorro.util.core.Console;
import com.hitorro.util.core.ListValue;
import com.hitorro.util.core.ListValue.ListValueSource;
import com.hitorro.util.typesystem.annotation.TypeClassMetaInfo;
import com.hitorro.util.typesystem.annotation.UiProperties;
import com.hitorro.util.typesystem.annotation.UiTypeProperties;
import com.hitorro.util.typesystem.annotation.ViewClassReference;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Class holding data to be used for testing ui components
 */
@TypeClassMetaInfo(shortTypeName = "ComponentTestModel",
        isView = false,
        isPersisted = false,
        schemaVersion = 1)
@UiTypeProperties(name = "ComponentTest", views = {@ViewClassReference(name = ViewClassReference.EditView, viewClass = ComponentTestModel.ComponentTestModelEditView.class)})
public class ComponentTestModel implements ListValueSource {
    public static final int SerializationVersion = 0;
    private static final int ZebraValue = -7070;
    private static final int MongooseValue = 8765;
    public static ListValue[] s_iListChoices = {
            new ListValue("zebra", ZebraValue),
            new ListValue("mongoose", MongooseValue)
    };
    private int _ia;
    private String _sShort;
    private String _sLong;
    private boolean _bool;
    private int _iList;
    private Calendar _cal;
    private String _fromFile;
    // values for testing formatting
    private Double _fDouble;
    private Date _fDate;

    public ComponentTestModel() {
        _ia = 99;
        _sShort = "hoho";
        _sLong = "when in disgrace with fortune and men's eyes I all alone beweep my outcast state troubling deaf heaven with my bootless cries";
        _bool = true;
        _iList = MongooseValue;
        _cal = new GregorianCalendar();
        _fDouble = -37.088;
        _fDate = new Date();
    }

    @UiProperties(displayName = "Integer", displayType = UiProperties.IntFieldDisplay, order = 30)
    public int getIa() {
        return _ia;
    }

    public void setIa(int ia) {
        _ia = ia;
    }

    @UiProperties(displayName = "Short String", displayType = UiProperties.TextFieldDisplay, order = 10)
    public String getSShort() {
        return _sShort;
    }

    public void setSShort(String sshort) {
        _sShort = sshort;
    }

    @UiProperties(displayName = "Long String", displayType = UiProperties.TextAreaDisplay, order = 20)
    public String getSLong() {
        return _sLong;
    }

    public void setSLong(String sshort) {
        _sLong = sshort;
    }

    @UiProperties(displayName = "Boolean", displayType = UiProperties.BooleanDisplay, order = 40)
    public boolean isBool() {
        return _bool;
    }

    public void setBool(boolean bl) {
        _bool = bl;
    }

    @UiProperties(displayName = "List", displayType = UiProperties.SelectListDisplay, order = 50)
    public int getIList() {
        return _iList;
    }

    public void setIList(int il) {
        _iList = il;
    }

    @UiProperties(displayName = "Calendar", displayType = UiProperties.CalendarDisplay, order = 60)
    public Calendar getCal() {
        return _cal;
    }

    public void setCal(Calendar cc) {
        _cal = cc;
    }

    @UiProperties(displayName = "File upload", displayType = UiProperties.FileUploadDisplay, order = 70)
    public String getFromFile() {
        return "?";
    }

    public void setFromFile(InputStream istream) {
        try {
            InputStreamReader reader = new InputStreamReader(istream);
            char[] buff = new char[4000];
            reader.read(buff, 0, 4000);
            _fromFile = new String(buff);
            Console.println("----- read: " + _fromFile);
            reader.close();
        } catch (IOException ioe) {
            _fromFile = ioe.toString();
        }
    }

    @UiProperties(displayName = "Formatted double", displayType = UiProperties.ReadOnlyDisplay, format = "#,##0.00", order = 80)
    public Double getFDouble() {
        return _fDouble;
    }

    @UiProperties(displayName = "Formatted date", displayType = UiProperties.ReadOnlyDisplay, format = "MM/dd/yy", order = 90)
    public Date getFDate() {
        return _fDate;
    }

    public ListValue[] getValues(Object obj, String fieldName, String tag) {
        if ("iList".equals(fieldName)) {
            return s_iListChoices;
        } else {
            return null;
        }
    }

    /**
     * View class enumerating which fields to show when editing.
     */
    @TypeClassMetaInfo(shortTypeName = "ComponentTestModelEditView",
            isView = true,
            isPersisted = false,
            schemaVersion = ComponentTestModel.SerializationVersion)
    public abstract static class ComponentTestModelEditView {
        public abstract int getIa();

        public abstract String getSShort();

        public abstract String getSLong();

        public abstract boolean isBool();

        public abstract int getIList();

        public abstract String getFromFile();

        public abstract Double getFDouble();

        public abstract Date getFDate();
    }
}
