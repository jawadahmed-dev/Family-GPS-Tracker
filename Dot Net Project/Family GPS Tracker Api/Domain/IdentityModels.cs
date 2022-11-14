using Microsoft.AspNetCore.Identity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Family_GPS_Tracker_Api.Domain
{
	public class IdentityModels
	{
		public class ApplicationUserToken : IdentityUserToken<Guid> { }
		public class ApplicationUserLogin : IdentityUserLogin<Guid> { }
		public class ApplicationRoleClaim : IdentityRoleClaim<Guid> { }
		public class ApplicationUserRole : IdentityUserRole<Guid> {
		
			public virtual ApplicationUser User { get; set; }
			public virtual ApplicationRole Role { get; set; }
		}
		public class ApplicationUser : IdentityUser<Guid> {
			public virtual Parent Parent { get; set; }
			public virtual Child Child { get; set; }
			public virtual IEnumerable<ApplicationUserRole> UserRoles { get; set; }
		}
		public class ApplicationUserClaim : IdentityUserClaim<Guid> { }
		public class ApplicationRole : IdentityRole<Guid> {

			public virtual IEnumerable<ApplicationUserRole> UserRoles { get; set; }
			public ApplicationRole() : base()
			{
			}

			public ApplicationRole(string roleName) : base(roleName)
			{
			}
		}
	}
}
