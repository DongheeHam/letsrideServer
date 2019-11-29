package clabs.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

public class FileUtils {

	public static String getMD5(String path) throws IOException {
		return getMD5(new File(path));
	}
	
    public static String getMD5(File file) throws IOException {
    	
        FileInputStream fileInputStream = new FileInputStream(file);
        String md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fileInputStream));
        fileInputStream.close();

        return md5;
    }
}
