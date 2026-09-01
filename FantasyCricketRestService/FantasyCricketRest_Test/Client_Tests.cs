using System;
using System.IO;
using System.Web;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using ClientAppRest.Controllers;
using ClientAppRest.Models;
using LPL.UI.REST.Core.Models;
using System.Web.Hosting;

namespace ClientAppRest_Test
{
    [TestClass]
    public class Client_Tests
    {
        // NOTE: For all test methods:  ** always remember to set Controller.IsTestCase = true!!!

        [TestMethod]
        public void Test_ActivateService_Success()
        {
            JsonResponse TestResponse = null;
            ClientController TestController = null;
            SimpleWorkerRequest request = null;
            HttpContext context = null;
            BnUserModel user = null;

            TestController = new ClientController();
            request = new SimpleWorkerRequest(string.Empty, string.Empty, string.Empty, null, new StringWriter());
            context = new HttpContext(request);
            user = new BnUserModel
            {
                FirmID = 1,
                AccountActive = true,
                UserName = "michael.hatch",
                RepID = "T2XA",
                OfficeID = "T2XA",
                CompanyID = "1",
                ProfileID = 2,
                AccountType = "R"
            };
            context.Items["lpl:BranchNet:Auth:BnUser"] = user;
            HttpContext.Current = context;

            TestController.IsTestCase = true; // always remember to set Controller.IsTestCase = true!!!
            TestResponse = TestController.ActivateService();

            Assert.AreEqual("success", TestResponse.Status);
        }

        [TestMethod]
        public void Test_CreateEntityClient_Success()
        {
            JsonResponse TestResponse = null;
            ClientController TestController = null;
            SimpleWorkerRequest request = null;
            HttpContext context = null;
            BnUserModel user = null;
            ClientModel TestClient = null;

            TestController = new ClientController();
            request = new SimpleWorkerRequest(string.Empty, string.Empty, string.Empty, null, new StringWriter());
            context = new HttpContext(request);
            user = new BnUserModel
            {
                FirmID = 1,
                AccountActive = true,
                UserName = "michael.hatch",
                RepID = "T2XA",
                OfficeID = "T2XA",
                CompanyID = "1",
                ProfileID = 2,
                AccountType = "R"
            };
            context.Items["lpl:BranchNet:Auth:BnUser"] = user;
            HttpContext.Current = context;

            TestController.IsTestCase = true; // always remember to set Controller.IsTestCase = true!!!
            TestClient = new ClientModel();
            // Required Entity Fields: Client.RepID,Client.Entity,Client.ProspectSource,Client.Username,Client.TenantID,Client.ClientInd == 2
            // Required Individual Fields: Client.RepID,Client.LName,Client.FName,Client.ProspectSource,Client.Username,Client.TenantID,Client.ClientInd == 1
            TestClient.RepId = "T2XA";
            TestClient.Entity = "Test Client Corporation";
            TestClient.ProspectSource = "IFS";
            TestClient.UserName = "MSUnit.Test";
            TestClient.TenantId = 1;
            TestClient.ClientInd = 2;

            TestResponse = TestController.CreateClient(TestClient);

            Assert.AreEqual("success", TestResponse.Status);
        }

