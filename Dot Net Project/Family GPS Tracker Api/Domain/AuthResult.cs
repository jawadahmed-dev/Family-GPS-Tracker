using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Family_GPS_Tracker_Api.Domain
{
	public class AuthResult
	{
		public string Token { get; set; }
		public string RefreshToken { get; set; }
		public bool IsSuccess { get; set; }
		public IEnumerable<string> Errors { get; set; }
	}
}
