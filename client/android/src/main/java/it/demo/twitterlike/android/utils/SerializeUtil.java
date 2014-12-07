package it.demo.twitterlike.android.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil {

	public static void writeObject(Object obj, File file) throws IOException {
		FileOutputStream fout = new FileOutputStream(file);
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(fout);
			oos.writeObject(obj);
		} finally {
			if (oos != null) {
				oos.close();
			}
		}
	}

	public static <T> T readObject(File file) throws Exception {
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(file);

			return readObject(fin);
		} finally {
			if (fin != null) {
				fin.close();
			}
		}

	}

	@SuppressWarnings("unchecked")
	public static <T> T readObject(InputStream file) throws Exception {
		T result = null;
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(file);
			result = (T) ois.readObject();

		} finally {
			if (ois != null) {
				ois.close();
			}
		}
		return result;
	}
}
