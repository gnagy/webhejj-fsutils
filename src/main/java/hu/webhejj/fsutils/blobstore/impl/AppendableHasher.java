package hu.webhejj.fsutils.blobstore.impl;

import hu.webhejj.commons.crypto.Hasher;
import hu.webhejj.commons.text.StringUtils;

import java.io.IOException;

public class AppendableHasher implements Appendable {

    private final Hasher hasher;
    private byte[] hash;

    public AppendableHasher(Hasher hasher) {
        hash = null;
        this.hasher = hasher;
    }

    public void reset() {
        hash = null;
        hasher.reset();
    }

    public byte[] getHash() {

        // digester.digest() resets the digester, so it cannot be called multiple times,
        // therefore we cache the hash
        if(hash == null) {
            hash = hasher.getDigester().digest();
            if(hasher.getSalt() != null) {
                hasher.getDigester().update(hasher.getSalt());
            }
        }
        return hash;
    }

    @Override
    public Appendable append(CharSequence csq) {
        hasher.getDigester().digest(StringUtils.decodeUtf8(csq.toString()));
        return this;
    }

    @Override
    public Appendable append(CharSequence csq, int start, int end) {
        hasher.getDigester().digest(StringUtils.decodeUtf8(csq.toString().substring(start, end)));
        return this;
    }

    @Override
    public Appendable append(char c) {
        hasher.getDigester().digest(StringUtils.decodeUtf8(String.valueOf(c)));
        return this;
    }
}
