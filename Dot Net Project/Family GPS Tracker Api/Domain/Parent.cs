using Microsoft.AspNetCore.Identity;
using System;
using System.Collections.Generic;
using static Family_GPS_Tracker_Api.Domain.IdentityModels;

#nullable disable

namespace Family_GPS_Tracker_Api.Domain
{
    public partial class Parent
    {
        

        public Guid ParentId { get; set; }
        public string DeviceToken { get; set; }
        public virtual ICollection<Child> Children { get; set; }
        public virtual ICollection<Notification> Notifications { get; set; }
        public virtual ApplicationUser User { get; set; }
        

    }
}
