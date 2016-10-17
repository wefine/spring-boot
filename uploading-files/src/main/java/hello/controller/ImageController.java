package hello.controller;

import hello.model.Image;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Controller
public class ImageController {
    private static final Logger log = LoggerFactory.getLogger(ImageController.class);

    @Value("${user.home}")
    private String userHome;

    @GetMapping("/images")
    public String images() {
        return "images";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public
    @ResponseBody
    Map list() {
        log.debug("uploadGet called");
//        List<Image> list = imageDao.list();
        List<Image> list = new ArrayList<>();
        for (Image image : list) {
            image.setUrl("/picture/" + image.getId());
            image.setThumbnailUrl("/thumbnail/" + image.getId());
            image.setDeleteUrl("/delete/" + image.getId());
            image.setDeleteType("DELETE");
        }
        Map<String, Object> files = new HashMap<>();
        files.put("files", list);
        log.debug("Returning: {}", files);
        return files;
    }

    @GetMapping("/upload")
    @ResponseBody
    public Map upload(MultipartHttpServletRequest request, HttpServletResponse response) {
        log.debug("uploadPost called");
        Iterator<String> itr = request.getFileNames();
        MultipartFile mpf;
        List<Image> list = new LinkedList<>();

        while (itr.hasNext()) {
            mpf = request.getFile(itr.next());
            log.debug("Uploading {}", mpf.getOriginalFilename());

            String newFilenameBase = UUID.randomUUID().toString();
            String originalFileExtension = mpf.getOriginalFilename().substring(mpf.getOriginalFilename().lastIndexOf("."));
            String newFilename = newFilenameBase + originalFileExtension;
            String storageDirectory = userHome + "/uploaded";
            String contentType = mpf.getContentType();

            File newFile = new File(storageDirectory + "/" + newFilename);
            try {
                mpf.transferTo(newFile);

                BufferedImage thumbnail = Scalr.resize(ImageIO.read(newFile), 290);
                String thumbnailFilename = newFilenameBase + "-thumbnail.png";
                File thumbnailFile = new File(storageDirectory + "/" + thumbnailFilename);
                ImageIO.write(thumbnail, "png", thumbnailFile);

                Image image = new Image();
                image.setName(mpf.getOriginalFilename());
                image.setThumbnailFilename(thumbnailFilename);
                image.setNewFilename(newFilename);
                image.setContentType(contentType);
                image.setSize(mpf.getSize());
                image.setThumbnailSize(thumbnailFile.length());
//                image = imageDao.create(image);

                image.setUrl("/picture/" + image.getId());
                image.setThumbnailUrl("/thumbnail/" + image.getId());
                image.setDeleteUrl("/delete/" + image.getId());
                image.setDeleteType("DELETE");

                list.add(image);

            } catch (IOException e) {
                log.error("Could not upload file " + mpf.getOriginalFilename(), e);
            }

        }

        Map<String, Object> files = new HashMap<>();
        files.put("files", list);
        return files;
    }

    @RequestMapping(value = "/picture/{id}", method = RequestMethod.GET)
    public void picture(HttpServletResponse response, @PathVariable Long id) {
//        Image image = imageDao.get(id);
        Image image = null;
        File imageFile = new File(userHome +  "/uploaded/" + image.getNewFilename());
        response.setContentType(image.getContentType());
        response.setContentLength(image.getSize().intValue());
        try {

            InputStream is = new FileInputStream(imageFile);
            FileCopyUtils.copy(is, response.getOutputStream());
        } catch (IOException e) {
            log.error("Could not show picture " + id, e);
        }
    }

    @RequestMapping(value = "/thumbnail/{id}", method = RequestMethod.GET)
    public void thumbnail(HttpServletResponse response, @PathVariable Long id) {
//        Image image = imageDao.get(id);
        Image image = null;
        File imageFile = new File(userHome + "/uploaded/" + image.getThumbnailFilename());
        response.setContentType(image.getContentType());
        response.setContentLength(image.getThumbnailSize().intValue());
        try {
            InputStream is = new FileInputStream(imageFile);
            FileCopyUtils.copy(is, response.getOutputStream());
        } catch (IOException e) {
            log.error("Could not show thumbnail " + id, e);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    List delete(@PathVariable Long id) {
//        Image image = imageDao.get(id);
//        File imageFile = new File(userHome+"/"+image.getNewFilename());
//        imageFile.delete();
//        File thumbnailFile = new File(userHome+"/"+image.getThumbnailFilename());
//        thumbnailFile.delete();
//        imageDao.delete(image);
        List<Map<String, Object>> results = new ArrayList<>();
        Map<String, Object> success = new HashMap<>();
        success.put("success", true);
        results.add(success);
        return results;
    }
}
