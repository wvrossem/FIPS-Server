package ips.data.upload;

import ips.data.DataManager;
import ips.data.entities.wlan.WLANFingerprint;
import ips.data.serialization.Serializer;
import ips.server.DataUploadRequest;

import java.io.File;

/**
 * 
 * Can be used to upload data 
 * 
 * @author Wouter Van Rossem
 * 
 */
public class DataUploader {

	private DataManager dm;
	private Serializer sz;

	public DataUploader() {

		dm = new DataManager();
		sz = Serializer.getInstance();
	}

	public WLANFingerprint findWithSamePosition(WLANFingerprint fp) {

		return fp;
	}

	public void upload(DataUploadRequest req) {

		req.resolveFilters();

		for (WLANFingerprint fp : req.getFps()) {

			dm.persist(fp);
		}
	}

	public void uploadFromXML(File file) {

		try {

			WLANFingerprint fp = (WLANFingerprint) sz.deserialize(
					WLANFingerprint.class, file);

			dm.persist(fp);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void uploadFolder(String folderPath) {

		File folder = new File(folderPath);

		for (File file : folder.listFiles()) {

			uploadFromXML(file);
		}
	}

	public void clearData() {

		dm.clearDatabase();
	}

}
