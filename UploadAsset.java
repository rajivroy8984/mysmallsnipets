package com.mysite.core.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.AssetManager;

@Component(service = Servlet.class, property = {
		Constants.SERVICE_DESCRIPTION + "=Upload the image to dam",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/uploadasset" })
public class UploadAsset extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(UploadAsset.class);

	protected void doGet(final SlingHttpServletRequest req,
			final SlingHttpServletResponse resp) throws ServletException, IOException {


		InputStream is = null;
		String mimeType = "";
		try {
			URL Url = new URL("https://www.karrieretag.org/wp-content/uploads/2019/01/diconium-Logo.jpg");
			URLConnection uCon = Url.openConnection();
			is = uCon.getInputStream();
			mimeType = uCon.getContentType();
			String fileExt = StringUtils.EMPTY;

			fileExt = mimeType.replaceAll("image/", "");

			AssetManager assetManager = req.getResourceResolver().adaptTo(AssetManager.class);
			Asset imageAsset = assetManager.createAsset("/content/dam/mysite/test."+fileExt, is, mimeType , true);
			resp.setContentType("text/plain");
			resp.getWriter().write("Image Uploaded = " + imageAsset.getName() +"  to this path ="+ imageAsset.getPath());

		}catch (Exception e) {
			log.error("error  occured while uploading the asset {}",e.getMessage());
		}finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				log.error("error  occured {}",e.getMessage());
			}
		}
	}


}
