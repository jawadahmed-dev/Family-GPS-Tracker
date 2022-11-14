using Family_GPS_Tracker_Api.Domain;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Family_GPS_Tracker_Api.Repositories
{
	public class NotificationRepository : INotificationRepository
	{
		private readonly AppDbContext _db;

		public NotificationRepository(AppDbContext db)
		{
			_db = db;
		}

		public async Task<bool> CreateNotificationAsync(Notification notification)
		{
			_db.Notifications.Add(notification);
			await _db.SaveChangesAsync();
			return true;
		}

		public async Task<IEnumerable<Notification>> GetNotificationsByChildIdAsync(Guid childId)
		{
			return await _db.Notifications
				.Where(x => x.ChildId == childId)
				.ToListAsync();
		}

		public async Task<IEnumerable<Notification>> GetNotificationsByParentIdAsync(Guid parentId)
		{
			return await _db.Notifications
				.Where(x => x.ParentId == parentId)
				.ToListAsync();
		}
	}
}
