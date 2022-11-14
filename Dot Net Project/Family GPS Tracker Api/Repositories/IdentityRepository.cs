using Family_GPS_Tracker_Api.Domain;
using Family_GPS_Tracker_Api.Domain;
using Family_GPS_Tracker_Api.Options;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using System;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;
using static Family_GPS_Tracker_Api.Domain.IdentityModels;

namespace Family_GPS_Tracker_Api.Repositories
{
	public class IdentityRepository : IIdentityRepository
	{
		private readonly UserManager<ApplicationUser> _userManager;
		private readonly RoleManager<ApplicationRole> _roleManager;
		private readonly JwtOptions _jwtOptions;
		private readonly TokenValidationParameters _tokenValidationParameters;
		private readonly AppDbContext _db;
		private readonly IParentRepository _parentRepository;
		private readonly IChildRepository _childRepository;



		public IdentityRepository(UserManager<ApplicationUser> userManager, JwtOptions jwtOptions, TokenValidationParameters tokenValidationParameters, AppDbContext db, RoleManager<ApplicationRole> roleManager, IParentRepository parentRepository, IChildRepository childRepository)
		{
			_userManager = userManager;
			_jwtOptions = jwtOptions;
			_tokenValidationParameters = tokenValidationParameters;
			_db = db;
			_roleManager = roleManager;
			_parentRepository = parentRepository;
			_childRepository = childRepository;
		}

		public async Task<AuthResult> LoginAsync(string email, string password)
		{
			var user = await _userManager.FindByEmailAsync(email);

			if (user == null) {
				return new AuthResult { 
					Errors = new [] { "User doesn't exist with this email." }
				};
			}

			var userHasValidPassword = await _userManager.CheckPasswordAsync(user, password);

			if (!userHasValidPassword) {
				return new AuthResult
				{
					Errors = new[] { "User/Password combination is wrong." }
				};
			}

			return await GenerateAuthenticationResultAsync(user);
		}

		public async Task<AuthResult> RefreshTokenAsync(string token, string refreshToken)
		{
			var validatedToken = GetPrincipalFromToken(token);

			if (validatedToken == null) {
				return new AuthResult
				{
					Errors = new[] { "Jwt token is invalid." }
				};
			}

			var jti = validatedToken.Claims.Single(x => x.Type == JwtRegisteredClaimNames.Jti).Value;

			var storedRefreshToken = await _db.RefreshTokens.SingleOrDefaultAsync(x => x.Token == refreshToken);

			if (storedRefreshToken == null) {
				return new AuthResult
				{
					Errors = new[] { "This Refresh token doesn't exist." }
				};
			}

			if (storedRefreshToken.JwtId != jti) {
				return new AuthResult
				{
					Errors = new[] { "This Refresh token doesn't match with Jwt." }
				};
			}

			if (storedRefreshToken.IsInvalidated)
			{
				return new AuthResult
				{
					Errors = new[] { "This Refresh token has been invalidated." }
				};
			}

			if (storedRefreshToken.IsUsed)
			{
				return new AuthResult
				{
					Errors = new[] { "This Refresh token has already been used." }
				};
			}

			storedRefreshToken.IsUsed = true;
			_db.RefreshTokens.Update(storedRefreshToken);
			await _db.SaveChangesAsync();

			var user = await _userManager.FindByIdAsync(validatedToken.Claims.Single(x => x.Type == "userId").Value);
			return await GenerateAuthenticationResultAsync(user);

		}

		private ClaimsPrincipal GetPrincipalFromToken(string token)
		{
			var tokenHandler = new JwtSecurityTokenHandler();

			try
			{
				var principal = tokenHandler.ValidateToken(token, _tokenValidationParameters, out var validateToken);
				if (!IsJwtWithValidSecurityAlgorithim(validateToken))
				{
					return null;
				}
				return principal;
			}
			catch(Exception e) {
				var error = e.Message.ToString();
				return null;
			}
		}

		private bool IsJwtWithValidSecurityAlgorithim(SecurityToken validateToken)
		{
			return (validateToken is JwtSecurityToken jwtSecurityToken) &&
				jwtSecurityToken.Header.Alg.Equals(SecurityAlgorithms.HmacSha256,
				StringComparison.InvariantCultureIgnoreCase);
		}

