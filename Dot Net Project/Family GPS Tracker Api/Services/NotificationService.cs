using Family_GPS_Tracker_Api.Domain;
using Family_GPS_Tracker_Api.Repositories;
using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using static Family_GPS_Tracker_Api.Domain.GoogleNotification;

namespace Family_GPS_Tracker_Api.Services
{
	public class NotificationService : INotificationService
	{
		public readonly IHttpClientFactory _httpClientFactory;
		public readonly INotificationRepository _notificationRepository;

		public NotificationService(IHttpClientFactory httpClientFactory, INotificationRepository notificationRepository)
		{
			_httpClientFactory = httpClientFactory;
			_notificationRepository = notificationRepository;
		}

		public async Task<ResponseModel<IEnumerable<Notification>>> GetNotificationsByChildIdAsync(Guid childId)
		{
			var notifications = await _notificationRepository.GetNotificationsByChildIdAsync(childId);
			if (notifications == null) {

				return new ResponseModel<IEnumerable<Notification>>
				{
					Message = "No notifications found"
				};

			}

			return new ResponseModel<IEnumerable<Notification>> {
				IsSuccess = true,
				Message = "Notifications found successfully",
				Data = notifications
			};
		}

		public async Task<ResponseModel<IEnumerable<Notification>>> GetNotificationsByParentIdAsync(Guid parentId)
		{
			var notifications = await _notificationRepository.GetNotificationsByParentIdAsync(parentId);
			if (notifications == null)
			{

				return new ResponseModel<IEnumerable<Notification>>
				{
					Message = "No notifications found"
				};

			}

			return new ResponseModel<IEnumerable<Notification>>
			{
				IsSuccess = true,
				Message = "Notifications found successfully",
				Data = notifications
			};
		}

		public async Task<ResponseModel<Notification>> SendNotificationAsync(NotificationModel notificationModel)
		{
			var jsonData = MakePayload(notificationModel);

			var httpClient = _httpClientFactory.CreateClient("FCM");

			var response = await httpClient.PostAsync("/fcm/send", new StringContent(jsonData, Encoding.UTF8, "application/json"));
			
			if (!response.IsSuccessStatusCode)
			{

				return new ResponseModel<Notification>
				{
					Message = "something went wrong!",
					StatusCode = response.StatusCode.ToString()
				};

			}

			var isCreated = await _notificationRepository.CreateNotificationAsync( new Notification {
				
				ChildId = notificationModel.ChildId,
				ParentId = notificationModel.ParentId,
				Title = notificationModel.Title,
				Message = notificationModel.Body,
				CreatedAt = DateTime.Now.ToString()
			});

			if (!isCreated) {

				return new ResponseModel<Notification>
				{
					
					Message = "Notification Couldnt be registered",
					StatusCode = "Internal Server Error"
				};
			}

			return new ResponseModel<Notification>
			{
				IsSuccess =true,
				Message = "Notification sent!",
				StatusCode = response.StatusCode.ToString()
			};

		}

		private string MakePayload(NotificationModel notificationModel)
		{
			DataPayload dataPayload = new DataPayload();
			dataPayload.Title = notificationModel.Title;
			dataPayload.Body = notificationModel.Body;

			GoogleNotification notification = new GoogleNotification();
			notification.Data = dataPayload;
			notification.Notification = dataPayload;
			notification.DeviceToken = notificationModel.DeviceToken;

			return JsonConvert.SerializeObject(notification,
				new JsonSerializerSettings
				{
					ContractResolver = new CamelCasePropertyNamesContractResolver()
				});
		}

		
	}
}
