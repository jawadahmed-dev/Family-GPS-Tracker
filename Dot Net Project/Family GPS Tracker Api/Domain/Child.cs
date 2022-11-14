using Family_GPS_Tracker_Api.Domain;
using Microsoft.AspNetCore.Identity;
using System;
using System.Collections.Generic;
using static Family_GPS_Tracker_Api.Domain.IdentityModels;

#nullable disable

namespace Family_GPS_Tracker_Api.Domain
{
    public partial class Child
    {
        public Guid? ParentId { get; set; }
        public Guid ChildId { get; set; }
        public virtual Parent Parent { get; set; }
        public virtual ICollection<Geofence> Geofences { get; set; }
        public virtual ICollection<Location> Locations { get; set; }
        public virtual ICollection<Notification> Notifications { get; set; }
        public virtual ApplicationUser User { get; set; }
        public virtual PairingCode PairingCode { get; set; }
    }
}
