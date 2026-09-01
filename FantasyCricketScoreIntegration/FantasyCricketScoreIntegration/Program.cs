using System;
using System.Net;
using System.Timers;
using System.Configuration;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Collections.Generic;
using System.Linq;
using System.ServiceProcess;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using FantasyCricketScoreIntegration.Models;
using FantasyCricket.Score.Repository;
using Topshelf;

namespace FantasyCricket.Score.Integration
{
    class Program
    {
        static void Main()
        {
            // Initialize log4net.
            log4net.Config.XmlConfigurator.Configure();

            //var data = new ScoreIntegartion();
            var rc = HostFactory.Run(configure =>
            {
                configure.Service<ScoreIntegartion>(service =>
                {
                    service.ConstructUsing(name => new ScoreIntegartion());
                    service.WhenStarted(tc => tc.Start());
                    service.WhenStopped(tc => tc.Stop());
                });
                configure.RunAsLocalSystem();

                configure.SetDescription("ScoreIntegartionTopShelf");
                configure.SetDisplayName("FCScoreIntegration");
                configure.SetServiceName("FCScoreIntegration");
            });

            var exitCode = (int)Convert.ChangeType(rc, rc.GetTypeCode());
            Environment.ExitCode = exitCode;

        }

    }
    
}