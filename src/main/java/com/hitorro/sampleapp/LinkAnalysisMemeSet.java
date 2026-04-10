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

import com.hitorro.util.io.StoreException;
import com.hitorro.util.typesystem.HTObjectInputStream;
import com.hitorro.util.typesystem.HTObjectOutputStream;
import com.hitorro.util.typesystem.HTSerializable;
import com.hitorro.util.typesystem.annotation.TypeClassMetaInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@TypeClassMetaInfo(shortTypeName = "LinkAnalysisMemeSet",
        isView = false,
        isPersisted = false,
        schemaVersion = 1)
public class LinkAnalysisMemeSet implements HTSerializable {
    private static final int SerializationVersion = 1;

    private String[] _feedGuids;
    //private int _nDays = 3;
    //private int _threshhold = 14;
    //private int _otherWeight = 2;
    //private int _seedWeight = 5;
    //private Date _calcDate;
    private List<LinkAnalysisMemeResult> _results;

    public LinkAnalysisMemeSet() {

    }

    public String[] getSeedFeedGuids() {
        return _feedGuids;
    }

    public void setSeedFeedGuids(String[] fg) {
        _feedGuids = fg;
    }

    public List<LinkAnalysisMemeResult> getResults() {
        return _results;
    }

    public void setResults(List<LinkAnalysisMemeResult> results) {
        _results = results;
    }

    public LinkAnalysisMemeResult getResultByTopicUrl(String url) {
        if (url == null) {
            return null;
        }
        for (LinkAnalysisMemeResult result : _results) {
            if (url.equals(result.getCommonLink())) {
                return result;
            }
        }

        return null;
    }

    /*
    public Date getCalcDate ()
    {
        return _calcDate;
    }

    public void setCalcDate (Date cd)
    {
        _calcDate = cd;
    }

    public int getNDays ()
    {
        return _nDays;
    }

    public void setNDays (int nDays)
    {
        _nDays = nDays;
    }

    public int getThreshhold ()
    {
        return _threshhold;
    }

    public void setThreshhold (int val)
    {
        _threshhold = val;
    }

    public int getOtherWeight ()
    {
        return _otherWeight;
    }

    public void setOtherWeight (int val)
    {
        _otherWeight = val;
    }

    public int getSeedWeight ()
    {
        return _seedWeight;
    }

    public void setSeedWeight (int val)
    {
        _seedWeight = val;
    }

    */

    // ----------------- HTSerializable

    public void serialize(HTObjectOutputStream os) throws IOException, StoreException {
        os.writeInt(getSerializationVersion());
        os.writeArrayOfString(_feedGuids);
        os.writeListOfHTSerializable(_results);

    }

    public void deserialize(HTObjectInputStream os) throws IOException, ClassNotFoundException, StoreException {
        int version = os.readInt();
        switch (version) {
            case 1:
                _feedGuids = os.readArrayOfStrings();

                _results = new ArrayList<LinkAnalysisMemeResult>();
                os.readListOfHTSerializable(_results);
        }
    }

    public int getSerializationVersion() {
        return SerializationVersion;
    }

    public boolean isPersisted() {
        return false;
    }

    public boolean hasGuid() {
        return false;
    }

    public boolean hasSoftGuid() {
        return false;
    }

}
