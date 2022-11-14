using AutoMapper;
using Family_GPS_Tracker_Api.Contracts;
using Family_GPS_Tracker_Api.Contracts.V1.DTOs.Requests;
using Family_GPS_Tracker_Api.Contracts.V1.RequestDtos;
using Family_GPS_Tracker_Api.Contracts.V1.Requests;
using Family_GPS_Tracker_Api.Contracts.V1.Responses;
using Family_GPS_Tracker_Api.Domain;
using Family_GPS_Tracker_Api.Repositories;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using static Family_GPS_Tracker_Api.Domain.IdentityModels;

namespace Family_GPS_Tracker_Api.V1.Controllers
{
	[ApiController]
	public class IdentityController : ControllerBase
	{

		private readonly IIdentityRepository _identityRepository;
		private readonly IParentRepository _parentRepository;
		private readonly IMapper _mapper;
		public IdentityController(IIdentityRepository identityRepository, IParentRepository parentRepository, IMapper mapper)
		{
			_identityRepository = identityRepository;
			_parentRepository = parentRepository;
			_mapper = mapper;
		}

		// Handling Registration Request

		[HttpPost(ApiRoutes.Identity.RegisterParent)]
		public async Task<IActionResult> RegisterParent([FromBody] CreateParentDto createParentDto) {

			// Creating new Identity user

			var applicationUser = new ApplicationUser
			{
				UserName = createParentDto.Name,
				Email = createParentDto.Email,
				PhoneNumber = createParentDto.PhoneNumber
			};

			// Invoking RegisterParentAsync for registering parent

			var authResult = await _identityRepository.RegisterParentAsync(
				applicationUser, createParentDto.Password);

			// Checking whether the action was successful

			if (!authResult.IsSuccess) {
				return BadRequest(_mapper.Map<AuthFailedResponse>(authResult));
			}
		
			// Returning Token on successfull registration

			return Ok(_mapper.Map<AuthSuccessResponse>(authResult));
		}


		[HttpPost(ApiRoutes.Identity.RegisterChild)]
		public async Task<IActionResult> RegisterChild([FromBody] CreateChildDto createChildDto)
		{

			// Invoking RegisterParentAsync for registering parent

			var authResult = await _identityRepository.RegisterChildAsync(
				new ApplicationUser
				{
					UserName = createChildDto.Name,
					Email = createChildDto.Email
				}, createChildDto.Password);

			// Checking whether the action was successful

			if (!authResult.IsSuccess)
			{
				return BadRequest(_mapper.Map<AuthFailedResponse>(authResult));
			}

			// Returning Token on successfull registration

			return Ok(_mapper.Map<AuthSuccessResponse>(authResult));
		}

		// Handling Login Request

		[HttpPost(ApiRoutes.Identity.Login)]
		public async Task<IActionResult> Login([FromBody] LoginRequest loginRequest)
		{
			// Checking if user exists


			// Invoking LoginAsync for signing in the user

			var authResult = await _identityRepository.LoginAsync(
				loginRequest.Email,
				loginRequest.Password);	

			// Checking whether the action was successful

			if (!authResult.IsSuccess)
			{
				return BadRequest(_mapper.Map<AuthFailedResponse>(authResult));
			}

			// Returning Token on successfull registration

			return Ok(_mapper.Map<AuthSuccessResponse>(authResult));
		}

		// Handling Refresh Token Request

		[HttpPost(ApiRoutes.Identity.RefreshToken)]
		public async Task<IActionResult> RefreshToken([FromBody] RefreshTokenRequest refreshTokenRequest)
		{
			// Checking if user exists

			// Invoking LoginAsync for signing in the user

			var authResult = await _identityRepository.RefreshTokenAsync(
				refreshTokenRequest.Token,
				refreshTokenRequest.RefreshToken);

			// Checking whether the action was successful

			if (!authResult.IsSuccess)
			{
				return BadRequest(_mapper.Map<AuthFailedResponse>(authResult));
			}

			// Returning Token on successfull registration

			return Ok(_mapper.Map<AuthSuccessResponse>(authResult));
		}

	}
}
