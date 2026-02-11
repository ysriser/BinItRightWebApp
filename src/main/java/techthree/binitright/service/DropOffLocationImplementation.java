package techthree.binitright.service;

import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import techthree.binitright.interfacemethods.DropOffLocationInterface;
import techthree.binitright.model.DropOffLocation;
import techthree.binitright.repository.DropOffLocationRepository;
import techthree.binitright.request.NearByBinDto;
import techthree.binitright.util.DistanceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
@Transactional
public class DropOffLocationImplementation implements DropOffLocationInterface{
    private static final Logger logger = LoggerFactory.getLogger(DropOffLocation.class);


    @Autowired
	private DropOffLocationRepository repository;

    public List<DropOffLocation> getAllBins() {
        return repository.findAll();
    }

    public List<NearByBinDto> getNearbyBins(
            double userLat, double userLng, double radiusMeters) {
        logger.info("getNearbyBins called with userLat={}", userLat);


        List<DropOffLocation> allBins = repository.findAll();

        return allBins.stream()
                .map(bin -> {
                    double distance = DistanceUtil.distanceInMeters(
                            userLat, userLng,
                            bin.getLatitude().doubleValue(), 
                            bin.getLongitude().doubleValue());

                    return new Object[]{bin, distance};
                })
                .filter(obj -> (double) obj[1] <= radiusMeters)
                .sorted(Comparator.comparingDouble(obj -> (double) obj[1]))
                .limit(3)
                .map(obj -> {
                    DropOffLocation bin = (DropOffLocation) obj[0];
                    double distance = (double) obj[1];

                    logger.info("Bin size: {}", allBins.size());


                    return new NearByBinDto(
                            bin.getId(),
                            bin.getName(),
                            bin.getAddress(),
                            bin.getDescription(),
                            bin.getPostalCode(),
                            bin.getBinType(),
                            bin.getStatus(),
                            bin.getLatitude(),
                            bin.getLongitude(),
                            distance
                    );
                })
                .toList();
    }

    public List<NearByBinDto> searchBins(
            double userLat,
            double userLng,
            String type
    ) {
        List<DropOffLocation> allBins = repository.findAll();
        logger.debug("AllBins size: {}", allBins.size());


        return allBins.stream()

                // Filter by bin type
        		.filter(bin ->
        	    type == null 
        	    || type.isBlank() 
        	    || type.equalsIgnoreCase("All")     
        	    || bin.getBinType().equalsIgnoreCase(type)
        	)

                // Distance
                .map(bin -> {
                    double distance = DistanceUtil.distanceInMeters(
                            userLat, userLng,
                            bin.getLatitude().doubleValue(), bin.getLongitude().doubleValue()
                    );
                    return new Object[]{bin, distance};
                })

                // Sort by nearest first
                .sorted(Comparator.comparingDouble(obj -> (double) obj[1]))

                // Convert to DTO
                .map(obj -> {
                    DropOffLocation bin = (DropOffLocation) obj[0];
                    double distance = (double) obj[1];

                    logger.info("Return from search bins");


                    return new NearByBinDto(
                            bin.getId(),
                            bin.getName(),
                            bin.getAddress(),
                            bin.getDescription(),
                            bin.getPostalCode(),
                            bin.getBinType(),
                            bin.getStatus(),
                            bin.getLatitude(),
                            bin.getLongitude(),
                            distance
                    );
                })
                .toList();
    }
}
