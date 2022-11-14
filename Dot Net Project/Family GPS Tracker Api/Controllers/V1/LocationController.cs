using Family_GPS_Tracker_Api.Domain;
using Family_GPS_Tracker_Api.Repositories;
using Family_GPS_Tracker_Api.Contracts.V1.RequestDtos;
using Family_GPS_Tracker_Api.Contracts.V1.ResponseDtos;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Family_GPS_Tracker_Api.Contracts;
using Family_GPS_Tracker_Api.Contracts.V1.DTOs.Responses;
using AutoMapper;

namespace Family_GPS_Tracker_Api.Controllers
{
	
	[ApiController]
	public class LocationController : ControllerBase
	{
		private readonly ILocationRepository _locationRepository;
		private readonly IChildRepository _childRepository;
		private readonly IMapper _mapper;

		public LocationController(ILocationRepository locationRepository,
			IChildRepository childRepository, IMapper mapper)
		{
			_locationRepository = locationRepository;
			_childRepository = childRepository;
			_mapper = mapper;
		}


		[HttpPost(ApiRoutes.Location.CreateLocation)]
		public async Task<ActionResult<Response<LocationDto>>> CreateLocationAsync([FromRoute(Name = "child-id")] Guid childId, [FromBody]CreateLocationDto createLocationDto)
		{

			var child = await _childRepository.GetChildByIdAsync(childId);

			if (child == null)
			{
				return NotFound(new Response<LocationDto>
				{

					StatusCode = "404",
					Message = "Child doesnt exist with this userId"
				});
			}

			if (!childId.ToString().Equals(HttpContext.GetUserId()))
			{

				return StatusCode(403, new Response<LocationDto>
				{

					StatusCode = "403",
					Message = "You cannot perform this operation"

				});
			}

			var location = new Location
			{
				LocationId = Guid.NewGuid(),
				Latitude = createLocationDto.Latitude,
				Longitude = createLocationDto.Longitude,
				Time = DateTime.Now.ToString("yyyy-MM-dd hh:mm"),
				ChildId = childId
			};

			var isLocationCreated = await _locationRepository.CreateLocationAsync(location);

			if (!isLocationCreated) {

				return StatusCode(500, new Response<LocationDto>
				{

					StatusCode = "500",
					Message = "Location couldn't be registered."

				});

			}

			return CreatedAtAction(nameof(GetLocationByChildIdAsync), new { childId = child.ChildId }, new Response<LocationDto>
			{
				StatusCode = "200",
				IsSuccess = true,
				Message = "Location Created successfuly",
				Data = _mapper.Map<LocationDto>(location)
			});

		}

		[HttpGet(ApiRoutes.Location.GetLocationByChildId)]
		public async Task<ActionResult<Response<LocationDto>>> GetLocationByChildIdAsync([FromRoute(Name = "child-id")] Guid childId)
		{
			var location = await _locationRepository.GetLocationByChildIdAsync(childId);

			if (location == null)
			{
				return NotFound(new Response<LocationDto>
				{

					StatusCode = "404",
					Message = "Location doesnt exist with this childId"
				});
			}

			return Ok(new Response<LocationDto> { 
				StatusCode = "200",
				IsSuccess = true,
				Message = "Location fetched successfuly",
				Data = _mapper.Map<LocationDto>(location)
			});
		}

		[HttpGet(ApiRoutes.Location.GetLastLocation)]
		public async Task<ActionResult<Response<LocationDto>>> GetLastLocationByChildIdAsync([FromRoute(Name = "child-id")]Guid childId)
		{
			var lastLocation = await _locationRepository.GetLastLocationByChildIdAsync(childId);
			
			if (lastLocation == null)
			{
				return NotFound(new Response<LocationDto>
				{

					StatusCode = "404",
					Message = "Location doesnt exist with this childId"
				});
			}

			return Ok(new Response<LocationDto>
			{
				StatusCode = "200",
				IsSuccess = true,
				Message = "Last location fetched successfuly",
				Data = _mapper.Map<LocationDto>(lastLocation)
			});
		}

		[HttpGet(ApiRoutes.Location.GetLastTenLocations)]
		public async Task<ActionResult<Response<IEnumerable<LocationDto>>>> GetLastTenLocationsByChildIdAsync([FromRoute(Name = "child-id")] Guid childId)
		{

			var lastTenLocations = await _locationRepository.GetLastTenLocationsByChildIdAsync(childId);

			if (lastTenLocations == null)
			{
				return NotFound(new Response<IEnumerable<LocationDto>>
				{

					StatusCode = "404",
					Message = "Location doesnt exist with this childId"
				});
			}

			return Ok(new Response<IEnumerable<LocationDto>>
			{

				StatusCode = "404",
				Message = "Location doesnt exist with this childId",
				IsSuccess = true,
				Data= _mapper.Map<IEnumerable<LocationDto>>(lastTenLocations)
			}); 
		}
	}
}
