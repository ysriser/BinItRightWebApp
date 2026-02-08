package tech3.binitright.interfacemethods;

import java.io.IOException;
import java.util.List;

import tech3.binitright.model.CheckIn;
import tech3.binitright.request.CheckInDataReq;

public interface CheckInInterface {
    List<CheckIn> getAllCheckIns();

    public CheckIn processCheckIn(CheckInDataReq data, Long userId) throws IOException;

    public List<CheckIn> getPendingCheckIns();

    public Integer getUserTotalRecycled(Long userId);

}
