package exercise.fontys.nl.exercisecontrolbackend;

import junit.framework.Assert;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import java.util.HashSet;
import java.util.Random;

public class FileStorageTestCase extends AndroidTestCase {

    private FileStorage fileStorage;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fileStorage = new FileStorage(getContext());
    }

    @SmallTest
    public void testStringStorage() throws Exception {
        String str = "Hello world!";
        fileStorage.<String>save("hello", str);
    }

    @SmallTest
    public void testStringEquals() throws Exception {
        String str = "Hello world!";
        fileStorage.<String>save("hello2", str);
        String str2 = fileStorage.<String>load("hello2");
        Assert.assertEquals(str, str2);
    }

    @SmallTest
    public void testHashSetEquals() throws Exception {
        HashSet<Long> set = new HashSet<Long>();
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 1000; i++) {
            set.add(random.nextLong());
        }

        fileStorage.<HashSet<Long>>save("longset", set);
        HashSet<Long> set2 = fileStorage.<HashSet<Long>>load("longset");
        Assert.assertEquals(set, set2);
    }
}
