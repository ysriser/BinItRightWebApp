package tech3.binitright.interfacemethods;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import tech3.binitright.model.CheckIn;
import tech3.binitright.request.CheckInDataReq;
import tech3.binitright.response.CheckInDataResponse;

public interface CheckInInterface {
	 public CheckIn processCheckIn(MultipartFile video, CheckInDataReq data) throws IOException;

	 public List<CheckIn> getPendingCheckIns();
	 
	 public CheckInDataResponse validateCheckIn(Long long1, int quantity);
	 	 
}
