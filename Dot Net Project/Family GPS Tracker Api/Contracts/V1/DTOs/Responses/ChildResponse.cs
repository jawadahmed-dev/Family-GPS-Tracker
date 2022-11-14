using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Family_GPS_Tracker_Api.Contracts.V1.ResponseDtos
{
	public class ChildResponse
	{
		public Guid ChildId { get; set; }
		public string Name { get; set; }
		public string Email { get; set; }
		public Guid? parentId { get; set; }
		public IEnumerable<String> Roles { get; set; }
	}
}
