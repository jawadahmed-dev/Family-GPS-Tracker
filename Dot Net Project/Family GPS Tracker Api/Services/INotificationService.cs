using Family_GPS_Tracker_Api.Domain;
using Family_GPS_Tracker_Api.Domain;
using Family_GPS_Tracker_Api.Domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Family_GPS_Tracker_Api.Services
{
	public interface INotificationService
	{
		public Task<ResponseModel<Notification>> SendNotificationAsync(NotificationModel notificationModel);
		Task<ResponseModel<IEnumerable<Notification>>> GetNotificationsByParentIdAsync(Guid parentId);
		Task<ResponseModel<IEnumerable<Notification>>> GetNotificationsByChildIdAsync(Guid childId);
	}
}
