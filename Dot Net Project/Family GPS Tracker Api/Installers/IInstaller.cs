using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Family_GPS_Tracker_Api.Installers
{
	public interface IInstaller
	{
		void installServices(IServiceCollection services, IConfiguration Configuration);
	}
}
