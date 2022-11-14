using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Family_GPS_Tracker_Api.Contracts.V1.Responses
{
	public class AuthSuccessResponse
	{
		public string Token { get; set; }
		public string RefreshToken { get; set; }
	}
}
