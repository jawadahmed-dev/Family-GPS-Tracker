using Family_GPS_Tracker_Api.Domain;
using Family_GPS_Tracker_Api.Repositories;
using Family_GPS_Tracker_Api.Contracts.V1.RequestDtos;
using Family_GPS_Tracker_Api.Contracts.V1.ResponseDtos;
using Microsoft.AspNetCore.Mvc;
using System;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Family_GPS_Tracker_Api.Contracts;
using System.Threading.Tasks;
using Family_GPS_Tracker_Api.Contracts.V1.DTOs.Responses;
using AutoMapper;
using Family_GPS_Tracker_Api.Contracts.V1.DTOs.Requests;

namespace Family_GPS_Tracker_Api.V1.Controllers
{
	[ApiController]
	//[Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
	public class ParentController : ControllerBase
	{
		private readonly IParentRepository _parentRepository;
		private readonly IChildRepository _childRepository;
		private readonly IMapper _mapper;
		public ParentController(IParentRepository parentRepository, IMapper mapper, IChildRepository childRepository)
		{
			_parentRepository = parentRepository;
			_mapper = mapper;
			_childRepository = childRepository;
		}

		[HttpGet(ApiRoutes.Parent.Get)]
		public async Task<ActionResult<ParentResponse>> GetParentById([FromRoute(Name = "parent-id")] Guid parentId)
		{
			Parent parent = await _parentRepository.GetParentByIdAsync(parentId);
			if (parent is null)
			{
				return NotFound(new Response<ParentResponse>
				{

					StatusCode = "404",
					Message = "User doesn't exist with this userId"

				});
			}

			return Ok(new Response<ParentResponse>
			{

				StatusCode = "404",
				Message = "User doesn't exist with this userId",
				Data = _mapper.Map<ParentResponse>(parent)

			});
		}

		[HttpGet(ApiRoutes.Parent.GetDetails)]
		public async Task<ActionResult<Response<ParentDetailResponse>>> GetParentDetailsById([FromRoute(Name = "parent-id")] Guid parentId)
		{
			Parent parent = await _parentRepository.GetParentDetailsByIdAsync(parentId);
			if (parent == null)
			{
				return NotFound(new Response<ParentDetailResponse>
				{

					StatusCode = "404",
					Message = "User doesn't exist with this userId"

				});
			}

			return Ok(new Response<ParentDetailResponse>
			{

				StatusCode = "404",
				Message = "User doesn't exist with this userId",
				Data = _mapper.Map<ParentDetailResponse>(parent)

			});
		}



		[HttpPut(ApiRoutes.Parent.UpdateToken)]
		public async Task<ActionResult<Response<DeviceTokenResponse>>> UpdateDeviceToken([FromRoute(Name = "parent-id")] Guid parentId, [FromBody] UpdateDeviceTokenRequest updateDeviceTokenRequest)
		{
			var parent = await _parentRepository.GetParentByIdAsync(parentId);
			if (parent == null)
			{
				return NotFound(new Response<DeviceTokenResponse>
				{

					StatusCode = "404",
					Message = "User doesn't exist with this userId"

				});
			}

			if (!parentId.ToString().Equals(HttpContext.GetUserId()))
			{

				return StatusCode(403, new Response<DeviceTokenResponse>
				{

					StatusCode = "403",
					Message = "You cannot perform this operation"

				});
			}

			var deviceToken = new DeviceToken
			{
				Token = updateDeviceTokenRequest.Token
			};

			var isDeviceTokenUpdated = await _parentRepository.UpdateDeviceTokenAsync(parent, deviceToken);

			if (!isDeviceTokenUpdated)
			{
				return StatusCode(500, new Response<DeviceTokenResponse>
				{

					StatusCode = "500",
					Message = "Token couldn't be updated."

				});
			}

			return Ok(new Response<DeviceTokenResponse>
			{
				IsSuccess = true,
				StatusCode = "200",
				Message = "Token has been updated.",
				Data = _mapper.Map<DeviceTokenResponse>(deviceToken)

			});
			
		}

		[HttpPut(ApiRoutes.Parent.LinkChild)]
		public async Task<ActionResult<Response<ParentDetailResponse>>> LinkChildAsync([FromRoute(Name = "parent-id")] Guid parentId, [FromBody] PairingCodeRequest pairingCodeRequest)
		{
			var parent = await _parentRepository.GetParentByIdAsync(parentId);
			if (parent == null)
			{
				return NotFound(new Response<ParentDetailResponse>
				{

					StatusCode = "404",
					Message = "User doesn't exist with this userId"

				});
			}

			if (!parentId.ToString().Equals(HttpContext.GetUserId()))
			{

				return StatusCode(403, new Response<ParentDetailResponse>
				{

					StatusCode = "403",
					Message = "You cannot perform this operation"

				});
			}

			var pairingCode = await _childRepository.GetPairingCodeAsyncByCode(pairingCodeRequest.Code);

			if (pairingCode == null)
			{

				return NotFound(new Response<ParentDetailResponse>
				{

					StatusCode = "404",
					Message = "This pairing code doesnt exist."

				});
			}

			if (pairingCode.IsUsed)
			{

				return BadRequest(new Response<ParentDetailResponse>
				{

					StatusCode = "400",
					Message = "Pairing code has already been used."

				});

			}

			pairingCode.IsUsed = true;

			var isChildLinked = await _parentRepository.LinkChildAsync(parent, pairingCode.Child);

			if (!isChildLinked)
			{
				return StatusCode(500, new Response<ParentDetailResponse>
				{

					StatusCode = "500",
					Message = "Child couldn't be linked."

				});
			}

			var parentDetail = await _parentRepository.GetParentDetailsByIdAsync(parentId);
			
			return Ok(new Response<ParentDetailResponse>
			{
				IsSuccess = true,
				StatusCode = "200",
				Message = "Child has been linked.",
				Data = _mapper.Map<ParentDetailResponse>(parentDetail)

			});
		}
	}
}
