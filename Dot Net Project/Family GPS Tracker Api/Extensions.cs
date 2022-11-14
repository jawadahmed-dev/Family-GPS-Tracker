using Family_GPS_Tracker_Api.Contracts.V1.DTOs.Responses;
using Family_GPS_Tracker_Api.Contracts.V1.RequestDtos;
using Family_GPS_Tracker_Api.Contracts.V1.ResponseDtos;
using Family_GPS_Tracker_Api.Domain;
using Family_GPS_Tracker_Api.Domain;
using Microsoft.AspNetCore.Http;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using static Family_GPS_Tracker_Api.Domain.IdentityModels;

namespace Family_GPS_Tracker_Api
{
	public static class Extentions
	{
		// User Extention Methods

		/*public static UserDto AsUserDto(this User user)
		{

			return new UserDto
			{
				userId = user.UserId,
				userType = user.UserType.Name,
				parent = user.Parent.AsParentDto(),
				child = user.Child.AsChildDto(),


			};
		}

		// Location Extenion Methods
		public static LocationDto AsLocationDto(this Location location)
		{
			if (location != null)
			{
				return new LocationDto()
				{
					LocationId = location.LocationId,
					Latitude = location.Latitude,
					Longitude = location.Longitude,
					Time = location.Time,
					ChildName = location.Child.Name
				};
			}

			return null;

		}

		// Location Extenion Methods
		public static IEnumerable<LocationDto> AsLocationDtoList(this IEnumerable<Location> locationList)
		{
			var locationDtoList = new List<LocationDto>();

			if (locationList != null)
			{
				foreach (Location location in locationList)
				{

					locationDtoList.Add(new LocationDto()
					{
						LocationId = location.LocationId,
						Latitude = location.Latitude,
						Longitude = location.Longitude,
						Time = location.Time,
						ChildName = location.Child.Name

					});
				}

				return locationDtoList;
			}



			return null;
		}

		// Child Extension Methods

		public static ChildDto AsChildDto(this Child child)
		{
			if (child != null)
			{
				return new ChildDto()
				{
					childId = child.ChildId,
					name = child.Name,
					email = child.Email,
					password = child.Password,
					parentId = child.ParentId


				};
			}

			return null;

		}

		public static ChildDetailDto AsChildDetailDto(this Child child)
		{
			return new ChildDetailDto
			{
				childId = child.ChildId,
				name = child.Name,
				email = child.Email,
				password = child.Password,
				parent = child.Parent.AsParentDto()

			};
		}
		*/
		public static IEnumerable<ChildResponse> AsChildDtoList(
			this ICollection<Child> children)
		{
			var childDtoList = new List<ChildResponse>();

			foreach (Child child in children)
			{

				childDtoList.Add(new ChildResponse
				{
					ChildId = child.ChildId,
					Name = child.User.UserName,
					Email = child.User.Email

				});
			}

			return childDtoList;
		}

		// Parent Extension Methods

		public static ParentResponse AsParentDto(this Parent parent)
		{
			if (parent != null)
			{
				return new ParentResponse()
				{
					ParentId = parent.ParentId,
					Name = parent.User.UserName,
					Email = parent.User.Email,
					PhoneNumber = parent.User.PhoneNumber,
					Roles = parent.User.UserRoles
					.Select(ur => ur.Role).ToList()
					.Select(r => r.Name).ToList(),

				};
			}
			return null;
		}

		/*public static ParentDetailDto AsParentDetailDto(this Parent parent)
		{
			return new ParentDetailDto
			{
				ParentId = parent.ParentId,
				Name = parent.User.UserName,
				Email = parent.User.Email,
				PhoneNumber = parent.User.PhoneNumber,
				Roles = parent.User.UserRoles
				.Select(ur => ur.Role).ToList()
				.Select(r => r.Name).ToList(),
				Children = parent.Children.AsChildDtoList()

			};
		}*/

		public static IEnumerable<String> AsRoleDtoList(this IEnumerable<ApplicationUserRole> userRoles)
		{
			var newRoles = new List<String>();
			if (userRoles != null)
			{
				foreach (var userRole in userRoles)
				{
					newRoles.Add(userRole.Role.Name);
				}
				return newRoles;
			}

			return null;
		}

		// PairingCode Extension Methods

/*
		public static PairingCodeDto AsPairingCodeDto(this String pairingCode)
		{
			return new PairingCodeDto
			{
				code = pairingCode
			};
		}*/

		// Notification Extension Methods

		public static NotificationDto AsNotificationDto(this Notification notification)
		{
			return new NotificationDto
			{
				NotificationId = notification.NotificationId,
				Title = notification.Title,
				Message = notification.Message,
				CreatedAt = notification.CreatedAt
			};
		}



		public static List<NotificationDto> AsNotificationDtoList(this ICollection<Notification> notifications)
		{
			var notificationDtoList = new List<NotificationDto>();

			foreach (Notification notification in notifications)
			{

				notificationDtoList.Add(notification.AsNotificationDto());
			}

			return notificationDtoList;
		}

		public static string GetUserId(this HttpContext httpContext)
		{

			if (httpContext.User == null)
			{
				return string.Empty;
			}

			return httpContext.User.Claims.Single(x => x.Type == "userId").Value;
		}
		public static DeviceTokenResponse AsDeviceTokenResponse(this DeviceToken deviceToken)
		{

			if (deviceToken != null)
			{
				return new DeviceTokenResponse
				{
					Token = deviceToken.Token
				};
			}

			return null;
		}

	}
}
