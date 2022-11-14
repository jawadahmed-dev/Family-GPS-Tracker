using System;
using System.Collections.Generic;

#nullable disable

namespace Family_GPS_Tracker_Api.Domain
{
    public partial class Location
    {
        public Guid LocationId { get; set; }
        public string Time { get; set; }
        public Guid ChildId { get; set; }
        public int UniqueNumber { get; set; }
        public double Latitude { get; set; }
        public double Longitude { get; set; }

        public virtual Child Child { get; set; }
    }
}
