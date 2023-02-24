package utils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {

	public static void downloadFileFromUrl(String urlStr, String token, String path) {
		URL url = null;
		try {
			url = new URL(HttpUtils.buildFileUrl(urlStr, token));
			BufferedInputStream bis = new BufferedInputStream(url.openStream());
			FileOutputStream fis = new FileOutputStream(path);
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = bis.read(buffer, 0, 1024)) != -1) {
				fis.write(buffer, 0, count);
			}
			fis.close();
			bis.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean extractArchiveFile(String zipPath, String desPath) {
		File destDir = new File(desPath);
		byte[] buffer = new byte[1024];
		try {
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath));
			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {
				File newFile = newFile(destDir, zipEntry);
				System.out.println("Extracting " + zipEntry.getName() + "...");
				if (zipEntry.isDirectory()) {
					if (!newFile.isDirectory() && !newFile.mkdirs()) {
						throw new IOException("Failed to create directory " + newFile);
					}
				} else {
					File parent = newFile.getParentFile();
					if (!parent.isDirectory() && !parent.mkdirs()) {
						throw new IOException("Failed to create directory " + parent);
					}

					// write file content
					FileOutputStream fos = new FileOutputStream(newFile);
					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					fos.close();
				}
				zipEntry = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			return false;

		}

		return true;
	}

	public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destinationDir, zipEntry.getName());

		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();

		if (!destFilePath.startsWith(destDirPath + File.separator)) {
			throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
		}
		return destFile;
	}

	public static void main(String[] args) {
		String url = "https://moodle-112112-0.cloudclusters.net/webservice/pluginfile.php/36/assignsubmission_file/submission_files/2/jar_files%20%282%29.zip";
		String token  = "d62dbf06c63992c474664fa38a645699";

		downloadFileFromUrl(url, token, "/home/lap15454/Desktop/file.zip");
	}
}
