using Family_GPS_Tracker_Api.Domain;
using Family_GPS_Tracker_Api.Domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Family_GPS_Tracker_Api.Repositories
{
	public interface IChildRepository
	{
		public Task<bool> CreateChildAsync(Child child);
		public Task<Child> GetChildByIdAsync(Guid childId);
		public Task<Child> GetChildDetailsByIdAsync(Guid childId);
		public Task<bool> UpdatePairingCodeAsync(PairingCode pairingCode);
		public Task<bool> CreatePairingCodeAsync(PairingCode pairingCode);
		public Task<PairingCode> GetPairingCodeAsyncByChildId(Guid childId);
		public Task<PairingCode> GetPairingCodeAsyncByCode(string pairingCode);

	}
}
