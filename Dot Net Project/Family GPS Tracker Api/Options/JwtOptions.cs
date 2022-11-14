using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Family_GPS_Tracker_Api.Options
{
	public class JwtOptions
	{
		public String Secret { get; set; }
		public TimeSpan TokenLifetime { get; set; }
	}
}
