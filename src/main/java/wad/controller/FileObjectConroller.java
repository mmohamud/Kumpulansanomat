/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import wad.repository.FileObjectRepository;
import wad.repository.NewsRepository;

/**
 *
 * @author mmohamud
 */
@Controller
@Transactional
public class FileObjectConroller {

    @Autowired
    private FileObjectRepository fileObjectRepository;
    @Autowired
    private NewsRepository newsRepository;

    @GetMapping(path = "/images/{id}", produces = "image/jpeg")
    @ResponseBody
    public byte[] kuvaSisalto(@PathVariable Long id) {
        return fileObjectRepository.findByuutiset(newsRepository.getOne(id)).getSisalto();
    }
}
