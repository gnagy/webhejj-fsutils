package hu.webhejj.fsutils.git;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class GitBlobOutputStreamTest {

    @Test
    public void testGitBlobOutputStream() throws IOException {

        String contents = "test";
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        GitBlobOutputStream out = new GitBlobOutputStream(bos, GitBlobOutputStream.TYPE_BLOB, contents.length());
        out.write(contents.getBytes("ASCII"));
        out.close();
        Assert.assertEquals("Hash", "30d74d258442c7c65512eafab474568dd706c430", out.getHash());
    }
}
