package hu.webhejj.fsutils.blobstore.impl;

import hu.webhejj.fsutils.blobstore.Id;

public class StringId implements Id, Comparable<StringId> {

    private final String value;

    public StringId(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int hashCode() {
        return 31 * value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj instanceof  StringId) {
            return value.equals(((StringId) obj).value);
        }
        return false;
    }

    @Override
    public int compareTo(StringId o) {
        return value.compareTo(o.value);
    }
}
