package tech3.binitright.interfacemethods;

import java.io.IOException;
import java.util.List;

import tech3.binitright.dto.LeaderboardDTO;
import tech3.binitright.model.CheckIn;
import tech3.binitright.request.CheckInDataReq;

public interface CheckInInterface {
    List<CheckIn> getAllCheckIns();

    CheckIn processCheckIn(CheckInDataReq data, Long userId) throws IOException;

    List<CheckIn> getPendingCheckIns();

    Integer getUserTotalRecycled(Long userId);

    List<LeaderboardDTO> getMonthlyLeaderboard();
}