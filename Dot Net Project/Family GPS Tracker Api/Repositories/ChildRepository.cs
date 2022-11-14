using Family_GPS_Tracker_Api.Domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using Family_GPS_Tracker_Api.Domain;

namespace Family_GPS_Tracker_Api.Repositories
{
	public class ChildRepository : IChildRepository
	{
		private readonly AppDbContext _db;

		public ChildRepository(AppDbContext db)
		{
			_db = db;
		}

		public async Task<bool> CreateChildAsync(Child child)
		{
			await _db.Children.AddAsync(child);
			var created = await _db.SaveChangesAsync();
			return created > 0;
		}

		public async Task<bool> CreatePairingCodeAsync(PairingCode pairingCode)
		{

			await _db.PairingCodes.AddAsync(pairingCode);
			var created = await _db.SaveChangesAsync();
			return created > 0;
		}

		public async Task<Child> GetChildByIdAsync(Guid childId)
		{
			return await _db.Children
				.Include(child => child.PairingCode)
				.Include(child => child.User)
				.ThenInclude(user => user.UserRoles)
				.ThenInclude(userRoles => userRoles.Role)
				.FirstOrDefaultAsync(child => child.ChildId == childId);
		}

		public async Task<Child> GetChildDetailsByIdAsync(Guid childId)
		{
			return await _db.Children
				.Include(c => c.Parent)
				.ThenInclude(p => p.User)
				.ThenInclude(u => u.UserRoles)
				.ThenInclude(ur => ur.Role)
				.Include(c => c.PairingCode)
				.FirstOrDefaultAsync(x => x.ChildId == childId);
		}

		public async Task<PairingCode> GetPairingCodeAsyncByChildId(Guid childId)
		{
			return await _db.PairingCodes
				.Include(x => x.Child)
				.FirstOrDefaultAsync(x => x.ChildId == childId);
		}

		public async Task<PairingCode> GetPairingCodeAsyncByCode(string pairingCode)
		{
			return await _db.PairingCodes
				.Include(x => x.Child)
				.FirstOrDefaultAsync();
		}

		public async Task<bool> UpdatePairingCodeAsync(PairingCode pairingCode)
		{
			_db.PairingCodes.Update(pairingCode);
			var updated = await _db.SaveChangesAsync();
			return updated > 0;
		}


		/*public Child Get(Guid id)
		{
			return _db.Children.SingleOrDefault(child => child.ChildId == id);
		}

		public Child GetDetails(Guid id)
		{
			return _db.Children
				.Include(child => child.Parent)
				.SingleOrDefault(child => child.ChildId == id);
		}

		public Child GetDetails(String pairingCode)
		{
			return _db.Children
				.Include(child => child.Parent)
				.SingleOrDefault(child => child.Code == pairingCode);
		}

		public void Create(Child child)
		{
			User user = new User()
			{
				UserId = Guid.NewGuid(),

			};

			UserType userType = new UserType()
			{
				UserTypeId = Guid.NewGuid(),
				Name = "child"
			};

			user.Child = child;
			user.UserType = userType;
			_db.Users.Add(user);
			_db.SaveChanges();
		}

		public Child LinkParent(Guid parentId, Child child)
		{

			child.ParentId = parentId;
			_db.Children.Update(child);
			_db.SaveChanges();
			return child;
		}

		public String GeneratePairingCode(Child child)
		{

			child.Code = new Random().Next(10000, 99999).ToString();
			_db.Children.Update(child);
			_db.SaveChanges();
			return child.Code;

		}*/
	}
}
