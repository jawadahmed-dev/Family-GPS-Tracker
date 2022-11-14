using Family_GPS_Tracker_Api.Domain;
using System.Threading.Tasks;
using static Family_GPS_Tracker_Api.Domain.IdentityModels;

namespace Family_GPS_Tracker_Api.Repositories
{
	public interface IIdentityRepository
	{
		Task<AuthResult>  RegisterParentAsync(ApplicationUser user, string password);
		Task<AuthResult> RegisterChildAsync(ApplicationUser user, string password);
		Task<AuthResult> LoginAsync(string email, string password);
		Task<AuthResult> RefreshTokenAsync(string token, string refreshToken);
	}
}