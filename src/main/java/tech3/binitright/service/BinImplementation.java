package tech3.binitright.service;

/*@Service
@Transactional
public class BinImplementation implements BinInterface{
	List<NearbyBin> result = new ArrayList<NearbyBin>();

	@Override
	@Transactional
	public List<NearbyBin> findNearbyBins(double lat, double lng, double radius) {
		for(RecyclingBin bin: HardcodedBins.bins) {
			double distance = DistanceCalculator.calculateDistance(lat, lng, bin.lat, bin.lng);

			if(distance<=radius) {
				result.add(new NearbyBin(bin, distance));
			}
		}

		Collections.sort(result, new Comparator<NearbyBin>() {
			@Override
			public int compare(NearbyBin a, NearbyBin b) {
				if(a.distance<b.distance) return -1;
				if(a.distance>b.distance) return 1;
				return 0;
			}
		});

		return result;
	}

}
*/
