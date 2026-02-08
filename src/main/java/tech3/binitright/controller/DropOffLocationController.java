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
public final class DropOffLocationController {

    private final DropOffLocationImplementation service;

    public DropOffLocationController(final DropOffLocationImplementation service) {
        this.service = service;
    }

    @GetMapping
    public List<DropOffLocation> getAllBins() {
        return service.getAllBins();
    }

    @GetMapping("/nearby")
    public List<NearByBinDto> nearbyBins(
            @RequestParam final double lat,
            @RequestParam final double lng,
            @RequestParam(defaultValue = "30") final double radius) {
        return service.getNearbyBins(lat, lng, radius);
    }

    @GetMapping("/search")
    public List<NearByBinDto> findBins(
            @RequestParam final double lat,
            @RequestParam final double lng,
            @RequestParam(required = false) final Double radius,
            @RequestParam(required = false) final String binType,
            @RequestParam(required = false) final Integer limit) {
        if (radius != null) {
            return service.getNearbyBins(lat, lng, radius)
                    .stream()
                    .filter(bin -> binType == null || bin.getBinType().equalsIgnoreCase(binType))
                    .limit(limit != null ? limit : Long.MAX_VALUE)
                    .toList();
        }
        return service.searchBins(lat, lng, binType)
                .stream()
                .limit(limit != null ? limit : Long.MAX_VALUE)
                .toList();
    }
}