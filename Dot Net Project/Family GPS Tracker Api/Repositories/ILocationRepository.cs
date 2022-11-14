using Family_GPS_Tracker_Api.Domain;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Family_GPS_Tracker_Api.Repositories
{
	public interface ILocationRepository
	{
		Task<bool> CreateLocationAsync(Location location);
		Task<Location> GetLocationByChildIdAsync(Guid childId);
		Task<Location> GetLastLocationByChildIdAsync(Guid childId);
		Task<IEnumerable<Location>> GetLastTenLocationsByChildIdAsync(Guid childId);
	}
}