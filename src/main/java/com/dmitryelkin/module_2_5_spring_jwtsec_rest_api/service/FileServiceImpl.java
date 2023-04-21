package com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.service;

import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.File;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.model.Status;
import com.dmitryelkin.module_2_5_spring_jwtsec_rest_api.repository.FileRepositoryI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FileServiceImpl implements FileServiceI {
    private final FileRepositoryI repository;

    @Autowired
    public FileServiceImpl(FileRepositoryI service) {
        this.repository = service;
    }

    @Override
    public File create(File item) {
        return repository.saveAndFlush(item);
    }

    @Override
    public File update(File item) {
        return repository.saveAndFlush(item);
    }

    @Override
    public File delete(long id) {
        File item = repository.findById(id).orElse(null);
        if (item!=null){
            item.setStatus(Status.DELETED);
            repository.saveAndFlush(item);
        }
        return item;
   }

    @Override
    public List<File> getAll() {
        return repository.findAll();
    }

    @Override
    public File getById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public File getByName(String name) {
        return repository.findByName(name).orElse(null);
    }
}
