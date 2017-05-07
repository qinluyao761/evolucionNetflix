// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.model.leafs;

import java.util.Iterator;
import com.google.gson.JsonObject;
import java.util.Map;
import com.netflix.mediaclient.Log;
import com.google.gson.JsonElement;
import com.netflix.mediaclient.servicemgr.model.JsonPopulator;

public class SearchPerson implements JsonPopulator, com.netflix.mediaclient.servicemgr.model.search.SearchPerson
{
    private static final String TAG = "Person";
    private String id;
    private String imgUrl;
    private String title;
    
    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public String getImgUrl() {
        return this.imgUrl;
    }
    
    @Override
    public String getName() {
        return this.title;
    }
    
    @Override
    public void populate(final JsonElement jsonElement) {
        final JsonObject asJsonObject = jsonElement.getAsJsonObject();
        if (Log.isLoggable("Person", 2)) {
            Log.v("Person", "Populating with: " + asJsonObject);
        }
        for (final Map.Entry<String, JsonElement> entry : asJsonObject.entrySet()) {
            final JsonElement jsonElement2 = entry.getValue();
            final String s = entry.getKey();
            int n = 0;
            Label_0134: {
                switch (s.hashCode()) {
                    case 3355: {
                        if (s.equals("id")) {
                            n = 0;
                            break Label_0134;
                        }
                        break;
                    }
                    case 110371416: {
                        if (s.equals("title")) {
                            n = 1;
                            break Label_0134;
                        }
                        break;
                    }
                    case -1185088852: {
                        if (s.equals("imgUrl")) {
                            n = 2;
                            break Label_0134;
                        }
                        break;
                    }
                }
                n = -1;
            }
            switch (n) {
                default: {
                    continue;
                }
                case 0: {
                    this.id = jsonElement2.getAsString();
                    continue;
                }
                case 1: {
                    this.title = jsonElement2.getAsString();
                    continue;
                }
                case 2: {
                    this.imgUrl = jsonElement2.getAsString();
                    continue;
                }
            }
        }
    }
    
    @Override
    public String toString() {
        return "SearchPerson [id=" + this.id + ", title=" + this.title + ", imgUrl=" + this.imgUrl + "]";
    }
}