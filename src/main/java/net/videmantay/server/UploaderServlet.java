package net.videmantay.server;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;


@SuppressWarnings("serial")
public class UploaderServlet extends HttpServlet{
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)throws IOException, ServletException{
		
		BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
		
		String url = 	bs.createUploadUrl("/app/updateappuser");
		res.setContentType("text/plain");
		res.getWriter().write(url);
		
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException{
		BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
		Map<String, List<BlobKey>> blobs ;
		blobs =bs.getUploads(req);
		
		BlobKey bKey= blobs.get("uploadPic").get(0);
		ImagesService is = ImagesServiceFactory.getImagesService();
		String picUrl = is.getServingUrl(ServingUrlOptions.Builder.withBlobKey(bKey));
		res.setContentType("text/plain");
		res.getWriter().write(picUrl);
	}

}
