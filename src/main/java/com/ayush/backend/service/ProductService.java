package com.ayush.backend.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ayush.backend.model.Product;
import com.ayush.backend.repo.ProductRepository;

@Service
public class ProductService {

    @Autowired
    ProductRepository repo;

    public List<Product> getAllProducts(){
        return repo.findAll();
    }

    public Product getProduct(int id) {
        return repo.findById(id).orElse(null);
    }

    public Product addProduct(Product p, MultipartFile imageFile) throws IOException {
        p.setImageName(imageFile.getOriginalFilename());
        p.setImageType(imageFile.getContentType());
        p.setImage(imageFile.getBytes());
        return repo.save(p);
    }

    public Product updateProduct(int id, Product p, MultipartFile imageFile) throws IOException {
       if (imageFile != null && !imageFile.isEmpty()) {
            p.setImage(imageFile.getBytes());
            p.setImageName(imageFile.getOriginalFilename());
            p.setImageType(imageFile.getContentType());
      }
       return repo.save(p);
    }

    public void deleteProduct(int id) {
        repo.deleteById(id);        
    }

    public List<Product> searchProducts(String keyword){
        return repo.searchProduct(keyword);
    }
    
}
