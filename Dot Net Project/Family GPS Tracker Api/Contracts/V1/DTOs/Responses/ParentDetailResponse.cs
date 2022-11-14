using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Family_GPS_Tracker_Api.Contracts.V1.ResponseDtos
{
	public class ParentDetailResponse
	{
		
		public Guid ParentId { get; set; }
		public string Name { get; set; }
		public string Email { get; set; }
		public string PhoneNumber { get; set; }
		public IEnumerable<String> Roles { get; set; }
		public IEnumerable<ChildResponse> Children { get; set; }
	}
}
