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

@Service
@Transactional
public class DropOffLocationImplementation implements DropOffLocationInterface{
	
	@Autowired
	private DropOffLocationRepository repository;

    public List<DropOffLocation> getAllBins() {
        return repository.findAll();
    }

    public List<NearByBinDto> getNearbyBins(
            double userLat, double userLng, double radiusMeters) {
    	System.out.println("###getNearbyBins"+userLat);

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

                    System.out.println("BinSize"+allBins.size());

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
        System.out.println("AllBins size"+allBins.size());

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
                    
                    System.out.println("Return from search bins");

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