		// Method for Child Registration
		public async Task<AuthResult> RegisterChildAsync(ApplicationUser user, string password)
		{
			// Checking if user exists with this credentials

			var existingUser = await _userManager.FindByEmailAsync(user.Email);

			if (existingUser != null)
			{

				return new AuthResult
				{
					Errors = new[] { "User with this email address already exists." }
				};
			}

			// Registering the user and checking if registration went successfully

			var createdUser = await _userManager.CreateAsync(user, password);

			if (!createdUser.Succeeded)
			{

				return new AuthResult
				{
					Errors = createdUser.Errors.Select(x => x.Description)
				};
			}

			// Assigning user roles

			await AddUserToRolesAsync(user, UserRoles.Child);

			// Creating Child corresponding to that user

			Child child = new Child
			{
				User = user
			};

			var isChildCreated = await _childRepository.CreateChildAsync(child);

			if (!isChildCreated)
			{
				return new AuthResult
				{
					Errors = new[] { "Couldn't register user as a child" }
				};
			}

			// Returning token on successful registration

			return await GenerateAuthenticationResultAsync(user);


		}

		// Method for Parent Registration
		public async Task<AuthResult> RegisterParentAsync(ApplicationUser user, string password)
		{
			// Checking if user exists with this credentials

			var existingUser = await _userManager.FindByEmailAsync(user.Email);

			if (existingUser != null) {

				return new AuthResult
				{
					Errors = new[] { "User with this email address already exists." }
				};
			}

			// Registering the user and checking if registration went successfully

			var createdUser = await _userManager.CreateAsync(user, password);

			if (!createdUser.Succeeded) {

				return new AuthResult
				{
					Errors = createdUser.Errors.Select(x => x.Description)
				};
			}

			// Assigning user roles

			await AddUserToRolesAsync(user,UserRoles.Parent);

			// Creating Parent corresponding to this user

			Parent parent = new Parent
			{
				DeviceToken = "empty",
				User = user
			};

			var isParentCreated = await _parentRepository.CreateParentAsync(parent);

			if (!isParentCreated)
			{
				return new AuthResult
				{
					Errors = new[] { "Couldn't register user as a parent" }
				};
			}

			// Returning token on successful registration

			return await GenerateAuthenticationResultAsync(user);

		}


		private async Task AddUserToRolesAsync(ApplicationUser user, string role)
		{
			if (!await _roleManager.RoleExistsAsync(UserRoles.Parent)) {
				await _roleManager.CreateAsync(new ApplicationRole(UserRoles.Parent));
			}

			if (!await _roleManager.RoleExistsAsync(UserRoles.Child))
			{
				await _roleManager.CreateAsync(new ApplicationRole(UserRoles.Child));
			}

			await _userManager.AddToRoleAsync(user, role);


		}

		private async Task<AuthResult> GenerateAuthenticationResultAsync(ApplicationUser user)
		{

			DateTime.UtcNow.Add(_jwtOptions.TokenLifetime);
			var tokenHandler = new JwtSecurityTokenHandler();
			var key = Encoding.ASCII.GetBytes(_jwtOptions.Secret);
			var tokenDescriptor = new SecurityTokenDescriptor
			{
				Subject = new ClaimsIdentity(new[] {
					new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString()),
					new Claim(JwtRegisteredClaimNames.Email, user.Email),
					new Claim("userId", user.Id.ToString())
				}),
				Expires = DateTime.UtcNow.Add(_jwtOptions.TokenLifetime),
				SigningCredentials = new SigningCredentials(
					new SymmetricSecurityKey(key),
					SecurityAlgorithms.HmacSha256Signature)
			};

			var token = tokenHandler.CreateToken(tokenDescriptor);

			// Creating a new Refresh Token and Storing it to database

			var refreshToken = new RefreshToken
			{
				Token = Guid.NewGuid().ToString(),
				JwtId = token.Id,
				UserId = user.Id,
				CreationDate = DateTime.UtcNow,
				ExpiryDate = DateTime.UtcNow.AddMonths(6)
			};

			await _db.RefreshTokens.AddAsync(refreshToken);
			await _db.SaveChangesAsync();

			return new AuthResult
			{
				IsSuccess = true,
				Token = tokenHandler.WriteToken(token),
				RefreshToken = refreshToken.Token
			};
		}

		
	}
}