package hu.webhejj.fsutils.git;

import hu.webhejj.commons.crypto.Hasher;
import hu.webhejj.commons.text.StringUtils;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class GitBlobOutputStream extends FilterOutputStream {

    public static final String TYPE_BLOB = "blob";
    public static final String TYPE_TREE = "tree";

    private final String type;
    private final long length;
    private final MessageDigest messageDigest;
    private String hash;
    private long actualLength;

    public GitBlobOutputStream(OutputStream delegate, String type, long length) throws IOException {
        super(new DeflaterOutputStream(delegate, new Deflater(), 512));
        this.type = type;
        this.length = length;
        try {
            this.messageDigest = MessageDigest.getInstance(Hasher.SHA_1);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Digest algorithm \'" + Hasher.SHA_1 + "\' not found", e);
        }
        writeHeader();
        this.actualLength = 0;
    }

    @Override
    public void write(int b) throws IOException {
        super.write(b);
        messageDigest.update((byte) b);
        actualLength++;
    }

    private void writeHeader() throws IOException {
        write(type.getBytes("ASCII"));
        write(' ');
        write(Long.toString(length).getBytes("ASCII"));
        write(0);
    }

    @Override
    public void close() throws IOException {
        if(actualLength != length) {
            throw new IOException(String.format("Error while closing, expected length to be %d but was %d.", length, actualLength));
        }
        super.close();
        hash = StringUtils.encodeHex(messageDigest.digest());
    }

    public String getType() {
        return type;
    }

    public long getLength() {
        return length;
    }

    public String getHash() {
        return hash;
    }
}
