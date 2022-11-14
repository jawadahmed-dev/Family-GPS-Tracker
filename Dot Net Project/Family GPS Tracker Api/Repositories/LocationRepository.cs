using Family_GPS_Tracker_Api.Domain;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Family_GPS_Tracker_Api.Repositories
{
	public class LocationRepository : ILocationRepository
	{
		private readonly AppDbContext _db;

		public LocationRepository(AppDbContext db)
		{
			_db = db;
		}

		public async Task<bool> CreateLocationAsync(Location location)
		{

			await _db.Locations.AddAsync(location);
			var isCreated = await _db.SaveChangesAsync();
			return isCreated > 0;
		}

		public async Task<Location> GetLastLocationByChildIdAsync(Guid childId)
		{
			return await _db.Locations
				.Include(location => location.Child)
				.ThenInclude(location => location.User)
				.Where(location => location.ChildId == childId)
				.OrderByDescending(Location => Location.UniqueNumber)
				.FirstOrDefaultAsync();
			
		}

		public async Task<IEnumerable<Location>> GetLastTenLocationsByChildIdAsync(Guid childId)
		{
			return await _db.Locations
				.Include(location => location.Child)
				.ThenInclude(location => location.User)
				.Where(location => location.ChildId == childId)
				.OrderByDescending(location => location.UniqueNumber)
				.Take(10)
				.ToListAsync();
		}

		public async Task<Location> GetLocationByChildIdAsync(Guid childId)
		{
			return await _db.Locations
				.Include(location => location.Child)
				.ThenInclude(location => location.User)
				.Where(location => location.ChildId == childId)
				.FirstOrDefaultAsync();
		}
	}
}
