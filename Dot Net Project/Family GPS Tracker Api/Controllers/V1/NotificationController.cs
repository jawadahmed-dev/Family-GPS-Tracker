using Family_GPS_Tracker_Api.Domain;
using Family_GPS_Tracker_Api.Contracts;
using Family_GPS_Tracker_Api.Contracts.V1.DTOs.Responses;
using Family_GPS_Tracker_Api.Contracts.V1.RequestDtos;
using Family_GPS_Tracker_Api.Domain;
using Family_GPS_Tracker_Api.Repositories;
using Family_GPS_Tracker_Api.Services;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Family_GPS_Tracker_Api.V1.Controllers
{
	[ApiController]
	public class NotificationController : ControllerBase
	{
		private readonly INotificationService _notificationService;
		private readonly IParentRepository _parentRepository;
		private readonly IChildRepository _childRepository;

		public NotificationController(INotificationService notificationService,
			IParentRepository parentRepository, IChildRepository childRepository)
		{
			_notificationService = notificationService;
			_parentRepository = parentRepository;
			_childRepository = childRepository;
		}

		[HttpPost(ApiRoutes.Notification.SendNotification)]
		public async Task<IActionResult> SendNotificationAsync(NotificationRequest notificationRequest)
		{

			if (!notificationRequest.ChildId.ToString().Equals(HttpContext.GetUserId()))
			{

				return StatusCode(403, new Response<DeviceTokenResponse>
				{

					StatusCode = "403",
					Message = "You cannot perform this operation"

				});
			}

			var parent = await _parentRepository.GetParentByIdAsync(notificationRequest.ParentId);

			if (parent == null)
			{
				return NotFound(new Response<DeviceTokenResponse>
				{

					StatusCode = "404",
					Message = "Parent doesn't exist with this parentId"

				});
			}

			if (parent.DeviceToken == null)
			{
				return NotFound(new Response<Boolean> { 
					StatusCode = "404",
					Message = "Token couldn't be found"
				});
			}

			var responseModel = await _notificationService.SendNotificationAsync(new NotificationModel
			{
				Title = notificationRequest.Title,
				Body = notificationRequest.Body,
				DeviceToken = parent.DeviceToken,
				IsAndroiodDevice = notificationRequest.IsAndroiodDevice,
				ChildId = notificationRequest.ChildId,
				ParentId = notificationRequest.ParentId
			});

			if (!responseModel.IsSuccess) {
				return StatusCode(500, new Response<Boolean> { 
					StatusCode = responseModel.StatusCode,
					Message = responseModel.Message
				});
			}

			return Ok(new Response<Boolean>
			{
				StatusCode = responseModel.StatusCode,
				Message = responseModel.Message
			});
		}

		[HttpGet(ApiRoutes.Notification.GetNotificationsByParentId)]
		public async Task<ActionResult> GetNotificationsByParentIdAsync([FromRoute(Name = "parent-id")] Guid parentId) {

			var parent = await _parentRepository.GetParentByIdAsync(parentId);

			if (parent == null)
			{
				return NotFound(new Response<IEnumerable<Notification>>
				{

					StatusCode = "404",
					Message = "Parent doesn't exist with this parentId"

				});
			}

			var responseModel = await _notificationService.GetNotificationsByParentIdAsync(parentId);

			if (!responseModel.IsSuccess) {

				return NotFound(new Response<IEnumerable<Notification>>
				{
					StatusCode = responseModel.StatusCode,
					Message = responseModel.Message
				});
			}

			return Ok(new Response<IEnumerable<Notification>>
			{
				IsSuccess = responseModel.IsSuccess,
				StatusCode = responseModel.StatusCode,
				Message = responseModel.Message
			});
		}

		[HttpGet(ApiRoutes.Notification.GetNotificationsByChildId)]
		public async Task<ActionResult> GetNotificationsByChildIdAsync([FromRoute(Name = "child-id")] Guid childId)
		{

			var child = await _childRepository.GetChildByIdAsync(childId);

			if (child == null)
			{
				return NotFound(new Response<IEnumerable<Notification>>
				{

					StatusCode = "404",
					Message = "Parent doesn't exist with this parentId"

				});
			}

			var responseModel = await _notificationService.GetNotificationsByChildIdAsync(childId);

			if (!responseModel.IsSuccess)
			{

				return NotFound(new Response<IEnumerable<Notification>>
				{
					StatusCode = responseModel.StatusCode,
					Message = responseModel.Message
				});
			}

			return Ok(new Response<IEnumerable<Notification>>
			{
				IsSuccess = responseModel.IsSuccess,
				StatusCode = responseModel.StatusCode,
				Message = responseModel.Message
			});
		}


	}
}
