using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ICA_LMS_WebSite
{
    /// <summary>
    /// Summary description for Downloader
    /// </summary>
    public class Downloader : IHttpHandler
    {

        public void ProcessRequest(HttpContext context)
        {
            //context.Response.ContentType = "text/plain";
            //context.Response.Write("Hello World");
            string file = "";

            // get the file name from the querystring
            if (context.Request.QueryString["FilePath"] != null)
            {
                file = context.Request.QueryString["FilePath"].ToString();
            }

            string filename = context.Server.MapPath(file);
            System.IO.FileInfo fileInfo = new System.IO.FileInfo(filename);

            try
            {
                if (fileInfo.Exists)
                {
                    context.Response.Clear();
                    context.Response.AddHeader("Content-Disposition", "attachment;filename=\"" + fileInfo.Name + "\"");
                    context.Response.AddHeader("Content-Length", fileInfo.Length.ToString());
                    context.Response.ContentType = "application/octet-stream";
                    context.Response.TransmitFile(fileInfo.FullName);
                    context.Response.Flush();
                }
                else
                {
                    //throw new Exception("File not found");
                    context.Response.ContentType = "text/plain";
                    context.Response.Write("File not found");
                }
            }
            catch (Exception ex)
            {
                context.Response.ContentType = "text/plain";
                context.Response.Write(ex.Message);
            }
            finally
            {
                context.Response.End();
            }
        }

        public bool IsReusable
        {
            get
            {
                return false;
            }
        }
    }
}