        [TestMethod]
        public void Test_CreateIndividualClient_Success()
        {
            JsonResponse TestResponse = null;
            ClientController TestController = null;
            SimpleWorkerRequest request = null;
            HttpContext context = null;
            BnUserModel user = null;
            ClientModel TestClient = null;

            TestController = new ClientController();
            request = new SimpleWorkerRequest(string.Empty, string.Empty, string.Empty, null, new StringWriter());
            context = new HttpContext(request);
            user = new BnUserModel
            {
                FirmID = 1,
                AccountActive = true,
                UserName = "michael.hatch",
                RepID = "T2XA",
                OfficeID = "T2XA",
                CompanyID = "1",
                ProfileID = 2,
                AccountType = "R"
            };
            context.Items["lpl:BranchNet:Auth:BnUser"] = user;
            HttpContext.Current = context;

            TestController.IsTestCase = true; // always remember to set Controller.IsTestCase = true!!!
            TestClient = new ClientModel();
            // Required Entity Fields: Client.RepID,Client.Entity,Client.ProspectSource,Client.Username,Client.TenantID,Client.ClientInd == 2
            // Required Individual Fields: Client.RepID,Client.LName,Client.FName,Client.ProspectSource,Client.Username,Client.TenantID,Client.ClientInd == 1
            TestClient.RepId = "T2XA";
            TestClient.FName = "Test";
            TestClient.LName = "Client";
            TestClient.ProspectSource = "IFS";
            TestClient.UserName = "MSUnit.Test";
            TestClient.TenantId = 1;
            TestClient.ClientInd = 1;

            TestResponse = TestController.CreateClient(TestClient);

            Assert.AreEqual("success", TestResponse.Status);
        }

        [TestMethod]
        public void Test_CreateClient_Fail_MissingEntityArgs()
        {
            JsonResponse TestResponse = null;
            ClientController TestController = null;
            SimpleWorkerRequest request = null;
            HttpContext context = null;
            BnUserModel user = null;
            ClientModel TestClient = null;

            TestController = new ClientController();
            request = new SimpleWorkerRequest(string.Empty, string.Empty, string.Empty, null, new StringWriter());
            context = new HttpContext(request);
            user = new BnUserModel
            {
                FirmID = 1,
                AccountActive = true,
                UserName = "michael.hatch",
                RepID = "T2XA",
                OfficeID = "T2XA",
                CompanyID = "1",
                ProfileID = 2,
                AccountType = "R"
            };
            context.Items["lpl:BranchNet:Auth:BnUser"] = user;
            HttpContext.Current = context;

            TestController.IsTestCase = true; // always remember to set Controller.IsTestCase = true!!!
            TestClient = new ClientModel();
            // Required Entity Fields: Client.RepID,Client.Entity,Client.ProspectSource,Client.Username,Client.TenantID,Client.ClientInd == 2
            // Required Individual Fields: Client.RepID,Client.LName,Client.FName,Client.ProspectSource,Client.Username,Client.TenantID,Client.ClientInd == 1
            TestClient.RepId = "T2XA";
            //TestClient.Entity = "Test Client Corporation"; -- missing field
            TestClient.ProspectSource = "IFS";
            TestClient.UserName = "MSUnit.Test";
            TestClient.TenantId = 1;
            TestClient.ClientInd = 2;

            TestResponse = TestController.CreateClient(TestClient);

            Assert.AreEqual("fail", TestResponse.Status);
        }

        [TestMethod]
        public void Test_CreateClient_Fail_MissingClientArgs()
        {
            JsonResponse TestResponse = null;
            ClientController TestController = null;
            SimpleWorkerRequest request = null;
            HttpContext context = null;
            BnUserModel user = null;
            ClientModel TestClient = null;

            TestController = new ClientController();
            request = new SimpleWorkerRequest(string.Empty, string.Empty, string.Empty, null, new StringWriter());
            context = new HttpContext(request);
            user = new BnUserModel
            {
                FirmID = 1,
                AccountActive = true,
                UserName = "michael.hatch",
                RepID = "T2XA",
                OfficeID = "T2XA",
                CompanyID = "1",
                ProfileID = 2,
                AccountType = "R"
            };
            context.Items["lpl:BranchNet:Auth:BnUser"] = user;
            HttpContext.Current = context;

            TestController.IsTestCase = true; // always remember to set Controller.IsTestCase = true!!!
            TestClient = new ClientModel();
            // Required Entity Fields: Client.RepID,Client.Entity,Client.ProspectSource,Client.Username,Client.TenantID,Client.ClientInd == 2
            // Required Individual Fields: Client.RepID,Client.LName,Client.FName,Client.ProspectSource,Client.Username,Client.TenantID,Client.ClientInd == 1
            TestClient.RepId = "T2XA";
            //TestClient.FName = "Test";    -- Missing field
            //TestClient.LName = "Client";  -- Missing field
            TestClient.ProspectSource = "IFS";
            TestClient.UserName = "MSUnit.Test";
            TestClient.TenantId = 1;
            TestClient.ClientInd = 1;

            TestResponse = TestController.CreateClient(TestClient);

            Assert.AreEqual("fail", TestResponse.Status);
        }

