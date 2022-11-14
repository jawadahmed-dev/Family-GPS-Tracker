using AutoMapper;
using Family_GPS_Tracker_Api.Contracts.V1.DTOs.Responses;
using Family_GPS_Tracker_Api.Contracts.V1.ResponseDtos;
using Family_GPS_Tracker_Api.Contracts.V1.Responses;
using Family_GPS_Tracker_Api.Domain;
using Family_GPS_Tracker_Api.Domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using static Family_GPS_Tracker_Api.Domain.IdentityModels;

namespace Family_GPS_Tracker_Api.MappingProfiles
{
	public class DomainToResponseProfile : Profile
	{
		public DomainToResponseProfile()
		{

			CreateMap<Parent, ParentResponse>()
				.ForMember(des => des.Name,
				opt => opt.MapFrom(src => src.User.UserName))
				.ForMember(des => des.Email,
				opt => opt.MapFrom(src => src.User.Email))
				.ForMember(des => des.PhoneNumber,
				opt => opt.MapFrom(src => src.User.PhoneNumber))
				.ForMember(des => des.Roles,
				opt => opt.MapFrom(src => src.User.UserRoles
				.Select(ur => ur.Role).ToList()
				.Select(r => r.Name).ToList()));

			CreateMap<Parent, ParentDetailResponse>()
				.ForMember(des => des.Name,
				opt => opt.MapFrom(src => src.User.UserName))
				.ForMember(des => des.Email,
				opt => opt.MapFrom(src => src.User.Email))
				.ForMember(des => des.PhoneNumber,
				opt => opt.MapFrom(src => src.User.PhoneNumber))
				.ForMember(des => des.Roles,
				opt => opt.MapFrom(src => src.User.UserRoles
				.Select(ur => ur.Role).ToList()
				.Select(r => r.Name).ToList()))
				.ForMember(des => des.Children,
				opt => opt.MapFrom(src => src.Children
				.Select(c => new ChildResponse {
					ChildId = c.ChildId,
					Name = c.User.UserName,
					Email = c.User.Email,
					Roles = c.User.UserRoles
					.Select(ur => ur.Role).ToList()
					.Select(r => r.Name).ToList()
				})));

			CreateMap<Child, ChildResponse>()
				.ForMember(des => des.Name,
				opt => opt.MapFrom(src => src.User.UserName))
				.ForMember(des => des.Email,
				opt => opt.MapFrom(src => src.User.Email))
				.ForMember(des => des.Roles,
				opt => opt.MapFrom(src => src.User.UserRoles
				.Select(ur => ur.Role).ToList()
				.Select(r => r.Name).ToList()));

			CreateMap<Child, ChildDetailResponse>()
				.ForMember(des => des.Name,
				opt => opt.MapFrom(src => src.User.UserName))
				.ForMember(des => des.Email,
				opt => opt.MapFrom(src => src.User.Email))
				.ForMember(des => des.Roles,
				opt => opt.MapFrom(src => src.User.UserRoles
				.Select(ur => ur.Role).ToList()
				.Select(r => r.Name).ToList()))
				.ForMember(des => des.Parent,
				opt => opt.MapFrom(src => new ParentResponse {
					ParentId = src.Parent.ParentId,
					Name = src.Parent.User.UserName,
					Email = src.Parent.User.Email,
					PhoneNumber = src.Parent.User.PhoneNumber,
					Roles = src.Parent.User.UserRoles
					.Select(ur => ur.Role).ToList()
					.Select(r => r.Name).ToList()
				}));

			CreateMap<PairingCode, PairingCodeResponse>();

			CreateMap<DeviceToken, DeviceTokenResponse>();

			CreateMap<AuthResult, AuthSuccessResponse>();

			CreateMap<AuthResult, AuthFailedResponse>();

			CreateMap<Location, LocationDto>()
				.ForMember(des => des.ChildId,
				opt => opt.MapFrom(src => src.ChildId))
				.ForMember(des => des.ChildName,
				opt => opt.MapFrom(src => src.Child.User.UserName));

		}
	}
}
