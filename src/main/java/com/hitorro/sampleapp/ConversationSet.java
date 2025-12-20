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

import com.hitorro.base.objects.PersistedSerializedObject;
import com.hitorro.basedms.session.DMSSession;
import com.hitorro.util.io.StoreException;
import com.hitorro.util.typesystem.HTObjectInputStream;
import com.hitorro.util.typesystem.HTObjectOutputStream;
import com.hitorro.util.typesystem.HTSerializable;
import com.hitorro.util.typesystem.annotation.TypeClassMetaInfo;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

/**
 * A setPhrases of conversation results.
 */
@TypeClassMetaInfo(shortTypeName = "ConversationSet",
        isView = false,
        isPersisted = false,
        schemaVersion = 1)
public class ConversationSet implements HTSerializable {
    // todo chris - this can be removed when we persist properly
    // identifier for collection in PersistedSerializedObject table

    private static final int SerializationVersion = 1;

    private LinkAnalysisMemeSet _results;
    private Date _calcDate;
    private String _name;

    public ConversationSet() {
        _name = "?";
    }

    public static String[] getConversationNames(DMSSession session) {
        Iterator itr = session.getIteratorFromQueryArgs("select name from PersistedSerializedObject where collectionId= :a",
                PersistedSerializedObject.CollectionID_MemeConversation);
        String[] temp = new String[100];
        int indx = 0;
        while (itr.hasNext()) {
            temp[indx++] = (String) itr.next();
        }
        String[] results = new String[indx];
        System.arraycopy(temp, 0, results, 0, indx);

        return results;
    }

    public static ConversationSet getConversations(DMSSession session, String setName) {
        //return s_savedConversations.get(setName);
        Object obj = session.getObject(PersistedSerializedObject.class, "where collectionId= :a and name= :b",
                PersistedSerializedObject.CollectionID_MemeConversation, setName);
        PersistedSerializedObject wrapper = (PersistedSerializedObject) obj;
        HTSerializable ser;
        try {
            ser = wrapper.getSerializableObject(session);
        } catch (Exception exc) {
            com.hitorro.basedms.Log.objectHandling.error("Can't get conversationset: ", exc);
            ser = null;
        }
        return (ser instanceof ConversationSet) ? (ConversationSet) ser : null;
    }

    public String getName() {
        return _name;
    }

    public void setName(String val) {
        _name = val;
    }

    public LinkAnalysisMemeSet getResults() {
        return _results;
    }

    public void setResults(LinkAnalysisMemeSet res) {
        _results = res;
    }

    public Date getCalcDate() {
        return _calcDate;
    }

    public void setCalcDate(Date val) {
        _calcDate = val;
    }

    // ----------------- HTSerializable

    public void serialize(HTObjectOutputStream os) throws IOException, StoreException {
        os.writeInt(getSerializationVersion());
        os.writeString(_name);
        os.writeDate(_calcDate);
        os.writeVersionedObject(_results);
    }

    public void deserialize(HTObjectInputStream os) throws IOException, ClassNotFoundException, StoreException {
        int version = os.readInt();
        switch (version) {
            case 1:
                _name = os.readString();
                _calcDate = os.readDate();
                _results = (LinkAnalysisMemeSet) os.readVersionedObject();
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
