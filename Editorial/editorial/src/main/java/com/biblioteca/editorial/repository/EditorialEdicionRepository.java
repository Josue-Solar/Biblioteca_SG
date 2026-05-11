package com.biblioteca.editorial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.biblioteca.editorial.model.EditorialEdicion;
import com.biblioteca.editorial.model.EditorialEdicionID;

@Repository
public interface EditorialEdicionRepository extends JpaRepository<EditorialEdicion, EditorialEdicionID>{
    List<EditorialEdicion> findAllByEditorialId(Long editorialId);
    List<EditorialEdicion> findAllByEdicionId(Long edicionId);
    EditorialEdicion findByEditorialId(Long edicionId);
}
