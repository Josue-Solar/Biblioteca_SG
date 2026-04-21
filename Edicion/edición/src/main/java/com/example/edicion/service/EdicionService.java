package com.example.edicion.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.edicion.repository.EdicionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EdicionService {
    @Autowired
    private EdicionRepository edicionRepository;

    

}
