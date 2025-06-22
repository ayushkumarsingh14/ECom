package com.ayush.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ayush.backend.model.Product;
import com.ayush.backend.service.ProductService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
class ProductController{
    
    @Autowired
    ProductService service;

    @GetMapping("/product")
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.ok(service.getAllProducts());
    }
    
    @GetMapping("/product/{id}")
public ResponseEntity<Product> getProduct(@PathVariable int id) {
    Product p = service.getProduct(id);
    if (p != null) {
        return ResponseEntity.ok(p);
    } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}



    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product p, @RequestPart MultipartFile imageFile){
        try{ 
           Product prod = service.addProduct(p,imageFile);
           return new ResponseEntity<>(prod, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

@PutMapping("/product/{id}")
public ResponseEntity<String> updateProduct(@PathVariable int id,
                                            @RequestPart("p") Product product,
                                            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
    try {
        Product updated = service.updateProduct(id, product, imageFile);
        if (updated != null) {
            return ResponseEntity.ok("Updated Successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update: " + e.getMessage());
    }
}


   @DeleteMapping("/product/{id}")
   public ResponseEntity<String> deleteProduct(@PathVariable int id){
    Product p = service.getProduct(id);
    if (p != null) {
        service.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted successfuly");
    }else{
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete");
    }
   }

   @GetMapping("/product/{id}/image")
   public ResponseEntity<byte []> getImageByProductId(@PathVariable int id){
    Product p = service.getProduct(id);
    if (p == null || p.getImage() == null) {
        return ResponseEntity.badRequest().build();
    }else {
        return ResponseEntity.ok()
               .contentType(MediaType.valueOf(p.getImageType()))
               .body(p.getImage());
    }
   }

   @GetMapping("/product/search")
   public ResponseEntity<List<Product>> searchProduct(@RequestParam String keyword){
    List<Product> p = service.searchProducts(keyword);
    return new ResponseEntity<>(p, HttpStatus.OK);
   }


}