using System;
using System.Collections.Generic;

#nullable disable

namespace Family_GPS_Tracker_Api.Domain
{
    public partial class Notification
    {
        public Guid NotificationId { get; set; }
        public string Title { get; set; }
        public string Message { get; set; }
        public string CreatedAt { get; set; }
        public Guid ChildId { get; set; }
        public Guid ParentId { get; set; }

        public virtual Child Child { get; set; }
        public virtual Parent Parent { get; set; }
    }
}
