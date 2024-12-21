package com.API.controller.test;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.API.controller.HomeController;
import com.API.model.Cart;
import com.API.model.LiveSession;
import com.API.model.ProductDetail;
import com.API.model.ProductImage;
import com.API.model.VoucherSan;
import com.API.model.tests;
import com.API.repository.BaoCaoRepository;
import com.API.repository.CartRepository;
import com.API.repository.DiaChiRepository;
import com.API.repository.DonHangRepository;
import com.API.repository.LiveDetail;
import com.API.repository.LiveSessionRepository;
import com.API.repository.OTPRepository;
import com.API.repository.ProductDetailRepository;
import com.API.repository.SanPhamYeuThichRepository;
import com.API.repository.TestRepo;
import com.API.repository.ThanhToanRepository;
import com.API.repository.ViPhamRepository;
import com.API.repository.VoucherSanNguoiDungRepository;
import com.API.repository.VoucherSanRepository;
import com.API.repository.VoucherShopRepository;
import com.API.service.HomeService;
import com.API.service.TestService;
import com.API.utils.ObjectRespone;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import io.jsonwebtoken.io.IOException;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.transaction.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.Console;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@CrossOrigin("*")
public class TestController {

	@Autowired
	TestRepo t;
	@Autowired
	LiveSessionRepository k;
	
	@Autowired
	HomeService homController;
	
	
	@Autowired
	LiveDetail ld;
	
	@Autowired
	BaoCaoRepository sanPhamYeuThichRepository;
	
	@GetMapping("/testopt")
	public Object getMethodName( ) {
		LiveSession l=k.findById(1).get();
		
		return ld.findByLiveId(1);
	}
	
	
	@GetMapping("/teh")
	public Object getMethodNames() {
		return sanPhamYeuThichRepository.findAll();
	}
	
	
	
	@Autowired
    private Cloudinary cloudinary;

    @PostMapping("/upload-multiple-files")
    public ObjectRespone uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) throws java.io.IOException {
        List<ProductImage>productImages=new ArrayList<ProductImage>();
        for (MultipartFile file : files) {
            try {
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                productImages.add(new ProductImage(uploadResult.get("url").toString()));
//                String url = uploadResult.get("url").toString();
            } catch (IOException e) {
                return new ObjectRespone(500,"failes",null);
            }
        }

        return new ObjectRespone(200,"success",productImages);
    }
    
    
    @PostMapping("/upload-single-files")
    public ObjectRespone uploadSingleFile(@RequestParam("files") MultipartFile file) throws java.io.IOException {
    		String url;
            try {
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                // Lấy URL từ kết quả trả về
                 url = uploadResult.get("url").toString();
                new ObjectRespone(200,"success",url);
            } catch (Exception e) {
            	e.printStackTrace();
                return new ObjectRespone(500,"failes",null);
            }
        return new ObjectRespone(200,"success",url);
    }
    
    @PostMapping("/upload-single-files-video")
    public ObjectRespone uploadSingleFileVideo(@RequestParam("files") MultipartFile file) throws java.io.IOException {
        String url;
        try {
            // Kiểm tra nếu tệp rỗng
            if (file.isEmpty()) {
                return new ObjectRespone(400, "File is empty", null);
            }

            // Upload file lên Cloudinary với resource_type là video
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), 
                ObjectUtils.asMap("resource_type", "video")); // Chỉ định resource_type là video

            // Lấy URL từ kết quả trả về
            url = uploadResult.get("url").toString();
            return new ObjectRespone(200, "success", url); // Trả về URL
        } catch (Exception e) {
            e.printStackTrace();
            return new ObjectRespone(500, "failed", null);
        }
    }
	


    
	
}