        [TestMethod]
        public void Test_CreateClient_Fail_BadJSONObject()
        {
            JsonResponse TestResponse = null;
            ClientController TestController = null;
            SimpleWorkerRequest request = null;
            HttpContext context = null;
            BnUserModel user = null;
            ClientModel TestClient = null;

            TestController = new ClientController();
            request = new SimpleWorkerRequest(string.Empty, string.Empty, string.Empty, null, new StringWriter());
            context = new HttpContext(request);
            user = new BnUserModel
            {
                FirmID = 1,
                AccountActive = true,
                UserName = "michael.hatch",
                RepID = "T2XA",
                OfficeID = "T2XA",
                CompanyID = "1",
                ProfileID = 2,
                AccountType = "R"
            };
            context.Items["lpl:BranchNet:Auth:BnUser"] = user;
            HttpContext.Current = context;

            TestController.IsTestCase = true; // always remember to set Controller.IsTestCase = true!!!

            // Send Argument as null to mimic bad JSON (Could not deserialize...)
            TestResponse = TestController.CreateClient(TestClient);

            Assert.AreEqual("fail", TestResponse.Status);
        }

        [TestMethod]
        public void Test_UpdateClient_Success()
        {
            JsonResponse TestResponse = null;
            ClientController TestController = null;
            SimpleWorkerRequest request = null;
            HttpContext context = null;
            BnUserModel user = null;
            ClientModel UpdatedClient = null;

            TestController = new ClientController();
            request = new SimpleWorkerRequest(string.Empty, string.Empty, string.Empty, null, new StringWriter());
            context = new HttpContext(request);
            user = new BnUserModel
            {
                FirmID = 1,
                AccountActive = true,
                UserName = "michael.hatch",
                RepID = "T2XA",
                OfficeID = "T2XA",
                CompanyID = "1",
                ProfileID = 2,
                AccountType = "R"
            };
            context.Items["lpl:BranchNet:Auth:BnUser"] = user;
            HttpContext.Current = context;

            TestController.IsTestCase = true; // always remember to set Controller.IsTestCase = true!!!
            UpdatedClient = new ClientModel();

            // set some properties for the update...
            UpdatedClient.LplClientId = 5342617;

            TestResponse = TestController.UpdateClient(UpdatedClient);

            Assert.AreEqual("success", TestResponse.Status);
        }

        [TestMethod]
        public void Test_UpdateClient_Fail_MissingArgs()
        {
            JsonResponse TestResponse = null;
            ClientController TestController = null;
            SimpleWorkerRequest request = null;
            HttpContext context = null;
            BnUserModel user = null;
            ClientModel UpdatedClient = null;

            TestController = new ClientController();
            request = new SimpleWorkerRequest(string.Empty, string.Empty, string.Empty, null, new StringWriter());
            context = new HttpContext(request);
            user = new BnUserModel
            {
                FirmID = 1,
                AccountActive = true,
                UserName = "michael.hatch",
                RepID = "T2XA",
                OfficeID = "T2XA",
                CompanyID = "1",
                ProfileID = 2,
                AccountType = "R"
            };
            context.Items["lpl:BranchNet:Auth:BnUser"] = user;
            HttpContext.Current = context;

            TestController.IsTestCase = true; // always remember to set Controller.IsTestCase = true!!!
            UpdatedClient = new ClientModel();
            // set properties for update - don't provide ClientID...
            UpdatedClient.LplClientId = -1;

            TestResponse = TestController.UpdateClient(UpdatedClient);

            Assert.AreEqual("fail", TestResponse.Status);
        }

