using Family_GPS_Tracker_Api.Domain;
using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.EntityFrameworkCore;
using System.Threading.Tasks;
using Family_GPS_Tracker_Api.Contracts.V1.RequestDtos;
using Family_GPS_Tracker_Api.Contracts.V1.ResponseDtos;
using Family_GPS_Tracker_Api.Domain;

namespace Family_GPS_Tracker_Api.Repositories
{

	public class ParentRepository : IParentRepository
	{
		private readonly AppDbContext _db;


		public ParentRepository(AppDbContext db)
		{
			_db = db;
		}

		public async Task<bool> CreateParentAsync(Parent parent)
		{
			await _db.Parents.AddAsync(parent);
			var created = await _db.SaveChangesAsync();
			return created > 0;
		}

		public async Task<Parent> GetParentByIdAsync(Guid parentId)
		{
			return await _db.Parents
				.Include(parent => parent.User)
				.ThenInclude(user => user.UserRoles)
				.ThenInclude(userRoles => userRoles.Role)
				.FirstOrDefaultAsync(x => x.ParentId == parentId);
		}

		public async Task<Parent> GetParentDetailsByIdAsync(Guid parentId)
		{
			return await _db.Parents
				.Include(parent => parent.Children)
				.ThenInclude(child => child.User)
				.ThenInclude(user => user.UserRoles)
				.ThenInclude(userRoles => userRoles.Role)
				.Include(parent => parent.User)
				.ThenInclude(user => user.UserRoles)
				.ThenInclude(userRoles => userRoles.Role)
				.FirstOrDefaultAsync(parent => parent.ParentId == parentId);


		}

		public async Task<bool> LinkChildAsync(Parent parent, Child child)
		{
			child.Parent = parent;
			var isChildLinked = await _db.SaveChangesAsync();
			return isChildLinked > 0;
		}

		public async Task<bool> UpdateDeviceTokenAsync(Parent parent, DeviceToken deviceToken)
		{
			parent.DeviceToken = deviceToken.Token;
			return await _db.SaveChangesAsync() > 0;
		}

		public async Task<String> GetDeviceTokenAsync(Guid parentId)
		{
			return await _db.Parents.Where(parent => parent.ParentId == parentId)
				.Select(parent => parent.DeviceToken)
				.SingleOrDefaultAsync();

		}



		/*public void CreateParent(Parent item)
		{
			User user = new User()
			{
				UserId = Guid.NewGuid(),

			};

			UserType userType = new UserType()
			{
				UserTypeId = Guid.NewGuid(),
				Name = "parent"
			};

			user.Parent = item;
			user.UserType = userType;
			_db.Users.Add(user);
			_db.SaveChanges();

		}

		public IEnumerable<Parent> GetAll()
		{
			throw new NotImplementedException();
		}

		public Parent Get(Guid id)
		{

			return _db.Parents.SingleOrDefault(parent => parent.ParentId == id);

		}

		public Parent GetDetails(Guid id)
		{
			return _db.Parents
				.Include(parent => parent.Children)
				.SingleOrDefault(parent => parent.ParentId == id);

		}


		

		public Parent UpdateDeviceToken(Parent parent, String deviceToken)
		{
			parent.DeviceToken = deviceToken;
			_db.Parents.Update(parent);
			_db.SaveChanges();
			return parent;

		}*/
	}
}

