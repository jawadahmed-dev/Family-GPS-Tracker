/*using CatalogWebApi.Domain;
using CorePush.Google;
using Family_GPS_Tracker_Api.Domain;
using Family_GPS_Tracker_Api.Repositories;
using Family_GPS_Tracker_Api.Options;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading.Tasks;
using static CatalogWebApi.Domain.GoogleNotification;
using Microsoft.Extensions.Options;

namespace Family_GPS_Tracker_Api.Services
{
	public interface INotificationService
	{
		Task<ResponseModel> SendNotification(NotificationModel notificationModel,
			Guid senderId,
			Guid receiverId);
		ICollection<Notification> GetNotifications();
	}

	public class NotificationService : INotificationService
	{
		private readonly FcmNotificationOptions _fcmNotificationSetting;
		private readonly NotificationRepository _repository;
		private readonly ChildRepository _childRepository;
		private readonly ParentRepository _parentRepository;

		public NotificationService(

			IOptions<FcmNotificationOptions> settings,
			NotificationRepository repository,
			ParentRepository parentRepository,
			ChildRepository childRepository
			)
		{
			_fcmNotificationSetting = settings.Value;
			_repository = repository;
			_parentRepository = parentRepository;
			_childRepository = childRepository;
		}

		public ICollection<Notification> GetNotifications()
		{
			return _repository.GetNotifications();
		}

		public async Task<ResponseModel> SendNotification(NotificationModel notificationModel,
			Guid senderId,
			Guid receiverId)
		{
			ResponseModel response = new ResponseModel();
			try
			{
				if (notificationModel.IsAndroiodDevice)
				{
					//FCM Sender(Android Device);

					FcmSettings settings = new FcmSettings()
					{
						SenderId = _fcmNotificationSetting.SenderId,
						ServerKey = _fcmNotificationSetting.ServerKey
					};
					HttpClient httpClient = new HttpClient();

					string authorizationKey = string.Format("key={0}", settings.ServerKey);
					string deviceToken = notificationModel.DeviceId;

					httpClient.DefaultRequestHeaders.TryAddWithoutValidation("Authorization", authorizationKey);
					httpClient.DefaultRequestHeaders.Accept
							.Add(new MediaTypeWithQualityHeaderValue("application/json"));

					DataPayload dataPayload = new DataPayload();
					dataPayload.Title = notificationModel.Title;
					dataPayload.Body = notificationModel.Body;

					GoogleNotification notification = new GoogleNotification();
					notification.Data = dataPayload;
					notification.Notification = dataPayload;

					var fcm = new FcmSender(settings, httpClient);
					var fcmSendResponse = await fcm.SendAsync(deviceToken, notification);

					if (fcmSendResponse.IsSuccess())
					{

						var parent = _parentRepository.Get(receiverId);
						var child = _childRepository.Get(senderId);
						if (parent != null && child != null)
						{
							var notificationEntity = new Notification
							{
								NotificationId = Guid.NewGuid(),
								Title = notificationModel.Title,
								Message = notificationModel.Body,
								//CreatedAt = DateTime.Now.ToString()
							};
							notificationEntity.Parent = parent;
							notificationEntity.Child = child;
							_repository.CreateNotification(notificationEntity);
						}
						response.IsSuccess = true;
						response.Message = "Notification sent successfully";
						return response;
					}
					else
					{
						response.IsSuccess = false;
						response.Message = fcmSendResponse.Failure.ToString();
						return response;
					}
				}
				else
				{
					//Code here for APN Sender (iOS Device) 
					//var apn = new ApnSender(apnSettings, httpClient);
					//await apn.SendAsync(notification, deviceToken);
				}
				return response;
			}
			catch (Exception ex)
			{
				response.IsSuccess = false;

				response.Message = ex.Message.ToString();
				return response;
			}
		}
	}
}

*/