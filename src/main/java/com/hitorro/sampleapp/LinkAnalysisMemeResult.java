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

/**
 *
 */
@TypeClassMetaInfo(shortTypeName = "LinkAnalysisMemeResult",
        isView = false,
        isPersisted = false,
        schemaVersion = 1)
public class LinkAnalysisMemeResult implements HTSerializable {
    private static final int SerializationVersion = 2;
    private String _link;
    private int _totalWeight;
    private String[] _postGuids;
    private String[] _feedGuids;
    private String _title;
    private String _postText;

    public LinkAnalysisMemeResult() {
        this("", 0);
    }

    public LinkAnalysisMemeResult(String lk, int tw) {
        _link = lk;
        _totalWeight = tw;
        _postGuids = null;
        _title = null;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        _title = title;
    }

    public String getCommonLink() {
        return _link;
    }

    public int getTotalWeight() {
        return _totalWeight;
    }

    public String[] getPostGuids() {
        return _postGuids;
    }

    public void setPostGuids(String[] guids) {
        _postGuids = guids;
    }

    public String[] getFeedGuids() {
        return _feedGuids;
    }

    public void setFeedGuids(String[] guids) {
        _feedGuids = guids;
    }

    public String getPostText() {
        return _postText;
    }

    public void setPostText(String val) {
        _postText = val;
    }

    public int getFirstFeedRef(String feedGuid) {
        int ii = 0;
        for (String fg : _feedGuids) {
            if (feedGuid.equals(fg)) {
                return ii;
            }
            ii++;
        }

        return -1;
    }

    public void serialize(HTObjectOutputStream os) throws IOException, StoreException {
        os.writeInt(getSerializationVersion());
        os.writeString(_link);
        os.writeInt(_totalWeight);
        os.writeString(_title);

        os.writeArrayOfString(_feedGuids);
        os.writeArrayOfString(_postGuids);
        os.writeString(_postText);
    }

    public void deserialize(HTObjectInputStream os) throws IOException, ClassNotFoundException {
        int version = os.readInt();
        switch (version) {
            case 1:
                _link = os.readString();
                _totalWeight = os.readInt();
                _title = os.readString();

                _feedGuids = os.readArrayOfStrings();
                _postGuids = os.readArrayOfStrings();
                _postText = "this is fake text from the common post";
                break;
            case 2:
                _link = os.readString();
                _totalWeight = os.readInt();
                _title = os.readString();

                _feedGuids = os.readArrayOfStrings();
                _postGuids = os.readArrayOfStrings();
                _postText = os.readString();
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
