using Family_GPS_Tracker_Api.Domain;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Family_GPS_Tracker_Api.Repositories
{
	public interface INotificationRepository
	{
		Task<bool> CreateNotificationAsync(Notification notification);
		Task<IEnumerable<Notification>> GetNotificationsByParentIdAsync(Guid parentId);
		Task<IEnumerable<Notification>> GetNotificationsByChildIdAsync(Guid childId);
	}
}