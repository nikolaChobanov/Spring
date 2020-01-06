package com.tutorialspoint.demo.service;

import com.tutorialspoint.demo.model.Robot;
import org.springframework.stereotype.Service;

import java.util.Collection;
@Service
public interface ProductService {
    public abstract void createProduct(Robot robot);
    public abstract void updateProduct(String id, String command);
    public abstract void deleteProduct(String id);
    public abstract Collection<Robot> getProducts();
}