        [TestMethod]
        public void Test_UpdateClient_Fail_BadJSON()
        {
            JsonResponse TestResponse = null;
            ClientController TestController = null;
            SimpleWorkerRequest request = null;
            HttpContext context = null;
            BnUserModel user = null;
            ClientModel UpdatedClient = null;

            TestController = new ClientController();
            request = new SimpleWorkerRequest(string.Empty, string.Empty, string.Empty, null, new StringWriter());
            context = new HttpContext(request);
            user = new BnUserModel
            {
                FirmID = 1,
                AccountActive = true,
                UserName = "michael.hatch",
                RepID = "T2XA",
                OfficeID = "T2XA",
                CompanyID = "1",
                ProfileID = 2,
                AccountType = "R"
            };
            context.Items["lpl:BranchNet:Auth:BnUser"] = user;
            HttpContext.Current = context;

            TestController.IsTestCase = true; // always remember to set Controller.IsTestCase = true!!!

            // Send Argument as null to mimic bad JSON (Could not deserialize...)
            TestResponse = TestController.UpdateClient(UpdatedClient);

            Assert.AreEqual("fail", TestResponse.Status);
        }

        [TestMethod]
        public void Test_UpdateClient_Fail_BadClientID()
        {
            JsonResponse TestResponse = null;
            ClientController TestController = null;
            SimpleWorkerRequest request = null;
            HttpContext context = null;
            BnUserModel user = null;
            ClientModel UpdatedClient = null;

            TestController = new ClientController();
            request = new SimpleWorkerRequest(string.Empty, string.Empty, string.Empty, null, new StringWriter());
            context = new HttpContext(request);
            user = new BnUserModel
            {
                FirmID = 1,
                AccountActive = true,
                UserName = "michael.hatch",
                RepID = "T2XA",
                OfficeID = "T2XA",
                CompanyID = "1",
                ProfileID = 2,
                AccountType = "R"
            };
            context.Items["lpl:BranchNet:Auth:BnUser"] = user;
            HttpContext.Current = context;

            TestController.IsTestCase = true; // always remember to set Controller.IsTestCase = true!!!
            UpdatedClient = new ClientModel();

            // set some properties for the update...
            UpdatedClient.LplClientId = 5342617;

            TestResponse = TestController.UpdateClient(UpdatedClient);

            Assert.AreEqual("success", TestResponse.Status);
        }

        [TestMethod]
        public void Test_AddAccountToClient_Success()
        {
            JsonResponse TestResponse = null;
            ClientController TestController = null;
            SimpleWorkerRequest request = null;
            HttpContext context = null;
            BnUserModel user = null;
            AddAccountToClientModel ClientAccount = null;

            TestController = new ClientController();
            request = new SimpleWorkerRequest(string.Empty, string.Empty, string.Empty, null, new StringWriter());
            context = new HttpContext(request);
            user = new BnUserModel
            {
                FirmID = 1,
                AccountActive = true,
                UserName = "michael.hatch",
                RepID = "T2XA",
                OfficeID = "T2XA",
                CompanyID = "1",
                ProfileID = 2,
                AccountType = "R"
            };
            context.Items["lpl:BranchNet:Auth:BnUser"] = user;
            HttpContext.Current = context;

            TestController.IsTestCase = true; // always remember to set Controller.IsTestCase = true!!!
            ClientAccount = new AddAccountToClientModel();

            // set properties for update - don't provide valid ClientID...
            ClientAccount.LplClientId = 12345678;
            ClientAccount.AccountId = 99999999;
            ClientAccount.TenantId = 1;
            ClientAccount.UserName = "Test.Case";

            TestResponse = TestController.AddAccountToClient(ClientAccount);

            Assert.AreEqual("success", TestResponse.Status);
        }

