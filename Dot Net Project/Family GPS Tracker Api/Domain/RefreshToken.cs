using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;
using static Family_GPS_Tracker_Api.Domain.IdentityModels;

namespace Family_GPS_Tracker_Api.Domain
{
	public class RefreshToken
	{
		[Key]
		public string Token { get; set; }
		public string JwtId { get; set; }
		public DateTime CreationDate { get; set; }
		public DateTime ExpiryDate { get; set; }
		public bool IsUsed { get; set; }
		public bool IsInvalidated { get; set; }
		public Guid UserId { get; set; }

		[ForeignKey(nameof(UserId))]
		public ApplicationUser User { get; set; }


	}
}
