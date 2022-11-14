/*using CatalogWebApi.Domain;
using Family_GPS_Tracker_Api;
using Family_GPS_Tracker_Api.Contracts.V1.RequestDtos;
using Family_GPS_Tracker_Api.Contracts.V1.ResponseDtos;
using Family_GPS_Tracker_Api.Domain;
using Family_GPS_Tracker_Api.Repositories;
using Family_GPS_Tracker_Api.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Catalog.Controllers
{
	[Route("notification")]
	[ApiController]
	public class NotificationController : ControllerBase
	{
		private readonly INotificationService _notificationService;
		private readonly ParentRepository _parentRepository;

		public NotificationController(INotificationService notificationService,
			ParentRepository parentRepository)
		{
			_notificationService = notificationService;
			_parentRepository = parentRepository;
		}

		[Route("send")]
		[HttpPost]
		public async Task<IActionResult> SendNotification(CreateNotificationDto notificationModelDto)
		{
			var token = _parentRepository.GetDeviceToken(notificationModelDto.RecieverId);
			if (token == null)
			{
				NotFound();
			}
			var result = await _notificationService.SendNotification(new NotificationModel
			{
				DeviceId = token,
				Body = notificationModelDto.Body,
				IsAndroiodDevice = notificationModelDto.IsAndroiodDevice,
				Title = notificationModelDto.Title

			}, notificationModelDto.SenderId, notificationModelDto.RecieverId);
			return Ok(result);
		}


		[HttpGet]
		public ActionResult<ICollection<NotificationDto>> GetAll()
		{
			return _notificationService.GetNotifications().AsNotificationDtoList();
		}

	}
}
*/