
using Family_GPS_Tracker_Api.Domain;
using Family_GPS_Tracker_Api.Repositories;
using Microsoft.AspNetCore.Mvc;
using Family_GPS_Tracker_Api.Contracts.V1.RequestDtos;
using Family_GPS_Tracker_Api.Contracts.V1.ResponseDtos;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Family_GPS_Tracker_Api.Contracts;
using AutoMapper;
using Family_GPS_Tracker_Api.Contracts.V1.DTOs.Responses;

namespace Family_GPS_Tracker_Api.V1.Controllers
{
	[ApiController]
	public class ChildController : ControllerBase
	{

		private IChildRepository _childRepository;
		private IMapper _mapper;
		public ChildController(IChildRepository childRepository, IMapper mapper)
		{
			_childRepository = childRepository;
			_mapper = mapper;
		}

		[HttpGet(ApiRoutes.Child.Get)]
		public async Task<ActionResult<Response<ChildResponse>>> GetChildByIdAsync([FromRoute(Name = "child-id")] Guid childId)
		{
			Child child = await _childRepository.GetChildByIdAsync(childId);
			if (child is null)
			{
				return NotFound(new Response<ChildResponse>
				{

					StatusCode = "404",
					Message = "User doesnt exist with this userId"
				});
			}
			return Ok(new Response<ChildResponse>
			{

				StatusCode = "200",
				Message = "This operation was successfull",
				Data = _mapper.Map<ChildResponse>(child)
			});
		}

		[HttpGet(ApiRoutes.Child.GetDetails)]
		public async Task<ActionResult<Response<ChildDetailResponse>>> GetChildDetailsByIdAsync([FromRoute(Name = "child-id")] Guid childId)
		{
			Child child = await _childRepository.GetChildDetailsByIdAsync(childId);
			if (child is null)
			{
				return NotFound(new Response<ChildDetailResponse> {
					StatusCode = "404",
					Message = "User doesnt exist with this userId",

				});
			}

			return Ok(new Response<ChildDetailResponse>
			{
				StatusCode = "200",
				Message = "This operation was successfull",
				Data = _mapper.Map<ChildDetailResponse>(child)
			});
		}

		[HttpGet(ApiRoutes.Child.GetPairingCode)]
		public async Task<ActionResult<Response<PairingCodeResponse>>> GetPairingCodeAsync([FromRoute(Name = "child-id")] Guid childId)
		{
			var existingPairingCode = await _childRepository.GetPairingCodeAsyncByChildId(childId);

			if (existingPairingCode == null)
			{

				var newPairingCode = new PairingCode
				{
					ChildId = childId,
					Code = new Random().Next(10000, 99999).ToString(),
					CreationDate = DateTime.UtcNow,
					ExpiryDate = DateTime.UtcNow.AddHours(2)
				};

				var isCreated = await _childRepository.CreatePairingCodeAsync(newPairingCode);

				if (isCreated)
				{

					return Ok(new Response<PairingCodeResponse>
					{

						StatusCode = "200",
						Message = $"This is pairing Code is only valid till {newPairingCode.ExpiryDate}",
						Data = _mapper.Map<PairingCodeResponse>(newPairingCode)
					});

				}
			}

			else if (existingPairingCode != null)
			{
				var updatedPairingCode = existingPairingCode;
				updatedPairingCode.Code = new Random().Next(10000, 99999).ToString();
				updatedPairingCode.CreationDate = DateTime.UtcNow;
				updatedPairingCode.ExpiryDate = DateTime.UtcNow.AddHours(2);
				updatedPairingCode.IsUsed = false;

				var isUpdated = await _childRepository.UpdatePairingCodeAsync(updatedPairingCode);

				if (isUpdated)
				{
					return Ok(new Response<PairingCodeResponse>
					{

						StatusCode = "200",
						Message = $"This is pairing Code is only valid till {updatedPairingCode.ExpiryDate}",
						Data = _mapper.Map<PairingCodeResponse>(updatedPairingCode)

					});
				}
			}

			return StatusCode(500, new Response<PairingCodeResponse>
			{

				StatusCode = "500",
				Message = $"This is pairing Code couldn't be generated."

			});

		}
	}
}

