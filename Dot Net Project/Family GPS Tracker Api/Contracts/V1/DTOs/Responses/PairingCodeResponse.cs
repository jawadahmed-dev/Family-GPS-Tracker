using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Family_GPS_Tracker_Api.Contracts.V1.ResponseDtos
{
	public class PairingCodeResponse
	{
		public Guid PairingCodeId { get; set; }
		public string Code { get; set; }
		public DateTime CreationDate { get; set; }
		public DateTime ExpiryDate { get; set; }
		public bool IsUsed { get; set; }
	}
}
