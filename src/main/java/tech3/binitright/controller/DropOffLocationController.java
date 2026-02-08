package tech3.binitright.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tech3.binitright.model.DropOffLocation;
import tech3.binitright.request.NearByBinDto;
import tech3.binitright.service.DropOffLocationImplementation;

@RestController
@RequestMapping("/api/bins")
public class DropOffLocationController {

    private final DropOffLocationImplementation service;

    public DropOffLocationController(DropOffLocationImplementation service) {
        this.service = service;
    }

    @GetMapping
    public List<DropOffLocation> getAllBins() {
        return service.getAllBins();
    }

    @GetMapping("/nearby")
    public List<NearByBinDto> nearbyBins(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "30") double radius) {
    	System.out.println("Inside dropOff controller:  "+lat);

        return service.getNearbyBins(lat, lng, radius);
    }

    @GetMapping("/search")
    public List<NearByBinDto> FindBins(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(required = false) Double radius,
            @RequestParam(required = false) String binType,
            @RequestParam(required = false) Integer limit) {
    	System.out.println("Inside.....");

        if (radius != null) {
            return service.getNearbyBins(lat, lng, radius)
                    .stream()
                    .filter(bin -> binType == null || bin.getBinType().equalsIgnoreCase(binType))
                    .limit(limit != null ? limit : Long.MAX_VALUE)
                    .toList();
        }


        List<NearByBinDto> list = service.searchBins(lat, lng, binType)
                .stream()
                .limit(limit != null ? limit : Long.MAX_VALUE)
                .toList();
        
       System.out.println("list size:"+list.size());
       return list;
    }
    
}
