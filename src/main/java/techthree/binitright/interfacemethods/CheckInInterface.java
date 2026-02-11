package techthree.binitright.interfacemethods;

import java.io.IOException;
import java.util.List;

import techthree.binitright.dto.LeaderboardDTO;
import techthree.binitright.model.CheckIn;
import techthree.binitright.request.CheckInDataReq;

public interface CheckInInterface {
    List<CheckIn> getAllCheckIns();

    CheckIn processCheckIn(CheckInDataReq data, Long userId) throws IOException;

    List<CheckIn> getPendingCheckIns();

    Integer getUserTotalRecycled(Long userId);

    List<LeaderboardDTO> getMonthlyLeaderboard();
}