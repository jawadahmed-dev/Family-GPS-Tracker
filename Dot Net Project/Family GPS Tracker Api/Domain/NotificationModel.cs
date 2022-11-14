using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Family_GPS_Tracker_Api.Domain
{
        public class NotificationModel
        {
            [JsonProperty("childId")]
            public Guid ChildId { get; set; }
            [JsonProperty("parentId")]
            public Guid ParentId { get; set; }
            [JsonProperty("deviceToken")]
            public string DeviceToken { get; set; }
            [JsonProperty("isAndroiodDevice")]
            public bool IsAndroiodDevice { get; set; }
            [JsonProperty("title")]
            public string Title { get; set; }
            [JsonProperty("body")]
            public string Body { get; set; }
        }
    }