        [TestMethod]
        public void Test_AddAccountToClient_Fail_InvalidArgs()
        {
            JsonResponse TestResponse = null;
            ClientController TestController = null;
            SimpleWorkerRequest request = null;
            HttpContext context = null;
            BnUserModel user = null;
            AddAccountToClientModel ClientAccount = null;

            TestController = new ClientController();
            request = new SimpleWorkerRequest(string.Empty, string.Empty, string.Empty, null, new StringWriter());
            context = new HttpContext(request);
            user = new BnUserModel
            {
                FirmID = 1,
                AccountActive = true,
                UserName = "michael.hatch",
                RepID = "T2XA",
                OfficeID = "T2XA",
                CompanyID = "1",
                ProfileID = 2,
                AccountType = "R"
            };
            context.Items["lpl:BranchNet:Auth:BnUser"] = user;
            HttpContext.Current = context;

            TestController.IsTestCase = true; // always remember to set Controller.IsTestCase = true!!!
            ClientAccount = new AddAccountToClientModel();

            // set properties for update - don't provide valid ClientID...
            ClientAccount.LplClientId = -1;
            ClientAccount.AccountId = 99999999;
            ClientAccount.TenantId = 1;
            ClientAccount.UserName = "Test.Case";

            TestResponse = TestController.AddAccountToClient(ClientAccount);

            Assert.AreEqual("fail", TestResponse.Status);
        }

        [TestMethod]
        public void Test_AddAccountToClient_Fail_BadClientID()
        {
            JsonResponse TestResponse = null;
            ClientController TestController = null;
            SimpleWorkerRequest request = null;
            HttpContext context = null;
            BnUserModel user = null;
            AddAccountToClientModel ClientAccount = null;

            TestController = new ClientController();
            request = new SimpleWorkerRequest(string.Empty, string.Empty, string.Empty, null, new StringWriter());
            context = new HttpContext(request);
            user = new BnUserModel
            {
                FirmID = 1,
                AccountActive = true,
                UserName = "michael.hatch",
                RepID = "T2XA",
                OfficeID = "T2XA",
                CompanyID = "1",
                ProfileID = 2,
                AccountType = "R"
            };
            context.Items["lpl:BranchNet:Auth:BnUser"] = user;
            HttpContext.Current = context;

            TestController.IsTestCase = true; // always remember to set Controller.IsTestCase = true!!!
            ClientAccount = new AddAccountToClientModel();

            // set properties on ClientAccount, mimic invalid ClientID
            ClientAccount.LplClientId = 99999999; // This value will mimic a bad client ID
            ClientAccount.AccountId = 99999999;
            ClientAccount.TenantId = 1;
            ClientAccount.UserName = "Test.Case";

            TestResponse = TestController.AddAccountToClient(ClientAccount);

            Assert.AreEqual("fail", TestResponse.Status);
        }

        [TestMethod]
        public void Test_DeleteProspect_Success()
        {
            JsonResponse TestResponse = null;
            ClientController TestController = null;
            SimpleWorkerRequest request = null;
            HttpContext context = null;
            BnUserModel user = null;

            TestController = new ClientController();
            request = new SimpleWorkerRequest(string.Empty, string.Empty, string.Empty, null, new StringWriter());
            context = new HttpContext(request);
            user = new BnUserModel
            {
                FirmID = 1,
                AccountActive = true,
                UserName = "michael.hatch",
                RepID = "T2XA",
                OfficeID = "T2XA",
                CompanyID = "1",
                ProfileID = 2,
                AccountType = "R"
            };
            context.Items["lpl:BranchNet:Auth:BnUser"] = user;
            HttpContext.Current = context;

            TestController.IsTestCase = true; // always remember to set Controller.IsTestCase = true!!!

            // No properties to set for Delete
            TestResponse = TestController.DeleteProspect(1234567, 1, "Test.Case");

            Assert.AreEqual("success", TestResponse.Status);
        }

    }
}
